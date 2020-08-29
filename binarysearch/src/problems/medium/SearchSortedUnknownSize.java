package problems.medium;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Before;
import org.junit.Test;

import problems.common.ArrayReader;

/*
 * problem statement: https://leetcode.com/problems/search-in-a-sorted-array-of-unknown-size/
 * Given an integer array sorted in ascending order, write a function to search target in nums.  
 * If target exists, then return its index, otherwise return -1. However, 
 * the array size is unknown to you. You may only access the array using an ArrayReader interface, 
 * where ArrayReader.get(k) returns the element of the array at index k (0-indexed).
 * 
 * You may assume all integers in the array are less than 10000, 
 * and if you access the array out of bounds, ArrayReader.get will return 2147483647.
 * 
 * Example 1:
 * Input: array = [-1,0,3,5,9,12], target = 9
 * Output: 4
 * 
 * Explanation: 9 exists in nums and its index is 4
 * 
 * Example 2:
 * Input: array = [-1,0,3,5,9,12], target = 2
 * Output: -1
 * 
 * Explanation: 2 does not exist in nums so return -1
 * 
 * Constraints
 * You may assume that all elements in the array are unique.
 * The value of each element in the array will be in the range [-9999, 9999].
 * The length of the array will be in the range [1, 10^4].
 */
public class SearchSortedUnknownSize {
	public int search_approachOne(ArrayReader reader, int target) {
		//brute-force solution, loop over the array in linear time
		//time complexity: O(n) 
		//space complexity :O(1)
		
		int indx = 0;
		while(reader.get(indx) != 2147483647) {
			if(target == reader.get(indx)) {
				return indx;
			}
			indx++;
		}
        return -1;
	}
	/*
	 * approach two: Binary search, we have one problem before we can start binary searching for the target:
	 * the high boundary since we don't know the length of the array,
	 * inside the code two different ways proposed for working around this
	 */
	public int search_approachTwo(ArrayReader reader, int target) {
		
		int low = 0;
		// we don't know the length of the array, 
		//so we could make the high pointer on the limit given by the problem definition
		//but that might make the search space very long in case the target is very close to the start
		//better approach is to subtract the target from first element
		int high = target - reader.get(0);
		//int high = 10000; 
		if(target == reader.get(high)) {
			return high;
		}
		while(low <= high) {
			int mid = low + (high - low) / 2;
			int midElem = reader.get(mid);
			if(midElem == 2147483647) {
				high = mid - 1;
			}
			if(midElem == target) {
				return mid;
			}
			if(midElem < target) {
				low = mid + 1;
			}else {
				high = mid - 1;
			}
		}
		
		return -1;		
	}
	/*
	 * Approach two:
	 * we have two sub-problems:
	 * 1- Define search limits, i.e. left and right boundaries for the search.
	 * 2- Perform binary search in the defined boundaries.
	 * and both should be done in a logarithmic time:
	 * take two first indexes, 0 and 1, as left and right boundaries. 
	 * If the target value is not among these zeroth and the first element,
	 * then it's outside the boundaries, on the right.
	 * That means that the left boundary could moved to the right, 
	 * and the right boundary should be extended. 
	 * To keep logarithmic time complexity, let's extend it twice as far: right = right * 2.
	 * 
	 * Note: to speed up use bitwise operation to muliply and divide
	 * Left shift: x << 1. The same as multiplying by 2: x * 2.
	 * Right shift: x >> 1. The same as dividing by 2: x / 2.
	 * 
	 * 
	 */
	public int search_approachThree(ArrayReader reader, int target) {

		if (reader.get(0) == target)
			return 0;

		// search boundaries
		int left = 0, right = 1;
		while (reader.get(right) < target) {
			left = right;
			right <<= 1; //left shift by 1 bit, which is equivalent to doing multiplication by 2 : right *= 2 
		}

		// binary search
		int pivot, num;
		while (left <= right) {
			pivot = left + ((right - left) >> 1);
			num = reader.get(pivot);

			if (num == target)
				return pivot;
			if (num > target)
				right = pivot - 1;
			else
				left = pivot + 1;
		}

		// there is no target element
		return -1;

	}
	
	//===========================================================================
	//Testing section
	ArrayReader reader;
	SearchSortedUnknownSize s;
	
	@Before
	public void beforeTest() {
		//initialization
		 reader = new ArrayReader();
		 s = new SearchSortedUnknownSize();
		 reader.nums = new int[] {-1,0,3,5,9,12};
		
	}
	@Test
	public void test_searchApproachTwo() {
       assertEquals(4, s.search_approachTwo(reader, 9));
       assertEquals(-1, s.search_approachTwo(reader, 2));
	}
	@Test
	public void test_searchApproachThree() {
		assertEquals(4, s.search_approachThree(reader, 9));
	    assertEquals(-1, s.search_approachThree(reader, 2));
	}

	public static void main(String[] args) {
		ArrayReader reader = new ArrayReader();
		SearchSortedUnknownSize s = new SearchSortedUnknownSize();
		reader.nums = new int[] {-1,0,3,5,9,12};
//		System.out.println(s.search_approachOne(reader, 9)); //9 exists in nums and its index is 4
//		
//		System.out.println(s.search_approachOne(reader, 2)); //2 does not exist in nums so return -1
		
		
//        System.out.println(s.search(reader, 9)); //9 exists in nums and its index is 4
//		
//		System.out.println(s.search(reader, 2)); //2 does not exist in nums so return -1
		
		reader.nums = new int[] {-1,0,5};
		System.out.println(s.search_approachTwo(reader, 0)); //2 does not exist in nums so return -1
		
	}
}
