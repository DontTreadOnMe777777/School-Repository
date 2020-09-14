package cs1410;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.lang.IllegalArgumentException;
import java.util.NoSuchElementException;
import java.util.stream.*;
import cs1410lib.Dialogs;
import graphics.GraphWindow;

/**
 * Methods in support of PS6.
 * 
 * @author Brandon Walters and Joe Zachary
 */
public class GraphingMethods
{
    /**
     * Constant used to request a max operation
     */
    public final static int MAX = 0;

    /**
     * Constant used to request a min operation
     */
    public final static int MIN = 1;

    /**
     * Constant used to request a sum operation
     */
    public final static int SUM = 2;

    /**
     * Constant used to request an average operation
     */
    public final static int AVG = 3;

    /**
     * The dataSource must consist of one or more lines. If there is not at least one line, the method throws an
     * IllegalArgumentException whose message explains what is wrong.
     * 
     * Each line must consist of some text (a key), followed by a tab character, followed by a double literal (a value),
     * followed by a newline.
     * 
     * If any lines are encountered that don't meet this criteria, the method throws an IllegalArgumentException whose
     * message explains what is wrong.
     * 
     * Otherwise, the map returned by the method (here called categoryMap) must have all of these properties:
     * 
     * (1) The set of keys contained by categoryMap must be the same as the set of keys that occur in the Scanner
     * 
     * (2) The list valueMap.get(key) must contain exactly the same numbers that appear as values on lines in the
     * Scanner that begin with key. The values must occur in the list in the same order as they appear in the Scanner.
     * 
     * For example, if the Scanner contains
     * 
     * <pre>
     * Utah        10
     * Nevada       3
     * Utah         2
     * California  14
     * Arizona     21
     * Utah         2
     * California   7
     * California   6
     * Nevada      11
     * California   1
     * </pre>
     * 
     * (where the spaces in each line are intended to be a single tab), then this map should be returned:
     * 
     * <pre>
     *  Arizona    {21}
     *  California {14, 7, 6, 1} 
     *  Nevada     {3, 11}
     *  Utah       {10, 2, 2}
     * </pre>
     */

    /**
     * 
     * This method, readTable, takes in a scanner of data and uses that data to create two states, one where the key is
     * already in the map and one where it is not. If any problem is encountered, the method throws an
     * IllegalArgumentException.
     */
    public static TreeMap<String, ArrayList<Double>> readTable (Scanner dataSource)
    {
        if (dataSource.hasNext() == false) {
            throw new IllegalArgumentException("Scanner is empty.");
        }
        TreeMap<String, ArrayList<Double>> map = new TreeMap<>();
        try
        {
            while (dataSource.hasNext())
            {
                String nextInputState = dataSource.next();
                String nextInputNumber = dataSource.next();
                double nextInputDouble = Double.parseDouble(nextInputNumber);

                if (map.containsKey(nextInputState) == false)
                {
                    ArrayList<Double> newStateArray = new ArrayList<Double>();
                    newStateArray.add(nextInputDouble);
                    map.put(nextInputState, newStateArray);
                }
                else
                {
                    ArrayList<Double> previousValues = map.get(nextInputState);
                    previousValues.add(nextInputDouble);
                    map.replace(nextInputState, previousValues);
                }
            }
        }
        catch (NoSuchElementException e)
        {
            throw new IllegalArgumentException("No data for scanner.");
        }
        return map;
    }

    /**
     * If categoryMap is of size zero, throws an IllegalArgumentException whose message explains what is wrong.
     * 
     * Else if any of the values in the category map is an empty set, throws an IllegalArgumentException whose message
     * explains what is wrong.
     * 
     * Else if any of the numbers in the categoryMap is not positive, throws an IllegalAgumentException whose message
     * explains what is wrong.
     * 
     * Else if operation is anything other than SUM, AVG, MAX, or MIN, throws an IllegalArgumentException whose message
     * explains what is wrong.
     *
     * Else, returns a TreeMap<String, Double> (here called summaryMap) such that:
     * 
     * (1) The sets of keys contained by categoryMap and summaryMap are the same
     * 
     * (2) For all keys, summaryMap.get(key) is the result of combining the numbers contained in the set
     * categoryMap.get(key) using the specified operation. If the operation is MAX, "combining" means finding the
     * largest of the numbers. If the operation is MIN, "combining" means finding the smallest of the numbers. If the
     * operation is SUM, combining means summing the numbers. If the operation is AVG, combining means averaging the
     * numbers.
     * 
     * For example, suppose the categoryMap maps like this:
     * 
     * <pre>
     *  Arizona    {21}
     *  California {14, 7, 6, 1} 
     *  Nevada     {3, 11}
     *  Utah       {10, 2, 2}
     * </pre>
     * 
     * and the operation is SUM. The map that is returned must map like this:
     * 
     * <pre>
     *  Arizona    21
     *  California 28 
     *  Nevada     14
     *  Utah       14
     * </pre>
     */

