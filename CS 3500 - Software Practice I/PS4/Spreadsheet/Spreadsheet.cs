// Written by Brandon Walters, CS 3500, 9/20/19
// Updated by Brandon Walters for PS5, CS 3500, 9/27/19

using SpreadsheetUtilities;
using System;
using System.Collections.Generic;
using System.Xml;

namespace SS
{
    public class Spreadsheet : AbstractSpreadsheet
    {
        // I am using a DependencyGraph to keep track of the dependencies between different cells
        private DependencyGraph dependencies = new DependencyGraph();

        // I am using a Dictionary with strings representing the names of the non-empty cells as keys and the Cell objects I created as strings
        private Dictionary<string, Cell> cells = new Dictionary<string, Cell>();

        // This boolean represents if the spreadsheet has been changed since it was last saved
        private bool isChanged = false;

        /// <summary>
        /// This is the delegate passed to the Evaluate method of the Formula class, which is used to represent the updateCellValues method.
        /// </summary>
        /// <param name="cellName"></param>
        /// <returns></returns>
        private delegate double Lookup(string cellName);

        /// <summary>
        /// This constructor uses the default values, such that all normally legal cell names are legal, no normalization occurs, and the version is set as 1.0.
        /// </summary>
        public Spreadsheet() : this(name => true, name => name, "default")
        {
        }

        /// <summary>
        /// This constructor uses the values inputted by the user.
        /// </summary>
        public Spreadsheet(Func<string, bool> isValid, Func<string, string> normalize, string version) : base(isValid, normalize, version)
        {
            this.IsValid = isValid;
            this.Normalize = normalize;
            this.Version = version;
        }

        /// <summary>
        /// This constructor uses the values inputted by the user, building the spreadsheet from the given file.
        /// </summary>
        public Spreadsheet(string filePath, Func<string, bool> isValid, Func<string, string> normalize, string version) : base(isValid, normalize, version)
        {
            this.IsValid = isValid;
            this.Normalize = normalize;
            this.Version = version;

            try
            {
                using (XmlReader xmlReader = XmlReader.Create(filePath))
                {
                    xmlReader.Read();
                    xmlReader.Read();
                    xmlReader.Read();
                    xmlReader.MoveToFirstAttribute();

                    if (xmlReader.Value != Version)
                    {
                        throw new SpreadsheetReadWriteException("Versions did not match!");
                    }

                    {

                    }
                        while (xmlReader.Read())
                    {
                        
                        if (xmlReader.IsStartElement())
                        {
                            // Adds cells one at a time
                            if (xmlReader.Name == "name")
                            {
                                xmlReader.Read();

                                string cellName = xmlReader.Value;

                                if (!isValidVariable(cellName) || !isValid(cellName))
                                {
                                    throw new SpreadsheetReadWriteException("One of the cell names was invalid! Check your cells!");
                                }

                                xmlReader.Read();
                                xmlReader.Read();
                                xmlReader.Read();

                                if (xmlReader.Name == "contents")
                                {
                                    xmlReader.Read();

                                    string cellContents = xmlReader.Value;
                                    try
                                    {
                                        SetContentsOfCell(cellName, cellContents);
                                    }
                                    
                                    catch
                                    {
                                        throw new SpreadsheetReadWriteException("Some cell contents are invalid!");
                                    }
                                }
                            }
                        }
                    }
                }
            }

            catch
            {
                throw new SpreadsheetReadWriteException("Spreadsheet was unable to be read!");
            }
        }

        public override bool Changed { get => isChanged; protected set => isChanged = false; }

