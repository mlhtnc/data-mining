package neuralnetwork;

import java.util.Random;
import java.util.function.Function;

/**
 *
 * @author tnc
 */
public class Matrix
{    
    public int rows;
    public int cols;
    public double[][] data;
    
    public static Random rnd = new Random();
    
    public Matrix(int rows, int cols)
    {
        this.rows = rows;
        this.cols = cols;
        data = new double[rows][cols];
    }
    
    public Matrix(double[][] arr)
    {
        this(arr.length, arr[0].length);
        
        for(int i = 0; i < rows; ++i)
        {
            for(int j = 0; j < cols; ++j)
            {
                data[i][j] = arr[i][j];
            }
        }
    }
    
    // Copy constructor
    public Matrix(Matrix m)
    {
        this(m.rows, m.cols);

        for(int i = 0; i < rows; i++)
            for(int j = 0; j < cols; j++)
                data[i][j] = m.data[i][j];
    }
    
    /*
    *    E L E M E N T - W I S E  O P E R A T I O N S
    */
    
    public Matrix add(Matrix m)
    {        
        for (int i = 0; i < m.rows; i++)
        {
            for (int j = 0; j < m.cols; j++)
            {
                this.data[i][j] += m.data[i][j];
            }
        }
        
        return this;
    }
    
    public static Matrix add(Matrix m1, Matrix m2)
    {
        Matrix sum = new Matrix(m1.rows, m1.cols);
        
        for (int i = 0; i < sum.rows; i++)
        {
            for (int j = 0; j < sum.cols; j++)
            {
                sum.data[i][j] = m1.data[i][j] + m2.data[i][j];
            }
        }
        
        return sum;
    }
    
    public Matrix sub(Matrix m)
    {        
        for (int i = 0; i < m.rows; i++)
        {
            for (int j = 0; j < m.cols; j++)
            {
                this.data[i][j] -= m.data[i][j];
            }
        }
        
        return this;
    }

    public static Matrix sub(Matrix m1, Matrix m2)
    {
        Matrix sub = new Matrix(m1.rows, m1.cols);
     
        for (int i = 0; i < sub.rows; i++)
        {
            for (int j = 0; j < sub.cols; j++)
            {
                sub.data[i][j] = m1.data[i][j] - m2.data[i][j];
            }
        }

        return sub;
    }

    // Hadamard Multiply
    public static Matrix mult(Matrix m1, Matrix m2)
    {
        Matrix mult = new Matrix(m1.rows, m1.cols);
        
        for (int i = 0; i < mult.rows; i++)
        {
            for (int j = 0; j < mult.cols; j++)
            {
                mult.data[i][j] = m1.data[i][j] * m2.data[i][j];
            }
        }

        return mult;
    }

    public static Matrix div(Matrix m1, Matrix m2)
    {
        Matrix div = new Matrix(m1.rows, m1.cols);
     
        for (int i = 0; i < div.rows; i++)
        {
            for (int j = 0; j < div.cols; j++)
            {
                if (m2.data[i][j] == 0.0)
                {
                    System.err.println("Error: Cannot divide by zero!");
                    return null;
                }

                div.data[i][j] = m1.data[i][j] / m2.data[i][j];
            }
        }

        return div;
    }

    /*
    *    S C A L A R  O P E R A T I O N S
    */
    
    public static Matrix add(Matrix m, double x)
    {
        Matrix sum = new Matrix(m.rows, m.cols);
        
        for (int i = 0; i < sum.rows; i++)
        {
            for (int j = 0; j < sum.cols; j++)
            {
                sum.data[i][j] = m.data[i][j] + x;
            }
        }

        return sum;
    }

    public static Matrix sub(Matrix m, double x)
    {
        Matrix sub = new Matrix(m.rows, m.cols);
        
        for (int i = 0; i < sub.rows; i++)
        {
            for (int j = 0; j < sub.cols; j++)
            {
                sub.data[i][j] = m.data[i][j] - x;
            }
        }

        return sub;
    }
    
    public static Matrix sub(double x, Matrix m)
    {
        return negative(sub(m, x));
    }

    public Matrix mult(double x)
    {        
        for (int i = 0; i < this.rows; i++)
        {
            for (int j = 0; j < this.cols; j++)
            {
                this.data[i][j] *= x;
            }
        }
        
        return this;
    }
    
    public static Matrix mult(Matrix m, double x)
    {
        Matrix mult = new Matrix(m.rows, m.cols);
        
        for (int i = 0; i < mult.rows; i++)
        {
            for (int j = 0; j < mult.cols; j++)
            {
                mult.data[i][j] = m.data[i][j] * x;
            }
        }

        return mult;
    }

    public static Matrix div(Matrix m, double x)
    {
        Matrix div = new Matrix(m.rows, m.cols);
        
        for (int i = 0; i < div.rows; i++)
        {
            for (int j = 0; j < div.cols; j++)
            {
                div.data[i][j] = m.data[i][j] / x;
            }
        }

        return div;
    }

