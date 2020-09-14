package assign03;

import java.util.Arrays;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Comparator;

public class SimplePriorityQueue<E> implements PriorityQueue<E> {

	/** Represents the queue itself */
	private E[] queueArray;

	/** Represents the size of the queue */
	private int size;

	/** Saves whether comparable is to be used or a comparator will be used */
	private boolean isComparable;

	/** Saves the potential comparator */
	private Comparator<? super E> comparator;

	/**
	 * Construct a priority queue that can accommodate any type comparable item. A
	 * priority queue is a container in which access is limited to the "minimum"
	 * item. The queue is kept sorted where the minimum item is determined by the
	 * item's compareTo method.
	 */
	@SuppressWarnings("unchecked")
	public SimplePriorityQueue() {
		queueArray = (E[]) new Object[16];
		size = 0;
		isComparable = true;
	}

	/**
	 * Construct a priority queue that can accommodate any type of item (e.g.,
	 * strings, integers, library books, etc.). A priority queue is a container in
	 * which access is limited to the "minimum" item. The queue is kept sorted where
	 * the minimum item is determined by the user supplied comparator.
	 */
	@SuppressWarnings("unchecked")
	public SimplePriorityQueue(Comparator<? super E> comparator) {
		queueArray = (E[]) new Object[16];
		size = 0;
		isComparable = false;
		this.comparator = comparator;
	}

	/**
	 * Returns the last item in the queue.
	 */
	@Override
	public E findMin() throws NoSuchElementException {
		if (size == 0) {
			throw new NoSuchElementException("Queue is empty.");
		}
		return queueArray[size - 1];
	}

	/**
	 * Returns the last item in the queue and deletes it from the queue.
	 */
	@Override
	public E deleteMin() throws NoSuchElementException {
		if (size == 0) {
			throw new NoSuchElementException("Queue is empty.");
		}
		size = size - 1;
		return queueArray[size];
	}

	/**
	 * Inserts the item into the queue at the appropriate sorted position.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void insert(E item) {
		if (size == 0) {
			queueArray[0] = item;
			size++;
			return;
		}
		if (isComparable) {
			// Checks if smaller than last item in queue
			if (((Comparable<? super E>) item).compareTo(queueArray[size - 1]) <= 0) {
				size++;
				// Add it to the end of the queue
				if (size < queueArray.length) {
					queueArray[size - 1] = item;
				}

				else {
					queueArray = Arrays.copyOf(queueArray, queueArray.length * 2);
					queueArray[size - 1] = item;
				}
				return;
			}
			// Checks if larger than first item in queue
			if (((Comparable<? super E>) item).compareTo(queueArray[0]) > 0) {
				size++;
				// Add it to the beginning of the queue
				if (size < queueArray.length) {
					shiftArray(0);
					queueArray[0] = item;
				}

				else {
					queueArray = Arrays.copyOf(queueArray, queueArray.length * 2);
					shiftArray(0);
					queueArray[0] = item;
				}
				return;
			}

			int lowerBound = 0;
			int upperBound = size - 1;
			while (lowerBound < upperBound) {
				if (((Comparable<? super E>) item).compareTo(queueArray[(upperBound + lowerBound) / 2]) == 0) {
					shiftArray((upperBound + lowerBound) / 2);
					queueArray[(upperBound + lowerBound) / 2] = item;
					size++;
					return;
				} else if (((Comparable<? super E>) item).compareTo(queueArray[(upperBound + lowerBound) / 2]) < 0) {
					lowerBound = (upperBound + lowerBound) / 2 + 1;
				} else {
					upperBound = (upperBound + lowerBound) / 2 - 1;
				}
			}
			if (((Comparable<? super E>) item).compareTo(queueArray[upperBound]) <= 0) {
				shiftArray(upperBound + 1);
				queueArray[upperBound + 1] = item;
				size++;
			} else {
				shiftArray(upperBound);
				queueArray[upperBound] = item;
				size++;
			}

		}

		else {
			// Checks if smaller than last item in queue
			if (comparator.compare(item, queueArray[size - 1]) <= 0) {
				size++;
				// Add it to the end of the queue
				if (size < queueArray.length) {
					queueArray[size - 1] = item;
				}

				else {
					queueArray = Arrays.copyOf(queueArray, queueArray.length * 2);
					queueArray[size - 1] = item;
				}
				return;
			}
			// Checks if larger than first item in queue
			if (comparator.compare(item, queueArray[0]) > 0) {
				size++;
				// Add it to the beginning of the queue
				if (size < queueArray.length) {
					shiftArray(0);
					queueArray[0] = item;
				}

				else {
					queueArray = Arrays.copyOf(queueArray, queueArray.length * 2);
					shiftArray(0);
					queueArray[0] = item;
				}
				return;
			}

			int lowerBound = 0;
			int upperBound = size - 1;
			while (lowerBound < upperBound) {
				if (comparator.compare(item, queueArray[(upperBound + lowerBound) / 2]) == 0) {
					shiftArray((upperBound + lowerBound) / 2);
					queueArray[(upperBound + lowerBound) / 2] = item;
					size++;
					return;
				} else if (comparator.compare(item, queueArray[(upperBound + lowerBound) / 2]) < 0) {
					lowerBound = (upperBound + lowerBound) / 2 + 1;
				} else {
					upperBound = (upperBound + lowerBound) / 2 - 1;
				}
			}
			if (comparator.compare(item, queueArray[upperBound]) <= 0) {
				shiftArray(upperBound + 1);
				queueArray[upperBound + 1] = item;
				size++;
			} else {
				shiftArray(upperBound);
				queueArray[upperBound] = item;
				size++;
			}

		}

	}

	/**
	 * Shifts a portion of the array over from the given index, which creates a gap
	 * in the array at the given index
	 */
	private void shiftArray(int index) {
		for (int i = size - 1; i >= index; i--) {
			if (i < queueArray.length - 1)
				queueArray[i + 1] = queueArray[i];
			else {
				queueArray = Arrays.copyOf(queueArray, queueArray.length * 2);
				queueArray[i + 1] = queueArray[i];
			}
		}
	}

	/**
	 * Inserts the collection into the queue ordering along the way
	 */
	@Override
	public void insertAll(Collection<? extends E> coll) {
		for (E item : coll)
			insert(item);
	}

	/** Returns the size of the queue */
	@Override
	public int size() {
		return size;
	}

	/** Returns if the queue is empty */
	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	/** Clears the queue */
	@Override
	public void clear() {
		size = 0;
	}

}
