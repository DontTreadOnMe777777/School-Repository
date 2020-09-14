package minesweeper;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

/**
 * Implements a Minesweeper game with a GUI interface.
 * 
 * @author Stephen DeBies, November 2017
 */

/**
 * Represents the state of a Minesweeper board on which multiple games can be played. The board consists of a
 * rectangular grid of squares in which playing pieces can be placed. Squares are identified by their zero-based row and
 * column numbers, where rows are numbered starting with the bottom row, and columns are numbered by starting from the
 * leftmost column.
 * 
 * Multiple games can be played on a single board. Whenever a new game begins, the board is covered. There is one
 * player. The goal of the game is to uncover all the squares without choosing a mine. The numbers represent how many
 * squares surrounding that square (up to 8) have a mine in it.
 * 
 * A MinesweeperModel also keeps track of the outcomes of the games that have been played on it. It tracks the number of
 * the time and how many mines are left to choose.
 */
public class MinesweeperModel
{
    /**
     * The contents of the playing board, where board[r][c] is the item that is contained at row r column c.
     * 
     * If board[r][c] contains a mine, then either 9, 10, or 11 will be stored there. 9 stands for unexploded mine, 10
     * stands for exploded mine, and 11 stands for incorrectly flagged mine.
     * 
     * If board[r][c] does not contain a mine, then an integer 0 through 8 will be stored there. This number gives the
     * number of directly adjacent squares that contain mines.
     */
    private int[][] board;

    /**
     * Controls whether the contents of board[r][c] is covered from the user's view.
     */
    private boolean[][] isSquareCovered;

    /** The image will be displayed on each button. */
    private String[][] buttonPic;

    /** The number of columns on the playing board */
    private int cols;

    /** The number of rows on the playing board */
    private int rows;

    /** The number of mines on the playing board */
    private int mines;

    /** Specifies if the game has been lost. */
    private boolean gameIsLost;

    /** Specifies if the game has been won. */
    private boolean gameIsWon;

    /** Specifies the total number of mines on the board. */
    private int targetMineCount;

    /** Specifies the number of mines that have not been tagged with a mark. */
    private int minesNotMarkedCount;

    /** Code stored in board to indicate an empty square */
    private final static int EMPTY = 0;

    /** Code stored in board to indicate an unexploded mine */
    private final static int MINE = 9;

    /** Code stored in board to indicate an exploded mine */
    private final static int EXPLODED_MINE = 10;

    /** Code stored in board to indicate an incorrectLy flagged mine */
    private final static int INCORRECTLY_FLAGGED_MINE = 11;

    // CONSTRUCTOR//
    /**
     * Creates a Minesweeper Board with the specified number of rows and columns. However, if either rows or cols is
     * less than four, throws an IllegalArgumentException.
     */
    public MinesweeperModel (int rows, int cols, int mines)
    {
        if (rows < 4 || cols < 4 || mines < 1)
            throw new IllegalArgumentException();

        this.cols = cols;
        this.rows = rows;
        this.mines = mines;

        // Creates a Minesweeper Board with the specified number of rows and columns
        board = new int[rows][cols];
        isSquareCovered = new boolean[rows][cols];
        buttonPic = new String[rows][cols];
        newGame(rows, cols);
    }

