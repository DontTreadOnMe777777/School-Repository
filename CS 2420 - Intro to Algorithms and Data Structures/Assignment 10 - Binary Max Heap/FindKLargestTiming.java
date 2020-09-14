package assign10;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FindKLargestTiming {

	public static void main(String[] args) {

		int timesToLoop = 1000;

		System.out.println(" \tSmall K Heap\tLarge K Heap\tSmall K Sort\tLarge K Sort");

		List<Integer> findKLargeHeap = new ArrayList<Integer>();
		List<Integer> findKLargeSort = new ArrayList<Integer>();

		// For each problem size n . . .
		for (int n = 10000; n <= 100000; n += 10000) {

			for (int i = 0; i < n; i++) {
				findKLargeHeap.add(i);
				findKLargeSort.add(i);
			}

			long startTime, midpointTime, stopTime;

			// First, spin computing stuff until one second has gone by.
			// This allows this thread to stabilize.
			startTime = System.nanoTime();

			while (System.nanoTime() - startTime < 1000000000) {

			}

			// Now, run the test.
			startTime = System.nanoTime();

			for (int i = 0; i < timesToLoop; i++) {
				FindKLargest.findKLargestHeap(findKLargeHeap, 10);
			}

			midpointTime = System.nanoTime();

			// Run a loop to capture the cost of running the "timesToLoop" loop.
			for (int i = 0; i < timesToLoop; i++) {
			}

			stopTime = System.nanoTime();

			// Compute the time, subtract the cost of running the loop
			// from the cost of running the loop and doing the lookups.
			// Average it over the number of runs.
			double averageTimeSmallKHeap = ((midpointTime - startTime) - (stopTime - midpointTime)) / (double) timesToLoop;

			// First, spin computing stuff until one second has gone by.
			// This allows this thread to stabilize.
			startTime = System.nanoTime();

			while (System.nanoTime() - startTime < 1000000000) {

			}

			// Now, run the test.
			startTime = System.nanoTime();

			for (int i = 0; i < timesToLoop; i++) {
				FindKLargest.findKLargestHeap(findKLargeHeap, n - (n / 10));
			}

			midpointTime = System.nanoTime();

			// Run a loop to capture the cost of running the "timesToLoop" loop.
			for (int i = 0; i < timesToLoop; i++) {
			}

			stopTime = System.nanoTime();

			// Compute the time, subtract the cost of running the loop
			// from the cost of running the loop and doing the lookups.
			// Average it over the number of runs.
			double averageTimeLargeKHeap = ((midpointTime - startTime) - (stopTime - midpointTime)) / (double) timesToLoop;
			

			// Now, run the test.
			startTime = System.nanoTime();

			for (int i = 0; i < timesToLoop; i++) {
				FindKLargest.findKLargestSort(findKLargeSort, 10);
			}

			midpointTime = System.nanoTime();

			// Run a loop to capture the cost of running the "timesToLoop" loop.
			for (int i = 0; i < timesToLoop; i++) {
			}

			stopTime = System.nanoTime();

			// Compute the time, subtract the cost of running the loop
			// from the cost of running the loop and doing the lookups.
			// Average it over the number of runs.
			double averageTimeSmallKSort = ((midpointTime - startTime) - (stopTime - midpointTime)) / (double) timesToLoop;

			// First, spin computing stuff until one second has gone by.
			// This allows this thread to stabilize.
			startTime = System.nanoTime();

			while (System.nanoTime() - startTime < 1000000000) {

			}

			// Now, run the test.
			startTime = System.nanoTime();

			for (int i = 0; i < timesToLoop; i++) {
				FindKLargest.findKLargestSort(findKLargeSort, n - (n / 10));
			}

			midpointTime = System.nanoTime();

			// Run a loop to capture the cost of running the "timesToLoop" loop.
			for (int i = 0; i < timesToLoop; i++) {
			}

			stopTime = System.nanoTime();

			// Compute the time, subtract the cost of running the loop
			// from the cost of running the loop and doing the lookups.
			// Average it over the number of runs.
			double averageTimeLargeKSort = ((midpointTime - startTime) - (stopTime - midpointTime)) / (double) timesToLoop;

			System.out.println(n + "\t" + averageTimeSmallKHeap + "\t" + averageTimeLargeKHeap + "\t" + averageTimeSmallKSort + "\t" + averageTimeLargeKSort);
		}
	}
}
