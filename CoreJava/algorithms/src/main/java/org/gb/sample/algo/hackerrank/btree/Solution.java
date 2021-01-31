package org.gb.sample.algo.hackerrank.btree;

class Solution {

	static <T> int getHeight(BinaryTree<T> btree) {
		return btree.getHeight();
	}

	private static class Status<T> {
		private int targetNodesCount;
		private Node<T> commonAncestor;

		Status(int targetNodesCount) {
			super();
			this.targetNodesCount = targetNodesCount;
		}

		Status(int targetNodesCount, Node<T> commonAncestor) {
			super();
			this.targetNodesCount = targetNodesCount;
			this.commonAncestor = commonAncestor;
		}

		int getTargetNodesCount() {
			return targetNodesCount;
		}

		Node<T> getCommonAncestor() {
			return commonAncestor;
		}
	}

	static <T> Node<T> findLCA(BinaryTree<T> btree, T v1, T v2) {
		Status<T> status = findTargetNodes(btree, v1, v2);
		return status.getCommonAncestor();
	}

	private static <T> Status<T> findTargetNodes(BinaryTree<T> btree, T v1, T v2) {
		if (btree == null) {
			return new Status<>(0);
		}

		BinaryTree<T> leftSubtree = btree.getLeftSubtree();
		Status<T> leftSubtreeStatus = findTargetNodes(leftSubtree, v1, v2);

		if (leftSubtreeStatus.getTargetNodesCount() == 2) {
			return leftSubtreeStatus;
		}

		BinaryTree<T> rightSubtree = btree.getRightSubtree();
		Status<T> rightSubtreeStatus = findTargetNodes(rightSubtree, v1, v2);

		if (rightSubtreeStatus.getTargetNodesCount() == 2) {
			return rightSubtreeStatus;
		}

		Node<T> currentRoot = btree.getRoot();
		int nodeCountIfCurrentRootIsATarget = (currentRoot.getValue() == v1 || currentRoot.getValue() == v2) ? 1 : 0;

		int combinedTgtNodesCount = leftSubtreeStatus.getTargetNodesCount() + rightSubtreeStatus.getTargetNodesCount()
				+ nodeCountIfCurrentRootIsATarget;

		if (combinedTgtNodesCount == 2) {
			// Both target nodes are found, hence currentRoot is the LCA.
			return new Status<>(2, currentRoot);
		} else {
			// Not all target nodes are found, yet to find the LCA.
			return new Status<>(combinedTgtNodesCount);
		}
	}
}
