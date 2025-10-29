package util;

public class PalindromeFinder {

    /**
     * Finds the longest palindromic substring in input String s using Manacher's Algorithm.
     *
     * @param s input string
     * @return longest palindrome substring
     */
    public static String longestPalindrome(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }

        // Preprocess string to handle even-length palindromes by inserting '#'
        char[] processed = preprocess(s);
        int[] p = new int[processed.length];
        int center = 0, right = 0;
        int maxLen = 0, centerIndex = 0;

        for (int i = 1; i < processed.length - 1; i++) {
            int mirror = 2 * center - i;

            if (i < right) {
                p[i] = Math.min(right - i, p[mirror]);
            }

            // Attempt to expand palindrome centered at i
            while (processed[i + (p[i] + 1)] == processed[i - (p[i] + 1)]) {
                p[i]++;
            }

            // Update center and right boundary if palindrome expanded past right
            if (i + p[i] > right) {
                center = i;
                right = i + p[i];
            }

            // Track max palindrome length and center position
            if (p[i] > maxLen) {
                maxLen = p[i];
                centerIndex = i;
            }
        }

        int start = (centerIndex - maxLen) / 2;  // Map back to original string indices
        return s.substring(start, start + maxLen);
    }

    // Helper to preprocess string by inserting boundaries ('#') between chars and adding sentinels
    private static char[] preprocess(String s) {
        char[] chars = new char[s.length() * 2 + 3];
        chars[0] = '^';
        int idx = 1;
        for (char c : s.toCharArray()) {
            chars[idx++] = '#';
            chars[idx++] = c;
        }
        chars[idx++] = '#';
        chars[idx++] = '$';
        return chars;
    }
}
