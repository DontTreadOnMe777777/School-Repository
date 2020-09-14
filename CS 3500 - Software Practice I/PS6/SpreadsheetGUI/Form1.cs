// Written by Brandon Walters, 10/4/19

using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Diagnostics;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using SpreadsheetUtilities;
using SS;

namespace SpreadsheetGUI
{
    public partial class Form1 : Form
    {
        // The backing spreadsheet
        private AbstractSpreadsheet spreadsheet;

        // Used to enforce the cell name validity requirements for PS6
        private delegate bool isValid(string cellName);
        Func<string, bool> newFunc;

        public Form1()
        {
            // A delegate is declared to enforce the cell name validity requirements for PS6.
            isValid validDelegate = new isValid(isValidCellName);
            newFunc = new Func<string, bool>(validDelegate);

            InitializeComponent();

            // This constructor uses the defined isValid delegate, as well as the normalization of all cell names to uppercase and the version string of "ps6".
            spreadsheet = new Spreadsheet(newFunc, s => s.ToUpper(), "ps6");

            // This an example of registering a method so that it is notified when
            // an event happens.  The SelectionChanged event is declared with a
            // delegate that specifies that all methods that register with it must
            // take a SpreadsheetPanel as its parameter and return nothing.  So we
            // register the displaySelection method below.

            // This could also be done graphically in the designer, as has been
            // demonstrated in class.
            spreadsheetPanel1.SelectionChanged += displaySelection;
            cellNameTextBox.Text = "Cell Name: A1";
        }

        /// <summary>
        /// Used to change the cell's contents when the button is pushed.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void setContentsButton_Clicked(object sender, EventArgs e)
        {
            // Disable the button
            setContentsButton.Enabled = false;

            // Get new contents
            string contentsToSet = setContentsTextBox.Text;
            spreadsheetPanel1.GetSelection(out int col, out int row);

            // Get the correct cell to change
            string column = Convert.ToChar(col + 65).ToString();
            string cellName = column + (row + 1).ToString();
            try
            {
                spreadsheet.SetContentsOfCell(cellName, contentsToSet);
            }
            
            // If an error is thrown, displays an error message and the spreadsheet does not change
            catch
            {
                DialogResult dialog = MessageBox.Show("These contents are invalid! Check the contents!", "Invalid contents!", MessageBoxButtons.OK);
            }

            // Update the cell value in the grid and the text box
            cellValueTextBox.Text = "Cell Value: " + spreadsheet.GetCellValue(cellName);
            spreadsheetPanel1.SetValue(col, row, spreadsheet.GetCellValue(cellName).ToString());

            // Re-enable the button
            setContentsButton.Enabled = true;
        }

        /// <summary>
        /// Used from the demo to switch between cells when the user clicks on a new cell, and updates the text boxes appropriately.
        /// </summary>
        /// <param name="ss"></param>
        private void displaySelection(SpreadsheetPanel ss)
        {
            int row, col;
            String value;
            ss.GetSelection(out col, out row);
            ss.GetValue(col, row, out value);

            // Gets the proper cell to update the text boxes when a new cell is selected
            string column = Convert.ToChar(col + 65).ToString();
            string cellName = column + (row + 1).ToString();
            cellNameTextBox.Text = "Cell Name: " + cellName;
            cellValueTextBox.Text = "Cell Value: " + value;
            setContentsTextBox.Text = spreadsheet.GetCellContents(cellName).ToString();
        }

        /// <summary>
        /// Used to enable or disable the content button.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void setContentsTextBox_Changed(object sender, EventArgs e)
        {
            // If the contents text box is empty, the button is disabled
            if (setContentsTextBox.Text != "")
            {
                setContentsButton.Enabled = true;
            }

            else
            {
                setContentsButton.Enabled = false;
            }
        }

        /// <summary>
        /// Used whenever the New option is selected
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void newSpreadsheetButton_Clicked(object sender, EventArgs e)
        {
            // Tell the application context to run the form on the same
            // thread as the other forms.
            DemoApplicationContext.getAppContext().RunForm(new Form1());
        }

