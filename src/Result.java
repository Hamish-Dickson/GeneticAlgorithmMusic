import org.jfree.data.xy.XYDataset;

import java.util.ArrayList;
import java.util.Collections;

public class Result {
    private ArrayList<String> population;
    private ArrayList<Double> scores;
    private double averageScore;
    private double maxScore;

    public Result(ArrayList<String> population, ArrayList<Double> scores) {
        this.population = population;
        this.scores = scores;
        double total = 0;
        for (double score : scores) {
            total += score;
        }
        averageScore = total/population.size();
        maxScore = Collections.max(scores);
    }

    public ArrayList<String> getPopulation() {
        return population;
    }

    public ArrayList<Double> getScores() {
        return scores;
    }

    public double getAverageScore() {
        return averageScore;
    }

    public double getMaxScore() {
        return maxScore;
    }
}
