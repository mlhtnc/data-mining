package neuroevolution;

import neuralnetwork.NeuralNetwork;

/**
 *
 * @author tnc
 */
public class Chromosome implements Comparable<Chromosome>
{
    private NeuralNetwork gene;
    
    private int fitness;
    
    public Chromosome()
    {
        
    }
    
    public void calculateFitness()
    {
    
    }
    
    public Chromosome mutate()
    {
        return null;
    }
    
    public Chromosome crossover(Chromosome other)
    {
        return null;
    }

    @Override
    public int compareTo(Chromosome o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
