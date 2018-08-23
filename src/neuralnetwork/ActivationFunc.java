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
    
    private static double softmax(double x, double sum)
    {
        return x / sum;
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
    
    public static Matrix softmax(Matrix m)
    {
        double max = m.getMaxElement();
        Matrix exps = Matrix.exp(Matrix.sub(m, max));
        double sum = exps.sum();
        
        Matrix softmax = new Matrix(m.rows, m.cols);
        for(int i = 0; i < m.rows; i++) {
            softmax.data[i][0] = softmax(exps.data[i][0], sum);
        }
        
        return softmax;
    }
    
    public static Matrix derSoftmax(Matrix m)
    {
        Matrix derSoftmax = new Matrix(m.rows, m.rows);
        for(int i = 0; i < m.rows; ++i)
        {
            for(int j = 0; j < m.rows; ++j)
            {
                derSoftmax.data[i][j] =
                        m.data[j][0] * ((i == j ? 1.0 : 0.0) - m.data[i][0]);
            }
        }
        return derSoftmax;
    }
}
