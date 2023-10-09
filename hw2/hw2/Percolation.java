package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private int[] openGrid;
    private WeightedQuickUnionUF fullGrid;
    private int size;
    private int openSites;
    public Percolation(int N) {
        if (N <= 0) {
            throw new java.lang.IllegalArgumentException("N must greater than or equal to 0");
        }
        openGrid = new int[N * N];
        fullGrid = new WeightedQuickUnionUF(N * N);
        size = N;
        openSites = 0;
    }

    private void outOfIndex(int row, int col) {
        if (row < 0 || col < 0 || row >= size || col >= size) {
            throw new java.lang.IndexOutOfBoundsException("the index out of range");
        }
    }

    public void open(int row, int col) {
        outOfIndex(row, col);
        int location = row * size + col;
        if (openGrid[location] == 0) {
            openGrid[location] = 1;
            openWater(row, col);
            openSites += 1;
        }
    }

    private void openWater(int row, int col) {
        int location = row * size + col;
        if (location > 0 && openGrid[location - 1] == 1
                && location % size != 0) {
            fullGrid.union(location, location - 1);
        }
        if (location < size * size - 1 && openGrid[location + 1] == 1
                && (location + 1) % size != 0) {
            fullGrid.union(location, location + 1);
        }
        if (row < size - 1) {
            if (openGrid[location + size] == 1) {
                fullGrid.union(location, location + size);
            }
        }
        if (row > 0) {
            if (openGrid[location - size] == 1) {
                fullGrid.union(location, location - size);
            }
        }
    }

    public boolean isOpen(int row, int col) {
        outOfIndex(row, col);
        int location = row * size + col;
        return openGrid[location] == 1;
    }

    public boolean isFull(int row, int col) {
        outOfIndex(row, col);
        int location = row * size + col;
        for (int i = 0; i < size; i++) {
            if (fullGrid.connected(i, location) && isOpen(0, i)) {
                return true;
            }
        }
        return false;
    }

    public int numberOfOpenSites() {
        return openSites;
    }

    public boolean percolates() {
        for (int i = 0; i < size; i++) {
            if (isFull(size - 1, i)) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        Percolation a = new Percolation(10);
        a.isFull(1, 0);
    }
}