        /// <summary>
        /// If name is null or invalid, throws an InvalidNameException.
        /// 
        /// Otherwise, returns the contents (as opposed to the value) of the named cell.  The return
        /// value should be either a string, a double, or a Formula.
        /// </summary>
        public override object GetCellContents(string name)
        {
            name = Normalize(name);

            if (name == null || !isValidVariable(name))
            {
                throw new InvalidNameException();
            }

            if (cells.ContainsKey(name))
            {
                cells.TryGetValue(name, out Cell cell);
                string value = cell.getContents();
                if (Double.TryParse(value, out double valueDouble) && !cell.getIsFormula())
                {
                    return valueDouble;
                }
                else if (cell.getIsFormula())
                {
                    return new Formula(value.Substring(1, value.Length - 1), Normalize, IsValid);
                }
                else
                {
                    return value;
                }
            }

            // If the cell name is valid but not instantiated, return the empty string that should be there
            else
            {
                return "";
            }
        }

        // ADDED FOR PS5
        /// <summary>
        /// If name is null or invalid, throws an InvalidNameException.
        /// 
        /// Otherwise, returns the value (as opposed to the contents) of the named cell.  The return
        /// value should be either a string, a double, or a SpreadsheetUtilities.FormulaError.
        /// </summary>
        public override object GetCellValue(string name)
        {
            if (name != null)
            {
                name = Normalize(name);

                if (isValidVariable(name) && IsValid(name))
                {
                    if (cells.TryGetValue(name, out Cell cellToReturn))
                    {
                        return cellToReturn.getValue();
                    }

                    else
                    {
                        return "";
                    }
                }

                else
                {
                    throw new InvalidNameException();
                }
            }

            else
            {
                throw new InvalidNameException();
            }
        }


        /// /// <summary>
        /// Enumerates the names of all the non-empty cells in the spreadsheet.
        /// </summary>
        public override IEnumerable<string> GetNamesOfAllNonemptyCells()
        {
            return cells.Keys;
        }

        // ADDED FOR PS5
        /// <summary>
        /// Returns the version information of the spreadsheet saved in the named file.
        /// If there are any problems opening, reading, or closing the file, the method
        /// should throw a SpreadsheetReadWriteException with an explanatory message.
        /// </summary>
        public override string GetSavedVersion(string filename)
        {
            try
            {
                using (XmlReader xmlReader = XmlReader.Create(filename))
                {
                    xmlReader.Read();
                    xmlReader.Read();
                    xmlReader.Read();
                    xmlReader.MoveToFirstAttribute();
                    return xmlReader.Value;
                }
            }

            catch
            {
                throw new SpreadsheetReadWriteException("Spreadsheet was unable to be read!");
            }

        }

        // ADDED FOR PS5
        /// <summary>
        /// Writes the contents of this spreadsheet to the named file using an XML format.
        /// The XML elements should be structured as follows:
        /// 
        /// <spreadsheet version="version information goes here">
        /// 
        /// <cell>
        /// <name>
        /// cell name goes here
        /// </name>
        /// <contents>
        /// cell contents goes here
        /// </contents>    
        /// </cell>
        /// 
        /// </spreadsheet>
        /// 
        /// There should be one cell element for each non-empty cell in the spreadsheet.  
        /// If the cell contains a string, it should be written as the contents.  
        /// If the cell contains a double d, d.ToString() should be written as the contents.  
        /// If the cell contains a Formula f, f.ToString() with "=" prepended should be written as the contents.
        /// 
        /// If there are any problems opening, writing, or closing the file, the method should throw a
        /// SpreadsheetReadWriteException with an explanatory message.
        /// </summary>
        public override void Save(string filename)
        {
            XmlWriterSettings settings = new XmlWriterSettings();
            settings.Indent = true;
            settings.IndentChars = "    ";

            try
            {
                using (XmlWriter xmlWriter = XmlWriter.Create(filename, settings))
                {
                    xmlWriter.WriteStartDocument();

                    // Writes the version information of the spreadsheet given
                    xmlWriter.WriteStartElement("spreadsheet");
                    xmlWriter.WriteAttributeString("version", Version);

                    // Iteratively writes each cell's name and contents
                    foreach (string cellName in GetNamesOfAllNonemptyCells())
                    {
                        xmlWriter.WriteStartElement("cell");

                        cells.TryGetValue(cellName, out Cell cell);

                        xmlWriter.WriteElementString("name", cellName);

                        if (cell.getIsFormula())
                        {
                            xmlWriter.WriteElementString("contents", cell.getContents());
                        }

                        else if (Double.TryParse(cell.getContents(), out double result))
                        {
                            xmlWriter.WriteElementString("contents", result.ToString());
                        }

                        else
                        {
                            xmlWriter.WriteElementString("contents", cell.getContents());
                        }

                        xmlWriter.WriteEndElement();

                    }
                    xmlWriter.WriteEndElement();
                    xmlWriter.WriteEndDocument();
                }
                isChanged = false;
            }

            catch
            {
                throw new SpreadsheetReadWriteException("Spreadsheet saving failed!");
            }
        }

