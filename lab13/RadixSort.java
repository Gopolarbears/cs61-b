import java.util.Arrays;


/**
 * Class for doing Radix sort
 *
 * @author Akhil Batra, Alexander Hwang
 *
 */
public class RadixSort {
    /**
     * Does LSD radix sort on the passed in array with the following restrictions:
     * The array can only have ASCII Strings (sequence of 1 byte characters)
     * The sorting is stable and non-destructive
     * The Strings can be variable length (all Strings are not constrained to 1 length)
     *
     * @param asciis String[] that needs to be sorted
     *
     * @return String[] the sorted array
     */
    private static int maxLength = 0;
    public static String[] sort(String[] asciis) {
        for (String ascii : asciis) {
            maxLength = Math.max(ascii.length(), maxLength);
        }

        String[] asciis1 = Arrays.copyOf(asciis, asciis.length);
        for (int i = 0; i < maxLength; i++) {
            sortHelperLSD(asciis1, i);
        }

        return asciis1;
    }

    /**
     * LSD helper method that performs a destructive counting sort the array of
     * Strings based off characters at a specific index.
     * @param asciis Input array of Strings
     * @param index The position to sort the Strings on.
     */
    private static void sortHelperLSD(String[] asciis, int index) {
        // Optional LSD helper method for required LSD radix sort
        int[] count = new int[256];
        for (int i = 0; i < asciis.length; i++) {
            if (asciis[i].length() < maxLength - index) {
                count[(int) '_']++;
            } else {
                count[(int) asciis[i].charAt(maxLength - index - 1)]++;
            }
        }

        int[] start = new int[256];
        int pos = 0;
        for (int i = 0; i < start.length; i++) {
            start[i] = pos;
            pos += count[i];
        }

        String[] sorted = new String[asciis.length];
        for (int i = 0; i < sorted.length; i++) {
            int item;
            if (asciis[i].length() < maxLength - index) {
                item = (int) '_';
                pos = start[(int) '_'];
            } else {
                item = (int) asciis[i].charAt(maxLength - index - 1);
                pos = start[(int) asciis[i].charAt(maxLength - index - 1)];
            }
            sorted[pos] = asciis[i];
            start[item]++;
        }

        for (int i = 0; i < sorted.length; i++) {
            asciis[i] = sorted[i];
        }
        return;
    }

    /**
     * MSD radix sort helper function that recursively calls itself to achieve the sorted array.
     * Destructive method that changes the passed in array, asciis.
     *
     * @param asciis String[] to be sorted
     * @param start int for where to start sorting in this method (includes String at start)
     * @param end int for where to end sorting in this method (does not include String at end)
     * @param index the index of the character the method is currently sorting on
     *
     **/
    private static void sortHelperMSD(String[] asciis, int start, int end, int index) {
        // Optional MSD helper method for optional MSD radix sort
        return;
    }
}
