package neuralnetwork;

/**
 *
 * @author tnc
 */
public class Trainer
{
    private final NeuralNetwork nn;
    private final Matrix[] inputs;
    private final Matrix[] targets;
    
    public Trainer(NeuralNetwork nn, Matrix[] inputs, Matrix[] targets)
    {
        this.nn = nn;
        this.inputs = inputs;
        this.targets = targets;
    }
    
    public void train(int epoch)
    {
        for(int i = 0; i < epoch; i++)
        {
            for(int j = 0; j < inputs.length; j++)
            {
                nn.setInput(inputs[j]);
                nn.setTarget(targets[j]);
                nn.feedForward();
                nn.backpropagation();
            }
            log(i);
        }
    }
    
    private void log(int epoch)
    {
        System.out.println("Epoch: " + (epoch + 1));
        System.out.println(String.format("Error: %.10f\n", nn.getLoss()));
    }
}