        /// <summary>
        /// If name is null or invalid, throws an InvalidNameException.
        /// 
        /// Otherwise, the contents of the named cell becomes number.  The method returns a
        /// list consisting of name plus the names of all other cells whose value depends, 
        /// directly or indirectly, on the named cell.
        /// 
        /// For example, if name is A1, B1 contains A1*2, and C1 contains B1+A1, the
        /// list {A1, B1, C1} is returned.
        /// </summary>
        protected override IList<string> SetCellContents(string name, double number)
        {
            name = Normalize(name);

            List<string> listOfCells;

            // Used to keep track of the previous contents of the cell
            Formula previousString;

                // If the cell has been previously instantiated
                if (cells.ContainsKey(name))
                {
                    cells.TryGetValue(name, out Cell cell);

                    if (cell.getIsFormula())
                    {
                        previousString = new Formula(cell.getContents().Substring(1, cell.getContents().Length - 1), Normalize, IsValid);
                    }

                    else
                    {
                        previousString = new Formula(cell.getContents());
                    }

                    if (previousString.ToString() != number.ToString())
                    {
                        cell.setContents(number.ToString(), false);

                        // Removes previous dependencies
                        foreach (String s in previousString.GetVariables())
                        {
                            dependencies.RemoveDependency(name, s);
                        }

                        listOfCells = new List<string>(GetCellsToRecalculate(name));

                        return listOfCells;
                    }

                    else
                    {
                        return new List<string>();
                    }
                    
                }

                // If the cell has not been previously instantiated
                else
                {
                    Cell cell = new Cell();
                    cell.setContents(number.ToString(), false);
                    cells.Add(name, cell);

                    listOfCells = new List<string>(GetCellsToRecalculate(name));

                    return listOfCells;
                }
        }

        /// <summary>
        /// If text is null, throws an ArgumentNullException.
        /// 
        /// Otherwise, if name is null or invalid, throws an InvalidNameException.
        /// 
        /// Otherwise, the contents of the named cell becomes text.  The method returns a
        /// list consisting of name plus the names of all other cells whose value depends, 
        /// directly or indirectly, on the named cell.
        /// 
        /// For example, if name is A1, B1 contains A1*2, and C1 contains B1+A1, the
        /// list {A1, B1, C1} is returned.
        /// </summary>
        protected override IList<string> SetCellContents(string name, string text)
        {
            name = Normalize(name);

            List<string> listOfCells = new List<string>();

            // Used to keep track of the previous contents of the cell
            Formula previousString;

            if (text == "")
            {
                return listOfCells;
            }

                // If the cell has been previously instantiated
                if (cells.ContainsKey(name))
                {
                    cells.TryGetValue(name, out Cell cell);

                    if (cell.getIsFormula())
                    {
                        previousString = new Formula(cell.getContents().Substring(1, cell.getContents().Length - 1), Normalize, IsValid);
                    }

                    else
                    {
                        previousString = new Formula(cell.getContents());
                    }

                    if (previousString.ToString() != text)
                    {
                        cell.setContents(text, false);

                        // Removes previous dependencies
                        foreach (String s in previousString.GetVariables())
                        {
                            dependencies.RemoveDependency(name, s);
                        }

                        listOfCells = new List<string>(GetCellsToRecalculate(name));

                        return listOfCells;
                    }

                    else
                    {
                        return new List<string>();
                    }
                    
                }

                // If the cell was not previously instantiated
                else
                {
                    Cell cell = new Cell();
                    cell.setContents(text, false);
                    cells.Add(name, cell);

                    listOfCells = new List<string>(GetCellsToRecalculate(name));

                    return listOfCells;
                }
        }

