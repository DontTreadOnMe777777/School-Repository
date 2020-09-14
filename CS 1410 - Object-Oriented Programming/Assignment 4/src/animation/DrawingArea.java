// Written by Joe Zachary  for CS 1410, September 2015
// Modified by Stephen DeBies for CS 1410, September 2018

package animation;

import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.*;

/**
 * Represents an area where animated graphics can be drawn.
 */
@SuppressWarnings("serial")
public class DrawingArea extends JPanel
{
    private int time = 0;            // Milliseconds that have elapsed since animation started
    private JLabel timeLabel;        // Label that displays elapsed time of animation
    private long startTime;          // System time when start button is clicked
    private int pauseTime;           // System time when pause button is clicked
    private AnimationWindow window;  // The top-level animation window
    
    /**
     * Creates a new DrawingArea
     */
    public DrawingArea (JLabel timeLabel, AnimationWindow window)
    {
        this.timeLabel = timeLabel;
        this.window = window;
    }

    /**
     * Increases the elapsed time by delta milliseconds.
     */
    public void frameAdvance ()
    {
        synchronized (this)
        {
            time = (int)(System.currentTimeMillis() - startTime + pauseTime);
        }
    }
    
    /**
     * Decreases the elapsed time by delta milliseconds.
     */
    public void frameReverse ()
    {
        synchronized (this)
        {
            time = (int) (startTime - System.currentTimeMillis() + pauseTime);
            if (time < 0) time = 0;
        }
    }
    
    /**
     * Sets startTime to current system time
     */
    public void startTimer ()
    {
        synchronized (this)
        {
            pauseTime = time;
            startTime = (int)(System.currentTimeMillis());
        }
    }

    /**
     * Keeps track of time when timer is paused
     */
    public void pauseTimer ()
    {
        synchronized (this)
        {
            pauseTime = time;
        }
    }
    
    /**
     * Resets elapsed time & pause time to zero
     */
    public void clearTimer ()
    {
        time = 0;
        pauseTime = 0;
    }

    public int getTime ()
    {
        return time;
    }
    
    /**
     * Paints the display area to the screen.
     */
    public void paintComponent (Graphics g)
    {
        g.clearRect(0, 0, getWidth(), getHeight());
        int ticks;
        synchronized (this)
        {
            ticks = time;
        }
        timeLabel.setText(String.format("%.2f", ticks / 1000.));
        if (Animation.paintFrame((Graphics2D) g, ticks, getHeight(), getWidth()))
        {
            window.stop();
        }
    }
}