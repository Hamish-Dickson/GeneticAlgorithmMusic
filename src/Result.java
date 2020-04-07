import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;

public class Result {
    private ArrayList<Double> improvingScores;
    private ArrayList<Double> averageScores;
    public ArrayList<String> population;
    private double maxScore;

    public Result(ArrayList<Double> improvingScores, ArrayList<Double> averageScores, ArrayList<String> population) {
        this.improvingScores = improvingScores;
        this.averageScores = averageScores;
        this.population = population;
    }

    public double getMaxScore() {
        return maxScore;
    }

    public ArrayList<Double> getImprovingScores() {
        return improvingScores;
    }

    public ArrayList<Double> getAverageScores() {
        return averageScores;
    }

    public void writeCSV(String filepath) {
        PrintWriter pw = null;
        File improvesFile = new File(filepath + " improves" + ".csv");
        File averagesFile = new File(filepath + " averages" + ".csv");
        File popFile = new File(filepath + " population" + ".csv");
        System.out.println("printing results");
        try {
            pw = new PrintWriter(improvesFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        for (double score : improvingScores) {
            //System.out.println("max printing: " + score);
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
        pw.flush();

        try {
            pw = new PrintWriter(popFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        for (String pop : population) {
            pw.write(String.valueOf(pop));
            pw.write(",");
        }

        pw.close();
    }
}
