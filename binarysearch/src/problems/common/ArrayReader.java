package problems.common;

/*
 * trying to simulate the interface needed for solving the problem (Search in sorted array of unknown size)
 * to run the test here, it's ofcourse implemented by leetcode platform behind the scene
 */
public class ArrayReader {
	public int[] nums;
	
 public int get(int index) {
		 if(nums == null || nums.length == 0) {
			 return -1;
		 }
		 //array index out of bound
		 if(index >= 10000) {
			 return 2147483647;
		 }
		 
		return index < nums.length ? nums[index] : 2147483647; 
	 }

}
