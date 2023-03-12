import edu.princeton.cs.algs4.Picture;

import java.util.Stack;

public class SeamCarver {
    private Picture p;
    private double[][] matrix;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        this.p = picture;
        double[][] a = new double[p.width()][p.height()];
        for (int col = 0; col < p.width(); col++)
            for (int row = 0; row < p.height(); row++)
                a[col][row] = energy(col, row);
        this.matrix = a;
    }

    // current picture
    public Picture picture() {
        return this.p;
    }

    // width of current picture
    public int width() {
        return this.p.width();
    }

    // height of current picture
    public int height() {
        return this.p.height();
    }

    // energy of pixel at column x and row y
    public double energy(int col, int row) {
        int x = compute_col_energy(col, row);
        int y = compute_row_energy(col, row);

        return Math.sqrt(y + x);
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        this.matrix = transposeMatrix(this.matrix);
        for (int row = 0; row < height(); row++) {
            for (int col = 0; col < width(); col++) {
                System.out.print(this.matrix[col][row] + " ");
            }
            System.out.println();
        }
        int[] horizontalSeam = findVerticalSeam();
        this.matrix = transposeMatrix(this.matrix);
        return horizontalSeam;
    }

    public static double[][] transposeMatrix(double[][] m) {
        double[][] temp = new double[m[0].length][m.length];
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[0].length; j++) {
                temp[j][i] = m[i][j];
            }
        }
        return temp;
    }


    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        int width = width();
        int height = height();
        int[] verticalSeam = new int[height()];
        double[][] dp = new double[width][height];

        int[][] from = new int[width][height];
        for (int col = 0; col < width(); ++col) {
            dp[col][0] = this.matrix[col][0];
        }

        for (int row = 1; row < height(); ++row) {
            for (int col = 0; col < width(); ++col) {
                double left = dp[Math.max(col - 1, 0)][row - 1];
                double up = dp[col][row - 1];
                double right = dp[Math.min(col + 1, width() - 1)][row - 1];

                if (left <= up && left <= right) {
                    from[col][row] = -1;
                } else if (up <= right && up <= left) {
                    from[col][row] = 0;
                } else {
                    from[col][row] = 1;
                }
                dp[col][row] = this.matrix[col][row] + Math.min(Math.min(left, up), right);
            }
        }

        double curr_min = Double.MAX_VALUE;
        int curr_col = 0;
        for (int col = 0; col < width(); ++col) {
            if (curr_min > dp[col][height() - 1]) {
                curr_min = dp[col][height() - 1];
                curr_col = col;
            }
        }

        int min_row = height() - 1;
        int min_col = curr_col;
        Stack<Integer> ans = new Stack<>();
        ans.push(curr_col);
        while (min_row != 0) {
            if (from[min_col][min_row] == -1) {
                min_row = min_row - 1;
                min_col = Math.max(0, min_col - 1);
                ans.push(min_col);
            } else if (from[min_col][min_row] == 0) {
                min_row = min_row - 1;
                ans.push(min_col);
            } else {
                min_row = min_row - 1;
                min_col = Math.min(width() - 1, min_col + 1);
                ans.push(min_col);
            }
        }

        int i = 0;
        while (!ans.empty()) {
            verticalSeam[i] = ans.pop();
            i += 1;
        }
        return verticalSeam;
    }


    // energy of pixel at column x and row y
    private int compute_row_energy(int x, int y) {
        int left = fixRow(y - 1);
        int right = fixRow(y + 1);

        int l_rgb = this.p.getRGB(x, left);
        int r_rgb = this.p.getRGB(x, right);

        return compute_rgb(l_rgb, r_rgb);
    }


    // energy of pixel at column x and row y
    private int compute_col_energy(int x, int y) {
        int up = fixCol(x - 1);
        int down = fixCol(x + 1);

        int up_rgb = this.p.getRGB(up, y);
        int down_rgb = this.p.getRGB(down, y);

        return compute_rgb(up_rgb, down_rgb);
    }

    private int compute_rgb(int x, int y) {
        int r = Math.abs(unpack_red(x) - unpack_red(y));
        int g = Math.abs(unpack_green(x) - unpack_green(y));
        int b = Math.abs(unpack_blue(x) - unpack_blue(y));

        return r * r + g * g + b * b;
    }

    private int fixRow(int row) {
        if (row == -1) {
            return height() - 1;
        }
        if (row == height()) {
            return 0;
        }
        return row;
    }

    private int fixCol(int col) {
        if (col == -1) {
            return width() - 1;
        }
        if (col == width()) {
            return 0;
        }
        return col;
    }


    private int unpack_red(int rgb) {
        return (rgb >> 16) & 0xFF;
    }

    private int unpack_green(int rgb) {
        return (rgb >> 8) & 0xFF;
    }

    private int unpack_blue(int rgb) {
        return rgb & 0xFF;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        Picture newPic = new Picture(width(), height() - 1);

        for (int col = 0; col < width(); col++)
            for (int row = 0; row < height() - 1; row++) {

                if (row < seam[col])
                    newPic.set(col, row, p.get(col, row));
                else
                    newPic.set(col, row, p.get(col, row + 1));

            }

        p = new Picture(newPic);
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        Picture newPic = new Picture(width() - 1, height());

        for (int row = 0; row < height(); row++)
            for (int col = 0; col < width() - 1; col++) {

                if (col < seam[row])
                    newPic.set(col, row, p.get(col, row));
                else
                    newPic.set(col, row, p.get(col + 1, row));

            }

        p = new Picture(newPic);
    }
}
