package minesweeper;

import static minesweeper.MinesweeperView.BOARD_COLOR;
import static minesweeper.MinesweeperView.BUTTON_SIZE;
import static minesweeper.MinesweeperView.COLS;
import static minesweeper.MinesweeperView.ROWS;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.TreeMap;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

/**
 * Implements a Mine sweeper game with a GUI interface.
 * 
 * @author Stephen DeBies, November 2017
 */
@SuppressWarnings("serial")
public class MinesweeperView extends JFrame implements ActionListener
{
    static int ROWS;
    static int COLS;
    static int BOMBS;
    public final static int BUTTON_SIZE = 20;
    public final static int HEADER_FOOTER_SIZE = 175;
    public final static int SQUARE_BORDER_SIZE = 5;

    // COLORS
    public final static Color BACKGROUND_COLOR = Color.GRAY;
    public final static Color FOREGROUND_COLOR = Color.DARK_GRAY;
    public final static Color BOARD_COLOR = Color.BLACK;
    public final static Color BUTTON_COLOR_1 = Color.WHITE;

    // FONTS
    public final static int FONT_SIZE_LABEL = 30; // 60; //20; //BOMB COUNT AND TIME
    public final static int FONT_SIZE_BUTTON = 9; // 15; //20; //START AND CLEAR BOARD

    /** The "smarts" of the game **/
    private MinesweeperModel model;

    private JPanel middleTopPanel;
    private JLabel bombCountLabel;
    private JButton smileButton;
    private JLabel timeLabel;

    /** The portion of the GUI that contains the playing surface **/
    public Board board;

    private Timer timer;
    private int currentTime;
    public static final TreeMap<String, Image> IMAGES = new TreeMap<>();
    public static final TreeMap<String, ImageIcon> IMAGE_ICONS = new TreeMap<>();

    static
    {
        // Set up the images
        readImageFiles();
    }

    // CONSTRUCTOR
    public MinesweeperView ()
    {
        this(12, 12, 5, null);
    }