        /// <summary>
        ///  Used whenever the help button is clicked
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void helpButton_Clicked(object sender, EventArgs e)
        {
            // TODO: Get file path fixed for teacher's comp
            //Process.Start("C:/Users/Backe/source/repos/Projects/PS6/Resources/README.txt");
            DialogResult dialog = MessageBox.Show("Howdy! Welcome to the Help section of my spreadsheet! This page is designed to help you utilize and understand my spreadsheet GUI. \n\n\nAt the top of the window is a menu containing several options. \n\nThe first option is New, which creates a new Spreadsheet window when pressed. \n\nThe next button is the Save button. Users are brought into a file saving menu when this button is pressed, which defaults to saving the spreadsheet as a spreadsheet file with the .sprd extension, but which can be changed to allow for saving as any file type. If any errors are encountered with the saving operation, an error message is displayed to the user and the spreadsheet remains unchanged and unsaved. \n\nThe next tab is the Open tab, which takes the users to another file selection menu to choose a file to attempt to open as a spreadsheet. Once again, the search defaults to looking for spreadsheets with the .sprd extension, but this can be changed to allow for searching of any file type. If an error occurs, a message is shown to the user and the spreadsheet remains unchanged. If an opening would result in the loss of unsaved data from the previous spreadsheet, a warning message is shown to the user, who can choose to go ahead anyways. \n\nThe next tab is the Close tab, which closes the given window. This does NOT close the entire application. If the closing of the spreadsheet would result in lost data, a warning is shown to the user, who can choose to close anyways. \nThe final tab is the Help tab, which opens this file for any users to read and better understand how to use the spreadsheet. \n\n\nThe bar below the menu tab contains a blank and button for the contents of the selected cell, which can be changed by inputting the new contents of the cell and then pressing the Set Contents button. This will store the contents given in the cell highlighted by the user, as well as calculating and displaying the cell value in the next text over, labeled Cell Value. \n\nThe next text is the Cell Name field, which displays the name of the currently highlighted cell. \n\nThe final text and field is the Cell Search, which is my unique addition to this spreadsheet assignment. There is a field and button. When the user types something into the search bar and hits the search button, three things can happen. If the cell name given is a valid cell name and it has contents, a message is shown that the cell exists in the spreadsheet and is non-empty. If the cell name given is valid, but the cell has no contents, a message is shown that the cell exists in the spreadsheet, but it is currently empty. If the cell name given is not a valid cell for the spreadsheet, a meaasge is shown that the cell does not exist in the spreadsheet, and to re-examine the cell name that the user typed. \n\n\nThe rest of the program GUI is used by the spreadsheet itself. It is resizeable and controlled with the two scroll bars. To select a cell, the user must click on it with the mouse. The selected cell is shown with a highlight around the cell borders, and the default cell that is selected upon creation of a spreadsheet is the first cell, A1. If a cell has contents, then the calculated value is displayed in the grid, inside the cell given. \n\n\nThanks for reading!", "Help", MessageBoxButtons.OK);
        }

