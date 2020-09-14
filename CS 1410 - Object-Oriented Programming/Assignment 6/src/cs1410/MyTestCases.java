package cs1410;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeMap;
import org.junit.Test;

public class MyTestCases
{

    @Test(expected = IllegalArgumentException.class)
    public void testReadTable ()
    {
        try (Scanner scn1 = new Scanner(""))
        {
            TreeMap<String, ArrayList<Double>> actual = GraphingMethods.readTable(scn1);
            TreeMap<String, ArrayList<Double>> expected = new TreeMap<>();
            assertEquals(expected, actual);
        }
        try (Scanner scn = new Scanner(
                "Utah\t10\nNevada\t3\nUtah\t2\nCalifornia\t14\nArizona\t21\nUtah\t2\nCalifornia\t7\nCalifornia\t6\nNevada\t11\nCalifornia\t1\n"))
        {
            TreeMap<String, ArrayList<Double>> actual = GraphingMethods.readTable(scn);

            TreeMap<String, ArrayList<Double>> expected = new TreeMap<>();
            ArrayList<Double> azList = new ArrayList<>();
            azList.add(21.0);
            expected.put("Arizona", azList);

            ArrayList<Double> caList = new ArrayList<>();
            caList.add(14.0);
            caList.add(7.0);
            caList.add(6.0);
            caList.add(1.0);
            expected.put("California", caList);

            ArrayList<Double> nvList = new ArrayList<>();
            nvList.add(3.0);
            nvList.add(11.0);
            expected.put("Nevada", nvList);

            ArrayList<Double> utList = new ArrayList<>();
            utList.add(10.0);
            utList.add(2.0);
            utList.add(2.0);
            expected.put("Utah", utList);

            assertEquals(expected, actual);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPrepareGraph ()
    {
        try (Scanner scn = new Scanner(
                "Utah\t10\nNevada\t3\nUtah\t2\nCalifornia\t14\nArizona\t21\nUtah\t2\nCalifornia\t7\nCalifornia\t6\nNevada\t11\nCalifornia\t1\n"))
        {
            TreeMap<String, ArrayList<Double>> categoryMap = GraphingMethods.readTable(scn);
            TreeMap<String, Double> expected = new TreeMap<>();
            TreeMap<String, Double> actual = GraphingMethods.prepareGraph(categoryMap, -1);
            expected.put("Nevada", 11.0);
            expected.put("California", 14.0);
            expected.put("Arizona", 21.0);
            expected.put("Utah", 10.0);

            assertEquals(expected, actual);
        }

        try (Scanner scn = new Scanner(""))
        {
            TreeMap<String, ArrayList<Double>> categoryMap = GraphingMethods.readTable(scn);
            TreeMap<String, Double> expected = new TreeMap<>();
            TreeMap<String, Double> actual = GraphingMethods.prepareGraph(categoryMap, 2);
            expected.put("Nevada", 11.0);
            expected.put("California", 14.0);
            expected.put("Arizona", 21.0);
            expected.put("Utah", 10.0);

            assertEquals(expected, actual);

        }
        try (Scanner scn = new Scanner(
                "Utah\t10\nNevada\t3\nUtah\t2\nCalifornia\t14\nArizona\t21\nUtah\t2\nCalifornia\t7\nCalifornia\t6\nNevada\t11\nCalifornia\t1\n"))
        {
            TreeMap<String, ArrayList<Double>> categoryMap = GraphingMethods.readTable(scn);
            TreeMap<String, Double> expected = new TreeMap<>();
            TreeMap<String, Double> actual = GraphingMethods.prepareGraph(categoryMap, 0);
            expected.put("Nevada", 11.0);
            expected.put("California", 14.0);
            expected.put("Arizona", 21.0);
            expected.put("Utah", 10.0);

            assertEquals(expected, actual);

        }
        try (Scanner scn = new Scanner(
                "Utah\t10\nNevada\t3\nUtah\t2\nCalifornia\t14\nArizona\t21\nUtah\t2\nCalifornia\t7\nCalifornia\t6\nNevada\t11\nCalifornia\t1\n"))
        {
            TreeMap<String, ArrayList<Double>> categoryMap = GraphingMethods.readTable(scn);
            TreeMap<String, Double> expected = new TreeMap<>();
            TreeMap<String, Double> actual = GraphingMethods.prepareGraph(categoryMap, 1);
            expected.put("Utah", 2.0);
            expected.put("Nevada", 3.0);
            expected.put("California", 1.0);
            expected.put("Arizona", 21.0);

            assertEquals(actual, expected);

        }

        try (Scanner scn = new Scanner(
                "Utah\t10\nNevada\t3\nUtah\t2\nCalifornia\t14\nArizona\t21\nUtah\t2\nCalifornia\t7\nCalifornia\t6\nNevada\t11\nCalifornia\t1\n"))
        {
            TreeMap<String, ArrayList<Double>> categoryMap = GraphingMethods.readTable(scn);
            TreeMap<String, Double> expected = new TreeMap<>();
            TreeMap<String, Double> actual = GraphingMethods.prepareGraph(categoryMap, 2);
            expected.put("Utah", 14.0);
            expected.put("Nevada", 14.0);
            expected.put("California", 28.0);
            expected.put("Arizona", 21.0);

            assertEquals(actual, expected);

        }

        try (Scanner scn = new Scanner(
                "Utah\t10\nNevada\t3\nUtah\t2\nCalifornia\t14\nArizona\t21\nUtah\t2\nCalifornia\t7\nCalifornia\t6\nNevada\t11\nCalifornia\t1\n"))
        {
            TreeMap<String, ArrayList<Double>> categoryMap = GraphingMethods.readTable(scn);
            TreeMap<String, Double> expected = new TreeMap<>();
            TreeMap<String, Double> actual = GraphingMethods.prepareGraph(categoryMap, 3);
            expected.put("Utah", 4.666666666666667);
            expected.put("Nevada", 7.0);
            expected.put("California", 7.0);
            expected.put("Arizona", 21.0);

            assertEquals(actual, expected);

        }
    }
}