    // CONSTRUCTOR
    /**
     * Creates and initializes the game.
     */
    public MinesweeperView (int rows, int cols, int bombs, MinesweeperView view)
    {
        if (view != null)
            view.dispose(); // Kill the old window

        ROWS = rows;
        COLS = cols;
        BOMBS = bombs;

        // Set the title that appears at the top of the window
        setTitle("CS 1410 - Minesweeper");
        // Cause the application to end when the windows is closed
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Give the window its initial dimensions
        setSize((BUTTON_SIZE * COLS) + 40, HEADER_FOOTER_SIZE + (BUTTON_SIZE * ROWS));
        // Set window so the size (width & height) can't be changed
        setResizable(false);
        // this.setLocation(600, 150); // this.getWidth(), this.getHeight());

        // GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        //GraphicsDevice[] gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
        // System.out.println("arraysize=" + gd.length);
        // System.out.println("array[0]=" + gd.[0]);
        Point p = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
        // int screenWidth = gd.getDisplayMode().getWidth();
        // int screenHeight = gd.getDisplayMode().getHeight();
        // int xLoc = (screenWidth / 2) - (getWidth() / 2);
        // int yLoc = (screenHeight / 2) - (getHeight() / 2);
        // setLocation(xLoc, yLoc);
        setLocation(p.x - (getWidth() / 2), p.y - (getHeight() / 2));
        // System.out.println(screenWidth + " " + getWidth() + " " + xLoc);
        // System.out.println(screenHeight + " " + getHeight() + " " + yLoc);

        timer = new Timer(1000, this);
        currentTime = 0;

        createMenus();

        // CREATE WINDOW
        // The main panel contains everything
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        setContentPane(mainPanel);

        // DEFINE TOP LABELS

        // TOP PANEL - The top portion contains remaining bomb count, smiley face, time
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        mainPanel.add(topPanel, "North");

        // TOP LEFT PANEL - BOMBS COUNT
        JPanel leftTopPanel = new JPanel();
        bombCountLabel = new JLabel();
        bombCountLabel.setFont(new Font("Courier", Font.BOLD, FONT_SIZE_LABEL));
        bombCountLabel.setOpaque(true);
        bombCountLabel.setBackground(Color.BLACK);
        bombCountLabel.setForeground(Color.RED);
        leftTopPanel.add(bombCountLabel);
        topPanel.add(leftTopPanel, "West");

        // TOP MIDDLE PANEL - SMILEY FACE
        smileButton = new JButton(IMAGE_ICONS.get("smile"));
        smileButton.setPreferredSize(new Dimension(BUTTON_SIZE * 2, BUTTON_SIZE * 2));
        smileButton.addActionListener(this);
        middleTopPanel = new JPanel();
        middleTopPanel.add(smileButton);
        middleTopPanel.setSize(BUTTON_SIZE * 12, BUTTON_SIZE * 12);
        topPanel.add(middleTopPanel, "Center");

        // TOP RIGHT PANEL - TIME LABEL
        JPanel rightTopPanel = new JPanel();
        timeLabel = new JLabel("000");
        timeLabel.setFont(new Font("Courier", Font.BOLD, FONT_SIZE_LABEL));
        timeLabel.setOpaque(true);
        timeLabel.setBackground(Color.BLACK);
        timeLabel.setForeground(Color.RED);
        timeLabel.setSize(1000, 1000);
        timeLabel.setBorder(new EmptyBorder(0, SQUARE_BORDER_SIZE, 0, SQUARE_BORDER_SIZE));
        rightTopPanel.add(timeLabel);
        topPanel.add(rightTopPanel, "East");

        // DEFINE BUTTONS - BOTTOM PANEL
        JButton newGameButton = new JButton("New Game");
        newGameButton.setFont(new Font("SansSerif", Font.BOLD, FONT_SIZE_BUTTON));
        newGameButton.addActionListener(this);

        JButton clearBoardButton = new JButton("Clear Board");
        clearBoardButton.setFont(new Font("SansSerif", Font.BOLD, FONT_SIZE_BUTTON));
        clearBoardButton.setForeground(BUTTON_COLOR_1);
        clearBoardButton.setBackground(BACKGROUND_COLOR);
        clearBoardButton.addActionListener(this);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(newGameButton);
        buttonPanel.add(clearBoardButton);
        mainPanel.add(buttonPanel, "South");

        // The center portion contains the playing board
        model = new MinesweeperModel(ROWS, COLS, BOMBS);
        board = new Board(model, this);
        mainPanel.add(board, "Center");
    }

    /**
     * Creates and caches the images and icons used by the program. If any are missing, displays an error message and
     * exits.
     */
    private static void readImageFiles ()
    {
        try
        {
            // IMAGES ON SQUARES
            IMAGES.put("1", ImageIO.read(MinesweeperView.class.getResource("/images/num1.JPG")));
            IMAGES.put("2", ImageIO.read(MinesweeperView.class.getResource("/images/num2.JPG")));
            IMAGES.put("3", ImageIO.read(MinesweeperView.class.getResource("/images/num3.JPG")));
            IMAGES.put("4", ImageIO.read(MinesweeperView.class.getResource("/images/num4.JPG")));
            IMAGES.put("5", ImageIO.read(MinesweeperView.class.getResource("/images/num5.JPG")));
            IMAGES.put("6", ImageIO.read(MinesweeperView.class.getResource("/images/num6.JPG")));
            IMAGES.put("7", ImageIO.read(MinesweeperView.class.getResource("/images/num7.JPG")));
            IMAGES.put("8", ImageIO.read(MinesweeperView.class.getResource("/images/num8.JPG")));
            IMAGES.put("9", ImageIO.read(MinesweeperView.class.getResource("/images/bomb.JPG")));
            IMAGES.put("10", ImageIO.read(MinesweeperView.class.getResource("/images/bomb_red.png")));
            IMAGES.put("11", ImageIO.read(MinesweeperView.class.getResource("/images/bomb_cross.JPG")));

            IMAGE_ICONS.put("blank",
                    scaleImage(ImageIO.read(MinesweeperView.class.getResource("/images/button.png")), 1.0));
            IMAGE_ICONS.put("flag",
                    scaleImage(ImageIO.read(MinesweeperView.class.getResource("/images/button_flag.png")), 1.0));
            IMAGE_ICONS.put("?", scaleImage(
                    ImageIO.read(MinesweeperView.class.getResource("/images/button_questionmark.png")), 1.0));

            IMAGE_ICONS.put("smile",
                    scaleImage(ImageIO.read(MinesweeperView.class.getResource("/images/face_smile.JPG")), 2.0));
            IMAGE_ICONS.put("frown",
                    scaleImage(ImageIO.read(MinesweeperView.class.getResource("/images/face_frown.JPG")), 2.0));
            IMAGE_ICONS.put("surprised",
                    scaleImage(ImageIO.read(MinesweeperView.class.getResource("/images/face_surprised.JPG")), 2.0));
            IMAGE_ICONS.put("sunglasses",
                    scaleImage(ImageIO.read(MinesweeperView.class.getResource("/images/face_sunglasses.JPG")), 2.0));
        }
        catch (Exception e)
        {
            JOptionPane.showInternalMessageDialog(null, e.getMessage() + "\n\n Program will exit.");
            System.exit(1);
        }
    }