    /**
     * 
     * This method, prepareGraph, takes in an operation and the categoryMap output from readGraph, and then uses that
     * map data to perform the selected operation. If any problem is encountered, the method throws an
     * IllegalArgumentException.
     */
    public static TreeMap<String, Double> prepareGraph (TreeMap<String, ArrayList<Double>> categoryMap, int operation)
    {
        if (categoryMap.size() == 0 || operation < 0 || operation > 3)
        {
            throw new IllegalArgumentException("Problem with operation/map size.");
        }
        TreeMap<String, Double> summaryMap = new TreeMap<>();
        /**
         * This branch performs the MAX operation.
         */
        if (operation == 0)
        {
            String stateToMax = categoryMap.firstKey();
            while (categoryMap.higherKey(stateToMax) != null)
            {
                ArrayList<Double> arrayToMax = categoryMap.get(stateToMax);
                double max = Collections.max(arrayToMax);
                summaryMap.put(stateToMax, max);
                stateToMax = categoryMap.higherKey(stateToMax);
            }
            ArrayList<Double> arrayToMax = categoryMap.get(stateToMax);
            double max = Collections.max(arrayToMax);
            summaryMap.put(stateToMax, max);
        }

        /**
         * This branch performs the MIN operation.
         */
        else if (operation == 1)
        {
            String stateToMin = categoryMap.firstKey();
            while (categoryMap.higherKey(stateToMin) != null)
            {
                ArrayList<Double> arrayToMin = categoryMap.get(stateToMin);
                double min = Collections.min(arrayToMin);
                summaryMap.put(stateToMin, min);
                stateToMin = categoryMap.higherKey(stateToMin);
            }
            ArrayList<Double> arrayToMin = categoryMap.get(stateToMin);
            double min = Collections.min(arrayToMin);
            summaryMap.put(stateToMin, min);
        }

        /**
         * This branch performs the SUM operation.
         */
        else if (operation == 2)
        {
            String stateToSum = categoryMap.firstKey();
            while (categoryMap.higherKey(stateToSum) != null)
            {
                double sum = 0;
                ArrayList<Double> arrayToSum = categoryMap.get(stateToSum);
                for (int i = 0; i < arrayToSum.size(); i++)
                {
                    sum = sum + arrayToSum.get(i);
                }
                summaryMap.put(stateToSum, sum);
                stateToSum = categoryMap.higherKey(stateToSum);
            }
            double sum = 0;
            ArrayList<Double> arrayToSum = categoryMap.get(stateToSum);
            for (int i = 0; i < arrayToSum.size(); i++)
            {
                sum = sum + arrayToSum.get(i);
            }
            summaryMap.put(stateToSum, sum);
        }

        /**
         * This branch performs the AVG operation.
         */
        else if (operation == 3)
        {
            String stateToAverage = categoryMap.firstKey();
            while (categoryMap.higherKey(stateToAverage) != null)
            {
                double sum = 0;
                ArrayList<Double> arrayToAverage = categoryMap.get(stateToAverage);
                for (int i = 0; i < arrayToAverage.size(); i++)
                {
                    sum = sum + arrayToAverage.get(i);
                }
                double average = sum / arrayToAverage.size();
                summaryMap.put(stateToAverage, average);
                stateToAverage = categoryMap.higherKey(stateToAverage);
            }
            double sum = 0;
            ArrayList<Double> arrayToAverage = categoryMap.get(stateToAverage);
            for (int i = 0; i < arrayToAverage.size(); i++)
            {
                sum = sum + arrayToAverage.get(i);
            }
            double average = sum / arrayToAverage.size();
            summaryMap.put(stateToAverage, average);
        }
        return summaryMap;
    }

    /**
     * If colorMap is empty, throws an IllegalArgumentException.
     * 
     * If there is a key in colorMap that does not occur in summaryMap, throws an IllegalArgumentException whose message
     * explains what is wrong.
     * 
     * If any of the numbers in the summaryMap is non-positive, throws an IllegalArgumentException whose message
     * explains what is wrong.
     * 
     * Otherwise, displays on g the subset of the data contained in summaryMap that has a key that appears in colorMap
     * with either a pie chart (if usePieChart is true) or a bar graph (otherwise), using the colors in colorMap.
     * 
     * Let SUM be the sum of all the values in summaryMap whose keys also appear in colorMap, let KEY be a key in
     * colorMap, let VALUE be the value to which KEY maps in summaryMap, and let COLOR be the color to which KEY maps in
     * colorMap. The area of KEY's slice (in a pie chart) and the length of KEY's bar (in a bar graph) must be
     * proportional to VALUE/SUM. The slice/bar should be labeled with both KEY and VALUE, and it should be colored with
     * COLOR.
     * 
     * For example, suppose summaryMap has this mapping:
     * 
     * <pre>
     *  Arizona    21
     *  California 28 
     *  Nevada     14
     *  Utah       14
     * </pre>
     * 
     * and colorMap has this mapping:
     * 
     * <pre>
     *  California Color.GREEN
     *  Nevada     Color.BLUE
     *  Utah       Color.RED
     * </pre>
     * 
     * Since Arizona does not appear as a key in colorMap, Arizona's entry in summaryMap is ignored.
     * 
     * In a pie chart Utah and Nevada should each have a quarter of the pie and California should have half. In a bar
     * graph, California's line should be twice as long as Utah's and Nevada's. Utah's slice/bar should be red, Nevada's
     * blue, and California's green.
     * 
     * The method should display the pie chart or bar graph by drawing on the g parameter. The example code below draws
     * both a pie chart and a bar graph for the situation described above.
     */

