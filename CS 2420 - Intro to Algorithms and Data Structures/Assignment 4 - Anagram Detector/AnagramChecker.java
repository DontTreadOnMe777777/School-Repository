package assign04;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Comparator;
import java.util.Scanner;

/**
 * A class used to check anagrams and retrieve the largest group of anagrams from an array of Strings 
 * or from a file in the file system
 * @author Brandon Ernst && Brandon Walters
 */
public class AnagramChecker {
	
	public static void main(String[] args) throws FileNotFoundException
	{
		System.out.println(getLargestAnagramGroup("Testerdoc.txt").toString());
	}

	/**
	 * Takes in an input string and orders it lexicographically using an insertion
	 * sort.
	 */
	public static String sort(String input) {
		//input = input.toLowerCase();
		char[] charArray = input.toCharArray();
		for (int i = 1; i < charArray.length; i++) {
			char val = charArray[i];
			int j;
			for (j = i - 1; j >= 0 && charArray[j] > val; j--) {
				charArray[j + 1] = charArray[j];
			}
			charArray[j + 1] = val;
		}
		String result = "";
		for (int i = 0; i < charArray.length; i++) {
			result = result + charArray[i];
		}
		return result;
	}

	/**
	 * Takes any array of items and a comparator and sorts the given array with insertion sort.
	 */
	public static <T> void insertionSort(T[] inputArr, Comparator<? super T> comparator) {
		for (int i = 1; i < inputArr.length; i++) {
			T val = inputArr[i];
			int j;
			for (j = i - 1; j >= 0 && comparator.compare(inputArr[j], val) > 0; j--) {
				inputArr[j + 1] = inputArr[j];
			}
			inputArr[j + 1] = val;
		}
	}

	/**
	 * Takes two strings and checks if they are anagrams.
	 */
	public static boolean areAnagrams(String s1, String s2) {
		s1 = s1.toLowerCase();
		s2 = s2.toLowerCase();
		return sort(s1).equals(sort(s2));
	}

	/**
	 * Finds and returns the largest group of strings that are anagrams from an array of strings.
	 */
	public static String[] getLargestAnagramGroup(String[] input) {
		Comparator<String> comp = new compareString();
		insertionSort(input, comp);
		int largestSize = 0;
		String[] finalArray;
		String[] holdArray = new String[0];
		for (int i = 0; i < input.length; i++) {
			String hold = input[i];
			int size = 0;
			String[] tempArray = new String[input.length];
			for (int j = 0; j < input.length; j++) {
				if (areAnagrams(input[j], hold)) {
					tempArray[size] = input[j];
					size++;
				}
			}
			if (size > largestSize) {
				largestSize = size;
				holdArray = tempArray.clone();
			}
		}
		finalArray = new String[largestSize];
		for (int i = 0; i < finalArray.length; i++)
			finalArray[i] = holdArray[i];
		if(finalArray.length == 1)
			return new String[0];
		return finalArray;
	}

	/**
	 * Finds and returns the largest group of strings that are anagrams from a file containing a number of strings.
	 */
	public static String[] getLargestAnagramGroup(String filename) {
		Scanner scnr;
		try {
			scnr = new Scanner(new File(filename));
		} catch (FileNotFoundException e) {
			return null;
		}
		String[] arr = new String[100];
		int size = 0;
		for(int i = 0; scnr.hasNextLine(); i++){
			arr[i] = scnr.nextLine();
			size++;
		}
		String[] tempArr = new String[size];
		for(int i = 0; i < size; i++)
			tempArr[i] = arr[i];
		scnr.close();
		return getLargestAnagramGroup(tempArr);
	}
}

class compareString implements Comparator<String> {

	@Override
	public int compare(String o1, String o2) {
		 if(o1.compareToIgnoreCase(o2) > 1) {
			return 1;
		}
		
		else if(o1.compareToIgnoreCase(o2) < 1) {
			return -1;
		}
		else {
			return 0;
		}
	}

	
	
}