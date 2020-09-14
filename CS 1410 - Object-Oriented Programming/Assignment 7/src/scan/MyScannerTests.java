package scan;

import static org.junit.Assert.*;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import org.junit.Test;

public class MyScannerTests
{

    @Test
    public void test ()
    {
        MyScanner scanner = new MyScanner("12 people are here.");
        assertEquals(true, scanner.hasNextInt());
    }
    
    @Test
    public void test2 ()
    {
        MyScanner scanner = new MyScanner("No people are here.");
        assertEquals(false, scanner.hasNextInt());
    }
    
    @Test
    public void test17 ()
    {
        MyScanner scanner = new MyScanner("Only 5 people are here.");
        assertEquals(false, scanner.hasNextInt());
    }
    
    @Test
    public void test3 ()
    {
        MyScanner scanner = new MyScanner("12 people are here.");
        assertEquals(12, scanner.nextInt());
    }
    
    @Test
    public void test4 ()
    {
        MyScanner scanner = new MyScanner("There are 10");
        assertEquals(10, scanner.nextInt());
    }
    
    @Test(expected = NoSuchElementException.class)
    public void test5 ()
    {
        MyScanner scanner = new MyScanner("");
        assertEquals(10, scanner.nextInt());
    }
    
    @Test(expected = InputMismatchException.class)
    public void test6 ()
    {
        MyScanner scanner = new MyScanner("6.0");
        assertEquals(6, scanner.nextInt());
    }
    
    @Test
    public void test7 ()
    {
        MyScanner scanner = new MyScanner("12 people are here.");
        assertEquals(true, scanner.hasNext());
    }
    
    @Test
    public void test8 ()
    {
        MyScanner scanner = new MyScanner("People are here.");
        assertEquals(true, scanner.hasNext());
    }
    
    @Test
    public void test9 ()
    {
        MyScanner scanner = new MyScanner("   12 people are here.");
        assertEquals(true, scanner.hasNext());
    }
    
    @Test
    public void test10 ()
    {
        MyScanner scanner = new MyScanner("    ");
        assertEquals(false, scanner.hasNext());
    }
    
    @Test
    public void test11 ()
    {
        MyScanner scanner = new MyScanner("");
        assertEquals(false, scanner.hasNext());
    }
    
    @Test(expected = NoSuchElementException.class)
    public void test12 ()
    {
        MyScanner scanner = new MyScanner("");
        assertEquals("", scanner.next());
    }
    
    @Test
    public void test13 ()
    {
        MyScanner scanner = new MyScanner("                        ");
        assertEquals("", scanner.next());
    }
    
    @Test
    public void test14 ()
    {
        MyScanner scanner = new MyScanner("1");
        assertEquals("1", scanner.next());
    }
    
    @Test
    public void test15 ()
    {
        MyScanner scanner = new MyScanner("There be monsters here");
        assertEquals("There", scanner.next());
    }
    
    @Test
    public void test16 ()
    {
        MyScanner scanner = new MyScanner("Nah");
        assertEquals("Nah", scanner.next());
    }
}
