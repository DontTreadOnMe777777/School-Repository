package cs1410;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/**
 * A CacheList is a collection of Cache objects together with these six constraints:
 * 
 * <ol>
 * <li>A title constraint</li>
 * <li>An owner constraint</li>
 * <li>A minimum difficulty constraint</li>
 * <li>A maximum difficulty constraint</li>
 * <li>A minimum terrain constraint</li>
 * <li>A maximum terrain constraint</li>
 * </ol>
 */
public class CacheList
{
    // The caches being managed by this CacheList. They are arranged in
    // ascending order according to cache title.
    private ArrayList<Cache> allCaches; // An array of all Caches from the Cache method
    private String titleConstraint; // The title constraint set by the user
    private String ownerConstraint; // The owner constraint set by the user
    private double diffMin; // The difficulty minimum set by the user
    private double terrMin; // The terrain minimum set by the user
    private double diffMax; // The difficulty maximum set by the user
    private double terrMax; // The terrain maximum set by the user

    // TODO: Put remainder of representation here

    /**
     * Creates a CacheList from the specified Scanner. Each line of the Scanner should contain the description of a
     * cache in a format suitable for consumption by the Cache constructor. The resulting CacheList should contain one
     * Cache object corresponding to each line of the Scanner.
     * 
     * Sets the initial value of the title and owner constraints to the empty string, sets the minimum difficulty and
     * terrain constraints to 1.0, and sets the maximum difficulty and terrain constraints to 5.0.
     * 
     * Throws an IOException if the Scanner throws an IOException, or an IllegalArgumentException if any of the
     * individual lines are not appropriate for the Cache constructor.
     * 
     * When an IllegalArgumentException e is thrown, e.getMessage() is the number of the line that was being read when
     * the error that triggered the exception was encountered. Lines are numbered beginning with 1.
     */
    public CacheList (Scanner caches) throws IOException
    {
        // Setting the instance variables
        titleConstraint = "";
        ownerConstraint = "";
        diffMin = 1.0;
        terrMin = 1.0;
        diffMax = 5.0;
        terrMax = 5.0;
        allCaches = new ArrayList<Cache>();
        // Adding all caches to the AllCaches array
        try
        {
            while (caches.hasNextLine())
            {
                String attributes = caches.nextLine();
                Cache cacheToAdd = new Cache(attributes);
                allCaches.add(cacheToAdd);
            }
        }
        // Exception thrown if a cache is not correct
        catch (IllegalArgumentException e)
        {
            throw new IllegalArgumentException("Cache data not correct at line" + e.getMessage());
        }
        // Sort the list of caches
        Collections.sort(allCaches, (c1, c2) -> c1.getTitle().compareToIgnoreCase(c2.getTitle()));
    }

    /**
     * Sets the title constraint to the specified value.
     */
    public void setTitleConstraint (String title)
    {
        titleConstraint = title.toLowerCase();
    }

    /**
     * Sets the owner constraint to the specified value.
     */
    public void setOwnerConstraint (String owner)
    {
        ownerConstraint = owner.toLowerCase();
    }

    /**
     * Sets the minimum and maximum difficulty constraints to the specified values.
     */
    public void setDifficultyConstraints (double min, double max)
    {
        diffMin = min;
        diffMax = max;
    }

    /**
     * Sets the minimum and maximum terrain constraints to the specified values.
     */
    public void setTerrainConstraints (double min, double max)
    {
        terrMin = min;
        terrMax = max;
    }
    
    /**
     *  Finds all caches in allCaches that match the title constraint, case-insensitive
     */
    public ArrayList<Cache> findCachesWithTitle ()
    {
        int i = 0;
        ArrayList<Cache> cachesToCheckTitle = new ArrayList<Cache>();
        while (i < allCaches.size())
        {
            if (allCaches.get(i).getTitle().toLowerCase().contains(titleConstraint))
            {
                cachesToCheckTitle.add(allCaches.get(i));
                i++;
            }
            else
            {
                i++;
            }
        }
        return cachesToCheckTitle;
    }

    /**
     *  Finds all caches from findCachesWithTitle that match the owner constraint, case-insensitive
     */
    public ArrayList<Cache> findCachesWithOwner (ArrayList<Cache> cachesToCheck)
    {
        int i = 0;
        ArrayList<Cache> cachesToCheckOwner = new ArrayList<Cache>();
        while (i < cachesToCheck.size())
        {
            if (cachesToCheck.get(i).getOwner().toLowerCase().contains(ownerConstraint))
            {
                cachesToCheckOwner.add(cachesToCheck.get(i));
                i++;
            }
            else
            {
                i++;
            }
        }
        return cachesToCheckOwner;
    }

    /**
     *  Finds all caches from findCachesWithOwner that fit inside the difficulty constraints
     */
    public ArrayList<Cache> findCachesWithDifficulty (ArrayList<Cache> cachesToCheck)
    {
        int i = 0;
        ArrayList<Cache> cachesToCheckDifficulty = new ArrayList<Cache>();
        while (i < cachesToCheck.size())
        {
            if (cachesToCheck.get(i).getDifficulty() >= diffMin && cachesToCheck.get(i).getDifficulty() <= diffMax)
            {
                cachesToCheckDifficulty.add(cachesToCheck.get(i));
                i++;
            }
            else
            {
                i++;
            }
        }
        return cachesToCheckDifficulty;
    }

    /**
     *  Finds all caches from findCachesWithDifficulty that fit the terrain constraints
     */
    public ArrayList<Cache> findCachesWithTerrain (ArrayList<Cache> cachesToCheck)
    {
        int i = 0;
        ArrayList<Cache> cachesToCheckTerrain = new ArrayList<Cache>();
        while (i < cachesToCheck.size())
        {
            if (cachesToCheck.get(i).getTerrain() >= terrMin && cachesToCheck.get(i).getTerrain() <= terrMax)
            {
                cachesToCheckTerrain.add(cachesToCheck.get(i));
                i++;
            }
            else
            {
                i++;
            }
        }
        return cachesToCheckTerrain;
    }

    /**
     * Returns a list that contains each cache c from the CacheList so long as c's title contains the title constraint
     * as a substring, c's owner equals the owner constraint (unless the owner constraint is empty), c's difficulty
     * rating is between the minimum and maximum difficulties (inclusive), and c's terrain rating is between the minimum
     * and maximum terrains (inclusive). Both the title constraint and the owner constraint are case insensitive.
     * 
     * The returned list is arranged in ascending order by cache title.
     */
    public ArrayList<Cache> select ()
    {
        // Selects the fitting list of caches based on the constraints
        ArrayList<Cache> cacheOfSelections = findCachesWithTitle();
        cacheOfSelections = findCachesWithOwner(cacheOfSelections);
        cacheOfSelections = findCachesWithDifficulty(cacheOfSelections);
        cacheOfSelections = findCachesWithTerrain(cacheOfSelections);
        return cacheOfSelections;

    }

    /**
     * Returns a list containing all the owners of all of the Cache objects in this CacheList. There are no duplicates.
     * The list is arranged in ascending order.
     */
    public ArrayList<String> getOwners ()
    {
        // Adds all non-duplicate owners to an ArrayList
        int i = 0;
        ArrayList<String> owners = new ArrayList<String>();
        while (i < allCaches.size())
        {
            String ownerToAdd = allCaches.get(i).getOwner();
            if (owners.contains(ownerToAdd))
            {
                i++;
            }
            else
            {
                owners.add(ownerToAdd);
                i++;
            }
        }

        // Sort the list of owners
        Collections.sort(owners, (s1, s2) -> s1.compareToIgnoreCase(s2));
        return owners;
    }
}
