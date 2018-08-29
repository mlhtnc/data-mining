package knn;

import java.util.Arrays;
import javafx.util.Pair;
import neuralnetwork.Matrix;

/**
 * This class is implemented especially for football data mining.
 * It is not generalized. So if you use it be careful.
 * 
 * @author tnc
 */
public class KNN
{
    private Matrix[] dataset;
    private int[] targetClasses;
    private int targetClassCount;
    
    public void setDataset(Matrix[] dataset)
    {
        this.dataset = dataset;
    }
    
    public void setTargets(Matrix[] targets)
    {
        targetClasses = new int[targets.length];    
        targetClassCount = targets[0].rows;
        
        for(int i = 0; i < targets.length; ++i)
        {
            for(int classIdx = 0; classIdx < targets[i].rows; ++classIdx)
            {
                if(targets[i].data[classIdx][0] == 1.0)
                {
                    targetClasses[i] = classIdx;
                    break;
                }
            }
        }
    }
    
    public Matrix predict(Matrix input, int k)
    {
        // Find distance between input and curr data.
        // After that sort them.
        Pair<Double, Integer>[] distances = new Pair[dataset.length];
        for(int i = 0; i < dataset.length; ++i)
        {
            distances[i] = new Pair<>(
                    distance(dataset[i], input), i
            );    
        }  
        sortData(distances);
        
        // Find classed of first k neighbours.
        int[] classCounter = new int[targetClassCount];
        for(int i = 0; i < k; ++i)
        {
            int neighbourClass = targetClasses[distances[i].getValue()];
            classCounter[neighbourClass]++;
        }
        
        // Find the distrubution of the neigbours.
        Matrix output = new Matrix(targetClassCount, 1);
        for(int i = 0; i < targetClassCount; ++i)
            output.data[i][0] = classCounter[i] / (double) k;

        // Return as percentages.
        return output;
    }
    
    private void sortData(Pair<Double, Integer>[] distances)
    {
        Arrays.sort(distances, (Pair<Double, Integer> p1, Pair<Double, Integer> p2) -> {
            if(p1.getKey() > p2.getKey())
                return 1;
            else if(p1.getKey() < p2.getKey())
                return -1;
            else
                return 0;
        });
    }
    
    private double distance(Matrix m1, Matrix m2)
    {
        double sum = 0.0;
        for(int i = 0; i < m1.rows; ++i)
        {
            double j = m1.data[i][0] - m2.data[i][0];
            sum += j * j;
        }
        
        return Math.sqrt(sum);
    }
}
