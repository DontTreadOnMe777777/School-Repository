package cs1410;

import static org.junit.Assert.*;
import static cs1410.MethodLibrary.*;
import org.junit.Test;

public class TestCases
{
    @Test
    public void testLifeStage ()
    {
        assertEquals("baby", getLifeStage(0));
        assertEquals("baby", getLifeStage(1));
        assertEquals("child", getLifeStage(2));
        assertEquals("child", getLifeStage(10));
        assertEquals("teen", getLifeStage(13));
        assertEquals("teen", getLifeStage(16));
        assertEquals("adult", getLifeStage(21));
        assertEquals("adult", getLifeStage(60));
        assertEquals("senior", getLifeStage(70));
        assertEquals("senior", getLifeStage(80));
    }

    @Test
    public void testToPigLatin ()
    {
        assertEquals("ickslay", toPigLatin("slick"));
        assertEquals("ICKSLay", toPigLatin("SLICK"));
        assertEquals("ongstray", toPigLatin("strong"));
        assertEquals("ONGSTRay", toPigLatin("STRONG"));
        assertEquals("xyzzy", toPigLatin("xyzzy"));
        assertEquals("orangeway", toPigLatin("orange"));
    }

    @Test
    public void testConvertToPigLatin ()
    {
        assertEquals("isthay isway away esttay ", convertToPigLatin("this is a test"));
        assertEquals("isthay isway away esttay ", convertToPigLatin("    this     is     a         test"));
        assertEquals("", convertToPigLatin(""));
        assertEquals("", convertToPigLatin("           "));
        assertEquals("eThay ainray inway ainSpay allsfay ainlymay inway ethay ainplay ",
                convertToPigLatin("The rain in Spain falls mainly in the plain"));
    }
    
    @Test
    public void testSumOfRoots ()
    {
        assertEquals(0.0, sumOfRoots("", 2), .000000001);
        assertEquals(10.0, sumOfRoots("1.0 4.0 9.0 16.0", 2), .000000001);
        assertEquals(7.524635948, sumOfRoots("1.0 2.0 3.0 4.0 5.0 6.0", 5), .000000001);
    }
    
    @Test
    public void testContainsAll ()
    {
        assertTrue(containsAll("", ""));
        assertTrue(containsAll("abc", "abracadabra"));
        assertFalse(containsAll("def", "Defect"));
        assertFalse(containsAll("x", ""));
    }
    
    @Test
    public void testMakeLine ()
    {
        assertEquals("+------+", makeLine('+', '-', 8));
        assertEquals("$$$$", makeLine('$', '$', 4));
        assertEquals("**", makeLine('*', '=', 2));
    }

    @Test
    public void testMakeRectangle ()
    {
        assertEquals("++\n++\n", makeRectangle(2, 2));
        assertEquals("+-+\n| |\n+-+\n", makeRectangle(3, 3));
        assertEquals("+--+\n|  |\n|  |\n|  |\n+--+\n", makeRectangle(5, 4));
    }

    @Test
    public void testNextHailstone ()
    {
        assertEquals(1, nextHailstone(1));
        assertEquals(16, nextHailstone(5));
        assertEquals(100, nextHailstone(33));
        assertEquals(1, nextHailstone(2));
        assertEquals(8, nextHailstone(16));
        assertEquals(1000000, nextHailstone(2000000));
    }

    @Test
    public void testHailstones ()
    {
        assertEquals("1 ", hailstones(1));
        assertEquals("16 8 4 2 1 ", hailstones(16));
        assertEquals("7 22 11 34 17 52 26 13 40 20 10 5 16 8 4 2 1 ", hailstones(7));
    }
    
    @Test
    public void testSameTokens ()
    {
        assertTrue(sameTokens("this is a test", " this  is  a  test "));
        assertTrue(sameTokens("", ""));
        assertFalse(sameTokens("hello there", "hello there Joe"));
        assertFalse(sameTokens("abc def", "def abc"));
        assertFalse(sameTokens("a", "A"));
        assertFalse(sameTokens("a b c", "abc"));
    }
}
