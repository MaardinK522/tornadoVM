package com.example.network;

import com.example.matrix.Matrix;
import uk.ac.manchester.tornado.api.ImmutableTaskGraph;
import uk.ac.manchester.tornado.api.TaskGraph;
import uk.ac.manchester.tornado.api.TornadoExecutionPlan;
import uk.ac.manchester.tornado.api.enums.DataTransferMode;
import com.example.matrix.MatrixExecutor;

public class NeuralNetwork {

    private int numberOfInputNode;
    private final int numberOfHiddenNode;
    private final int numberOfOutputNode;
    Matrix weightsIH;
    Matrix biasIH;
    Matrix weightsHO;
    Matrix biasHO;
    Matrix hiddenMatrix;
    Matrix outputMatrix;
    double learningRate;

    // Activation functions
    private static final ActivationFunctionMapper SIGMOID_ACTIVATION = ((row, column, x) -> 1.0 / (1.0 + Math.exp(-x)));
    private static final ActivationFunctionMapper TANH_ACTIVATION = ((row, column, x) -> (Math.exp(x) - Math.exp(-x)) / (Math.exp(x) + Math.exp(-x)));
    private static final ActivationFunctionMapper RELU_ACTIVATION = ((row, column, x) -> Math.max(0, x));

    // Derivatives of the activation functions.
    private static final ActivationFunctionMapper SIGMOID_ACTIVATION_DERIVATION = ((row, column, y) -> y * (1 - y));
    private static final ActivationFunctionMapper TANH_ACTIVATION_DERIVATION = ((row, column, y) -> 1 - (y * y));
    private static final ActivationFunctionMapper RELU_ACTIVATION_DERIVATION = ((row, column, y) -> (y < 0) ? 0 : 1);
    private TaskGraph networkTaskGraph;

    public NeuralNetwork(int numberOfInputNode, int numberOfHiddenNode, int numberOfOutputNode) {
        this.numberOfInputNode = numberOfInputNode;
        this.numberOfHiddenNode = numberOfHiddenNode;
        this.numberOfOutputNode = numberOfOutputNode;

        // Hidden layer
        this.weightsIH = new Matrix(this.numberOfHiddenNode, this.numberOfInputNode);
        this.biasIH = new Matrix(this.numberOfHiddenNode, 1);

        // Output layer
        this.weightsHO = new Matrix(this.numberOfOutputNode, this.numberOfHiddenNode);
        this.biasHO = new Matrix(this.numberOfOutputNode, 1);

        this.hiddenMatrix = new Matrix(this.numberOfHiddenNode, 1);
        this.outputMatrix = new Matrix(numberOfOutputNode, 1);

        this.learningRate = 0.01;

        this.networkTaskGraph = new TaskGraph("neural_network");
    }

    public double[] processInputs(double[] inputs) {
        if (inputs.length != this.numberOfInputNode)
            throw new RuntimeException("Number of inputs mismatch: " + inputs.length + " != " + this.numberOfInputNode);
        // Matrices for converting the input array into input Marix
        Matrix inputMatrixT = new Matrix(new double[][]{inputs});
        Matrix inputMatrix = new Matrix(inputMatrixT.getNumColumns(), inputMatrixT.getNumRows());
        // Creating the executing strategies for GPU.
        this.networkTaskGraph = this.networkTaskGraph
                .transferToDevice(DataTransferMode.EVERY_EXECUTION, inputMatrixT, inputMatrix, this.weightsIH, this.biasIH, this.hiddenMatrix, this.weightsHO, this.biasHO, this.outputMatrix)
                .task("input_transpose", MatrixExecutor::matrixTranspose, inputMatrixT, inputMatrix)
                .task("weight_product", MatrixExecutor::matrixMultiplication, inputMatrix, this.weightsIH, this.hiddenMatrix)
                .task("adding_bias", MatrixExecutor::matrixAddition, this.hiddenMatrix, this.biasIH, this.hiddenMatrix)
                .task("applying_activation", MatrixExecutor::matrixToActivationFunctionMapper, this.hiddenMatrix, SIGMOID_ACTIVATION)
                .task("hidden_weights_product", MatrixExecutor::matrixMultiplication, this.hiddenMatrix, this.weightsHO, this.outputMatrix)
                .task("adding_bias", MatrixExecutor::matrixAddition, this.outputMatrix, this.biasHO, this.outputMatrix)
                .task("output_activation", MatrixExecutor::matrixToActivationFunctionMapper, this.outputMatrix, SIGMOID_ACTIVATION)
                .transferToHost(DataTransferMode.EVERY_EXECUTION, this.outputMatrix);

        ImmutableTaskGraph itg = this.networkTaskGraph.snapshot();
        TornadoExecutionPlan tornadoExecutionPlan = new TornadoExecutionPlan(itg);
        tornadoExecutionPlan.execute();
        return this.outputMatrix.column(0).getArray();
    }
}
