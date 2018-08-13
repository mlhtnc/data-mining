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
    
    static final String _17_18 = DATA_PATH + "17-18" + File.separator + "E0.csv";
    static final String _16_17 = DATA_PATH + "16-17" + File.separator + "E0.csv";
    static final String _15_16 = DATA_PATH + "15-16" + File.separator + "E0.csv";

    
    public static void main(String[] args)
    {
        ArrayList<League> leagues = new ArrayList<>();        
        leagues.add(CSV_Reader.read(_17_18));
        leagues.add(CSV_Reader.read(_16_17));
        leagues.add(CSV_Reader.read(_15_16));
        
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
        trainer.train(5000);      
        trainer.test();       
    }
}
