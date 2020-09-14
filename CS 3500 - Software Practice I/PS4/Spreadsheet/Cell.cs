// Written by Brandon Walters, CS 3500, 9/20/19
// Written by Brandon Walters, CS 3500, 9/27/19

using System;
using System.Collections.Generic;
using System.Text;
using SpreadsheetUtilities;

namespace SS
{
    /// <summary>
    /// This class represents the Cell object that makes up the Spreadsheet we are building. It contains contents (what is written into the cell) and a value (what the contents are after evaluation).
    /// </summary>
    class Cell
    {
        private string cellContents = "";
        private object cellValue = "";
        private bool isFormula;
        private bool wasLastFormula = false;

        public Cell()
        {
        }
        /// <summary>
        /// This method is used to set the contents of the Cell.
        /// </summary>
        /// <param name="contentsToSet"></param>
        public void setContents(string contentsToSet, bool isFormulaString)
        {
            wasLastFormula = isFormula;

            cellContents = contentsToSet;
            isFormula = isFormulaString;
        }

        /// <summary>
        /// This method is used to get the contents of the Cell.
        /// </summary>
        public string getContents()
        {
            return cellContents;
        }

        /// <summary>
        /// This method is used to get the value of the Cell.
        /// </summary>
        public object getValue()
        {
            return cellValue;
        }

        /// <summary>
        /// This method is used to set the value of the Cell.
        /// </summary>
        public void setValue(Object cellValueObject)
        {
            cellValue = cellValueObject;
        }

        public bool getIsFormula()
        {
            return isFormula;
        }

        public bool wasPreviousContentsFormula()
        {
            return wasLastFormula;
        }
    }
}