    /**
     * 
     * This method, drawGraph, takes the output map from the prepareGraph method, a Graphics object, and a color map to
     * create either a bar or pie graph. If an exception is encountered, an IllegalArgumentException is thrown.
     */
    public static void drawGraph (Graphics g, TreeMap<String, Double> summaryMap, TreeMap<String, Color> colorMap,
            boolean usePieChart)
    {

        final int TOP = 10;        // Offset of graph from top edge
        final int LEFT = 10;       // Offset of graph from left edge
        final int DIAM = 300;      // Diameter of pie chart
        final int WIDTH = 10;      // Width of bar in bar chart

        if (colorMap.isEmpty())
        {
            throw new IllegalArgumentException("Problem with color map.");
        }

        /**
         * The method creates two Arrays, one for names and one for values.
         */
        double sumOfAllValues = 0;
        String maxOutputString = summaryMap.lastKey();
        double maxOutput = summaryMap.get(maxOutputString);
        ArrayList<String> arrayOfNames = new ArrayList<>();
        ArrayList<Double> arrayOfValues = new ArrayList<>();
        while (summaryMap.lowerKey(maxOutputString) != null)
        {
            for (int i = 0; i < summaryMap.size(); i++)
            {
                arrayOfNames.add(maxOutputString);
                arrayOfValues.add(maxOutput);
                sumOfAllValues = sumOfAllValues + maxOutput;
                if (summaryMap.lowerKey(maxOutputString) != null)
                {
                    maxOutputString = summaryMap.lowerKey(maxOutputString);
                    maxOutput = summaryMap.get(maxOutputString);
                }
                else
                {

                }
            }
        }

        /**
         * The method then takes the sum of all the values given and calculates each individual name's percentage of the
         * total.
         */
        int sumOfAllValuesInt = (int) sumOfAllValues;
        int biggestPercentage = (int) ((arrayOfValues.get(0) * 100) / sumOfAllValuesInt);
        int nextBiggestPercentage = (int) ((arrayOfValues.get(1) * 100) / sumOfAllValuesInt);
        int thirdBiggestPercentage = (int) ((arrayOfValues.get(2) * 100) / sumOfAllValuesInt);
        ArrayList<Integer> percentageArray = new ArrayList<Integer>();
        percentageArray.add(biggestPercentage);
        percentageArray.add(nextBiggestPercentage);
        percentageArray.add(thirdBiggestPercentage);

        if (arrayOfValues.get(0) < 0 || arrayOfValues.get(1) < 0 || arrayOfValues.get(2) < 0)
        {
            throw new IllegalArgumentException("Negative double from summaryMap.");
        }

        /**
         * Finally, the method then uses a for loop to iterate through the colorMap and create each key's graph.
         */
        
        // Draw a pie chart if requested
        if (usePieChart)
        {
            g.setColor(colorMap.get(arrayOfNames.get(0)));
            g.fillArc(LEFT, TOP, DIAM, DIAM, 0, 360);
            g.fillRect(LEFT + DIAM + 2 * WIDTH, TOP, WIDTH, WIDTH);
            g.setColor(Color.black);
            g.drawString(arrayOfNames.get(0) + " " + arrayOfValues.get(0), LEFT + DIAM + 4 * WIDTH, TOP + WIDTH);

            int yChange = 3;
            for (int i = 1; i < colorMap.size() - 1; i++)
            {
                g.setColor(colorMap.get(arrayOfNames.get(i)));
                g.fillArc(LEFT, TOP, DIAM, DIAM, 0, percentageArray.get(i) * DIAM);
                g.fillRect(LEFT + DIAM + 2 * WIDTH, TOP + (8 * yChange), WIDTH, WIDTH);
                g.setColor(Color.black);
                g.drawString(arrayOfNames.get(i) + " " + arrayOfValues.get(i), LEFT + DIAM + 4 * WIDTH,
                        TOP + yChange * WIDTH);

                yChange = yChange + 2;
            }
        }
        // Draw a bar chart if requested
        else
        {

            int yChange = 0;

            for (int i = 0; i < colorMap.size() - 1; i++)
            {
                g.setColor(colorMap.get(arrayOfNames.get(i)));
                g.fillRect(LEFT + (DIAM - 130) - DIAM / (percentageArray.get(i) + 5), TOP + (yChange * WIDTH), DIAM / 2,
                        2 * WIDTH);
                g.setColor(Color.black);
                g.drawString(arrayOfNames.get(i) + " " + arrayOfValues.get(i), LEFT + DIAM + 2 * WIDTH,
                        TOP + ((yChange + 1) * WIDTH) + WIDTH / 2);

                yChange = yChange + 3;
            }
        }
    }
}
