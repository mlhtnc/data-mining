package neuralnetwork;

/**
 *
 * @author tnc
 */
public class ActivationFunc
{
    public static Matrix sigmoid(Matrix m)
    {
        Matrix sigmoid;     //  1 / ( 1 + exp( -m ) );
        
        sigmoid = Matrix.negative(m);
        sigmoid = Matrix.exp(sigmoid);
        sigmoid = Matrix.add(sigmoid, 1.0);
        sigmoid = Matrix.div(1.0, sigmoid);
        
        return sigmoid;
    }

    public static Matrix derSigmoid(Matrix m)
    {
        Matrix derSigmoid;      //  m * ( 1 - m )
        
        derSigmoid = Matrix.sub(1f, m);
        derSigmoid = Matrix.mult(m, derSigmoid);
        
        return derSigmoid;
    }
    
    public static Matrix tanh(Matrix m)
    {
        Matrix tanh;    //  2 / (1 + exp(-2 * m)) - 1;        
        
        tanh = Matrix.mult(m, -2.0);
        tanh = Matrix.exp(tanh);
        tanh = Matrix.add(tanh, 1.0);
        tanh = Matrix.div(2.0, tanh);
        tanh = Matrix.sub(tanh, 1.0);
        
        return tanh;
    }

    public static Matrix derTanh(Matrix tanh)
    {
        //  1 - (tanh ^ 2)
        return Matrix.sub(1.0, Matrix.mult(tanh, tanh));
    }

    

//    public static Matrix Softmax(Matrix m)
//    {
//        //find the max of the array
//        double max = m.data.OfType<double>().Max();
//
//        //find the exp of the array
//        Matrix expMatrix = Matrix.Exp(m - max);
//
//        //make a query for using linq
//        var query = expMatrix.data.OfType<double>();
//
//        //i=>i is a lamda expression for selecting value we are selecing the current item
//        double expMatrixSum = query.Sum<double>(i => i);
//
//        return expMatrix / expMatrixSum;
//    }
//
//    public static Matrix DerOfSoftmax(Matrix x)
//    {
//        Matrix der = new Matrix(x.rows, x.rows);
//
//        for (int i = 0; i < x.rows; i++)
//        {
//            for (int j = 0; j < x.rows; j++)
//            {
//                der[i, j] = x[i, 0] * ((i == j ? 1 : 0) - x[j, 0]);
//            }
//        }
//
//        return Matrix.DecreaseToOneDimension(der);
//    }
}
