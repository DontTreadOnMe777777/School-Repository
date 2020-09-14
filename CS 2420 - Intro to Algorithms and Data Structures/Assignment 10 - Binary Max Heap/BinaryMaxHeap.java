package assign10;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

public class BinaryMaxHeap<E> implements PriorityQueue<E> {

	private int count = 0;
	
	private int arraySize = 16;
	
	@SuppressWarnings("unchecked")
	private E[] backingArray = (E[]) new Object[arraySize];

	private Comparator<? super E> comp = null;

	public BinaryMaxHeap() {
		// TODO Auto-generated constructor stub
	}

	public BinaryMaxHeap(Comparator<? super E> comparator) {
		comp = comparator;
	}

	public BinaryMaxHeap(List<? extends E> list) {
		buildHeap(list);
	}

	public BinaryMaxHeap(List<? extends E> list, Comparator<? super E> comparator) {
		comp = comparator;
		buildHeap(list);
	}

	@Override
	public void add(E item) {
		if(count + 1 >= arraySize) {
			expandArray();
		}
		if(count == 0) {
			backingArray[1] = item;
			count++;
		}
		else {
			backingArray[count + 1] = item;
			percolateUp(count + 1);
			count++;
		}
	}

	@Override
	public E peek() throws NoSuchElementException {
		if (this.isEmpty()) {
			throw new NoSuchElementException("The heap is empty.");
		}
		return backingArray[1];
	}

	@Override
	public E extractMax() throws NoSuchElementException {
		if (this.isEmpty()) {
			throw new NoSuchElementException("The heap is empty.");
		}
		E data = backingArray[1];
		backingArray[1] = null;
		percolateDown(1);
		count--;
		return data;
	}

	@Override
	public int size() {
		return count;
	}

	@Override
	public boolean isEmpty() {
		return count == 0;
	}

	@Override
	public void clear() {
		backingArray = (E[]) new Object[arraySize];
		count = 0;
	}

	@Override
	public Object[] toArray() {
		@SuppressWarnings("unchecked")
		E[] tempArray = (E[]) new Object[count];
		for (int i = 0; i < count + 1; i++) {
			try {
				tempArray[i] = backingArray[i + 1];
			}
			catch(IndexOutOfBoundsException e) {
			}
		}

		return tempArray;
	}

	private void buildHeap(List<? extends E> list) {
		backingArray = (E[]) shiftList(list);
		arraySize = list.size() + 1;
		count = list.size();
		for (int i = 0; i > list.size(); i++) {
			percolateDown(i);
		}
		//for(E item: list)
			//this.add(item);
	}

	private void percolateUp(int indexToCheck) {
		int index = indexToCheck;
		// For left child
		if (index != 1 && index % 2 == 0 && innerCompare(backingArray[index], backingArray[index / 2]) > 0) {
			E temp = backingArray[index / 2];
			backingArray[index / 2] = backingArray[index];
			backingArray[index] = temp;
			percolateUp(index / 2);
		}

		// For right child
		else if (index != 1 && index % 2 == 1 && innerCompare(backingArray[index], backingArray[index / 2]) > 0) {
			E temp = backingArray[index / 2];
			backingArray[index / 2] = backingArray[index];
			backingArray[index] = temp;
			percolateUp(index / 2);
		}
	}

	private void percolateDown(int indexOfHole) {
		int indexHole = indexOfHole;
		if (indexHole * 2 + 1 >= arraySize) {
			expandArray();
		}
		// If both children exist
		if (backingArray[indexHole * 2] != null && backingArray[indexHole * 2 + 1] != null) {
			// Left child is bigger
			if (innerCompare(backingArray[indexHole * 2], backingArray[indexHole * 2 + 1]) == 1) {
				E temp = backingArray[indexHole * 2];
				backingArray[indexHole * 2] = backingArray[indexHole];
				backingArray[indexHole] = temp;
				percolateDown(indexHole * 2);
			}
			// Right is bigger or equal
			else {
				E temp = backingArray[indexHole * 2 + 1];
				backingArray[indexHole * 2 + 1] = backingArray[indexHole];
				backingArray[indexHole] = temp;
				percolateDown(indexHole * 2 + 1);
			}
		}
		
		// Left child only
		else if (backingArray[indexHole * 2] != null && backingArray[indexHole * 2 + 1] == null) {
			E temp = backingArray[indexHole * 2];
			backingArray[indexHole * 2] = backingArray[indexHole];
			backingArray[indexHole] = temp;
		}
		//right child only
		else if (backingArray[indexHole * 2] == null  && backingArray[indexHole * 2 + 1] != null) {
			E temp = backingArray[indexHole * 2 + 1];
			backingArray[indexHole * 2 + 1] = backingArray[indexHole];
			backingArray[indexHole] = temp;
		}
	}

	private int innerCompare(E element1, E element2) {
		if (comp == null) {
			return ((Comparable<? super E>) element1).compareTo(element2);
		}

		else {
			return comp.compare(element1, element2);
		}
	}
	
	private Object[] shiftList (List<? extends E> listToFix) {
		Object[] arrayOfList = listToFix.toArray();
		Object[] tempArray = new Object[arrayOfList.length + 1];
		for (int i = tempArray.length - 1; i > 0; i--) {
			tempArray[i] = arrayOfList[i - 1];
		}
		return tempArray;
	}
	
	private Object[] expandArray () {
		arraySize *= 2;
		E[] tempArray = backingArray.clone();
		backingArray = (E[]) new Object[arraySize];
		for(int i = 1; i < count + 1; i++) {
			backingArray[i] = tempArray[i];
		}
		return backingArray;
	}

}
