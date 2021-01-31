package org.gb.sample.algo.hackerrank.btree.bst;

import org.gb.sample.algo.hackerrank.btree.BinaryTree;
import org.gb.sample.algo.hackerrank.btree.Node;
import org.gb.sample.algo.hackerrank.btree.bst.Solution;
import org.junit.Assert;
import org.junit.Test;

public class SolutionTest {
	private BinaryTree<Integer> nonBST = buildNonBST();
	private BinaryTree<Integer> bst = buildBST();

	private BinaryTree<Integer> buildNonBST() {
		Node<Integer> node2_1 = new Node<>(1, null, null);
		Node<Integer> node2_2 = new Node<>(3, null, null);
		Node<Integer> node1_1 = new Node<>(2, node2_1, node2_2);
		Node<Integer> node2_3 = new Node<>(3, null, null);
		Node<Integer> node2_4 = new Node<>(7, null, null);
		Node<Integer> node1_2 = new Node<>(6, node2_3, node2_4);
		Node<Integer> node0 = new Node<>(4, node1_1, node1_2);

		BinaryTree<Integer> binaryTree = new BinaryTree<>(node0);
		return binaryTree;
	}

	private BinaryTree<Integer> buildBST() {
		Node<Integer> node2_1 = new Node<>(1, null, null);
		Node<Integer> node2_2 = new Node<>(3, null, null);
		Node<Integer> node1_1 = new Node<>(2, node2_1, node2_2);
		Node<Integer> node2_3 = new Node<>(5, null, null);
		Node<Integer> node2_4 = new Node<>(7, null, null);
		Node<Integer> node1_2 = new Node<>(6, node2_3, node2_4);
		Node<Integer> node0 = new Node<>(4, node1_1, node1_2);

		BinaryTree<Integer> binaryTree = new BinaryTree<>(node0);
		return binaryTree;
	}

	@Test
	public void isBST_NotBST() {
		boolean isBST = Solution.isBST(nonBST);

		Assert.assertFalse(isBST);
	}

	@Test
	public void isBST_IsBST() {
		boolean isBST = Solution.isBST(bst);

		Assert.assertTrue(isBST);
	}
}
