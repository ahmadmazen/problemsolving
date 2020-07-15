package problems.easy;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * problem definition:https://leetcode.com/problems/sqrtx/
 */
public class sqrt {
	/*
	 * using binary search
	 * Time complexity : O(log N).
	 * Space complexity: O(1).
	 * Note: this problem can be solved using Newton's Method with same complexity, a bit better
	 */
	public int mySqrt(int x) {
       // since sqrt(1) = 1, sqrt(0) = 0
		if(x < 2) {
			return x;
		}
		//left boundary start from 2
		//why our end boundary is at the half point of x
		//because it's proven that if x >= 2 the  sqrt(x) is always smaller than x/2
		//for example: when x = 10 --> sqrt(10) = 3.16 which is smaller than 10/2
		
		int left = 2, right = x/2, pivot;
		long pivotSquared;
		while(left <= right) {
			pivot = left + ((right - left) / 2);
			//wouldn't pass some test cases without long casting
			//because the pivot is declared int and might overflow with the larg numbers
			pivotSquared = (long)pivot * pivot;
			if(pivotSquared > x) { //then go left direction
				right = pivot - 1;
			}else if(pivotSquared < x) { // go right
				left = pivot + 1;
			}else { // pivotSquared == x --> the solution
				return pivot;
			}
		}
		 
		// why return right and not left
		// because if the loop would not end without returning the solution (line: 32)
		// then it would end with the state of left > rigth, 
		// for example: sqrt(10) let's dry-run it until the last iteration: 
		// iteration 1 : 
		// left = 2, right = 5, pivot = 3, pivotSquared = 9 which is less than 10 --> go right
		// iteration 2 :
		// left = 3, rigth = 5, pivot = 4, pivotSquared = 16 which is greater than 10 --> go left
		// iteration 3:
		// left = 3, right = 4, pivot = 3, pivotSquared = 9 which is less than 10 --> go right
		// iteration 4:
		// left = 4, right = 4, pivot = 4, pivotSquared = 16 which is greater than 10 --> go left
		// iteration 5: the loop would end since
		// left = 4, right = 3 --> left > right
		// so, right = 3 --> the solution 
		return right;
	}
	
	
	@Test
	public void test_mySqrt() {
		assertEquals(mySqrt(10), 3);
		assertEquals(mySqrt(4), 2);
		assertEquals(mySqrt(25), 5);
	}
	
	
	
}
