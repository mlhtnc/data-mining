package neuralnetwork;

/**
 *
 * @author tnc
 */
public class LossFunction
{
    public static double mse(Matrix output, Matrix target)
    {
        Matrix diff = Matrix.sub(target, output);
        Matrix errorMatrix = Matrix.mult(diff, diff);
        errorMatrix = Matrix.div(errorMatrix, 2.0);
        return errorMatrix.sum();
    }
    
    public static Matrix derMse(Matrix output, Matrix target)
    {
        return Matrix.sub(output, target);
    }
    
    public static double crossEntropy(Matrix output, Matrix target)
    {
        Matrix crossEntropy = Matrix.mult(target, Matrix.log(output));
        return -crossEntropy.sum();
    }
    
    public static Matrix derCrossEntropy(Matrix output, Matrix target)
    {
        Matrix derCrossEntropy = Matrix.div(target, output);
        return Matrix.negative(derCrossEntropy);
    }
}
