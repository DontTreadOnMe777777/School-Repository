package c4;

/**
 * Provides a useful helper method for Connect Four
 * 
 * @author Joe Zachary
 */
public class FourInARow
{
    /**
     * Reports whether A contains four consecutive entries that lie in a line (either horizontally, vertically, or
     * diagonally) and contain the same non-zero value.
     */
    public static boolean fourInRow (int A[][])
    {
        // Check for lines beginning at each entry in the array.
        for (int i = 0; i < A.length; i++)
        {
            for (int j = 0; j < A[i].length; j++)
            {
                if (checkFour(A, i, j))
                {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Reports whether A contains four consecutive entries that lie in a line beginning with A[i][j] (either up, right,
     * diagonally up and to the right, or diagonally up and to the left) and contain the same non-zero value.
     */
    private static boolean checkFour (int A[][], int i, int j)
    {
        // Make sure A[i][j] contains a non-zero value
        int match = A[i][j];
        if (match == 0)
        {
            return false;
        }

        // Look up
        try
        {
            if (A[i + 1][j] == match && A[i + 2][j] == match && A[i + 3][j] == match)
            {
                return true;
            }
        }
        catch (ArrayIndexOutOfBoundsException e)
        {
        }

        // Look right
        try
        {
            if (A[i][j + 1] == match && A[i][j + 2] == match && A[i][j + 3] == match)
            {
                return true;
            }
        }
        catch (ArrayIndexOutOfBoundsException e)
        {
        }

        // Look up and to the right
        try
        {
            if (A[i + 1][j + 1] == match && A[i + 2][j + 2] == match && A[i + 3][j + 3] == match)
            {
                return true;
            }
        }
        catch (ArrayIndexOutOfBoundsException e)
        {
        }

        // Look up and to the left
        try
        {
            if (A[i - 1][j + 1] == match && A[i - 2][j + 2] == match && A[i - 3][j + 3] == match)
            {
                return true;
            }
        }
        catch (ArrayIndexOutOfBoundsException e)
        {
        }

        // Nothing found
        return false;
    }
}
