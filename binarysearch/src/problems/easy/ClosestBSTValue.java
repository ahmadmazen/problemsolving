package problems.easy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Stack;

import problems.common.TreeNode;

/*
 * problem statement:
 * https://leetcode.com/problems/closest-binary-search-tree-value/
 * Given a non-empty binary search tree and a target value, 
 * find the value in the BST that is closest to the target.
 * Note:
 * Given target value is a floating point.
 * You are guaranteed to have only one unique value in the BST that is closest to the target.
 * 
 * Example:
 * Input: root = [4,2,5,1,3], target = 3.714286
 * Output: 4
 */
public class ClosestBSTValue {
	List<Integer> flattenedBSTArray;

	/*
	 * approach one: flattening the BST by inorder traversal, I will end up having
	 * array of BST values sorted Find the closest to target element in that array.
	 * Time complexity: O(n) the recursion traversal will visit each node in the
	 * tree to fill the list then linear search to find the minimum Space
	 * complexity: O(n) space required to the list
	 */
	public int closestValue_approachOne(TreeNode root, double target) {
		// flattening the BST by inorder traversal, I will end up having array of
		// BST values sorted
		this.flattenedBSTArray = new ArrayList<Integer>();
		_inorderTraversal(root);
		// using java8 collections.min utility to return the value that has minimum
		// difference with the target
		// or implement the comparator interface by anonymous object
//		return Collections.min(flattenedBSTArray, new Comparator<Integer>() {
//			@Override
//			public int compare(Integer o1, Integer o2) {
//				return Double.compare(Math.abs(o1 - target), Math.abs(o2 - target));
//			}
//		});
		return Collections.min(flattenedBSTArray, Comparator.comparingDouble(o -> Math.abs(target - o)));
	}

	private void _inorderTraversal(TreeNode root) {
		if (root == null) {
			return;
		}
		_inorderTraversal(root.getLeft());
		flattenedBSTArray.add(root.getVal());
		_inorderTraversal(root.getRight());

	}

	/*
	 * approach two: when index k of the closest element is much smaller than the
	 * tree heigh H. First, one could merge both steps by traversing the tree and
	 * searching the closest value at the same time. Second, one could stop just
	 * after identifying the closest value, there is no need to traverse the whole
	 * tree. The closest value is found if the target value is in-between of two
	 * inorder array elements nums[i] <= target < nums[i + 1]. Then the closest
	 * value is one of these elements.
	 * 
	 * Time complexity: O(k) where k is the index of the closest value 
	 * Space complexity: up to O(H) to keep the stack in the case of non-balanced tree.
	 */
	public int closestValue_approachTwo(TreeNode root, double target) {
		Stack<TreeNode> stack = new Stack<TreeNode>();
		long pred = Long.MIN_VALUE;
		while (!stack.isEmpty() || root != null) {
			// traverse the leftmost
			while (root != null) {
				stack.push(root);
				root = root.getLeft();
			}
			// start processing the first elem in the stack, check if it's the closest (the
			// target lies between the pred and the current)
			root = stack.pop();
			if (root.getVal() > target && pred <= target) {
				return Math.abs(target - root.getVal()) < Math.abs(target - pred) ? root.getVal() : (int) pred;
			}
			// if it's not the closest, continue processing
			pred = root.getVal();// assign the current to the pred
			root = root.getRight(); // go right if there is right because the left branch has reached leaf
		}
		// We're here because during the loop one couldn't identify the closest value.
		// That means that the closest value is the last value in the inorder traversal,
		// i.e. current predecessor value. Return it.
		return (int) pred;//
	}

	/**
	 * 
	 * Approach 3: Binary Search, O(H) time Approach 2 works fine when index k of
	 * closest element is much smaller than the tree height H.
	 * 
	 * Let's now consider another limit and optimize Approach 1 in the case of
	 * relatively large k, comparable with N.
	 * Then it makes sense to use a binary search: go left if target is smaller than
	 * current root value, and go right otherwise.
	 * Choose the closest to target value at each step.
	 * 
	 * Time Complexity: O(h) since here one goes from root down to a leaf.
	 * Space Complexity: O(1)
	 * 
	 */

	public int closestValue(TreeNode root, double target) {

		int val, closest = root.getVal();
		while (root != null) {
			val = root.getVal();
			closest = Math.abs(val - target) < Math.abs(closest - target) ? val : closest;
			root = target < root.getVal() ? root.getLeft() : root.getRight();
		}

		return closest;
	}

	public static void main(String[] args) {
		TreeNode rootTest = new TreeNode(4);
		rootTest.setLeft(new TreeNode(2));
		rootTest.setRight(new TreeNode(5));
		rootTest.getLeft().setLeft(new TreeNode(1));
		rootTest.getLeft().setRight(new TreeNode(3));
		ClosestBSTValue c = new ClosestBSTValue();
		System.out.println("============ approach one ========================");
		System.out.println(c.closestValue_approachOne(rootTest, 3.714286));

		TreeNode rootTest1 = new TreeNode(2);
		rootTest1.setLeft(new TreeNode(1));

		System.out.println(c.closestValue_approachOne(rootTest1, 3.0));
		// approach two
		System.out.println("============ approach two ========================");
		System.out.println(c.closestValue_approachTwo(rootTest, 3.714286));
		System.out.println(c.closestValue_approachOne(rootTest1, 3.0));

		System.out.println("============ approach three ========================");
		System.out.println(c.closestValue(rootTest, 3.714286));
		System.out.println(c.closestValue(rootTest1, 3.0));
	}

}
