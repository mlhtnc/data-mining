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
    // -After each epoch print accuracy. e.g: Accuracy: 85% (17/20)
    //
    // -We only see loss of last output after each epoch. Last output is not
    // informative enough so take avarage loss of all outputs in one epoch.
    // 
    // -In the trainer, we need a structure that we can add more league data.
    // Redesign structure, so we can add more data onto current data after.
    // Note: Create a league list and when a new league added, initialize data
    // again.
    //
    // -We need to shuffle data according to given test percentage.
    //
    // -Get all table information of each input, so it can help us later when
    // we analyse result. e.g: position, won, lost, draw, pts...
    
    
    static String dataPath = System.getProperty("user.home") + File.separator +
            "Desktop" + File.separator + "data" + File.separator;
    
    static String E0 = dataPath + "E0.csv";
    static String T1 = dataPath + "T1.csv";
    
    public static void main(String[] args)
    {
        League league_E0 = CSV_Reader.read(E0);
        League league_T1 = CSV_Reader.read(T1);

        NeuralNetwork nn = new NeuralNetwork(
            new int[]{6, 40, 3},
            new ActivationType[]{
                ActivationType.SIGMOID,
                ActivationType.SIGMOID
            },
            LossType.MSE,
            0.05
        );
        
        Trainer trainer = new Trainer(nn, league_E0, 0.03);
        trainer.train(5000);
        
        trainer.test();       
    }
}
