package neuralnetwork;

/**
 *
 * @author tnc
 */
public class ActivationFunc
{
    // Private Methods
    
    private static double sigmoid(double x)
    {
        return 1.0 / (1.0 + Math.exp(-x));
    }
    
    private static double derSigmoid(double x)
    {
        return x * (1.0 - x);
    }
    
    private static double tanh(double x)
    {
        return 2.0 / (1.0 + Math.exp(-2.0 * x)) - 1.0;
    }
    
    private static double derTanh(double x)
    {
        return 1.0 - (x * x);
    }
    
    
    // Public Methods
    
    public static Matrix sigmoid(Matrix m)
    {
        return Matrix.map(m, ActivationFunc::sigmoid);
    }

    public static Matrix derSigmoid(Matrix m)
    {
        return Matrix.map(m, ActivationFunc::derSigmoid);
    }
    
    public static Matrix tanh(Matrix m)
    {
        return Matrix.map(m, ActivationFunc::tanh);
    }
    
    public static Matrix derTanh(Matrix tanh)
    {
        return Matrix.map(tanh, ActivationFunc::derTanh);
    }
}
