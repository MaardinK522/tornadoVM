package com.example;

import com.example.matrix.Matrix;
import com.example.matrix.MatrixExecutor;
import com.example.network.NeuralNetwork;
import uk.ac.manchester.tornado.api.*;
import uk.ac.manchester.tornado.api.common.TornadoDevice;
import uk.ac.manchester.tornado.api.enums.DataTransferMode;
import uk.ac.manchester.tornado.api.runtime.TornadoRuntime;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello and welcome!");
//        testingMatrixLib();
        testingNeuralNetwork();
    }


    public static void testingNeuralNetwork() {
        NeuralNetwork neuralNetwork = new NeuralNetwork(2, 100, 10);

        System.out.println("Output of the network" + Arrays.toString(neuralNetwork.processInputs(new double[]{1, 1})));
    }

    public static void testingMatrixLib() {
        TornadoRuntimeInterface tornadoRuntime = TornadoRuntime.getTornadoRuntime();
        TornadoDevice tornadoDevice = tornadoRuntime.getDefaultDevice();
        System.out.println("Device name: " + tornadoDevice.getDeviceName());
        System.out.println("Device description: ");
        System.out.println(tornadoDevice.getDescription());

        int m = 4096;

        Matrix matrix1 = new Matrix(m, m);
        Matrix matrix2 = new Matrix(m, m);
        Matrix result = new Matrix(m, m);
        for (int a = 0; a < m; a++) {
            for (int b = 0; b < m; b++) {
                matrix1.set(a, b, 25);
                matrix2.set(a, b, 25);
                result.set(a, b, 25);
            }
        }

        System.out.println("Executing the Parallel task: ");

        executeParallelAddition(matrix1, matrix2, result);
    }

    public static void executeParallelAddition(Matrix matrix1, Matrix matrix2, Matrix result) {
        TaskGraph taskGraph = new TaskGraph("addition")
                .transferToHost(DataTransferMode.EVERY_EXECUTION, matrix1, matrix2)
                .task("matrix_addition", MatrixExecutor::matrixMultiplication, matrix1, matrix2, result)
                .transferToDevice(DataTransferMode.EVERY_EXECUTION, result);
        ImmutableTaskGraph immutableTaskGraph = taskGraph.snapshot();
        TornadoExecutionPlan tornadoExecutionPlan = new TornadoExecutionPlan(immutableTaskGraph);
        TornadoExecutionResult executionResult = tornadoExecutionPlan.execute();
        System.out.println("Transfer time: " + executionResult.getProfilerResult().getDataTransfersTime());
        System.out.println(result);
    }
}
