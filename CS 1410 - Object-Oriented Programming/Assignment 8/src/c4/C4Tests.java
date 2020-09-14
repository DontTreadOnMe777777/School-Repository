package c4;

import static org.junit.Assert.*;
import org.junit.Test;

public class C4Tests
{

    @Test(expected = IllegalArgumentException.class)
    public void test1 ()
    {
        C4Board newBoard = new C4Board(1,1);
        assertEquals(0, newBoard.getOccupant(0, 0));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void test2 ()
    {
        C4Board newBoard = new C4Board(6,7);
        newBoard.moveTo(10);
    }
    
    @Test
    public void test3 ()
    {
        C4Board newBoard = new C4Board(6,7);
        newBoard.moveTo(4);
        newBoard.moveTo(4);
        newBoard.moveTo(3);
        newBoard.moveTo(3);
        newBoard.moveTo(2);
        newBoard.moveTo(2);
        newBoard.moveTo(1);
        assertEquals(1, newBoard.getWinsForP1());
    }
    
    @Test
    public void test4 ()
    {
        C4Board newBoard = new C4Board(6,7);
        newBoard.moveTo(4);
        newBoard.moveTo(4);
        newBoard.newGame();
        assertEquals(2, newBoard.getToMove());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void test5 ()
    {
        C4Board newBoard = new C4Board(6,7);
        newBoard.moveTo(-3);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void test6 ()
    {
        C4Board newBoard = new C4Board(6,7);
        newBoard.getOccupant(-3, -1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void test7 ()
    {
        C4Board newBoard = new C4Board(6,7);
        newBoard.getOccupant(10, 15);
    }

    @Test
    public void test8 ()
    {
        C4Board newBoard = new C4Board(6,7);
        newBoard.moveTo(4);
        assertEquals(2, newBoard.getToMove());
    }
    
    @Test
    public void test9 ()
    {
        C4Board newBoard = new C4Board(6,7);
        newBoard.moveTo(4);
        assertEquals(1, newBoard.getOccupant(0, 4));
    }
    
    @Test
    public void test10 ()
    {
        C4Board newBoard = new C4Board(6,7);
        newBoard.moveTo(4);
        assertEquals(0, newBoard.getOccupant(0, 0));
    }
    
}
