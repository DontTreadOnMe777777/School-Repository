package scan;

import java.util.InputMismatchException;
import java.util.NoSuchElementException;

public class MyScanner
{
    private int length; // Length of intake string
    private String string; // String used in methods

    public MyScanner (String intake)
    {
        // Assign values to instance variables
        length = intake.length();
        string = intake;
    }

    /**
     * Finds if the string has a next character that is not whitespace.
     */
    public boolean hasNext ()
    {
        int count = 0;
        boolean hasNextChar = false;
        if (length == 0)
        {
            return hasNextChar;
        }
        while (count < length)
        {
            if (string.charAt(count) != ' ')
            {
                hasNextChar = true;
                break;
            }
            else
            {
                count++;
            }
        }
        return hasNextChar;
    }

    /**
     * Returns next token bounded by whitespace. If no such token exists, throws a NoSuchElementException.
     */
    public String next ()
    {
        int count = 0;
        String nextString = "";
        if (length == 0)
        {
            throw new NoSuchElementException("Scanner is empty.");
        }
        try
        {
            // Adds together characters to form next token
            while (count < length)
            {
                if (string.charAt(count) != ' ')
                {
                    nextString = nextString + string.charAt(count);
                    if (string.charAt(count + 1) == ' ')
                    {
                        break;
                    }
                    count++;
                }
                else
                {
                    count++;
                }
            }
        }
        catch (StringIndexOutOfBoundsException e)
        {
            return nextString;
        }
        return nextString;
    }

    /**
     * Finds if the next token is an integer.
     */
    public boolean hasNextInt ()
    {
        int count = 0;
        boolean isDigit = false;
        while (count < length)
        {
            if (string.charAt(count) != '1' && string.charAt(count) != '2' && string.charAt(count) != '3'
                    && string.charAt(count) != '4' && string.charAt(count) != '5' && string.charAt(count) != '6'
                    && string.charAt(count) != '7' && string.charAt(count) != '8' && string.charAt(count) != '9'
                    && string.charAt(count) != '0' && string.charAt(count) != ' ')
            {
                isDigit = false;
                break;
            }
            else if (string.charAt(count) == ' ')
            {
                count++;
            }
            else
            {
                isDigit = true;
                break;
            }
        }
        return isDigit;
    }

    /**
     * Returns the next integer in the string. If the string is empty, throws a NoSuchElementException.
     */
    public int nextInt ()
    {
        int count = 0;
        int finalInt = 0;
        if (length == 0)
        {
            throw new NoSuchElementException("Scanner is empty.");
        }
        String intToParse = "";
        try
        {
            // Builds integer up if more than one digit
            while (count < length)
            {
                if (string.charAt(count) == '1' || string.charAt(count) == '2' || string.charAt(count) == '3'
                        || string.charAt(count) == '4' || string.charAt(count) == '5' || string.charAt(count) == '6'
                        || string.charAt(count) == '7' || string.charAt(count) == '8' || string.charAt(count) == '9'
                        || string.charAt(count) == '0' || string.charAt(count) == '.')
                {
                    intToParse = intToParse + string.charAt(count);
                    if (string.charAt(count + 1) != '1' && string.charAt(count + 1) != '2'
                            && string.charAt(count + 1) != '3' && string.charAt(count + 1) != '4'
                            && string.charAt(count + 1) != '5' && string.charAt(count + 1) != '6'
                            && string.charAt(count + 1) != '7' && string.charAt(count + 1) != '8'
                            && string.charAt(count + 1) != '9' && string.charAt(count + 1) != '0'
                            && string.charAt(count + 1) != '.')
                    {
                        break;
                    }
                    count++;
                }
                else
                {
                    count++;
                }
            }
        }
        catch (StringIndexOutOfBoundsException e)
        {
            try
            {
                finalInt = Integer.parseInt(intToParse);
                return finalInt;
            }
            // Catches doubles and other incorrect formats
            catch (NumberFormatException e2)
            {
                throw new InputMismatchException("Double instead of int.");
            }
        }

        finalInt = Integer.parseInt(intToParse);
        return finalInt;
    }
}
