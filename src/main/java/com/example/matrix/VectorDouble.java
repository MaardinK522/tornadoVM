package com.example.matrix;


import uk.ac.manchester.tornado.api.collections.math.TornadoMath;
import uk.ac.manchester.tornado.api.collections.types.PrimitiveStorage;

import java.nio.DoubleBuffer;
import java.util.Arrays;

public class VectorDouble implements PrimitiveStorage<DoubleBuffer> {
    private final int numElements;
    private final double[] storage;
    private static final int ELEMENT_SIZE = 1;

    protected VectorDouble(int numElements, double[] array) {
        this.numElements = numElements;
        this.storage = array;
    }

    public VectorDouble(int numElements) {
        this(numElements, new double[numElements]);
    }

    public VectorDouble(double[] storage) {
        this(storage.length, storage);
    }

    public double[] getArray() {
        return this.storage;
    }

    public double get(int index) {
        return this.storage[index];
    }

    public void set(int index, double value) {
        this.storage[index] = value;
    }

    public void set(VectorDouble values) {
        System.arraycopy(values.storage, 0, this.storage, 0, values.storage.length);
    }

    public void set(double[] values) {
        for (int i = 0; i < values.length; ++i) {
            this.storage[i] = values[i];
        }

    }

    public void fill(double value) {
        Arrays.fill(this.storage, value);

    }

    public VectorDouble subVector(int start, int length) {
        VectorDouble v = new VectorDouble(length);

        if (length >= 0) System.arraycopy(this.storage, start, v.storage, 0, length);

        return v;
    }

    public VectorDouble duplicate() {
        return new VectorDouble(Arrays.copyOf(this.storage, this.storage.length));
    }

    public static double min(VectorDouble v) {
        double result = Double.MAX_VALUE;

        for (int i = 0; i < v.storage.length; ++i) {
            result = Math.min(v.storage[i], result);
        }

        return result;
    }

    public static double max(VectorDouble v) {
        double result = Double.MIN_VALUE;

        for (int i = 0; i < v.storage.length; ++i) {
            result = Math.max(v.storage[i], result);
        }

        return result;
    }

    public boolean isEqual(VectorDouble vector) {
        return TornadoMath.isEqual(this.storage, vector.storage);
    }

    public static double dot(VectorDouble a, VectorDouble b) {
        double sum = 0.0;
        for (int i = 0; i < a.size(); ++i) sum += a.get(i) * b.get(i);
        return sum;
    }

    public String toString(String fmt) {
        StringBuilder sb = new StringBuilder("[");
        sb.append("[ ");

        for (int i = 0; i < this.numElements; ++i) {
            String var10001 = String.format(fmt, this.get(i));
            sb.append(var10001).append(" ");
        }

        sb.append("]");
        return sb.toString();
    }

    public String toString() {
        String str = String.format("VectorDouble <%d>", this.numElements);
        if (this.numElements < 32)
            str = str + this.toString("%.3f");
        return str;
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

    public int getLength() {
        return this.numElements;
    }
}
