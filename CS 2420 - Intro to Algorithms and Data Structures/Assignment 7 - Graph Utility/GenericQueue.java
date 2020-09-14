package assign07;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

public class GenericQueue<T> implements Queue<T>, Iterable<T> {
	
	private T[] queueArray;
	
	private int size;
	
	private int front = 0;
	
	private int rear = -1;

	public GenericQueue() {
		queueArray = (T[]) new Object[1000];
		size = 0;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public boolean isEmpty() {
		if (size == 0) {
			return true;
		}
		return false;
	}

	@Override
	public boolean contains(Object o) {
		for (int i = 0; i < size; i++) {
			if (queueArray[i].equals(o)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Iterator<T> iterator() {
		return new queueIterator();
	}

	@Override
	public T[] toArray() {
		T[] array = (T[]) new Object[size];
		for (int i = 0; i < size; i++) {
			array[i] = queueArray[i];
		}
		return array;
	}

	@Override
	public T[] toArray(Object[] a) {
		T[] array = (T[]) new Object[size];
		for (int i = 0; i < size; i++) {
			array[i] = (T) a[i];
		}
		return array;
	}

	@Override
	public boolean remove(Object o) {
		for (int i = 0; i < size; i++) {
			if (queueArray[i].equals((T) o)) {
				queueArray[i] = queueArray[i + 1];
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean containsAll(Collection c) {
		return true;
	}

	@Override
	public boolean addAll(Collection c) {
		T[] arrayToCheck = (T[]) c.toArray();
		for (int i = 0; i < arrayToCheck.length; i++) {
			add(arrayToCheck[i]);
		}
		return true;
	}

	@Override
	public boolean removeAll(Collection c) {
		T[] arrayToCheck = (T[]) c.toArray();
		for (int i = 0; i < arrayToCheck.length; i++) {
			remove(arrayToCheck[i]);
		}
		return true;
	}

	@Override
	public boolean retainAll(Collection c) {
		return true;
	}

	@Override
	public void clear() {
		size = 0;
	}

	@Override
	public boolean add(Object e) {
		if (size == queueArray.length) {
			queueArray = Arrays.copyOf(queueArray, queueArray.length * 2);
		}
		rear++;
		queueArray[rear] = (T) e;
		size++;
		return true;
	}

	@Override
	public boolean offer(Object e) {
		if (size == queueArray.length - 1) {
			queueArray = Arrays.copyOf(queueArray, queueArray.length * 2);
		}
		rear++;
		queueArray[rear] = (T) e;
		size++;
		return true;
	}

	@Override
	public T remove() {
		if (size == 0) {
			throw new NoSuchElementException("Queue is empty.");
		}
		size--;
		T element = queueArray[front];
		front++;
		return element;	
	}

	@Override
	public T poll() {
		if (size == 0) {
			return null;
		}
		size--;
		return queueArray[0];
	}

	@Override
	public T element() {
		if (size == 0) {
			throw new NoSuchElementException("Queue is empty.");
		}
		return queueArray[size - 1];
	}

	@Override
	public T peek() {
		if (size == 0) {
			return null;
		}
		return queueArray[size - 1];
	}
	
	public class queueIterator implements Iterator<T> {

		private int index = 0;

		private boolean hasNextBeenCalled = false;

		public queueIterator() {
		}

		@Override
		public boolean hasNext() {
			return index < size;
		}

		@Override
		public T next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			hasNextBeenCalled = true;
			return (T) queueArray[index++];
		}

		public void remove() {
			if (!hasNextBeenCalled) {
				throw new IllegalStateException();
			}
			index--;
			size--;
			for (int i = index; i < size; i++) {
				queueArray[i] = queueArray[i + 1];
			}
		}
	}

}
