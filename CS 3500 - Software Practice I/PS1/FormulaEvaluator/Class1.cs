using System;
using System.Collections;
using System.Collections.Generic;
using System.Text.RegularExpressions;

namespace FormulaEvaluator
{

    public static class Evaluator
    {
        /// <summary>
        /// The lookup delegate that, when given the name of a variable that begins with one or more letters and ends with one or more numbers, 
        /// returns the integer value associated with the variable. 
        /// If the name is not found or incorrectly formatted, throws an ArgumentException.
        /// </summary>
        /// <param name="v"></param>
        /// <returns></returns>
        public delegate int Lookup(String v);

        /// <summary>
        /// The method used to evaluate a given expression, 
        /// given the expression and the lookup delegate used to find the value of any given variables in the expression. 
        /// If any errors are encountered, throws an ArgumentException.
        /// </summary>
        /// <param name="exp"></param>
        /// <param name="variableEvaluator"></param>
        /// <returns></returns>
        public static int Evaluate(String exp, Lookup variableEvaluator)
        {
            Stack<string> operatorStack = new Stack<string>();
            Stack<int> valueStack = new Stack<int>();

            exp = exp.Trim();
            string[] substrings = Regex.Split(exp, "(\\()|(\\))|(-)|(\\+)|(\\*)|(/)");

            foreach (String s in substrings)
            {
                // Takes off any leading or trailing whitespace from the given string
                string sTrim = s.Trim();
                if (sTrim.Equals(""))
                {

                }
                //If the token is an integer
                else if (int.TryParse(sTrim, out int result))
                {
                    // If a * operator is on the stack
                    if (stackCheckOperator(operatorStack, "*"))
                    {
                        // If the value stack is empty, throws an error
                        if (valueStack.Count == 0)
                        {
                            throw new ArgumentException("Value stack is empty!");
                        }
                        operatorStack.Pop();
                        int value = valueStack.Pop();
                        result = value * result;
                        valueStack.Push(result);
                    }

                    // If a / operator is on the stack
                    else if (stackCheckOperator(operatorStack, "/"))
                    {
                        // If a division by 0 will occur, throws an error
                        if (result == 0)
                        {
                            throw new ArgumentException("Division by 0 occured!");
                        }
                        // If the value stack is empty, throws an error
                        if (valueStack.Count == 0)
                        {
                            throw new ArgumentException("Value stack is empty!");
                        }
                        operatorStack.Pop();
                        int value = valueStack.Pop();
                        result = value / result;
                        valueStack.Push(result);
                    }

                    // If not, just push onto the value stack
                    else
                    {
                        valueStack.Push(result);
                    }
                }

                // If the token is multiplication or division
                else if (sTrim.Equals("*") || sTrim.Equals("/"))
                {
                    operatorStack.Push(s);
                }

                // If the token is a left parenthesis
                else if (sTrim.Equals("("))
                {
                    operatorStack.Push(s);
                }

                // If the token is a plus sign or minus sign
                else if (sTrim.Equals("+") || sTrim.Equals("-"))
                {
                    if (stackCheckOperator(operatorStack, "+"))
                    {
                        if (valueStack.Count < 2)
                        {
                            throw new ArgumentException("Not enough values in stack!");
                        }
                        operatorStack.Pop();
                        int value1 = valueStack.Pop();
                        int value2 = valueStack.Pop();
                        result = value1 + value2;
                        valueStack.Push(result);
                    }

                    else if (stackCheckOperator(operatorStack, "-"))
                    {
                        if (valueStack.Count < 2)
                        {
                            throw new ArgumentException("Not enough values in stack!");
                        }
                        operatorStack.Pop();
                        int value1 = valueStack.Pop();
                        int value2 = valueStack.Pop();
                        result = value2 - value1;
                        valueStack.Push(result);
                    }
                    operatorStack.Push(s);
                }

                // If the token is a right parenthesis
                else if (sTrim.Equals(")"))
                {
                    if (stackCheckOperator(operatorStack, "+"))
                    {
                        operatorStack.Pop();
                        int value1 = valueStack.Pop();
                        int value2 = valueStack.Pop();
                        result = value1 + value2;
                        valueStack.Push(result);
                    }

                    else if (stackCheckOperator(operatorStack, "-"))
                    {
                        operatorStack.Pop();
                        int value1 = valueStack.Pop();
                        int value2 = valueStack.Pop();
                        result = value2 - value1;
                        valueStack.Push(result);
                    }

                    if (!stackCheckOperator(operatorStack, "("))
                    {
                        throw new ArgumentException("Missing parenthesis!");
                    }

                    else
                    {
                        operatorStack.Pop();
                    }

                    // If a * operator is on the stack
                    if (stackCheckOperator(operatorStack, "*"))
                    {
                        // If the value stack is empty, throws an error
                        if (valueStack.Count == 0)
                        {
                            throw new ArgumentException("Value stack is empty!");
                        }
                        operatorStack.Pop();
                        int value1 = valueStack.Pop();
                        int value2 = valueStack.Pop();
                        result = value1 * value2;
                        valueStack.Push(result);
                    }

                    // If a / operator is on the stack
                    else if (stackCheckOperator(operatorStack, "/"))
                    {
                        // If a division by 0 will occur, throws an error
                        if (valueStack.Peek() == 0)
                        {
                            throw new ArgumentException("Division by 0 occured!");
                        }
                        // If the value stack is empty, throws an error
                        if (valueStack.Count == 0)
                        {
                            throw new ArgumentException("Value stack is empty!");
                        }
                        operatorStack.Pop();
                        int value1 = valueStack.Pop();
                        int value2 = valueStack.Pop();
                        result = value2 / value1;
                        valueStack.Push(result);
                    }
                }

                // If the token is a variable
                else if (char.IsLetter(sTrim[0]) && char.IsDigit(sTrim[sTrim.Length - 1]))
                {
                    // Uses the delegate to get the value of the variable
                    int valueInt = variableEvaluator.Invoke(sTrim);

                    // If a * operator is on the stack
                    if (stackCheckOperator(operatorStack, "*"))
                    {
                        // If the value stack is empty, throws an error
                        if (valueStack.Count == 0)
                        {
                            throw new ArgumentException("Value stack is empty!");
                        }
                        operatorStack.Pop();
                        int value = valueStack.Pop();
                        result = value * valueInt;
                        valueStack.Push(result);
                    }

                    // If a / operator is on the stack
                    else if (stackCheckOperator(operatorStack, "/"))
                    {
                        // If a division by 0 will occur, throws an error
                        if (valueInt == 0)
                        {
                            throw new ArgumentException("Division by 0 occured!");
                        }
                        // If the value stack is empty, throws an error
                        if (valueStack.Count == 0)
                        {
                            throw new ArgumentException("Value stack is empty!");
                        }
                        operatorStack.Pop();
                        int value = valueStack.Pop();
                        result = value / valueInt;
                        valueStack.Push(result);
                    }

                    // If not, just push onto the value stack
                    else
                    {
                        valueStack.Push(valueInt);
                    }
                }
            }

            // If there is no operator left after the last token
            if (operatorStack.Count == 0)
            {
                if (valueStack.Count != 1)
                {
                    throw new ArgumentException("Extra value in stack!");
                }

                else
                {
                    return valueStack.Pop();
                }
            }

            // If there is one operator after the last token
            else if (operatorStack.Count == 1)
            {
                if (valueStack.Count != 2)
                {
                    throw new ArgumentException("Extra value in stack!");
                }

                else
                {
                    int value1 = valueStack.Pop();
                    int value2 = valueStack.Pop();

                    if (stackCheckOperator(operatorStack, "+"))
                    {
                        operatorStack.Pop();
                        return value1 + value2;
                    }

                    else if (stackCheckOperator(operatorStack, "-"))
                    {
                        operatorStack.Pop();
                        return value2 - value1;
                    }
                }
            }

            else
            {
                throw new ArgumentException("Extra operator in stack!");
            }
            return 0;
        }

        /// <summary>
        /// A method to check the operator stack for the desired operator.
        /// </summary>
        /// <param name="stackToCheck"></param>
        /// <param name="stringToCheck"></param>
        /// <returns></returns>
        public static bool stackCheckOperator(Stack<string> stackToCheck, string stringToCheck)
        {
            if (stackToCheck.Count > 0 && stackToCheck.Peek().Equals(stringToCheck))
            {
                return true;
            }

            else
            {
                return false;
            }
        }
    }
}
