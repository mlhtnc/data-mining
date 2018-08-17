package datamining;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

/**
 *
 * @author tnc
 */
public class CSV_Reader
{
    public static League read(String path)
    {
        BufferedReader br;
        FileReader fr;
        
        String[] dataList = new File(path).list();
        Arrays.sort(dataList);
        
        LinkedList matchList = new LinkedList();
        LinkedList teamList  = new LinkedList();
        HashMap<String, Integer> map = new HashMap<>();
        HashMap<String, Integer> teamNameMap = new HashMap<>();

        int currTeamId = 0;
        
        for(String filename : dataList)
        {
            try
            {
                fr = new FileReader(path + File.separator + filename);
                br = new BufferedReader(fr);

                String line = br.readLine();
                String[] splitted = line.split(",");

                for(int i = 0; i < splitted.length; i++)
                    map.put(splitted[i], i);

                while ((line = br.readLine()) != null)
                {
                    splitted = line.split(",");
                    
                    // If data is empty just pass it.
                    if(splitted.length == 0)
                        continue;

                    Match match = new Match();
                    String homeTeam = splitted[map.get("HomeTeam")];
                    String awayTeam = splitted[map.get("AwayTeam")];
                    match.FTHG      = Integer.parseInt(splitted[map.get("FTHG")]);
                    match.FTAG      = Integer.parseInt(splitted[map.get("FTAG")]);
                    match.FTR       = splitted[map.get("FTR")].charAt(0);
                    match.HTHG      = Integer.parseInt(splitted[map.get("HTHG")]);
                    match.HTAG      = Integer.parseInt(splitted[map.get("HTAG")]);
                    match.HTR       = splitted[map.get("HTR")].charAt(0);
                    
                    if(splitted[map.get("B365H")].isEmpty() ||
                       splitted[map.get("B365D")].isEmpty() ||
                       splitted[map.get("B365A")].isEmpty())
                    {
                        match.B365H = 2.0;
                        match.B365D = 2.0;
                        match.B365A = 2.0;
                    }
                    else
                    {
                        match.B365H     = Double.parseDouble(splitted[map.get("B365H")]);
                        match.B365D     = Double.parseDouble(splitted[map.get("B365D")]);
                        match.B365A     = Double.parseDouble(splitted[map.get("B365A")]);
                    }

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
            }
        }
        
        
        Match[] matches = (Match[]) matchList.toArray(new Match[matchList.size()]);
        Team[]  teams   = (Team[])  teamList.toArray(new Team[teamList.size()]);
        
        return new League(matches, teams);
    }
}
