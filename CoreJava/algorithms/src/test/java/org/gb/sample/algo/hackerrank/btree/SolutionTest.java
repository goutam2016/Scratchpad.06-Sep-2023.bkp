package org.gb.sample.algo.hackerrank.btree;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

public class SolutionTest {

	private static final Logger LOGGER = Logger.getLogger(SolutionTest.class);

	private BinaryTree<Integer> binaryTree = buildBinaryTree();

	private BinaryTree<Integer> buildBinaryTree() {
		Node<Integer> node19 = new Node<>(19, null, null);
		Node<Integer> node20 = new Node<>(20, null, null);
		Node<Integer> node15 = new Node<>(15, node19, null);
		Node<Integer> node16 = new Node<>(16, node20, null);
		Node<Integer> node17 = new Node<>(17, null, null);
		Node<Integer> node18 = new Node<>(18, null, null);
		Node<Integer> node7 = new Node<>(7, null, null);
		Node<Integer> node8 = new Node<>(8, null, null);
		Node<Integer> node9 = new Node<>(9, null, null);
		Node<Integer> node10 = new Node<>(10, null, null);
		Node<Integer> node11 = new Node<>(11, node15, node16);
		Node<Integer> node12 = new Node<>(12, node17, node18);
		Node<Integer> node13 = new Node<>(13, null, null);
		Node<Integer> node14 = new Node<>(14, null, null);
		Node<Integer> node3 = new Node<>(3, node7, node8);
		Node<Integer> node4 = new Node<>(4, node9, node10);
		Node<Integer> node5 = new Node<>(5, node11, node12);
		Node<Integer> node6 = new Node<>(6, node13, node14);
		Node<Integer> node1 = new Node<>(1, node3, node4);
		Node<Integer> node2 = new Node<>(2, node5, node6);
		Node<Integer> node0 = new Node<>(0, node1, node2);

		BinaryTree<Integer> binaryTree = new BinaryTree<>(node0);
		return binaryTree;
	}

	@Test
	public void getHeight_Test() {
		int height = Solution.getHeight(binaryTree);

		Assert.assertEquals(5, height);
	}

	@Test
	public void findLCA_Test1() {
		Node<Integer> lca = Solution.findLCA(binaryTree, 15, 13);

		Assert.assertEquals(2, lca.getValue().intValue());
	}

	@Test
	public void findLCA_Test2() {
		Node<Integer> lca = Solution.findLCA(binaryTree, 19, 20);

		Assert.assertEquals(11, lca.getValue().intValue());
	}

	@Test
	public void findLCA_Test3() {
		Node<Integer> lca = Solution.findLCA(binaryTree, 8, 6);

		Assert.assertEquals(0, lca.getValue().intValue());
	}
}