        /// <summary>
        /// If the formula parameter is null, throws an ArgumentNullException.
        /// 
        /// Otherwise, if name is null or invalid, throws an InvalidNameException.
        /// 
        /// Otherwise, if changing the contents of the named cell to be the formula would cause a 
        /// circular dependency, throws a CircularException, and no change is made to the spreadsheet.
        /// 
        /// Otherwise, the contents of the named cell becomes formula.  The method returns a
        /// list consisting of name plus the names of all other cells whose value depends,
        /// directly or indirectly, on the named cell.
        /// 
        /// For example, if name is A1, B1 contains A1*2, and C1 contains B1+A1, the
        /// list {A1, B1, C1} is returned.
        /// </summary>
        protected override IList<string> SetCellContents(string name, Formula formula)
        {
            name = Normalize(name);

            List<string> listOfCells = new List<string>();

            // This string keeps track of the last formula stored here, to use in reversion of a CircularException
            Formula previousString;

            // This list of strings keeps track of the previous dependents stored here, to use in reversion of a CircularException
            List<string> previousDependents;

                // If a cell with this name has been created already
                if (cells.ContainsKey(name))
                {
                    previousDependents = new List<string>(dependencies.GetDependents(name));

                    cells.TryGetValue(name, out Cell cell);

                    if (cell.getIsFormula())
                    {
                        previousString = new Formula(cell.getContents().Substring(1, cell.getContents().Length - 1), Normalize, IsValid);
                    }

                    else
                    {
                        previousString = new Formula(cell.getContents());
                    }

                    if ("=" + previousString.ToString() != formula.ToString())
                    {
                        cell.setContents("=" + formula.ToString(), true);

                        // Removes previous dependencies
                        foreach (String s in previousString.GetVariables())
                        {
                            dependencies.RemoveDependency(name, s);
                        }

                        // Adds new dependencies
                        foreach (String s in formula.GetVariables())
                        {
                            dependencies.AddDependency(name, s);
                        }

                        try
                        {
                            listOfCells = new List<string>(GetCellsToRecalculate(name));
                        }

                        // If a CircularException is thrown
                        catch
                        {

                        cell.setContents(previousString.ToString(), cell.wasPreviousContentsFormula());

                            // Removes the newly added dependencies
                            foreach (String s in formula.GetVariables())
                            {
                                dependencies.RemoveDependency(name, s);
                            }

                            // Adds back the previous dependencies
                            foreach (String s in previousDependents)
                            {
                                dependencies.AddDependency(name, s);
                            }
                            throw new CircularException();
                        }
                        return listOfCells;
                    }

                    else
                {
                    return new List<string>();
                }
                    
                }

                // If the cell was not previously instantiated
                else
                {
                    Cell cell = new Cell();
                    cell.setContents("=" + formula.ToString(), true);
                    cells.Add(name, cell);

                    // Removes previous dependencies
                    foreach (String s in formula.GetVariables())
                    {
                        dependencies.AddDependency(name, s);
                    }

                    try
                    {
                        listOfCells = new List<string>(GetCellsToRecalculate(name));
                    }

                    catch
                    {
                        // Removes the newly added dependencies
                        foreach (String s in formula.GetVariables())
                        {
                            dependencies.RemoveDependency(name, s);
                        }

                        cell.setContents("", false);
                        cells.Remove(name);
                        throw new CircularException();
                    }
                    return listOfCells;
                }
        }

