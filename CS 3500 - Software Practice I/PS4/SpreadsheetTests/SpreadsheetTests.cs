// Written by Brandon Walters, CS 3500, 9/20/19
// Updated by Brandon Walters for PS5, CS 3500, 9/27/19

using Microsoft.VisualStudio.TestTools.UnitTesting;
using SpreadsheetUtilities;
using SS;
using System;
using System.Collections.Generic;

namespace SpreadsheetTests
{
    [TestClass]
    public class SpreadsheetTests
    {

        [TestMethod]
        public void saveAndVersion()
        {
            AbstractSpreadsheet spreadsheet = new Spreadsheet();
            spreadsheet.SetContentsOfCell("A1", "5");
            spreadsheet.SetContentsOfCell("B1", "5");
            spreadsheet.SetContentsOfCell("C1", "5");
            spreadsheet.SetContentsOfCell("D1", "A1 + B1 + C1");
            spreadsheet.SetContentsOfCell("E1", "=A1 + B1 + C1");
            Assert.IsTrue(spreadsheet.Changed);
            spreadsheet.Save("temp.xml");
            Assert.IsFalse(spreadsheet.Changed);
            Assert.AreEqual("1.0", spreadsheet.GetSavedVersion("temp.xml"));
        }

        [TestMethod]
        public void buildSSFromXMLFile()
        {
            AbstractSpreadsheet spreadsheet = new Spreadsheet();
            spreadsheet.SetContentsOfCell("A1", "5");
            spreadsheet.SetContentsOfCell("B1", "5");
            spreadsheet.SetContentsOfCell("C1", "5");
            spreadsheet.SetContentsOfCell("D1", "A1 + B1 + C1");
            spreadsheet.SetContentsOfCell("E1", "=A1 + B1 + C1");
            spreadsheet.Save("temp.xml");

            AbstractSpreadsheet spreadsheet2 = new Spreadsheet("temp.xml", s => true, s => s, "1.0");
            Assert.AreEqual("1.0", spreadsheet2.Version);
            Assert.IsTrue(new HashSet<string>() { "A1", "B1", "C1", "D1", "E1" }.SetEquals(spreadsheet2.GetNamesOfAllNonemptyCells()));
            Assert.AreEqual(5.0, spreadsheet.GetCellValue("A1"));
            Assert.AreEqual("A1 + B1 + C1", spreadsheet.GetCellValue("D1"));
            Assert.AreEqual(15.0, spreadsheet.GetCellValue("E1"));
        }

        [TestMethod]
        public void testGetValueDouble()
        {
            AbstractSpreadsheet spreadsheet = new Spreadsheet();
            spreadsheet.SetContentsOfCell("A1", "5");
            Assert.AreEqual(5.0, spreadsheet.GetCellValue("A1"));
        }

        [TestMethod]
        public void testGetValueFormula()
        {
            AbstractSpreadsheet spreadsheet = new Spreadsheet();
            spreadsheet.SetContentsOfCell("A1", "=5");
            Assert.AreEqual(5.0, spreadsheet.GetCellValue("A1"));
        }

        [TestMethod]
        public void testGetValueText()
        {
            AbstractSpreadsheet spreadsheet = new Spreadsheet();
            spreadsheet.SetContentsOfCell("A1", "howdy");
            Assert.AreEqual("howdy", spreadsheet.GetCellValue("A1"));
        }

        [TestMethod]
        public void threeTypesOfCell()
        {
            AbstractSpreadsheet spreadsheet = new Spreadsheet();
            spreadsheet.SetContentsOfCell("A1", "5");
            spreadsheet.SetContentsOfCell("B1", "5");
            spreadsheet.SetContentsOfCell("C1", "5");
            spreadsheet.SetContentsOfCell("D1", "A1 + B1 + C1");
            spreadsheet.SetContentsOfCell("E1", "=A1 + B1 + C1");
            Assert.AreEqual(5, (Double) spreadsheet.GetCellContents("A1"));
            Assert.AreEqual("A1 + B1 + C1", spreadsheet.GetCellContents("D1"));
            Assert.AreEqual(new Formula("A1+B1+C1"), (Formula) spreadsheet.GetCellContents("E1"));
        }

