package c4;

import javax.swing.SwingUtilities;

/**
 * Launches a game of Connect 4.
 * 
 * @author Joe Zachary
 */
public class Connect4
{
    public static void main (String[] args)
    {
        SwingUtilities.invokeLater( () -> new C4Display());
    }
}
