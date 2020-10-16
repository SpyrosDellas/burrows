import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

/**
 * The main idea of move-to-front encoding is to maintain an ordered sequence of
 * the characters in the alphabet by repeatedly reading a character from the
 * input message; saving the position in the sequence in which that character
 * appears; and moving that character to the front of the sequence.
 * <p>
 * If equal characters occur near one another other many times in the input,
 * then many of the output values will be small integers (such as 0, 1, and 2).
 * The resulting high frequency of certain characters (0s, 1s, and 2s) provides
 * exactly the kind of input for which Huffman coding achieves favorable
 * compression ratios.
 * <p>
 * Performance:
 * The running time of both move-to-front encoding and decoding is proportional
 * to n*R (or better) in the worst case and proportional to n+R (or better) on
 * inputs that arise when compressing typical English text, where n is the
 * number of characters in the input and R is the alphabet size.
 * The amount of memory used by both move-to-front encoding and decoding is
 * proportional to n + R (or better) in the worst case.
 *
 * @author Spyros Dellas
 */
public class MoveToFront {

    private static final int EXTENDED_ASCII = 256;
    private static final int BITS_PER_BYTE = 8;

    /**
     * Do not instantiate
     */
    private MoveToFront() {

    }

    /**
     * Apply move-to-front encoding, reading from standard input and writing
     * to standard output
     * <p>
     * We maintain an ordered sequence of the 256 extended ASCII characters.
     * Initialize the sequence by making the ith character in the sequence equal
     * to the ith extended ASCII character. Now, read each 8-bit character c
     * from standard input, one at a time; output the 8-bit index in the sequence
     * where c appears; and move c to the front.
     */
    public static void encode() {
        char[] symbols = initializeSymbols();
        int position;
        while (!BinaryStdIn.isEmpty()) {
            char symbol = BinaryStdIn.readChar();
            position = 0;
            while (symbols[position] != symbol) {
                position++;
            }
            BinaryStdOut.write(position, BITS_PER_BYTE);
            System.arraycopy(symbols, 0, symbols, 1, position);
            symbols[0] = symbol;
        }
        BinaryStdOut.close();
    }

    private static char[] initializeSymbols() {
        char[] codes = new char[EXTENDED_ASCII];
        for (char symbol = 0; symbol < EXTENDED_ASCII; symbol++) {
            codes[symbol] = symbol;
        }
        return codes;
    }

    /**
     * Apply move-to-front decoding, reading from standard input and writing to
     * standard output
     */
    public static void decode() {
        char[] symbols = initializeSymbols();
        int position;
        while (!BinaryStdIn.isEmpty()) {
            position = BinaryStdIn.readChar();
            char symbol = symbols[position];
            BinaryStdOut.write(symbol, BITS_PER_BYTE);
            System.arraycopy(symbols, 0, symbols, 1, position);
            symbols[0] = symbol;
        }
        BinaryStdOut.close();
    }

    private static void test() {
        String input = "ABRACADABRA!";
        System.out.println(input);
        encodeTest(input);
        System.out.println();
        char[] encoded = { 0x41, 0x42, 0x52, 0x02, 0x44, 0x01, 0x45, 0x01, 0x04, 0x04, 0x02, 0x26 };
        decodeTest(encoded);
    }

    private static void encodeTest(String input) {
        char[] symbols = initializeSymbols();
        int position;
        for (int i = 0; i < input.length(); i++) {
            char symbol = input.charAt(i);
            position = 0;
            while (symbols[position] != symbol) {
                position++;
            }
            System.out.printf("%02x ", position);
            System.arraycopy(symbols, 0, symbols, 1, position);
            symbols[0] = symbol;
        }
    }

    private static void decodeTest(char[] input) {
        char[] symbols = initializeSymbols();
        int position;
        for (int i = 0; i < input.length; i++) {
            position = input[i];
            char symbol = symbols[position];
            System.out.print(symbol);
            System.arraycopy(symbols, 0, symbols, 1, position);
            symbols[0] = symbol;
        }
    }

    /**
     * If {@code args[0]} is "-", apply move-to-front encoding.
     * <p>
     * If {@code args[0]} is "+", apply move-to-front decoding.
     *
     * @param args A single argument; either '-' or '+'
     */
    public static void main(String[] args) {
        // test();
        if (args[0].compareTo("-") == 0)
            encode();
        else if (args[0].compareTo("+") == 0)
            decode();
    }
}
