package asteroids.participants;

import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import asteroids.destroyers.AlienDestroyer;
import asteroids.destroyers.AsteroidDestroyer;
import asteroids.destroyers.BulletDestroyer;
import asteroids.game.Controller;
import asteroids.game.Participant;
import asteroids.game.ParticipantCountdownTimer;
import static asteroids.game.Constants.*;

/**
 * Represents bullets
 */
public class Bullet extends Participant implements AlienDestroyer, AsteroidDestroyer
{
    /** The outline of the bullet */
    private Shape outline;

    /** The game controller */
    private Controller controller;

    /**
     * Creates a bullet at the nose of the given ship in the direction that the ship is pointed. The bullets move at
     * BULLET_SPEED and will expire after BULLET_DURATION milliseconds.
     */
    public Bullet (Ship source, Controller controller)
    {
        // Saves the controller
        this.controller = controller;

        // Makes a bullet that moves at BULLET_SPEED in the direction the ship is pointed
        setVelocity(BULLET_SPEED + source.getSpeed(), source.getRotation());

        // Makes a small circle to represent the bullet at the location of the ship's nose
        Ellipse2D.Double circle = new Ellipse2D.Double(source.getXNose(), source.getYNose(), 1, 1);

        // Saves the circle
        outline = circle;

        // Sets a timer for the bullet's lifespan
        new ParticipantCountdownTimer(this, BULLET_DURATION);
    }

    /**
     * returns the outline of the bullet
     */
    @Override
    protected Shape getOutline ()
    {
        // returns the outline created when initialized
        return outline;
    }

    /**
     * Is called if the bullet contacts another participant. If a bullet hits an asteroid, the bullet is destroyed.
     */
    @Override
    public void collidedWith (Participant p)
    {
        if (p instanceof BulletDestroyer)
        {
            // Destroys the bullet
            Participant.expire(this);

            // Inform the controller
            controller.bulletDestroyed();
        }
    }

    /**
     * Expires the bullet if the bullet duration is reached
     */
    public void countdownComplete (Object payload)
    {
        // Destroys the bullet
        Participant.expire(this);

        // Inform the controller
        controller.bulletDestroyed();
    }
}
