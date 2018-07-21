package neuralnetwork;

/**
 *
 * @author tnc
 */
public class Layer
{
    private NeuralNetwork network;
    
    private Layer inputLayer;
    private Layer outputLayer;
    
    private Matrix input;
    private Matrix output;
    private Matrix output_d_E;
    
    private Matrix weights;
    private Matrix biases;
    
    private ActivationType activationType;
    
    public Layer(int inputNeurons, int nextLayerNeurons, ActivationType activationType)
    {
        this.activationType = activationType;
        weights = new Matrix(nextLayerNeurons, inputNeurons);
        biases  = new Matrix(nextLayerNeurons, 1);
        weights.randomize();
        biases.randomize();
    }
    
    public void feedForward()
    {
        output = Matrix.naiveMult(weights, input);
        output = Matrix.add(output, biases);
        output = applyActivationFunction(output);
        
        // Attach output to next layer. If this is last layer, we cannot attach,
        // because there is no output layer.
        if(outputLayer != null)
            outputLayer.setInput(output);
        // This is last layer, so calculate loss.
        else
            network.calcLoss();
    }
    
    public void backpropagation()
    {
        Matrix net_d_E   = calcDerivativeOfErrorWrtNet();
        Matrix w_d_E     = Matrix.naiveMult(net_d_E, Matrix.transpose(input));
        Matrix input_d_E = Matrix.naiveMult(Matrix.transpose(weights), net_d_E);
        
        // Update weights
        weights = Matrix.sub(weights, Matrix.mult(w_d_E, network.learningRate));
        
        // Update biases
        biases = Matrix.sub(biases, Matrix.mult(net_d_E, network.learningRate));
        
        // Backpropagate the gradients to input layer of this layer.
        // If input layer is null, there is nothing to backpropagate.
        if(inputLayer != null)
            inputLayer.setOutput_d_E(input_d_E);
    }
    
    private Matrix applyActivationFunction(Matrix input)
    {
        switch(activationType)
        {
            case SIGMOID:
                return ActivationFunc.sigmoid(input);
            case TANH:
                return ActivationFunc.derSigmoid(input);
            default:
                System.err.println("Error: Undefined Activation Type");
                return null;
        }
    }
    
    private Matrix calcDerivativeOfErrorWrtNet()
    {
        Matrix net_d_E;
        
        // If this layer is last layer
        if(outputLayer == null)
        {
            if(network.lossType == LossType.MSE)
            {
                output_d_E = LossFunction.derMse(output, network.getTarget());
            }
            else
            {
                System.err.println("Error: Undefined loss type, output_d_E couldn't determined.");
                return null;
            }
        }
        
        switch(activationType)
        {
            case SIGMOID:
                net_d_E = Matrix.mult(output_d_E, ActivationFunc.derSigmoid(output));
                break;
            case TANH:
                net_d_E = Matrix.mult(output_d_E, ActivationFunc.derTanh(output));
                break;
            default:
                System.err.println("Error: Undefined activation type");
                return null;
        }
        
        return net_d_E;
    }

    public void setNetwork(NeuralNetwork network) {
        this.network = network;
    }

    public void setInputLayer(Layer inputLayer) {
        this.inputLayer = inputLayer;
    }

    public void setOutputLayer(Layer outputLayer) {
        this.outputLayer = outputLayer;
    }

    public void setInput(Matrix input) {
        this.input = input;
    }

    public void setOutput(Matrix output) {
        this.output = output;
    }

    public Matrix getOutput() {
        return output;
    }

    public void setOutput_d_E(Matrix output_d_E) {
        this.output_d_E = output_d_E;
    }   
}
