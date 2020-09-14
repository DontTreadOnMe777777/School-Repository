package asteroids.participants;

import java.awt.Shape;
import java.awt.geom.Path2D;
import asteroids.game.Controller;
import asteroids.game.Participant;
import asteroids.game.ParticipantCountdownTimer;
import java.util.Random;

public class Debris extends Participant
{

    /** The outline of the bullet */
    private Shape outline;

    /** The game controller */
    private Controller controller;

    /**
     * Creates debris for when a ship is destroyed
     */
    public Debris (Ship deadShip, Controller controller)
    {
        // Saves the controller
        this.controller = controller;

        Random r = new Random();

        // Creates the debris
        Path2D.Double poly = new Path2D.Double();
        poly.moveTo(deadShip.getX(), deadShip.getY());
        poly.lineTo(deadShip.getX() + r.nextInt(5), deadShip.getY() + r.nextInt(10) + 10);
        outline = poly;

        // Moves it slowly
        setVelocity(r.nextDouble() + 0.5, r.nextDouble() + 0.5);

        // Prepares a timer to destroy it
        new ParticipantCountdownTimer(this, 2000);
    }

    public Debris (Alien deadAlien, Controller controller)
    {
        // Saves the controller
        this.controller = controller;

        Random r = new Random();

        // Creates the debris
        Path2D.Double poly = new Path2D.Double();
        poly.moveTo(deadAlien.getX(), deadAlien.getY());
        poly.lineTo(deadAlien.getX() + r.nextInt(5), deadAlien.getY() + r.nextInt(10) + 10);
        outline = poly;

        // Moves it slowly
        setVelocity(r.nextDouble() + 0.5, r.nextDouble() + 0.5);

        // Prepares a timer to destroy it
        new ParticipantCountdownTimer(this, 2000);
    }

    /**
     * Creates debris for when an asteroid is destroyed
     */
    public Debris (Asteroid deadAsteroid, Controller controller)
    {
        // Saves the controller
        this.controller = controller;

        // Creates the debris
        Path2D.Double poly = new Path2D.Double();
        poly.moveTo(deadAsteroid.getX(), deadAsteroid.getY());
        poly.lineTo(deadAsteroid.getX() + 1, deadAsteroid.getY());
        outline = poly;

        // Moves it slowly in a random direction
        Random r = new Random();
        setVelocity(r.nextDouble() + 0.5, r.nextDouble() + 0.5);

        // Prepares a timer to destroy it
        new ParticipantCountdownTimer(this, 2000);

    }

    /**
     * Returns the outline of the debris
     */
    @Override
    protected Shape getOutline ()
    {
        return outline;
    }

    @Override
    public void collidedWith (Participant p)
    {

    }

    /**
     * When a timer ends, the debris is erased
     */
    public void countdownComplete (Object payload)
    {
        // Destroys the debris
        Participant.expire(this);
    }

}
