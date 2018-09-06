package datamining;

import java.io.File;
import knn.KNN;
import neuralnetwork.ActivationType;
import neuralnetwork.LossType;
import neuralnetwork.NeuralNetwork;

/**
 *
 * @author tnc
 */
public class DataMining
{    
    // Data Path
    static final String DATA_PATH = System.getProperty("user.home") + File.separator +
            "Desktop" + File.separator + "data" + File.separator;
    
    // League Paths
    static final String ENG_PATH = DATA_PATH + "eng" + File.separator;
    static final String ITA_PATH = DATA_PATH + "ita" + File.separator;
    static final String TUR_PATH = DATA_PATH + "tur" + File.separator;
    static final String SPA_PATH = DATA_PATH + "spa" + File.separator;
    static final String GER_PATH = DATA_PATH + "ger" + File.separator;
    
    League lg = CSV_Reader.read(ENG_PATH);
    
    public static void main(String[] args)
    {        
        DataMining data_mining = new DataMining();
        data_mining.run_NE();
    }
     
    public void run_NN()
    {
        Trainer trainer = new Trainer(lg, 0.1f, true);
        
        NeuralNetwork nn = new NeuralNetwork(
            new int[]{17, 13, 24, 3},
            new ActivationType[]{
                ActivationType.SIGMOID,
                ActivationType.SIGMOID,
                ActivationType.SOFTMAX
            },
            LossType.CROSS_ENTROPY,
            0.0005
        );
        
        trainer.train_NN(nn, 5000); 
        trainer.test_NN(nn);
    }
    
    public void run_NE()
    {
        Trainer trainer = new Trainer(lg, 0.1f, false);
        trainer.train_NE();
    }
    
    public void run_KNN()
    {
        Trainer trainer = new Trainer(lg, 0.1f, false);
        KNN knn = new KNN();
        trainer.test_KNN(knn);
    }
}
