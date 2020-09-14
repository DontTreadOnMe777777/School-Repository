package c4;

/**
 * Represents the state of a Connect Four board on which multiple games can be played. The board consists of a
 * rectangular grid of squares in which playing pieces can be placed. Squares are identified by their zero-based row and
 * column numbers, where rows are numbered starting with the bottom row, and columns are numbered by starting from the
 * leftmost column.
 * 
 * Multiple games can be played on a single board. Whenever a new game begins, the board is empty. There are two
 * players, identified by the constants P1 (Player 1) and P2 (Player 2). P1 moves first in the first game, P2 moves
 * first in the second game, and so on in alternating fashion.
 * 
 * A C4Board also keeps track of the outcomes of the games that have been played on it. It tracks the number of wins by
 * P1, the number of wins by P2, and the number of ties. It does not track games that were abandoned before being
 * completed.
 */
public class C4Board
{
    /** The number used to indicate Player 1 */
    public static final int P1 = 1;

    /** The number used to indicate Player 2 */
    public static final int P2 = 2;

    /** The number used to indicate a tie */
    public static final int TIE = 3;

    /** The array used to create the board */
    private int[][] c4BoardArray;

    /** The number of the person first to move when a new game is started */
    private int firstToMove = P1;

    /** The number of the person second to move when a new game is started */
    private int secondToMove = P2;

    /** The number of the person who has the current move */
    private int personToMove = firstToMove;

    /** The number of wins by player 1 */
    private int P1Wins = 0;

    /** The number of wins by player 2 */
    private int P2Wins = 0;

    /** The number of ties*/
    private int ties = 0;

    /**
     * Creates a C4Board with the specified number of rows and columns. There should be no pieces on the board and it
     * should be player 1's turn to move.
     * 
     * However, if either rows or cols is less than four, throws an IllegalArgumentException.
     */
    public C4Board (int rows, int cols)
    {
        if (rows < 4 || cols < 4)
        {
            throw new IllegalArgumentException("Rows/columns less than 4.");
        }
        c4BoardArray = new int[rows][cols];
    }

    /**
     * Sets up the board to play a new game, whether or not the current game is complete. All of the pieces should be
     * removed from the board. The player who had the second move in the previous game should have the first move in the
     * new game.
     */
    public void newGame ()
    {
        for (int i = 0; i < c4BoardArray.length; i++)
        {
            for (int j = 0; j < c4BoardArray.length + 1; j++)
            {
                if (c4BoardArray[i][j] != 0)
                {
                    c4BoardArray[i][j] = 0;
                }
            }
        }
        int swapInt = firstToMove;
        firstToMove = secondToMove;
        personToMove = firstToMove;
        secondToMove = swapInt;
    }

    /**
     * Records a move, by the player whose turn it is to move, in the first open square in the specified column. Returns
     * P1 if the move resulted in a win for player 1, returns P2 if the move resulted in a win for player 2, returns TIE
     * if the move resulted in a tie, and returns 0 otherwise.
     * 
     * If the column is full, or if the game is over because a win or a tie has previously occurred, does nothing but
     * return zero.
     * 
     * If a non-existent column is specified, throws an IllegalArgumentException.
     */
    public int moveTo (int col)
    {
        if (col > c4BoardArray.length || col < 0)
        {
            throw new IllegalArgumentException("Non-existant column.");
        }
        try
        {
            if (FourInARow.fourInRow(c4BoardArray) == false)
            {
                /** Token for player 1 */
                if (personToMove == P1)
                {
                    int i = 0;
                    while (getOccupant(i, col) != 0)
                    {
                        i++;
                    }
                    c4BoardArray[i][col] = P1;
                    if (FourInARow.fourInRow(c4BoardArray) == true)
                    {
                        P1Wins = P1Wins + 1;
                        return P1;
                    } 
                }
                /** Token for player 2 */
                if (personToMove == P2)
                {
                    int i = 0;
                    while (getOccupant(i, col) != 0)
                    {
                        i++;
                    }
                    c4BoardArray[i][col] = P2;
                    if (FourInARow.fourInRow(c4BoardArray) == true)
                    {
                        P2Wins = P2Wins + 1;
                        return P2;
                    }
                }
                /** Change next move for next player */
                if (personToMove == P1)
                {
                    personToMove = P2;
                }
                else
                {
                    personToMove = P1;
                }
                /** Check for tie */
                int numberOfOpenSpaces = 0;
                for (int h = 0; h < c4BoardArray.length; h++)
                {
                    for (int j = 0; j < c4BoardArray.length + 1; j++)
                    {
                        if (getOccupant(h, j) == 0)
                        {
                            numberOfOpenSpaces++;
                        }
                    }
                }
                if (numberOfOpenSpaces == 0) {
                    ties = ties++;
                    return TIE;
                }
            }
        }
        catch (ArrayIndexOutOfBoundsException e)
        {
            return 0;
        }
        return 0;

    }

    /**
     * Reports the occupant of the board square at the specified row and column. If the row or column doesn't exist,
     * throws an IllegalArgumentException. If the square is unoccupied, returns 0. Otherwise, returns P1 (if the square
     * is occupied by player 1) or P2 (if the square is occupied by player 2).
     */
    public int getOccupant (int row, int col)
    {
        if (col > c4BoardArray.length || col < 0 || row > c4BoardArray.length || row < 0)
        {
            throw new IllegalArgumentException("Non-existant column.");
        }
        if (c4BoardArray[row][col] == 0)
        {
            return 0;
        }
        if (c4BoardArray[row][col] == P1)
        {
            return P1;
        }
        if (c4BoardArray[row][col] == P2)
        {
            return P2;
        }
        return 0;
    }

    /**
     * Reports whose turn it is to move. Returns P1 (if it is player 1's turn to move), P2 (if it is player 2's turn to
     * move, or 0 (if it is neither player's turn to move because the current game is over because of a win or tie).
     */
    public int getToMove ()
    {
        return personToMove;
    }

    /**
     * Reports how many games have been won by player 1 since this board was created.
     */
    public int getWinsForP1 ()
    {
        return P1Wins;
    }

    /**
     * Reports how many games have been won by player 2 since this board was created.
     */
    public int getWinsForP2 ()
    {
        return P2Wins;
    }

    /**
     * Reports how many ties have occurred since this board was created.
     */
    public int getTies ()
    {
        return ties;
    }
}