        // ADDED FOR PS5
        /// <summary>
        /// If content is null, throws an ArgumentNullException.
        /// 
        /// Otherwise, if name is null or invalid, throws an InvalidNameException.
        /// 
        /// Otherwise, if content parses as a double, the contents of the named
        /// cell becomes that double.
        /// 
        /// Otherwise, if content begins with the character '=', an attempt is made
        /// to parse the remainder of content into a Formula f using the Formula
        /// constructor.  There are then three possibilities:
        /// 
        ///   (1) If the remainder of content cannot be parsed into a Formula, a 
        ///       SpreadsheetUtilities.FormulaFormatException is thrown.
        ///       
        ///   (2) Otherwise, if changing the contents of the named cell to be f
        ///       would cause a circular dependency, a CircularException is thrown,
        ///       and no change is made to the spreadsheet.
        ///       
        ///   (3) Otherwise, the contents of the named cell becomes f.
        /// 
        /// Otherwise, the contents of the named cell becomes content.
        /// 
        /// If an exception is not thrown, the method returns a list consisting of
        /// name plus the names of all other cells whose value depends, directly
        /// or indirectly, on the named cell. The order of the list should be any
        /// order such that if cells are re-evaluated in that order, their dependencies 
        /// are satisfied by the time they are evaluated.
        /// 
        /// For example, if name is A1, B1 contains A1*2, and C1 contains B1+A1, the
        /// list {A1, B1, C1} is returned.
        /// </summary>
        public override IList<string> SetContentsOfCell(string name, string content)
        {
            if (name != null)
            {
                name = Normalize(name);

                // If the cell name passes the validator
                if (isValidVariable(name) && IsValid(name))
                {
                    if (content != null)
                    {
                        // If the content of the cell is a double
                        if (Double.TryParse(content, out double contentDouble))
                        {
                            IList<string> valuesToRecalculate = SetCellContents(name, contentDouble);

                            // Updates values of every dependent
                            foreach (string cellName in valuesToRecalculate)
                            {
                                cells.TryGetValue(cellName, out Cell cellToUpdate);

                                try
                                {
                                    // Updates values of every dependent
                                    cellToUpdate.setValue(updateCellValues(cellName));
                                }

                                catch (ArgumentException)
                                {
                                    cellToUpdate.setValue(new FormulaError());
                                }
                            }

                            isChanged = true;

                            return valuesToRecalculate;
                        }

                        // If the content is a formula
                        else if (content.StartsWith("="))
                        {
                            IList<string> valuesToRecalculate = SetCellContents(name, new Formula(content.Substring(1, content.Length - 1), Normalize, IsValid));

                            foreach (string cellName in valuesToRecalculate)
                            {
                                cells.TryGetValue(cellName, out Cell cellToUpdate);

                                    try
                                    {
                                        // Updates values of every dependent
                                        cellToUpdate.setValue(updateCellValues(cellName));
                                    }

                                    catch (ArgumentException)
                                    {
                                        cellToUpdate.setValue(new FormulaError());
                                    }
                            }

                            isChanged = true;

                            return valuesToRecalculate;
                        }

                        // If the content is a string
                        else
                        {
                            IList<string> valuesToRecalculate = SetCellContents(name, content);

                            // Updates values of every dependent
                            foreach (string cellName in valuesToRecalculate)
                            {
                                cells.TryGetValue(cellName, out Cell cellToUpdate);

                                try
                                {
                                    // Updates values of every dependent
                                    cellToUpdate.setValue(updateCellValues(cellName));
                                }

                                catch (ArgumentException)
                                {
                                    cellToUpdate.setValue(new FormulaError());
                                }
                            }

                            isChanged = true;

                            return valuesToRecalculate;
                        }

                    }

                    else
                    {
                        throw new ArgumentNullException("Content was null!");
                    }
                }

                else
                {
                    throw new InvalidNameException();
                }
            }

            else
            {
                throw new InvalidNameException();
            }
        }