    /**
     * Scales an image by the given scaling factor and creates an ImageIcon from it.
     */
    private static ImageIcon scaleImage (Image image, double scale)
    {
        image = image.getScaledInstance((int) (BUTTON_SIZE * scale), (int) (BUTTON_SIZE * scale),
                java.awt.Image.SCALE_SMOOTH);
        return new ImageIcon(image);
    }

    /**
     * Lays out the menus for the GUI
     */
    private void createMenus ()
    {
        // MENU BAR
        JMenuBar menuBar = new JMenuBar();
        this.setJMenuBar(menuBar);
        // this.setContentPane(menuBar);

        // MENU ITEM 1
        JMenu menu1 = new JMenu("Game");
        menuBar.add(menu1);
        JMenuItem menuItem11 = new JMenuItem("New");
        JMenuItem menuItem12 = new JMenuItem("Debug");
        JMenuItem menuItem13 = new JMenuItem("Beginner");
        JMenuItem menuItem14 = new JMenuItem("Intermediate");
        JMenuItem menuItem15 = new JMenuItem("Expert");
        JMenuItem menuItem16 = new JMenuItem("Custom");
        JMenuItem menuItem17 = new JMenuItem("Marks(?)");
        JMenuItem menuItem18 = new JMenuItem("Best Times...");
        JMenuItem menuItem19 = new JMenuItem("Exit");
        menu1.add(menuItem11);
        menu1.addSeparator();
        menu1.add(menuItem12);
        menu1.add(menuItem13);
        menu1.add(menuItem14);
        menu1.add(menuItem15);
        menu1.add(menuItem16);
        menu1.addSeparator();
        menu1.add(menuItem17);
        menu1.add(menuItem18);
        menu1.addSeparator();
        menu1.add(menuItem19);

        // MENU ITEM 2
        JMenu menu2 = new JMenu("Help");
        menuBar.add(menu2);
        JMenuItem menuItem21 = new JMenuItem("Help Topics");
        JMenuItem menuItem22 = new JMenuItem("About Minesweeper");
        menu2.add(menuItem21);
        menu2.add(menuItem22);

        // MENU ITEM 3
        JMenu menu3 = new JMenu("Extras");
        menuBar.add(menu3);
        JMenuItem menuItem31 = new JMenuItem("Statistics Window");
        JMenuItem menuItem32 = new JMenuItem("Play Recording...");
        JMenuItem menuItem33 = new JMenuItem("Save Recording");
        JMenuItem menuItem34 = new JMenuItem("Save Board...");
        JMenuItem menuItem35 = new JMenuItem("Auto Options...");
        JMenuItem menuItem36 = new JMenuItem("Win XP Skin");
        JMenuItem menuItem37 = new JMenuItem("Win 98 Skin");
        JMenuItem menuItem38 = new JMenuItem("Custom Skin");
        menu3.add(menuItem31);
        menu3.addSeparator();
        menu3.add(menuItem32);
        menu3.addSeparator();
        menu3.add(menuItem33);
        menu3.add(menuItem34);
        menu3.add(menuItem35);
        menu3.addSeparator();
        menu3.add(menuItem36);
        menu3.add(menuItem37);
        menu3.add(menuItem38);

        menuItem11.addActionListener(this);
        menuItem12.addActionListener(this);
        menuItem13.addActionListener(this);
        menuItem14.addActionListener(this);
        menuItem15.addActionListener(this);
        menuItem16.addActionListener(this);
        menuItem17.addActionListener(this);
        menuItem18.addActionListener(this);
        menuItem19.addActionListener(this);
    }

