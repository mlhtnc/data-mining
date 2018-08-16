package datamining;

import java.io.File;
import neuralnetwork.ActivationType;
import neuralnetwork.LossType;
import neuralnetwork.NeuralNetwork;

/**
 *
 * @author tnc
 */
public class DataMining
{   
    // TODO:
    // -Make a user interface for predicting matches.
    //
    // -Softmax classifier
    
    
    // Data Path
    static final String DATA_PATH = System.getProperty("user.home") + File.separator +
            "Desktop" + File.separator + "data" + File.separator;
    
    // League Paths
    static final String PL_PATH = DATA_PATH + "pl" + File.separator;
    static final String TSL_PATH = DATA_PATH + "tsl" + File.separator;
    
    public static void main(String[] args)
    {
        League pl = CSV_Reader.read(PL_PATH);

        NeuralNetwork nn = new NeuralNetwork(
            new int[]{17, 13, 24, 3},
            new ActivationType[]{
                ActivationType.SIGMOID,
                ActivationType.SIGMOID,
                ActivationType.SIGMOID
            },
            LossType.MSE,
            0.05
        );
        
        Trainer trainer = new Trainer(nn, pl, 0.01f, true);
        trainer.train(500);      
        trainer.test();     
    }
}
