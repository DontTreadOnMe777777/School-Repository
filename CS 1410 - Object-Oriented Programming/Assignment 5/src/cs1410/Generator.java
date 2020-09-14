package cs1410;

import cs1410lib.Dialogs;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;
import java.io.IOException;

public class Generator
{

    public static void main (String[] args) throws FileNotFoundException
    {
        try
        {
            String levelInput = Dialogs
                    .showInputDialog("Hello! Please give a positive integer for your level of evaluation.");
            int levelInputInt = Integer.parseInt(levelInput);
            while (levelInputInt < 0)
            {
                levelInput = Dialogs.showInputDialog("Whoops! That wasn't a positive number. Please try again.");
                levelInputInt = Integer.parseInt(levelInput);
            }
            String lengthInput = Dialogs.showInputDialog(
                    "Thank you! How long would you like your output text to be, in characters? \nPlease give a positive number.");
            int lengthInputInt = Integer.parseInt(lengthInput);
            while (lengthInputInt < 0)
            {
                lengthInput = Dialogs.showInputDialog("Whoops! That wasn't a positive number. Please try again.");
                lengthInputInt = Integer.parseInt(lengthInput);
            }
            File text = Dialogs.showOpenFileDialog(
                    "Alright, thank you! Now, please select the text file you would like to randomize from.");
            while (!text.getName().endsWith(".txt"))
            {
                text = Dialogs.showOpenFileDialog("Whoops, that wasn't a text file! Please try a file that ends in .txt.");
            }
            InputStream textInput = new FileInputStream(text);
            Scanner textScnr = new Scanner(textInput);
            Dialogs.showMessageDialog(PS5Library.generateText(textScnr, levelInputInt, lengthInputInt));
        }
        catch (FileNotFoundException e)
        {
            throw new FileNotFoundException("No file found.");
        }
        catch (NumberFormatException e)
        {
            Dialogs.showMessageDialog("We ran into a problem with that input. The program will now close.\nHave a nice day!");
            throw new NumberFormatException("Number too large/Non-number entered.");
        }
        catch (IllegalArgumentException e)
        {
            Dialogs.showMessageDialog(
                    "Well, this is awkward! The text file was too short, program will now end. \nHave a nice day!");
            throw new IllegalArgumentException("Text file too short.");
        }
    }

}