    /**
     * Sets up the board to play a new game, whether or not the current game is complete. All of the buttons on the
     * board are replaced. All the data is cleared, new mines are placed.
     */
    public void newGame (int rows, int cols)
    {
        gameIsLost = false;
        gameIsWon = false;

        // Clear the board (all mines and numbers)
        for (int i = 0; i < board.length; i++)
        {
            for (int j = 0; j < board[i].length; j++)
            {
                board[i][j] = EMPTY;
            }
        }

        // if (rows == 8 && cols == 8) // Beginner level
        targetMineCount = mines;
        // else if (rows == 12 && cols == 12) // default debugging mode
        // targetMineCount = 5;
        // else if (rows == 16 && cols == 16) // Intermediate level
        // targetMineCount = 40; // 40;
        // else if (rows == 16 && cols == 31) // Expert level
        // targetMineCount = 99;
        // else
        // targetMineCount = (int) (rows * cols * 0.15625);

        int mineCount = 0;
        Random random = new Random();

        // SET MINE ON BOARD
        while (mineCount < targetMineCount)
        {
            int x = random.nextInt(rows);
            int y = random.nextInt(cols);
            if (board[x][y] != MINE)
            {
                board[x][y] = MINE;
                mineCount++;
            }
        }
        minesNotMarkedCount = mineCount;

        // CALCULATE NUMBERS IN SQUARES, i.e. HOW MANY ADJACENT MINES
        for (int i = 0; i < board.length; i++)
        {
            for (int j = 0; j < board[i].length; j++)
            {
                // if this square is not a mine, count the surrounding mines
                if (board[i][j] != MINE)
                {
                    board[i][j] = countMines(i, j, findSurroundingSquares(i, j));
                }
            }
        }

        // COVER ALL SQUARES WITH BLANK BUTTONS
        for (int i = 0; i < isSquareCovered.length; i++)
        {
            for (int j = 0; j < isSquareCovered[i].length; j++)
            {
                isSquareCovered[i][j] = true;
                buttonPic[i][j] = "blank";
            }
        }
    }

    /**
     * Counts the mines on the playing board.
     */
    private int countMines (int i, int j, ArrayList<Point> surroundingPoints)
    {
        int sum = 0;
        for (Point p : surroundingPoints)
        {
            if (board[p.x][p.y] == MINE)
                sum++;
        }
        return sum;
    }

    /**
     * Returns a list of coordinates of all the squares that are adjacent to the given square (maximum 8). Each Point p
     * in the list encodes a coordinate, where p.x is the row and p.y is the col.
     */
    private ArrayList<Point> findSurroundingSquares (int row, int col)
    {
        ArrayList<Point> surroundingPoints = new ArrayList<>();
        if (row > 0)
        {
            surroundingPoints.add(new Point(row - 1, col));
            if (col > 0)
                surroundingPoints.add(new Point(row - 1, col - 1));
            if (col < cols - 1)
                surroundingPoints.add(new Point(row - 1, col + 1));
        }
        if (row < rows - 1)
        {
            surroundingPoints.add(new Point(row + 1, col));
            if (col > 0)
                surroundingPoints.add(new Point(row + 1, col - 1));
            if (col < cols - 1)
                surroundingPoints.add(new Point(row + 1, col + 1));
        }
        if (col > 0)
            surroundingPoints.add(new Point(row, col - 1));
        if (col < cols - 1)
            surroundingPoints.add(new Point(row, col + 1));

        return surroundingPoints;
    }

    /**
     * Reports whether the given square is marked with a flag.
     */
    private boolean isFlagged (int row, int col)
    {
        return buttonPic[row][col].equals("flag");
    }

    /**
     * If the square is uncovered, the game is over, or the square is flagged, does nothing.
     * 
     * Uncovers the specified square, which will display what is underneath. If the current game complete (already won
     * or lost), it does nothing. Else, if a mine is found, ends the game. Else, if a number is found, uncovers the
     * square. Otherwise, if the square is blank, uncovers the square and then recursively calls all adjacent squares.
     */
    public void uncover (int row, int col)
    {
        // Sometimes there's nothing to do
        if (!isSquareCovered[row][col] || gameIsLost || gameIsWon || isFlagged(row, col))
            return;

        // The square is a mine
        if (board[row][col] == MINE)
        {
            // Explode the mine, uncover the square, and mark the game as lost
            board[row][col] = EXPLODED_MINE;
            isSquareCovered[row][col] = false;
            gameIsLost = true;

            // Uncover all mines and flags
            for (int i = 0; i < board.length; i++)
            {
                for (int j = 0; j < board[i].length; j++)
                {
                    if (board[i][j] == MINE)
                    {
                        if (!isFlagged(i, j))
                        {
                            isSquareCovered[i][j] = false;
                        }
                    }
                    else if (isFlagged(i, j))
                    {
                        board[i][j] = INCORRECTLY_FLAGGED_MINE;
                        isSquareCovered[i][j] = false;
                    }
                }
            }
        }

        // The square is a number or is empty
        else
        {
            uncoverEmptyRegionAndBoundary(row, col);
        }

        // Update gameIsWon
        if (isGameWon())
        {
            gameIsWon = true;
        }
    }

