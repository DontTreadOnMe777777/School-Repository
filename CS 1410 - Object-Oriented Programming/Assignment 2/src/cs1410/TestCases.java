package cs1410;

import static org.junit.Assert.*;
import static cs1410.MethodLibrary.*;
import org.junit.Test;

/**
 * Development tests for PS2 of CS1410, Fall 2017.
 * 
 * @author Joe Zachary
 */
public class TestCases
{
    @Test
    public void testNthRoot ()
    {
        assertEquals(3.0, nthRoot(27.0, 3), 1e-9);
        assertEquals(2.0, nthRoot(64.0, 6), 1e-9);
        assertEquals(15.5, nthRoot(15.5, 1), 1e-9);
        assertEquals(3e3, nthRoot(9e6, 2), 1e-9);
    }

    @Test
    public void testIsVowel ()
    {
        assertTrue(isVowel('a'));
        assertTrue(isVowel('e'));
        assertTrue(isVowel('i'));
        assertTrue(isVowel('o'));
        assertTrue(isVowel('u'));
        assertTrue(isVowel('A'));
        assertTrue(isVowel('E'));
        assertTrue(isVowel('I'));
        assertTrue(isVowel('O'));
        assertTrue(isVowel('U'));
        assertFalse(isVowel('x'));
        assertFalse(isVowel('H'));
        assertFalse(isVowel('p'));
        assertFalse(isVowel('Q'));
    }

    @Test
    public void testIsMultipleOf ()
    {
        assertTrue(isMultipleOf(15, 3, 5));
        assertTrue(isMultipleOf(28782 * 9215, 28782, 9215));
        assertTrue(isMultipleOf(98927934, 1, 2));
        assertFalse(isMultipleOf(27, 3, 4));
        assertFalse(isMultipleOf(100, 9, 1));
        assertFalse(isMultipleOf(8749 * 9883 + 1, 8749, 9883));
    }

    @Test
    public void testCapitalize ()
    {
        assertEquals("X", capitalize("x"));
        assertEquals("Hello", capitalize("hello"));
        assertEquals("Jack", capitalize("Jack"));
        assertEquals("123ab", capitalize("123ab"));
        assertEquals("Ab123", capitalize("ab123"));
    }

    @Test
    public void testFlip ()
    {
        assertEquals("efgabc", flip("abcdefg", 'd'));
        assertEquals("abada", flip("ababad", 'b'));
        assertEquals("", flip("x", 'x'));
        assertEquals("nopqrstuvwxyzabcdefghijklm", flip("abcdefghijklm$nopqrstuvwxyz", '$'));
        assertEquals("-------------", flip("--------------", '-'));
    }

    @Test
    public void testCapitalizeVowels ()
    {
        assertEquals("hEllO", capitalizeVowels("hello"));
        assertEquals("StrIng", capitalizeVowels("String"));
        assertEquals("nth", capitalizeVowels("nth"));
        assertEquals("AEIOUAEIOU", capitalizeVowels("aeiouAEIOU"));
        assertEquals("", capitalizeVowels(""));
    }

    @Test
    public void testSameEnding ()
    {
        assertTrue(sameEnding("abcde", "xde", 2));
        assertFalse(sameEnding("abcde", "xde", 3));
        assertTrue(sameEnding("", "", 0));
        assertFalse(sameEnding("123456", "123457", 5));
        assertTrue(sameEnding("abcdefghijklm", "Abcdefghijklm", 12));
    }

    @Test
    public void testLargestOfFive ()
    {
        assertEquals(62, largestOfFive(" 15 28 -99 62 44 "));
        assertEquals(-1, largestOfFive(" -1  -2  -3  -4  -5"));
        assertEquals(0, largestOfFive("0 0 0 0 0"));
        assertEquals(987589, largestOfFive("876342   932874   98734   87634   987589"));
    }

    @Test
    public void testIsEarlierThan ()
    {
        assertTrue(isEarlierThan("12-01-2015", "02-15-2017"));
        assertFalse(isEarlierThan("10-11-2016", "10-11-2016"));
        assertFalse(isEarlierThan("09-09-1967", "02-15-1933"));
        assertTrue(isEarlierThan("02-14-2017", "02-15-2017"));
    }

    @Test
    public void testAddNumerals ()
    {
        assertEquals("189", addNumerals("125", "64"));
        
        assertEquals("747879498758606160553718495182726143747375432098767800124344657426434841235243422220870052",
                addNumerals("747382938463847595746362849506857462515264758586969679698567386468748585234689", 
                        "747879498757858777615254647586979780897868574636252535365757687746736273848774673635635363"));
        
        assertEquals("888888674444444444444444111111111111222222233333333333333333299999999966666666675555555555555555554777777777777777777666666444444444444333333334111111111111111117888888888888888888888889111111111111111111111088888866666666666666655555555555666666666666666",
                addNumerals("7777777777777777777777777777888888888888888888888888888888888855555555555555555555555555555444444444444444444444444222222222222222222222999999999999999999000000000000000000000000444444444444444444444422222222222222222222211111111111111111111111111", 
                        "888888666666666666666666333333333333333333344444444444444444411111111111111111119999999999999999999333333333333333333222222222222222222111111111111111111111111118888888888888888888888888666666666666666666666666666644444444444444444444444444555555555555555"));

    }

}
