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
    private final League[] leagues;
    private Match[] allMatches;
    private Match[] testingMatches;
    
    private Matrix[] inputs;
    private Matrix[] targets;
    private Matrix[] trainingInputs;
    private Matrix[] trainingTargets;
    private Matrix[] testingInputs;
    private Matrix[] testingTargets;
    
    private final float testPercentage;
    private final int sampleCount;
    
    public Trainer(NeuralNetwork nn, League[] leagues, float testPercentage)
    {
        this.nn = nn;
        this.leagues = leagues;
        this.testPercentage = testPercentage;
        this.sampleCount = calcSampleCount();
        
        extractData();
        initializeData();
    }
    
    private void extractData()
    {        
        allMatches = new Match[sampleCount];
        inputs = new Matrix[sampleCount];
        targets = new Matrix[sampleCount];
        
        int idx = 0;
        for(int i = 0; i < leagues.length; i++)
        {
            Match[] matches = leagues[i].getMatches();
            for(int j = 0; j < matches.length; j++)
            {
                Match m = matches[j];
                allMatches[idx] = m;
                
                inputs[idx] = new Matrix(6, 1);
                inputs[idx].data[0][0] = (m.homeTeam.played == 0) ? m.homeTeam.played : (double) m.homeTeam.won / m.homeTeam.played;
                inputs[idx].data[1][0] = (m.homeTeam.played == 0) ? m.homeTeam.played : (double) m.homeTeam.draw / m.homeTeam.played;
                inputs[idx].data[2][0] = (m.homeTeam.played == 0) ? m.homeTeam.played : (double) m.homeTeam.lost / m.homeTeam.played;
                inputs[idx].data[3][0] = (m.awayTeam.played == 0) ? m.awayTeam.played : (double) m.awayTeam.won / m.awayTeam.played;
                inputs[idx].data[4][0] = (m.awayTeam.played == 0) ? m.awayTeam.played : (double) m.awayTeam.draw / m.awayTeam.played;
                inputs[idx].data[5][0] = (m.awayTeam.played == 0) ? m.awayTeam.played : (double) m.awayTeam.lost / m.awayTeam.played;
                        
                targets[idx] = new Matrix(3, 1);
                targets[idx].data[0][0] = (m.FTR == 'H') ? 1.0 : 0.0;
                targets[idx].data[1][0] = (m.FTR == 'D') ? 1.0 : 0.0;
                targets[idx].data[2][0] = (m.FTR == 'A') ? 1.0 : 0.0;
                
                idx++;
            }
        }
    }
    
    private void initializeData()
    {
        int testingCount = (int) (sampleCount * testPercentage);
        int trainingCount = sampleCount - testingCount;
        
        trainingInputs = new Matrix[trainingCount];
        trainingTargets = new Matrix[trainingCount];
        testingInputs = new Matrix[testingCount];
        testingTargets = new Matrix[testingCount];
        testingMatches = new Match[testingCount];
        
        Random rnd = new Random();
        ArrayList<Integer> matchIndexes = new ArrayList<>();
        for(int i = 0; i < sampleCount; i++)
            matchIndexes.add(i);
        
        for(int i = 0; i < trainingCount; i++)
        {
            int rndMatchIdx = Math.abs(rnd.nextInt()) % matchIndexes.size();
            int idx = matchIndexes.get(rndMatchIdx);
            matchIndexes.remove(rndMatchIdx);
            
            trainingInputs[i] = inputs[idx];
            trainingTargets[i] = targets[idx];
        }
        
        for(int i = 0; i < testingCount; i++)
        {
            int rndMatchIdx = Math.abs(rnd.nextInt()) % matchIndexes.size();
            int idx = matchIndexes.get(rndMatchIdx);
            matchIndexes.remove(rndMatchIdx);
            
            testingInputs[i] = inputs[idx];
            testingTargets[i] = targets[idx];
            testingMatches[i] = allMatches[idx];
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
    
    private int calcSampleCount()
    {
        int sum = 0;
        for(int i = 0; i < leagues.length; i++)
        {
            sum += leagues[i].getMatchCount();
        }
        return sum;
    }
}
