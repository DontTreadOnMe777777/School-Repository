package assign08;

/**
 * This BinarySearchNode class represents the object nodes which make up
 * a BST. They consist of some form of data and references to the node's
 * parent node and children nodes, if they exist.
 *         
 * @author Brandon Walters and Brandon Ernst   
 */

public class BinarySearchNode<Type> {

	private Type data;

	private BinarySearchNode<Type> leftChild;

	private BinarySearchNode<Type> rightChild;

	private BinarySearchNode<Type> parent;

	public BinarySearchNode(Type item, BinarySearchNode<Type> parentNode) {
		setData(item);
		this.parent = parentNode;
	}

	public BinarySearchNode<Type> getRightChild() {
		return rightChild;
	}

	public void setRightChild(BinarySearchNode<Type> rightChild) {
		this.rightChild = rightChild;
	}

	public BinarySearchNode<Type> getLeftChild() {
		return leftChild;
	}

	public void setLeftChild(BinarySearchNode<Type> leftChild) {
		this.leftChild = leftChild;
	}

	public Type getData() {
		return data;
	}

	public void setData(Type data) {
		this.data = data;
	}

	public BinarySearchNode<Type> getParent() {
		return parent;
	}

	public void setParent(BinarySearchNode<Type> parent) {
		this.parent = parent;
	}

}
