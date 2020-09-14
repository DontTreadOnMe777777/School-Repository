package assign06;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * This class creates and implements a singly-linked list for use in a linked list stack.
 * @author Brandon Walters and Brandon Ernst
 * @param <E>
 */
public class SinglyLinkedList<E> implements List<E> {

	private class Node  {
		 public E element;
		 public Node nextNode;
		 
		 public Node(E element, Node nextNode) {
			 this.element = element;
			 this.nextNode = nextNode;
		 }
	}

	private Node head;
	private int count;
	
	public SinglyLinkedList() {
		head = null;
		count = 0;
	}

	@Override
	public void addFirst(E element) {
		head = new Node(element, head);
		count++;
	}

	@Override
	public void add(int index, E element) throws IndexOutOfBoundsException {
		if (index < 0 || index > count) {
			throw new IndexOutOfBoundsException("Invalid index.");
		}
		Node hold = this.head;
		for (int i = 0; i < index - 1; i++) {
			hold = hold.nextNode;
		}
		hold.nextNode = new Node(element, hold.nextNode);
		count++;
	}

	@Override
	public E getFirst() throws NoSuchElementException {
		if(count == 0)
			throw new NoSuchElementException("The Singly Linked List is Empty,");
		return head.element;
	}

	@Override
	public E get(int index) throws IndexOutOfBoundsException {
		if(index < 0 || index > count)
			throw new IndexOutOfBoundsException("Invalid index");
		Node hold = head;
		for(int i = 0; i < index; i++) 
			hold = hold.nextNode;
		return hold.element;
	}

	@Override
	public E removeFirst() throws NoSuchElementException {
		if(count == 0)
			throw new NoSuchElementException("Singly linked list is empty.");
		Node hold = head;
		head = head.nextNode;
		count--;
		return hold.element;
	}

	@Override
	public E remove(int index) throws IndexOutOfBoundsException {
		if(index < 0 || index > count)
			throw new IndexOutOfBoundsException("Invalid index.");
		if(index == 0) {
			Node prevHead = head;
			head = head.nextNode;
			return prevHead.element;
		}
		Node hold = head;
		for(int i = 0; i < index - 1; i++) {
			hold = hold.nextNode;
		}
		Node removed = hold.nextNode;
		hold.nextNode = removed.nextNode;
		count--;
		return hold.element;
	}

	@Override
	public int indexOf(E element) {
		int index = 0;
		Node hold = head;
		for(int i = 0; i < count; i ++) {
			if(hold.element.equals(element))
				return index;
			hold = hold.nextNode;
			index++;
		}
		return -1;
	}

	@Override
	public int size() {
		return count;
	}

	@Override
	public boolean isEmpty() {
		return count <= 0;
	}

	@Override
	public void clear() {
		head = null;
		count = 0;
	}

	@Override
	public E[] toArray() {
		E[] hold = (E[]) new Object[count];
		Node toAdd = head;
		for(int i = 0; i < count; i ++) {
			hold[i] = toAdd.element;
			toAdd = toAdd.nextNode;
		}
		return hold;
	}

	@Override
	public Iterator<E> iterator() {
		return new linkedListIterator();
	}

	public class linkedListIterator implements Iterator<E> {

		private int nextIndex;
		private Node prevNode;
		private Node nodeAt;
		private boolean okToRemove;

		public linkedListIterator() {
			nextIndex = 0;
			nodeAt = head;
			okToRemove = false;
		}

		@Override
		public boolean hasNext() {
			return nextIndex < count;
		}

		@Override
		public E next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			okToRemove = true;
			prevNode = nodeAt;
			nodeAt = nodeAt.nextNode;
			return prevNode.element;
		}

		public void remove() {
			if (!okToRemove) {
				throw new IllegalStateException();
			}
			nextIndex--;
			count--;
			prevNode.nextNode = nodeAt.nextNode;
		}
	}
	
	
	
}
