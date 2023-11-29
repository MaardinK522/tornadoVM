package com.example.matrix;

import com.example.network.ActivationFunctionMapper;
import uk.ac.manchester.tornado.api.annotations.Parallel;

public class MatrixExecutor {
    public static void matrixAddition(Matrix matrix1, Matrix matrix2, Matrix matrix3) {
        for (@Parallel int a = 0; a < matrix3.getNumRows(); a++)
            for (@Parallel int b = 0; b < matrix3.getNumColumns(); b++)
                matrix3.set(a, b, matrix1.get(a, b) + matrix2.get(a, b));
    }

    public static void matrixSubtraction(Matrix matrix1, Matrix matrix2, Matrix matrix3) {
        for (@Parallel int a = 0; a < matrix3.getNumRows(); a++)
            for (@Parallel int b = 0; b < matrix3.getNumColumns(); b++)
                matrix3.set(a, b, matrix1.get(a, b) - matrix2.get(a, b));
    }

    public static void matrixTranspose(Matrix matrix, Matrix result) {
        for (@Parallel int a = 0; a < result.getNumRows(); a++)
            for (@Parallel int b = 0; b < result.getNumColumns(); b++)
                result.set(a, b, matrix.get(b, a));
    }

    public static void ElementWiseMultiplication(Matrix matrix1, Matrix matrix2, Matrix matrix3) {
        for (@Parallel int a = 0; a < matrix3.getNumRows(); a++)
            for (@Parallel int b = 0; b < matrix3.getNumColumns(); b++)
                matrix3.set(a, b, matrix1.get(a, b) * matrix2.get(a, b));

    }

    public static void scaleMatrix(Matrix matrix, double scaleFactor) {
        for (@Parallel int a = 0; a < matrix.getNumRows(); a++)
            for (@Parallel int b = 0; b < matrix.getNumColumns(); b++)
                matrix.set(a, b, matrix.get(a, b) * scaleFactor);
    }

    public static void matrixMultiplication(Matrix matrix1, Matrix matrix2, Matrix matrix3) {
        for (@Parallel int a = 0; a < matrix3.getNumRows(); a++)
            for (@Parallel int b = 0; b < matrix3.getNumColumns(); b++) {
                double sum = 0;
                for (int k = 0; k < matrix1.getNumColumns(); k++)
                    sum += matrix1.get(a, k) * matrix2.get(k, a);
                matrix3.set(a, b, sum);
            }
    }

    public static void matrixToActivationFunctionMapper(Matrix matrix, ActivationFunctionMapper mapper) {
        for (@Parallel int a = 0; a < matrix.getNumRows(); a++)
            for (@Parallel int b = 0; b < matrix.getNumColumns(); b++) {
                double value = matrix.get(a, b);
                double activation = mapper.map(a, b, value);
                matrix.set(a, b, activation);
            }
    }
}