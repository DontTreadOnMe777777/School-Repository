package asteroids.participants;

import static asteroids.game.Constants.*;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.util.Random;
import asteroids.destroyers.AlienDestroyer;
import asteroids.destroyers.AsteroidDestroyer;
import asteroids.destroyers.BulletDestroyer;
import asteroids.destroyers.ShipDestroyer;
import asteroids.game.Controller;
import asteroids.game.Participant;
import asteroids.game.ParticipantCountdownTimer;

public class Alien extends Participant implements AsteroidDestroyer, BulletDestroyer, ShipDestroyer
{
    /** The size of the alien ship (0 = small, 1 = large) */
    private int size;

    /** The outline of the alien ship */
    private Shape outline;

    /** The game controller */
    private Controller controller;

    /** A random number generator used throughout */
    private Random rand;

    /** Represents the side that the ship starts on */
    private boolean startsLeft;

    /** How long between each change in direction for the alien */
    public final static int ZIGZAG_DELAY = 1000;

    /** How long between each bullet from the alien */
    public final static int ALIEN_SHOOTING_DELAY = 2500;

    /** The size of the spawn range for the alien */
    public final static int ALIEN_REGION = 700;

    /** The speeds for the sizes of aliens */
    public final static double[] ALIEN_SPEED = { 7, 4 };

    /** Creates an alien ship that instantly starts moving */
    public Alien (int size, Controller controller)
    {
        // Initializes the random number generator
        rand = new Random();

        // Initializes the side that the ship starts on
        startsLeft = rand.nextBoolean();

        // Saves the controller and size
        this.controller = controller;
        this.size = size;

        // sets a random location off screen along one of the sides
        setPosition(-20, rand.nextInt(ALIEN_REGION));
        setRotation(Math.PI);

        // Draws the alien ship
        Path2D.Double poly = new Path2D.Double();
        poly.moveTo(17, 0);
        poly.lineTo(-17, 0);
        poly.lineTo(-7, 10);
        poly.lineTo(-5, 15);
        poly.lineTo(5, 15);
        poly.lineTo(7, 10);
        poly.lineTo(-7, 10);
        poly.lineTo(7, 10);
        poly.lineTo(17, 0);
        poly.lineTo(7, -10);
        poly.lineTo(-7, -10);
        poly.lineTo(-17, 0);
        poly.closePath();

        // Scale to the desired size
        double scale = ALIENSHIP_SCALE[size];
        poly.transform(AffineTransform.getScaleInstance(scale, scale));

        // Gets the ship ready to shoot
        new ParticipantCountdownTimer(this, "shooting", ALIEN_SHOOTING_DELAY);

        // Saves the outline
        outline = poly;
        firstMove();
    }

    /**
     * Sets the velocity for the alien's first move.
     */
    private void firstMove ()
    {
        if (startsLeft)
        {
            setVelocity(ALIEN_SPEED[size], 0);
        }
        else
        {
            setVelocity(ALIEN_SPEED[size], Math.PI);
        }
        new ParticipantCountdownTimer(this, "zigzag", ZIGZAG_DELAY);

    }

    @Override
    protected Shape getOutline ()
    {
        return outline;
    }

    @Override
    public void collidedWith (Participant p)
    {
        if (p instanceof AlienDestroyer)
        {
            // Expire the ship from the game
            Participant.expire(this);

            // Tell the controller the ship was destroyed
            controller.alienDestroyed(this);
        }
    }

    /**
     * Called when it is time for the alien to shoot
     */
    public void shoot ()
    {
        // only initializing to avoid an error
        double direction = 0;
        // if it is a large ship
        if (size == 1)
        {
            // fires in a random direction (in 15 degree increments)
            direction = rand.nextInt(25) * Math.PI / 12;
        }
        // if it is a small ship
        else if (size == 0)
        {
            // generates an angle between -5 degrees to 5 degrees
            double randAngle = (rand.nextInt(11) - 5) * Math.PI / 180;

            // adds the random angle to the direction to the player's ship
            direction = directionTo(this, controller.getShip()) + randAngle;
        }
        // puts an alien bullet in the game with the proper direction
        controller.addParticipant(new AlienBullet(direction, this));
    }

    /**
     * This method is invoked when a ParticipantCountdownTimer completes its countdown.
     */
    public void countdownComplete (Object payload)
    {
        // If the alien is supposed to change angle of movement
        if (payload.equals("zigzag"))
        {
            int randAngle = rand.nextInt(3) - 1;
            if (startsLeft)
            {
                setDirection(randAngle);
            }
            else
            {
                setDirection(randAngle + Math.PI);
            }
            new ParticipantCountdownTimer(this, "zigzag", ZIGZAG_DELAY);
        }

        // If the alien is supposed to shoot
        if (payload.equals("shooting"))
        {
            if (controller.getShip() != null)
            {
                {
                    shoot();
                    new ParticipantCountdownTimer(this, "shooting", ALIEN_SHOOTING_DELAY);
                }
            }
            else
            {
                new ParticipantCountdownTimer(this, "shooting", ALIEN_SHOOTING_DELAY);
            }
        }
    }

    /**
     * Returns the direction (in radians) from the first participant to the second participant
     */
    protected static double directionTo (Participant first, Participant second)
    {
        // Saves the coordinates
        double firstX = first.getX();
        double firstY = first.getY();
        double secondX = second.getX();
        double secondY = second.getY();

        // gets the length of the hypotenuse of the right triangle between the two points
        double hypotenuse = Math.sqrt(Math.pow((firstX - secondX), 2) + Math.pow((firstY - secondY), 2));

        // just makes sure to use the correct angle
        if (firstY < secondY)
        {
            return Math.acos((secondX - firstX) / hypotenuse);
        }
        else if (firstY > secondY)
        {
            return Math.acos((secondX - firstX) / hypotenuse) * -1.0;
        }
        // should never happen because if the ship and alien collide, both will be expired
        else
        {
            return 0;
        }
    }
}
