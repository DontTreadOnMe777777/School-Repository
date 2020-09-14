package assign06;

import java.util.NoSuchElementException;

/**
 * A stack backed by an instance of the SinglyLinkedList class.
 * @author Brandon Walters and Brandon Ernst
 * @param <E>
 */
public class LinkedListStack<E> implements Stack<E> {
	
	private SinglyLinkedList<E> list;

	public LinkedListStack() {
		list = new SinglyLinkedList<E>();
	}

	@Override
	public void clear() {
		list.clear();
	}

	@Override
	public boolean isEmpty() {
		return list.isEmpty();
	}

	@Override
	public E peek() throws NoSuchElementException {
		return list.getFirst();
	}

	@Override
	public E pop() throws NoSuchElementException {
		return list.removeFirst();
	}

	@Override
	public void push(E element) {
		list.addFirst(element);
	}

	@Override
	public int size() {
		return list.size();
	}

}
