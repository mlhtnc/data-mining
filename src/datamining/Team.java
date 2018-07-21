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
        else
            return 0;
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
