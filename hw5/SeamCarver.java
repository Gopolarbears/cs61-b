import edu.princeton.cs.algs4.Picture;

import java.awt.Color;

public class SeamCarver {
    private Picture picture;
    private int[][] energy;
    private int[][] R;
    private int[][] G;
    private int[][] B;

    public SeamCarver(Picture picture) {
        this.picture = new Picture(picture);
        energy = new int[picture.width()][picture.height()];
        rgbInitial();
        energyInitial();
    }

    public Picture picture() {
        return new Picture(picture);
    }

    public int width() {
        return this.picture.width();
    }

    public int height() {
        return this.picture.height();
    }

    public double energy(int x, int y) {
        return energy[x][y];
    }

    public int[] findHorizontalSeam() {
        int[][] transportEnergy = new int[picture.height()][picture.width()];
        for (int i = 0; i < width(); i++) {
            for (int j = 0; j < height(); j++) {
                transportEnergy[j][i] = energy[i][j];
            }
        }
        int[][] temp = energy;
        energy = transportEnergy;
        int[] path = findVerticalSeam();
        energy = temp;
        return path;
    }

    public int[] findVerticalSeam() {
        int[][] paths = new int[energy.length][energy[0].length];
        int[][] M = new int[energy.length][energy[0].length];

        for (int i = 0; i < energy.length; i++) {
            M[i][0] = energy[i][0];
        }

        for (int j = 1; j < energy[0].length; j++) {
            for (int i = 0; i < energy.length; i++) {
                int leftM = (i - 1) < 0 ? Integer.MAX_VALUE : M[i - 1][j - 1];
                int middleM = M[i][j - 1];
                int rightM = (i + 1) >= energy.length ? Integer.MAX_VALUE : M[i + 1][j - 1];
                int lastM = Math.min(leftM, Math.min(middleM, rightM));
                M[i][j] = lastM + energy[i][j];
                paths[i][j] = leftM == lastM ? i - 1 : middleM == lastM ? i : i + 1;
            }
        }

        int lastLineM = Integer.MAX_VALUE;
        int lastLineI = 0;
        for (int i = 0; i < energy.length; i++) {
            if (M[i][energy[0].length - 1] < lastLineM) {
                lastLineM = M[i][energy[0].length - 1];
                lastLineI = i;
            }
        }

        int[] path = new int[energy[0].length];
        path[energy[0].length - 1] = lastLineI;
        for (int j = energy[0].length - 2; j >= 0; j--) {
            path[j] = paths[path[j + 1]][j + 1];
        }

        return path;
    }

    public void removeHorizontalSeam(int[] seam) {
        for (int i = 0; i < seam.length; i++) {
            if (seam[i] < 0 || seam[i] >= height()) {
                throw new IndexOutOfBoundsException(seam[i]);
            }
        }
        if (seam.length != width()) {
            throw new IllegalArgumentException();
        }

        for (int i = 0; i < seam.length - 1; i++) {
            if (Math.abs(seam[i] - seam[i + 1]) > 1) {
                throw new IllegalArgumentException();
            }
        }
    }

    public void removeVerticalSeam(int[] seam) {
        for (int i = 0; i < seam.length; i++) {
            if (seam[i] < 0 || seam[i] >= width()) {
                throw new IndexOutOfBoundsException(seam[i]);
            }
        }
        if (seam.length != height()) {
            throw new IllegalArgumentException();
        }

        for (int i = 0; i < seam.length - 1; i++) {
            if (Math.abs(seam[i] - seam[i + 1]) > 1) {
                throw new IllegalArgumentException();
            }
        }
    }

    private void energyInitial() {
        energy = new int[width()][height()];

        for (int i = 0; i < picture.width(); i++) {
            for (int j = 0; j < picture.height(); j++) {
                int rx = R[i - 1 < 0 ? width() - 1 : i - 1][j]
                        - R[i + 1 >= picture.width() ? 0 : i + 1][j];
                int gx = G[i - 1 < 0 ? width() - 1 : i - 1][j]
                        - G[i + 1 >= picture.width() ? 0 : i + 1][j];
                int bx = B[i - 1 < 0 ? width() - 1 : i - 1][j]
                        - B[i + 1 >= picture.width() ? 0 : i + 1][j];
                int ry = R[i][j - 1 < 0 ? height() - 1 : j - 1]
                        - R[i][j + 1 >= picture.height() ? 0 : j + 1];
                int gy = G[i][j - 1 < 0 ? height() - 1 : j - 1]
                        - G[i][j + 1 >= picture.height() ? 0 : j + 1];
                int by = B[i][j - 1 < 0 ? height() - 1 : j - 1]
                        - B[i][j + 1 >= picture.height() ? 0 : j + 1];
                energy[i][j] = rx * rx + gx * gx + bx * bx + ry * ry + gy * gy + by * by;
            }
        }
    }

    private void rgbInitial() {
        R = new int[width()][height()];
        G = new int[width()][height()];
        B = new int[width()][height()];

        for (int i = 0; i < picture.width(); i++) {
            for (int j = 0; j < picture.height(); j++) {
                Color rgb = picture.get(i, j);
                R[i][j] = rgb.getRed();
                G[i][j] = rgb.getGreen();
                B[i][j] = rgb.getBlue();
            }
        }
    }
}