    /**
     * Let S be the square at the given row and column of the board.
     * 
     * S must meet the following three conditions. If it doesn't, no particular behavior is guaranteed.
     * 
     * (1) S must either contain a number (1 through 8) or be empty (0).
     * 
     * (2) S must not be marked with a flag
     * 
     * (3) S must be covered
     * 
     * If S is non-empty, this method uncovers S.
     * 
     * If S is empty, then it belongs to a region R of unflagged empty squares. R consists of all of the unflagged empty
     * squares that you could reach from S by stepping from square to square (each step consisting of moving to an
     * adjacent square) without ever stepping on a flagged or non-empty square. (Each square has up to eight squares
     * that are vertically, horizontally, or diagonally adjacent.)
     * 
     * If S is empty, this method uncovers all of the squares in R plus all of the unflagged non-empty squares that are
     * adjacent to some square in R.
     */
    private void uncoverEmptyRegionAndBoundary (int row, int col)
    {
        if (board[row][col] >= 1 && board[row][col] <= 8)
        {
            isSquareCovered[row][col] = false;
        }

        else if (isFlagged(row, col) == true)
        {
            isSquareCovered[row][col] = true;
        }

        else if (board[row][col] == 0)
        {
            isSquareCovered[row][col] = false;
            ArrayList<Point> surroundingPoints = findSurroundingSquares(row, col);
            for (Point p : surroundingPoints)
            {
                uncoverEmptyRegionAndBoundary(p.x, p.y);
            }
            
            
        }

        // Put your implementation here. You will not need to put code anywhere else.

        // You will need to use the instance variables board and isSquareCovered.

        // You will find the methods findSurroundingSquares() and isFlagged() useful.

        // You will find recursion extremely useful.
    }

    /**
     * Return the current win status of the game, whether it has been won or not.
     */
    public boolean isGameWon ()
    {
        if (gameIsWon == true)
            return true;

        boolean temp = true;
        for (int i = 0; i < board.length; i++)
        {
            for (int j = 0; j < board[i].length; j++)
            {
                if ((board[i][j] != MINE && isSquareCovered[i][j] == true)
                        || (board[i][j] == MINE && isSquareCovered[i][j] == false))
                {
                    temp = false;
                    break;
                }
            }
        }
        gameIsWon = temp;
        return gameIsWon;
    }

    /**
     * Uncovers all the squares, which displays the mines and numbers.
     */
    public void uncoverAll ()
    {
        for (int i = 0; i < isSquareCovered.length; i++)
            for (int j = 0; j < isSquareCovered[i].length; j++)
                isSquareCovered[i][j] = false;
    }

    /***********************/
    /* GETTERS AND SETTERS */
    /***********************/
    /**
     * Reports the data of the board square at the specified row and column. If the row or column doesn't exist, throws
     * an IllegalArgumentException.
     */
    public int getBoardValue (int row, int col)
    {
        return board[row][col];
    }

    public int getMinesNotMarkedCount ()
    {
        return minesNotMarkedCount;
    }

    public void setMinesNotMarkedCount (int count)
    {
        this.minesNotMarkedCount = count;
    }

    public boolean getIsMineFound ()
    {
        return gameIsLost;
    }

    public boolean getIsSquareCovered (int row, int col)
    {
        return isSquareCovered[row][col];
    }

    public int getNumberInSquare (int row, int col)
    {
        return board[row][col];
    }

    public void setButtonPic (String text, int row, int col)
    {
        buttonPic[row][col] = text;
    }

    public String getButtonPic (int row, int col)
    {
        return buttonPic[row][col];
    }
}
