package assign10;


import java.util.NoSuchElementException;

/**
 * This interface represents the priority queue abstract data type,
 * defining the operations and running times expected of any data 
 * structure used to implement a priority queue.  
 * 
 * NOTE: The item with the highest priority is the "maximum" item.
 * 
 * @author Erin Parker
 * @version April 3, 2019
 *
 * @param <E>
 */
public interface PriorityQueue<E> {
	
	/**
	 * Adds the given item to this priority queue.
	 * O(1) in the average case, O(log N) in the worst case
	 * 
	 * @param item
	 */
	public void add(E item);
	
	/**
	 * Returns, but does not remove, the maximum item this priority queue.
	 * O(1)
	 * 
	 * @return the maximum item
	 * @throws NoSuchElementException if this priority queue is empty
	 */
	public E peek() throws NoSuchElementException;
	
	/**
	 * Returns and removes the maximum item this priority queue.
	 * O(log N)
	 * 
	 * @return the maximum item
	 * @throws NoSuchElementException if this priority queue is empty
	 */
	public E extractMax() throws NoSuchElementException;

	/**
	 * Returns the number of items in this priority queue.
	 * O(1)
	 */
	public int size();
	
	/**
	 * Returns true if this priority queue is empty, false otherwise.
	 * O(1)
	 */
	public boolean isEmpty();
	
	/**
	 * Empties this priority queue of items.
	 * O(1)
	 */
	public void clear();
	
	/** 
	 * Creates and returns an array of the items in this priority queue,
	 * in the same order they appear in the backing array.
	 * O(N)
	 * 
	 * (NOTE: This method is needed for grading purposes. The root item 
	 * must be stored at index 0 in the returned array, regardless of 
	 * whether it is in stored there in the backing array.) 
	 */
	public Object[] toArray();
}