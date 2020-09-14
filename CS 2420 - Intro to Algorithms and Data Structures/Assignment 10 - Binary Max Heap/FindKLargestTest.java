package assign10;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FindKLargestTest {
	
	BinaryMaxHeap<Integer> intTester;
	BinaryMaxHeap<String> stringTester;
	List<Integer> hold = new ArrayList<Integer>();
	List<Integer> hold3 = new ArrayList<Integer>();
	List<String> hold2 = new ArrayList<String>();
	List<String> hold4 = new ArrayList<String>();
	
	@BeforeEach
	void setUp() {
		hold.add(160);
		hold.add(150);
		hold.add(140);
		hold.add(130);
		hold.add(120);
		hold.add(110);
		hold.add(100);
		hold.add(90);
		hold.add(80);
		hold.add(70);
		hold.add(60);
		hold.add(50);
		hold.add(40);
		hold.add(30);
		hold.add(20);
		hold.add(10);
		intTester = new BinaryMaxHeap<Integer>(hold);
		
		hold2.add("iii");
		hold2.add("hhh");
		hold2.add("ggg");
		hold2.add("fff");
		hold2.add("eee");
		hold2.add("ddc");
		hold2.add("ccc");
		hold2.add("bbb");
		hold2.add("aaa");
		stringTester = new BinaryMaxHeap<String>(hold2);
		
	}

	@Test
	void testFindKLargestHeap() {
		ArrayList<Integer> kLargestInt = new ArrayList<Integer>();
		kLargestInt.add(160);
		kLargestInt.add(150);
		kLargestInt.add(140);
		kLargestInt.add(130);
		kLargestInt.add(120);
		hold3 = FindKLargest.findKLargestHeap(hold, 5);
		for (int i = 0; i < 5; i++) {
			if (!hold3.get(i).equals(kLargestInt.get(i))) {
				assertFalse(true);
			}
		}
		
		ArrayList<String> kLargestString = new ArrayList<String>();
		kLargestString.add("iii");
		kLargestString.add("hhh");
		kLargestString.add("ggg");
		hold4 = FindKLargest.findKLargestHeap(hold2, 3);
		for (int i = 0; i < 3; i++) {
			if (!hold3.get(i).equals(kLargestInt.get(i))) {
				assertFalse(true);
			}
		}
		
	}
	
	@Test
	void testFindKLargestSort() {
		ArrayList<Integer> kLargestInt = new ArrayList<Integer>();
		kLargestInt.add(160);
		kLargestInt.add(150);
		kLargestInt.add(140);
		kLargestInt.add(130);
		kLargestInt.add(120);
		hold3 = FindKLargest.findKLargestSort(hold, 5);
		for (int i = 0; i < 5; i++) {
			if (!hold3.get(i).equals(kLargestInt.get(i))) {
				assertFalse(true);
			}
		}
		
		ArrayList<String> kLargestString = new ArrayList<String>();
		kLargestString.add("iii");
		kLargestString.add("hhh");
		kLargestString.add("ggg");
		hold4 = FindKLargest.findKLargestSort(hold2, 3);
		for (int i = 0; i < 3; i++) {
			if (!hold3.get(i).equals(kLargestInt.get(i))) {
				assertFalse(true);
			}
		}
	}

}
