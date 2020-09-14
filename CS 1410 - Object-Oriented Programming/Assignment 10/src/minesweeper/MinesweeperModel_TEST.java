package minesweeper;

import static org.junit.Assert.*;
import java.awt.Point;
import java.util.ArrayList;
import org.junit.Test;

public class MinesweeperModel_TEST
{
    //fail("Not yet implemented");
    
    @Test (expected = IllegalArgumentException.class)
    public void test_MinesweeperModel_Exception_row1 ()
    {
        new MinesweeperModel(12, 3, 10);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void test_MinesweeperModel_Exception_row2 ()
    {
        new MinesweeperModel(12, -1, 10);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void test_MinesweeperModel_Exception_col1 ()
    {
        new MinesweeperModel(3, 20, 10);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void test_MinesweeperModel_Exception_col2 ()
    {
        new MinesweeperModel(-1, 20, 10);
    }
        
    @Test
    public void test_MinesweeperModel_setGetBombCount1000Times ()
    {
        MinesweeperModel model = new MinesweeperModel(16, 20, 10);
        for(int i = 0; i < 1000; i++)
        {
            model.setMinesNotMarkedCount(i);
            assertTrue(model.getMinesNotMarkedCount() == i);
        }
    }
    
    @Test
    public void test_MinesweeperModel_getIsBombFound1 ()
    {
        MinesweeperModel model = new MinesweeperModel(16, 20, 10);
        assertFalse(model.getIsMineFound());
    }
    
    @Test
    public void test_MinesweeperModel_getIsBombFound2 ()
    {
        MinesweeperModel model = new MinesweeperModel(16, 20, 10);
        for(int i = 0; i < 16; i++)
        {
            for(int j = 0; j < 20; j++)
            {
                model.uncover(i, j);
            }
        }
        assertTrue(model.getIsMineFound());
    }
    
    @Test
    public void test_MinesweeperModel_getIsBombFound3 ()
    {
        MinesweeperModel model = new MinesweeperModel(16, 20, 10);
        for(int i = 0; i < 16; i++)
        {
            for(int j = 0; j < 20; j++)
            {
                model.uncover(i, j);
            }
        }
        assertTrue(model.getIsMineFound());
        model.newGame(16, 20);
        assertFalse(model.getIsMineFound());
    }
    
    @Test
    public void test_MinesweeperModel_isAllSquaresCovered ()
    {
        MinesweeperModel model = new MinesweeperModel(16, 20, 10);
        for(int i = 0; i < 16; i++)
        {
            for(int j = 0; j < 20; j++)
            {
                assertTrue(model.getIsSquareCovered(i,j));        
            }
        }
    }
    
    @Test
    public void test_MinesweeperModel_isEachSquareUNCovered ()
    {
        MinesweeperModel model = new MinesweeperModel(16, 20, 10);
        for(int i = 0; i < 16; i++)
        {
            for(int j = 0; j < 20; j++)
            {
                if(model.getNumberInSquare(i, j) != 9)
                {
                    model.uncover(i, j);
                    assertFalse(model.getIsSquareCovered(i,j));
                }
                        
            }
        }
    }
    
    @Test
    public void test_MinesweeperModel_verifyTextBlank ()
    {
        MinesweeperModel model = new MinesweeperModel(16, 20, 10);
        for(int i = 0; i < 16; i++)
        {
            for(int j = 0; j < 20; j++)
            {
                assertTrue(model.getButtonPic(i,j).equals("blank"));
            }
        }
    }
    
    @Test
    public void test_MinesweeperModel_verifyTextX ()
    {
        MinesweeperModel model = new MinesweeperModel(16, 20, 10);
        for(int i = 0; i < 16; i++)
        {
            for(int j = 0; j < 20; j++)
            {
                model.setButtonPic("X", i, j);
                assertTrue(model.getButtonPic(i, j).equals("X"));
            }
        }
    }
    
    @Test
    public void test_MinesweeperModel_verifyTextQuestionMark ()
    {
        MinesweeperModel model = new MinesweeperModel(16, 20, 10);
        for(int i = 0; i < 16; i++)
        {
            for(int j = 0; j < 20; j++)
            {
                model.setButtonPic("?", i, j);
                assertTrue(model.getButtonPic(i, j).equals("?"));
            }
        }
    }
    
    @Test
    public void test_MinesweeperModel_verifyTextBackToBlank ()
    {
        MinesweeperModel model = new MinesweeperModel(16, 20, 10);
        for(int i = 0; i < 16; i++)
        {
            for(int j = 0; j < 20; j++)
            {
                model.setButtonPic("X", i, j);
                assertTrue(model.getButtonPic(i, j).equals("X"));
            }
            for(int j = 0; j < 20; j++)
            {
                model.setButtonPic("", i, j);
                assertTrue(model.getButtonPic(i, j).equals(""));
            }
        }
    }
    
    @Test
    public void test_MinesweeperModel_numbersBetween0and9 ()
    {
        MinesweeperModel model = new MinesweeperModel(16, 20, 10);
        for(int i = 0; i < 16; i++)
        {
            for(int j = 0; j < 20; j++)
            {
                assertTrue(model.getNumberInSquare(i,j) >= 0);
                assertTrue(model.getNumberInSquare(i,j) <= 9);
            }
        }
    }
    
    
    
    
    
    @Test
    public void test_MinesweeperModel_correctNumbers ()
    {
        MinesweeperModel model = new MinesweeperModel(12, 16, 10);
        
        //PRINTS BOARD
        System.out.println("BOARD SIZE: " + 12 + " X " + 16);
        for(int j = 0; j < 16; j++)
            System.out.print("_");
        System.out.println();
            
        for(int i = 0; i < 12; i++)
        {
            for(int j = 0; j < 16; j++)
            {
                int count; 
                if(model.getNumberInSquare(i, j) == 9)
                    count = 9;
                else
                    count = this.countBombs(i, j, model, this.findSurroundingPoints(i, j));
                System.out.print(count==9?"*":(count==0?" ":count));
            }
            System.out.println("|");
        }
        for(int j = 0; j < 16; j++)
            System.out.print("-");
        System.out.println();
        
        //TESTS
        int countSquares = 0;
        int countBombs = 0;
        for(int i = 0; i < 12; i++)
        {
            for(int j = 0; j < 16; j++)
            {
                int expected;
                if(model.getNumberInSquare(i, j) == 9) //if a bomb
                {
                    //expected = 9; 
                    countBombs++;
                    continue;
                }
                else                             //if blank or has a number
                    expected = this.countBombs(i, j, model, this.findSurroundingPoints(i, j));
                
                int actual = model.getNumberInSquare(i,j);
                if(expected != actual)
                    System.out.println("i=" + i + " j=" + j + " expected=" + expected + " - actual=" + actual);

                assertTrue(expected == actual);
                countSquares++;
            }
        }
        assertTrue(model.getMinesNotMarkedCount() == countBombs);
        assertTrue(countSquares + countBombs == 12 * 16);
    }
    
    private int countBombs (int i, int j, MinesweeperModel model,
                            ArrayList<Point> surroundingPoints)
    {
        int sum = 0;
        for(Point p : surroundingPoints)
        {
            if(model.getBoardValue(p.x, p.y) == 9)
                sum++;
        }
        return sum;
    }

    public ArrayList<Point> findSurroundingPoints (int i, int j)
    {
        int ROWS = 12;
        int COLS = 16;
        
        ArrayList<Point> array = new ArrayList<>();
        if(i > 0)
        {
                           array.add(new Point(i-1,j));
            if(j > 0)      array.add(new Point(i-1,j-1));
            if(j < COLS-1) array.add(new Point(i-1,j+1));
        }
        if(i < ROWS-1)
        {
                           array.add(new Point(i+1,j));
            if(j > 0)      array.add(new Point(i+1,j-1));
            if(j < COLS-1) array.add(new Point(i+1,j+1));
        }
        if(j > 0)          array.add(new Point(i,j-1));
        if(j < COLS-1)     array.add(new Point(i,j+1));
        
        return array;
    }
    
    @Test
    public void test_MinesweeperModel_unCover ()
    {
        MinesweeperModel model = new MinesweeperModel(16, 20, 10);
        for(int i = 0; i < 16; i++)
        {
            for(int j = 0; j < 20; j++)
            {
                assertTrue(model.getIsSquareCovered(i,j));
            }
        }
        model.uncoverAll();
        for(int i = 0; i < 16; i++)
        {
            for(int j = 0; j < 20; j++)
            {
                assertFalse(model.getIsSquareCovered(i,j));
            }
        }
    }
    
    @Test
    public void test1 ()
    {
        MinesweeperModel model = new MinesweeperModel(16, 20, 10);
        model.uncover(5, 5);
        model.uncover(0, 0);
    }
}
