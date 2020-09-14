package asteroids.game;

import static asteroids.game.Constants.*;
import java.awt.event.*;
import java.util.Iterator;
import java.util.Random;
import javax.swing.*;
import asteroids.participants.Alien;
import asteroids.participants.Asteroid;
import asteroids.participants.Bullet;
import asteroids.participants.Debris;
import asteroids.participants.HUDShip;
import asteroids.participants.Ship;
import asteroids.game.Sounds.*;

/**
 * Controls a game of Asteroids.
 * @author Justin Rogosienski and Brandon Walters
 */
public class Controller implements KeyListener, ActionListener
{
    /** The state of all the Participants */
    private ParticipantState pstate;

    /** The ship (if one is active) or null (otherwise) */
    private Ship ship;

    /** When this timer goes off, it is time to refresh the animation */
    private Timer refreshTimer;

    /** Keeps track of if the ship is turning right */
    private boolean isShipTurningRight = false;

    /** Keeps track of if the ship is turning left */
    private boolean isShipTurningLeft = false;

    /** Keeps track of if the ship is accelerating */
    private boolean isShipMoving = false;

    /** Represents if the player is holding the shoot button */
    private boolean isShooting;

    /** The number of bullets on screen at the current time */
    private int numBullets;

    /**
     * The time at which a transition to a new stage of the game should be made. A transition is scheduled a few seconds
     * in the future to give the user time to see what has happened before doing something like going to a new level or
     * resetting the current level.
     */
    private long transitionTime;

    /** Number of lives left */
    private int lives;

    /** Number of the level that the player is at */
    private int level;

    /** Number of the score that the player is at */
    private int score;

    /** The game display */
    private Display display;

    /** Used to call sounds */
    private Sounds soundToCall = new Sounds();

    /** Used to create the ships shown as the lives counter on the HUD */
    private HUDShip HUDShip1;

    /** Used to create the ships shown as the lives counter on the HUD */
    private HUDShip HUDShip2;

    /** Used to create the ships shown as the lives counter on the HUD */
    private HUDShip HUDShip3;

    /** When this timer goes off, it is time to place a new ship */
    private Timer alienTimer;

    /** Represents if the game has an active alien */
    private boolean hasAlien;

    /** Flickers the thruster every frame */
    private boolean thrusterOn = false;

    /** Used to control the background music */
    private Timer musicTimer;

    /** Used to alternate between even and odd beats */
    private boolean musicOddBeat = true;

    /** Used to make the music quicker */
    private int delayTimer = 0;

    /**
     * Constructs a controller to coordinate the game and screen
     */
    public Controller ()
    {
        // Initialize the ParticipantState
        pstate = new ParticipantState();

        // Set up the refresh timer.
        refreshTimer = new Timer(FRAME_INTERVAL, this);

        // Set up the alien timer.
        alienTimer = new Timer(ALIEN_DELAY, this);
        hasAlien = false;

        // Clear the transitionTime
        transitionTime = Long.MAX_VALUE;

        // Record the display object
        display = new Display(this);

        // Bring up the splash screen and start the refresh timer
        splashScreen();
        display.setVisible(true);
        refreshTimer.start();
    }

    /**
     * Returns the ship, or null if there isn't one
     */
    public Ship getShip ()
    {
        return ship;
    }

    /**
     * Configures the game screen to display the splash screen
     */
    private void splashScreen ()
    {
        // Clear the screen, reset the level, and display the legend
        clear();
        display.setLegend("Asteroids");

        // Place four asteroids near the corners of the screen.
        placeAsteroids();
    }

    /**
     * The game is over. Displays a message to that effect.
     */
    private void finalScreen ()
    {
        display.setLegend(GAME_OVER);
        display.removeKeyListener(this);
    }

    /**
     * Place a new ship in the center of the screen. Remove any existing ship first.
     */
    private void placeShip ()
    {
        // Place a new ship
        Participant.expire(ship);
        ship = new Ship(SIZE / 2, SIZE / 2, -Math.PI / 2, this);
        addParticipant(ship);
        display.setLegend("");
    }

