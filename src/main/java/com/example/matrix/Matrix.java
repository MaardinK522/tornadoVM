package com.example.matrix;


import java.nio.DoubleBuffer;
import java.util.Arrays;

public class Matrix extends Matrix2DType implements PrimitiveStorage<DoubleBuffer> {
    protected final double[] storage;
    private final int numElements;

    public Matrix(int rows, int columns, double[] array) {
        super(rows, columns);
        this.storage = array;
        this.numElements = columns * rows;
    }

    public Matrix(int rows, int columns) {
        this(rows, columns, new double[rows * columns]);
    }

    public Matrix(double[][] matrix) {
        this(matrix.length, matrix[0].length, StorageFormats.toRowMajor(matrix));
    }

    public double get(int i, int j) {
        return this.storage[StorageFormats.toRowMajor(i, j, this.COLUMNS)];
    }

    public void set(int i, int j, double value) {
        this.storage[StorageFormats.toRowMajor(i, j, this.COLUMNS)] = value;
    }

    public VectorDouble row(int row) {
        int index = StorageFormats.toRowMajor(row, 0, this.COLUMNS);
        return new VectorDouble(this.COLUMNS, Arrays.copyOfRange(this.storage, index, this.getFinalIndexOfRange(index)));
    }

    public VectorDouble column(int col) {
        int index = StorageFormats.toRowMajor(0, col, this.COLUMNS);
        VectorDouble v = new VectorDouble(this.ROWS);

        for (int i = 0; i < this.ROWS; ++i) {
            v.set(i, this.storage[index + i * this.COLUMNS]);
        }

        return v;
    }

    public VectorDouble diag() {
        VectorDouble v = new VectorDouble(Math.min(this.ROWS, this.COLUMNS));

        for (int i = 0; i < this.ROWS; ++i) {
            v.set(i, this.storage[i * (this.COLUMNS + 1)]);
        }

        return v;
    }

    public void fill(double value) {
        for (int i = 0; i < this.storage.length; ++i) {
            this.storage[i] = value;
        }

    }

    public void multiply(uk.ac.manchester.tornado.api.collections.types.Matrix2DDouble a, uk.ac.manchester.tornado.api.collections.types.Matrix2DDouble b) {
        for (int row = 0; row < this.getNumRows(); ++row) {
            for (int col = 0; col < this.getNumColumns(); ++col) {
                double sum = 0.0;

                for (int k = 0; k < b.getNumRows(); ++k) {
                    sum += a.get(row, k) * b.get(k, col);
                }

                this.set(row, col, sum);
            }
        }

    }

    public static void transpose(uk.ac.manchester.tornado.api.collections.types.Matrix2DDouble matrix) {
        if (matrix.getNumColumns() == matrix.getNumColumns()) {
            for (int i = 0; i < matrix.getNumRows(); ++i) {
                for (int j = 0; j < i; ++j) {
                    double tmp = matrix.get(i, j);
                    matrix.set(i, j, matrix.get(j, i));
                    matrix.set(j, i, tmp);
                }
            }
        }

    }

    public Matrix duplicate() {
        Matrix matrix = new Matrix(this.ROWS, this.COLUMNS);
        matrix.set(this);
        return matrix;
    }

    public void set(Matrix m) {
        for (int i = 0; i < m.storage.length; ++i) {
            this.storage[i] = m.storage[i];
        }

    }

    public String toString(String fmt) {
        StringBuilder str = new StringBuilder();

        for (int i = 0; i < this.ROWS; ++i) {
            for (int j = 0; j < this.COLUMNS; ++j) {
                String var10001 = String.format(fmt, this.get(i, j));
                str.append(var10001 + " ");
            }

            str.append("\n");
        }

        return str.toString().trim();
    }

    public String toString() {
        String result = String.format("MatrixDouble <%d x %d>", this.ROWS, this.COLUMNS);
        if (this.ROWS < 16 && this.COLUMNS < 16) {
            result = result + "\n" + this.toString("%.3f");
        }

        return result;
    }

    public static void scale(Matrix matrix, double value) {
        for (int i = 0; i < matrix.storage.length; ++i) {
            matrix.storage[i] *= value;
        }
    }

    public void loadFromBuffer(DoubleBuffer buffer) {
        this.asBuffer().put(buffer);
    }

    public DoubleBuffer asBuffer() {
        return DoubleBuffer.wrap(this.storage);
    }

    public int size() {
        return this.numElements;
    }
}
