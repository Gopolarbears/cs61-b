package hw3.hash;

import java.util.List;

public class OomageTestUtility {
    public static boolean haveNiceHashCodeSpread(List<Oomage> oomages, int M) {
        /*
         * Write a utility function that returns true if the given oomages
         * have hashCodes that would distribute them fairly evenly across
         * M buckets. To do this, convert each oomage's hashcode in the
         * same way as in the visualizer, i.e. (& 0x7FFFFFFF) % M.
         * 2.5 Oand ensure that no bucket has fewer than N / 50
         * Oomages and no bucket has more than N / omages.
         */
        int[] buckets = new int[M];
        int hashNum;
        int downSize = oomages.size() / 50;
        double upSize = oomages.size() / 2.5;

        for (Oomage o: oomages) {
            hashNum = (o.hashCode() & 0x7FFFFFFF) % M;
            buckets[hashNum]++;
        }
        for (int i = 0; i < M; i++) {
            if (buckets[i] < downSize || buckets[i] > upSize) {
                return false;
            }
        }
        return true;
    }
}
