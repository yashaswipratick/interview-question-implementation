package src.main.java;

public class RoundIIQuestionIII {

    /*
    Given an array of positive numbers and a positive number ‘k,’
    find the maximum sum of any contiguous subarray of size ‘k’.

    Example 1:

    Input: [2, 1, 5, 1, 3, 2], k=3
    Output: 9
    Explanation: Subarray with maximum sum is [5, 1, 3].

    point - contiguous subarray
    Example 2:

    Input: [2, 3, 4, 1, 5], k=2
    Output: 7
    Explanation: Subarray with maximum sum is [3, 4].
     */

    private static int getMaxSum(int[] nums, int K) {
        //Edge case
        if (nums.length == 0) {
            return 0;//TODO -  discuss what to return
        }

        //Edge cases
        if (K == 0) {
            return 0;//TODO -  discuss what to return
        }

        //Input: [2, 1, 5, 1, 3, 2], k=3
        //    Output: 9
        int maxSum = 0;
        int sum = 0;
        int KBkp = K;
        for (int i = 0; i < nums.length; i++) {
            //get window sum
            if (K > 0) {
                sum += nums[i];
                maxSum = Math.max(sum, maxSum);
                K--;
            } else {
                sum -= nums[i-KBkp];
                sum += nums[i];
                maxSum = Math.max(sum, maxSum);
            }
        }
        return maxSum;
    }

    public static void main(String[] args) {
        int[] nums = {2, 1, 5, 1, 3, 2};
        System.out.println(getMaxSum(nums, 3));

        int[] nums1 = {2, 3, 4, 1, 5};
        System.out.println(getMaxSum(nums1, 2));
    }
}