    /**
     * Sets the label that displays bombs remaining
     */
    public void setBombsRemainingLabel (int n)
    {
        bombCountLabel.setText(String.format("%03d", n));
    }

    /**
     * Sets the label that displays time elapsed
     */
    public void setTimeLabel (int n)
    {
        timeLabel.setText(String.format("%03d", n));
    }

    /**
     * Changes the requested face image on the top bar.
     */
    public void setSmileyFaceImage (String s)
    {
        middleTopPanel.remove(smileButton);
        ImageIcon imageIcon = IMAGE_ICONS.get(s);
        smileButton = new JButton(imageIcon);
        smileButton.setPreferredSize(new Dimension(BUTTON_SIZE * 2, BUTTON_SIZE * 2));
        smileButton.addActionListener(this);
        middleTopPanel.add(smileButton);
        smileButton.revalidate();
    }

    /**
     * Starts the game timer
     */
    public void startTimer ()
    {
        if (timer.isRunning() == false)
        {
            timer.start();
        }
    }

    /**
     * Stops the game timer
     */
    public void stopTimer ()
    {
        if (timer.isRunning())
        {
            timer.stop();
        }
    }

    /**
     * CALLED WHEN THE "NEW GAME" OR "CLEAR BOARD" BUTTON IS CLICKED. Tells the model to begin a new game, then
     * refreshes the display.
     */
    @Override
    public void actionPerformed (ActionEvent e)
    {
        // UNCOMMENT THIS PRINT STATEMENT TO DETERMINE WHEN THIS METHOD IS ACTIVATED
        // System.out.println("MinesweeperView : ActionPerformed");

        if (e.getSource() instanceof JButton)
        {
            JButton b = (JButton) e.getSource();

            if (b.getText().equals("New Game"))
            {
                newGame();
            }
            else if (b.getText().equals("Clear Board"))
            {
                timer.stop();
                model.uncoverAll();
                board.refreshBoard();
            }
            else // if smileyface
            {
                newGame();
            }
        }
        else if (e.getSource() instanceof JMenuItem)
        {
            // System.out.println("inside menu");
            JMenuItem m = (JMenuItem) e.getSource();

            if (m.getText().equals("New"))
            {
                newGame();
            }
            else if (m.getText().equals("Debug"))
            {
                MinesweeperView display = new MinesweeperView(12, 12, 5, this);
                display.setVisible(true);
            }
            else if (m.getText().equals("Beginner"))
            {
                MinesweeperView display = new MinesweeperView(8, 8, 10, this);
                display.setVisible(true);
            }
            else if (m.getText().equals("Intermediate"))
            {
                MinesweeperView display = new MinesweeperView(16, 16, 40, this);
                display.setVisible(true);
            }
            else if (m.getText().equals("Expert"))
            {
                MinesweeperView display = new MinesweeperView(16, 31, 99, this);
                display.setVisible(true);
            }
            else if (m.getText().equals("Custom"))
            {
                // NOTE: POPUP WINDOW ONLY HAS ONE BOX
                try
                {
                    String input = JOptionPane.showInputDialog("Custom Game: \nEnter 'Rows, Columns, #ofBombs'");
                    String[] array = input.split(",");
                    int rowsInt = Integer.parseInt(array[0]);
                    int colsInt = Integer.parseInt(array[1]);
                    int bombsInt = Integer.parseInt(array[2]);
                    if (rowsInt >= 8 && rowsInt <= 30 && colsInt >= 8 && colsInt <= 70 && bombsInt > 0)
                    {
                        MinesweeperView display = new MinesweeperView(rowsInt, colsInt, bombsInt, this);
                        display.setVisible(true);
                    }
                    else
                    {
                        throw new RuntimeException();
                    }
                }
                catch (Exception ex)
                {
                    JOptionPane.showMessageDialog(null, "Window size is too big, too small, or with no bombs."
                            + "\nLimits: rows 8-30, cols 8-70, bombs > 0");
                }
            }
            else if (m.getText().equals("Best Times..."))
            {
                // NOTE: NOT FINISHED
                System.out.println("best times...");
                JOptionPane.showInternalMessageDialog(null, "Best Times");
            }
            else if (m.getText().equals("Exit"))
            {
                System.exit(0);
            }
        }
        // TIMER ACTIVATES EVERY SECOND (1000 msec)
        else
        {
            currentTime++;
            setTimeLabel(currentTime);
        }
    }

