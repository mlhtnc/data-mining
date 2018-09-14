package datamining;

import java.util.Scanner;

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
    private static final String[] LEAGUE_PATHS = new String[5];
    
    private League league;
    
    public DataMining()
    {
        LEAGUE_PATHS[0] = DATA_PATH + "eng" + SEP;
        LEAGUE_PATHS[1] = DATA_PATH + "spa" + SEP;
        LEAGUE_PATHS[2] = DATA_PATH + "ita" + SEP;
        LEAGUE_PATHS[3] = DATA_PATH + "ger" + SEP;
        LEAGUE_PATHS[4] = DATA_PATH + "tur" + SEP;

        chooseLeague();
    }
     
    public void run_NN()
    {
        Trainer trainer = new Trainer(league, 0.3f, true, true);
        trainer.train_NN(1000); 
    }
    
    public void run_NE()
    {
        Trainer trainer = new Trainer(league, 0.3f, false, false);
        trainer.train_NE(100);
    }
    
    public void chooseLeague()
    {
        Scanner sc = new Scanner(System.in);

        System.out.println("Choose League:");
        System.out.println("1- Premier League");
        System.out.println("2- La Liga");
        System.out.println("3- Serie A");
        System.out.println("4- Bundesliga");
        System.out.println("5- Turkey Super League");        
        System.out.print("\n > ");
        
        league = CSV_Reader.read(LEAGUE_PATHS[sc.nextInt() - 1]);
    }
    
    public static void main(String[] args)
    {        
        DataMining data_mining = new DataMining();
        data_mining.run_NN();
    }
}
