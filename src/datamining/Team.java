package datamining;

/**
 *
 * @author tnc
 */
public class Team implements Comparable<Team>
{
    public String name;
    
    public int id;
    
    public int position;
    
    public int played;
    
    public int homePlayed;
    
    public int awayPlayed;
    
    public int win;
    
    public int draw;
    
    public int lose;
    
    public int homeWin;
    
    public int homeDraw;
    
    public int homeLose;
    
    public int awayWin;
    
    public int awayDraw;
    
    public int awayLose;
        
    public int GF;
    
    public int GA;
    
    public int GD;
    
    public int pts;
    
    public Team(String name, int id)
    {
        this.name = name;
        this.id = id;
    }
    
    public Team copy()
    {
        Team copy = new Team(this.name, this.id);
        copy.position = this.position;
        copy.played   = this.played;
        copy.win  = this.win;
        copy.draw = this.draw;
        copy.lose = this.lose;
        copy.GF   = this.GF;
        copy.GA   = this.GA;
        copy.GD   = this.GD;
        copy.pts  = this.pts;
        copy.homePlayed = this.homePlayed;
        copy.awayPlayed = this.awayPlayed;
        copy.homeWin    = this.homeWin;
        copy.homeDraw   = this.homeDraw;
        copy.homeLose   = this.homeLose;
        copy.awayWin    = this.awayWin;
        copy.awayDraw   = this.awayDraw;
        copy.awayLose   = this.awayLose;
        return copy;
    }
    
    public double getPercentageOfWin()
    {
        if(played == 0) {
            return 0.0;
        } else {
            return (double) win / played;
        }
    }
    
    public double getPercentageOfDraw()
    {   
        if(played == 0) {
            return 0.0;
        } else {
            return (double) draw / played;
        }
    }
    
    public double getPercentageOfLose()
    {
        if(played == 0) {
            return 0.0;
        } else {
            return (double) lose / played;
        }
    }

    public double getPercentageOfHomeWin()
    {
        if(homePlayed == 0) {
            return 0.0;
        } else {
            return (double) homeWin / homePlayed;
        }
    }
    
    public double getPercentageOfHomeDraw()
    {
        if(homePlayed == 0) {
            return 0.0;
        } else {
            return (double) homeDraw / homePlayed;
        }
    }
    
    public double getPercentageOfHomeLose()
    {
        if(homePlayed == 0) {
            return 0.0;
        } else {
            return (double) homeLose / homePlayed;
        }
    }
    
    public double getPercentageOfAwayWin()
    {
        if(awayPlayed == 0) {
            return 0.0;
        } else {
            return (double) awayWin / awayPlayed;
        }
    }
    
    public double getPercentageOfAwayDraw()
    {
        if(awayPlayed == 0) {
            return 0.0;
        } else {
            return (double) awayDraw / awayPlayed;
        }
    }
    
    public double getPercentageOfAwayLose()
    {
        if(awayPlayed == 0) {
            return 0.0;
        } else {
            return (double) awayLose / awayPlayed;
        }
    }
    
    @Override
    public int compareTo(Team other)
    {
        if(this.pts > other.pts)
            return -1;
        else if(this.pts < other.pts)
            return 1;
        else if(this.GD > other.GD)
            return -1;
        else if(this.GD < other.GD)
            return 1;
        else if(this.GF > other.GF)
            return -1;
        else if(this.GF < other.GF)
            return 1;
        else if(this.name.compareTo(other.name) < 0)
            return -1;
        else
            return 1;
    }
    
    @Override
    public String toString()
    {
        String ret = String.format("%2d.%-20s%d\t%d\t%d\t%d\t%d\t%d\t%d\t%d\n",
            position,
            name,
            played,
            win,
            draw,
            lose,
            GF,
            GA,
            GD,
            pts
        );
   
        return ret;
    }
}
