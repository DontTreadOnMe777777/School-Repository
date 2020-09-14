package rational;

import static org.junit.Assert.*;
import java.math.BigInteger;
import org.junit.Test;

public class BigRatTests
{
    @Test
    public void testConstructor1 ()
    {
        BigRat r = new BigRat();
        assertEquals("0", r.toString());
    }

    @Test
    public void testConstructor2 ()
    {
        BigRat r = new BigRat(5);
        assertEquals("5", r.toString());
    }

    @Test
    public void testConstructor3 ()
    {
        BigRat r1 = new BigRat(1, 2);
        assertEquals("1/2", r1.toString());

        BigRat r2 = new BigRat(5, 15);
        assertEquals("1/3", r2.toString());

        BigRat r3 = new BigRat(-4, -2);
        assertEquals("2", r3.toString());

        BigRat r4 = new BigRat(6, -8);
        assertEquals("-3/4", r4.toString());

        BigRat r5 = new BigRat(3, -15);
        assertEquals("-1/5", r5.toString());
        
        try {
            new BigRat(4, 0);
            fail("No exception thrown");
        }
        catch (IllegalArgumentException e)
        {           
        }
    }

    @Test
    public void testAdd ()
    {
        BigRat r1 = new BigRat(2,5);
        BigRat r2 = new BigRat(3,4);
        assertEquals("23/20", r1.add(r2).toString());
        
        r1 = new BigRat(1, 7);
        r2 = new BigRat(-1, 5);
        assertEquals("-2/35", r1.add(r2).toString());
    }

    @Test
    public void testSub ()
    {
        BigRat r1 = new BigRat(2,5);
        BigRat r2 = new BigRat(3,4);
        assertEquals("-7/20", r1.sub(r2).toString());
        
        r1 = new BigRat(1, 7);
        r2 = new BigRat(-1, 5);
        assertEquals("12/35", r1.sub(r2).toString());
    }

    @Test
    public void testMul ()
    {
        BigRat r1 = new BigRat(2,5);
        BigRat r2 = new BigRat(3,4);
        assertEquals("3/10", r1.mul(r2).toString());
        
        r1 = new BigRat(1, 7);
        r2 = new BigRat(-1, 5);
        assertEquals("-1/35", r1.mul(r2).toString());
    }

    @Test
    public void testDiv ()
    {
        BigRat r1 = new BigRat(2,5);
        BigRat r2 = new BigRat(3,4);
        assertEquals("8/15", r1.div(r2).toString());
        
        r1 = new BigRat(1, 7);
        r2 = new BigRat(-1, 5);
        assertEquals("-5/7", r1.div(r2).toString());
        
        try {
            r1 = new BigRat(3,4);
            r2 = new BigRat(0);
            r1.div(r2);
            fail("No exception thrown");
        }
        catch (IllegalArgumentException e)
        {
        }
    }

    @Test
    public void testCompareTo ()
    {
        BigRat r1 = new BigRat(3,4);
        BigRat r2 = new BigRat(6,8);
        BigRat r3 = new BigRat(1,2);
        assertEquals(0, r1.compareTo(r2));
        assertTrue(r1.compareTo(r3) > 0);
        assertTrue(r3.compareTo(r1) < 0);
    }
    
    @Test
    public void testBigExample ()
    {
        BigRat r1 = new BigRat(
                new BigInteger("987876765674565435423544657879080989878678676"),
                new BigInteger("89787676564654675786897988908987786785764654645675"));
        BigRat r2 = r1.div(r1);
        assertEquals("1", r2.toString());
    }
    
    @Test
    public void testBigExample2 ()
    {
        BigRat r1 = new BigRat(
                new BigInteger("5000000000000000000000000000000000000000000000000000000000"),
                new BigInteger("1000000000000000000000000000000000000000000000000000000000"));
        BigRat r2 = new BigRat(
                new BigInteger("1000000000000000000000000000000000000000000000000000000000"),
                new BigInteger("1000000000000000000000000000000000000000000000000000000000"));
        BigRat r3 = r1.div(r2);
        assertEquals("5", r3.toString());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBigExample3 ()
    {
        BigRat r1 = new BigRat(
                new BigInteger("0"),
                new BigInteger("123456789012345678901234567890"));
        BigRat r2 = r1.div(r1);
        assertEquals("1", r2.toString());
    }
    
    @Test
    public void testBigExample4 ()
    {
        BigRat r1 = new BigRat(
                new BigInteger("500000000000000000000000000000"),
                new BigInteger("100000000000000000000000000000"));
        BigRat r2 = new BigRat(
                new BigInteger("500000000000000000000000000000"),
                new BigInteger("100000000000000000000000000000"));
        BigRat r3 = r1.add(r2);
        assertEquals("10", r3.toString());
    }
    
    @Test
    public void testBigExample5 ()
    {
        BigRat r1 = new BigRat(
                new BigInteger("5000000000000000000000000000000000000000000000000000000000"),
                new BigInteger("1000000000000000000000000000000000000000000000000000000000"));
        BigRat r2 = new BigRat(
                new BigInteger("1000000000000000000000000000000000000000000000000000000000"),
                new BigInteger("1000000000000000000000000000000000000000000000000000000000"));
        BigRat r3 = r1.sub(r2);
        assertEquals("4", r3.toString());
    }
    @Test
    public void testBigExample6 ()
    {
        BigRat r1 = new BigRat(
                new BigInteger("5000000000000000000000000000000000000000000000000000000000"),
                new BigInteger("1000000000000000000000000000000000000000000000000000000000"));
        BigRat r2 = new BigRat(
                new BigInteger("1000000000000000000000000000000000000000000000000000000000"),
                new BigInteger("1000000000000000000000000000000000000000000000000000000000"));
        BigRat r3 = r1.mul(r2);
        assertEquals("5", r3.toString());
    }
}
