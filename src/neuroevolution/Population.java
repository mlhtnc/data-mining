package neuroevolution;

import java.util.Arrays;
import neuralnetwork.Matrix;

/**
 *
 * @author tnc
 */
public class Population
{
    private final Chromosome[] chroms;
    
    private final int populationSize;
    
    private final double mutationRate;
    
    private int generationNumber;
    
    public Population(int populationSize, double mutationRate)
    {
        this.populationSize = populationSize;
        this.mutationRate  = mutationRate;
        chroms = new Chromosome[populationSize];
        
        for(int i = 0; i < chroms.length; ++i)
        {
            chroms[i] = new Chromosome();
            chroms[i].setPopulation(this);
        }
    }
    
    public void evolve()
    {
        // Crossover the best two chromosomes.
        Chromosome child = chroms[0].crossover(chroms[1]);
        
        // Mutate this child and produce new chromosomes.
        for(int i = 0; i < chroms.length; ++i)
            chroms[i] = child.mutate();
        
        // Calculate fitness of each chromosome.
        for(int i = 0; i < chroms.length; ++i)
            chroms[i].calculateFitness();
        
        Arrays.sort(chroms);
                
        this.generationNumber++;
    }

    public double getMutationRate() {
        return mutationRate;
    }

    public int getGenerationNumber() {
        return generationNumber;
    }
    
    public Chromosome getFittest() {
        return chroms[0];
    }
    
    @Override
    public String toString()
    {
        String ret = "Chromosomes:\n";
        for(int i = 0; i < populationSize; ++i)
        {
            ret += String.format("#%d\t", i + 1);
            ret += chroms[i].toString();
        }
        return ret;
    }
}
