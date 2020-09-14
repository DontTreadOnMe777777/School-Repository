package assign10;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class BinaryMaxHeapTiming {

	public static void main(String[] args) {

		int timesToLoop = 100000;

		System.out.println(" \tAdd\tPeek\textractMax");

		List<Integer> extractMaxList = new ArrayList<Integer>();
		List<Integer> peekList = new ArrayList<Integer>();
		List<Integer> addListWorstCase = new ArrayList<Integer>();
		BinaryMaxHeap<Integer> addHeap = new BinaryMaxHeap<Integer>();

		// For each problem size n . . .
		for (int n = 100000; n <= 2000000; n += 100000) {

			for (int i = 0; i < n; i++) {
				extractMaxList.add(i);
				peekList.add(i);
				addListWorstCase.add(i);
			}
			Collections.shuffle(extractMaxList);
			BinaryMaxHeap<Integer> extractMaxHeap = new BinaryMaxHeap<Integer>(extractMaxList);
			Collections.shuffle(peekList);
			BinaryMaxHeap<Integer> peekHeap = new BinaryMaxHeap<Integer>(peekList);
			BinaryMaxHeap<Integer> addHeapWorstCase = new BinaryMaxHeap<Integer>();

			long startTime, midpointTime, stopTime;

			// First, spin computing stuff until one second has gone by.
			// This allows this thread to stabilize.
			startTime = System.nanoTime();

			while (System.nanoTime() - startTime < 1000000000) {

			}

			// Now, run the test.
			startTime = System.nanoTime();

			for (int i = 0; i < timesToLoop; i++) {
				addHeap.add(peekList.get(n - 1));
			}

			midpointTime = System.nanoTime();

			// Run a loop to capture the cost of running the "timesToLoop" loop.
			for (int i = 0; i < timesToLoop; i++) {
				peekList.get(n - 1);
			}

			stopTime = System.nanoTime();

			// Compute the time, subtract the cost of running the loop
			// from the cost of running the loop and doing the lookups.
			// Average it over the number of runs.
			double averageTimeAdd = ((midpointTime - startTime) - (stopTime - midpointTime)) / (double) timesToLoop;

			// First, spin computing stuff until one second has gone by.
			// This allows this thread to stabilize.
			startTime = System.nanoTime();

			while (System.nanoTime() - startTime < 1000000000) {

			}

			// Now, run the test.
			startTime = System.nanoTime();

			for (int i = 0; i < timesToLoop; i++) {
				addHeapWorstCase.add(addListWorstCase.get(i));
			}

			midpointTime = System.nanoTime();

			// Run a loop to capture the cost of running the "timesToLoop" loop.
			for (int i = 0; i < timesToLoop; i++) {
				addListWorstCase.get(i);
			}

			stopTime = System.nanoTime();

			// Compute the time, subtract the cost of running the loop
			// from the cost of running the loop and doing the lookups.
			// Average it over the number of runs.
			double averageTimeAddWorstCase = ((midpointTime - startTime) - (stopTime - midpointTime))
					/ (double) timesToLoop;

			// First, spin computing stuff until one second has gone by.
			// This allows this thread to stabilize.
			startTime = System.nanoTime();

			while (System.nanoTime() - startTime < 1000000000) {

			}

			// Now, run the test.
			startTime = System.nanoTime();

			for (int i = 0; i < timesToLoop; i++) {
				peekHeap.peek();
			}

			midpointTime = System.nanoTime();

			// Run a loop to capture the cost of running the "timesToLoop" loop.
			for (int i = 0; i < timesToLoop; i++) {
			}

			stopTime = System.nanoTime();

			// Compute the time, subtract the cost of running the loop
			// from the cost of running the loop and doing the lookups.
			// Average it over the number of runs.
			double averageTimePeek = ((midpointTime - startTime) - (stopTime - midpointTime)) / (double) timesToLoop;

			// First, spin computing stuff until one second has gone by.
			// This allows this thread to stabilize.
			startTime = System.nanoTime();

			while (System.nanoTime() - startTime < 1000000000) {

			}

			// Now, run the test.
			startTime = System.nanoTime();

			for (int i = 0; i < timesToLoop; i++) {
				extractMaxHeap.extractMax();
			}

			midpointTime = System.nanoTime();

			// Run a loop to capture the cost of running the "timesToLoop" loop.
			for (int i = 0; i < timesToLoop; i++) {

			}

			stopTime = System.nanoTime();

			// Compute the time, subtract the cost of running the loop
			// from the cost of running the loop and doing the lookups.
			// Average it over the number of runs.
			double averageTimeExtractMax = ((midpointTime - startTime) - (stopTime - midpointTime))
					/ (double) timesToLoop;

			System.out.println(n + "\t" + averageTimeAdd + "\t" + averageTimePeek + "\t" + averageTimeExtractMax + "\t" + averageTimeAddWorstCase);
		}
	}
}
