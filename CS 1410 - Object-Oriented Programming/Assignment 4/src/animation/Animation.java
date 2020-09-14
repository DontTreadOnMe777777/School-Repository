package animation;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class Animation
{
    /** Initial velocity */
    private final static double V = 0.08;

    /** Interval in msec between cannon fires */
    private final static int INTERVAL = 2500;

    /** The diameter of a ball */
    private final static int BALL_DIAM = 30;
    
    /** The x coordinate of the cannon */
    private final static int CANNON_X = 80;

    /** The y coordinate of the cannon */
    private final static int CANNON_Y = 400;

    /** Font size of the title */
    private final static int FONT_SIZE = 36;

    /** Name to display in title bar */
    public final static String STUDENT_NAME = "Brandon Walters";  // PUT YOUR NAME HERE!!!
    
    public static int count = 0;

    /**
     * This is the method that you need to rewrite to create a custom animation. This method is called repeatedly as the
     * animation proceeds. It needs to draw on g how the animation should look after time milliseconds have passed.
     * 
     * The method returns true if the end of the animation has been reached or false if the animation should continue.
     * 
     * @param g    Graphics object on which to draw
     * @param time Number of milliseconds that have passed since animation started
     * @param height Current height of the frame
     * @param width Current width of the frame
     */
    public static boolean paintFrame (Graphics2D g, int time, int height, int width)
    {
       drawBall(g, time, 0, Color.BLUE, false);
       
       if (time < 1.5 * INTERVAL) {
           drawRect(g, time, 0, Color.RED, false);
       }
       
       else if (time < 1.6 * INTERVAL) {
           drawRect(g, time, 0, Color.GREEN, false);
       }
       
       else if (time < 1.7 * INTERVAL) {
           drawRect(g, time, 0, Color.RED, false);
       }
       
       else if (time < 1.8 * INTERVAL) {
           drawRect(g, time, 0, Color.GREEN, false);
       }
       
       else if (time < 1.9 * INTERVAL) {
           drawRect(g, time, 0, Color.RED, false);
       }
       
       else {
           drawRect(g, time, 0, Color.GREEN, false);
       }
       
       if (time < 1.0 * INTERVAL) {
           drawTitle(g, "Go!");
       }
       
       else if (time < 6.0 * INTERVAL) {
           drawTitle(g, "Oh no, Red's hurt!");
       }
       
       else if (time < 7.5 * INTERVAL) {
           drawTitle(g, "Blue wins the heat!");
           drawBall(g, time, 0, Color.YELLOW, false);
       }
       
       return time >= 7.6 * INTERVAL;
    }
    
    
    
    
    public static void drawTitle (Graphics2D g, String title)
    {
        // Use a larger than default font for the title
        Font f = g.getFont();
        g.setFont(new Font(f.getName(), f.getStyle(), FONT_SIZE));
        g.drawString(title, FONT_SIZE, FONT_SIZE);
    }
    
    public static void drawBall (Graphics2D g, double time, double angle, Color color, boolean disappear)
    {
        // Display something only for non-negative times
        if (time >= 0)
        {
            // Get the duration of the flight of a ball fired at the given angle
            double duration = 15;

            // Display the ball if it hasn't hit the ground or if it isn't supposed to disappear
            // when it does hit the ground
            if (time < duration || !disappear)
            {
                // Compute the x and y coordinates of the ball relative to the cannon
                double x = CANNON_X + (V * time);
                double y = CANNON_Y - 100;

                // Display the ball. Note that the x coordinate increases from left to right, and that the
                // y coordinate increases from top to bottom. To make the ball appear higher, we must make
                // the y coordinate smaller.
                g.setColor(color);
                g.fillOval((int) Math.rint(x), (int)Math.rint((y) - 100), 40, 40);
            }
        }
    }

    
    public static void drawRect (Graphics2D g, double time, double angle, Color color, boolean disappear)
    {
        // Display something only for non-negative times
        if (time >= 0)
        {
            // Get the duration of the flight of a ball fired at the given angle
            double duration = 15;

            // If the ball has hit the ground, adjust the time back to the point when it hit the ground
            time = Math.min(time, duration);

            // Display the ball if it hasn't hit the ground or if it isn't supposed to disappear
            // when it does hit the ground
            if (time < duration || !disappear)
            {
                // Compute the x and y coordinates of the ball relative to the cannon
                double x = CANNON_X + (V * time * 1.5);
                double y = CANNON_Y;

                // Display the ball. Note that the x coordinate increases from left to right, and that the
                // y coordinate increases from top to bottom. To make the ball appear higher, we must make
                // the y coordinate smaller.
                g.setColor(color);
                g.fillRect((int) Math.rint(x), (int)Math.rint(y), 40, 40);
            }
        }
    }   
}
       