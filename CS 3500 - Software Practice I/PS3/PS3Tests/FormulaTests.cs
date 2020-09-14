// Written by Brandon Walters, 9/13/19

using Microsoft.VisualStudio.TestTools.UnitTesting;
using SpreadsheetUtilities;
using System.Collections.Generic;

namespace PS3Tests
{
    [TestClass]
    public class FormulaTests
    {

        // These tests are all designed to pass

        [TestMethod]
        public void createFormula()
        {
            Formula formula1 = new Formula("5 + 5");
            Assert.AreEqual("5+5", formula1.ToString());
        }

        [TestMethod]
        public void FormulaEqualsSpaces()
        {
            Formula formula1 = new Formula("5 + 5");
            Formula formula2 = new Formula("5+5");
            Assert.IsTrue(formula1.Equals(formula2));
        }

        [TestMethod]
        public void WeirdVariableName()
        {
            Formula formula1 = new Formula("_a1b2_");
            Assert.AreEqual(1.0, formula1.Evaluate(s => 1));
        }

        [TestMethod]
        public void FormulaVariableEquals ()
        {
            Formula formula1 = new Formula("5 + 5");
            Formula formula2 = new Formula("5+x1", s => "5", s => true);
            Assert.IsTrue(formula1.Equals(formula2));
        }

        [TestMethod]
        public void FormulaVariableNotEquals()
        {
            Formula formula1 = new Formula("5 + 5");
            Formula formula2 = new Formula("5+x1", s => "3", s => true);
            Assert.IsFalse(formula1.Equals(formula2));
        }

        [TestMethod]
        public void FormulaVariableEqualsOperator()
        {
            Formula formula1 = new Formula("5 + 5");
            Formula formula2 = new Formula("5+x1", s => "5", s => true);
            Assert.IsTrue(formula1 == formula2);
        }

        [TestMethod]
        public void FormulaNullEqualsOperator()
        {
            Formula formula1 = new Formula(null);
            Formula formula2 = new Formula(null);
            Assert.IsTrue(formula1 == formula2);
        }

        [TestMethod]
        public void FormulaNullNotEqualsOperator()
        {
            Formula formula1 = new Formula(null);
            Formula formula2 = new Formula(null);
            Assert.IsFalse(formula1 != formula2);
        }

        [TestMethod]
        public void FormulaVariableNotEqualsOperator()
        {
            Formula formula1 = new Formula("5 + 5");
            Formula formula2 = new Formula("5+x1", s => "3", s => true);
            Assert.IsTrue(formula1 != formula2);
        }

        [TestMethod]
        public void FormulaVariableHashCode()
        {
            Formula formula1 = new Formula("5 + 5");
            Formula formula2 = new Formula("5+x1", s => "5", s => true);
            Assert.IsTrue(formula1.GetHashCode() == formula2.GetHashCode());
        }

        [TestMethod]
        public void FormulaHashCode()
        {
            Formula formula1 = new Formula("5 + 5");
            Formula formula2 = new Formula("5+5");
            Assert.IsTrue(formula1.GetHashCode() == formula2.GetHashCode());
        }

        [TestMethod]
        public void FormulaHashCodeNotEqual()
        {
            Formula formula1 = new Formula("5 + 5");
            Formula formula2 = new Formula("5+10");
            Assert.IsTrue(formula1.GetHashCode() != formula2.GetHashCode());
        }

        [TestMethod]
        public void GetVariablesEmpty()
        {
            Formula formula1 = new Formula("5 + 5");
            Formula formula2 = new Formula("5+10");
            List<string> varList = (List<string>) formula1.GetVariables();
            Assert.IsTrue(varList.Count == 0);
        }

