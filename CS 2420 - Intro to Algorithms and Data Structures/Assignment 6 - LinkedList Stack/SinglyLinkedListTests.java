package assign06;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.*;

import java.util.NoSuchElementException;

import assign06.SinglyLinkedList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * A series of tests for the SinglyLinkedList class.
 * @author Brandon Walters and Brandon Ernst
 *
 */
class SinglyLinkedListTests {
	
	private SinglyLinkedList<Integer> list, list2;
	private SinglyLinkedList<String> list3;
	
	@BeforeEach
	void setUp() throws Exception{
		list2 = new SinglyLinkedList<Integer>();
		list2.addFirst(3);
		list2.addFirst(6);
		list2.addFirst(9);
		list2.addFirst(12);
		list2.addFirst(15);
		list2.addFirst(18);
		list2.addFirst(21);
		
		list3 = new SinglyLinkedList<String>();
		list3.addFirst("hi");
		list3.addFirst("this");
		list3.addFirst("is");
		list3.addFirst("a");
		list3.addFirst("big");
		list3.addFirst("test");
		list3.addFirst("using");
		list3.addFirst("strings");
	}
	
	@Test
	void testThrows()  {
		list = new SinglyLinkedList<Integer>();
		
		assertThrows(IndexOutOfBoundsException.class, ()->{list.get(5);});
		assertThrows(IndexOutOfBoundsException.class, ()->{list.get(-3);});
		
		assertThrows(IndexOutOfBoundsException.class, ()->{list.remove(-3);});
		assertThrows(IndexOutOfBoundsException.class, ()->{list.remove(5);});
		
		assertThrows(NoSuchElementException.class, ()->{list.removeFirst();});
		assertThrows(NoSuchElementException.class, ()->{list.getFirst();});
		
		assertThrows(IndexOutOfBoundsException.class, ()->{list.add(-3, 19);});
		assertThrows(IndexOutOfBoundsException.class, ()->{list.add(5, 19);});
	}
	
	@Test
	void testAddFirst() {		
		assertEquals(18, (int)list2.get(1));
		assertEquals(12, (int)list2.get(3));
		assertEquals(6, (int)list2.get(5));
		
		assertEquals("using", list3.get(1));
		assertEquals("big", list3.get(3));
		assertEquals("is", list3.get(5));
		assertEquals("hi", list3.get(7));
	}
	
	@Test
	void testAdd() {
		list2.add(4, 56);
		list2.add(6, 50);
		list2.add(7, 40);
		assertEquals(56, (int)list2.get(4));
		assertEquals(50, (int)list2.get(6));
		assertEquals(40, (int)list2.get(7));
		list2.add(4, 60);
		assertEquals(60, (int)list2.get(4));
		assertEquals(56, (int)list2.get(5));
		
		list3.add(2, "heck");
		list3.add(4, "hell");
		list3.add(6, "ham");
		assertEquals("heck", list3.get(2));
		assertEquals("hell", list3.get(4));
		assertEquals("ham", list3.get(6));
	}
	
	@Test
	void testClear() {
		list2.clear();
		list3.clear();
		
		assertThrows(NoSuchElementException.class, ()->{list2.getFirst();});
		assertEquals(0, list2.size());
		assertThrows(NoSuchElementException.class, ()->{list3.getFirst();});
		assertEquals(0, list3.size());
	}
	
	@Test
	void testRemoveFirst() {
		list2.removeFirst();
		assertEquals(18, (int)list2.get(0));
		list2.removeFirst();
		assertEquals(15, (int)list2.get(0));
		list2.removeFirst();
		assertEquals(12, (int)list2.get(0));
		
		list3.removeFirst();
		assertEquals("using", (String)list3.get(0));
		list3.removeFirst();
		assertEquals("test", (String)list3.get(0));
		list3.removeFirst();
		assertEquals("big", (String)list3.get(0));
	}
	
	@Test
	void testRemove() {
		assertEquals(21, (int)list2.remove(0));
		list2.remove(1);
		assertEquals(18, (int)list2.get(0));
		assertEquals(12, (int)list2.get(1));
		
		list3.remove(0);
		list3.remove(1);
		assertEquals("using", (String)list3.get(0));
		assertEquals("big", (String)list3.get(1));
	}
	
	@Test
	void testToArray() {
		Integer[] finish = new Integer[] {21, 18, 15, 12, 9, 6, 3};
		assertArrayEquals(finish, list2.toArray());
		
		String[] finish2 = new String[] {"strings", "using", "test", "big", "a", "is", "this", "hi"};
		assertArrayEquals(finish2, list3.toArray());

	}

}
