// Written by Joe Zachary for CS 1410, September 2015
// Modified by Stephen DeBies for CS 1410, September 2018

package animation;

import java.awt.*;
import java.awt.event.*;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 * Provides a top-level animation window
 */
@SuppressWarnings("serial")
public class AnimationWindow extends JFrame implements ActionListener
{
    /** Animation refresh rate in milliseconds */
    private final static int REFRESH_RATE = 33;

    /** Initial height of window */
    private final static int HEIGHT = 700;

    /** Initial idth of window */
    private final static int WIDTH = 1500;

    private DrawingArea area;       // JPanel that contains animation
    private JButton reverseButton;  // Reverse button
    private JButton playButton;     // Start button
    private JButton resetButton;    // Reset button
    private Timer timer;            // Animation timer
    private boolean forwardPlay;    // Direction of play
    private String author;          // Will display on window title

    /**
     * Launches an AnimationWindow
     */
    public static void main (String[] args)
    {
        SwingUtilities.invokeLater( () -> {
            AnimationWindow g = new AnimationWindow();
            g.setSize(WIDTH, HEIGHT);
            g.setVisible(true);
        });
    }

    /**
     * Constructs a custom CS 1410 graphics window
     */
    public AnimationWindow ()
    {
        author = Animation.STUDENT_NAME;
        setTitle("CS 1410 Animation Window" + " - " + author);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        timer = new Timer(REFRESH_RATE, this);
        JLabel timeLabel = new JLabel();
        area = new DrawingArea(timeLabel, this);
        playButton = new JButton("Play");
        playButton.addActionListener(this);
        resetButton = new JButton("Reset");
        resetButton.addActionListener(this);
        resetButton.setEnabled(false);
        reverseButton = new JButton("Reverse");
        reverseButton.addActionListener(this);
        reverseButton.setEnabled(false);
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(area, "Center");
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.add(reverseButton);
        buttonsPanel.add(playButton);
        buttonsPanel.add(resetButton, "South");
        mainPanel.add(buttonsPanel, "South");
        timeLabel.setText("xxx");
        JPanel timerPanel = new JPanel();
        timerPanel.add(timeLabel);
        mainPanel.add(timerPanel, "North");
        Font timerFont = timeLabel.getFont();
        Font font = new Font(timerFont.getName(), timerFont.getStyle(), timerFont.getSize() * 3);
        timeLabel.setFont(font);
        playButton.setFont(font);
        reverseButton.setFont(font);
        resetButton.setFont(font);
        setContentPane(mainPanel);
        this.forwardPlay = true;
    }

    /**
     * Handles button presses and timer events.
     */
    public void actionPerformed (ActionEvent e)
    {

        // Timer to generate a new frame
        if (e.getSource() == timer)
        {
            synchronized (area)
            {
                if (forwardPlay)
                    area.frameAdvance();
                else
                {
                    area.frameReverse();

                    if (area.getTime() <= 0)
                    {
                        reverseButton.setEnabled(false);
                        resetButton.setEnabled(false);
                    }
                }
            }
            area.repaint();
        }

        // Start button has been pressed
        else if (e.getSource() == playButton)
        {
            if (playButton.getText().equals("Play"))
            {
                playButton.setText("Pause");
                reverseButton.setText("Reverse");
                timer.start();
                forwardPlay = true;
                reverseButton.setEnabled(true);
                resetButton.setEnabled(true);
                synchronized (area)
                {
                    area.startTimer();
                }
            }
            else // if (start.getText().equals("Pause"))
            {
                playButton.setText("Play");
                timer.stop();
                synchronized (area)
                {
                    area.pauseTimer();
                }
            }
        }

        // Start button has been pressed
        else if (e.getSource() == reverseButton)
        {
            // System.out.println("inside: == reverseButton");

            if (reverseButton.getText().equals("Reverse"))
            {
                reverseButton.setText("Pause");
                playButton.setText("Play");
                playButton.setEnabled(true);
                timer.start();
                forwardPlay = false;
                synchronized (area)
                {
                    area.startTimer();
                }
            }
            else // if (start.getText().equals("Pause"))
            {
                reverseButton.setText("Reverse");
                timer.stop();
                synchronized (area)
                {
                    area.pauseTimer();
                }
            }
        }

        // Reset button has been pressed
        else if (e.getSource() == resetButton)
        {
            timer.stop();
            synchronized (area)
            {
                area.clearTimer();
            }
            playButton.setText("Play");
            playButton.setEnabled(true);
            reverseButton.setText("Reverse");
            reverseButton.setEnabled(false);
            resetButton.setEnabled(false);
            area.repaint();
        }
    }
    
    /**
     * Stops the animation because the end has been reached
     */
    public void stop ()
    {
        reverseButton.setText("Reverse");
        playButton.setEnabled(false);
        timer.stop();
        synchronized (area)
        {
            area.pauseTimer();
        }
    }
}
