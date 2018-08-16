package datamining;

import java.util.ArrayList;
import java.util.Random;
import neuralnetwork.Matrix;
import neuralnetwork.NeuralNetwork;

/**
 *
 * @author tnc
 */
public class Trainer
{
    private final NeuralNetwork nn;
    private final League league;
    private Match[] allMatches;
    private Match[] testingMatches;
    
    private Matrix[] inputs;
    private Matrix[] targets;
    private Matrix[] trainingInputs;
    private Matrix[] trainingTargets;
    private Matrix[] testingInputs;
    private Matrix[] testingTargets;
    
    private final int sampleCount;
    
    public Trainer(NeuralNetwork nn, League league,
            float testPercentage, boolean shuffle)
    {
        this.nn = nn;
        this.league = league;
        this.sampleCount = league.getMatchCount();
        
        extractFeatures();
        initialize(shuffle, testPercentage);
    }
    
    /**
     * Features:
     * 1. Goal differences of home team
     * 2. The percentage of winning of home team
     * 3. The percentage of draw of home team
     * 4. The percentage of lose of home team
     * 5. The percentage of winning when home team was home
     * 6. The percentage of draw when home team was home
     * 7. The percentage of lose when home team was home
     * 8. Goal differences of away team
     * 9. The percentage of winning of away team
     * 10. The percentage of draw of away team
     * 11. The percentage of lose of away team
     * 12. The percentage of winning when away team was away
     * 13. The percentage of draw when away team was away
     * 14. The percentage of lose when away team was away
     * 15. B365H = Bet365 home win odds
     * 16. B365D = Bet365 draw odds
     * 17. B365A = Bet365 away win odds 
     */
    private void extractFeatures()
    {
        Match[] matches = league.getMatches();
        int idx = 0;
        
        allMatches = new Match[sampleCount];
        inputs = new Matrix[sampleCount];
        targets = new Matrix[sampleCount];
        
        for(int i = 0; i < sampleCount; i++)
        {
            Match m = matches[i];
            allMatches[idx] = m;

            inputs[idx] = new Matrix(17, 1);
            
            // Home team features
            inputs[idx].data[0][0] = normalize(league.minGD, league.maxGD, m.homeTeam.GD);
            inputs[idx].data[1][0] = m.homeTeam.getPercentageOfWin();
            inputs[idx].data[2][0] = m.homeTeam.getPercentageOfDraw();
            inputs[idx].data[3][0] = m.homeTeam.getPercentageOfLose();
            inputs[idx].data[4][0] = m.homeTeam.getPercentageOfHomeWin();
            inputs[idx].data[5][0] = m.homeTeam.getPercentageOfHomeDraw();
            inputs[idx].data[6][0] = m.homeTeam.getPercentageOfHomeLose();
            
            // Away team features
            inputs[idx].data[7][0] = normalize(league.minGD, league.maxGD, m.awayTeam.GD);
            inputs[idx].data[8][0] = m.awayTeam.getPercentageOfWin();
            inputs[idx].data[9][0] = m.awayTeam.getPercentageOfDraw();
            inputs[idx].data[10][0] = m.awayTeam.getPercentageOfLose();
            inputs[idx].data[11][0] = m.awayTeam.getPercentageOfAwayWin();
            inputs[idx].data[12][0] = m.awayTeam.getPercentageOfAwayDraw();
            inputs[idx].data[13][0] = m.awayTeam.getPercentageOfAwayLose();
            
            // Bet365 odds
            inputs[idx].data[14][0] = normalize(league.minB365H, league.maxB365H, m.B365H);
            inputs[idx].data[15][0] = normalize(league.minB365D, league.maxB365D, m.B365D);
            inputs[idx].data[16][0] = normalize(league.minB365A, league.maxB365A, m.B365A);

            // Targets: Full Time Result
            targets[idx] = new Matrix(3, 1);
            targets[idx].data[0][0] = (m.FTR == 'H') ? 1.0 : 0.0;
            targets[idx].data[1][0] = (m.FTR == 'D') ? 1.0 : 0.0;
            targets[idx].data[2][0] = (m.FTR == 'A') ? 1.0 : 0.0;

            idx++;
        }
    }
    
    private void initialize(boolean shuffle, float testPercentage)
    {
        ArrayList<Integer> matchIndexes;
        Random rnd;
        
        int testingCount = (int) (sampleCount * testPercentage);
        int trainingCount = sampleCount - testingCount;
        trainingInputs = new Matrix[trainingCount];
        trainingTargets = new Matrix[trainingCount];
        testingInputs = new Matrix[testingCount];
        testingTargets = new Matrix[testingCount];
        testingMatches = new Match[testingCount];
        
        rnd = new Random();
        matchIndexes = new ArrayList<>();
        for(int i = 0; i < trainingCount; i++)
            matchIndexes.add(i);
        
        for(int i = 0; i < trainingCount; i++)
        {
            if(shuffle == true)
            {
                int rndMatchIdx = Math.abs(rnd.nextInt()) % matchIndexes.size();
                int idx = matchIndexes.get(rndMatchIdx);
                matchIndexes.remove(rndMatchIdx);

                trainingInputs[i] = inputs[idx];
                trainingTargets[i] = targets[idx];
            }
            else
            {
                trainingInputs[i] = inputs[i];
                trainingTargets[i] = targets[i];
            }
        }
        
        for(int i = trainingCount; i < sampleCount; i++)
        {   
            testingInputs[i - trainingCount]  = inputs[i];
            testingTargets[i - trainingCount] = targets[i];
            testingMatches[i - trainingCount] = allMatches[i];
        }
    }
    
    public void train(int epoch)
    {
        nn.train(trainingInputs, trainingTargets,
                testingInputs, testingTargets, epoch);
    }
    
    public void test()
    {
        for(int i = 0; i < testingInputs.length; i++)
        {
            nn.setInput(testingInputs[i]);
            nn.setTarget(testingTargets[i]);
            nn.feedForward();

            System.out.println("\nTest#" + (i + 1));
            testingMatches[i].print();
            System.out.print(testingTargets[i]);
            System.out.println(nn.getOutput());
        }  
    }
    
    public static double normalize(double oldMin, double oldMax, double value)
    {
        return ((value - oldMin) / (oldMax - oldMin)) * (1.0 - (-1.0)) + (-1.0);
    }
}
