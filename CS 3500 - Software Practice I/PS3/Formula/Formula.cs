// Skeleton written by Joe Zachary for CS 3500, September 2013
// Finished by Brandon Walters, 9/13/19
// Read the entire skeleton carefully and completely before you
// do anything else!

// Version 1.1 (9/22/13 11:45 a.m.)

// Change log:
//  (Version 1.1) Repaired mistake in GetTokens
//  (Version 1.1) Changed specification of second constructor to
//                clarify description of how validation works

// (Daniel Kopta) 
// Version 1.2 (9/10/17) 

// Change log:
//  (Version 1.2) Changed the definition of equality with regards
//                to numeric tokens


using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;

namespace SpreadsheetUtilities
{
    /// <summary>
    /// Represents formulas written in standard infix notation using standard precedence
    /// rules.  The allowed symbols are non-negative numbers written using double-precision 
    /// floating-point syntax (without unary preceeding '-' or '+'); 
    /// variables that consist of a letter or underscore followed by 
    /// zero or more letters, underscores, or digits; parentheses; and the four operator 
    /// symbols +, -, *, and /.  
    /// 
    /// Spaces are significant only insofar that they delimit tokens.  For example, "xy" is
    /// a single variable, "x y" consists of two variables "x" and y; "x23" is a single variable; 
    /// and "x 23" consists of a variable "x" and a number "23".
    /// 
    /// Associated with every formula are two delegates:  a normalizer and a validator.  The
    /// normalizer is used to convert variables into a canonical form, and the validator is used
    /// to add extra restrictions on the validity of a variable (beyond the standard requirement 
    /// that it consist of a letter or underscore followed by zero or more letters, underscores,
    /// or digits.)  Their use is described in detail in the constructor and method comments.
    /// </summary>
    public class Formula
    {
        // Used to store the initial variables given by the formula, as well as their normalized version
        private Dictionary<string, string> variablesList = new Dictionary<string, string>();

        // Used to iterate through each of the tokens in the formula
        private List<string> tokens = new List<string>();
        /// <summary>
        /// Creates a Formula from a string that consists of an infix expression written as
        /// described in the class comment.  If the expression is syntactically invalid,
        /// throws a FormulaFormatException with an explanatory Message.
        /// 
        /// The associated normalizer is the identity function, and the associated validator
        /// maps every string to true.  
        /// </summary>
        public Formula(String formula) :
            this(formula, s => s, s => true)
        {

        }

