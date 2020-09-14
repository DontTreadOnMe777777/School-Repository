package assign08;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class BinarySearchTreeTiming {

	public static void main(String[] args) {

		// Do 1000 lookups and use the average running time.
		int timesToLoop = 10;
		
		Random rdm = new Random();
		
		List<Integer> sortedItems = new ArrayList<Integer>();
		List<Integer> unsortedItems;
		BinarySearchTree<Integer> bst = new BinarySearchTree<Integer>();
		
		System.out.println(" \tadd\tremove\tcontains");

		// For each problem size n . . .
		for (int n = 1; n <= 100; n += 1) {
			
			
			
			//sortedItems.add(n);
			//unsortedItems = new ArrayList<Integer>(sortedItems.subList(0, n));
			//Collections.shuffle(unsortedItems);
			
			bst.add(rdm.nextInt(n));
			//bst.add(n);
			
			long startTime, midpointTime, stopTime;

			// First, spin computing stuff until one second has gone by.
			// This allows this thread to stabilize.
			startTime = System.nanoTime();

			while (System.nanoTime() - startTime < 1000000000) {

			}

			// Now, run the test.
			startTime = System.nanoTime();

			for (int i = 0; i < timesToLoop; i++) {
				bst.contains(n);
			}

			midpointTime = System.nanoTime();

			// Run a loop to capture the cost of running the "timesToLoop" loop.
			for (int i = 0; i < timesToLoop; i++) {
				
			}

			stopTime = System.nanoTime();

			// Compute the time, subtract the cost of running the loop
			// from the cost of running the loop and doing the lookups.
			// Average it over the number of runs.
			double averageTime = ((midpointTime - startTime) - (stopTime - midpointTime)) / (double) timesToLoop;
			
			System.out.println(n + "\t" + averageTime);
		}
	}
}
