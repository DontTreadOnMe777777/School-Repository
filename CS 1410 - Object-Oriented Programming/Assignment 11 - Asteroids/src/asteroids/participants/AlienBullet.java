package asteroids.participants;

import static asteroids.game.Constants.BULLET_DURATION;
import static asteroids.game.Constants.BULLET_SPEED;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import asteroids.destroyers.AlienBulletDestroyer;
import asteroids.destroyers.AsteroidDestroyer;
import asteroids.destroyers.ShipDestroyer;
import asteroids.game.Participant;
import asteroids.game.ParticipantCountdownTimer;

public class AlienBullet extends Participant implements AsteroidDestroyer, ShipDestroyer
{
    /** The outline of the bullet */
    private Shape outline;

    /**
     * Creates a bullet at the nose of the given ship in the direction that the ship is pointed. The bullets move at
     * BULLET_SPEED and will expire after BULLET_DURATION milliseconds.
     */
    public AlienBullet (double direction, Alien source)
    {
        // Makes a bullet that moves at BULLET_SPEED in the direction the ship is pointed
        setVelocity(BULLET_SPEED + source.getSpeed(), direction);

        // Sets the bullets starting position to the center of the alien
        setPosition(source.getX(), source.getY());

        // Makes a small circle to represent the bullet
        Ellipse2D.Double circle = new Ellipse2D.Double(0, 0, 1, 1);

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
        if (p instanceof AlienBulletDestroyer)
        {
            // Destroys the bullet
            Participant.expire(this);
        }
    }

    /**
     * Expires the bullet if the bullet duration is reached
     */
    public void countdownComplete (Object payload)
    {
        // Destroys the bullet
        Participant.expire(this);
    }
}