    /**
     * Places an random type of asteroid near each corner of the screen. Gives it a random velocity and rotation. At
     * higher levels, the number of asteroids increases and the location of the additional asteroid will be a random
     * corner.
     */
    private void placeAsteroids ()
    {
        Random rand = new Random();

        // Places the 4 original asteroids
        // top left
        addParticipant(new Asteroid(rand.nextInt(4), 2, EDGE_OFFSET, EDGE_OFFSET,
                rand.nextInt(MAXIMUM_LARGE_ASTEROID_SPEED) + 1, this));
        // bottom left
        addParticipant(new Asteroid(rand.nextInt(4), 2, EDGE_OFFSET, SIZE - EDGE_OFFSET,
                rand.nextInt(MAXIMUM_LARGE_ASTEROID_SPEED) + 1, this));
        // top right
        addParticipant(new Asteroid(rand.nextInt(4), 2, SIZE - EDGE_OFFSET, EDGE_OFFSET,
                rand.nextInt(MAXIMUM_LARGE_ASTEROID_SPEED) + 1, this));
        // bottom right
        addParticipant(new Asteroid(rand.nextInt(4), 2, SIZE - EDGE_OFFSET, SIZE - EDGE_OFFSET,
                rand.nextInt(MAXIMUM_LARGE_ASTEROID_SPEED) + 1, this));

        // Places additional asteroids (for higher levels) in random locations with random velocity according to the
        // level
        int numAsteroids = level - 1;
        for (int i = 0; i < numAsteroids; i++)
        {
            switch (rand.nextInt(4))
            {
                case 0:
                    addParticipant(new Asteroid(rand.nextInt(4), 2, EDGE_OFFSET, EDGE_OFFSET,
                            rand.nextInt(MAXIMUM_LARGE_ASTEROID_SPEED) + 1, this));
                    break;
                case 1:
                    addParticipant(new Asteroid(rand.nextInt(4), 2, EDGE_OFFSET, SIZE - EDGE_OFFSET,
                            rand.nextInt(MAXIMUM_LARGE_ASTEROID_SPEED) + 1, this));
                    break;
                case 2:
                    addParticipant(new Asteroid(rand.nextInt(4), 2, SIZE - EDGE_OFFSET, EDGE_OFFSET,
                            rand.nextInt(MAXIMUM_LARGE_ASTEROID_SPEED) + 1, this));
                    break;
                case 3:
                    addParticipant(new Asteroid(rand.nextInt(4), 2, SIZE - EDGE_OFFSET, SIZE - EDGE_OFFSET,
                            rand.nextInt(MAXIMUM_LARGE_ASTEROID_SPEED) + 1, this));
            }
        }
    }

    /**
     * Places a bullet at the nose of the given ship. It has the direction on the ship's rotation and has a speed of
     * BULLET_LIMIT. Increments the number of bullets on screen.
     */
    private void placeBullet ()
    {
        // Adds a new bullet
        addParticipant(new Bullet(ship, this));
        numBullets++;

        // Plays the bullet sound
        soundToCall.playSounds("bulletSound");
    }

    /**
     * Places special debris for a ship at the ship's position.
     */
    private void placeShipDebris ()
    {
        // Adds ship debris
        addParticipant(new Debris(ship, this));
    }

    /**
     * Places special debris for an alien at the alien's position.
     */
    private void placeAlienDebris (Alien deadAlien)
    {
        // Adds alien debris
        addParticipant(new Debris(deadAlien, this));
    }

    /**
     * Places special debris for an asteroid at the asteroid's position.
     */
    private void placeAsteroidDebris (Asteroid destroyedAsteroid)
    {
        // Adds asteroid debris
        addParticipant(new Debris(destroyedAsteroid, this));
    }

    /**
     * Places an alien at a random location along the side and places the proper size according to the level
     */
    private void placeAlien ()
    {
        // places a large alien if it is level 2
        if (level == 2)
        {
            addParticipant(new Alien(1, this));

            // Starts alien sound
            soundToCall.playSounds("bigSaucerSound");
        }
        // places a small alien if it is past level 2
        else if (level > 2)
        {
            addParticipant(new Alien(0, this));

            // Starts alien sound
            soundToCall.playSounds("smallSaucerSound");
        }

        // tells the controller that there is an active alien
        hasAlien = true;
    }

    /**
     * Clears the screen so that nothing is displayed
     */
    private void clear ()
    {
        pstate.clear();
        display.setLegend("");
        ship = null;
    }

