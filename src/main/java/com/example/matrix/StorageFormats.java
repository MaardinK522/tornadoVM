package com.example.matrix;


public final class StorageFormats {
    private StorageFormats() {
    }

    public static int toColumnMajor(int i, int j, int ld) {
        return j * ld + i;
    }

    public static int toRowMajor(int i, int j, int yMax) {
        return i * yMax + j;
    }

    public static int toRowMajorVector(int i, int j, int numColumns, int vectorElements) {
        return i * numColumns * vectorElements + j;
    }

    public static int toRowMajor3D(int i, int j, int k, int zMax, int yMax) {
        return i * zMax * yMax + j * zMax + k;
    }

    public static int toRowMajor3DVector(int i, int j, int k, int zSize, int ySize, int vectorWidth) {
        return i * zSize * ySize * vectorWidth + j * zSize + k;
    }

    public static int toRowMajor(int i, int j, int ld, int el) {
        return i * ld + j * el;
    }

    public static int toRowMajor(int i, int j, int k, int ld1, int ld2, int el) {
        return toRowMajor(i, j, ld1, el) + k * ld2;
    }

    public static int toRowMajor(int i, int j, int incm, int incn, int ld) {
        return i * ld * incn + j * incm;
    }

    public static int toFortran(int i, int j, int ld) {
        return (j - 1) * ld + (i - 1);
    }

    public static double[] toRowMajor(double[][] matrix) {
        int cols = matrix[0].length;
        int rows = matrix.length;
        double[] flattenMatrix = new double[rows * cols];

        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols; ++j) {
                flattenMatrix[toRowMajor(i, j, cols)] = matrix[i][j];
            }
        }

        return flattenMatrix;
    }

    public static float[] toRowMajor(float[][] matrix) {
        int M = matrix.length;
        int N = matrix[0].length;
        float[] flattenMatrix = new float[M * N];

        for (int i = 0; i < M; ++i) {
            for (int j = 0; j < N; ++j) {
                flattenMatrix[toRowMajor(i, j, N)] = matrix[i][j];
            }
        }

        return flattenMatrix;
    }

    public static float[] toRowMajor3D(float[][][] matrix) {
        int X = matrix.length;
        int Y = matrix[0].length;
        int Z = matrix[0][0].length;
        float[] flattenMatrix = new float[X * Y * Z];

        for (int i = 0; i < X; ++i) {
            for (int j = 0; j < Y; ++j) {
                for (int k = 0; k < Z; ++k) {
                    int index = toRowMajor3D(i, j, k, Z, Y);
                    flattenMatrix[index] = matrix[i][j][k];
                }
            }
        }

        return flattenMatrix;
    }

    public static int[] toRowMajor(int[][] matrix) {
        int M = matrix.length;
        int N = matrix[0].length;
        int[] matrixRM = new int[M * N];

        for (int i = 0; i < M; ++i) {
            for (int j = 0; j < N; ++j) {
                matrixRM[toRowMajor(i, j, N)] = matrix[i][j];
            }
        }

        return matrixRM;
    }

    public static byte[] toRowMajor(byte[][] matrix) {
        int m = matrix[0].length;
        int n = matrix.length;
        byte[] matrixRM = new byte[m * n];

        for (int i = 0; i < m; ++i) {
            for (int j = 0; j < n; ++j) {
                matrixRM[toRowMajor(i, j, m)] = matrix[i][j];
            }
        }

        return matrixRM;
    }
}

