package datamining;

import java.util.Arrays;

/**
 *
 * @author tnc
 */
public class League
{
    private Match[] matches;
    private Team[]  table;
    
    private int numberOfMatch;
    private int currMatch = 0;
        
    public League(Match[] matches, Team[] teams)
    {
        this.matches = matches;
        this.table   = teams;
        this.numberOfMatch = matches.length;
    }
    
    // Plays the current match
    public void playCurrMatch()
    {
        Match match = matches[currMatch];
        Team home = match.homeTeam;
        Team away = match.awayTeam;
        
        home.GD += match.FTHG - match.FTAG;
        away.GD += match.FTAG - match.FTHG;        
        home.GF += match.FTHG;
        away.GF += match.FTAG;
        home.GA += match.FTAG;
        away.GA += match.FTHG;
        home.played += 1;
        away.played += 1;
        
        switch(match.FTR)
        {
            // Home won
            case 'H':
                home.pts += 3;
                home.won += 1;
                away.lost += 1;
                break;
                
            // Away won
            case 'A':
                away.pts += 3;
                away.won += 1;
                home.lost += 1;
                break;
                
            // Draw
            case 'D':
                home.pts += 1;
                away.pts += 1;
                home.draw += 1;
                away.draw += 1;
                break;
            default:
                System.out.println("Undefined result");
        }
  
        sortTeams();
        currMatch++;
    }
    
    public Match getCurrMatch()
    {
        return matches[currMatch];
    }

    public int getNumberOfMatch()
    {
        return numberOfMatch;
    }    
    
    private void sortTeams()
    {
        Arrays.sort(table);
        
        for(int i = 0; i < table.length; i++)
            table[i].position = i + 1;
    }
    
    public void printTable()
    {
        System.out.println(
            String.format(
                "%10s\t%11s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t",
                "Team",
                "Played",
                "Won",
                "Draw",
                "Lost",
                "GF",
                "GA",
                "GD",
                "Pts"
            )
        );
        
        for(Team t : table)
            System.out.print(t.toString());
        
        System.out.println();
    }
}
