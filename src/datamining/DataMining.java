package datamining;

/**
 *
 * @author tnc
 */
public class DataMining
{
    // TODO:
    // We need to keep the fittest of all generations.
    
    private static final String SEP = java.io.File.separator;
    private static final String HOME_PATH = System.getProperty("user.home") + SEP;
    private static final String DATA_PATH = HOME_PATH + "Desktop" + SEP + "data" + SEP;
    
    // League Paths
    private static final String ENG_PATH = DATA_PATH + "eng" + SEP;
    private static final String ITA_PATH = DATA_PATH + "ita" + SEP;
    private static final String TUR_PATH = DATA_PATH + "tur" + SEP;
    private static final String SPA_PATH = DATA_PATH + "spa" + SEP;
    private static final String GER_PATH = DATA_PATH + "ger" + SEP;
    
    private final League league;
    
    public DataMining()
    {
        league = CSV_Reader.read(ENG_PATH);
    }
     
    public void run_NN()
    {
        Trainer trainer = new Trainer(league, 0.1f, true);
        trainer.train_NN(500); 
    }
    
    public void run_NE()
    {
        Trainer trainer = new Trainer(league, 0.1f, false);
        trainer.train_NE(100);
    }
    
    public static void main(String[] args)
    {        
        DataMining data_mining = new DataMining();
        data_mining.run_NE();
    }
}
