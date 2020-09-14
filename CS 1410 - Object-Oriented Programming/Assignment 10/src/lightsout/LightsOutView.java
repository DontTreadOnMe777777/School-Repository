package lightsout;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import static lightsout.LightsOutView.*;
import static lightsout.LightsOutModel.*;

@SuppressWarnings("serial")
public class LightsOutView extends JFrame implements ActionListener
{
    // Constants for formatting
    private final static int WIDTH = 600;
    private final static int HEIGHT = 600;
    public final static int ROWS = 5;
    public final static int COLS = 5;
    public final static Color BACKGROUND_COLOR = Color.GRAY;
    public final static int BORDER = 5;
    public final static int FONT_SIZE = 20;

    // Variable used to represent the "logic" of the game, all the rules and what's supposed to happen when the player
    // does something
    private LightsOutModel model;

    // Variable used to represent the playable surface and all of the squares within that react to the player
    private Board board;

    // Button assignments to use for changing button text and knowing if a button is pressed
    private JButton newGameInstance;

    private JButton manualSetupInstance;

    // Boolean to know whether the manual setup is on or off
    private boolean manualSetupStatus = false;

    // An boolean to check if the manual setup button has just been pressed, so that we can use it to help the
    // newGame function in the model.
    private boolean manualSetupJustExited = false;

    /**
     * This method creates and lays out the window, including the board and the buttons.
     */
    public LightsOutView ()
    {
        setTitle("Lights Out by Brandon Walters");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);

        JPanel root = new JPanel();
        root.setLayout(new BorderLayout());
        setContentPane(root);

        model = new LightsOutModel(ROWS, COLS);
        board = new Board(model, this);
        root.add(board, "Center");

        JPanel buttons = new JPanel();
        buttons.setBackground(BACKGROUND_COLOR);

        JButton newGame = new JButton("New Game");
        newGame.setFont(new Font("SansSerif", Font.BOLD, FONT_SIZE));
        newGame.setForeground(Color.YELLOW);
        newGame.setBackground(Color.black);
        newGame.addActionListener(this);
        newGameInstance = newGame;

        JButton manualSetup = new JButton("Enter Manual Setup");
        manualSetup.setFont(new Font("SansSerif", Font.BOLD, FONT_SIZE));
        manualSetup.setForeground(Color.YELLOW);
        manualSetup.setBackground(Color.black);
        manualSetup.addActionListener(this);
        manualSetupInstance = manualSetup;

        buttons.add(newGame);
        buttons.add(manualSetup);
        root.add(buttons, "South");

        board.refresh();
        setVisible(true);
    }

    /**
     * If a button is pressed, the program calls the model to execute that action.
     */
    @Override
    public void actionPerformed (ActionEvent e)
    {
        if (e.getSource() == newGameInstance)
        {
            model.newGame(manualSetupStatus, manualSetupJustExited);
            board.refresh();
        }
        else
        {
            if (manualSetupStatus == true)
            {
                manualSetupInstance.setText("Enter Manual Setup");
                manualSetupStatus = !manualSetupStatus;
                manualSetupJustExited = true;
                model.newGame(manualSetupStatus, manualSetupJustExited);
                manualSetupJustExited = false;
            }
            else
            {
                manualSetupInstance.setText("Exit Manual Setup");
                manualSetupStatus = !manualSetupStatus;
                model.newGame(manualSetupStatus, manualSetupJustExited);
                
            }
            board.refresh();
        }

    }
}

@SuppressWarnings("serial")
class Board extends JPanel implements MouseListener
{
    /** The "smarts" of the game */
    private LightsOutModel model;

    /** The top level GUI for the game */
    private LightsOutView display;

    /**
     * Creates a playable surface for the game that contains the squares used to simulate moves.
     */
    public Board (LightsOutModel model, LightsOutView display)
    {
        // Record the model and the top-level display
        this.model = model;
        this.display = display;

        // Set the background color and the layout
        setBackground(BACKGROUND_COLOR);
        setLayout(new GridLayout(ROWS, COLS));

        // Create and lay out the grid of squares that make up the game.
        for (int i = ROWS - 1; i >= 0; i--)
        {
            for (int j = 0; j < COLS; j++)
            {
                Square s = new Square(i, j);
                s.addMouseListener(this);
                add(s);
            }
        }
    }

    /**
     * Refreshes and repaints the window
     */
    public void refresh ()
    {
        // Iterate through all of the squares that make up the grid
        Component[] squares = getComponents();
        for (Component c : squares)
        {
            Square s = (Square) c;

            // Set the color of the squares appropriately
            int status = model.getOccupant(s.getRow(), s.getCol());
            if (status == lightSpace)
            {
                s.setColor(Color.WHITE);
            }
            else
            {
                s.setColor(Color.BLACK);
            }
        }
        // Ask that this board be repainted
        repaint();
    }

    @Override
    public void mouseClicked (MouseEvent e)
    {
        // TODO Auto-generated method stub

    }

    /**
     * Calls the model to make a move and if the move is a winning one, prompts the user with a message box telling them
     * they won.
     */
    @Override
    public void mousePressed (MouseEvent e)
    {
        Square s = (Square) e.getSource();
        boolean result = model.makeMove(s.getRow(), s.getCol());
        refresh();
        if (result == true)
        {
            JOptionPane.showMessageDialog(this, "You win!");
        }
    }

    @Override
    public void mouseReleased (MouseEvent e)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseEntered (MouseEvent e)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseExited (MouseEvent e)
    {
        // TODO Auto-generated method stub

    }
}

/**
 * This class creates the square, which is the thing that the player interacts with on the board.
 */
@SuppressWarnings("serial")
class Square extends JPanel
{
    /**
     * The row within the board of this Square. Rows are numbered from zero; row zero is at the bottom of the board.
     */
    private int row;

    /**
     * The column within the board of this Square. Columns are numbered from zero; column zero is at the left
     */
    private int col;

    /** The current Color of this Square */
    private Color color;

    /**
     * Creates a square and records its row and column
     */
    public Square (int row, int col)
    {
        this.row = row;
        this.col = col;
        this.color = BACKGROUND_COLOR;
    }

    /**
     * Returns the row of this Square
     */
    public int getRow ()
    {
        return row;
    }

    /**
     * Returns the column of this Square
     */
    public int getCol ()
    {
        return col;
    }

    /**
     * Sets the color of this square
     */
    public void setColor (Color color)
    {
        this.color = color;
    }

    /**
     * Paints this Square onto g
     */
    @Override
    public void paintComponent (Graphics g)
    {
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(BACKGROUND_COLOR);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(color);
        g.fillOval(BORDER, BORDER, getWidth() - 2 * BORDER, getHeight() - 2 * BORDER);
    }
}