        [TestMethod]
        public void overwriteDoubleCell()
        {
            AbstractSpreadsheet spreadsheet = new Spreadsheet();
            spreadsheet.SetContentsOfCell("A1", "5");
            spreadsheet.SetContentsOfCell("A1", "10");
            Assert.AreEqual(10, (Double) spreadsheet.GetCellContents("A1"));
        }

        [TestMethod]
        public void overwriteTextCell()
        {
            AbstractSpreadsheet spreadsheet = new Spreadsheet();
            spreadsheet.SetContentsOfCell("A1", "unepic");
            spreadsheet.SetContentsOfCell("A1", "now this is epic");
            Assert.AreEqual("now this is epic", spreadsheet.GetCellContents("A1"));
        }

        [TestMethod]
        public void overwriteFormulaCell()
        {
            AbstractSpreadsheet spreadsheet = new Spreadsheet();
            spreadsheet.SetContentsOfCell("E1", "=A1");
            spreadsheet.SetContentsOfCell("E1", "=A1 + B1 + C1");
            Assert.AreEqual(new Formula("A1+B1+C1"), (Formula) spreadsheet.GetCellContents("E1"));
        }

        [TestMethod]
        public void overwriteCellThreeTypes()
        {
            AbstractSpreadsheet spreadsheet = new Spreadsheet();
            spreadsheet.SetContentsOfCell("A1", "5");
            spreadsheet.SetContentsOfCell("A1", "A1 + B1 + C1");
            spreadsheet.SetContentsOfCell("B1", "5");
            spreadsheet.SetContentsOfCell("C1", "5");
            spreadsheet.SetContentsOfCell("A1", "=B1 + C1");
            Assert.AreEqual(new Formula("B1+C1"), (Formula)spreadsheet.GetCellContents("A1"));
        }

        [TestMethod]
        public void getNonEmptyCells()
        {
            AbstractSpreadsheet spreadsheet = new Spreadsheet();
            spreadsheet.SetContentsOfCell("A1", "5");
            spreadsheet.SetContentsOfCell("B1", "howdy");
            spreadsheet.SetContentsOfCell("C1", "=10");

            List<string> stringList = new List<string>(spreadsheet.GetNamesOfAllNonemptyCells());
            Assert.AreEqual(3, stringList.Count);
        }

        [TestMethod]
        public void CellGetContentsEmptyCell()
        {
            AbstractSpreadsheet spreadsheet = new Spreadsheet();
            spreadsheet.GetCellContents("A1");
            Assert.AreEqual("", spreadsheet.GetCellContents("A1"));
        }

        [TestMethod]
        public void changeDoubleToFormulaCell()
        {
            AbstractSpreadsheet spreadsheet = new Spreadsheet();
            spreadsheet.SetContentsOfCell("E1", "10");
            spreadsheet.SetContentsOfCell("E1", "=A1 + B1 + C1");
            Assert.AreEqual(new Formula("A1+B1+C1"), (Formula)spreadsheet.GetCellContents("E1"));
        }

        [TestMethod]
        public void changeFormulaToDoubleCell()
        {
            AbstractSpreadsheet spreadsheet = new Spreadsheet();
            spreadsheet.SetContentsOfCell("E1", "=A1 + B1 + C1");
            spreadsheet.SetContentsOfCell("E1", "10");
            Assert.AreEqual(10, (Double)spreadsheet.GetCellContents("E1"));
        }

        [TestMethod]
        public void changeFormulaToTextCell()
        {
            AbstractSpreadsheet spreadsheet = new Spreadsheet();
            spreadsheet.SetContentsOfCell("E1", "=A1 + B1 + C1");
            spreadsheet.SetContentsOfCell("E1", "howdy");
            Assert.AreEqual("howdy", spreadsheet.GetCellContents("E1"));
            Assert.AreEqual("howdy", spreadsheet.GetCellValue("E1"));
        }

        [TestMethod]
        public void formulaErrorValueCheck()
        {
            AbstractSpreadsheet spreadsheet = new Spreadsheet();
            spreadsheet.SetContentsOfCell("A1", "=B1 + C1");
            spreadsheet.SetContentsOfCell("B1", "howdy");
            Assert.AreEqual("howdy", spreadsheet.GetCellContents("B1"));
            Assert.AreEqual(new FormulaError(), spreadsheet.GetCellValue("A1"));
        }