    /**
     * Sets things up and begins a new game.
     */
    private void initialScreen ()
    {
        // Clear the screen
        clear();

        // Place asteroids
        placeAsteroids();

        // Place the ship
        thrusterOn = false;
        placeShip();
        ship.flickerThruster(thrusterOn);

        // Reset statistics
        lives = 3;
        level = 1;
        score = 0;
        hasAlien = false;

        // Set up the HUD
        display.setScore(score);
        display.setLevel(level);

        HUDShip1 = new HUDShip(30, 70, -Math.PI / 2, this);
        addParticipant(HUDShip1);

        HUDShip2 = new HUDShip(60, 70, -Math.PI / 2, this);
        addParticipant(HUDShip2);

        HUDShip3 = new HUDShip(90, 70, -Math.PI / 2, this);
        addParticipant(HUDShip3);

        // Start the music
        playMusic();

        // Start listening to events (but don't listen twice)
        display.removeKeyListener(this);
        display.addKeyListener(this);

        // Give focus to the game screen
        display.requestFocusInWindow();
    }

    /**
     * Adds a new Participant
     */
    public void addParticipant (Participant p)
    {
        pstate.addParticipant(p);
    }

    /**
     * The ship has been destroyed
     */
    public void shipDestroyed ()
    {
        // Play the ship explosion sound
        soundToCall.playSounds("shipExplosion");

        // Stop the sound of the thruster
        soundToCall.playSounds("stopThruster");

        // Place the debris representing the destroyed ship
        placeShipDebris();
        placeShipDebris();
        placeShipDebris();

        // Null out the ship
        ship = null;

        // Remove ship counter from the HUD depending on lives left before the death
        if (lives == 3)
        {
            Participant.expire(HUDShip3);
        }

        if (lives == 2)
        {
            Participant.expire(HUDShip2);
        }

        if (lives == 1)
        {
            Participant.expire(HUDShip1);
        }

        // Decrement lives
        lives--;

        // Since the ship was destroyed, schedule a transition
        scheduleTransition(END_DELAY);
    }

    /**
     * An asteroid has been destroyed
     */
    public void asteroidDestroyed ()
    {
        // If all the asteroids are gone, schedule a transition
        if (pstate.countAsteroids() == 0)
        {

            scheduleTransition(END_DELAY);
        }
    }

    /**
     * The asteroid was hit by a bullet, so it splits if appropriate
     */
    public void asteroidSplit (Asteroid previous)
    {
        // Places debris to represent the destroyed asteroid
        placeAsteroidDebris(previous);
        placeAsteroidDebris(previous);
        placeAsteroidDebris(previous);
        placeAsteroidDebris(previous);

        Random rand = new Random();

        // If it was a large asteroid
        if (previous.getSize() == 2)
        {
            // Play the sound of a large asteroid explosion
            soundToCall.playSounds("largeAsteroidExplosion");

            // Update the scoreboard
            score = score + 20;
            display.setScore(score);
            display.refresh();

            // Place 2 medium ones that can go faster
            addParticipant(new Asteroid(rand.nextInt(4), 1, previous.getX(), previous.getY(),
                    rand.nextInt(MAXIMUM_MEDIUM_ASTEROID_SPEED) + 1, this));
            addParticipant(new Asteroid(rand.nextInt(4), 1, previous.getX(), previous.getY(),
                    rand.nextInt(MAXIMUM_MEDIUM_ASTEROID_SPEED) + 1, this));
        }

        // If it was a medium asteroid
        if (previous.getSize() == 1)
        {
            // Play the sound of a medium asteroid explosion
            soundToCall.playSounds("mediumAsteroidExplosion");

            // Update the scoreboard
            score = score + 50;
            display.setScore(score);
            display.refresh();

            // Place 2 small ones that can go fastest
            addParticipant(new Asteroid(rand.nextInt(4), 0, previous.getX(), previous.getY(),
                    rand.nextInt(MAXIMUM_SMALL_ASTEROID_SPEED) + 1, this));
            addParticipant(new Asteroid(rand.nextInt(4), 0, previous.getX(), previous.getY(),
                    rand.nextInt(MAXIMUM_SMALL_ASTEROID_SPEED) + 1, this));
        }
        // If it was a small asteroid
        if (previous.getSize() == 0)
        {
            // Play the sound of a small asteroid explosion
            soundToCall.playSounds("smallAsteroidExplosion");

            // Update the scoreboard
            score = score + 100;
            display.setScore(score);
            display.refresh();
        }
    }

