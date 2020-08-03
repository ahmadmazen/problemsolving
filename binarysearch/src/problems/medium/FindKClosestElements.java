package problems.medium;

import static org.junit.Assert.assertArrayEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

/*
 * Given a sorted array arr, two integers k and x, 
 * find the k closest elements to x in the array. 
 * The result should also be sorted in ascending order. 
 * If there is a tie, the smaller elements are always preferred.
 * 
 * Example 1:
 * Input: arr = [1,2,3,4,5], k = 4, x = 3
 * Output: [1,2,3,4]
 * 
 * Example 2:
 * Input: arr = [1,2,3,4,5], k = 4, x = -1
 * Output: [1,2,3,4]
 * 
 * Example 3:
 * Input: arr = [1,2,3,4,5], k = 4, x = 6
 * Output: [2, 3, 4, 5] 
 */
public class FindKClosestElements {
	/*
	 * the best solution for this problem I have found so far was posted by a guy on
	 * leetcode discussion on the below link
	 * https://leetcode.com/problems/find-k-closest-elements/discuss/106419/O(log-n)-Java-1-line-O(log(n)-+-k)-Ruby
	 * 
	 * Algorithm: Binary-search for where the resulting elements(k elements) start
	 * in the array if the first index i so that arr[i] is better than arr[i+k]
	 * (with "better" meaning closer to or equally close to x). Then just return the
	 * k elements starting there.
	 * 
	 * Time Complexity: The binary search costs O(log n) (actually also just O(log (n - k))
	 * the part of filling the k result list costs O(k),
	 * so the overall time complexity is O(log(n-k) + k )
	 * 
	 * Space Complexity: we don't consume any auxiliary spaces while processing only the resulting k list
	 *  O(1) No extra spaces 
	 */
	public List<Integer> findClosestElements_approach_one(int[] arr, int k, int x) {
		// we need to set the two pointer start & end (search space)
		// to search for the better starting elements for k range
		// start pointer will be 0 the start of the array
		// end pointer will be the length of the array minus k (arr.length - k)
		// remember we search for the first element of k range that's why the end
		// pointer should be (arr.length - k)

		int start = 0;
		int end = arr.length - k;
		//the binary search iteration here costs O(log(n - k))
		while (start < end) {
			int mid = start + (end - start) / 2;
			//compare the difference between the x target and pivot element with the 
			// diff(x, mid) compared to diff(mid + k, x) 
			if ((x - arr[mid] > arr[mid + k] - x)) {
				start = mid + 1;
			} else {
				end = mid;
			}
		}
		// fill the k elements into the result list
		//  O(k)
		List<Integer> result = new ArrayList(k);
		for(int i = start; i < start + k ; i++) {
			result.add(arr[i]);
		}
		
		
		return result;

	}

	/*
	 * easy to understand solution https://www.youtube.com/watch?v=kSxcZdpV2CA
	 * Algorithm: 1. binary search the array for the closest value to x and return
	 * its index 2. start expanding to left and right around the closest value's
	 * index to reach the k closest elements 3. if the closest value is at one of
	 * the array edges(beginning or ending), so we need to set the pointers(low |
	 * high) at the closest range look at line 82 4. finally we iterate over the
	 * range we have already set the pointers onto and start adding the k elements
	 * to the result list
	 * 
	 * Time complexity : O(logn) for the binary search helper method, then we expand
	 * around the closest that could cost us O(n) if k close or equal to n
	 * 
	 */
	public List<Integer> findClosestElements_approach_two(int[] arr, int k, int x) {
		List<Integer> closestList = new ArrayList<>();
		int closest = binarySearch(arr, x);

		// expanding around the index of the closest value in the range of k
		int low = closest, high = closest;
		while (high - low + 1 < k && low > 0 && high < arr.length - 1) {
			if (Math.abs(x - arr[low - 1]) <= Math.abs(x - arr[high + 1])) {
				low--;
			} else {
				high++;
			}
		}
		// if the closest value right on on one of the array edges(beginning or ending)
		// ex. arr[1, 2, 3, 4, 5] target x = 6, in such case the closest = the last
		// element in the array(5)
		// so we need to move the low pointers (k - 1) steps which is here(3 steps) away
		// from the closest element

		while (high - low + 1 < k) {
			if (low > 0) {
				low--;
			} else {
				high++;
			}
		}
		while (low <= high) {
			closestList.add(arr[low]);
			low++;
		}
		return closestList;

	}

	/*
	 * helper method binary search the closest element, return it's index
	 */
	private int binarySearch(int[] arr, int x) {
		int low = 0, high = arr.length - 1, min_diff = Integer.MAX_VALUE, closest = -1;
		while (low <= high) {
			int mid = low + (high - low) / 2;
			if (Math.abs(x - arr[mid]) < min_diff) {
				min_diff = Math.abs(x - arr[mid]);
				closest = mid;
			}
			if (arr[mid] == x) {
				return mid;
			} else if (arr[mid] < x) {
				low = mid + 1;
			} else {
				high = mid - 1;
			}
		}
		return closest;
	}

