package com.example.matrix;

import java.io.Serializable;
import java.nio.Buffer;

public interface PrimitiveStorage<T extends Buffer> extends Serializable {
    void loadFromBuffer(T var1);

    T asBuffer();

    int size();
}
