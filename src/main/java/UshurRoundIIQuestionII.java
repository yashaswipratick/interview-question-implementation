package src.main.java;

import java.util.HashMap;

public class UshurRoundIIQuestionII {

    /*
    Given a string, find the length of the longest substring in it with no more than K distinct
    characters.
    You can assume that K is less than or equal to the length of the given string.

    Example 1:

    Input: String="araaci", K=2
    Output: 4
    Explanation: The longest substring with no more than '2' distinct characters is "araa".

    Example 2:

    Input: String="araaci", K=1
    Output: 2
    Explanation: The longest substring with no more than '1' distinct characters is "aa".

    Example 3:

    Input: String="cbbebi", K=3
    Output: 5
    Explanation: The longest substrings with no more than '3' distinct characters are "cbbeb" & "bbebi".
     */
    //TC - O(N)
    public static int getLongestSubstringLength(String s, int K) {
        int count = 0;
        int i = 0;
        int maxCount = 0;
        int j = 0;
        HashMap<Character, Integer> map = new HashMap<>();
        while (i < s.length()) {
            char c = s.charAt(i);
            if (map.size() < K) {
                map.put(c, map.getOrDefault(c, 0) + 1);
                count++;
            } else if (map.containsKey(c)) {
                map.put(c, map.getOrDefault(c, 0) + 1);
                count++;
            } else {
                count--;
                int KCount = K;
                while (map.size() == KCount) {
                    Integer charCount = map.get(s.charAt(j));
                    charCount -= 1;
                    if (charCount == 0) {
                        map.remove(s.charAt(j));
                    } else {
                        map.put(s.charAt(j), charCount);
                    }
                    j++;
                }
                map.put(c, map.getOrDefault(c, 0) + 1);
                count++;
            }
            maxCount = Math.max(count, maxCount);
            i++;
        }
        return maxCount;
    }
    public static void main(String[] args) {
        String s = "araaci";
        System.out.println(getLongestSubstringLength(s, 2));

        String s1 = "araaci";
        System.out.println(getLongestSubstringLength(s1, 1));

        String s2 = "cbbebi";
        System.out.println(getLongestSubstringLength(s2, 3));
    }
}
