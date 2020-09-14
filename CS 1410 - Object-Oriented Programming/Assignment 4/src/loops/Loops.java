package loops;

import java.util.Scanner;

/**
 * This class contains 5 methods, which are all using optimization or accumulation loops to perform their functions.
 * 
 * @author Brandon Walters
 *
 */

public class Loops
{

    public static void main (String[] args)
    {
    }

    /**
     * 
     * This method, containsToken, takes tokens from an input string, s, and compares them to another token, t. If the
     * two tokens match, containsToken returns true. If not, containsToken returns false.
     */
    public static boolean containsToken (String s, String t)
    {
        Scanner sScnr = new Scanner(s);
        boolean isEqual = false;

        while (sScnr.hasNext())
        {
            String sNextToken = sScnr.next();
            isEqual = (sNextToken.equals(t));

            if (isEqual == true)
            {
                return isEqual;
            }
        }
        return isEqual;
    }

    /**
     * 
     * This method, findLineWithToken, scans a string to take a line of tokens. It then calls the containsToken method
     * to see if the line being checked contains the token. If true, it returns the line where the token was found. If
     * not, it returns an empty string.
     */

    public static String findLineWithToken (Scanner scn, String t)
    {
        String stringToCheck;
        while (scn.hasNextLine())
        {
            stringToCheck = scn.nextLine();
            containsToken(stringToCheck, t);
            if (containsToken(stringToCheck, t) == true)
            {
                return stringToCheck;
            }
        }
        return "";
    }

    /**
     * 
     * This method, isPalindrome, turns an input string into a character array. It then compares the character at the
     * beginning of the string to the one at the end, and then works its way towards the middle of the string. If all
     * corresponding letters match, then the method returns true. If not, then the method returns false.
     */
    public static boolean isPalindrome (String s)
    {
        char[] charArray = s.toCharArray();
        int stringBeginning = 0;
        int stringEnd = s.length() - 1;
        while (stringEnd > stringBeginning)
        {
            if (charArray[stringBeginning] != charArray[stringEnd])
            {
                return false;
            }

            stringBeginning++;
            stringEnd--;
        }
        return true;
    }

    /**
     * 
     * This method, findLongestPalindrome, takes a scanner input and then calls on the isPalindrome method to find
     * palindromes in the string that was scanned. If one is found, the length of the palindrome is taken and, if it is
     * longer than the current longest stored length, sets that length as the new maximum length. If no palindromes are
     * found, the method returns 0.
     */

    public static String findLongestPalindrome (Scanner scn)
    {
        String stringToCheck = "";
        String currentMax = "";
        int currentMaxLength = 0;
        while (scn.hasNext())
        {
            stringToCheck = scn.next();
            int stringToCheckLength = stringToCheck.length();
            boolean isPalindrome = isPalindrome(stringToCheck);

            if (isPalindrome == true && stringToCheckLength > currentMaxLength)
                currentMax = stringToCheck;
            currentMaxLength = stringToCheckLength;
        }
        return currentMax;
    }

    /**
     * 
     * This method, findWhitespace, is used by findMostWhitespace to scan input strings for whitespaces.
     */
    public static int findWhitespace (Scanner scn)
    {
        String lineToCheck = scn.nextLine();
        int whitespaceCount = 0;
        for (int i = 0; i < lineToCheck.length(); i++)
        {
            if (Character.isWhitespace(i))
            {
                whitespaceCount++;
            }

            else
            {
            }
        }
        return whitespaceCount;
    }

    /**
     * 
     * This method, findMostWhitespace, takes an input scanner and then uses the method findWhitespace to count the
     * number of whitespaces in a given line. If that number is larger than the current maximum, then that value is
     * stored as the new maximum. If no whitespaces are found, the method returns -1.
     */
    public static int findMostWhitespace (Scanner scn)
    {
        int currentMax = -1;
        while (scn.hasNextLine())
        {
            String lineToCheck = scn.toString();
            Scanner lineScanner = new Scanner(lineToCheck);
            int whitespaceForLine = findWhitespace(lineScanner);

            if (whitespaceForLine > currentMax)
            {
                currentMax = whitespaceForLine;
            }

            lineToCheck = scn.nextLine();
        }
        return currentMax;
    }
}
