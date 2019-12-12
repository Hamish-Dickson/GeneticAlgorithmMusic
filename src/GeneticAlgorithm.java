import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.jfugue.player.Player;

/**
 * Implementation of a genetic algorithm for the generation of music
 *
 * @author Hamish Dickson
 */
class GeneticAlgorithm {
    private final Map<Integer, String> notes = new HashMap<>() {{
        put(1, "C#");
        put(2, "D");
        put(3, "E");
        put(4, "F#");
        put(5, "G");
        put(6, "A");
        put(7, "B");
    }};

    private int currGeneration = 0;
    private int maxGenerations;

    private final int POPULATION_SIZE = 5;
    private final int SOLUTION_LENGTH = 10;
    private final int POSSIBLE_NOTES = notes.size();


    /**
     * Constructor for the genetic algorithm
     *
     * @param generations the number of generations the algorithm should run for
     */
    GeneticAlgorithm(int generations) {
        maxGenerations = generations;
    }

    /**
     * Starts the genetic algorithm process
     */
    void start() {
        ArrayList<String> population = generatePopulation();//initial random population to be used
        Map<String, Double> scoredPopulation = new HashMap<>();
        String eliteCandidate = "";

        while (currGeneration < maxGenerations) {
            if (population != null) {
                scoredPopulation = evaluatePopulation(population);
            }
            eliteCandidate = elitism(scoredPopulation);
            population = selection(scoredPopulation);
            crossover(population);
            mutation(population);
            currGeneration++;
        }
        System.out.println("The Weighted Population: " + scoredPopulation);
        System.out.println("The Elite Candidate: " + eliteCandidate);
        //playSolution(population);
    }

    /**
     * Finds the best candidate from a given population
     *
     * @param scoredPopulation a pre-evaluated population set
     * @return the elite candidate
     */
    private String elitism(Map<String, Double> scoredPopulation) {
        String bestSolution = "";
        double bestScore = 0;

        for (String solution : scoredPopulation.keySet()) {
            double solutionScore = scoredPopulation.get(solution);
            if (solutionScore > bestScore) {
                bestSolution = solution;
                bestScore = solutionScore;
            }
        }

        return bestSolution;
    }

    /**
     * uses the JFugue player to playback the given population
     *
     * @param population the population of tunes to be played
     */
    private void playSolution(ArrayList<String> population) {
        String solution = population.get(0);
        //TODO select song(s) to play

        Player player = new Player();
        player.play(solution);
        //player.play("CmajQ AmajQ BmajQ BmajQ AmajQ GmajQ EmajQ DmajQ");
        //player.play("V0 [] D4q F4q G4q. | D4i Ri F4i Ri Ab4i G4h | D4q F4q G4q. | F4i D4qh.");
    }

    private void mutation(ArrayList<String> population) {
        //TODO implement mutation operator
    }

    private void crossover(ArrayList<String> population) {
        //TODO implement crossover operator
    }

    private ArrayList<String> selection(Map<String, Double> population) {
        ArrayList<String> newPopulation = new ArrayList<>();
        //TODO implement selection of population
        for (int i = 0; i < population.size()-1; i++) {
            newPopulation.add(tournament(population, i, i + 1));
        }

        System.out.println(newPopulation);

        return newPopulation;
    }

    private String tournament(Map<String, Double> population, int firstCandidate, int secondCandidate) {
        String victor = "";
        Object[] solutions = population.keySet().toArray();

        int randomInt = new Random().nextInt(100);

        if (population.get(solutions[firstCandidate]) > population.get(solutions[secondCandidate]) && randomInt < 75) {
            victor = (String) solutions[firstCandidate];
        } else {
            victor = (String) solutions[secondCandidate];
        }

        return victor;
    }

    /**
     * evaluates the given population
     *
     * @param population the population of tunes to be evaluated
     * @return a set of tunes with their associated scores
     */
    private Map<String, Double> evaluatePopulation(ArrayList<String> population) {
        //TODO implement evaluation criteria
        Map<String, Double> scoredPopulation = new HashMap<>();

        //test scoring system so i can develop rest of GA
        for (int i = 0; i < population.size(); i++) {
            scoredPopulation.put(population.get(i), (double) i);
        }

        return scoredPopulation;
    }

    /**
     * generates a population set
     *
     * @return a set of randomly generated tunes, represented as strings
     */
    private ArrayList<String> generatePopulation() {
        ArrayList<String> population = new ArrayList<>();

        for (int i = 0; i < POPULATION_SIZE; i++) {
            population.add(generateSolution());
        }
        System.out.println(population.toString());
        return population;
    }

    /**
     * generates a single solution
     *
     * @return a single tune, represented as a string
     */
    private String generateSolution() {
        StringBuilder solution = new StringBuilder();
        Random random = new Random();
        System.out.println("Notes available: " + notes.values());
        for (int i = 0; i < SOLUTION_LENGTH; i++) {
            int noteSelector = random.nextInt(POSSIBLE_NOTES) + 1;
            solution.append(notes.get(noteSelector)).append(" ");
        }

        System.out.println(solution.toString());
        return solution.toString();
    }
}