        [TestMethod]
        public void GetVariables()
        {
            Formula formula1 = new Formula("5+x1");
            List<string> varList = (List<string>)formula1.GetVariables();
            Assert.IsTrue(varList.Count == 1);

            formula1 = new Formula("x1+x1");
            varList = (List<string>)formula1.GetVariables();
            Assert.IsTrue(varList.Count == 1);

            formula1 = new Formula("x1+x2");
            varList = (List<string>)formula1.GetVariables();
            Assert.IsTrue(varList.Count == 2);
        }

        [TestMethod]
        public void FormulaEvaluateNoVariables()
        {
            Formula formula1 = new Formula("5 + 6 / 3", s => s, s => true);
            Assert.AreEqual(7.0, formula1.Evaluate(null));
        }

        [TestMethod]
        public void FormulaEvaluateVariables()
        {
            Formula formula1 = new Formula("5 + x1 / 3", s => s, s => true);
            Assert.AreEqual(7.0, formula1.Evaluate(s => 6));
        }

        [TestMethod()]
        public void FormulaEqualsOperator()
        {
            Formula formula1 = new Formula("y1*3-8/2+4*(8-9*2)/14*x7", s => s, s => true);
            Formula formula2 = new Formula("y1*3-8/2+4*(8-9*2)/14*x7", s => s, s => true);
            formula1.Evaluate(s => (s == "x7") ? 1 : 4);
            Assert.IsTrue(formula1 == formula2);
        }

        [TestMethod()]
        public void FormulaEqualsDoubles()
        {
            Formula formula1 = new Formula("2.0000000000");
            Formula formula2 = new Formula("2.0");
            Assert.IsTrue(formula1.Equals(formula2));
        }

        [TestMethod()]
        public void FormulaEqualsScientificNotation()
        {
            Formula formula1 = new Formula("0.1");
            Formula formula2 = new Formula("1e-1");
            Assert.IsTrue(formula1.Equals(formula2));
        }


        // All methods down here should throw an FormulaFormatException or a FormulaError


        [TestMethod]
        [ExpectedException(typeof(FormulaFormatException))]
        public void FormulaEmptyError()
        {
            Formula formula1 = new Formula(" ");
        }

        

        [TestMethod]
        [ExpectedException(typeof(FormulaFormatException))]
        public void FormulaBeginningError()
        {
            Formula formula1 = new Formula("+ 3 * (5)");
        }

        [TestMethod]
        [ExpectedException(typeof(FormulaFormatException))]
        public void FormulaEndingError()
        {
            Formula formula1 = new Formula("3 * (5)/");
        }

        [TestMethod]
        [ExpectedException(typeof(FormulaFormatException))]
        public void FormulaLastTokenError()
        {
            Formula formula1 = new Formula("5 + 4 *");
        }

        [TestMethod]
        [ExpectedException(typeof(FormulaFormatException))]
        public void FormulaNormalizedVariableError()
        {
            Formula formula1 = new Formula("3 * y1 + 1", s => s, s => (s == "x1"));
        }

        [TestMethod]
        [ExpectedException(typeof(FormulaFormatException))]
        public void FormulaUnequalClosingParenthesesError()
        {
            Formula formula1 = new Formula("(5)) + 8");
        }

        [TestMethod]
        [ExpectedException(typeof(FormulaFormatException))]
        public void FormulaUnequalOpeningParenthesesError()
        {
            Formula formula1 = new Formula("(((5)) + 8");
        }

        [TestMethod]
        [ExpectedException(typeof(FormulaFormatException))]
        public void FormulaVariableFormattingError()
        {
            Formula formula1 = new Formula("4 + % * 9");
        }

        [TestMethod]
        [ExpectedException(typeof(FormulaFormatException))]
        public void FormulaVariableFormattingErrorMiddle()
        {
            Formula formula1 = new Formula("4 + a$#^B * 9");
        }

        [TestMethod()]
        public void FormulaErrorDivisionBy0()
        {
            Formula formula1 = new Formula("5/0");
            FormulaError error = new FormulaError("Division by 0 occured!");
            Assert.AreEqual(error, formula1.Evaluate(null));
        }

