package datamining;

/**
 *
 * @author tnc
 */
public class Match
{
    // Home Team
    public Team homeTeam;
    
    // Away Team
    public Team awayTeam;
   
    // Full Time Home Goals
    public int FTHG;
    
    // Full Time Away Goals
    public int FTAG;
    
    // Full Time Result
    public char FTR;
    
    // Half Time Home Goals
    public int HTHG;
    
    // Half Time Away Goals
    public int HTAG;
    
    // Half Time Result
    public char HTR;
    
    public void print()
    {
        System.out.println(toString());
    }
    
    @Override
    public String toString()
    {
        String ret = String.format(
            "%s %d - %d %s\n",
            homeTeam.name,
            FTHG,
            FTAG,
            awayTeam.name
        );
        
        return ret;
    }
}