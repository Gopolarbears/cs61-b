package hw2;
import edu.princeton.cs.introcs.StdRandom;
import edu.princeton.cs.introcs.StdStats;

public class PercolationStats {
    private double[] x;
    private double mean;
    private double stddev;
    private double doubleT;
    public PercolationStats(int N, int T, PercolationFactory pf) {
        if (N <= 0 || T <= 0) {
            throw new java.lang.IllegalArgumentException("index error");
        }
        doubleT = T;
        Percolation oneTest;
        x = new double[N];

        for (int i = 0; i < T; i++) {
            oneTest = pf.make(N);
            while (!oneTest.percolates()) {
                oneTest.open(StdRandom.uniform(0, N), StdRandom.uniform(0, N));
            }
            x[i] = oneTest.numberOfOpenSites() / (double) (N * N);
        }
        mean = StdStats.mean(x);
        stddev = StdStats.stddev(x);
    }

    public double mean() {
        return mean;
    }

    public double stddev() {
        return stddev;
    }

    public double confidenceLow() {
        return (mean - 1.96 * stddev / Math.sqrt(doubleT));
    }

    public double confidenceHigh() {
        return (mean + 1.96 * stddev / Math.sqrt(doubleT));
    }

}
