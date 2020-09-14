package assign05;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

class ArrayListSorterTests {

	@Test
	void testQuickSort() {
		ArrayList<Integer> list1 = new ArrayList<Integer>();
		list1 = ArrayListSorter.generatePermuted(100);
		ArrayListSorter.quickSort(list1);
		for (int i = 0; i < list1.size() - 1; i++) {
			assertTrue(list1.get(i) <= list1.get(i + 1));
		}
		
	}
	
	@Test
	void testMergeSort() {
		ArrayList<Integer> list1 = new ArrayList<Integer>();
		list1 = ArrayListSorter.generatePermuted(100);
		ArrayListSorter.mergeSort(list1);
		for (int i = 0; i < list1.size() - 1; i++) {
			assertTrue(list1.get(i) <= list1.get(i + 1));
		}
		
	}
	
	@Test
	void testGenerateAscending() {
		ArrayList<Integer> list1 = new ArrayList<Integer>();
		list1 = ArrayListSorter.generateAscending(100);
		for (int i = 0; i < list1.size() - 1; i++) {
			assertTrue(list1.get(i) < list1.get(i + 1));
		}
		
	}
	
	@Test
	void testGeneratDescending() {
		ArrayList<Integer> list1 = new ArrayList<Integer>();
		list1 = ArrayListSorter.generateDescending(100);
		for (int i = 0; i < list1.size() - 1; i++) {
			assertTrue(list1.get(i) > list1.get(i + 1));
		}
		
	}


}