        /// <summary>
        /// Creates a Formula from a string that consists of an infix expression written as
        /// described in the class comment.  If the expression is syntactically incorrect,
        /// throws a FormulaFormatException with an explanatory Message.
        /// 
        /// The associated normalizer and validator are the second and third parameters,
        /// respectively.  
        /// 
        /// If the formula contains a variable v such that normalize(v) is not a legal variable, 
        /// throws a FormulaFormatException with an explanatory message. 
        /// 
        /// If the formula contains a variable v such that isValid(normalize(v)) is false,
        /// throws a FormulaFormatException with an explanatory message.
        /// 
        /// Suppose that N is a method that converts all the letters in a string to upper case, and
        /// that V is a method that returns true only if a string consists of one letter followed
        /// by one digit.  Then:
        /// 
        /// new Formula("x2+y3", N, V) should succeed
        /// new Formula("x+y3", N, V) should throw an exception, since V(N("x")) is false
        /// new Formula("2x+y3", N, V) should throw an exception, since "2x+y3" is syntactically incorrect.
        /// </summary>
        public Formula(String formula, Func<string, string> normalize, Func<string, bool> isValid)
        {
            // If the formula received is null
            if (formula == null)
            {
                string formulaString = "";
                tokens = GetTokens(formulaString).ToList();
            }

            // If the formula received is not null
            else
            {
                tokens = GetTokens(formula).ToList();
            }

            // Used to keep track of the amount of left and right parentheses seen
            int leftParCount = 0;
            int rightParCount = 0;

            // Count of how many tokens have been passed through
            int tokensCount = 0;

            // The token before the current token
            string lastToken = "";

            // If the formula was empty but not null
            if (tokens.Count() == 0 && formula != null)
            {
                throw new FormulaFormatException("Formula was empty. Try adding something!");
            }

            // For each token...
            foreach (string s in tokens)
            {
                // If the first token is an invalid token for the first position, such as an operator
                if (tokensCount.Equals(0))
                {
                    if (!checkToken(s, 0))
                    {
                        throw new FormulaFormatException("Beginning of formula is invalid! Check your first token!");
                    }
                }

                // If the last token is an invalid token for the last position, such as an operator
                if (tokensCount.Equals(tokens.Count() - 1))
                {
                    if (!checkToken(s, tokens.Count() - 1))
                    {
                        throw new FormulaFormatException("Ending of formula is invalid! Check your last token!");
                    }
                }

                // If not the first token and the last token is not compatible with the scanned token, such as two operators next to each other
                if (tokensCount > 0 && !lastTokenBool(s, lastToken))
                {
                    throw new FormulaFormatException("The last token was not compatible with this one! Check your ordering and variables!");
                }

                // If the given token is a variable
                if (isValidVariable(s))
                {
                    // Normalize the variable
                    string sNormalized;
                    sNormalized = normalize(s);

                    // If the normalized variable is not valid
                    if (!isValid(sNormalized))
                    {
                        throw new FormulaFormatException("Normalized variable did not exist! Check your variables!");
                    }

                    // If the variable is not already in the list of variables, add it
                    if (!variablesList.ContainsKey(s))
                    {
                        variablesList.Add(s, sNormalized);
                    }
                }

                // If the token is a left parenthesis
                if (s.Equals("("))
                {
                    leftParCount++;
                }

                // If the token is a right parenthesis
                if (s.Equals(")"))
                {
                    rightParCount++;
                }

                // If there are more closing parentheses than open parentheses
                if (rightParCount > leftParCount)
                {
                    throw new FormulaFormatException("There were more closing parentheses than opening parentheses! Check your parentheses!");
                }

                lastToken = s;
                tokensCount++;
            }

            // If the total count of parentheses are not equal
            if (rightParCount != leftParCount)
            {
                throw new FormulaFormatException("The number of parentheses did not match! Check your parentheses!");
            }
        }

        /// <summary>
        /// Evaluates this Formula, using the lookup delegate to determine the values of
        /// variables.  When a variable symbol v needs to be determined, it should be looked up
        /// via lookup(normalize(v)). (Here, normalize is the normalizer that was passed to 
        /// the constructor.)
        /// 
        /// For example, if L("x") is 2, L("X") is 4, and N is a method that converts all the letters 
        /// in a string to upper case:
        /// 
        /// new Formula("x+7", N, s => true).Evaluate(L) is 11
        /// new Formula("x+7").Evaluate(L) is 9
        /// 
        /// Given a variable symbol as its parameter, lookup returns the variable's value 
        /// (if it has one) or throws an ArgumentException (otherwise).
        /// 
        /// If no undefined variables or divisions by zero are encountered when evaluating 
        /// this Formula, the value is returned.  Otherwise, a FormulaError is returned.  
        /// The Reason property of the FormulaError should have a meaningful explanation.
        ///
        /// This method should never throw an exception.
        /// </summary>
        public object Evaluate(Func<string, double> lookup)
        {
            Stack<string> operatorStack = new Stack<string>();
            Stack<double> valueStack = new Stack<double>();

            IEnumerable<string> formulaString = tokens;

