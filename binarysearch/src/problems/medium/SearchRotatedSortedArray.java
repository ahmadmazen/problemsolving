package problems.medium;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/*
 * https://leetcode.com/problems/search-in-rotated-sorted-array
 * problem definition : 
 * Suppose an array sorted in ascending order is rotated at some pivot unknown to you beforehand.
 * (i.e., [0,1,2,4,5,6,7] might become [4,5,6,7,0,1,2]).
 * You are given a target value to search.
 * If found in the array return its index, otherwise return -1.
 * You may assume no duplicate exists in the array.
 * Your algorithm's runtime complexity must be in the order of O(log n).
 * Example 1:
 * Input: nums = [4,5,6,7,0,1,2], target = 0
 * Output: 4
 * Example 2:
 * Input: nums = [4,5,6,7,0,1,2], target = 3
 * Output: -1
 */
public class SearchRotatedSortedArray {
	int[] nums;
	int target;
	/*
	 * This problem can be solved by one of two approaches:
	 * approach one: two passes-Binary Search
	 * first pass binary search the rotation-index, 
	 * then you will know the two sub-arrays (sorted one, and the unsorted one)
	 * then determine which one to binary search for the target
	 * follow the code below along with comments to understand
	 * Time Complexity: O(logn) -- Space Complexity: O(1)
	 */
	public int search(int[] nums, int target) {
		this.nums = nums;
		this.target = target;
		int size = nums.length;
		
		// always some sanity checks
		if (nums == null || size == 0) {
			return -1;
		}
		//in case the array of only one element
		if(size == 1 ) {
			return nums[0] == target ? 0 : -1;
		}
		//binary search the rotation index, in which the array is splitted into two subarrays
		//one is sorted, the other is not
		//e.g. nums = [4,5,6,7,0,1,2] from 4--> 7 unsorted, from 0-->2 sorted.
		
		int rotate_index = find_rotate_index(0, size - 1);
		
		//then the array in not rotated and we will search the entire array for the target
		if(rotate_index == 0) {
			return binarySearchTarget(0, size - 1);
		}else {
			//it's rotated, we need to determine which sub-array to search for the target,
			//since the rotated is splitted into two sub-arrays: one sorted and the other not sorted
			//we will check whether the target is greater or lesser than the first element in the array
			//then determine where to go (right-side of the rotate index of left-side of it)
			//if target is greater than the first element then it's in the sorted sub-array right before
			//the rotation point so you will search from the (first element : rotation_index)
			//else: then the target exists after the rotation point: (rotation_index : last element)
			//e.g: { 4, 5, 6, 7, 0, 1, 2 } 
			//if target = 1 --> 1 < 4 then we should search from rotate index to the last element
			//if target = 7 --> 7 > 4 then we should search from 0 to the rotate index.
			if(target < nums[0]) {
				return binarySearchTarget(rotate_index, size - 1);
			}else {
				return binarySearchTarget(0, rotate_index);
			}
		}
	}
	/*
	 * binary search the array to find the rotation index = index of the smallest element
	 * if no rotation index which means the array is not rotated --> return 0
	 */
	private int find_rotate_index(int left, int right) {
		
		
		//if the last element is bigger than the first element, then it's not rotated array
		//e.g : 1, 2, 3, 4, 5
		if(nums[right] > nums[left]) {
			return 0;
		}
		//start binary searching the array for finding the rotation index
		//rotation index is where the element will be lower than the previous in array given that
		//it's sorted ascendingly
		//e.g: 6, 7, 0, 1 --> index no 2 here is the rotation index, since 0 is lower than 7
		//where the rotation started to happen
		
		while(left <= right) {
			int pivot = left + ((right - left) / 2);
			//if the pivot element is greater than the next element, then the next element is 
			//the rotation index
			//e.g:  {5, 6, 7, 0, 1} --> suppose this is an array 
			// or even half an array in binary search iteration
			// the (pivot = 7) and the next element is lower then we found it
			if(nums[pivot] > nums[pivot + 1]) {
				return pivot + 1;
			//if not then we need to adjust our boundary and iterate more
			// to determine the direction you would go:
			// - we need to compare the pivot element with the left element
			// - if the pivot is lower like in this half: {7, 0, 1} so we need to go left
			// be careful this is gonna be a bit confusy, why going left since we stand right upon
			//the rotation index
			//answer: because our condition to find the rotation index is like upward:
			//if(nums[pivot] > nums[pivot + 1])  return pivot + 1;
			//	in order to make it happen you should go left: to make your right = 7 (your left already 7 also)
			// so in the next iteration:left = right = pivot = 7 and the condition would be true 7 < 0
			
			// but what if your left is lower: then you would go right
			// like: {5, 6, 7, 0, 1} pivot = 7 and it's lower than left 5, that' why go right
			// by making your left = 0
			}else {
				if(nums[pivot] < nums[left] ) {
					right = pivot - 1;
				}else {
					left = pivot + 1;
				}
			}
			
		}		
		//in case the array is found sorted return 0
		return 0;
	}
	