    public static Matrix div(double x, Matrix m)
    {
        Matrix div = new Matrix(m.rows, m.cols);
        
        for (int i = 0; i < div.rows; i++)
        {
            for (int j = 0; j < div.cols; j++)
            {
                div.data[i][j] = x / m.data[i][j];
            }
        }

        return div;
    }

    public static Matrix negative(Matrix m)
    {
        Matrix neg = new Matrix(m.rows, m.cols);
        
        for (int i = 0; i < neg.rows; i++)
        {
            for (int j = 0; j < neg.cols; j++)
            {
                neg.data[i][j] = -m.data[i][j];
            }
        }

        return neg;
    }

    public static Matrix exp(Matrix m)
    {
        Matrix exp = new Matrix(m.rows, m.cols);
        
        for (int i = 0; i < exp.rows; i++)
        {
            for (int j = 0; j < exp.cols; j++)
            {
                exp.data[i][j] = Math.exp(m.data[i][j]);
            }
        }

        return exp;
    }
    
    public static Matrix log(Matrix m)
    {
        Matrix log = new Matrix(m.rows, m.cols);
        
        for (int i = 0; i < log.rows; i++)
        {
            for (int j = 0; j < log.cols; j++)
            {
                if(m.data[i][j] == 0.0)
                    log.data[i][j] = Math.log(0.0000000001);
                else
                    log.data[i][j] = Math.log(m.data[i][j]);
            }
        }

        return log;
    }
    
    public static Matrix transpose(Matrix m)
    {
        Matrix transpose = new Matrix(m.cols, m.rows);

        for(int i = 0; i < m.rows; i++)
        {
            for(int j = 0; j < m.cols; j++)
            {
                transpose.data[j][i] = m.data[i][j];
            }
        }

        return transpose;
    }


    public void zero()
    {
        for (int i = 0; i < this.rows; i++)
            for (int j = 0; j < this.cols; j++)
                data[i][j] = 0.0;
    }
    
    public void randomize()
    {        
        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < cols; j++)
            {
                data[i][j] = rnd.nextDouble() * 2f - 1f;
            }
        }
    }
    
    public static Matrix map(Matrix m, Function<Double, Double> func)
    {
        Matrix mapped = new Matrix(m.rows, m.cols);
        
        for(int i = 0; i < m.rows; ++i)
        {
            for(int j = 0; j < m.cols; ++j)
            {
                mapped.data[i][j] = func.apply(m.data[i][j]);
            }
        }
        
        return mapped;
    }
    
    public double sum()
    {
        double sum = 0.0;
        for(int i = 0; i < this.rows; ++i)
        {
            for(int j = 0; j < this.cols; ++j)
            {
                sum += this.data[i][j];
            }
        }
        return sum;
    }
    
    /**
     * Gets maximum element of this matrix.
     * @return Maximum element.
     */
    public double getMaxElement()
    {
        double max = Double.NEGATIVE_INFINITY;
        for(int i = 0; i < this.rows; ++i)
        {
            for(int j = 0; j < this.cols; ++j)
            {
                max = Math.max(max, this.data[i][j]);
            }
        }
        return max;
    }
    
    /**
     * This method only works for one dimensional matrices. e.g: 10x1, 20x1
     * @return Returns index of max row.
     */
    public int getMaxRow()
    {
        int maxIdx = 0;
        for(int i = 1; i < this.rows; i++)
        {
            if(this.data[i][0] > this.data[maxIdx][0])
                maxIdx = i;
        }
        
        return maxIdx;
    }
    
    /**
    * Matrix Multiplication O(n^3)
    * @param m1
    * @param m2
    * @return
    */
    public static Matrix naiveMult(Matrix m1, Matrix m2)
    {
        Matrix product = new Matrix(m1.rows, m2.cols);

        for (int i = 0; i < m1.rows; i++)
        {
            for (int k = 0; k < m1.cols; k++)
            {
                for (int j = 0; j < m2.cols; j++)
                {
                    product.data[i][j] += m1.data[i][k] * m2.data[k][j];
                }
            }
        }

        return product;
    }

    @Override
    public String toString()
    {
        String res = "";
        
        for(int i = 0; i < rows; i++)
        {
            res += "[";
            for(int j = 0; j < cols; j++)
            {    
                res += String.format("%.10f,\t", data[i][j]);
            }
            res = res.substring(0, res.length() - 2);
            res += "]\n";
        }

        return res;
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if(!(obj instanceof Matrix))
            return false;

        Matrix m = (Matrix) obj;
        
        if(m.rows != this.rows || m.cols != this.cols)
            return false;

        for(int i = 0; i < this.rows; i++)
        {
            for(int j = 0; j < this.cols; j++)
            {
                if(Math.abs(this.data[i][j] - m.data[i][j]) > 1e-14)
                    return false;
            }
        }
        
        return true;
    }
}
