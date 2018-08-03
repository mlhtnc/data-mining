package datamining;

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
    
    private Matrix[] trainingInputs;
    private Matrix[] trainingTargets;
    private Matrix[] testingInputs;
    private Matrix[] testingTargets;
    
    private Match[] testingMatches;
    
    public Trainer(NeuralNetwork nn, League league, double testPercentage)
    {
        this.nn = nn;
        this.league = league;
        
        initalizeData(testPercentage);
    }
    
    private void initalizeData(double testPercentage)
    {
        int sampleCount = league.getNumberOfMatch();
        int testingCount = (int) (sampleCount * testPercentage);
        int trainingCount = sampleCount - testingCount;
        int testCntr = 0, trainingCntr = 0;

        trainingInputs = new Matrix[trainingCount];
        trainingTargets = new Matrix[trainingCount];
        testingInputs = new Matrix[testingCount];
        testingTargets = new Matrix[testingCount];
        testingMatches = new Match[testingCount];

        for(int i = 0; i < sampleCount; i++)
        {
            Match m = league.getCurrMatch();

            if(i >= trainingCount)
            {
                testingInputs[testCntr] = new Matrix(6, 1);
                testingInputs[testCntr].data[0][0] = (m.homeTeam.played == 0) ? m.homeTeam.played : (double) m.homeTeam.won / m.homeTeam.played;
                testingInputs[testCntr].data[1][0] = (m.homeTeam.played == 0) ? m.homeTeam.played : (double) m.homeTeam.draw / m.homeTeam.played;
                testingInputs[testCntr].data[2][0] = (m.homeTeam.played == 0) ? m.homeTeam.played : (double) m.homeTeam.lost / m.homeTeam.played;
                testingInputs[testCntr].data[3][0] = (m.awayTeam.played == 0) ? m.awayTeam.played : (double) m.awayTeam.won / m.awayTeam.played;
                testingInputs[testCntr].data[4][0] = (m.awayTeam.played == 0) ? m.awayTeam.played : (double) m.awayTeam.draw / m.awayTeam.played;
                testingInputs[testCntr].data[5][0] = (m.awayTeam.played == 0) ? m.awayTeam.played : (double) m.awayTeam.lost / m.awayTeam.played;
                        
                testingTargets[testCntr] = new Matrix(3, 1);
                testingTargets[testCntr].data[0][0] = (m.FTR == 'H') ? 1.0 : 0.0;
                testingTargets[testCntr].data[1][0] = (m.FTR == 'D') ? 1.0 : 0.0;
                testingTargets[testCntr].data[2][0] = (m.FTR == 'A') ? 1.0 : 0.0;
                
                testingMatches[testCntr] = m;
                
                testCntr++;
            }
            else
            {
                trainingInputs[trainingCntr] = new Matrix(6, 1);
                trainingInputs[trainingCntr].data[0][0] = (m.homeTeam.played == 0) ? m.homeTeam.played : (double) m.homeTeam.won / m.homeTeam.played;
                trainingInputs[trainingCntr].data[1][0] = (m.homeTeam.played == 0) ? m.homeTeam.played : (double) m.homeTeam.draw / m.homeTeam.played;
                trainingInputs[trainingCntr].data[2][0] = (m.homeTeam.played == 0) ? m.homeTeam.played : (double) m.homeTeam.lost / m.homeTeam.played;
                trainingInputs[trainingCntr].data[3][0] = (m.awayTeam.played == 0) ? m.awayTeam.played : (double) m.awayTeam.won / m.awayTeam.played;
                trainingInputs[trainingCntr].data[4][0] = (m.awayTeam.played == 0) ? m.awayTeam.played : (double) m.awayTeam.draw / m.awayTeam.played;
                trainingInputs[trainingCntr].data[5][0] = (m.awayTeam.played == 0) ? m.awayTeam.played : (double) m.awayTeam.lost / m.awayTeam.played;
                
                trainingTargets[trainingCntr] = new Matrix(3, 1);
                trainingTargets[trainingCntr].data[0][0] = (m.FTR == 'H') ? 1.0 : 0.0;
                trainingTargets[trainingCntr].data[1][0] = (m.FTR == 'D') ? 1.0 : 0.0;
                trainingTargets[trainingCntr].data[2][0] = (m.FTR == 'A') ? 1.0 : 0.0;
                
                trainingCntr++;
            }
            
            league.playCurrMatch();
        }
    }
    
    public void train(int epoch)
    {
        nn.train(trainingInputs, trainingTargets, epoch);
    }
    
    public void test()
    {
        System.out.println();
        for(int i = 0; i < testingInputs.length; i++)
        {
            System.out.println("Test#" + (i + 1));
            testingMatches[i].print();
            
            nn.setInput(testingInputs[i]);
            nn.feedForward();

            System.out.print(testingTargets[i]);
            System.out.println(nn.getOutput());
        }  
    }
}
