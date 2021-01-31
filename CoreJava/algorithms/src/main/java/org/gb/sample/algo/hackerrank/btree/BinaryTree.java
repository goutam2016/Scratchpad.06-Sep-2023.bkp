package org.gb.sample.algo.hackerrank.btree;

public class BinaryTree<T> {

	private Node<T> root;

	public BinaryTree(Node<T> root) {
		this.root = root;
	}

	int getHeight() {
		int leftSubtreeHeight = getHeight(root.getLeftChild());
		int rightSubtreeHeight = getHeight(root.getRightChild());

		int maxHeightOfChildren = Integer.max(leftSubtreeHeight, rightSubtreeHeight);
		return maxHeightOfChildren + 1;
	}

	private int getHeight(Node<T> node) {
		if (node == null) {
			return -1;
		} else {
			BinaryTree<T> subtree = new BinaryTree<>(node);
			return subtree.getHeight();
		}
	}

	public BinaryTree<T> getLeftSubtree() {
		if (root.getLeftChild() == null) {
			return null;
		} else {
			return new BinaryTree<>(root.getLeftChild());
		}
	}

	public BinaryTree<T> getRightSubtree() {
		if (root.getRightChild() == null) {
			return null;
		} else {
			return new BinaryTree<T>(root.getRightChild());
		}
	}

	@Override
	public String toString() {
		return "root: ".concat(String.valueOf(root.getValue()));
	}

	public Node<T> getRoot() {
		return root;
	}
}
