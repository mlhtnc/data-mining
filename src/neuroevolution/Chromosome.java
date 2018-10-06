package neuroevolution;

import java.util.Random;
import neuralnetwork.ActivationType;
import neuralnetwork.Layer;
import neuralnetwork.LossType;
import neuralnetwork.Matrix;
import neuralnetwork.NeuralNetwork;

/**
 *
 * @author tnc
 */
public class Chromosome implements Comparable<Chromosome>
{
    public static Random rnd = new Random();
    
    private static Matrix[] inputs;
    
    private static Matrix[] targets;
    
    private static int[] topology;
    
    private static ActivationType[] activations;
    
    private static LossType lossType;
    
    private Population population;
    
    private NeuralNetwork gene;
    
    private double fitness;
    
    public Chromosome()
    {
        gene = new NeuralNetwork(
            topology,
            activations,
            lossType,
            0.0005      // It doesn't matter because we don't use backprop.
        );
    }
    
    // Copy constructor.
    public Chromosome(Chromosome other)
    {
        this.population = other.population;
        this.gene = new NeuralNetwork(other.gene);
    }
    
    public void calculateFitness()
    {
        fitness = 0;
        for(int i = 0; i < inputs.length; ++i)
        {
            gene.setInput(inputs[i]);
            gene.setTarget(targets[i]);
            gene.feedForward();
            
            switch(population.getFitnessType())
            {
                case ABS_DIFFERENCE:
                    if(population.getFitnessType() == FitnessType.ABS_DIFFERENCE)
                        fitness += 1.0 - Math.abs(gene.getOutput().data[0][0] - targets[i].data[0][0]);
                    break;
                case MAX_ONE:
                    if(gene.getOutput().getMaxRow() == targets[i].getMaxRow())
                        this.fitness++;
                    break;
            }
        }
    }
    
    public Chromosome crossover(Chromosome other)
    {
        Chromosome child = new Chromosome(this);
        Layer[] layers1 = gene.getLayers();
        Layer[] layers2 = other.gene.getLayers();
        Layer[] layersOfChild = child.gene.getLayers();
        
        for(int i = 0; i < layers1.length; ++i)
        {
            int rows = layers1[i].getWeights().rows;
            int cols = layers1[i].getWeights().cols;
        
            for(int j = 0; j < rows; ++j)
            {
                for(int k = 0; k < cols; ++k)
                {
                    Matrix weight1 = layers1[i].getWeights();
                    Matrix weight2 = layers2[i].getWeights();
                    Matrix weightOfChild = layersOfChild[i].getWeights();

                    if(k % 2 == 0)
                        weightOfChild.data[j][k] = weight1.data[j][k];
                    else
                        weightOfChild.data[j][k] = weight2.data[j][k];
                }
            }
            
            rows = layers1[i].getBiases().rows;
            cols = layers1[i].getBiases().cols;
            
            for(int j = 0; j < rows; ++j)
            {
                for(int k = 0; k < cols; ++k)
                {
                    Matrix biases1 = layers1[i].getBiases();
                    Matrix biases2 = layers2[i].getBiases();
                    Matrix biasesOfChild = layersOfChild[i].getBiases();

                    if(k % 2 == 0)
                        biasesOfChild.data[j][k] = biases1.data[j][k];
                    else
                        biasesOfChild.data[j][k] = biases2.data[j][k];                    
                }
            }
        }

        return child;
    }
    
    public Chromosome mutate()
    {
        Chromosome mutated = new Chromosome(this);
        Layer[] layers = mutated.gene.getLayers();
        
        for(int i = 0; i < layers.length; ++i)
        {
            Matrix weights = layers[i].getWeights();
            Matrix biases  = layers[i].getBiases();

            int numberOfMutation = (int) (population.getMutationRate() *
                    weights.rows * weights.cols);
            
            for(int j = 0; j < numberOfMutation; ++j)
            {
                int row = rnd.nextInt(weights.rows);
                int col = rnd.nextInt(weights.cols); 
                weights.data[row][col] = rnd.nextDouble() * 2.0 - 1.0;
            }
            
            numberOfMutation = (int) (population.getMutationRate() *
                    biases.rows * biases.cols);
            
            for(int j = 0; j < numberOfMutation; ++j)
            {
                int row = rnd.nextInt(biases.rows);
                int col = rnd.nextInt(biases.cols); 
                biases.data[row][col] = rnd.nextDouble() * 2.0 - 1.0;
            }
        }
        
        return mutated;
    }

    public static void initGene(Matrix[] inputs, Matrix[] targets,
            int[] topology, ActivationType[] act, LossType lossType)
    {
        Chromosome.inputs = inputs;
        Chromosome.targets = targets;
        Chromosome.topology = topology;
        Chromosome.activations = act;
        Chromosome.lossType = lossType;
    }
    
    public double getFitness() {
        return fitness;
    }
    
    public void setPopulation(Population population)
    {
        this.population = population;
    }
    
    public NeuralNetwork getGene()
    {
        return gene;
    }

    @Override
    public int compareTo(Chromosome other) {
        if(this.fitness > other.fitness)
            return -1;
        else if(this.fitness < other.fitness)
            return 1;
        else
            return 0;
    }
    
    @Override
    public String toString()
    {
        String ret = String.format("Fitness: %.2f\n", fitness);
        return ret;
    }
}