        [TestMethod()]
        public void FormulaErrorUndefinedVariable()
        {
            Formula formula1 = new Formula("5 + x1");
            FormulaError error = new FormulaError("Undefined variable was given!");
            Assert.AreEqual(error, formula1.Evaluate(null));
        }

        [TestMethod()]
        public void TestDivisionVariableDivBy0()
        {
            Formula formula1 = new Formula("16/x1");
            FormulaError error = new FormulaError("Division by 0 occured!");
            Assert.AreEqual(error, formula1.Evaluate(s => 0));
        }

        [TestMethod()]
        public void TestDivisionParenthesesDivBy0()
        {
            Formula formula1 = new Formula("16/(1 - 1)");
            FormulaError error = new FormulaError("Division by 0 occured!");
            Assert.AreEqual(error, formula1.Evaluate(null));
        }


        // Below are PS1 tests, corrected for PS3 and largely used to flesh out code coverage for the Evaluate method.


        [TestMethod()]
        public void TestComplexNestedParensRight()
        {
            Formula formula1 = new Formula("x1+(x2+(x3+(x4+(x5+x6))))");
            Assert.AreEqual(6.0, formula1.Evaluate(s => 1));
        }

        [TestMethod()]
        public void TestComplexNestedParensLeft()
        {
            Formula formula1 = new Formula("((((x1+x2)+x3)+x4)+x5)+x6");
            Assert.AreEqual(12.0, formula1.Evaluate(s => 2));
        }

        [TestMethod()]
        public void TestLeftToRight()
        {
            Formula formula1 = new Formula("2*6+3");
            Assert.AreEqual(15.0, formula1.Evaluate(null));
        }

        [TestMethod()]
        public void TestOrderOperations()
        {
            Formula formula1 = new Formula("2+6*3");
            Assert.AreEqual(20.0, formula1.Evaluate(null));
        }

        [TestMethod()]
        public void TestParenthesesTimes()
        {
            Formula formula1 = new Formula("(2+6)*3");
            Assert.AreEqual(24.0, formula1.Evaluate(null));
        }

        [TestMethod()]
        public void TestTimesParentheses()
        {
            Formula formula1 = new Formula("2*(3+5)");
            Assert.AreEqual(16.0, formula1.Evaluate(null));
        }

        [TestMethod()]
        public void TestPlusParentheses()
        {
            Formula formula1 = new Formula("2+(3+5)");
            Assert.AreEqual(10.0, formula1.Evaluate(null));
        }

        [TestMethod()]
        public void TestPlusComplex()
        {
            Formula formula1 = new Formula("2+(3+5*9)");
            Assert.AreEqual(50.0, formula1.Evaluate(null));
        }

        [TestMethod()]
        public void TestOperatorAfterParens()
        {
            Formula formula1 = new Formula("(1*1)-2/2");
            Assert.AreEqual(0.0, formula1.Evaluate(null));
        }

        [TestMethod()]
        public void TestComplexTimesParentheses()
        {
            Formula formula1 = new Formula("2+3*(3+5)");
            Assert.AreEqual(26.0, formula1.Evaluate(null));
        }

        [TestMethod()]
        public void TestComplexAndParentheses()
        {
            Formula formula1 = new Formula("2+3*5+(3+4*8)*5+2");
            Assert.AreEqual(194.0, formula1.Evaluate(null));
        }

        [TestMethod()]
        public void TestDivision()
        {
            Formula formula1 = new Formula("16/2");
            Assert.AreEqual(8.0, formula1.Evaluate(null));
        }

        [TestMethod()]
        public void TestDivisionVariable()
        {
            Formula formula1 = new Formula("16/x1");
            Assert.AreEqual(8.0, formula1.Evaluate(s => 2));
        }

        [TestMethod()]
        public void TestDivisionParentheses()
        {
            Formula formula1 = new Formula("16/(2 + 0)");
            Assert.AreEqual(8.0, formula1.Evaluate(null));
        }  
    }
}
