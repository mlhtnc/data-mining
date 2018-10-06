package datamining;

import java.util.Arrays;

/**
 *
 * @author tnc
 */
public class League
{
    private Match[] matches;
    private final Team[] teams;
    
    private final int numberOfMatch;
    private int currMatch = 0;
    
    public int minGoals = Integer.MAX_VALUE;
    public int maxGoals = Integer.MIN_VALUE;
    public int maxGD;
    public int minGD;
    
    public League(Match[] matches, Team[] teams)
    {
        this.matches = matches;
        this.teams = teams;
        this.numberOfMatch = matches.length;

        sortTeams();
        extractTableData();
    }
    
    private void extractTableData()
    {
        Match[] _matches = new Match[numberOfMatch];
        for(int i = 0; i < numberOfMatch; i++)
        {
            _matches[i] = matches[currMatch].copy();
            playCurrMatch();
        }
        matches = _matches;
    }
    
    // Plays the current match
    private void playCurrMatch()
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
        home.homePlayed += 1;
        away.awayPlayed += 1;
        
        findMinMaxGD(home);
        findMinMaxGD(away);
        
        minGoals = Math.min(minGoals, match.FTHG);
        minGoals = Math.min(minGoals, match.FTAG);
        maxGoals = Math.max(maxGoals, match.FTHG);
        maxGoals = Math.max(maxGoals, match.FTAG);
                
        switch(match.FTR)
        {
            // Home wins
            case 'H':
                home.pts += 3;
                home.win += 1;
                away.lose += 1;
                home.homeWin += 1;
                away.awayLose += 1;
                break;
                
            // Away wins
            case 'A':
                away.pts += 3;
                away.win += 1;
                home.lose += 1;
                away.awayWin += 1;
                home.homeLose += 1;
                break;
                
            // Draw
            case 'D':
                home.pts += 1;
                away.pts += 1;
                home.draw += 1;
                away.draw += 1;
                home.homeDraw += 1;
                away.awayDraw += 1;
                break;
            default:
                System.out.println("Undefined result");
        }
        
        sortTeams();
        currMatch++;
    }
    
    private void findMinMaxGD(Team t)
    {
        maxGD = Math.max(maxGD, t.GD);
        minGD = Math.min(minGD, t.GD);  
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
        
        for(Team t : teams)
            System.out.print(t.toString());
        
        System.out.println();
    }
    
    private void sortTeams()
    {
        Arrays.sort(teams);
        
        for(int i = 0; i < teams.length; i++)
            teams[i].position = i + 1;
    }
    
    public Match[] getMatches()
    {
        return matches;
    }
    
    public Team[] getTeams()
    {
        return teams;
    }
    
    public int getMatchCount()
    {
        return matches.length;
    }
}
