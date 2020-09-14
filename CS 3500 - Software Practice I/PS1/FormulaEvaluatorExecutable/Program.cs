using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace FormulaEvaluatorExecutable
{
    class Program
    {
        /// <summary>
        /// The map used to keep track of variable values.
        /// </summary>
        static Dictionary<string, int> varMap = new Dictionary<string, int>();

        static void Main(string[] args)
        {
            // Creation of a lookup delegate used to evaluate variable values
            FormulaEvaluator.Evaluator.Lookup varLookup = new FormulaEvaluator.Evaluator.Lookup(Program.VarLookup);
            try
            {
                // Building the variable map for the lookup delegate
                varMap.Add("a7", 5);
                varMap.Add("A7", 6);
                varMap.Add("AA77", 10);
                varMap.Add("ABC123", 20);
                varMap.Add("wrong", 1);
                varMap.Add("123", 1);
                varMap.Add("LeTsGoDoOd1", 2);

                Console.WriteLine("2 + 3 is...");
                Console.WriteLine(FormulaEvaluator.Evaluator.Evaluate("2 + 3", null));

                Console.WriteLine("5 * (2 + 3) is...");
                Console.WriteLine(FormulaEvaluator.Evaluator.Evaluate("5 * (2 + 3)", null));

                Console.WriteLine("2/2 is...");
                Console.WriteLine(FormulaEvaluator.Evaluator.Evaluate("2/2", null));

                Console.WriteLine("5 / 2 is...");
                Console.WriteLine(FormulaEvaluator.Evaluator.Evaluate("5 / 2", null));

                Console.WriteLine("(2 + 3) / (2 + 3) is...");
                Console.WriteLine(FormulaEvaluator.Evaluator.Evaluate("(2 + 3) / (2 + 3)", null));

                Console.WriteLine("(2 / 2) + (2 / 2) is...");
                Console.WriteLine(FormulaEvaluator.Evaluator.Evaluate("(2 / 2) + (2 / 2)", null));

                Console.WriteLine("16/(2 / 2) is...");
                Console.WriteLine(FormulaEvaluator.Evaluator.Evaluate("16/(2 / 2)", null));

                Console.WriteLine("16/(2 * 2) is...");
                Console.WriteLine(FormulaEvaluator.Evaluator.Evaluate("16/(2 * 2)", null));

                Console.WriteLine("8 / LeTsGoDoOd1 is...");
                Console.WriteLine(FormulaEvaluator.Evaluator.Evaluate("8 / LeTsGoDoOd1", varLookup));

                Console.WriteLine("8 * ABC123 is...");
                Console.WriteLine(FormulaEvaluator.Evaluator.Evaluate("8*ABC123", varLookup));

                Console.WriteLine("8 / 123 is...");
                Console.WriteLine(FormulaEvaluator.Evaluator.Evaluate("8 / 123", varLookup));

                Console.WriteLine("(1) + (1) is...");
                Console.WriteLine(FormulaEvaluator.Evaluator.Evaluate("(1) + (1)", null));


                // Everything past here WILL throw an exception

                Console.WriteLine("8 +  is...");
                Console.WriteLine(FormulaEvaluator.Evaluator.Evaluate("8 + ", varLookup));

                Console.WriteLine("8 /  is...");
                Console.WriteLine(FormulaEvaluator.Evaluator.Evaluate("8 / ", varLookup));

                Console.WriteLine("8* is...");
                Console.WriteLine(FormulaEvaluator.Evaluator.Evaluate("8*", varLookup));

                Console.WriteLine("8- is...");
                Console.WriteLine(FormulaEvaluator.Evaluator.Evaluate("8-", varLookup));

                Console.WriteLine("8 / 0 is...");
                Console.WriteLine(FormulaEvaluator.Evaluator.Evaluate("8 / 0", varLookup));

                Console.WriteLine("8 / (1 - 1) is...");
                Console.WriteLine(FormulaEvaluator.Evaluator.Evaluate("8 / (1 - 1)", varLookup));

                Console.WriteLine("8 / DNE123 is...");
                Console.WriteLine(FormulaEvaluator.Evaluator.Evaluate("8 / DNE123", varLookup));

                Console.WriteLine("8 / wrong is...");
                Console.WriteLine(FormulaEvaluator.Evaluator.Evaluate("8 / wrong", varLookup));

                Console.WriteLine("() is...");
                Console.WriteLine(FormulaEvaluator.Evaluator.Evaluate("()", null));

                Console.WriteLine("8 ** 4 is...");
                Console.WriteLine(FormulaEvaluator.Evaluator.Evaluate("8 ** 4", null));

                Console.WriteLine("8 // 4 is...");
                Console.WriteLine(FormulaEvaluator.Evaluator.Evaluate("8 // 4", null));

                Console.WriteLine("8 ++ 4 is...");
                Console.WriteLine(FormulaEvaluator.Evaluator.Evaluate("8 ++ 4", null));

                Console.WriteLine("8 -- 4 is...");
                Console.WriteLine(FormulaEvaluator.Evaluator.Evaluate("8 -- 4", null));



                Console.ReadLine();


            }

            // If an exception is thrown
            catch (ArgumentException)
            {
                Console.WriteLine("Exception occured!");
                Console.ReadLine();
            }
        }

        /// <summary>
        /// The method used by the lookup delegate to store and return integer values for variables. 
        /// These values are stored using the Dictionary class, which functions as a Map.
        /// </summary>
        /// <param name="varName"></param>
        /// <returns></returns>
        static int VarLookup (string varName)
        {
            // If the map contains the variable searched for
            if (varMap.ContainsKey(varName))
            {
                varMap.TryGetValue(varName, out int value);
                return value;
            }
            
            // If the given variable does not exist
            else
            {
                throw new ArgumentException("Variable does not exist!");
            }
        } 
    }
}
