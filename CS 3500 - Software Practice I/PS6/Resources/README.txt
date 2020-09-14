Written by Brandon Walters, 10/4/19

Howdy! Welcome to the Help section/README of my spreadsheet! This page is designed to help you utilize and understand my spreadsheet GUI.

At the top of the window is a menu containing several options. The first option is New, which creates a new Spreadsheet window when pressed.

The next button is the Save button. Users are brought into a file saving menu when this button is pressed, 
which defaults to saving the spreadsheet as a spreadsheet file with the .sprd extension, but which can be changed to allow for saving as any file type. 
If any errors are encountered with the saving operation, an error message is displayed to the user and the spreadsheet remains unchanged and unsaved.

The next tab is the Open tab, which takes the users to another file selection menu to choose a file to attempt to open as a spreadsheet. Once again, 
the search defaults to looking for spreadsheets with the .sprd extension, but this can be changed to allow for searching of any file type. 
If an error occurs, a message is shown to the user and the spreadsheet remains unchanged. If an opening would result in the loss of unsaved data from the previous spreadsheet,
a warning message is shown to the user, who can choose to go ahead anyways.

The next tab is the Close tab, which closes the given window. This does NOT close the entire application. If the closing of the spreadsheet would result in lost data,
a warning is shown to the user, who can choose to close anyways.

The final tab is the Help tab, which opens this file for any users to read and better understand how to use the spreadsheet.



The bar below the menu tab contains a blank and button for the contents of the selected cell, which can be changed by inputting the new contents of the cell and then pressing
the Set Contents button. This will store the contents given in the cell highlighted by the user, as well as calculating and displaying the cell value in the next text over, labeled Cell Value.

The next text is the Cell Name field, which displays the name of the currently highlighted cell. 

The final text and field is the Cell Search, which is my unique addition to this spreadsheet assignment. There is a field and button. When the user types something into the search bar and hits the search button,
three things can happen. If the cell name given is a valid cell name and it has contents, a message is shown that the cell exists in the spreadsheet and is non-empty.
If the cell name given is valid, but the cell has no contents, a message is shown that the cell exists in the spreadsheet, but it is currently empty.
If the cell name given is not a valid cell for the spreadsheet, a meaasge is shown that the cell does not exist in the spreadsheet, and to re-examine the cell name that the user typed.



The rest of the program GUI is used by the spreadsheet itself. It is resizeable and controlled with the two scroll bars. To select a cell, the user must click on it with the mouse.
The selected cell is shown with a highlight around the cell borders, and the default cell that is selected upon creation of a spreadsheet is the first cell, A1.
If a cell has contents, then the calculated value is displayed in the grid, inside the cell given.





The internals of the spreadsheet are pretty straightforward. The code used for updating the grid and for the New and Close functions was largely from the demo given in PS6Skeleton. 
Otherwise, the main new addition to the code was a delegate method used to check the strict validity of cell names given for the PS6 specifications. The final addition was my unique addition
to the spreadsheet, which was my Cell Search function, which is a good way to quickly ascertain the status of cells across the spreadsheet, without having to scroll around.

I would like to quickly mention that, unfortunately, I could not get the Coded UI Tests to work with this project, my computer was having issues with the tracking of the mouse and sometimes with the background windows.
I have tried to test extensively with several spreadsheet files built from the PS4/PS5 library, as well as my own writing.

I hope this README/Help file was helpful, and I hope you enjoy using my spreadsheet!