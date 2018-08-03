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
    static String dataPath = System.getProperty("user.home") + File.separator +
            "Desktop" + File.separator + "data" + File.separator + "E0.csv";
    
    public static void main(String[] args)
    {
        League league = CSV_Reader.read(dataPath);

        NeuralNetwork nn = new NeuralNetwork(
            new int[]{6, 40, 3},
            new ActivationType[]{
                ActivationType.SIGMOID,
                ActivationType.SIGMOID
            },
            LossType.MSE,
            0.1
        );
        
        Trainer trainer = new Trainer(nn, league, 0.03);
        trainer.train(5000);
        trainer.test();       
    }
}
