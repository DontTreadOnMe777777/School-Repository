package cs1410;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeMap;
import org.junit.Test;

public class SampleTestCase
{
    /**
     * One sample test case.  Don't conclude that your implementation is perfect simply because it passes this test!
     */
    @Test
    public void testReadTable ()
    {
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
}