        /// <summary>
        /// Used whenever the close button is clicked
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void closeButton_Clicked(object sender, EventArgs e)
        {
            // If the spreadsheet has been changed without saving, gives a warning to the user
            if (spreadsheet.Changed == true)
            {
                DialogResult dialog = MessageBox.Show("The spreadsheet has not been saved! Changes could be lost! Are you sure?", "Unsaved changes!", MessageBoxButtons.YesNo);
                
                if (dialog == DialogResult.Yes)
                {
                    Close();
                }

                else if (dialog == DialogResult.No)
                {

                }
            }

            else
            {
                Close();
            }
        }
        /// <summary>
        /// Used whenever the Save button is selected to save a file.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void saveButton_Clicked(object sender, EventArgs e)
        {
            // Opens a SaveFileDialog that filters between just Spreadsheet files and all file types
            SaveFileDialog saveDialog = new SaveFileDialog();
            saveDialog.Filter = "Spreadsheet File (*.sprd)|*.sprd|All File Types|*";
            saveDialog.Title = "Save your spreadsheet...";
            saveDialog.ShowDialog();

            // If the file name is not an empty string open it for saving.
            if (saveDialog.FileName != "")
            {
                // If the user selects the Spreadsheet files filter
                if (saveDialog.FilterIndex == 1)
                {
                    // If the user did not already add the Spreadsheet file extension, add it
                    if (!saveDialog.FileName.Contains(".sprd"))
                    {
                        saveDialog.FileName = saveDialog.FileName + ".sprd";
                    }

                    // If the save does not work, throws an error message for the user, and the spreadsheet does not change.
                    try
                    {
                        spreadsheet.Save(saveDialog.FileName);
                    }

                    catch
                    {
                        DialogResult errorDialog = MessageBox.Show("Error saving the spreadsheet file! Check your file for errors!", "Invalid spreadsheet file!", MessageBoxButtons.OK);
                    }
                }

                // If the user selects the all files filter
                else
                {
                    // If the save does not work, throws an error message for the user, and the spreadsheet does not change.
                    try
                    {
                        spreadsheet.Save(saveDialog.FileName);
                    }

                    catch
                    {
                        DialogResult errorDialog = MessageBox.Show("Error saving the spreadsheet file! Check your file for errors!", "Invalid spreadsheet file!", MessageBoxButtons.OK);
                    }
                }
            }
        }

        /// <summary>
        /// Used whenever the Open button is clicked to open a file.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void openButton_Clicked(object sender, EventArgs e)
        {
            int row;
            int column;

            // If the spreadsheet has been changed without saving, throws a warning for the user. The user can bypass this if they wish.
            if (spreadsheet.Changed == true)
            {
                DialogResult dialog = MessageBox.Show("The spreadsheet has not been saved! Changes could be lost! Are you sure?", "Unsaved changes!", MessageBoxButtons.YesNo);

                if (dialog == DialogResult.Yes)
                {
                    // Opens a new OpenFileDialog that filters between Spreadsheet files and all file types
                    OpenFileDialog openDialog = new OpenFileDialog();
                    openDialog.Filter = "Spreadsheet File (*.sprd)|*.sprd|All File Types|*";
                    openDialog.Title = "Open a spreadsheet...";
                    openDialog.ShowDialog();

                    string filePath = openDialog.FileName;

                    // Attempts to create a new spreadsheet with the given file path. If the spreadsheet cannot be created, 
                    // throws an error message for the user and the spreadsheet is not changed.
                    try
                    {
                        spreadsheet = new Spreadsheet(filePath, newFunc, s => s.ToUpper(), "ps6");
                    }

                    catch
                    {
                        DialogResult errorDialog = MessageBox.Show("Error reading the opened spreadsheet file! Check your file for errors!", "Invalid spreadsheet file!", MessageBoxButtons.OK);
                    }
                }

                else if (dialog == DialogResult.No)
                {

                }
            }

            else
            {
                // Opens a new OpenFileDialog that filters between Spreadsheet files and all file types
                OpenFileDialog openDialog = new OpenFileDialog();
                openDialog.Filter = "Spreadsheet File (*.sprd)|*.sprd|All File Types|*";
                openDialog.Title = "Open a spreadsheet...";
                
                openDialog.ShowDialog();

                string filePath = openDialog.FileName;

                // Attempts to create a new spreadsheet with the given file path. If the spreadsheet cannot be created, 
                // throws an error message for the user and the spreadsheet is not changed.
                try
                {
                    spreadsheet = new Spreadsheet(filePath, newFunc, s => s.ToUpper(), "ps6");
                }

                catch
                {
                    DialogResult errorDialog = MessageBox.Show("Error reading the opened spreadsheet file! Check your file for errors!", "Invalid spreadsheet file!", MessageBoxButtons.OK);
                }
            }

            // For each cell in the new spreadsheet, places it in the grid and updates the GUI to match its value and contents
            foreach (string s in spreadsheet.GetNamesOfAllNonemptyCells())
            {
                column = ((int) s[0]) - 65;
                row = Convert.ToInt32(s.Substring(1, s.Length - 1)) - 1;

                spreadsheetPanel1.SetValue(column, row, spreadsheet.GetCellValue(s).ToString());
                cellValueTextBox.Text = "Cell Value: " + spreadsheet.GetCellValue(s).ToString();
                setContentsTextBox.Text = spreadsheet.GetCellContents(s).ToString();
            }

            // Resets the selection and GUI back to the default A1 cell
            spreadsheetPanel1.SetSelection(0, 0);
            cellValueTextBox.Text = "Cell Value: " + spreadsheet.GetCellValue("A1").ToString();
            setContentsTextBox.Text = spreadsheet.GetCellContents("A1").ToString();

            
        }

