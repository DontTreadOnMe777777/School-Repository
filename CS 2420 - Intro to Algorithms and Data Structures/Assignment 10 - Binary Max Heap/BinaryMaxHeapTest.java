package assign10;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BinaryMaxHeapTest {
	
	BinaryMaxHeap<Integer> intTester;
	BinaryMaxHeap<String> stringTester;
	List<Integer> hold = new ArrayList<Integer>();
	List<String> hold2 = new ArrayList<String>();
	
	@BeforeEach
	void setUp() {
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
		hold2.add("ddd");
		hold2.add("ccc");
		hold2.add("bbb");
		hold2.add("aaa");
		stringTester = new BinaryMaxHeap<String>(hold2);
		
	}

	@Test
	void testAddandToArray() {
		for(int i = 0; i < hold.size(); i++) {
			assertEquals((int)hold.get(i), (int)intTester.toArray()[i]);
		}
		
		for(int i = 0; i < hold2.size(); i++) {
			if(!hold2.get(i).equals((String)stringTester.toArray()[i]))
				assertFalse(true);
		}
		
		intTester.add(0);
		hold.add(0);
		
		for(int i = 0; i < hold.size(); i++) {
			assertEquals((int)hold.get(i), (int)intTester.toArray()[i]);
		}
		
		intTester.add(65);
		hold.add(40);
		hold.add(3, 60);
		hold.remove(4);
		hold.add(1, 65);
		hold.remove(2);
		
		for(int i = 0; i < hold.size(); i++) {
			assertEquals((int)hold.get(i), (int)intTester.toArray()[i]);
		}
		
		stringTester.add("eeh");
		hold2.add("eee");
		hold2.add(4, "eeh");
		hold2.remove(5);
		
		for(int i = 0; i < hold2.size(); i++) {
			if(!hold2.get(i).equals((String)stringTester.toArray()[i]))
				assertFalse(true);
		}
		


	}
	
	@Test
	void testClearAndIsEmpty () {
		assertFalse(intTester.isEmpty());
		assertFalse(stringTester.isEmpty());
		intTester.clear();
		stringTester.clear();	
		assertFalse(!intTester.isEmpty());
		assertFalse(!stringTester.isEmpty());
	}
	
	@Test
	void testSize() {
		assertEquals(7, intTester.size());
		intTester.add(80);
		assertEquals(8, intTester.size());
		intTester.extractMax();
		assertEquals(7, intTester.size());
		intTester.extractMax();
		intTester.extractMax();
		assertEquals(5, intTester.size());
	}
	
	@Test
	void testExtractMaxAndPeek() {
		assertEquals(70, (int) intTester.extractMax());
		assertEquals(60, (int) intTester.extractMax());
		assertEquals(50, (int) intTester.peek());
		assertEquals(50, (int) intTester.peek());
		
		assertFalse(!"iii".equals(stringTester.extractMax()));
		assertFalse(!"hhh".equals(stringTester.extractMax()));
		assertFalse(!"ggg".equals(stringTester.peek()));
		assertFalse(!"ggg".equals(stringTester.peek()));
	}
	
	@Test
	void printArray() {
		System.out.println(intTester.toArray());
	}

}
