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
        
        double error = 0.0;
        for(int i = 0; i < errorMatrix.rows; i++)
            error += errorMatrix.data[i][0];
        
        return error;
    }
    
    public static Matrix derMse(Matrix output, Matrix target)
    {
        return Matrix.sub(output, target);
    }
}
