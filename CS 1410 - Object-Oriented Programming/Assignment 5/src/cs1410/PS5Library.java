package cs1410;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Scanner;
import java.lang.IllegalArgumentException;

/**
 * A library of methods for implementing the random text generation algorithm described in PS5, Fall 2017.
 * 
 * @author Brandon Walters and Joe Zachary
 */
public class PS5Library
{
    /**
     * Demonstrates the use of the generateText method.
     */
    public static void main (String[] args) throws IOException
    {
        // You won't need to use this feature in PS5, but this shows how to include a resource (in this
        // case a text file) as part of a project. I created a package called "books", then put two
        // text files into the package. I was then able to open one of those files as shown below. When
        // I export the project, the resources go along with it.
        try (InputStream book = PS5Library.class.getResourceAsStream("/books/PrideAndPrejudice.txt");
                Scanner input = new Scanner(book))
        {
            System.out.println(generateText(input, 6, 100));
        }
    }

    /**
     * 
     * This method, scannerToString, takes a scanner as input and uses a StringBuilder to quickly build a string of
     * text.
     */

    public static String scannerToString (Scanner toStringScnr)
    {
        StringBuilder scannerToStringOutput = new StringBuilder();
        while (toStringScnr.hasNextLine())
        {
            String nextLine = toStringScnr.nextLine();
            scannerToStringOutput.append(nextLine);
            scannerToStringOutput.append("\n");
        }
        if (scannerToStringOutput.length() == 0)
        {
            scannerToStringOutput.append("\n");
        }
        return scannerToStringOutput.toString();
    }

    /**
     * 
     * This method, chooseSubstring, takes a string of text, a specified length, and a random number, and uses it to
     * choose a random substring of text from the text string. If the length specified is less than 0, if the length
     * specified is larger than the text itself, or if the text is equal to or less than 0, the method throws an
     * IllegalArgumentException.
     */

    public static String chooseSubstring (String text, int length, Random rand)
    {
        int randInt = rand.nextInt(text.length());
        if (length < 0 || length > text.length() || text.length() <= 0)
        {
            throw new IllegalArgumentException();
        }
        while (randInt + length > text.length())
        {
            randInt = rand.nextInt(text.length());
        }
        String randomizedStringOutput = text.substring(randInt, randInt + length);
        return randomizedStringOutput;
    }

    /**
     * 
     * This method, getCharsThatFollowPattern, takes the character immediately following a specified pattern in a text
     * input. If the pattern occurs at the very end of the string, a NoSuchElementException is thrown.
     */

    public static ArrayList<Character> getCharsThatFollowPattern (String text, String pattern)
    {
        ArrayList<Character> arrayOfChars = new ArrayList<Character>();
        int previousIndex = 0;
        while (text.indexOf(pattern, previousIndex) != -1)
        {
            int newIndex = text.indexOf(pattern, previousIndex) + pattern.length();
            if (newIndex >= text.length())
            {
                break;
            }
            char nextChar = text.charAt(newIndex);
            previousIndex = newIndex - pattern.length() + 1;
            arrayOfChars.add(nextChar);
        }
        return arrayOfChars;
    }

    /**
     * 
     * This method, pickCharThatFollowsPattern, chooses one of the characters from after the pattern by using the
     * getCharsThatFollowPattern method, and then uses a random number generator to pick one of the characters from the array
     * returned. If there are no numbers in the array, a NoSuchElementException is thrown.
     */

    public static char pickCharThatFollowsPattern (String text, String pattern, Random rand)
    {
        try
        {
            ArrayList<Character> arrayOfChars = new ArrayList<Character>();
            arrayOfChars = getCharsThatFollowPattern(text, pattern);
            int numOfChars = arrayOfChars.size();
            if (numOfChars == 0)
            {
                throw new NoSuchElementException();
            }
            int randInt = rand.nextInt(numOfChars);
            return arrayOfChars.get(randInt);
        }
        catch (NoSuchElementException exception)
        {
            throw new NoSuchElementException("No numbers in array.");
        }
    }

    /**
     * Uses all the text in the input to generate and return random text with the specified level and length, using the
     * algorithm described in PS5, CS 1410, Fall 2018.
     * 
     * @throws IllegalArgumentException if level < 0, or length < 0, or there are less than level+1 chars in the input.
     */
    public static String generateText (Scanner input, int level, int length)
    {
        // Validate the parameters
        if (level < 0 || length < 0)
        {
            throw new IllegalArgumentException();
        }

        // Grab all the text from the Scanner and make sure it is long enough.
        String text = scannerToString(input);
        if (level >= text.length())
        {
            throw new IllegalArgumentException();
        }

        // Create a random number generator to pass to the methods that make random choices
        Random rand = new Random();

        // Get the initial pattern.
        String pattern = chooseSubstring(text, level, rand);

        // Build up the final result one character at a time. We use a StringBuilder because
        // it is more efficient than using a String when doing long sequences of appends.
        StringBuilder result = new StringBuilder();
        while (result.length() < length)
        {
            try
            {
                // Pick at random a character that follows the pattern in the text and append it
                // to the result. If there is no such character (which can happen if the pattern
                // occurs only once, at the very end of text), the method we're calling will throw
                // a NoSuchElementException, which is caught below.
                char newChar = pickCharThatFollowsPattern(text, pattern, rand);
                result.append(newChar);

                // Update the pattern by removing its first character and adding on the new
                // character. The length of the pattern remains the same. If the pattern is
                // the empty string, though, it never changes.)
                if (pattern.length() > 0)
                {
                    pattern = pattern.substring(1) + newChar;
                }
            }
            catch (NoSuchElementException e)
            {
                // It is possible to get stuck if the pattern occurs only once in the text and
                // that occurrence is at the very end of the text. In this case, we pick a new
                // seed and keep going.
                pattern = chooseSubstring(text, level, rand);
            }
        }

        // Return the string we've accumulated.
        return result.toString();
    }
}
