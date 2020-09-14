package assign08;

import java.util.ArrayList;
import java.util.Collection;
import java.util.NoSuchElementException;

/**
 * This class represents a generic BST, or binary search tree. It is comprised
 * of nodes that create a structure that is ordered with smaller values on the
 * left of any given node and larger ones to the right.
 * 
 * @author Brandon Walters and Brandon Ernst
 */
public class BinarySearchTree<Type extends Comparable<? super Type>> implements SortedSet<Type> {

	private BinarySearchNode<Type> root;
	private int numNodes;

	public BinarySearchTree() {

	}

	/**
	 * This add method adds a new node to the binary search tree, adding the new
	 * node into the tree in a way that presorts the tree.
	 */
	@Override
	public boolean add(Type item) {
		if (root == null) {
			root = new BinarySearchNode<Type>(item, null);
			numNodes++;
			return true;
		}

		BinarySearchNode<Type> nodeOn = root;
		while (true) {
			if (nodeOn.getLeftChild() == null && item.compareTo(nodeOn.getData()) < 0) {
				nodeOn.setLeftChild(new BinarySearchNode<Type>(item, nodeOn));
				numNodes++;
				return true;
			}
			if (nodeOn.getRightChild() == null && item.compareTo(nodeOn.getData()) > 0) {
				nodeOn.setRightChild(new BinarySearchNode<Type>(item, nodeOn));
				numNodes++;
				return true;
			}
			if (item.compareTo(nodeOn.getData()) > 0) {
				nodeOn = nodeOn.getRightChild();
			} else if (item.compareTo(nodeOn.getData()) < 0) {
				nodeOn = nodeOn.getLeftChild();
			}
		}
	}

	@Override
	public boolean addAll(Collection<? extends Type> items) {
		for (Type item : items)
			this.add(item);
		return true;
	}

	@Override
	public void clear() {
		root = null;
		numNodes = 0;

	}

	/**
	 * This method is used to check if the BST contains a node with the data that is
	 * specified.
	 */
	@Override
	public boolean contains(Type item) {
		BinarySearchNode<Type> nodeOn = root;
		while (true) {
			if (item.compareTo(nodeOn.getData()) == 0)
				return true;

			if (nodeOn.getLeftChild() == null && item.compareTo(nodeOn.getData()) < 0)
				return false;
			if (nodeOn.getRightChild() == null && item.compareTo(nodeOn.getData()) > 0)
				return false;

			if (item.compareTo(nodeOn.getData()) > 0)
				nodeOn = nodeOn.getRightChild();
			else if (item.compareTo(nodeOn.getData()) < 0)
				nodeOn = nodeOn.getLeftChild();
		}
	}

	@Override
	public boolean containsAll(Collection<? extends Type> items) {
		for (Type item : items) {
			if (!contains(item))
				return false;
		}
		return true;
	}

	/**
	 * This method finds the lowest (left-most) value in the tree.
	 */
	@Override
	public Type first() throws NoSuchElementException {
		if (root == null)
			throw new NoSuchElementException("Binary Search Tree is empty.");

		BinarySearchNode<Type> nodeOn = root;
		while (nodeOn.getLeftChild() != null)
			nodeOn = nodeOn.getLeftChild();
		return nodeOn.getData();
	}

	@Override
	public boolean isEmpty() {
		if (root == null)
			return true;
		return false;
	}

	/**
	 * This method finds the highest (right-most) value in the tree.
	 */
	@Override
	public Type last() throws NoSuchElementException {
		if (root == null)
			throw new NoSuchElementException("Binary Search Tree is empty.");

		BinarySearchNode<Type> nodeOn = root;
		while (nodeOn.getRightChild() != null)
			nodeOn = nodeOn.getRightChild();
		return nodeOn.getData();
	}

