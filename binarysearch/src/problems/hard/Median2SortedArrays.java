package problems.hard;

/*
 * problem statement: https://leetcode.com/problems/median-of-two-sorted-arrays/
 * 
 * Given two sorted arrays nums1 and nums2 of size m and n respectively.
 * Return the median of the two sorted arrays.
 * 
 * Follow up: The overall run time complexity should be O(log (m+n)).
 * 
 * Example1:
 * Input: nums1 = [1,3], nums2 = [2]
 * Output: 2.00000
 * Explanation: merged array = [1,2,3] and median is 2.
 * 
 * Example2:
 * Input: nums1 = [1,2], nums2 = [3,4]
 * Output: 2.50000
 * Explanation: merged array = [1,2,3,4] and median is (2 + 3) / 2 = 2.5.
 * 
 * Example 3:
 * Input: nums1 = [0,0], nums2 = [0,0]
 * Output: 0.00000
 * 
 * Example 4:
 * Input: nums1 = [], nums2 = [1]
 * Output: 1.00000
 * 
 * Example 5:
 * Input: nums1 = [2], nums2 = []
 * Output: 2.00000
 * 
 * 
 * /* nice explanation of the optimized solution
 * https://www.youtube.com/watch?v=LPFhl65R7ww&t=1013s -- 
 * https://leetcode.com/problems/median-of-two-sorted-arrays/discuss/2481/share-my-ologminmn-solution-with-explanation
 * https://github.com/mission-peace/interview/blob/master/src/com/interview/binarysearch/MedianOfTwoSortedArrayOfDifferentLength.java
 */
 
public class Median2SortedArrays {
/*
	// brute-force solution, the sort after merging will cost O(nlogn),
	// we should improve that to be o(log m+n) where m the length of arr1, n length
	// of arr2
	// then improved the merge to avoid the need for sorting
	// still we are in linear time, we need to improve more to reach the logarithmic
	// time
	public static double findMedianSortedArrays(int[] nums1, int[] nums2) {
		// int[] result = mergeTwoArrays(nums1, nums2);
		// Arrays.sort(result);
		int[] result = mergeTwoArrays(nums1, nums2); // O(m+n)
		int resultLength = result.length;
		boolean isEven = resultLength % 2 == 0 ? true : false;
		double median = 0;
		if (isEven) {
			int medianPos = resultLength / 2 - 1;
			median = (result[medianPos] + result[medianPos + 1]) / 2.0;
			return median;
		}
		int medianPos = resultLength / 2;
		return result[medianPos];

	}

	
//	 * old version of merging, this would need to sort after using it
//	 * 
//	 * private static int[] mergeTwoArrays(int[] arr1, int[] arr2) { int[] result =
//	 * new int[arr1.length + arr2.length]; System.arraycopy(arr1, 0, result, 0,
//	 * arr1.length); System.arraycopy(arr2, 0, result, arr1.length, arr2.length);
//	 * return result;
//	 * 
//	 * }
	 
	// improved the merge to avoid the need to sort
	// Time complexity O(m + n) since I only need to iterate over
	static int[] mergeTwoArrays(int[] arr1, int[] arr2) {
		int[] result = new int[arr1.length + arr2.length];
		int arr1Pointer = 0;
		int arr2Pointer = 0;
		int resultPointer = 0;
		while (arr1Pointer < arr1.length && arr2Pointer < arr2.length) {
			if (arr1[arr1Pointer] <= arr2[arr2Pointer]) {
				result[resultPointer++] = arr1[arr1Pointer++];
			} else {
				result[resultPointer++] = arr2[arr2Pointer++];
			}

		}
		if (arr1Pointer < arr1.length) {
			while (arr1Pointer < arr1.length) {
				result[resultPointer] = arr1[arr1Pointer];
				arr1Pointer++;
				resultPointer++;
			}
		}
		if (arr2Pointer < arr2.length) {
			while (arr2Pointer < arr2.length) {
				result[resultPointer] = arr2[arr2Pointer];
				arr2Pointer++;
				resultPointer++;
			}
		}
		return result;

	}
	*/
	
	  /*
	  * Solution
	  * Take minimum size of two array. Possible number of partitions are from 0 to m in m size array.
	  * Try every cut in binary search way. When you cut first array at i then you cut second array at (m + n + 1)/2 - i
	  * Now try to find the i where a[i-1] <= b[j] and b[j-1] <= a[i]. 
	  * So this i is partition around which lies the median.
	  *
	  * Time complexity is O(log(min(x,y))
	  * Space complexity is O(1)
	 */
	public static double findMedianSortedArrays(int[] nums1, int[] nums2) {
		//assert that the first input is lower in length, why? 
		//because we are looking to reduce the search space and making the final time complexity O(log(min(x,y))
		//by that we guarantee that we run the binary search on the array of minimum length
		if(nums1.length > nums2.length) {
			return findMedianSortedArrays(nums2, nums1);
		}
		
		
		int x = nums1.length;
		int y = nums2.length;
		
		if(x > y) {
			return findMedianSortedArrays(nums2, nums1);
		}
		int low = 0;
		int high = x;
		while(low <= high) {
			int partitionPoint_x = (low + high) / 2; //calculate the length of left partition of x
			int partitionPoint_y = (x + y + 1 ) / 2 - partitionPoint_x; //calculate the length of left partition of y
			
			//if partitionX is 0 it means nothing is there on left side. Use -INFINITY for maxLeftX
            //if partitionX is length of input then there is nothing on right side. Use +INFINITY for minRightX
			int max_leftPartition_x = partitionPoint_x == 0 ?  Integer.MIN_VALUE : nums1[partitionPoint_x - 1];// minus 1 because zero-index based
			int min_rightPartition_x = partitionPoint_x == x ? Integer.MAX_VALUE : nums1[partitionPoint_x];
			
			int max_leftPartition_y = partitionPoint_y == 0 ? Integer.MIN_VALUE : nums2[partitionPoint_y - 1];// minus 1 because zero-index based
			int min_rightPartition_y = partitionPoint_y == y ? Integer.MAX_VALUE : nums2[partitionPoint_y];
			//
			if(max_leftPartition_x <= min_rightPartition_y && max_leftPartition_y <= min_rightPartition_x) {
				 //We have partitioned array at correct place
                // Now get max of left elements and min of right elements to get the median in case of even length combined array size
                // or get max of left for odd length combined array size.
				if((x + y) % 2 == 0) {
					return ((double)Math.max(max_leftPartition_x, max_leftPartition_y) + Math.min(min_rightPartition_x, min_rightPartition_y)) / 2; 
				}else {
					return (double)Math.max(max_leftPartition_x, max_leftPartition_y);
				}
				
			}else if(max_leftPartition_x > min_rightPartition_y) {//we are too far on right side for partitionX. Go on left side.
				high = partitionPoint_x - 1;
				
			}else{  // in case max_leftPartition_y > min_rightPartition_x, //we are too far on left side for partitionX. Go on right side.
				low = partitionPoint_x + 1;
			}
			
		}
		
		 //Only we we can come here is if input arrays were not sorted.
        throw new IllegalArgumentException();
	}

	public static void main(String[] args) { // 1 2 3 4 5
		System.out.println(findMedianSortedArrays(new int[] { 1, 3 }, new int[] { 2 }));
		System.out.println(findMedianSortedArrays(new int[] { 1, 2 }, new int[] { 3, 4 }));
		
		int[] x = {1, 3, 8, 9, 15};
	    int[] y = {7, 11, 19, 21, 18, 25};

	    System.out.println(findMedianSortedArrays(x, y));
	
	}

}
