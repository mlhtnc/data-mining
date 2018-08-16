package datamining;

import static datamining.Trainer.normalize;
import java.util.Scanner;
import java.io.File;
import neuralnetwork.ActivationType;
import neuralnetwork.LossType;
import neuralnetwork.Matrix;
import neuralnetwork.NeuralNetwork;

/**
 *
 * @author tnc
 */
public class DataMining
{   
    // TODO:
    // -Make a user interface for predicting matches.
    //
    // -Softmax classifier
    
    
    // Data Path
    static final String DATA_PATH = System.getProperty("user.home") + File.separator +
            "Desktop" + File.separator + "data" + File.separator;
    
    // League Paths
    static final String PL_PATH = DATA_PATH + "pl" + File.separator;
    static final String TSL_PATH = DATA_PATH + "tsl" + File.separator;
    
    League pl = CSV_Reader.read(PL_PATH);
    NeuralNetwork nn;
    Trainer trainer;
    
    public static void main(String[] args)
    {
        DataMining dt = new DataMining();
        dt.train();
        dt.test();
    }
    
    public void train()
    {
        pl = CSV_Reader.read(PL_PATH);

        nn = new NeuralNetwork(
            new int[]{17, 13, 24, 3},
            new ActivationType[]{
                ActivationType.SIGMOID,
                ActivationType.SIGMOID,
                ActivationType.SIGMOID
            },
            LossType.MSE,
            0.05
        );
        
        trainer = new Trainer(nn, pl, 0.01f, true);
        trainer.train(500);      
        trainer.test();
    }
    
    public void test()
    {
        Scanner sc = new Scanner(System.in);
        pl = CSV_Reader.read(PL_PATH);
        Team[] teams = pl.getTeams();
        
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
        inputs.data[0][0] = normalize(pl.minGD, pl.maxGD, homeTeam.GD);
        inputs.data[1][0] = homeTeam.getPercentageOfWin();
        inputs.data[2][0] = homeTeam.getPercentageOfDraw();
        inputs.data[3][0] = homeTeam.getPercentageOfLose();
        inputs.data[4][0] = homeTeam.getPercentageOfHomeWin();
        inputs.data[5][0] = homeTeam.getPercentageOfHomeDraw();
        inputs.data[6][0] = homeTeam.getPercentageOfHomeLose();

        // Away team features
        inputs.data[7][0] = normalize(pl.minGD, pl.maxGD, awayTeam.GD);
        inputs.data[8][0] = awayTeam.getPercentageOfWin();
        inputs.data[9][0] = awayTeam.getPercentageOfDraw();
        inputs.data[10][0] = awayTeam.getPercentageOfLose();
        inputs.data[11][0] = awayTeam.getPercentageOfAwayWin();
        inputs.data[12][0] = awayTeam.getPercentageOfAwayDraw();
        inputs.data[13][0] = awayTeam.getPercentageOfAwayLose();

        // Bet365 odds
        inputs.data[14][0] = normalize(pl.minB365H, pl.maxB365H, B365H);
        inputs.data[15][0] = normalize(pl.minB365D, pl.maxB365D, B365D);
        inputs.data[16][0] = normalize(pl.minB365A, pl.maxB365A, B365A);
        
        nn.setInput(inputs);
        nn.feedForward();
        
        System.out.println(nn.getOutput());
    }
}
