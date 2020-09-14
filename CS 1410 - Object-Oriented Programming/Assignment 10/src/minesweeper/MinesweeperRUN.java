package minesweeper;

import javax.swing.SwingUtilities;

/**
 * Launches a game of Minesweeper.
 * 
 * @author Stephen DeBies, November 2017
 */
public class MinesweeperRUN
{
    public static void main (String[] args)
    {
        SwingUtilities.invokeLater( () -> {
            MinesweeperView display = new MinesweeperView();
            display.setVisible(true);
        });
    }
}