        [TestMethod]
        public void formulaErrorValueCheckFlipped()
        {
            AbstractSpreadsheet spreadsheet = new Spreadsheet();
            spreadsheet.SetContentsOfCell("B1", "howdy");
            spreadsheet.SetContentsOfCell("A1", "=B1 + C1");
            Assert.AreEqual("howdy", spreadsheet.GetCellContents("B1"));
            Assert.AreEqual(new FormulaError(), spreadsheet.GetCellValue("A1"));
        }

        [TestMethod]
        public void changeTextToFormulaCell()
        {
            AbstractSpreadsheet spreadsheet = new Spreadsheet();
            spreadsheet.SetContentsOfCell("E1", "howdy");
            spreadsheet.SetContentsOfCell("E1", "=A1 + B1 + C1");
            Assert.AreEqual(new Formula("A1+B1+C1"), (Formula)spreadsheet.GetCellContents("E1"));
        }


        // Everything below here is supposed to throw an exception!

        //[TestMethod]
        //[ExpectedException(typeof(SpreadsheetReadWriteException))]
        //public void saveError()
        //{
        //    AbstractSpreadsheet spreadsheet = new Spreadsheet();
        //    spreadsheet.SetContentsOfCell("A1", "5");
        //    spreadsheet.SetContentsOfCell("D1", "A1 + B1 + C1");
        //    spreadsheet.SetContentsOfCell("E1", "=A1 + B1 + C1");
        //    spreadsheet.Save("thisshouldntwork&!#(*!*@$.nah");
        //}

        [TestMethod]
        [ExpectedException(typeof(SpreadsheetReadWriteException))]
        public void buildSSFromXMLFileNonexistant()
        {
            AbstractSpreadsheet spreadsheet = new Spreadsheet();
            spreadsheet.SetContentsOfCell("A1", "5");
            spreadsheet.SetContentsOfCell("B1", "5");
            spreadsheet.SetContentsOfCell("C1", "5");
            spreadsheet.SetContentsOfCell("D1", "A1 + B1 + C1");
            spreadsheet.SetContentsOfCell("E1", "=A1 + B1 + C1");
            spreadsheet.Save("temp.xml");

            AbstractSpreadsheet spreadsheet2 = new Spreadsheet("notreal.xml", s => true, s => s, "1.0");
        }

        [TestMethod]
        [ExpectedException(typeof(SpreadsheetReadWriteException))]
        public void buildSSFromXMLFileWrongVersion()
        {
            AbstractSpreadsheet spreadsheet = new Spreadsheet(s => true, s => s, "1.0");
            spreadsheet.SetContentsOfCell("A1", "5");
            spreadsheet.SetContentsOfCell("B1", "5");
            spreadsheet.SetContentsOfCell("C1", "5");
            spreadsheet.SetContentsOfCell("D1", "A1 + B1 + C1");
            spreadsheet.SetContentsOfCell("E1", "=A1 + B1 + C1");
            spreadsheet.Save("temp.xml");

            AbstractSpreadsheet spreadsheet2 = new Spreadsheet("temp.xml", s => true, s => s, "1.1");
        }

        [TestMethod]
        [ExpectedException(typeof(SpreadsheetReadWriteException))]
        public void getSavedVersionNonexistant()
        {
            AbstractSpreadsheet spreadsheet = new Spreadsheet();
            spreadsheet.SetContentsOfCell("A1", "5");
            spreadsheet.SetContentsOfCell("B1", "5");
            spreadsheet.SetContentsOfCell("C1", "5");
            spreadsheet.SetContentsOfCell("D1", "A1 + B1 + C1");
            spreadsheet.SetContentsOfCell("E1", "=A1 + B1 + C1");
            spreadsheet.Save("temp.xml");

            spreadsheet.GetSavedVersion("notreal.xml");
        }

        [TestMethod]
        [ExpectedException(typeof(SpreadsheetReadWriteException))]
        public void buildSSCircular()
        {
            AbstractSpreadsheet spreadsheet = new Spreadsheet();
            spreadsheet.SetContentsOfCell("A1", "=B1 * 2");
            spreadsheet.SetContentsOfCell("B1", "=C1 * 2");
            spreadsheet.SetContentsOfCell("C1", "=a1 * 2");
            spreadsheet.Save("temp.xml");

            AbstractSpreadsheet spreadsheet2 = new Spreadsheet("temp.xml", s => true, s => s.ToUpper(), "1.0");
        }

