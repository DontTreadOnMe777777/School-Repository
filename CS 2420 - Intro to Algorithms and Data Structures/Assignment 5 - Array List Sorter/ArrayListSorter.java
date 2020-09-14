package assign05;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

/**
 * A generic class, that sorts array lists filled with objects that extend
 * the comparable class
 * 
 * @author Brandon Ernst && Brandon Walters
 *
 */
public class ArrayListSorter {
	
	private static int threshold = 4;
	
	/**
	 * Takes an Array List and sorts it using a merge Sort
	 */
	public static <T extends Comparable<? super T>> void mergesort(ArrayList<T> input) {
		mergesort(input, 0, input.size());
	}
	
	/**
	 * A method that sorts an array using an insertion sort and two temporary arrays.
	 **/
	@SuppressWarnings("unchecked")
	private static <T extends Comparable<? super T>> void mergesort(ArrayList<T> input, int low, int high) {
		if(input.size() == 0)
			throw new IllegalArgumentException("Can not sort an empty array");
		if(input.size() == 1)
			return;
		
		else {
			int mid = (low + high) / 2;
			ArrayList<T> firstTemp = new ArrayList<>();
			ArrayList<T> secondTemp = new ArrayList<>();
			
			for (int i = 0; i < mid; i++) {
				firstTemp.add(input.get(i));
			}
			
			for (int i = mid; i < high; i++) {
				secondTemp.add(input.get(i));
			}
			
			mergesort(firstTemp, low, firstTemp.size());
			mergesort(secondTemp, low, secondTemp.size());
			
			merge(input, firstTemp, secondTemp);
		}
		
	}
	
	/**
	 * Recursively sorts and combines small arrays using two temporary arrays and an insertion sort.
	 */
	private static <T extends Comparable<? super T>> void merge(ArrayList<T> input, ArrayList<T> firstTemp, ArrayList<T> secondTemp) {
		
		if (input.size() <= threshold) {
			insertionSort(input);
		}
		
		int i = 0;
		int j = 0;
		input.clear();
		
		while (i < firstTemp.size() && j < secondTemp.size()) {
			if (firstTemp.get(i).compareTo(secondTemp.get(j)) <= 0) {
				input.add(firstTemp.get(i++));
			}
			else {
				input.add(secondTemp.get(j++));
			}
		}
		while (i < firstTemp.size()) {
			input.add(firstTemp.get(i++));
		}
		while (j < secondTemp.size()) {
			input.add(secondTemp.get(j++));
		}
	}

	/**
	 * A method that takes an array list of objects that extend the comparable class
	 * and sorts it using a quick sort
	 * 
	 * Can choose between using the bottom, middle, or top element as the pivot point
	 * by inputing it as a string
	 */
	public static <T extends Comparable<? super T>> void quicksort(ArrayList<T> input) {
		quicksort(input, 0, input.size() - 1);
	}
	
	private static <T extends Comparable<? super T>> void quicksort(ArrayList<T> input, int low, int high) {
		if(low < high) {
			int pos = partition(input, low, high);
			quicksort(input, low, pos - 1);
			quicksort(input, pos + 1, high);
					
		}
			
	}
	
	private static <T extends Comparable<? super T>> int partition(ArrayList<T> input, int low, int high) {
		Random rand = new Random();
		int pivot = rand.nextInt(input.size());
		swap(input, pivot, high);
		pivot = high;
		int i = low;
		for(int j = low; j < high; j++) {
			if(input.get(j).compareTo(input.get(pivot)) < 0) {
				swap(input, j, i);
				i++;
			}
		}
		swap(input, i, pivot);
		return i;
	}
	
	private static <T extends Comparable<? super T>> void swap(ArrayList<T> input, int i, int j) {
		T temp = input.get(i);
		input.set(i, input.get(j));
		input.set(j, temp);
	}


	public static ArrayList<Integer> generateAscending(int size) {
		ArrayList<Integer> output = new ArrayList<Integer>();
		for (int i = 1; i <= size; i++) {
			output.add(i);
		}
		return output;
	}

	public static ArrayList<Integer> generateDescending(int size) {
		ArrayList<Integer> output = new ArrayList<Integer>();
		for (int i = 0; i < size; i++) {
			output.add(size - i);
		}
		return output;
	}

	public static ArrayList<Integer> generatePermuted(int size) {
		ArrayList<Integer> output = new ArrayList<Integer>();
		for (int i = 1; i <= size; i++) {
			output.add(i);
		}
		Collections.shuffle(output);
		return output;
	}
	
	/**
	 * Takes any array of items and a comparator and sorts the given array with insertion sort.
	 */
	public static <T extends Comparable<? super T>> void insertionSort(ArrayList<T> input) {
		for (int i = 1; i < input.size(); i++) {
			T val = input.get(i);
			int j;
			for (j = i - 1; j >= 0 && input.get(j).compareTo(val) > 0; j--) {
				input.set(j + 1, input.get(j));
			}
			input.set(j + 1, val);
		}
	}
}