	/*
	 * simple and standard binary search the target.
	 * @return its index if found, otherwise return -1
	 */
	private int binarySearchTarget(int left, int right) {
		
		while(left <= right) {
			int pivot = left + ((right - left) / 2);
			if(nums[pivot] == target) {
				return pivot;
			}else if(nums[pivot] > target) {
				right = pivot - 1;
			}else {
				left = pivot + 1;
			}
		}
		return -1;
		
	}
	
	/*
	 * Approach Two: one-pass Binary Search
	 * leetCode explanation of the approach:
	 * Instead of going through the input array in two passes,
	 * we could achieve the goal in one pass with customized binary search.
	 * The idea is that we add some additional condition checks in the normal binary search 
	 * in order to better narrow down the scope of the search.
	 * follow the code along with the comments
	 * 
	 * Time Complexity: O(logn) -- Space Complexity: O(1)
	 */
	public int search_approach_two(int[] nums, int target) {
		
		int left = 0;
		int right = nums.length - 1;
		
		while(left <= right) {
			int pivot = left + (right - left) / 2;
			//if the target equal to pivot, then the job is done
			if(nums[pivot] == target) {
				return pivot;
			//we have two scenarios:
			//one: when the pivot is located in non-rotated part
	        //two: when the pivot is located in somewhere in the rotated part
			//to distinguish that by comparing the pivot element with the first element
			//if greater then scenario one (non-rotated)
			}else if(nums[pivot] >= nums[left]) {
				//scenario one --> non-rotated part
				//we need to check if the target is in this non-rotated half
				//by comparing the target with left element and the pivot element
				
				//be careful the equality operator below here(line : 183) is tricky
				//target >= nums[left]
				//but if you removed it the test case in line 225 will fail
				//dry-run it you will understand or even run it in debug-mode and see the flow
				if(target >= nums[left] && target < nums[pivot]) {
					right = pivot - 1;
				}else {
					left = pivot + 1;
				}
			}else {
				//scenario two: rotated part
				//so we need to check if the target is located in this part
				//by comparing the target with the right element and pivot element
				if(target > nums[pivot] && target <= nums[right]) {
					left = pivot + 1;
				}else {
					right = pivot - 1;
				}
			}
		}
		return -1;
	}
	@Test
	public void test_rotationIndex() {
		this.nums = new int[] { 4, 5, 6, 7, 0, 1, 2 }; 
		assertEquals(4, find_rotate_index(0, nums.length - 1));
		this.nums = new int[] { 2, 4, 5, 6, 7, 0, 1 };
		assertEquals(5, find_rotate_index(0, nums.length - 1));
	}
	@Test
	public void test_binarySearchTarget() {
		this.nums = new int[] { 4, 5, 6, 7, 0, 1, 2 }; 
		this.target = 5;
		assertEquals(1, binarySearchTarget(0, 3));
		this.target = 2;
		assertEquals(6, binarySearchTarget(4, 6));
		this.target = 5;
		assertEquals(-1, binarySearchTarget(4, 6));
		
	}
	@Test
	public void test_search_approach_One() {
		int[] arr = new int[] { 4, 5, 6, 7, 0, 1, 2 }; 
		
		assertEquals(-1, search(arr, 3));
		
		assertEquals(1, search(arr, 5));
		
		assertEquals(6, search(arr, 2));
		
	}
	
	@Test
	public void test_search_approach_two() {
		int[] arr = new int[] { 4, 5, 6, 7, 0, 1, 2 }; 
		
		assertEquals(-1, search_approach_two(arr, 3));
		
		assertEquals(1, search_approach_two(arr, 5));
		
		assertEquals(6, search_approach_two(arr, 2));
		
		arr = new int[] {3, 1};
		assertEquals(1, search_approach_two(arr, 1));
		
		//tricky test case, it's the reason for the comment in line 167 for the equality operator
		assertEquals(4, search_approach_two(arr, 0));
		
	}
	
}
