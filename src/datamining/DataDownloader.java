
package datamining;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tnc
 */
public class DataDownloader {
    
    private static final String DOWNLOAD_BASE_PATH =
            "http://www.football-data.co.uk/mmz4281/";
    
    private static final String PROJECT_DIRECTORY =
            System.getProperty("user.dir");
    
    private static final int START_YEAR = 17;
    
    private static final int END_YEAR = 20;
    
    public static void downloadAll()
    {
        download("eng");
        download("tur");
        download("fra");
        download("ger");
        download("ita");
        download("spa");
    }
    
    private static void download(String leagueName)
    {
        File requiredDirectory = new File(PROJECT_DIRECTORY + File.separator +
                "dataset" + File.separator + leagueName);
        
        if (requiredDirectory.exists() == false)
        {
            requiredDirectory.mkdir();
        }
        
        String filename;
        switch(leagueName)
        {
            case "eng":
                filename = "E0";
                break;
            case "tur":
                filename = "T1";
                break;
            case "ger":
                filename = "D1";
                break;
            case "fra":
                filename = "F1";
                break;
            case "ita":
                filename = "I1";
                break;
            case "spa":
                filename = "SP1";
                break;
            default:
                filename = "E0";
        }
        
        for(int i = START_YEAR; i < END_YEAR; ++i)
        {
            String downloadPath = DOWNLOAD_BASE_PATH + i + (i + 1) +
                    File.separator + filename + ".csv";
            
            String outputPath = PROJECT_DIRECTORY + File.separator + "dataset" +
                    File.separator + leagueName + File.separator +
                    i + "-" + (i + 1) + ".csv";
                    
            
            System.out.println("[INFO] Downloading " + downloadPath);
            System.out.println("[INFO] Saving to " + outputPath);
            downloadFile(downloadPath, outputPath);
        }        
    }
    
    private static void downloadFile(String inputPath, String outputPath)
    {   
        FileOutputStream fos = null;
        try {
            URL website = new URL(inputPath);
            ReadableByteChannel rbc = Channels.newChannel(website.openStream());
            fos = new FileOutputStream(outputPath);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DataDownloader.class.getName())
                    .log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(DataDownloader.class.getName())
                    .log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DataDownloader.class.getName())
                    .log(Level.SEVERE, null, ex);
        } finally {
            try {
                fos.close();
            } catch (IOException ex) {
                Logger.getLogger(DataDownloader.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
        }
    }
}
