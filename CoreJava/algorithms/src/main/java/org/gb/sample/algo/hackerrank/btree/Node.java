package org.gb.sample.algo.hackerrank.btree;

public class Node<T> {

	private T value;
	private Node<T> leftChild;
	private Node<T> rightChild;

	public Node(T value, Node<T> leftChild, Node<T> rightChild) {
		super();
		this.value = value;
		this.leftChild = leftChild;
		this.rightChild = rightChild;
	}

	public T getValue() {
		return value;
	}
	Node<T> getLeftChild() {
		return leftChild;
	}
	Node<T> getRightChild() {
		return rightChild;
	}
	
	@Override
	public String toString() {
		return String.valueOf(value);
	}
}
