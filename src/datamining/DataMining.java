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
    // TODO:
    // -After each epoch print accuracy. e.g: Accuracy: 85% (17/20)
    //
    // -We only see loss of last output after each epoch. Last output is not
    // informative enough so take avarage loss of all outputs in one epoch.
    
    static String dataPath = System.getProperty("user.home") + File.separator +
            "Desktop" + File.separator + "data" + File.separator;
    
    static String E0 = dataPath + "E0.csv";
    static String T1 = dataPath + "T1.csv";
    
    public static void main(String[] args)
    {
        ArrayList<League> leagues = new ArrayList<>();        
        leagues.add(CSV_Reader.read(E0));
        //leagues.add(CSV_Reader.read(T1));
        
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
