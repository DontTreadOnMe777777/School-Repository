package cs1410;

import static org.junit.Assert.*;
import org.junit.Test;

public class CacheTests
{
    /**
     * An example test for Cache objects
     */
    @Test
    public void test1 ()
    {
        Cache c = new Cache("GCRQWK\tOld Three Tooth\tgeocadet\t3.5\t3\tN40 45.850\tW111 48.045");
        assertEquals("GCRQWK", c.getGcCode());
    }
    
    @Test
    public void test2 ()
    {
        Cache c = new Cache("GCRQWK\tOld Three Tooth\tgeocadet\t3.5\t3\tN40 45.850\tW111 48.045");
        assertEquals("Old Three Tooth", c.getTitle());
    }
    
    @Test
    public void test3 ()
    {
        Cache c = new Cache("GCRQWK\tOld Three Tooth\tgeocadet\t3.5\t3\tN40 45.850\tW111 48.045");
        assertEquals("geocadet", c.getOwner());
    }
    
    @Test
    public void test4 ()
    {
        Cache c = new Cache("GCRQWK\tOld Three Tooth\tgeocadet\t3.5\t3\tN40 45.850\tW111 48.045");
        assertEquals(3.5, c.getDifficulty(), 0.0001);
    }
    
    @Test
    public void test5 ()
    {
        Cache c = new Cache("GCRQWK\tOld Three Tooth\tgeocadet\t3.5\t3\tN40 45.850\tW111 48.045");
        assertEquals(3.0, c.getTerrain(), 0.0001);
    }
    
    @Test
    public void test6 ()
    {
        Cache c = new Cache("GCRQWK\tOld Three Tooth\tgeocadet\t3.5\t3\tN40 45.850\tW111 48.045");
        assertEquals("N40 45.850", c.getLatitude());
    }
    
    @Test
    public void test7 ()
    {
        Cache c = new Cache("GCRQWK\tOld Three Tooth\tgeocadet\t3.5\t3\tN40 45.850\tW111 48.045");
        assertEquals("W111 48.045", c.getLongitude());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void test8 ()
    {
        Cache c = new Cache("GCRQWK\tOld Three Tooth\t\t3.5\t3\tN40 45.850\t    ");
        assertEquals("W111 48.045", c.getLongitude());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void test9 ()
    {
        Cache c = new Cache("GC\tOld Three Tooth\tgeocadet\t3.5\t3\tN40 45.850\tW111 48.045");
        assertEquals("GC", c.getGcCode());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void test10 ()
    {
        Cache c = new Cache("GCRQWK\tOld Three Tooth\tgeocadet\t3.9\t3\tN40 45.850\tW111 48.045");
        assertEquals(3.9, c.getDifficulty(), 0.0001);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void test11 ()
    {
        Cache c = new Cache("GCRQWK\tOld Three Tooth\tgeocadet\t3.5\t0\tN40 45.850\tW111 48.045");
        assertEquals(0.0, c.getTerrain(), 0.0001);
    }
    
    @Test
    public void test12 ()
    {
        Cache c = new Cache("GCRQWK\tOld Three Tooth\tgeocadet\t3.5\t0\tN40 45.850\tW111 48.045");
        assertEquals("Old Three Tooth by geocadet", c.toString());
    }
}
