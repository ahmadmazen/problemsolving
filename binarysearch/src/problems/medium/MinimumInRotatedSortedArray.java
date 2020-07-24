package problems.medium;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/*
 * Suppose an array sorted in ascending order is rotated at some pivot unknown to you beforehand.
 * (i.e.,  [0,1,2,4,5,6,7] might become  [4,5,6,7,0,1,2]).
 * 
 * Find the minimum element.
 * You may assume no duplicate exists in the array.
 * 
 * Example 1:
 * Input: [3,4,5,1,2] 
 * Output: 1
 * 
 * Example 2:
 * Input: [4,5,6,7,0,1,2]
 * Output: 0
 */
public class MinimumInRotatedSortedArray {
	/*
	 * a brute force solution built upon the fact that this array is sorted ascendingly
	 *  is to scan the array and whenever you encounter element bigger than the next element
	 *  you return the next element --> return nums[i + 1] where nums[i] > nums[i+1]
	 *  but this would cost us O(n) time complexity
	 *  
	 *  better solution is to binary search the array:
	 *  Algorithm:
	 *  1. pick middle point.
	 *  2. compare the middle element with the last element of the array
	 *  3. if the middle element is lower then we need to go left 
	 *  since we would be in the after-rotation-part, otherwise we go right
	 *  
	 *  and at the end it's for sure the minimum element will be in the left pointer
	 *  4. return nums[left]
	 *  
	 *  Time Complexity: O(logn), Space Complexity : O(1)
	 */
	 public int findMin(int[] nums) {
	        if(nums == null || nums.length == 0){
	            return -1;
	        }
	        //check if the array is not rotated, in this case we return the first element(it's minimum)
	        if(nums[0] < nums[nums.length - 1]) {
	        	return nums[0];
	        }
	        //if the array has only one element
	        if(nums.length == 11) {
	          return nums[0];
	        }
	        
	        //the last element to compare with the middle 
	        //in each iteration and determin which direction to go
	        int lastElement = nums[nums.length - 1];
	        int left = 0;
	        int right = nums.length - 1;
	        
	        while(left <= right){
	            int middle = left + (right - left) / 2;
	            if(nums[middle] > lastElement){
	                left = middle + 1;
	            }
	            else{
	                right = middle - 1;
	            }
	        }
	        return nums[left];
	        
	    }
	 @Test
	 public void test_findMin() {
		 
		 assertEquals(1, findMin(new int[] {3,4,5,1,2}));  
		 
		 assertEquals(0, findMin(new int[] {4,5,6,7,0,1,2}));  
	 }

}