	/**
	 * This method removes nodes from the tree based on how many subtrees of nodes
	 * the node has: 0, 1, or 2.
	 */
	@Override
	public boolean remove(Type item) {
		BinarySearchNode<Type> nodeOn = root;
		while (true) {
			if (item.compareTo(nodeOn.getData()) == 0) {

				// If node is a leaf
				if (nodeOn.getLeftChild() == null && nodeOn.getRightChild() == null) {
					if (nodeOn.getParent() == null) {
						nodeOn = null;
						numNodes--;
						return true;
					}
					if (nodeOn.getParent().getLeftChild() != null) {
						if (nodeOn.getParent().getLeftChild().equals(nodeOn)) {
							nodeOn.getParent().setLeftChild(null);
						}
					} else if (nodeOn.getParent().getRightChild() != null) {
						if (nodeOn.getParent().getRightChild().equals(nodeOn)) {
							nodeOn.getParent().setRightChild(null);
						}
					}
					nodeOn = null;
					numNodes--;
					return true;
				}
				// If node has a left subtree only
				else if (nodeOn.getLeftChild() != null && nodeOn.getRightChild() == null) {
					if (nodeOn.getParent() == null) {
						nodeOn.getLeftChild().setParent(null);
						root = nodeOn.getLeftChild();
						nodeOn = null;
						numNodes--;
						return true;
					}
					if (nodeOn.getParent().getLeftChild() != null) {
						if (nodeOn.getParent().getLeftChild().equals(nodeOn)) {
							nodeOn.getParent().setLeftChild(nodeOn.getLeftChild());
						}
					} else if (nodeOn.getParent().getRightChild() != null) {
						if (nodeOn.getParent().getRightChild().equals(nodeOn)) {
							nodeOn.getParent().setRightChild(nodeOn.getLeftChild());
						}
					}
					nodeOn = null;
					numNodes--;
					return true;
				}
				// If node has a right subtree only
				else if (nodeOn.getLeftChild() == null && nodeOn.getRightChild() != null) {
					if (nodeOn.getParent() == null) {
						nodeOn.getRightChild().setParent(null);
						root = nodeOn.getRightChild();
						nodeOn = null;
						numNodes--;
						return true;
					}
					if (nodeOn.getParent().getLeftChild() != null) {
						if (nodeOn.getParent().getLeftChild().equals(nodeOn)) {
							nodeOn.getParent().setLeftChild(nodeOn.getRightChild());
						}
					} else if (nodeOn.getParent().getRightChild() != null) {
						if (nodeOn.getParent().getRightChild().equals(nodeOn)) {
							nodeOn.getParent().setRightChild(nodeOn.getRightChild());
						}
					}
					numNodes--;
					return true;
				}
				// If node has two subtrees
				else {
					BinarySearchNode<Type> nodeToReplaceWith = nodeOn.getRightChild();
					while (nodeToReplaceWith.getLeftChild() != null)
						nodeToReplaceWith = nodeToReplaceWith.getLeftChild();
					nodeOn.setData(nodeToReplaceWith.getData());
					nodeToReplaceWith.getParent().setLeftChild(null);
					nodeToReplaceWith = null;
					numNodes--;
					return true;
				}

			}

			if (nodeOn.getLeftChild() == null && item.compareTo(nodeOn.getData()) < 0)
				return false;
			if (nodeOn.getRightChild() == null && item.compareTo(nodeOn.getData()) > 0)
				return false;

			if (item.compareTo(nodeOn.getData()) > 0)
				nodeOn = nodeOn.getRightChild();
			else if (item.compareTo(nodeOn.getData()) < 0)
				nodeOn = nodeOn.getLeftChild();
		}
	}

	@Override
	public boolean removeAll(Collection<? extends Type> items) {
		for (Type item : items)
			this.remove(item);
		return true;
	}

	@Override
	public int size() {
		return numNodes;
	}

	/**
	 * This method converts the tree to a sorted ArrayList.
	 */
	@Override
	public ArrayList<Type> toArrayList() {
		ArrayList<Type> arrayList = new ArrayList<Type>();
		arrayList = this.toArrayList(root);
		return arrayList;
	}

	/**
	 * This method prints the tree in in-order ordering to help create the ArrayList
	 * returned by toArrayList().
	 */
	private ArrayList<Type> toArrayList(BinarySearchNode<Type> node) {

		ArrayList<Type> arrayList = new ArrayList<Type>();

		if (node == null) {
			return arrayList;
		}

		if (node.getLeftChild() != null) {
			arrayList.addAll(toArrayList(node.getLeftChild()));
		}

		arrayList.add(node.getData());

		if (node.getRightChild() != null) {
			arrayList.addAll(toArrayList(node.getRightChild()));
		}

		return arrayList;
	}

}
