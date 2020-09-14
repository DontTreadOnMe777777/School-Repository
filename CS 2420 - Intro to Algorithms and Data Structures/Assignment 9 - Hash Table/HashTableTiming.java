package assign09;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class HashTableTiming {

	public static void main(String[] args) {
		// Do 10000 lookups and use the average running time.
		int timesToLoop = 10000;
		
		Random rnd = new Random();
		
		HashTable<StudentBadHash, Integer> badTable = new HashTable<StudentBadHash, Integer>();
		//HashTable<StudentMediumHash, Double> mediumTable = new HashTable<StudentMediumHash, Double>();
		//HashTable<StudentGoodHash, Double> goodTable = new HashTable<StudentGoodHash, Double>();
		
		ArrayList<Integer> UIDList = new ArrayList<Integer>();
		for(int i = 0; i <= 2000000; i+= 1)
			UIDList.add(i);
		Collections.shuffle(UIDList);


		//StudentMediumHash studentMedium = new StudentMediumHash(1000000, "First", "Last");
		//StudentGoodHash studentGood = new StudentGoodHash(1000000, "First", "Last");
		// For each problem size n . . .
		for (int n = 100000; n <= 2000000; n += 10000) {
			badTable.put(new StudentBadHash(UIDList.get(n), "bubbles", "Bubbles"), rnd.nextInt());

			
			long startTime, midpointTime, stopTime;

			// First, spin computing stuff until one second has gone by.
			// This allows this thread to stabilize.
			startTime = System.nanoTime();

			while (System.nanoTime() - startTime < 1000000000) { 
				
			}

			// Now, run the test.
			startTime = System.nanoTime();

			for (int i = 0; i < timesToLoop; i++) {
				badTable.containsValue(rnd.nextInt());
			}

			midpointTime = System.nanoTime();

			// Run a loop to capture the cost of running the "timesToLoop" loop and generating a random number for the GPA search.
			for (int i = 0; i < timesToLoop; i++) {
				rnd.nextInt();
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