	/*
	 * O(nlogn) solution 1. using the sort helper method offered by Collections
	 * class 2. then sublist the first k elements of the result, 3. then sort the
	 * sublist
	 * 
	 * the overall Time Complexity : O(nlogn) Space Complexity : O(k) The in-place
	 * sorting does not consume any extra space. However, generating a k length
	 * sublist will take some space.
	 */
	public List<Integer> findClosestElements_approach_three(List<Integer> arr, int k, int x) {
		// the second parameter of the sort method is taking comparator
		// which can be implemented inline using lambda expression java 8 and higher
		// just as below
		// or by anonymous class like old way
		Collections.sort(arr, (a1, a2) -> a1 == a2 ? a1 - a2 : Math.abs(a1 - x) - Math.abs(a2 - x)); // O(nlogn)

		arr = arr.subList(0, k); // will cost us k space
		Collections.sort(arr); // O(nlogn)
		return arr;
	}

	public static void main(String[] args) {
		FindKClosestElements fcke = new FindKClosestElements();

		List<Integer> closestList = fcke.findClosestElements_approach_one(new int[] { 1, 2, 3, 4, 5 }, 4, 3);
		System.out.println(Arrays.toString(closestList.toArray()));

		closestList = fcke.findClosestElements_approach_one(new int[] { 1, 2, 3, 4, 5 }, 4, -1);
		System.out.println(Arrays.toString(closestList.toArray()));

		closestList = fcke.findClosestElements_approach_one(new int[] { 1, 2, 3, 4, 5 }, 4, 6);
		System.out.println(Arrays.toString(closestList.toArray()));
	}

	@Test
	public void test_findClosestKElements_approach_one() {
		// case when the target inside the array
		List<Integer> closestList = findClosestElements_approach_one(new int[] { 1, 2, 3, 4, 5 }, 4, 3);
		assertArrayEquals(new int[] { 1, 2, 3, 4 }, closestList.stream().mapToInt(elem -> elem.intValue()).toArray());

		// case when the target lower than the first element
		closestList = findClosestElements_approach_one(new int[] { 1, 2, 3, 4, 5 }, 4, -1);
		assertArrayEquals(new int[] { 1, 2, 3, 4 }, closestList.stream().mapToInt(elem -> elem.intValue()).toArray());

		// case when the target bigger than the last element
		closestList = findClosestElements_approach_one(new int[] { 1, 2, 3, 4, 5 }, 4, 6);
		assertArrayEquals(new int[] { 2, 3, 4, 5 }, closestList.stream().mapToInt(elem -> elem.intValue()).toArray());

	}

	@Test
	public void test_findClosestKElements_approach_three() {
		// case when the target inside the array
		List<Integer> closestList = findClosestElements_approach_three(Arrays.asList(new Integer[] { 1, 2, 3, 4, 5 }),
				4, 3);
		assertArrayEquals(new int[] { 1, 2, 3, 4 }, closestList.stream().mapToInt(elem -> elem.intValue()).toArray());

		// case when the target lower than the first element
		closestList = findClosestElements_approach_three(Arrays.asList(new Integer[] { 1, 2, 3, 4, 5 }), 4, -1);
		assertArrayEquals(new int[] { 1, 2, 3, 4 }, closestList.stream().mapToInt(elem -> elem.intValue()).toArray());

		// case when the target bigger than the last element
		closestList = findClosestElements_approach_three(Arrays.asList(new Integer[] { 1, 2, 3, 4, 5 }), 4, 6);
		assertArrayEquals(new int[] { 2, 3, 4, 5 }, closestList.stream().mapToInt(elem -> elem.intValue()).toArray());

	}

	@Test
	public void test_findClosestKElements_approach_two() {
		// case when the target inside the array
		List<Integer> closestList = findClosestElements_approach_two(new int[] { 1, 2, 3, 4, 5 }, 4, 3);
		assertArrayEquals(new int[] { 1, 2, 3, 4 }, closestList.stream().mapToInt(elem -> elem.intValue()).toArray());

		// case when the target lower than the first element
		closestList = findClosestElements_approach_two(new int[] { 1, 2, 3, 4, 5 }, 4, -1);
		assertArrayEquals(new int[] { 1, 2, 3, 4 }, closestList.stream().mapToInt(elem -> elem.intValue()).toArray());

		// case when the target bigger than the last element
		closestList = findClosestElements_approach_two(new int[] { 1, 2, 3, 4, 5 }, 4, 6);
		assertArrayEquals(new int[] { 2, 3, 4, 5 }, closestList.stream().mapToInt(elem -> elem.intValue()).toArray());
	}

}
