package problems.easy;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

/*
 * https://leetcode.com/problems/first-bad-version
 * problem definition :
 * You are a product manager and currently leading a team to develop a new product. 
 * Unfortunately, the latest version of your product fails the quality check. 
 * Since each version is developed based on the previous version, 
 * all the versions after a bad version are also bad.
 * Suppose you have n versions [1, 2, ..., n] and you want to find out the first bad one,
 * which causes all the following ones to be bad.
 * Example:
 * Given n = 5, and version = 4 is the first bad version.
 * call isBadVersion(3) -> false
 * call isBadVersion(5) -> true
 * call isBadVersion(4) -> true
 * Then 4 is the first bad version.
 * You are given an API bool isBadVersion(version) which will return whether version is bad. 
 * Implement a function to find the first bad version. 
 * You should minimize the number of calls to the API.
 */
public class FirstBadVersion {
	private Map<Integer, Boolean> badVersionMap = new HashMap<>();
	private int numberOfVersions;
	private int firstBadVersion;

	/*
	 * It's straightforward we can binary search the series of versions until we find the
	 * first bad version
	 * this time since it's mentioned in the problem definition "you should minmize the number of calls to the API"
	 * so, we will use a bit different implementation of regular binary search to reduce the
	 * number of calls to isBadVersion.
	 * Essentially that can be achieved by making the condition of loop(left < right)
	 * instead of  (left <= right) and return left after the loop, to lower the number of iteration
	 * this is a bit tricky and you need to dry-run it with the numbers or even run your debugger to
	 * see what's going on
	 */
	public int firstBadVersion(int n) {
		int left = 1;
		int right = n;
		//as I said above left < right, no need to iterate one more time when the right = left
		//because it's easy to induce that the left is the solution
		//let's dry-run this with example
		//e.g: n = 5, firstBadVersion = 4
		// -iteration 1 : left = 1, right = 5, middle = 3 --> 3 is not bad version
		// so we need to adjust our left to start after the middle : left = middle + 1
		// to search in the right half
		
		// -iteration 2 : left = 4, right = 5, middle = 4 --> 4 is bad version
		//  so we need to adjust our right to start searching in the left side because there might be
		//  versions bad before it
		//  the trick here that we don't make it as traditional binarySearch(right = middle - 1),
		//  instead we make it(right = middle) --> the reason for that is that this version can be
		//  the first bad version, so we should keep it in our search space by making it the right boundary
		
		//and if you notice that this trick happended with our current test data, 4 is the actual
		//first bad version
		
		//now : left = 4, right = 4 (after adjusting in the iteration 2 ) --> so will not go for
		//more iteration since left < right is false (right = left)
		//finally we can return left, given it's for sure the first bad version
		while(left < right) {
			int middle = left + (right -left) / 2;
			if(isBadVersion(middle)) {
				right = middle;
			}else {
				left = middle + 1;
			}
		}
		return left;

	}

	/*
	 * helper method to set the bad versions we want to test with, to simulate the one 
	 * run behind the scene in leetcode platform, to be able to test our code outside the platform
	 * @param n : the number of versions
	 * @param startBadVersion: the start bad version --> to mark the versions bad starting from it
	 */

	private void fillBadVersionMap(int n, int startBadVersion) {
		for (int i = 1; i <= n; i++) {
			this.badVersionMap.put(i, i >= startBadVersion ? true : false);
		}
	}

	private boolean isBadVersion(int n) {
		return badVersionMap.get(n);
	}
	
	@Test
	public void test_firstBadVersion() {
		FirstBadVersion fbv = new FirstBadVersion();
		fbv.numberOfVersions = 5;
		fbv.firstBadVersion = 4;
		fbv.fillBadVersionMap(fbv.numberOfVersions, fbv.firstBadVersion);
		
		assertEquals(fbv.firstBadVersion, fbv.firstBadVersion(fbv.numberOfVersions));
	
	}
}
