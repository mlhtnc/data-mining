package datamining;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

/**
 *
 * @author tnc
 */
public class CSV_Reader
{
    public static League read(String filename)
    {
        BufferedReader br;
        FileReader fr;
        
        LinkedList matchList = new LinkedList();
        LinkedList teamList  = new LinkedList();
        HashMap<String, Integer> map = new HashMap<>();
        HashMap<String, Integer> teamNameMap = new HashMap<>();
        
        int currTeamId = 0;
        
        try
        {
            fr = new FileReader(filename);
            br = new BufferedReader(fr);

            String line = br.readLine();
            String[] splitted = line.split(",");
            
            for(int i = 0; i < splitted.length; i++)
                map.put(splitted[i], i);
                
            while ((line = br.readLine()) != null)
            {
                splitted = line.split(",");
             
                Match match = new Match();
                String homeTeam = splitted[map.get("HomeTeam")];
                String awayTeam = splitted[map.get("AwayTeam")];
                match.FTHG      = Integer.parseInt(splitted[map.get("FTHG")]);
                match.FTAG      = Integer.parseInt(splitted[map.get("FTAG")]);
                match.FTR       = splitted[map.get("FTR")].charAt(0);
                match.HTHG      = Integer.parseInt(splitted[map.get("HTHG")]);
                match.HTAG      = Integer.parseInt(splitted[map.get("HTAG")]);
                match.HTR       = splitted[map.get("HTR")].charAt(0);
                
                // If team doesn't exist, create a new team.
                if(teamNameMap.containsKey(homeTeam) == false)
                {
                    Team team = new Team(homeTeam, currTeamId);
                    teamList.add(team);
                    teamNameMap.put(homeTeam, currTeamId);
                    match.homeTeam = team;
                    currTeamId++;
                }
                // If it is exist, just just get id.
                else
                {
                    int homeTeamId = teamNameMap.get(homeTeam);
                    match.homeTeam = (Team) teamList.get(homeTeamId);
                }
                
                // If team doesn't exist, create a new team.
                if(teamNameMap.containsKey(awayTeam) == false)
                {
                    Team team = new Team(awayTeam, currTeamId);
                    teamList.add(team);
                    teamNameMap.put(awayTeam, currTeamId);
                    match.awayTeam = team;
                    currTeamId++;
                }
                // If it is exist, just just get id.
                else
                {
                    int awayTeamId = teamNameMap.get(awayTeam);
                    match.awayTeam = (Team) teamList.get(awayTeamId);
                }

                matchList.add(match);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        Match[] matches = (Match[]) matchList.toArray(new Match[matchList.size()]);
        Team[]  teams   = (Team[])  teamList.toArray(new Team[teamList.size()]);
        
        return new League(matches, teams);
    }
}