    /**
     * Decrements the number of bullets accounted for on screen
     */
    public void bulletDestroyed ()
    {
        numBullets--;
    }

    /**
     * An alien has been destroyed
     */
    public void alienDestroyed (Alien previous)
    {
        // If large alien
        if (level == 2)
        {
            // Update the scoreboard
            score = score + 200;
            display.setScore(score);
            display.refresh();

            // Stop alien sound
            soundToCall.playSounds("stopBigSaucer");
        }

        // If small alien
        if (level > 2)
        {
            // Update the scoreboard
            score = score + 1000;
            display.setScore(score);
            display.refresh();

            // Stop alien sound
            soundToCall.playSounds("stopSmallSaucer");
        }

        // Places debris
        placeAlienDebris(previous);
        placeAlienDebris(previous);
        placeAlienDebris(previous);
        placeAlienDebris(previous);
        placeAlienDebris(previous);
        placeAlienDebris(previous);

        // tells the controller that there is no longer an active alien
        hasAlien = false;

        // makes it so an alien will spawn in 5 seconds
        alienTimer.restart();

        // Plays alien death sound
        soundToCall.playSounds("alienExplosion");
    }

    /**
     * Schedules a transition m msecs in the future
     */
    private void scheduleTransition (int m)
    {
        transitionTime = System.currentTimeMillis() + m;
    }

    /**
     * This method will be invoked because of button presses and timer events.
     */
    @Override
    public void actionPerformed (ActionEvent e)
    {
        // The start button has been pressed. Stop whatever we're doing
        // and bring up the initial screen
        if (e.getSource() instanceof JButton)
        {
            initialScreen();
        }

        // Time to refresh the screen and deal with keyboard input
        else if (e.getSource() == refreshTimer)
        {
            // It may be time to make a game transition
            performTransition();

            // Move the participants to their new locations
            pstate.moveParticipants();

            // Check if the ship is accelerating or turning at the time of the refresh
            if (ship != null)
            {
                if (isShipTurningRight)
                {
                    ship.turnRight();
                }
                if (isShipTurningLeft)
                {
                    ship.turnLeft();
                }
                if (isShipMoving)
                {
                    ship.accelerate();

                    // Flickers the flame every frame
                    thrusterOn = !thrusterOn;
                    ship.flickerThruster(thrusterOn);
                }
            }

            // Do something if the player is shooting
            if (isShooting && (ship != null))
            {
                if (numBullets < BULLET_LIMIT)
                {
                    // Places a bullet every frame the player is shooting
                    placeBullet();
                }
            }

            // Refresh screen
            display.refresh();
        }

        // If the source is the alienTimer ending
        if (e.getSource() == alienTimer && !hasAlien)
        {
            placeAlien();
            alienTimer.stop();
        }

        // If the source is the musicTimer ending
        if (e.getSource() == musicTimer)
        {
            // If the beat is odd
            if (musicOddBeat == true)
            {
                soundToCall.playSounds("musicOddBeats");
                musicOddBeat = false;
            }

            // If the beat is even
            else if (musicOddBeat == false)
            {
                soundToCall.playSounds("musicEvenBeats");
                musicOddBeat = true;
            }
            playMusic();
        }
    }

    /**
     * Returns an iterator over the active participants
     */
    public Iterator<Participant> getParticipants ()
    {
        return pstate.getParticipants();
    }

