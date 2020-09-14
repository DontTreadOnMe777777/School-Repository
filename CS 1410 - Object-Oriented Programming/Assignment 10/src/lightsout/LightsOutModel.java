package lightsout;

import java.util.Random;

public class LightsOutModel
{
    public static final int darkSpace = 0;

    public static final int lightSpace = 1;

    // Array used to create the board
    private int[][] lightsOutBoardArray;

    // Array used to make sure that when New Game is pressed, the randomizer does not come up with an identical board
    private int[][] copyForDuplicate;

    // Boolean used to check if the manual setup is on or off
    private boolean manualStatusBoolean;

    /**
     * Constructs the board to play on.
     */
    public LightsOutModel (int rows, int cols)
    {
        if (rows != 5 || cols != 5)
        {
            throw new IllegalArgumentException("Rows/columns not 5.");
        }
        lightsOutBoardArray = new int[rows][cols];
    }

    /**
     * Creates a new game, checking if the manual setup is on or off and the number of times the manual setup button has
     * been pressed. If on, the board does not refresh. If the button has been pressed an even number of times, meaning
     * that it has been used and reset to off, the board does not refresh. If off, it wipes the board and then makes a
     * series of random moves to create a starting board for the player.
     */
    public void newGame (boolean manualStatus, boolean manualJustExited)
    {
        copyForDuplicate = lightsOutBoardArray.clone();

        if (manualStatus == true)
        {

        }

        else if (manualJustExited == true)
        {

        }

        else if (manualStatus == false && manualJustExited == false)
        {
            for (int i = 0; i < lightsOutBoardArray.length; i++)
            {
                for (int j = 0; j < lightsOutBoardArray.length; j++)
                {
                    if (lightsOutBoardArray[i][j] != darkSpace)
                    {
                        lightsOutBoardArray[i][j] = darkSpace;
                    }
                }
            }
            Random rand = new Random();
            int randInt = rand.nextInt(4);
            makeMove(randInt - 1, randInt + 1);
            makeMove(randInt, randInt + 2);
            makeMove(randInt + 3, randInt);
            makeMove(randInt - 1, randInt - 4);
            makeMove(randInt + 3, randInt - 2);
            makeMove(randInt + 1, randInt);
            makeMove(randInt - 3, randInt + 2);
            makeMove(randInt - 3, randInt);
            makeMove(randInt + 3, randInt - 4);
            makeMove(randInt - 3, randInt + 2);
            makeMove(randInt, randInt);

            if (copyForDuplicate == lightsOutBoardArray)
            {
                newGame(manualStatus, manualJustExited);
            }
        }
        manualStatusBoolean = manualStatus;
    }

    /**
     * If manual status is on, a click only affects that particular square. If off, all immediately adjacent squares are
     * also toggled. After move, checks for a win.
     */
    public boolean makeMove (int row, int col)
    {
        if (row < 0 || col < 0 || row >= lightsOutBoardArray.length || col >= lightsOutBoardArray.length)
        {
            return false;
        }
        if (manualStatusBoolean == false)
        {
            if (row <= lightsOutBoardArray.length - 1 && row >= 0 && col <= lightsOutBoardArray.length - 1 && col >= 0)
            {
                if (lightsOutBoardArray[row][col] == darkSpace)
                {
                    lightsOutBoardArray[row][col] = lightSpace;
                }
                else
                {
                    lightsOutBoardArray[row][col] = darkSpace;
                }
            }

            if (row + 1 <= lightsOutBoardArray.length - 1)
            {
                if (lightsOutBoardArray[row + 1][col] == darkSpace)
                {
                    lightsOutBoardArray[row + 1][col] = lightSpace;
                }
                else
                {
                    lightsOutBoardArray[row + 1][col] = darkSpace;
                }
            }

            if (row - 1 >= 0)
            {
                if (lightsOutBoardArray[row - 1][col] == darkSpace)
                {
                    lightsOutBoardArray[row - 1][col] = lightSpace;
                }
                else
                {
                    lightsOutBoardArray[row - 1][col] = darkSpace;
                }
            }

            if (col + 1 <= lightsOutBoardArray.length - 1)
            {
                if (lightsOutBoardArray[row][col + 1] == darkSpace)
                {
                    lightsOutBoardArray[row][col + 1] = lightSpace;
                }
                else
                {
                    lightsOutBoardArray[row][col + 1] = darkSpace;
                }
            }

            if (col - 1 >= 0)
            {
                if (lightsOutBoardArray[row][col - 1] == darkSpace)
                {
                    lightsOutBoardArray[row][col - 1] = lightSpace;
                }
                else
                {
                    lightsOutBoardArray[row][col - 1] = darkSpace;
                }
            }
        }
        else
        {
            if (manualStatusBoolean == true)
            {
                if (lightsOutBoardArray[row][col] == darkSpace)
                {
                    lightsOutBoardArray[row][col] = lightSpace;
                }
                else
                {
                    lightsOutBoardArray[row][col] = darkSpace;
                }
                return false;
            }
        }
        return allLightsOut(lightsOutBoardArray);
    }

    /**
     * Finds if a certain board square is on or off.
     */
    public int getOccupant (int row, int col)
    {
        if (col > lightsOutBoardArray.length || col < 0 || row > lightsOutBoardArray.length || row < 0)
        {
            throw new IllegalArgumentException("Non-existant column.");
        }
        if (lightsOutBoardArray[row][col] == lightSpace)
        {
            return lightSpace;
        }
        else
        {
            return darkSpace;
        }
    }

    /**
     * Checks to see if the board is all dark, thereby winning the game.
     */
    public boolean allLightsOut (int[][] arrayToCheck)
    {
        for (int i = 0; i < arrayToCheck.length; i++)
        {
            for (int j = 0; j < arrayToCheck[i].length; j++)
            {
                if (arrayToCheck[i][j] == 1)
                {
                    return false;
                }
            }
        }
        return true;
    }

}
