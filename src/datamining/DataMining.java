package datamining;

import java.io.File;
import java.util.ArrayList;
import neuralnetwork.ActivationType;
import neuralnetwork.LossType;
import neuralnetwork.NeuralNetwork;

/**
 *
 * @author tnc
 */
public class DataMining
{   
    static final String DATA_PATH = System.getProperty("user.home") + File.separator +
            "Desktop" + File.separator + "data" + File.separator;
    
    static final String E0 = DATA_PATH + "E0.csv";
    static final String T1 = DATA_PATH + "T1.csv";
    
    public static void main(String[] args)
    {
        ArrayList<League> leagues = new ArrayList<>();        
        leagues.add(CSV_Reader.read(E0));
        leagues.add(CSV_Reader.read(T1));
        
        NeuralNetwork nn = new NeuralNetwork(
            new int[]{6, 40, 3},
            new ActivationType[]{
                ActivationType.SIGMOID,
                ActivationType.SIGMOID
            },
            LossType.MSE,
            0.05
        );
        
        League[] leagueArr = leagues.toArray(new League[leagues.size()]);
        Trainer trainer = new Trainer(nn, leagueArr, 0.1f);
        trainer.train(10000);      
        trainer.test();       
    }
}
