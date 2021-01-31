package org.gb.sample.algo.hackerrank.btree.bst;

import org.gb.sample.algo.hackerrank.btree.BinaryTree;

class Solution {

	static boolean isBST(BinaryTree<Integer> btree) {

		boolean leftSubtreeBST = isLeftSubtreeBST(btree.getLeftSubtree(), btree.getRoot().getValue(), -1);

		if (!leftSubtreeBST) {
			return false;
		}

		boolean rightSubtreeBST = isRightSubtreeBST(btree.getRightSubtree(), btree.getRoot().getValue(), -1);

		if (!rightSubtreeBST) {
			return false;
		}

		return true;
	}

	private static boolean areChildSubtreesBST(BinaryTree<Integer> btree, Integer currentValue, Integer parentValue) {
		boolean leftSubtreeBST = isLeftSubtreeBST(btree.getLeftSubtree(), currentValue, parentValue);

		if (!leftSubtreeBST) {
			return false;
		}

		boolean rightSubtreeBST = isRightSubtreeBST(btree.getRightSubtree(), currentValue, parentValue);

		if (!rightSubtreeBST) {
			return false;
		}

		return true;
	}

	private static boolean isLeftSubtreeBST(BinaryTree<Integer> btree, Integer parentValue, Integer grandParentValue) {
		if (btree == null) {
			return true;
		}

		Integer currentValue = btree.getRoot().getValue();

		if (grandParentValue == -1 || parentValue < grandParentValue) {
			if (currentValue > parentValue) {
				return false;
			}
		}
		if (parentValue > grandParentValue) {
			if (!(currentValue < parentValue && currentValue > grandParentValue)) {
				return false;
			}
		}

		return areChildSubtreesBST(btree, currentValue, parentValue);
	}

	private static boolean isRightSubtreeBST(BinaryTree<Integer> btree, Integer parentValue, Integer grandParentValue) {
		if (btree == null) {
			return true;
		}

		Integer currentValue = btree.getRoot().getValue();

		if (parentValue < grandParentValue) {
			if (!(currentValue > parentValue && currentValue < grandParentValue)) {
				return false;
			}
		}
		if (grandParentValue == -1 || parentValue > grandParentValue) {
			if (currentValue < parentValue) {
				return false;
			}
		}

		return areChildSubtreesBST(btree, currentValue, parentValue);
	}

}