    /**
     * If the transition time has been reached, transition to a new state
     */
    private void performTransition ()
    {
        // Do something only if the time has been reached
        if (transitionTime <= System.currentTimeMillis())
        {
            // Clear the transition time
            transitionTime = Long.MAX_VALUE;

            // If there are no lives left, the game is over. Show the final
            // screen.
            if (lives <= 0)
            {
                isShipTurningRight = false;
                isShipTurningLeft = false;
                isShipMoving = false;
                finalScreen();
            }
            // If there are lives left, stop the input from the player and then place a new ship
            else if (lives > 0)
            {
                isShipTurningRight = false;
                isShipTurningLeft = false;
                isShipMoving = false;
                // Tests if there are any asteroids left
                if (pstate.countAsteroids() == 0)
                {
                    // If there aren't, starts a new level
                    level++;
                    clear();
                    soundToCall.playSounds("stopBigSaucer");
                    soundToCall.playSounds("stopSmallSaucer");

                    // Update the HUD
                    display.setScore(score);
                    display.setLevel(level);

                    if (lives == 3)
                    {
                        HUDShip1 = new HUDShip(30, 70, -Math.PI / 2, this);
                        addParticipant(HUDShip1);

                        HUDShip2 = new HUDShip(60, 70, -Math.PI / 2, this);
                        addParticipant(HUDShip2);

                        HUDShip3 = new HUDShip(90, 70, -Math.PI / 2, this);
                        addParticipant(HUDShip3);
                    }

                    else if (lives == 2)
                    {
                        HUDShip1 = new HUDShip(30, 70, -Math.PI / 2, this);
                        addParticipant(HUDShip1);

                        HUDShip2 = new HUDShip(60, 70, -Math.PI / 2, this);
                        addParticipant(HUDShip2);
                    }

                    else if (lives == 1)
                    {
                        HUDShip1 = new HUDShip(30, 70, -Math.PI / 2, this);
                        addParticipant(HUDShip1);
                    }

                    // Start the next level
                    placeAsteroids();
                    hasAlien = false;
                    alienTimer.start();

                    // Restart the music
                    delayTimer = 0;
                    musicOddBeat = true;
                    playMusic();
                }
                placeShip();
            }
        }
    }

    public void playMusic ()
    {
        // If the tempo hasn't hit its max yet
        if (INITIAL_BEAT - delayTimer > FASTEST_BEAT)
        {
            musicTimer = new Timer(INITIAL_BEAT - delayTimer, this);
            musicTimer.start();
            delayTimer = delayTimer + BEAT_DELTA;
        }

        // If the tempo is at max
        else if (INITIAL_BEAT - delayTimer <= FASTEST_BEAT)
        {
            musicTimer = new Timer(FASTEST_BEAT, this);
            musicTimer.start();
        }
    }

    /**
     * If a key of interest is pressed, record that it is down.
     */
    @Override
    public void keyPressed (KeyEvent e)
    {
        // If turning right
        if (e.getKeyCode() == KeyEvent.VK_RIGHT && ship != null || e.getKeyCode() == KeyEvent.VK_D && ship != null)
        {
            isShipTurningRight = true;
        }

        // If turning left
        if (e.getKeyCode() == KeyEvent.VK_LEFT && ship != null || e.getKeyCode() == KeyEvent.VK_A && ship != null)
        {
            isShipTurningLeft = true;
        }

        // If accelerating
        if (e.getKeyCode() == KeyEvent.VK_UP && ship != null || e.getKeyCode() == KeyEvent.VK_W && ship != null)
        {
            isShipMoving = true;
            soundToCall.playSounds("thrusterSound");
        }

        // Do something if the player is holding down the space bar
        else if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_DOWN
                || e.getKeyCode() == KeyEvent.VK_S)
        {
            // Tells the controller that the player is shooting
            isShooting = true;

        }
    }

    /**
     * These events are ignored.
     */
    @Override
    public void keyTyped (KeyEvent e)
    {
    }

    /**
     * If the player is no longer holding a key, update appropriately.
     */
    @Override
    public void keyReleased (KeyEvent e)
    {
        // If no longer turning right
        if (e.getKeyCode() == KeyEvent.VK_RIGHT && ship != null || e.getKeyCode() == KeyEvent.VK_D && ship != null)
        {
            isShipTurningRight = false;
        }

        // If no longer turning left
        if (e.getKeyCode() == KeyEvent.VK_LEFT && ship != null || e.getKeyCode() == KeyEvent.VK_A && ship != null)
        {
            isShipTurningLeft = false;
        }

        // If no longer accelerating
        if (e.getKeyCode() == KeyEvent.VK_UP && ship != null || e.getKeyCode() == KeyEvent.VK_W && ship != null)
        {
            isShipMoving = false;
            soundToCall.playSounds("stopThruster");
            ship.flickerThruster(false);
        }

        // Do something if the player releases the space bar
        if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_DOWN
                || e.getKeyCode() == KeyEvent.VK_S)
        {
            // Tells the controller that the player is no longer shooting
            isShooting = false;
        }
    }
}
