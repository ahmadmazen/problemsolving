package problems.medium;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/*
 * https://leetcode.com/problems/find-peak-element/solution/
 * it's well explained in leetCode, I encourage you to read the article there
 * 
 * problem definition:
 * A peak element is an element that is greater than its neighbors.
 * Given an input array nums, where nums[i] ≠ nums[i+1], find a peak element and return its index.
 * The array may contain multiple peaks, in that case return the index to any one of the peaks is fine.
 * You may imagine that nums[-1] = nums[n] = -∞.
 * 
 * Example 1:
 * Input: nums = [1,2,3,1]
 * Output: 2
 * Explanation: 3 is a peak element and your function should return the index number 2.
 * 
 * Example 2:
 * Input: nums = [1,2,1,3,5,6,4]
 * Output: 1 or 5 
 * Explanation: Your function can return either index number 1 where the peak element is 2, 
             or index number 5 where the peak element is 6.
 */
public class FindPeakElement {
	/*
	 * Approach one: Linear time
	 * Time complexity : O(n). We traverse the nums array of size n once only.
	 * Space complexity : O(1). Constant extra space is used.
	 */
	  public int findPeakElement_linearTime(int[] nums) {
	        for (int i = 0; i < nums.length - 1; i++) {
	            if (nums[i] > nums[i + 1])
	                return i;
	        }
	        return nums.length - 1;
	    }
	  /*
	   * Approach two: Binary search
	   * logarithmic time
	   * Time complexity : O(log_2(n). 
	   * We reduce the search space in half at every step. 
	   * Thus, the total search space will be consumed in log_2(n) steps. 
	   * Here, n refers to the size of nums array.
	   * Space complexity : O(1). Constant extra space is used.
	   */
	   public int findPeakElement(int[] nums) {
		   if(nums == null || nums.length == 0){
	            return -1;
	        }
	        
	        int left = 0;
	        int right = nums.length - 1;
	        
	        while(left < right){
	            int mid = left + (right - left) /2;
	            if(nums[mid] > nums[mid + 1]) {
	                right = mid;
	            }else if(nums[mid] < nums[mid + 1]){
	                left = mid + 1;
	            }
	        }
	        return left;
	        
	    }
	   @Test	
	   public void test_findPeakElement() {
		   int[] nums = new int[] {1,2,3,1};
		   assertEquals(2, findPeakElement(nums));
           nums = new int[] {1,2,1,3,5,6,4};
           assertEquals(5, findPeakElement(nums));
	   }

}