    private void newGame ()
    {
        timer.stop();
        currentTime = 0;
        setTimeLabel(0);
        model.newGame(ROWS, COLS);
        board.refreshBoard();
    }
}

/*****************************************************************************************/
/*****************************************************************************************/
/**
 * The playing surface of the game.
 */
@SuppressWarnings("serial")
class Board extends JPanel implements MouseListener, ActionListener// , KeyListener
{
    /**
     * The intelligence of the game. A Java class that contains methods that control the data and logic of the game.
     */
    private MinesweeperModel model;

    /**
     * The top level GUI for the game. A Java class that defines the window and board on which the game is played.
     */
    public MinesweeperView view;

    // CONSTRUCTOR
    /**
     * Creates the board portion of the GUI.
     */
    public Board (MinesweeperModel model, MinesweeperView view)
    {
        // Record the model and the top-level display
        this.model = model;
        this.view = view;

        // Set the background color and the layout
        setBackground(BOARD_COLOR);
        setLayout(new GridLayout(ROWS, COLS));

        refreshBoard();
    }

    /**
     * Refreshes the display. This should be called whenever something changes in the model.
     */
    public void refreshBoard ()
    {
        // Remove all previous squares, Iterate through all of the squares
        Component[] squares = getComponents();
        for (Component c : squares)
        {
            remove(c);
        }
        // Draw new squares
        for (int row = ROWS - 1; row >= 0; row--)
        {
            for (int col = 0; col < COLS; col++)
            {
                // SQUARE COVERED WITH BUTTON
                if (model.getIsSquareCovered(row, col) == true)
                {
                    JButton button = new JButton(MinesweeperView.IMAGE_ICONS.get(model.getButtonPic(row, col)));

                    if (!model.isGameWon() && !model.getIsMineFound())
                    {
                        button.addActionListener(this); // LEFT-CLICK
                        button.addMouseListener(this);  // RIGHT-CLICK
                        button.setName(row + ":" + col); // USED BY RIGHT-CLICK & LEFT-CLICK
                        button.setActionCommand(model.getButtonPic(row, col));
                    }
                    add(button);
                }
                // SQUARE UNCOVERED
                else
                {
                    int number = model.getNumberInSquare(row, col);
                    Image image;
                    Square square;
                    if (number == 0)
                    {
                        image = null;
                        square = new Square(row, col);
                    }
                    else
                    {
                        image = MinesweeperView.IMAGES.get(number + "");
                        square = new Square(row, col, image);
                    }
                    square.setSize(BUTTON_SIZE, BUTTON_SIZE);
                    add(square); // add square to the board
                }
            }
        }

        view.setBombsRemainingLabel(model.getMinesNotMarkedCount());

        if (model.isGameWon() || model.getIsMineFound())
        {
            view.stopTimer();

            if (model.isGameWon())
            {
                view.setSmileyFaceImage("sunglasses");
            }
            else if (model.getIsMineFound())
            {
                view.setSmileyFaceImage("frown");
            }
        }
        else
        {
            view.setSmileyFaceImage("smile");
        }

        // Ask that this board be repainted
        revalidate();
    }

