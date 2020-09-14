package animation;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class Animation_SourceCode
{
    /** Gravitational constant */
    private final static double G = .0002;

    /** Initial velocity */
    private final static double V = 0.58;

    /** Interval in msec between cannon fires */
    private final static int INTERVAL = 2500;

    /** The x coordinate of the cannon */
    private final static int CANNON_X = 80;

    /** The y coordinate of the cannon */
    private final static int CANNON_Y = 475;

    /** The diameter of a ball */
    private final static int BALL_DIAM = 20;

    /** The length of the cannon */
    private final static int CANNON_LENGTH = 80;

    /** The height of the cannon */
    private final static int CANNON_HEIGHT = 30;

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
        // Draw the balls. Notice that the times are offset so that no ball will be displayed
        // until it is fired.
        drawBall(g, time, getAngle(0), Color.GREEN, false);
        drawBall(g, time - INTERVAL, getAngle(INTERVAL), Color.RED, false);
        drawBall(g, time - 2 * INTERVAL, getAngle(2 * INTERVAL), Color.BLUE, false);
        drawBall(g, time - 3 * INTERVAL, getAngle(3 * INTERVAL), Color.ORANGE, false);
        drawBall(g, time - 4 * INTERVAL, getAngle(4 * INTERVAL), Color.CYAN, true);

        // Get the angle currently being used by the cannon
        int angle = getAngle(time);

        // Until halfway through the last interval, display the cannon and the angle
        if (time < 3.5 * INTERVAL + getDuration(angle))
        {
            drawCannon(g, width);
            drawTitle(g, angle + " degrees");
        }

        // During the second half of the last interval, display the cannon and "Uh Oh"
        else if (time < 4 * INTERVAL + getDuration(angle))
        {
            drawCannon(g, width);
            drawTitle(g, "Uh Oh");
        }

        // After the last interval display a scene of destruction
        else
        {
            drawDestruction(g, width);
        }

        // Stop the animation if it has run long enough
        return time >= 5.5 * INTERVAL;
    }

    /**
     * Return the angle (in degrees) at which the cannon is aimed at the given time.
     */
    public static int getAngle (int time)
    {
        if (time < INTERVAL)
        {
            return 30;
        }
        else if (time < 2 * INTERVAL)
        {
            return 45;
        }
        else if (time < 3 * INTERVAL)
        {
            return 55;
        }
        else if (time < 4 * INTERVAL)
        {
            return 80;
        }
        else
        {
            return 90;
        }
    }

    /**
     * Draws a cannon ball as it would appear at the given time (in msec), assuming it was fired at the given angle at
     * time zero. The provided color is used. Nothing is displayed for negative times. If the ball would be on the
     * ground at the given time, it is displayed on the ground unless disappear is true.
     */
    public static void drawBall (Graphics2D g, double time, double angle, Color color, boolean disappear)
    {
        // Display something only for non-negative times
        if (time >= 0)
        {
            // Get the duration of the flight of a ball fired at the given angle
            double duration = getDuration(angle);

            // If the ball has hit the ground, adjust the time back to the point when it hit the ground
            time = Math.min(time, duration);

            // Display the ball if it hasn't hit the ground or if it isn't supposed to disappear
            // when it does hit the ground
            if (time < duration || !disappear)
            {
                // Compute the x and y coordinates of the ball relative to the cannon
                double x = V * time * Math.cos(degreesToRadians(angle));
                double y = V * time * Math.sin(degreesToRadians(angle)) - G * time * time;

                // Display the ball. Note that the x coordinate increases from left to right, and that the
                // y coordinate increases from top to bottom. To make the ball appear higher, we must make
                // the y coordinate smaller.
                g.setColor(color);
                g.fillOval((int) Math.rint(x + CANNON_X - BALL_DIAM / 2), (int) Math.rint(CANNON_Y - y - BALL_DIAM),
                        BALL_DIAM, BALL_DIAM);
            }
        }
    }

    /**
     * Returns the time in msec that it takes a ball fired at the given angle to hit the ground.
     */
    public static double getDuration (double angle)
    {
        return 2 * V * Math.sin(degreesToRadians(angle)) / (2 * G);
    }

    /**
     * Draws the cannon and a placid background, given the width of the animation.
     */
    public static void drawCannon (Graphics2D g, int width)
    {
        g.setColor(Color.BLACK);
        g.drawLine(0, CANNON_Y, width, CANNON_Y);
        g.fillArc(CANNON_X - CANNON_LENGTH / 2, CANNON_Y - CANNON_HEIGHT, CANNON_LENGTH, CANNON_HEIGHT * 2, 0, 180);
    }

    /**
     * Draws the title in the upper-left corner
     */
    public static void drawTitle (Graphics2D g, String title)
    {
        // Use a larger than default font for the title
        Font f = g.getFont();
        g.setFont(new Font(f.getName(), f.getStyle(), FONT_SIZE));
        g.drawString(title, FONT_SIZE, FONT_SIZE);
    }

    /**
     * Draws a scene of destruction
     */
    public static void drawDestruction (Graphics2D g, int width)
    {
        // Set color for scene
        g.setColor(Color.BLACK);

        // Some values that control the drawing
        int left = CANNON_X - CANNON_LENGTH;
        int length = 5 * CANNON_LENGTH / 4;
        int top = CANNON_Y;
        int height = 2 * CANNON_HEIGHT;

        // Jagged segments
        g.drawLine(0, top, left, top);
        g.drawLine(left, top, left + length / 4, top + height / 5);
        g.drawLine(left + length / 4, top + height / 5, left + length / 2, top + height);
        g.drawLine(left + length / 2, top + height, left + length, top + 3*height / 4);
        g.drawLine(left + length, top + 3*height / 4, left + 3 * length / 2, top + 3 * height / 4);
        g.drawLine(left + 3 * length / 2, top + 3 * height / 4, left + 2 * length, top);
        g.drawLine(left + 2 * length, top, width, top);
    }

    /**
     * Converts the given angle from degrees into radians
     */
    public static double degreesToRadians (double degrees)
    {
        return degrees * Math.PI / 180;
    }
}