        /// <summary>
        /// Used as a helper method to determine the validity of a given cell name, in accordance with the PS6 specification restrictions.
        /// </summary>
        /// <param name="name"></param>
        /// <returns></returns>
        private bool isValidCellName(string name)
        {
            if (Char.IsLetter(name[0]))
            {
                // For cells like A1
                if (name.Length == 2)
                {
                    if (Char.IsDigit(name[1]))
                    {
                        return true;
                    }

                    else
                    {
                        return false;
                    }
                }

                // For cells like Z99
                else if (name.Length == 3)
                {
                    if (Char.IsDigit(name[1]) && Char.IsDigit(name[2]))
                    {
                        return true;
                    }

                    else
                    {
                        return false;
                    }
                }

                else
                {
                    return false;
                }
            }

            else
            {
                return false;
            }
        }

        /// <summary>
        /// Used if the cell search button is clicked to find the status of the given cell
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void cellSearchButton_Clicked(object sender, EventArgs e)
        {
            cellSearchButton.Enabled = false;

            // If the cell is valid and nonempty
            if (isValidCellName(cellSearchBox.Text) && (string) spreadsheet.GetCellContents(cellSearchBox.Text) != "")
            {
                DialogResult Dialog = MessageBox.Show("This cell does have contents in this spreadsheet!", "Cell has contents!", MessageBoxButtons.OK);
            }

            // If the cell is valid but empty
            else if (isValidCellName(cellSearchBox.Text))
            {
                DialogResult Dialog = MessageBox.Show("This cell does exist, but does not have contents in this spreadsheet!", "Cell does not have contents!", MessageBoxButtons.OK);
            }

            // If the cell is not valid
            else
            {
                DialogResult errorDialog = MessageBox.Show("This cell does not exist in this spreadsheet!", "Invalid cell!", MessageBoxButtons.OK);
            }

            cellSearchButton.Enabled = true;
        }

        /// <summary>
        /// Used to control the enabled status of the cell search button.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void cellSearchTextBox_Changed(object sender, EventArgs e)
        {
            if (cellSearchBox.Text != "")
            {
                cellSearchButton.Enabled = true;
            }

            else 
            {
                cellSearchButton.Enabled = false;
            }
        }
    }

    /// <summary>
    /// Keeps track of how many top-level forms are running. Taken from the demo.
    /// </summary>
    class DemoApplicationContext : ApplicationContext
    {
        // Number of open forms
        private int formCount = 0;

        // Singleton ApplicationContext
        private static DemoApplicationContext appContext;

        /// <summary>
        /// Private constructor for singleton pattern
        /// </summary>
        private DemoApplicationContext()
        {
        }

        /// <summary>
        /// Returns the one DemoApplicationContext.
        /// </summary>
        public static DemoApplicationContext getAppContext()
        {
            if (appContext == null)
            {
                appContext = new DemoApplicationContext();
            }
            return appContext;
        }

        /// <summary>
        /// Runs the form
        /// </summary>
        public void RunForm(Form form)
        {
            // One more form is running
            formCount++;

            // When this form closes, we want to find out
            form.FormClosed += (o, e) => { if (--formCount <= 0) ExitThread(); };

            // Run the form
            form.Show();
        }

    }
}
