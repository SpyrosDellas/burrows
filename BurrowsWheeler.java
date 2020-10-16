import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.Arrays;

/**
 * Implementation of the Burrows-Wheeler transform and its inverse.
 */
public class BurrowsWheeler {

    private static final int RADIX = 256;

    /**
     * Do not instantiate
     */
    private BurrowsWheeler() {

    }

    /**
     * Apply the Burrows-Wheeler transform, reading from standard input and
     * writing to standard output.
     * <p>
     * - The running time is proportional to n + R (or better) in the worst case,
     * excluding the time to construct the circular suffix array.
     * <p>
     * - The amount of memory used is proportional to n + R (or better) in the worst case.
     * <p>
     * The goal of the Burrows–Wheeler transform is not to compress a message, but
     * rather to transform it into a form that is more amenable for compression.
     * It rearranges the characters in the input so that there are lots of clusters
     * with repeated characters, but in such a way that it is still possible to recover
     * the original input.
     * It relies on the following intuition: if you see the letters hen in English
     * text, then, most of the time, the letter preceding it is either t or w. If
     * you could somehow group all such preceding letters together (mostly t’s and
     * some w’s), then you would have a propitious opportunity for data compression.
     * <p>
     * The Burrows–Wheeler transform of a {@code String} s of length n is defined
     * as follows:
     * Consider the result of sorting the n circular suffixes of s.
     * The Burrows–Wheeler transform is the last column in the sorted suffixes
     * array <i>t[]</i>, preceded by the row number <i>first</i> in which the
     * original string ends up.
     */
    public static void transform() {
        String s = BinaryStdIn.readString();
        CircularSuffixArray suffixArray = new CircularSuffixArray(s);
        int first = 0;
        while (suffixArray.index(first) != 0)
            first++;
        BinaryStdOut.write(first, 32);
        for (int i = 0; i < first; i++) {
            BinaryStdOut.write(s.charAt(suffixArray.index(i) - 1));
        }
        BinaryStdOut.write(s.charAt(s.length() - 1));
        for (int i = first + 1; i < s.length(); i++) {
            BinaryStdOut.write(s.charAt(suffixArray.index(i) - 1));
        }
        BinaryStdOut.close();
    }

    /**
     * Apply the Burrows-Wheeler inverse transform, reading from standard input
     * and writing to standard output.
     * <p>
     * - The running time is proportional to n + R (or better) in the worst case.
     * <p>
     * - The amount of memory used is proportional to n + R (or better) in the
     * worst case.
     * <p>
     * Now, we describe how to invert the Burrows–Wheeler transform and recover
     * the original input string. If the nth original suffix is the ith row
     * in the sorted order, we define next[i] to be the row in the sorted order
     * where the (n + 1) original suffix appears. For example, if first is the
     * row in which the original input string appears, then next[first] is the
     * row in the sorted order where the 1st original suffix appears; next[next[first]]
     * is the row in the sorted order where the 2nd original suffix appears; and so forth
     * <p>
     * Inverting the message given the Burrows-Wheeler transform array btw[],
     * the index 'start' of the original text in the ordered array and
     * the next[] array:
     * <p>
     * The input to the Burrows–Wheeler decoder is the last column bwt[] of the
     * sorted suffixes along with first.
     * From bwt[], we can deduce the ordered array,which is the first column of
     * the sorted suffixes because it consists of precisely the same characters
     * contained in bwt[], but in sorted order.
     * We can also deduce next[], which is the ordered array, but instead of the
     * characters themselves it contains their indices in bwt[].
     * Now we can recover the first character of the input string, which is
     * bwt[next[first]]. The second character is bwt[next[next[first]]] and
     * so forth.
     */
    public static void inverseTransform() {
        int first = BinaryStdIn.readInt();
        char[] bwt = BinaryStdIn.readString().toCharArray();
        int length = bwt.length;

        int[] next = next(bwt);

        int index = first;
        for (int counter = 0; counter < length; counter++) {
            char letter = bwt[next[index]];
            BinaryStdOut.write(letter);
            index = next[index];
        }
        BinaryStdOut.close();
    }

    /**
     * Count the frequencies of appearance of each character in the given array
     * and calculate the corresponding starting indices in the sorted array.
     *
     * @param bwt The input {@code char[]} array
     * @return An array containing the starting indices of each alphabet
     * character in the sorted input array
     */
    private static int[] count(char[] bwt) {
        int[] count = new int[RADIX + 1];
        for (char c : bwt)
            count[c]++;
        count[RADIX - 1] = bwt.length - count[RADIX - 1];
        for (int i = RADIX - 2; i >= 0; i--) {
            count[i] = count[i + 1] - count[i];
        }
        return count;
    }

    /**
     * Counting sort the given array, with one key difference:
     * We populate the sorted array not with the characters themselves, but with
     * their indices in the given array.
     *
     * @param bwt The input {@code char[]} array
     * @return The modified sorted array
     */
    private static int[] next(char[] bwt) {
        int[] next = new int[bwt.length];
        int[] count = count(bwt);
        for (int index = 0; index < bwt.length; index++) {
            char c = bwt[index];
            next[count[c]++] = index;
        }
        return next;
    }

    private static void test() {
        // String input = "ABRACADABRA!";
        String input = "a";
        System.out.println(input);
        transformTest(input);
        System.out.println();
        /*
        char[] message = {
                3, 0x41, 0x52, 0x44, 0x21, 0x52, 0x43, 0x41, 0x41, 0x41, 0x41, 0x42, 0x42
        };

         */
        char[] message = { 0, 0x61 };
        inverseTransformTest(message);
    }

    private static void transformTest(String s) {
        CircularSuffixArray suffixArray = new CircularSuffixArray(s);
        int first = 0;
        while (suffixArray.index(first) != 0)
            first++;
        System.out.printf("%02x ", first >>> 24 & 0xff);
        System.out.printf("%02x ", first >>> 16 & 0xff);
        System.out.printf("%02x ", first >>> 8 & 0xff);
        System.out.printf("%02x ", first & 0xff);
        for (int i = 0; i < first; i++) {
            System.out.printf("%02x ", s.charAt(suffixArray.index(i) - 1) & 0xff);
        }
        System.out.printf("%02x ", s.charAt(s.length() - 1) & 0xff);
        for (int i = first + 1; i < s.length(); i++) {
            System.out.printf("%02x ", s.charAt(suffixArray.index(i) - 1) & 0xff);
        }
    }

    private static void inverseTransformTest(char[] message) {
        int first = message[0];
        char[] bwt = new char[message.length - 1];
        System.arraycopy(message, 1, bwt, 0, message.length - 1);
        int length = bwt.length;

        System.out.println("bwt = " + Arrays.toString(bwt));

        int[] next = next(bwt);
        System.out.println("next = " + Arrays.toString(next));

        int index = first;
        for (int counter = 0; counter < length; counter++) {
            char letter = bwt[next[index]];
            System.out.print(letter + " ");
            index = next[index];
        }
    }

    /**
     * If {@code args[0]} is "-", apply Burrows-Wheeler transform.
     * <p>
     * If {@code args[0]} is "+", apply Burrows-Wheeler inverse transform
     *
     * @param args A single argument; either '-' or '+'
     */
    public static void main(String[] args) {
        // test();
        if (args[0].compareTo("-") == 0)
            transform();
        else if (args[0].compareTo("+") == 0)
            inverseTransform();
    }

}
