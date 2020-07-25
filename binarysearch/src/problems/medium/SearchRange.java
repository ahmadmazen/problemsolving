package problems.medium;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/*
 * Given an array of integers nums sorted in ascending order, 
 * find the starting and ending position of a given target value.
 * 
 * Your algorithm's runtime complexity must be in the order of O(log n).
 * 
 * If the target is not found in the array, return [-1, -1].
 * 
 * Example 1:
 * Input: nums = [5,7,7,8,8,10], target = 8
 * Output: [3,4]
 * 
 * Example 2:
 * Input: nums = [5,7,7,8,8,10], target = 6
 * Output: [-1,-1]
 * 
 * Constraints:
 * 0 <= nums.length <= 10^5
 * -10^9 <= nums[i] <= 10^9
 * nums is a non decreasing array.
 * -10^9 <= target <= 10^9
 */
public class SearchRange {

	/*
	 * Approach One: linear scan the whole array looking for the target Algorithm:
	 * 1. starting from the first element of the array checking if the element ==
	 * target 2. whenever we found the target we will break the loop as we found the
	 * first occurence(leftmost one) of the target 3. starting from the end of the
	 * array (reverse scan) looking for the target, once we hit it we break (we
	 * found the rightmost one) the last occurence 4. return result array
	 * 
	 * Time Complexity: O(n) this algorithm exhausts the search space, in the worst
	 * case we could end up visiting each element twice so the overall runtime is
	 * linear. Space Complexity: O(1)
	 */

	public int[] searchRange_approach_one(int[] nums, int target) {
		int[] targetRange = { -1, -1 };

		if (nums == null || nums.length == 0) {
			return targetRange;
		}
		// the first occurence of the target becomes the left boundary of the range
		// then we break
		for (int i = 0; i < nums.length; i++) {
			if (nums[i] == target) {
				targetRange[0] = i;
				break;
			}
		}
		// that means the target is not exist in the array
		if (targetRange[0] == -1) {
			return targetRange;
		}
		// start searching in a reverse way right--> left
		// to find the first occurence from the right->left direction
		for (int i = nums.length - 1; i >= 0; i--) {
			if (nums[i] == target) {
				targetRange[1] = i;
				break;
			}
		}

		return targetRange;
	}

	/*
	 * Approach Two: Binary Search the array looking for leftmost and rightmost
	 * occurence
	 * 
	 * Algorithm: 1. binary search the leftmost occurence of the target 2. binary
	 * search the rightmost occurence of the target
	 * 
	 * Improvment: we can abstract the two methods to be one helper method searching
	 * in a direction specified by direction argument for example:
	 * binarySearchExtremeIndex() look at line 151
	 * 
	 * Time Complexity : O(logn), Space Complexity : O(1)
	 */
	public int[] searchRange_approach_two(int[] nums, int target) {
		int[] targetRange = new int[] { -1, -1 };

		if (nums == null || nums.length == 0) {
			return targetRange;
		}

		targetRange[0] = binarySearchBeginningIndex(nums, target);
		targetRange[1] = binarySearchEndingIndex(nums, target);
		return targetRange;
	}

	/*
	 * binary search the leftmost occurence of the target
	 */
	private int binarySearchBeginningIndex(int[] nums, int target) {
		int leftmostIndex = -1;
		int left = 0, right = nums.length - 1;

		while (left <= right) {
			int mid = left + (right - left) / 2;
			// since we are going in the left direction (the beginning index in the
			// leftmost),
			// so whenever we found the target or it's still lower than the middle, we go
			// further left

			if (nums[mid] >= target) {
				right = mid - 1;
				// we keep checking each iteration if we are in the target range
				// we update the leftmost with the current index, otherwise we keep it -1
				leftmostIndex = nums[mid] == target ? mid : -1;
			} else {
				left = mid + 1;
			}
		}

		return leftmostIndex;
	}

	private int binarySearchEndingIndex(int[] nums, int target) {
		int firstIndex = -1;
		int left = 0, right = nums.length - 1;

		while (left <= right) {
			int mid = left + (right - left) / 2;

			// since we are going in the right direction (the Ending index in the
			// rightmost),
			// so whenever we found the target or it's still bigger than the middle, we go
			// further right
			if (nums[mid] <= target) {
				left = mid + 1;
				// we keep checking each iteration if we are in the target range
				// we update the leftmost with the current index, otherwise we keep it -1
				firstIndex = nums[mid] == target ? mid : -1;
			} else {
				right = mid - 1;
			}
		}

		return firstIndex;
	}

	private int binarySearchExtremeIndex(int[] nums, int target, boolean left) {
		int lo = 0;
		int hi = nums.length;

		while (lo < hi) {
			int mid = (lo + hi) / 2;
			if (nums[mid] > target || (left && target == nums[mid])) {
				hi = mid;
			} else {
				lo = mid + 1;
			}
		}

		return lo;
	}

	public int[] searchRange_approach_three(int[] nums, int target) {
		int[] targetRange = { -1, -1 };

		int leftIdx = binarySearchExtremeIndex(nums, target, true);

		// assert that `leftIdx` is within the array bounds and that `target`
		// is actually in `nums`.
		if (leftIdx == nums.length || nums[leftIdx] != target) {
			return targetRange;
		}

		targetRange[0] = leftIdx;
		targetRange[1] = binarySearchExtremeIndex(nums, target, false) - 1;

		return targetRange;
	}

	@Test
	public void test_searchRange_approach_one() {

		assertArrayEquals(new int[] { 3, 4 }, searchRange_approach_one(new int[] { 5, 7, 7, 8, 8, 10 }, 8));

		assertArrayEquals(new int[] { -1, -1 }, searchRange_approach_one(new int[] { 5, 7, 7, 8, 8, 10 }, 6));
	}

	@Test
	public void test_searchRange_approach_two() {

		assertArrayEquals(new int[] { 3, 4 }, searchRange_approach_two(new int[] { 5, 7, 7, 8, 8, 10 }, 8));

		assertArrayEquals(new int[] { -1, -1 }, searchRange_approach_two(new int[] { 5, 7, 7, 8, 8, 10 }, 6));
	}
	
	@Test
	public void test_searchRange_approach_three() {

		assertArrayEquals(new int[] { 3, 4 }, searchRange_approach_three(new int[] { 5, 7, 7, 8, 8, 10 }, 8));

		assertArrayEquals(new int[] { -1, -1 }, searchRange_approach_three(new int[] { 5, 7, 7, 8, 8, 10 }, 6));
	}
}
