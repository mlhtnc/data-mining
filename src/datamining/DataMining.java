package datamining;

import java.util.Scanner;

/**
 *
 * @author tnc
 */
public class DataMining
{
    private static final String SEP = java.io.File.separator;
    private static final String PROJECT_PATH = System.getProperty("user.dir");
    private static final String DATASET_PATH = PROJECT_PATH + SEP +
            "dataset" + SEP;
    private static final String[] LEAGUE_PATHS = new String[6];
    
    private League league;
        
    public DataMining()
    {
        LEAGUE_PATHS[0] = DATASET_PATH + "eng" + SEP;
        LEAGUE_PATHS[1] = DATASET_PATH + "spa" + SEP;
        LEAGUE_PATHS[2] = DATASET_PATH + "ita" + SEP;
        LEAGUE_PATHS[3] = DATASET_PATH + "ger" + SEP;
        LEAGUE_PATHS[4] = DATASET_PATH + "fra" + SEP;
        LEAGUE_PATHS[5] = DATASET_PATH + "tur" + SEP;
        chooseLeague();
    }
     
    public void run_NN()
    {
        if(league == null)
            return;
        
        Trainer trainer = new Trainer(league, 0.2f, true, TrainingType.FULL_TIME_RESULT);
        trainer.train_NN(500);
    }
    
    public void chooseLeague()
    {
        Scanner sc = new Scanner(System.in);

        System.out.println("Please type \"download\" to redownload dataset");
        System.out.println("Choose League:");
        System.out.println("1- Premier League");
        System.out.println("2- La Liga");
        System.out.println("3- Serie A");
        System.out.println("4- Bundesliga");
        System.out.println("5- Ligue 1");
        System.out.println("6- Turkey Super League"); 
        System.out.print("\n > ");
        
        String respond = sc.nextLine();
        if(respond.equals("download"))
        {
            DataDownloader.downloadAll();
            return;
        }
        
        int leagueNo = Integer.parseInt(respond.charAt(0) + "");
        league = CSV_Reader.read(LEAGUE_PATHS[leagueNo - 1]);
    }
    
    public static void main(String[] args)
    {
        DataMining data_mining = new DataMining();
        data_mining.run_NN();
    }
}
