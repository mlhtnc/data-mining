package neuralnetwork;

/**
 *
 * @author tnc
 */
public class NeuralNetwork
{
    private final Layer[] layers;
    private Matrix target;
    
    double learningRate;
    double loss;
    LossType lossType;
    
    public NeuralNetwork(
        int[] topology,
        ActivationType[] actTypes,
        LossType lossType,
        double learningRate
    ){
        layers = new Layer[topology.length - 1];
        this.lossType = lossType;
        this.learningRate = learningRate;
        
        for (int i = 0; i < layers.length; i++)
        {
            layers[i] = new Layer(topology[i], topology[i + 1], actTypes[i]);
            layers[i].setNetwork(this);
            
            if (i > 0)
            {
                layers[i].setInputLayer(layers[i - 1]);
                layers[i - 1].setOutputLayer(layers[i]);
            }
        }
    }
    
    public void train(Matrix[] trainingInputs, Matrix[] trainingTargets,
            Matrix[] testingInputs, Matrix[] testingTargets, int epoch)
    {
        for(int i = 0; i < epoch; i++)
        {
            double avarageLoss = 0.0;
            for(int j = 0; j < trainingInputs.length; j++)
            {
                this.setInput(trainingInputs[j]);
                this.setTarget(trainingTargets[j]);
                this.feedForward();
                this.backpropagation();
                avarageLoss += loss;
            }
            
            int correct = 0;
            for(int j = 0; j < testingInputs.length; j++)
            {
                this.setInput(testingInputs[j]);
                this.setTarget(testingTargets[j]);
                this.feedForward();
                
                if(testingTargets[j].getMaxRow() == getOutput().getMaxRow())
                    correct++;
            }
            
            avarageLoss /= trainingInputs.length;
            printLog(i, avarageLoss, correct, testingInputs.length);
        }
    }
    
    public void feedForward()
    {
        for(int i = 0; i < layers.length; i++)
        {
            layers[i].feedForward();
        }
    }
    
    public void backpropagation()
    {
        for(int i = layers.length - 1; i >= 0; i--)
        {
            layers[i].backpropagation();
        }
    }
    
    public void calcLoss()
    {
        Matrix output = layers[layers.length - 1].getOutput();
        
        if(lossType == LossType.MSE)
        {
            loss = LossFunction.mse(output, target);
        }
        else
        {
            System.err.println("Error: Undefined loss type, cannot calculate loss.");
            loss = -1.0;
        }
    }
    
    private void printLog(int epoch, double avarageLoss, int correct, int total)
    {
        String epochStr = String.format("Epoch: %d, ", epoch + 1);
        String lossStr  = String.format("Loss = %.10f, ", avarageLoss);
        String accuracyStr = String.format("Accuracy: %.0f%% (%d/%d)",
                (((double) correct) / total) * 100.0, correct, total);
        
        System.out.println(epochStr + lossStr + accuracyStr);
    }
    
    public void setInput(Matrix input) {
        layers[0].setInput(new Matrix(input));
    }

    public void setTarget(Matrix target) {
        this.target = new Matrix(target);
    }
    
    public Matrix getOutput()
    {
        return layers[layers.length - 1].getOutput();
    }
    
    public double getLoss()
    {
        return loss;
    }
    
    public Matrix getTarget() {
        return target;
    }
}
