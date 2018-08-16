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
    
    // Bet365 home win odds
    public double B365H;
    
    // Bet365 draw odds
    public double B365D;
    
    // Bet365 away win odds
    public double B365A;
    
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
        copy.B365H = this.B365H;
        copy.B365D = this.B365D;
        copy.B365A = this.B365A;
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
            "Win",
            "Draw",
            "Lose",
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
        
        ret += "\nHome Team:\n";
        ret += String.format("Win Percentage:\t\t%.0f%%\n", homeTeam.getPercentageOfWin() * 100.0);
        ret += String.format("Draw Percentage:\t%.0f%%\n", homeTeam.getPercentageOfDraw() * 100.0);
        ret += String.format("Lose Percentage:\t%.0f%%\n", homeTeam.getPercentageOfLose() * 100.0);
        ret += String.format("Home Win Percentage:\t%.0f%%\n", homeTeam.getPercentageOfHomeWin() * 100.0);
        ret += String.format("Home Draw Percentage:\t%.0f%%\n", homeTeam.getPercentageOfHomeDraw() * 100.0);
        ret += String.format("Home Lose Percentage:\t%.0f%%\n", homeTeam.getPercentageOfHomeLose() * 100.0);
        
        ret += "\nAway Team:\n";
        ret += String.format("Win Percentage:\t\t%.0f%%\n", awayTeam.getPercentageOfWin() * 100.0);
        ret += String.format("Draw Percentage:\t%.0f%%\n", awayTeam.getPercentageOfDraw() * 100.0);
        ret += String.format("Lose Percentage:\t%.0f%%\n", awayTeam.getPercentageOfLose() * 100.0);
        ret += String.format("Away Win Percentage:\t%.0f%%\n", awayTeam.getPercentageOfAwayWin() * 100.0);
        ret += String.format("Away Draw Percentage:\t%.0f%%\n", awayTeam.getPercentageOfAwayDraw() * 100.0);
        ret += String.format("Away Lose Percentage:\t%.0f%%\n", awayTeam.getPercentageOfAwayLose() * 100.0);
        
        return ret;
    }
}