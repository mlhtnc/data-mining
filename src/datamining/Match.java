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
    
    public Match copy()
    {
        Match copy = new Match();
        copy.homeTeam = this.homeTeam.copy();
        copy.awayTeam = this.awayTeam.copy();
        copy.FTHG = this.FTHG;
        copy.FTAG = this.FTAG;
        copy.FTR  = this.FTR;
        copy.HTHG = this.HTHG;
        copy.HTAG = this.HTAG;
        copy.HTR  = this.HTR;
        return copy;
    }
    
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
        
        ret += String.format(
            "%10s\t%11s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t\n",
            "Team",
            "Played",
            "Won",
            "Draw",
            "Lost",
            "GF",
            "GA",
            "GD",
            "Pts"
        );
        
        if(homeTeam.compareTo(awayTeam) < 0)
        {
            ret += homeTeam.toString();
            ret += awayTeam.toString();
        }
        else
        {
            ret += awayTeam.toString();
            ret += homeTeam.toString();
        }
        
        return ret;
    }
}