package cs1410;

import java.util.Scanner;

/**
 * Represents a variety of information about a geocache. A geocache has a title, an owner, a difficulty rating, a
 * terrain rating, a GC code, a latitude, and a longitude.
 */
public class Cache
{
    private String code; // GC Code
    private String author; // Owner name
    private String name; // Title of cache
    private Double difficulty; // Difficulty of cache
    private Double terrain; // Terrain rating of cache
    private String latitude; // Lat coords of cache
    private String longitude; // Long coords of cache

    /**
     * Creates a Cache from a string that consists of these seven cache attributes: the GC code, the title, the owner,
     * the difficulty rating, the terrain rating, the latitude, and the longitude, in that order, separated by single
     * TAB ('\t') characters.
     * 
     * If any of the following problems are present, throws an IllegalArgumentException:
     * <ul>
     * <li>Fewer than seven attributes</li>
     * <li>More than seven attributes</li>
     * <li>A GC code that is anything other than "GC" followed by one or more upper-case letters and/or digits</li>
     * <li>A difficulty or terrain rating that parses to anything other than the doubles 1, 1.5, 2, 2.5, 3, 3.5, 4, 4.5,
     * or 5.</li>
     * <li>A title, owner, latitude, or longitude that consists only of white space</li>
     */
    public Cache (String attributes)
    {
        String[] attributesArray = attributes.split("\t");
        if (attributesArray.length != 7)
        {
            throw new IllegalArgumentException("Fewer/More than 7 attributes.");
        }
        else
        {
            for (int i = 0; i < attributesArray.length; i++)
            {
                Scanner errorScanner = new Scanner(attributesArray[i]);
                if (errorScanner.hasNext() == false)
                {
                    throw new IllegalArgumentException("One or more attributes are blank.");
                }
            }
        }
        // Assign values from attributes to all instance variables
        code = attributesArray[0];
        name = attributesArray[1];
        author = attributesArray[2];
        difficulty = Double.parseDouble(attributesArray[3]);
        terrain = Double.parseDouble(attributesArray[4]);
        latitude = attributesArray[5];
        longitude = attributesArray[6];
    }

    /**
     * Converts this cache to a string
     */
    public String toString ()
    {
        return getTitle() + " by " + getOwner();
    }

    /**
     * Returns the owner of this cache
     */
    public String getOwner ()
    {
        return author;
    }

    /**
     * Returns the title of this cache
     */
    public String getTitle ()
    {
        return name;
    }

    /**
     * Returns the difficulty rating of this cache
     */
    public double getDifficulty ()
    {
        // Checks for correct number format
        if (difficulty != 1.0 && difficulty != 1.5 && difficulty != 2.0 && difficulty != 2.5 && difficulty != 3.0
                && difficulty != 3.5 && difficulty != 4.0 && difficulty != 4.5 && difficulty != 5.0)
        {
            throw new IllegalArgumentException("Difficulty input is incorrect.");
        }
        return difficulty;
    }

    /**
     * Returns the terrain rating of this cache
     */
    public double getTerrain ()
    {
        // Checks for correct number format
        if (terrain != 1.0 && terrain != 1.5 && terrain != 2.0 && terrain != 2.5 && terrain != 3.0 && terrain != 3.5
                && terrain != 4.0 && terrain != 4.5 && terrain != 5.0)
        {
            throw new IllegalArgumentException("Terrain input is incorrect.");
        }
        return terrain;
    }

    /**
     * Returns the GC code of this cache
     */
    public String getGcCode ()
    {
        // Checks for correct GC code format
        try
        {
            if (code.charAt(2) == ' ')
            {
                throw new IllegalArgumentException("GC Code is incomplete.");
            }
            else
            {
                char charToCheck = code.charAt(2);

                if (code.startsWith("GC"))
                {
                    if (Character.isDigit(charToCheck) == false && Character.isLetter(charToCheck) == false)
                    {
                        throw new IllegalArgumentException("GC Code is incorrect.");
                    }
                }
            }
        }
        catch (StringIndexOutOfBoundsException e)
        {
            throw new IllegalArgumentException("GC Code is incomplete.");
        }
        return code;

    }

    /**
     * Returns the latitude of this cache
     */
    public String getLatitude ()
    {
        return latitude;
    }

    /**
     * Returns the longitude of this cache
     */
    public String getLongitude ()
    {
        return longitude;
    }
}
