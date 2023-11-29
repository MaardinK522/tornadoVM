package com.example.matrix;


public class Matrix2DType {
    protected final int ROWS;
    protected final int COLUMNS;

    Matrix2DType(int numRows, int numColumns) {
        this.ROWS = numRows;
        this.COLUMNS = numColumns;
    }

    public int getNumRows() {
        return this.ROWS;
    }

    public int getNumColumns() {
        return this.COLUMNS;
    }

    public int getFinalIndexOfRange(int fromIndex) {
        return fromIndex + this.COLUMNS;
    }
}

