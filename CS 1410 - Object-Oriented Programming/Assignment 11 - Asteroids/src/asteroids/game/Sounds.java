package asteroids.game;

import java.io.BufferedInputStream;
import java.io.IOException;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Sounds
{
    // Clips for all the sounds
    private Clip bulletSound;

    private Clip bigSaucerSound;

    private Clip smallSaucerSound;

    private Clip thrusterSound;

    private Clip musicOddBeats;

    private Clip musicEvenBeats;

    private Clip shipExplosion;

    private Clip alienShipExplosion;

    private Clip smallAsteroidExplosion;

    private Clip mediumAsteroidExplosion;

    private Clip largeAsteroidExplosion;

    /**
     * Constructs all of the sounds beforehand, to make sure the game doesn't have to take time to make the sound clip
     * when it is called
     */
    public Sounds ()
    {
        bulletSound = createClip("/sounds/fire.wav");
        bigSaucerSound = createClip("/sounds/saucerBig.wav");
        smallSaucerSound = createClip("/sounds/saucerSmall.wav");
        thrusterSound = createClip("/sounds/thrust.wav");
        musicOddBeats = createClip("/sounds/beat1.wav");
        musicEvenBeats = createClip("/sounds/beat2.wav");
        shipExplosion = createClip("/sounds/bangShip.wav");
        alienShipExplosion = createClip("/sounds/bangAlienShip.wav");
        smallAsteroidExplosion = createClip("/sounds/bangSmall.wav");
        mediumAsteroidExplosion = createClip("/sounds/bangMedium.wav");
        largeAsteroidExplosion = createClip("/sounds/bangLarge.wav");
    }

    /**
     * Creates a clip that can be played in game from a sound file
     */
    public Clip createClip (String soundFile)
    {
        try (BufferedInputStream sound = new BufferedInputStream(getClass().getResourceAsStream(soundFile)))
        {
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(sound));
            return clip;
        }
        catch (LineUnavailableException e)
        {
            return null;
        }
        catch (IOException e)
        {
            return null;
        }
        catch (UnsupportedAudioFileException e)
        {
            return null;
        }
    }

    /**
     * Plays the requested clip.
     */
    public void playSounds (String clipToPlay)
    {
        if (clipToPlay.contains("bulletSound") && bulletSound != null)
        {
            if (bulletSound.isRunning())
            {
                bulletSound.stop();
            }
            bulletSound.setFramePosition(0);
            bulletSound.start();
        }

        if (clipToPlay.contains("bigSaucerSound") && bigSaucerSound != null)
        {
            if (bigSaucerSound.isRunning())
            {
                bigSaucerSound.stop();
            }
            bigSaucerSound.setFramePosition(0);
            bigSaucerSound.loop(Clip.LOOP_CONTINUOUSLY);
        }

        if (clipToPlay.contains("smallSaucerSound") && smallSaucerSound != null)
        {
            if (smallSaucerSound.isRunning())
            {
                smallSaucerSound.stop();
            }
            smallSaucerSound.setFramePosition(0);
            smallSaucerSound.loop(Clip.LOOP_CONTINUOUSLY);
        }

        if (clipToPlay.contains("thrusterSound") && thrusterSound != null)
        {
            if (thrusterSound.isRunning())
            {
                thrusterSound.stop();
            }
            thrusterSound.setFramePosition(0);
            thrusterSound.loop(Clip.LOOP_CONTINUOUSLY);
        }

        if (clipToPlay.contains("musicOddBeats") && musicOddBeats != null)
        {
            if (musicOddBeats.isRunning())
            {
                musicOddBeats.stop();
            }
            musicOddBeats.setFramePosition(0);
            musicOddBeats.start();
        }

        if (clipToPlay.contains("musicEvenBeats") && musicEvenBeats != null)
        {
            if (musicEvenBeats.isRunning())
            {
                musicEvenBeats.stop();
            }
            musicEvenBeats.setFramePosition(0);
            musicEvenBeats.start();
        }

        if (clipToPlay.contains("shipExplosion") && shipExplosion != null)
        {
            if (shipExplosion.isRunning())
            {
                shipExplosion.stop();
            }
            shipExplosion.setFramePosition(0);
            shipExplosion.start();
        }

        if (clipToPlay.contains("alienShipExplosion") && alienShipExplosion != null)
        {
            if (alienShipExplosion.isRunning())
            {
                alienShipExplosion.stop();
            }
            alienShipExplosion.setFramePosition(0);
            alienShipExplosion.start();
        }

        if (clipToPlay.contains("smallAsteroidExplosion") && smallAsteroidExplosion != null)
        {
            if (smallAsteroidExplosion.isRunning())
            {
                smallAsteroidExplosion.stop();
            }
            smallAsteroidExplosion.setFramePosition(0);
            smallAsteroidExplosion.start();
        }

        if (clipToPlay.contains("mediumAsteroidExplosion") && mediumAsteroidExplosion != null)
        {
            if (mediumAsteroidExplosion.isRunning())
            {
                mediumAsteroidExplosion.stop();
            }
            mediumAsteroidExplosion.setFramePosition(0);
            mediumAsteroidExplosion.start();
        }

        if (clipToPlay.contains("largeAsteroidExplosion") && largeAsteroidExplosion != null)
        {
            if (largeAsteroidExplosion.isRunning())
            {
                largeAsteroidExplosion.stop();
            }
            largeAsteroidExplosion.setFramePosition(0);
            largeAsteroidExplosion.start();
        }

        if (clipToPlay.contains("stopThruster"))
        {
            thrusterSound.stop();
        }

        if (clipToPlay.contains("stopSmallSaucer"))
        {
            smallSaucerSound.stop();
        }

        if (clipToPlay.contains("stopBigSaucer"))
        {
            bigSaucerSound.stop();
        }
    }
}
