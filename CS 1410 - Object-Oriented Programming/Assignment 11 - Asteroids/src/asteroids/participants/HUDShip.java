package asteroids.participants;

import java.awt.Shape;
import java.awt.geom.Path2D;
import asteroids.game.Controller;
import asteroids.game.Participant;

/**
 * Creates a ship with no collision or interaction to be used in the HUD as a life counter
 */
public class HUDShip extends Participant
{

    /** The outline of the ship */
    private Shape outline;

    /** Game controller */
    private Controller controller;

    /** Creates a ship with no collision or interaction to be used in the HUD as a life counter */
    public HUDShip (int x, int y, double direction, Controller controller)
    {
        this.controller = controller;
        setPosition(x, y);
        setRotation(direction);

        // Draw the ship
        Path2D.Double poly = new Path2D.Double();
        poly.moveTo(21, 0);
        poly.lineTo(-21, 12);
        poly.lineTo(-14, 10);
        poly.lineTo(-14, -10);
        poly.lineTo(-21, -12);
        poly.closePath();
        outline = poly;
    }

    @Override
    protected Shape getOutline ()
    {
        return outline;
    }

    @Override
    public void collidedWith (Participant p)
    {
        // TODO Auto-generated method stub

    }
}
