package datamining;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import neuralnetwork.ActivationType;
import neuralnetwork.LossType;
import neuralnetwork.Matrix;
import neuralnetwork.NeuralNetwork;

/**
 *
 * @author tnc
 */
public class Trainer
{
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
    private final TrainingType trainingType;
    
    public Trainer(League league, float testPercentage,
            boolean shuffle, TrainingType trainingType)
    {
        this.league = league;
        this.sampleCount = league.getMatchCount();
        this.trainingType = trainingType;
        
        extractFeatures();
        initialize(shuffle, testPercentage);
    }
    
    /**
     * Features:
     * 1. Goal differences of home team
     * 2. The percentage of winning when home team was home
     * 3. The percentage of draw when home team was home
     * 4. The percentage of lose when home team was home
     * 5. Goal differences of away team
     * 6. The percentage of winning when away team was away
     * 7. The percentage of draw when away team was away
     * 8. The percentage of lose when away team was away
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

            inputs[idx] = new Matrix(8, 1);
            
            // Home team features
            inputs[idx].data[0][0] = normalize(league.minGD, league.maxGD, 0.0, 1.0, m.homeTeam.GD);
            inputs[idx].data[1][0] = m.homeTeam.getPercentageOfHomeWin();
            inputs[idx].data[2][0] = m.homeTeam.getPercentageOfHomeDraw();
            inputs[idx].data[3][0] = m.homeTeam.getPercentageOfHomeLose();
            
            // Away team features
            inputs[idx].data[4][0] = normalize(league.minGD, league.maxGD, 0.0, 1.0, m.awayTeam.GD);
            inputs[idx].data[5][0] = m.awayTeam.getPercentageOfAwayWin();
            inputs[idx].data[6][0] = m.awayTeam.getPercentageOfAwayDraw();
            inputs[idx].data[7][0] = m.awayTeam.getPercentageOfAwayLose();

            switch(trainingType)
            {
                case FULL_TIME_RESULT:
                    targets[idx] = new Matrix(3, 1);
                    targets[idx].data[0][0] = (m.FTR == 'H') ? 1.0 : 0.0;
                    targets[idx].data[1][0] = (m.FTR == 'D') ? 1.0 : 0.0;
                    targets[idx].data[2][0] = (m.FTR == 'A') ? 1.0 : 0.0;
                    break;
                case PREDICT_SCORE:
                    targets[idx] = new Matrix(2, 1);
                    targets[idx].data[0][0] = normalize(league.minGoals, league.maxGoals, 0.0, 1.0, m.FTHG);
                    targets[idx].data[1][0] = normalize(league.minGoals, league.maxGoals, 0.0, 1.0, m.FTAG);
                    break;
                case OVER_UNDER:
                    targets[idx] = new Matrix(1, 1);
                    targets[idx].data[0][0] = (m.FTHG + m.FTAG > 2.5) ? 1.0 : 0.0;
                    break;
                case FTR_REDUCED:
                    targets[idx] = new Matrix(2, 1);
                    targets[idx].data[0][0] = (m.FTR == 'H') ? 1.0 : ((m.FTR == 'A') ? 0.0 : 0.5);
                    targets[idx].data[1][0] = (m.FTR == 'A') ? 1.0 : ((m.FTR == 'H') ? 0.0 : 0.5);
                    break;
                default:
                    System.err.println("Undefined TrainingType.");
                    break;
            }
            
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
    
    public void train_NN(int epoch)
    {
        NeuralNetwork nn = new NeuralNetwork(
            new int[]{inputs[0].rows, 20, 30, targets[0].rows},
            new ActivationType[]{
                ActivationType.SIGMOID,
                ActivationType.SIGMOID,
                ActivationType.SOFTMAX
            },
            LossType.CROSS_ENTROPY,
            0.001
        );
        
        NeuralNetwork bestNN = null;
        int bestCorrectCount = -1;
        int bestEpoch = 0;
        double bestAverageLoss = 0.0;
        
        for(int i = 0; i < epoch; i++)
        {
            double averageLoss = 0.0;
            for(int j = 0; j < trainingInputs.length; j++)
            {
                nn.setInput(trainingInputs[j]);
                nn.setTarget(trainingTargets[j]);
                nn.feedForward();
                nn.backpropagation();
                averageLoss += nn.getLoss();
            }
            averageLoss /= trainingInputs.length;
            
            int correct = 0;
            for(int j = 0; j < testingInputs.length; j++)
            {
                nn.setInput(testingInputs[j]);
                nn.setTarget(testingTargets[j]);
                nn.feedForward();
                
                switch(trainingType)
                {
                    case FULL_TIME_RESULT:
                        if(testingTargets[j].getMaxRow() == nn.getOutput().getMaxRow())
                            correct++;
                        break;
                    case PREDICT_SCORE:
                        if(Math.round(nn.getOutput().data[0][0] * league.maxGoals) == testingMatches[j].FTHG &&
                            Math.round(nn.getOutput().data[1][0] * league.maxGoals) == testingMatches[j].FTAG)
                        {
                            correct++;
                        }
                        break;
                    case OVER_UNDER:
                        if(Math.abs(nn.getOutput().data[0][0] - testingTargets[j].data[0][0]) < 0.5)
                            correct++;
                        break;
                    case FTR_REDUCED:
                        if(testingMatches[j].FTR == 'D')
                        {
                            if(Math.abs(nn.getOutput().data[0][0] - nn.getOutput().data[1][0]) < 0.1)
                            {
                                correct++;
                            }
                        }
                        else
                        {
                            if(testingTargets[j].getMaxRow() == nn.getOutput().getMaxRow())
                                correct++;
                        }
                                
                        break;
                }
            }
            
            if(correct > bestCorrectCount)
            {
                bestNN = new NeuralNetwork(nn);
                bestCorrectCount = correct;
                bestAverageLoss = averageLoss;
                bestEpoch = i + 1;
            }
            
            printLog(i, averageLoss, correct, testingInputs.length);
        }

        test_NN(bestNN);
        printBestEpoch(bestEpoch, bestAverageLoss, bestCorrectCount, testingInputs.length);
        testNewData(bestNN);
    }
    
    public void test_NN(NeuralNetwork nn)
    {
        // Test out testing data.
        for(int i = 0; i < testingInputs.length; i++)
        {
            nn.setInput(testingInputs[i]);
            nn.setTarget(testingTargets[i]);
            nn.feedForward();

            System.out.println("\nTest#" + (i + 1));
            testingMatches[i].print();
            
            switch(trainingType)
            {
                case FULL_TIME_RESULT:
                case OVER_UNDER:
                case FTR_REDUCED:
                    System.out.println(testingTargets[i]);
                    System.out.println(nn.getOutput());
                    break;
                case PREDICT_SCORE:
                    System.out.print(Matrix.mult(testingTargets[i], league.maxGoals));
                    System.out.println(Matrix.mult(nn.getOutput(), league.maxGoals));
                    break;
            }
        }
    }
    
    private void testNewData(NeuralNetwork nn)
    {
        while(true)
        {
            // Predict new inputs.
            Scanner sc = new Scanner(System.in);
            League lg = league;
            Team[] teams = lg.getTeams();

            for(int i = 0; i < teams.length; i++)
                System.out.println(String.format("%-15s -> %d", teams[i].name, i));

            System.out.print("\nHome Team: ");
            Team homeTeam = teams[sc.nextInt()];
            System.out.print("Away Team: ");
            Team awayTeam = teams[sc.nextInt()];

            Matrix _inputs;
            _inputs = new Matrix(8, 1);

            // Home team features
            _inputs.data[0][0] = Trainer.normalize(lg.minGD, lg.maxGD, 0.0, 1.0, homeTeam.GD);
            _inputs.data[1][0] = homeTeam.getPercentageOfHomeWin();
            _inputs.data[2][0] = homeTeam.getPercentageOfHomeDraw();
            _inputs.data[3][0] = homeTeam.getPercentageOfHomeLose();

            // Away team features
            _inputs.data[4][0] = Trainer.normalize(lg.minGD, lg.maxGD, 0.0, 1.0, awayTeam.GD);
            _inputs.data[5][0] = awayTeam.getPercentageOfAwayWin();
            _inputs.data[6][0] = awayTeam.getPercentageOfAwayDraw();
            _inputs.data[7][0] = awayTeam.getPercentageOfAwayLose();

            nn.setInput(_inputs);
            nn.feedForward();

            System.out.println(homeTeam.name + " - " + awayTeam.name);
            
            switch(trainingType)
            {
                case FULL_TIME_RESULT:
                case OVER_UNDER:
                case FTR_REDUCED:
                    System.out.println(nn.getOutput());
                    break;
                case PREDICT_SCORE:
                    System.out.println(Matrix.mult(nn.getOutput(), lg.maxGoals));
                    break;
            }               
        }
    }
    
    private void printLog(int epoch, double averageLoss, int correct, int total)
    {
        String epochStr = String.format("Epoch: %d, ", epoch + 1);
        String lossStr  = String.format("Loss = %.10f, ", averageLoss);
        String accuracyStr = String.format("Accuracy: %.0f%% (%d/%d)",
                (((double) correct) / total) * 100.0, correct, total);
        
        System.out.println(epochStr + lossStr + accuracyStr);
    }
    
    private void printBestEpoch(int epoch, double averageLoss, int correct, int total)
    {
        System.out.println("Best Epoch:");
        printLog(epoch - 1, averageLoss, correct, total);     
        System.out.println();
    }
    
    public static double normalize(double oldMin, double oldMax, double newMin, double newMax, double value)
    {
        return ((value - oldMin) / (oldMax - oldMin)) * (newMax - newMin) + (newMin);
    }
}
