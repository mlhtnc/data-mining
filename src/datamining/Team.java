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
    
    public int won;
    
    public int draw;
    
    public int lost;
    
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
        copy.played = this.played;
        copy.won = this.won;
        copy.draw = this.draw;
        copy.lost = this.lost;
        copy.GF = this.GF;
        copy.GA = this.GA;
        copy.GD = this.GD;
        copy.pts = this.pts;
        return copy;
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
        String ret = String.format(
            "%2d.%-20s%d\t%d\t%d\t%d\t%d\t%d\t%d\t%d\n",
            position,
            name,
            played,
            won,
            draw,
            lost,
            GF,
            GA,
            GD,
            pts
        );
        
        return ret;
    }
}
