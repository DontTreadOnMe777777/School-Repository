package animation;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class Animation_Real
{
    /** Gravitational constant */
    private final static double G = 0;

    /** Initial velocity */
    private final static double V = 0.58;

    /** Interval in msec between cannon fires */
    private final static int INTERVAL = 2500;

    /** The diameter of a ball */
    private final static int BALL_DIAM = 30;

    /** Font size of the title */
    private final static int FONT_SIZE = 36;

    /** Name to display in title bar */
    public final static String STUDENT_NAME = "Brandon Walters";  // PUT YOUR NAME HERE!!!

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
       drawBlueBall(g, time, 0, Color.BLUE, false);
       drawRedBall(g, time, 0, Color.RED, false);
       
       if (time < 1.0 * INTERVAL) {
           drawTitle(g, "Go!");
       }
       
       else if (time < 4.0 * INTERVAL) {
           drawTitle(g, "Red's pulling away!");
       }
       
       else if (time < 7.5 * INTERVAL) {
           drawTitle(g, "Red wins the race!");
       }
       
       return time >= 10 * INTERVAL;
    }
    
    
    
    public static void drawTitle (Graphics2D g, String title)
    {
        // Use a larger than default font for the title
        Font f = g.getFont();
        g.setFont(new Font(f.getName(), f.getStyle(), FONT_SIZE));
        g.drawString(title, FONT_SIZE, FONT_SIZE);
    }
    
    public static void drawRedBall (Graphics2D g, double time, double angle, Color color, boolean disappear)
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
                double x = V * time * 1.5;
                double y = 300;

                // Display the ball. Note that the x coordinate increases from left to right, and that the
                // y coordinate increases from top to bottom. To make the ball appear higher, we must make
                // the y coordinate smaller.
                g.setColor(color);
                g.fillOval((int) Math.rint(x + 80 - BALL_DIAM / 2), (int) Math.rint(300 - y - BALL_DIAM),
                        BALL_DIAM, BALL_DIAM);
            }
        }
    }

    
    public static void drawBlueBall (Graphics2D g, double time, double angle, Color color, boolean disappear)
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
                double x = V * time;
                double y = 400;

                // Display the ball. Note that the x coordinate increases from left to right, and that the
                // y coordinate increases from top to bottom. To make the ball appear higher, we must make
                // the y coordinate smaller.
                g.setColor(color);
                g.fillOval((int) Math.rint(x + 80 - BALL_DIAM / 2), (int) Math.rint(400 - y - BALL_DIAM),
                        BALL_DIAM, BALL_DIAM);
            }
        }
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}
       