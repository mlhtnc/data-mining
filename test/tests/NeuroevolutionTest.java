package tests;

import neuralnetwork.ActivationType;
import neuralnetwork.LossType;
import neuralnetwork.Matrix;
import neuroevolution.Chromosome;
import neuroevolution.FitnessType;
import neuroevolution.Population;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author tnc
 */
public class NeuroevolutionTest {
    
    public NeuroevolutionTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    
    @Test
    public void testNeuroevolution_XOR()
    {
        Matrix[] inputs = new Matrix[4];
        inputs[0] = new Matrix(new double[][]{ {0.0},{0.0} });
        inputs[1] = new Matrix(new double[][]{ {0.0},{1.0} });
        inputs[2] = new Matrix(new double[][]{ {1.0},{0.0} });
        inputs[3] = new Matrix(new double[][]{ {1.0},{1.0} });
        
        Matrix[] targets = new Matrix[4];
        targets[0] = new Matrix(new double[][]{ {0.0} });
        targets[1] = new Matrix(new double[][]{ {1.0} });
        targets[2] = new Matrix(new double[][]{ {1.0} });
        targets[3] = new Matrix(new double[][]{ {0.0} });
        
        // We should call this method before creating a population!
        // It will set parameters for neural network.
        Chromosome.initGene(
            inputs,
            targets,
            new int[]{2, 74, 60, 1},
            new ActivationType[]{
                ActivationType.TANH,
                ActivationType.TANH,
                ActivationType.TANH
            },
            LossType.MSE
        );
        
        Population population = new Population(100, 0.05, FitnessType.ABS_DIFFERENCE);
        Chromosome fittest = null;
        
        while(true)
        {
            population.evolve();
            
            fittest = population.getFittest();
            if(fittest.getFitness() >= 3.9)
                break;
        }
        
        // Test
        for(int i = 0; i < 4; i++)
        {
            fittest.getGene().setInput(inputs[i]);
            fittest.getGene().setTarget(targets[i]);
            fittest.getGene().feedForward();
            
            assertTrue(Math.abs(targets[i].data[0][0] - fittest.getGene().getOutput().data[0][0]) < 0.5);

            System.out.print("\nOutput: " + fittest.getGene().getOutput());
            System.out.println(String.format("Error: %.10f\n", fittest.getGene().getLoss()));
        }
    }
}