    /**
     * CALLED WHENEVER A SQUARE IS RIGHT-CLICKED WITH THE MOUSE. Notifies the model that a square has been clicked and
     * changes what is displayed on the button.
     */
    @Override
    public void mouseClicked (MouseEvent e)
    {
        // UNCOMMENT THIS PRINT STATEMENT TO DETERMINE WHEN THIS METHOD IS ACTIVATED
        // System.out.println("Board : MouseClicked");

        if (model.isGameWon() || model.getIsMineFound())
            return;

        if (e.getSource() instanceof JButton)
        {
            JButton button = (JButton) e.getSource();

            // IF RIGHT-CLICK
            if (e.getButton() == MouseEvent.BUTTON3)
            {
                int colonLoc = button.getName().indexOf(":");
                int row = Integer.parseInt(button.getName().substring(0, colonLoc));
                int col = Integer.parseInt(button.getName().substring(colonLoc + 1));

                if (button.getActionCommand().equals("blank"))
                {
                    model.setButtonPic("flag", row, col);
                    model.setMinesNotMarkedCount(model.getMinesNotMarkedCount() - 1);
                }
                else if (button.getActionCommand().equals("flag"))
                {
                    model.setButtonPic("?", row, col);
                    model.setMinesNotMarkedCount(model.getMinesNotMarkedCount() + 1);
                }
                else if (button.getActionCommand().equals("?"))
                {
                    model.setButtonPic("blank", row, col);
                }
            }
        }
        refreshBoard();
    }

    @Override
    public void mousePressed (MouseEvent e)
    {
        if (e.getButton() == 1)
        {
            view.setSmileyFaceImage("surprised");
        }
    }

    @Override
    public void mouseReleased (MouseEvent e)
    {
        if (e.getButton() == 1 && !model.isGameWon() && !model.getIsMineFound())
        {
            view.setSmileyFaceImage("smile");
        }
    }

    @Override
    public void mouseEntered (MouseEvent e)
    {
        // UNCOMMENT THIS PRINT STATEMENT TO DETERMINE WHEN THIS METHOD IS ACTIVATED
        // System.out.println("Board : MouseEntered");
    }

    @Override
    public void mouseExited (MouseEvent e)
    {
        // UNCOMMENT THIS PRINT STATEMENT TO DETERMINE WHEN THIS METHOD IS ACTIVATED
        // System.out.println("Board : MouseExited");
    }

    // FOR THE BOARD CLASS
    @Override
    public void actionPerformed (ActionEvent e)
    {
        // UNCOMMENT THIS PRINT STATEMENT TO DETERMINE WHEN THIS METHOD IS ACTIVATED
        // System.out.println("Board : ActionPerformed");
        // System.out.println("e=" + e);

        if (model.isGameWon() || model.getIsMineFound())
        {
            return;
        }

        // ACTIVATED WHEN BUTTON ON GRID BOARD IS LEFT-CLICKED
        if (e.getSource() instanceof JButton)
        {
            view.startTimer();
            JButton b = (JButton) e.getSource();

            // if(b.getText().equals("X")==false)
            if (b.getActionCommand().equals("X") == false)
            {
                int colonLoc = b.getName().indexOf(":");
                int row = Integer.parseInt(b.getName().substring(0, colonLoc));
                int col = Integer.parseInt(b.getName().substring(colonLoc + 1));
                model.uncover(row, col);
            }
            refreshBoard();
        }
    }
}

/*****************************************************************************************/
/*****************************************************************************************/
/**
 * A single square on the board where a move can be made
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

    /** The image displayed in the square */
    private Image image;

    /** Width of the square's border */
    private int border = 1;

    // CONSTRUCTORS
    /** Creates a square and records its row and column. */
    public Square (int row, int col)
    {
        this.row = row;
        this.col = col;
        this.setSize(BUTTON_SIZE, BUTTON_SIZE);
    }

    /** Creates a square and records its row, column, and image that will be displayed. */
    public Square (int row, int col, Image image)
    {
        this.row = row;
        this.col = col;
        this.image = image;
        this.setSize(BUTTON_SIZE, BUTTON_SIZE);
    }

    /**
     * Returns the row of this Square.
     */
    public int getRow ()
    {
        return row;
    }

    /**
     * Returns the column of this Square.
     */
    public int getCol ()
    {
        return col;
    }

    /**
     * Paints this Square onto g.
     */
    @Override
    public void paintComponent (Graphics g)
    {
        // SQUARE BACKGROUND
        g.setColor(Color.GRAY);
        g.fillRect(0, 0, getWidth(), getHeight());

        // DRAW IMAGE
        if (image != null)
        {
            g.drawImage(image, border, border, getWidth() - (2 * border), getHeight() - (2 * border), null);
        }
        // DRAW BLANK SQUARE
        else
        {
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(border, border, getWidth() - (2 * border), getHeight() - (2 * border));
        }
    }
}
