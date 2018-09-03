package datamining;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import knn.KNN;
import neuralnetwork.ActivationType;
import neuralnetwork.LossType;
import neuralnetwork.Matrix;
import neuralnetwork.NeuralNetwork;
import neuroevolution.Chromosome;
import neuroevolution.Population;

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
    
    public Trainer(League league, float testPercentage, boolean shuffle)
    {
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
     * 15. Bet365 home win odds
     * 16. Bet365 draw odds
     * 17. Bet365 away lose odds 
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
    
    public void train_NN(NeuralNetwork nn, int epoch)
    {
        nn.train(trainingInputs, trainingTargets,
                testingInputs, testingTargets, epoch);
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
            System.out.print(testingTargets[i]);
            System.out.println(nn.getOutput());
        }
        
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
            System.out.print("B365H: ");
            double B365H = sc.nextDouble();
            System.out.print("B365D: ");
            double B365D = sc.nextDouble();
            System.out.print("B365A: ");
            double B365A = sc.nextDouble();

            Matrix inputs;
            inputs = new Matrix(17, 1);

            // Home team features
            inputs.data[0][0] = Trainer.normalize(lg.minGD, lg.maxGD, homeTeam.GD);
            inputs.data[1][0] = homeTeam.getPercentageOfWin();
            inputs.data[2][0] = homeTeam.getPercentageOfDraw();
            inputs.data[3][0] = homeTeam.getPercentageOfLose();
            inputs.data[4][0] = homeTeam.getPercentageOfHomeWin();
            inputs.data[5][0] = homeTeam.getPercentageOfHomeDraw();
            inputs.data[6][0] = homeTeam.getPercentageOfHomeLose();

            // Away team features
            inputs.data[7][0] = Trainer.normalize(lg.minGD, lg.maxGD, awayTeam.GD);
            inputs.data[8][0] = awayTeam.getPercentageOfWin();
            inputs.data[9][0] = awayTeam.getPercentageOfDraw();
            inputs.data[10][0] = awayTeam.getPercentageOfLose();
            inputs.data[11][0] = awayTeam.getPercentageOfAwayWin();
            inputs.data[12][0] = awayTeam.getPercentageOfAwayDraw();
            inputs.data[13][0] = awayTeam.getPercentageOfAwayLose();

            // Bet365 odds
            inputs.data[14][0] = Trainer.normalize(lg.minB365H, lg.maxB365H, B365H);
            inputs.data[15][0] = Trainer.normalize(lg.minB365D, lg.maxB365D, B365D);
            inputs.data[16][0] = Trainer.normalize(lg.minB365A, lg.maxB365A, B365A);

            nn.setInput(inputs);
            nn.feedForward();

            System.out.println(homeTeam.name + " - " + awayTeam.name);
            System.out.println(nn.getOutput());
        }   
    }
    
    public void train_NE()
    {
        // We should call this method before creating a population!
        // It will set parameters for neural network.
        Chromosome.initGene(
            inputs,
            targets,
            new int[]{17, 13, 24, 3},
            new ActivationType[]{
                ActivationType.SIGMOID,
                ActivationType.SIGMOID,
                ActivationType.SOFTMAX
            },
            LossType.CROSS_ENTROPY
//            new int[]{2, 74, 60, 1},
//            new ActivationType[]{
//                ActivationType.TANH,
//                ActivationType.TANH,
//                ActivationType.TANH
//            },
//            LossType.MSE
        );
        
        Population population = new Population(120, 0.01);
        
        for(int i = 0; i < 1005; ++i) {
            population.evolve();
            System.out.print(population.getFittest());
        }
    }
    
    public void test_KNN(KNN knn)
    {
        knn.setDataset(trainingInputs);
        knn.setTargets(trainingTargets);

        int correct = 0;

        // Test out testing data.
        for(int i = 0; i < testingInputs.length; i++)
        {
            Matrix output = knn.predict(testingInputs[i], 140);

            System.out.println("\nTest#" + (i + 1));
            testingMatches[i].print();
            System.out.print(testingTargets[i]);
            System.out.println(output.toString());

            if(output.getMaxRow() == testingTargets[i].getMaxRow())
                correct++;
        }

        System.out.println(String.format("Accuracy: %.2f%%" , ((double) correct) / testingInputs.length * 100.0));        
    }
    
    public static double normalize(double oldMin, double oldMax, double value)
    {
        return ((value - oldMin) / (oldMax - oldMin)) * (1.0 - (-1.0)) + (-1.0);
    }
}
