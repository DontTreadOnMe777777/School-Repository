package cs1410;

import java.util.*;
import java.math.*;

/**
 * A collection of methods for the second assignment of CS 1410.
 * 
 * @author Brandon Walters
 */
public class MethodLibrary
{
    /**
     * You can use this main method for experimenting with your methods if you like, but it is not part of the
     * assignment. You might find it easier to experiment using JShell.
     */
    public static void main (String[] args)
    {
    }

    /**
     * Returns the nth root of x, where n is positive. For example, nthRoot(27.0, 3) is 3.0 (the cube root of 27), and
     * nthRoot(64.0, 6) is 2.0. NOTE: A small amount of roundoff error is acceptable.
     * 
     * The number x is required to have a real-valued nth root, and n is required to be positive. If this requirement is
     * violated, the behavior of the method is undefined (it does not matter what it does).
     */
    public static double nthRoot (double x, int n)
    {
        return Math.pow(x, 1.0 / n);
    }

    /**
     * Reports whether or not c is a vowel ('a', 'e', 'i', 'o', 'u' or the upper-case version). For example,
     * isVowel('a') and isVowel('U') are true; isVowel('x') and isVowel('H') are false.
     */
    public static boolean isVowel (char c)
    {
        if (c == 'A' || c == 'a' || c == 'E' || c == 'e' || c == 'I' || c == 'i' || c == 'O' || c == 'o' || c == 'U'
                || c == 'u')
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Reports whether or not number is a multiple of both factor1 and factor2. For example, isMultipleOf(15, 3, 5) is
     * true and isMutipleOf(27, 3, 4) is false.
     * 
     * Both factor1 and factor2 are required to be non-zero. If this requirement is violated, the behavior of the method
     * is undefined (it does not matter what it does).
     */
    public static boolean isMultipleOf (int number, int factor1, int factor2)
    {
        if (number % factor1 == 0 && number % factor2 == 0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Returns the string that results from capitalizing the first character of s, which is required to have at least
     * one character. For example, capitalize("hello") is "Hello" and capitalize("Jack") is "Jack".
     * 
     * The string s is required to be non-empty. If this requirement is violated, the behavior of the method is
     * undefined (it does not matter what it does).
     * 
     * IMPLEMENTATION HINT: The Character.toUpperCase() method will be helpful. The method s.substring() [where s is a
     * String] will also be helpful. Learn more about them before starting!
     */
    public static String capitalize (String s)
    {
        String capitalizedS = s.substring(0, 1).toUpperCase();
        String capitalizedString = capitalizedS + s.substring(1);
        return capitalizedString;
    }

    /**
     * Returns a new string that (1) begins with all the characters of original that come after the first occurrence of
     * target and (2) ends with all the characters of original that come before the first occurrence of pattern. For
     * example, flip("abcdefg", 'd') is "efgabc", flip("ababad", 'b') is "abada", and flip("x", 'x') is "".
     * 
     * The string original is required to contain the character target. If this requirement is violated, the behavior of
     * the method is undefined (it does not matter what it does).
     * 
     * IMPLEMENTATION HINT: The methods s.indexOf() and s.substring() [where s is a String] will be helpful.
     */
    public static String flip (String original, char target)
    {
        int targetLocation = original.indexOf(target);
        String beforeLocation = original.substring(0, targetLocation);
        String afterLocation = original.substring(targetLocation + 1);
        return afterLocation + beforeLocation;
    }

    /**
     * Returns a new string that is just like s except all of the lower-case vowels ('a', 'e', 'i', 'o', 'u') have been
     * capitalized. For example, capitalizeVowels("hello") is "hEllO", capitalizeVowels("String") is "StrIng", and
     * capitalizeVowels("nth") is "nth".
     * 
     * IMPLEMENTATION HINT: The method s.replace() [where s is a String] will be helpful.
     */
    public static String capitalizeVowels (String s)
    {
        s = s.replace('a', 'A');
        s = s.replace('e', 'E');
        s = s.replace('i', 'I');
        s = s.replace('o', 'O');
        s = s.replace('u', 'U');
        return s;
    }

    /**
     * Reports whether s1 and s2 end with the same n characters. For example, sameEnding("abcde" "xde", 2) is true and
     * sameEnding("abcde", "xde", 3) is false.
     * 
     * The value of n is required to be non-negative, and the two strings must each contain at least n characters. If
     * this requirement is violated, the behavior of the method is undefined (it does not matter what it does).
     * 
     * IMPLEMENTATION HINT: The methods s.length() and s.substring() [where s is a String] will be helpful.
     */
    public static boolean sameEnding (String s1, String s2, int n)
    {
        int s1Length = s1.length();
        int s2Length = s2.length();
        String s1End = s1.substring(s1Length - n);
        String s2End = s2.substring(s2Length - n);
        boolean sameEnd = s1End.equals(s2End);
        return sameEnd;
    }

    /**
     * Returns the value of the largest of the five int literals, separated by white space, that make up the integerList
     * parameter. For example, largestOfFive(" 15 28 -99 62 44 ") is 62.
     * 
     * The string integerList is required to consist of exactly five int literals surrounded by white space. If this
     * requirement is violated, the behavior of the method is undefined (it does not matter what it does).
     * 
     * IMPLEMENTATION HINT: The class java.util.Scanner will be helpful. Use the one-argument constructor that takes a
     * String as a parameter and the nextInt() method.
     */
    public static int largestOfFive (String integerList)
    {
        Scanner intScanner = new Scanner(integerList);
        int integer1 = intScanner.nextInt();
        int integer2 = intScanner.nextInt();
        int integer3 = intScanner.nextInt();
        int integer4 = intScanner.nextInt();
        int integer5 = intScanner.nextInt();
        int max1 = Math.max(integer1, integer2);
        int max2 = Math.max(integer3, integer4);
        int superMax1 = Math.max(max1, max2);
        int finalMax = Math.max(superMax1, integer5);
        return finalMax;
    }

    /**
     * Reports whether or not date1 comes earlier in time than date2. For example, isEarlierThan("12-01-2015",
     * "02-15-2017") is true but isEarlierThan("10-11-2016", "10-11-2016") and isEarlierThan("09-09-1967", "02-15-1933")
     * is false.
     * 
     * The two parameters must be of the form MM-DD-YYYY where YYYY is a year, MM is the two-digit number of a month, DD
     * is a two-digit number of a day, and the entire date is valid. If this requirement is violated, the behavior of
     * the method is undefined (it does not matter what it does).
     * 
     * IMPLEMENTATION HINT: Turn this into a String comparison problem.
     */
    public static boolean isEarlierThan (String date1, String date2)
    {
        int yearDate1 = Integer.parseInt(date1.substring(6));
        int yearDate2 = Integer.parseInt(date2.substring(6));
        int monthDate1 = Integer.parseInt(date1.substring(3, 5));
        int monthDate2 = Integer.parseInt(date2.substring(3, 5));
        int dayDate1 = Integer.parseInt(date1.substring(0, 2));
        int dayDate2 = Integer.parseInt(date2.substring(0, 2));

        if (yearDate1 < yearDate2)
        {
            return true;
        }

        else if (yearDate1 > yearDate2)
        {
            return false;
        }

        else if (yearDate1 == yearDate2)
        {

            if (monthDate1 < monthDate2)
            {
                return true;
            }

            else if (monthDate1 > monthDate2)
            {
                return false;
            }

            else if (dayDate1 == dayDate2)
            {

                if (dayDate1 < dayDate2)
                {
                    return true;
                }

                else if (dayDate1 > dayDate2)
                {
                    return false;
                }

                else if (dayDate1 == dayDate2)
                {
                    return false;
                }

                else
                {
                    return false;
                }

            }
        }
        return false;

    }

    /**
     * Returns the integer numeral that represents the sum of the integer numerals integer1 and integer2. For example,
     * addNumerals("125", "64") is "189". The method works for numerals of any length, including numerals that consist
     * of hundreds or thousands of digits and are far too big to parse as ints or longs.
     * 
     * The two parameters must both be valid integer numerals. That is, both must consist of one or more digits
     * optionally preceded by a + and - sign. If this requirement is violated, the behavior of the method is undefined
     * (it does not matter what it does).
     * 
     * IMPLEMENTATION HINT: The class java.math.BigInteger will be helpful. Use the one-argument constructor that takes
     * a String as a parameter, the add method(), and the toString() method.
     */
    public static String addNumerals (String integer1, String integer2)
    {
        BigInteger bInteger1, bInteger2, bSum;
        bInteger1 = new BigInteger(integer1);
        bInteger2 = new BigInteger(integer2);
        bSum = bInteger1.add(bInteger2);
        String sumString = bSum.toString();
        return sumString;
    }
}
