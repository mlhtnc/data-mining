package datamining;

import java.io.File;

/**
 *
 * @author tnc
 */
public class DataMining
{
    static String dataPath = System.getProperty("user.home") + File.separator +
            "Desktop" + File.separator + "data" + File.separator + "E0.csv";
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        League league = CSV_Reader.read(dataPath);

        Match m = league.getCurrMatch();
        m.print();
        league.playCurrMatch();
        league.printTable();

        m = league.getCurrMatch();
        m.print();
        league.playCurrMatch();
        league.printTable();
    }
    
}
