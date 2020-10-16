/**
 * Calculate the circular suffix array of a given {@code String} using the 3-way
 * string quicksort algorithm.
 * <p>
 * On typical English text the running time is n*log(n) or better.
 *
 * @author Spyros Dellas
 */
public class CircularSuffixArray {

    private final char[] s;   // the input string
    private final int length; // the length of the input string
    private int[] sa;         // the suffix array

    /**
     * Construct the suffix array of s
     *
     * @param text The input {@code String}
     * @throws IllegalArgumentException if the input {@code String} is {@code null}
     */
    public CircularSuffixArray(String text) {

        if (text == null)
            throw new IllegalArgumentException("Input string cannot be null");

        s = text.toCharArray();
        length = s.length;
        sa = new int[length];

        for (int i = 0; i < length; i++) {
            sa[i] = i;
        }

        // If the string is empty, no further action is required
        if (s.length == 0) return;

        sort(0, length - 1, 0);
    }

    // Sort sa[start..end] comparing characters positioned at an offset from the
    // starting index of each suffix
    private void sort(int start, int end, int offset) {
        if (end - start < 1 || offset >= length)
            return;

        char pivot = s[(sa[start] + offset) % length];
        int lo = start;
        int hi = end;
        int pointer = start;
        while (pointer <= hi) {
            char current = s[(sa[pointer] + offset) % length];
            if (current == pivot) {
                pointer++;
            }
            else if (current < pivot) {
                exchange(lo, pointer);
                pointer++;
                lo++;
            }
            else {
                while (hi > pointer && s[(sa[hi] + offset) % length] > pivot) {
                    hi--;
                }
                exchange(pointer, hi);
                hi--;
            }
        }

        sort(start, lo - 1, offset);
        sort(lo, hi, offset + 1);
        sort(hi + 1, end, offset);
    }

    private void exchange(int i, int j) {
        int buffer = sa[i];
        sa[i] = sa[j];
        sa[j] = buffer;
    }

    /**
     * @return The length of the input {@code String}
     */
    public int length() {
        return length;
    }

    /**
     * Returns the index of the ith sorted suffix
     *
     * @param i The ith sorted suffix
     * @return The index in the original string of the ith sorted suffix
     * @throws IllegalArgumentException if the argument {@code i} is outside
     *                                  its prescribed range (between 0 and n − 1)
     */
    public int index(int i) {
        if (i >= 0 && i < length)
            return sa[i];
        else
            throw new IllegalArgumentException("Parameter i out of range");
    }

    /**
     * Unit testing.
     * <p>
     * Calls each public method directly and verifies that they
     * work as prescribed (e.g., by printing results to standard output)
     */
    public static void main(String[] args) {
        String s = "ABABAABAAA";
        CircularSuffixArray suffixArray = new CircularSuffixArray(s);
        System.out.println("The length of the input string is: " + suffixArray.length());
        System.out.println("The suffix array is:");
        System.out.print("[ ");
        for (int i = 0; i < suffixArray.length(); i++) {
            System.out.print(suffixArray.index(i) + " ");
        }
        System.out.print("]");
    }
}