        /// <summary>
        /// Returns an enumeration, without duplicates, of the names of all cells whose
        /// values depend directly on the value of the named cell.  In other words, returns
        /// an enumeration, without duplicates, of the names of all cells that contain
        /// formulas containing name.
        /// 
        /// For example, suppose that
        /// A1 contains 3
        /// B1 contains the formula A1 * A1
        /// C1 contains the formula B1 + A1
        /// D1 contains the formula B1 - C1
        /// The direct dependents of A1 are B1 and C1
        /// </summary>
        protected override IEnumerable<string> GetDirectDependents(string name)
        {
            name = Normalize(name);

            return dependencies.GetDependees(name);
        }

        /// <summary>
        /// EDITED FOR PS5 TO MATCH SPECS
        /// A method used to check if a given token is a valid variable. If not, can assist in throwing a FormulaFormatException.
        /// </summary>
        /// <param name="tokenToCheck"></param>
        /// <returns></returns>
        private bool isValidVariable(string tokenToCheck)
        {
            // If the first character is not a letter
            if (!char.IsLetter(tokenToCheck[0]))
            {
                return false;
            }

            // If any characters after are not digits or letters
            for (int i = 0; i < tokenToCheck.Length; i++)
            {
                if (!char.IsLetterOrDigit(tokenToCheck[i]))
                {
                    return false;
                }
            }
            return true;
        }

        /// <summary>
        /// This method is used as a helper method to determine the values of every cell that needs updating after the contents of a cell have been changed.
        /// </summary>
        /// <param name="cellToCheck"></param>
        /// <returns></returns>
        private object updateCellValues(string cellToCheck)
        { 
            cells.TryGetValue(cellToCheck, out Cell cell);

            string cellContents = cell.getContents();

            // If the content is a double, the value is that double
            if (Double.TryParse(cell.getContents(), out double result))
            {
                return result;
            }

            // If the content is a string, returns that string
            else if (!cellContents.StartsWith("="))
            {
                return cellContents;
            }

            // If the contents is a formula
            else
            {
                Lookup lookupDelegate = new Lookup(lookupMethod);
                Func<string, double> newFunc = new Func<string, double>(lookupDelegate);

                Formula formulaToEval = new Formula(cell.getContents().Substring(1, cell.getContents().Length - 1), Normalize, IsValid);

                object cellValue = "";

                // Evaluates the new formula
                cellValue = formulaToEval.Evaluate(newFunc);

                // If the formula evaluates to a double, the value becomes that formula
                if (cellValue is double)
                {
                    return (Double)cellValue;
                }

                // If not, then the value is a FormulaError
                else
                {
                    throw new ArgumentException();
                }
            }
        }

        /// <summary>
        /// This method is used as a helper method to determine the values of every cell that needs updating after the contents of a cell have been changed. This function also functions as
        /// the lookup function used by the Evaluate method of the Formula class.
        /// </summary>
        /// <param name="cellToCheck"></param>
        /// <returns></returns>
        private double lookupMethod(string cellToCheck)
        {
            cells.TryGetValue(cellToCheck, out Cell cell);

            string cellContents = cell.getContents();

            // If the content is a double, the value is that double
            if (Double.TryParse(cell.getContents(), out double result))
            {
                return result;
            }

            // If not a double
            else
            {
                Lookup lookupDelegate = new Lookup(lookupMethod);
                Func<string, double> newFunc = new Func<string, double>(lookupDelegate);

                Formula formulaToEval = new Formula(cell.getContents().Substring(1, cell.getContents().Length - 1), Normalize, IsValid);

                object cellValue = "";

                // Evaluates the new formula
                cellValue = formulaToEval.Evaluate(newFunc);

                // If the formula evaluates to a double, the value becomes that formula
                if (cellValue is double)
                {
                    return (Double)cellValue;
                }

                // If not, then the value is a FormulaError
                else
                {
                    throw new ArgumentException();
                }
            }
        }
    }
}
