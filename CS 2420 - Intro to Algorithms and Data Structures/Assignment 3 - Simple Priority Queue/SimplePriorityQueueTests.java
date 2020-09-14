package assign03;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Comparator;
import javax.swing.JFrame;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

class SimplePriorityQueueTests {

	@Test
	void testInsertToEmptyQueue() {
		SimplePriorityQueue<String> Q = new SimplePriorityQueue<>();
		Q.insert("hello world");
		assertEquals(1, Q.size());
		assertEquals("hello world", Q.findMin());
		Q.clear();
		assertEquals(0, Q.size());
	}
	
	@Test
	void testInsertDescendingOrderQueue() {
		SimplePriorityQueue<String> Q = new SimplePriorityQueue<>();
		Q.insert("z");
		Q.insert("y");
		Q.insert("x");
		assertEquals(3, Q.size());
		assertEquals("x", Q.deleteMin());
		assertEquals("y", Q.deleteMin());
		assertEquals("z", Q.deleteMin());
	}
	@Test
	void testInsertSameInQueue() {
		SimplePriorityQueue<String> Q = new SimplePriorityQueue<>();
		Q.insert("z");
		Q.insert("z");
		Q.insert("z");
		assertEquals(3, Q.size());
	}
	@Test
	void testInsertSameToMiddleQueue() {
		SimplePriorityQueue<String> Q = new SimplePriorityQueue<>();
		Q.insert("z");
		Q.insert("y");
		Q.insert("w");
		Q.insert("y");
		assertEquals(4, Q.size());
		assertEquals("w", Q.deleteMin());
		assertEquals("y", Q.deleteMin());
		assertEquals("y", Q.deleteMin());
		assertEquals("z", Q.deleteMin());
	}
	@Test
	void testInsertToMiddleQueue() {
		SimplePriorityQueue<String> Q = new SimplePriorityQueue<>();
		Q.insert("z");
		Q.insert("y");
		Q.insert("w");
		Q.insert("x");
		assertEquals(4, Q.size());
		assertEquals("w", Q.deleteMin());
		assertEquals("x", Q.deleteMin());
		assertEquals("y", Q.deleteMin());
		assertEquals("z", Q.deleteMin());
	}
	
	@Test
	void testInsertAscendingOrderQueue() {
		SimplePriorityQueue<String> Q = new SimplePriorityQueue<>();
		Q.insert("a");
		Q.insert("b");
		Q.insert("c");
		assertEquals(3, Q.size());
		assertEquals("a", Q.deleteMin());
		assertEquals("b", Q.deleteMin());
		assertEquals("c", Q.deleteMin());
	}
	
	//aabdfkmnopwz
	@Test
	void testInsertBigQueue() {
		SimplePriorityQueue<String> Q = new SimplePriorityQueue<>();
		Q.insert("z");
		Q.insert("b");
		Q.insert("f");
		Q.insert("a");
		Q.insert("k");
		Q.insert("a");
		Q.insert("m");
		Q.insert("n");
		Q.insert("p");
		Q.insert("d");
		Q.insert("o");
		Q.insert("w");
		assertEquals(12, Q.size());
		assertEquals("a", Q.deleteMin());
		assertEquals("a", Q.deleteMin());
		assertEquals("b", Q.deleteMin());
		assertEquals("d", Q.deleteMin());
		assertEquals("f", Q.deleteMin());
		assertEquals("k", Q.deleteMin());
		assertEquals("m", Q.deleteMin());
		assertEquals("n", Q.deleteMin());
		assertEquals("o", Q.deleteMin());
		assertEquals("p", Q.deleteMin());
		assertEquals("w", Q.deleteMin());
		assertEquals("z", Q.deleteMin());
	}
	
	@Test
	void testInsertAll() {
		ArrayList<String> a = new ArrayList<String>();
		for(int i = 122; i >= 97; i--) {
			for(int j = 97; j <= 122; j++) {
				a.add("" + (char) i);
				a.add("" + (char) j);
			}
		}
		SimplePriorityQueue<String> Q = new SimplePriorityQueue<>();
		Q.insertAll(a);
		assertEquals(1352, Q.size());
		for (int j = 97; j <= 122; j++) {
		for (int i = 0; i < 52; i++) {
				assertEquals(("" + (char) j), Q.deleteMin());
			}
		}
	}
	
	@Test
	void testInsertComparator() {
		SimplePriorityQueue<String> Q = new SimplePriorityQueue<String>((s1, s2) -> s1.length() - s2.length());
		Q.insert("");
		Q.insert("Hello world");
		Q.insert("Big");
		Q.insert("Nah m8");
		assertEquals(4, Q.size());
		assertEquals("", Q.deleteMin());
		assertEquals("Big", Q.deleteMin());
		assertEquals("Nah m8", Q.deleteMin());
		assertEquals("Hello world", Q.deleteMin());
	}
	
	@Test
	void testInsertComparator2() {
		SimplePriorityQueue<Boolean> Q = new SimplePriorityQueue<Boolean>(new compareBoolean());
		Q.insert(false);
		Q.insert(true);
		Q.insert(false);
		Q.insert(true);
		assertEquals(4, Q.size());
		assertEquals(false, Q.deleteMin());
		assertEquals(false, Q.deleteMin());
		assertEquals(true, Q.deleteMin());
		assertEquals(true, Q.deleteMin());
	}
	
	@Test
	void testNotComparable() {
		SimplePriorityQueue<JFrame> Q = new SimplePriorityQueue<JFrame>();
		Q.insert(new JFrame());
		assertThrows(ClassCastException.class, () -> { Q.insert(new JFrame());});
	}
}
class compareBoolean implements Comparator<Boolean> {

	@Override
	public int compare(Boolean o1, Boolean o2) {
		 if(o1 && !o2) {
			return 1;
		}
		
		else if(!o1 && o2) {
			return -1;
		}
		else {
			return 0;
		}
	}

	
	
}

