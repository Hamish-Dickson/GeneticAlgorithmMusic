import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;

public class Result {
    private ArrayList<Double> improvingScores;
    private ArrayList<Double> averageScores;
    private double maxScore;

    public Result(ArrayList<Double> improvingScores, ArrayList<Double> averageScores) {
        this.improvingScores = improvingScores;
        this.averageScores = averageScores;
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
    }
}