            foreach (String s in formulaString)
            {
                // Takes off any leading or trailing whitespace from the given string
                string sTrim = s.Trim();
                if (sTrim.Equals(""))
                {

                }
                //If the token is an integer
                else if (Double.TryParse(sTrim, out double result))
                {
                    // If a * operator is on the stack
                    if (stackCheckOperator(operatorStack, "*"))
                    {
                        operatorStack.Pop();
                        double value = valueStack.Pop();
                        result = value * result;
                        valueStack.Push(result);
                    }

                    // If a / operator is on the stack
                    else if (stackCheckOperator(operatorStack, "/"))
                    {
                        // If a division by 0 will occur, throws an error
                        if (result == 0)
                        {
                            FormulaError error = new FormulaError("Division by 0 occured!");
                            return error;
                        }
                        operatorStack.Pop();
                        double value = valueStack.Pop();
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
                        operatorStack.Pop();
                        double value1 = valueStack.Pop();
                        double value2 = valueStack.Pop();
                        result = value1 + value2;
                        valueStack.Push(result);
                    }

                    else if (stackCheckOperator(operatorStack, "-"))
                    {
                        operatorStack.Pop();
                        double value1 = valueStack.Pop();
                        double value2 = valueStack.Pop();
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
                        double value1 = valueStack.Pop();
                        double value2 = valueStack.Pop();
                        result = value1 + value2;
                        valueStack.Push(result);
                    }

                    else if (stackCheckOperator(operatorStack, "-"))
                    {
                        operatorStack.Pop();
                        double value1 = valueStack.Pop();
                        double value2 = valueStack.Pop();
                        result = value2 - value1;
                        valueStack.Push(result);
                    }

                    operatorStack.Pop();

                    // If a * operator is on the stack
                    if (stackCheckOperator(operatorStack, "*"))
                    {
                        operatorStack.Pop();
                        double value1 = valueStack.Pop();
                        double value2 = valueStack.Pop();
                        result = value1 * value2;
                        valueStack.Push(result);
                    }

                    // If a / operator is on the stack
                    else if (stackCheckOperator(operatorStack, "/"))
                    {
                        // If a division by 0 will occur, throws an error
                        if (valueStack.Peek() == 0)
                        {
                            FormulaError error = new FormulaError("Division by 0 occured!");
                            return error;
                        }
                        operatorStack.Pop();
                        double value1 = valueStack.Pop();
                        double value2 = valueStack.Pop();
                        result = value2 / value1;
                        valueStack.Push(result);
                    }
                }

                // If the token is a variable
                else if (isValidVariable(sTrim))
                {
                    // Replaces the original variable with the normalized version
                    variablesList.TryGetValue(sTrim, out string sNormalized);

                    double valueInt;
                    // Uses the delegate to get the value of the variable
                    try
                    {
                        valueInt = lookup.Invoke(sNormalized);
                    }

                    catch
                    {
                        FormulaError error = new FormulaError("Undefined variable was given!");
                        return error;
                    }

                    // If a * operator is on the stack
                    if (stackCheckOperator(operatorStack, "*"))
                    {
                        operatorStack.Pop();
                        double value = valueStack.Pop();
                        result = value * valueInt;
                        valueStack.Push(result);
                    }

                    // If a / operator is on the stack
                    else if (stackCheckOperator(operatorStack, "/"))
                    {
                        // If a division by 0 will occur, throws an error
                        if (valueInt == 0)
                        {
                            FormulaError error = new FormulaError("Division by 0 occured!");
                            return error;
                        }
                        operatorStack.Pop();
                        double value = valueStack.Pop();
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
                return valueStack.Pop();
            }

            // If there is one operator after the last token
            else
            {
                double value1 = valueStack.Pop();
                double value2 = valueStack.Pop();

                if (stackCheckOperator(operatorStack, "+"))
                {
                    operatorStack.Pop();
                    return value1 + value2;
                }

                else
                {
                    operatorStack.Pop();
                    return value2 - value1;
                }
            }
        }

        /// <summary>
        /// Enumerates the normalized versions of all of the variables that occur in this 
        /// formula.  No normalization may appear more than once in the enumeration, even 
        /// if it appears more than once in this Formula.
        /// 
        /// For example, if N is a method that converts all the letters in a string to upper case:
        /// 
        /// new Formula("x+y*z", N, s => true).GetVariables() should enumerate "X", "Y", and "Z"
        /// new Formula("x+X*z", N, s => true).GetVariables() should enumerate "X" and "Z".
        /// new Formula("x+X*z").GetVariables() should enumerate "x", "X", and "z".
        /// </summary>
        public IEnumerable<String> GetVariables()
        {
            return variablesList.Values.ToList();
        }

        /// <summary>
        /// Returns a string containing no spaces which, if passed to the Formula
        /// constructor, will produce a Formula f such that this.Equals(f).  All of the
        /// variables in the string should be normalized.
        /// 
        /// For example, if N is a method that converts all the letters in a string to upper case:
        /// 
        /// new Formula("x + y", N, s => true).ToString() should return "X+Y"
        /// new Formula("x + Y").ToString() should return "x+Y"
        /// </summary>
        public override string ToString()
        {
            string formulaString = "";
            foreach (String s in tokens)
            {
                if (isValidVariable(s))
                {
                    variablesList.TryGetValue(s, out string sNormalized);
                    formulaString = formulaString + sNormalized;
                }

                else if (Double.TryParse(s, out double result))
                {
                    formulaString = formulaString + result.ToString();
                }

                else
                {
                    formulaString = formulaString + s;
                }
            }

            return formulaString;
        }

        /// <summary>
        /// If obj is null or obj is not a Formula, returns false.  Otherwise, reports
        /// whether or not this Formula and obj are equal.
        /// 
        /// Two Formulae are considered equal if they consist of the same tokens in the
        /// same order.  To determine token equality, all tokens are compared as strings 
        /// except for numeric tokens and variable tokens.
        /// Numeric tokens are considered equal if they are equal after being "normalized" 
        /// by C#'s standard conversion from string to double, then back to string. This 
        /// eliminates any inconsistencies due to limited floating point precision.
        /// Variable tokens are considered equal if their normalized forms are equal, as 
        /// defined by the provided normalizer.
        /// 
        /// For example, if N is a method that converts all the letters in a string to upper case:
        ///  
        /// new Formula("x1+y2", N, s => true).Equals(new Formula("X1  +  Y2")) is true
        /// new Formula("x1+y2").Equals(new Formula("X1+Y2")) is false
        /// new Formula("x1+y2").Equals(new Formula("y2+x1")) is false
        /// new Formula("2.0 + x7").Equals(new Formula("2.000 + x7")) is true
        /// </summary>
        public override bool Equals(object obj)
        {
            string string1 = "" + this;
            string string2 = "" + obj;
            return string1 == string2;
        }

        /// <summary>
        /// Reports whether f1 == f2, using the notion of equality from the Equals method.
        /// Note that if both f1 and f2 are null, this method should return true.  If one is
        /// null and one is not, this method should return false.
        /// </summary>
        public static bool operator ==(Formula f1, Formula f2)
        {
            string string1 = "" + f1;
            string string2 = "" + f2;
            return string1 == string2;
        }

        /// <summary>
        /// Reports whether f1 != f2, using the notion of equality from the Equals method.
        /// Note that if both f1 and f2 are null, this method should return false.  If one is
        /// null and one is not, this method should return true.
        /// </summary>
        public static bool operator !=(Formula f1, Formula f2)
        {
            string string1 = "" + f1;
            string string2 = "" + f2;
            return string1 != string2;
        }

        /// <summary>
        /// Returns a hash code for this Formula.  If f1.Equals(f2), then it must be the
        /// case that f1.GetHashCode() == f2.GetHashCode().  Ideally, the probability that two 
        /// randomly-generated unequal Formulae have the same hash code should be extremely small.
        /// We multiply each value to add to the hashCode by 37, as it is a prime number, and will therefore make random generation of the same hashCode for different formulas much less likely.
        /// </summary>
        public override int GetHashCode()
        {
            int hashCode = 0;

            foreach (String s in tokens)
            {
                if (isValidVariable(s))
                {
                    variablesList.TryGetValue(s, out string sNormalized);
                    Double.TryParse(sNormalized, out double resultDouble);
                    hashCode = hashCode + (resultDouble.ToString().GetHashCode() * 37);
                }

                else if (Double.TryParse(s, out double result))
                {
                    hashCode = hashCode + (result.ToString().GetHashCode() * 37);
                }

                else
                {
                    hashCode = hashCode + (s.GetHashCode() * 37);
                }
            }
            return hashCode;
        }

        /// <summary>
        /// Given an expression, enumerates the tokens that compose it.  Tokens are left paren;
        /// right paren; one of the four operator symbols; a string consisting of a letter or underscore
        /// followed by zero or more letters, digits, or underscores; a double literal; and anything that doesn't
        /// match one of those patterns.  There are no empty tokens, and no token contains white space.
        /// </summary>
        private static IEnumerable<string> GetTokens(String formula)
        {
            // Patterns for individual tokens
            String lpPattern = @"\(";
            String rpPattern = @"\)";
            String opPattern = @"[\+\-*/]";
            String varPattern = @"[a-zA-Z_](?: [a-zA-Z_]|\d)*";
            String doublePattern = @"(?: \d+\.\d* | \d*\.\d+ | \d+ ) (?: [eE][\+-]?\d+)?";
            String spacePattern = @"\s+";

            // Overall pattern
            String pattern = String.Format("({0}) | ({1}) | ({2}) | ({3}) | ({4}) | ({5})",
                                            lpPattern, rpPattern, opPattern, varPattern, doublePattern, spacePattern);

            // Enumerate matching tokens that don't consist solely of white space.
            foreach (String s in Regex.Split(formula, pattern, RegexOptions.IgnorePatternWhitespace))
            {
                if (!Regex.IsMatch(s, @"^\s*$", RegexOptions.Singleline))
                {
                    yield return s;
                }
            }

        }

        /// <summary>
        /// A method used to check if the first or last token are valid tokens for their position in the formula. If not, will assist in throwing a FormulaFormatException.
        /// </summary>
        /// <param name="tokenToCheck"></param>
        /// <param name="tokenPosition"></param>
        /// <returns></returns>
        private bool checkToken(string tokenToCheck, int tokenPosition)
        {
            // If the token is the first token
            if (tokenPosition == 0)
            {
                return char.IsLetterOrDigit(tokenToCheck[0]) || tokenToCheck[0].Equals('_') || tokenToCheck[0].Equals('(');
            }

            // If the token is the last token
            else
            {
                return char.IsLetterOrDigit(tokenToCheck[0]) || tokenToCheck[0].Equals('_') || tokenToCheck[0].Equals(')');
            }
        }

        /// <summary>
        /// A method used to check if the token is valid based on the token that it is directly following. If not, will assist in throwing a FormulaFormatException.
        /// </summary>
        /// <param name="tokenToCheck"></param>
        /// <param name="lastToken"></param>
        /// <returns></returns>
        private bool lastTokenBool(string tokenToCheck, string lastToken)
        {
            if (lastToken == "(" || lastToken == "*" || lastToken == "/" || lastToken == "+" || lastToken == "-")
            {
                return Double.TryParse(tokenToCheck, out double doubleReturned) || isValidVariable(tokenToCheck) || tokenToCheck == "(";
            }

            else
            {
                return tokenToCheck == ")" || tokenToCheck == "*" || tokenToCheck == "/" || tokenToCheck == "+" || tokenToCheck == "-";
            }
        }

        /// <summary>
        /// A method used to check if a given token is a valid variable. If not, can assist in throwing a FormulaFormatException.
        /// </summary>
        /// <param name="tokenToCheck"></param>
        /// <returns></returns>
        private bool isValidVariable(string tokenToCheck)
        {
            // If the first character is not a letter or underscore
            if (!char.IsLetter(tokenToCheck[0]) && tokenToCheck[0] != '_')
            {
                return false;
            }

            // If any characters after are not digits, letters, or underscores
            for (int i = 0; i < tokenToCheck.Length; i++)
            {
                if (!char.IsLetterOrDigit(tokenToCheck[i]) && tokenToCheck[i] != '_')
                {
                    return false;
                }
            }
            return true;
        }

        /// <summary>
        /// A method to check the operator stack for the desired operator.
        /// </summary>
        /// <param name="stackToCheck"></param>
        /// <param name="stringToCheck"></param>
        /// <returns></returns>
        private static bool stackCheckOperator(Stack<string> stackToCheck, string stringToCheck)
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

    /// <summary>
    /// Used to report syntactic errors in the argument to the Formula constructor.
    /// </summary>
    public class FormulaFormatException : Exception
    {
        /// <summary>
        /// Constructs a FormulaFormatException containing the explanatory message.
        /// </summary>
        public FormulaFormatException(String message)
            : base(message)
        {
        }
    }

    /// <summary>
    /// Used as a possible return value of the Formula.Evaluate method.
    /// </summary>
    public struct FormulaError
    {
        /// <summary>
        /// Constructs a FormulaError containing the explanatory reason.
        /// </summary>
        /// <param name="reason"></param>
        public FormulaError(String reason)
            : this()
        {
            Reason = reason;
        }

        /// <summary>
        ///  The reason why this FormulaError was created.
        /// </summary>
        public string Reason { get; private set; }
    }
}