        [TestMethod]
        [ExpectedException(typeof(SpreadsheetReadWriteException))]
        public void buildSSInvalidCell()
        {
            AbstractSpreadsheet spreadsheet = new Spreadsheet();
            spreadsheet.SetContentsOfCell("A1", "5");
            spreadsheet.SetContentsOfCell("B1", "5");
            spreadsheet.SetContentsOfCell("C1", "5");
            spreadsheet.SetContentsOfCell("D1", "A1 + B1 + C1");
            spreadsheet.SetContentsOfCell("E1", "=A1 + B1 + C1");
            spreadsheet.Save("temp.xml");

            AbstractSpreadsheet spreadsheet2 = new Spreadsheet("temp.xml", s => false, s => s, "1.0");
        }

        [TestMethod]
        [ExpectedException(typeof(InvalidNameException))]
        public void doubleCellInvalidCellNames()
        {
            AbstractSpreadsheet spreadsheet = new Spreadsheet();
            spreadsheet.SetContentsOfCell("%", "5");
        }

        [TestMethod]
        [ExpectedException(typeof(InvalidNameException))]
        public void getCellValueNull()
        {
            AbstractSpreadsheet spreadsheet = new Spreadsheet();
            spreadsheet.SetContentsOfCell("A1", "5");
            spreadsheet.GetCellValue(null);
        }

        [TestMethod]
        [ExpectedException(typeof(InvalidNameException))]
        public void getCellValueInvalid()
        {
            AbstractSpreadsheet spreadsheet = new Spreadsheet();
            spreadsheet.SetContentsOfCell("A1", "5");
            spreadsheet.GetCellValue("_not_Valid_Cell");
        }

        [TestMethod]
        [ExpectedException(typeof(InvalidNameException))]
        public void textCellInvalidCellNames()
        {
            AbstractSpreadsheet spreadsheet = new Spreadsheet();
            spreadsheet.SetContentsOfCell("%", "A1 + B1 + C1");
        }

        [TestMethod]
        [ExpectedException(typeof(InvalidNameException))]
        public void formulaCellInvalidCellNames()
        {
            AbstractSpreadsheet spreadsheet = new Spreadsheet();
            spreadsheet.SetContentsOfCell("A!", "=A1 + B1 + C1");
        }

        [TestMethod]
        [ExpectedException(typeof(InvalidNameException))]
        public void CellGetContentsInvalid()
        {
            AbstractSpreadsheet spreadsheet = new Spreadsheet();
            spreadsheet.GetCellContents(".");
        }

        [TestMethod]
        [ExpectedException(typeof(InvalidNameException))]
        public void CellGetContentsNull()
        {
            AbstractSpreadsheet spreadsheet = new Spreadsheet();
            spreadsheet.GetCellContents(null);
        }

        [TestMethod]
        [ExpectedException(typeof(InvalidNameException))]
        public void doubleNullInvalid()
        {
            AbstractSpreadsheet spreadsheet = new Spreadsheet();
            spreadsheet.SetContentsOfCell(null, "5");
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentNullException))]
        public void textNullInvalid()
        {
            string epic = null;
            AbstractSpreadsheet spreadsheet = new Spreadsheet();
            spreadsheet.SetContentsOfCell("A1", epic);
        }

        [TestMethod]
        [ExpectedException(typeof(ArgumentNullException))]
        public void formulaNullInvalid()
        {
            string epic = null;
            AbstractSpreadsheet spreadsheet = new Spreadsheet();
            spreadsheet.SetContentsOfCell("A1", epic);
        }

        [TestMethod]
        [ExpectedException(typeof(CircularException))]
        public void formulaCellCircular()
        {
            AbstractSpreadsheet spreadsheet = new Spreadsheet();
            spreadsheet.SetContentsOfCell("A1", "=B1 * 2");
            spreadsheet.SetContentsOfCell("B1", "=C1 * 2");
            spreadsheet.SetContentsOfCell("C1", "=A1 * 2");
        }

        [TestMethod]
        [ExpectedException(typeof(CircularException))]
        public void formulaCellCircularChange()
        {
            AbstractSpreadsheet spreadsheet = new Spreadsheet();
            spreadsheet.SetContentsOfCell("A1", "=B1 * 2");
            spreadsheet.SetContentsOfCell("B1", "=C1 * 2");
            spreadsheet.SetContentsOfCell("C1", "=D1 * 2");
            spreadsheet.SetContentsOfCell("C1", "=A1 * 2");
        }
    }
}
