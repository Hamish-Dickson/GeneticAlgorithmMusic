import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;

public class Result {
    private ArrayList<String> population;
    private ArrayList<Double> scores;
    private ArrayList<Double> improvingScores;
    private ArrayList<Double> averageScores;
    private double maxScore;

    public Result(ArrayList<String> population, ArrayList<Double> scores, ArrayList<Double> improvingScores, ArrayList<Double> averageScores) {
        this.population = population;
        this.scores = scores;
        this.improvingScores = improvingScores;
        this.averageScores = averageScores;
        maxScore = Collections.max(scores);
    }

    public ArrayList<String> getPopulation() {
        return population;
    }

    public ArrayList<Double> getScores() {
        return scores;
    }

    public double getMaxScore() {
        return maxScore;
    }

    public void writeCSV(String filepath) {
        PrintWriter pw = null;
        File improvesFile = new File(filepath + " improves" + ".csv");
        File averagesFile = new File(filepath + " averages" + ".csv");
        System.out.println("printing results");
        try {
            pw = new PrintWriter(improvesFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        for (double score : improvingScores) {
            System.out.println("max printing: " + score);
            pw.write(String.valueOf(score));
            pw.write(",");
        }
        pw.close();
        pw.flush();

        try {
            pw = new PrintWriter(averagesFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        for (double score : averageScores) {
            pw.write(String.valueOf(score));
            pw.write(",");
        }

        pw.close();
    }
}
