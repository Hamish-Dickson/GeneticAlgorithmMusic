import org.jfugue.player.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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

    private final int POPULATION_SIZE = 10;
    private final int SOLUTION_LENGTH = 10;
    private final int POSSIBLE_NOTES = notes.size();

    private final double EVALUATION_WEIGHT = 0.7;
    private final double ENTROPY_WEIGHT = 0.3;


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
        Map<String, Double> scoredPopulation;
        String eliteCandidate;

        while (currGeneration < maxGenerations) {
            scoredPopulation = evaluatePopulation(population);
            eliteCandidate = elitism(scoredPopulation);
            population = selection(scoredPopulation);
            population = crossover(population);
            population = mutation(population);
            population = consolidatePopulation(population, eliteCandidate);
            currGeneration++;

            System.out.println("Generation: " + currGeneration);
            System.out.println("Scored population: " + scoredPopulation);
            System.out.println("The elite candidate this generation was: " + eliteCandidate + "\n");


        }
        //playSolution(population);
    }

    /**
     * weights the evaluation according to the weightings defined
     *
     * @param evaluation the evaluation score
     * @param entropy    the entropy score
     * @return the weighted score
     */
    private double weightedScore(double evaluation, double entropy) {
        return (evaluation * EVALUATION_WEIGHT) + (entropy * ENTROPY_WEIGHT);
    }

    /**
     * calculates the total "distance" between notes
     *
     * @param solution a single tune
     * @return the total distance of the tune
     */
    private double entropy(String solution) {
        double entropy = 0.0;

        String[] solutionNotes = solution.split(" ");

        for (int i = 0; i < solutionNotes.length - 1; i++) {
            String firstNote = solutionNotes[i];
            String secondNote = solutionNotes[i + 1];
            int firstKey = 0, secondKey = 0;

            //must lookup the key assigned to the note, giving a "position", usable for distance calculation
            for (Integer key : notes.keySet()) {
                if (firstNote.equals(notes.get(key))) {
                    firstKey = key;
                }
                if (secondNote.equals(notes.get(key))) {
                    secondKey = key;
                }
            }

            entropy += Math.abs(firstKey - secondKey);
        }

        return entropy;
    }

    /**
     * adds the elite candidate back into the population
     *
     * @param population     the population of tunes
     * @param eliteCandidate the elite candidate solution
     * @return the complete population including the elite candidate
     */
    private ArrayList<String> consolidatePopulation(ArrayList<String> population, String eliteCandidate) {
        ArrayList<String> newPopulation = new ArrayList<>(population);
        newPopulation.add(eliteCandidate);
        return newPopulation;
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
     * implementation of a single point crossover
     *
     * @param population the population of tunes
     * @return the crossed over population
     */
    private ArrayList<String> crossover(ArrayList<String> population) {
        ArrayList<String> newPopulation = new ArrayList<>();

        for (int i = 0; i < population.size() - 1; i += 2) {
            StringBuilder firstCandidate = new StringBuilder(), secondCandidate = new StringBuilder();
            String[] firstNotes = population.get(i).split(" ");
            String[] secondNotes = population.get(i + 1).split(" ");

            for (int j = 0; j < firstNotes.length; j++) {
                if (j > firstNotes.length / 2) {
                    firstCandidate.append(firstNotes[j]).append(" ");
                    secondCandidate.append(secondNotes[j]).append(" ");
                } else {
                    firstCandidate.append(secondNotes[j]).append(" ");
                    secondCandidate.append(firstNotes[j]).append(" ");
                }
            }

            newPopulation.add(firstCandidate.toString());
            newPopulation.add(secondCandidate.toString());
        }
        return newPopulation;
    }

    /**
     * mutation operator
     *
     * @param population the population of tunes
     * @return the mutated population
     */
    private ArrayList<String> mutation(ArrayList<String> population) {
        ArrayList<String> newPopulation = new ArrayList<>();

        for (String member : population) {
            newPopulation.add(augment(member));
        }

        return newPopulation;
    }

    /**
     * increment or decrements a random note in the solution
     *
     * @param solution
     * @return
     */
    private String augment(String solution) {
        StringBuilder mutatedSolution = new StringBuilder();
        String[] solutionNotes = solution.split(" ");

        Random random = new Random();
        int posToChange = random.nextInt(solutionNotes.length);
        boolean increase = random.nextBoolean();

        String noteToAugment = solutionNotes[posToChange];
        int keyToAugment = 1;


        //lookup the key of the note to augment
        for (Integer key : notes.keySet()) {
            if (notes.get(key).equals(noteToAugment)) {
                keyToAugment = key;
            }
        }

        if (increase) {
            if (keyToAugment == notes.size()) {//if the note is the highest possible note available, reset to lowest
                keyToAugment = 1;
            } else {
                keyToAugment++;
            }
        } else {
            if (keyToAugment == 1) {//if the note is the lowest possible note available, reset to highest
                keyToAugment = notes.size();
            } else {
                keyToAugment--;
            }
        }

        solutionNotes[posToChange] = notes.get(keyToAugment);

        for (String solutionNote : solutionNotes) {
            mutatedSolution.append(solutionNote).append(" ");
        }

        return mutatedSolution.toString();
    }


    /**
     * implements the selection operator
     *
     * @param population the current weighted population
     * @return the new population to be used for crossover
     */
    private ArrayList<String> selection(Map<String, Double> population) {
        ArrayList<String> newPopulation = new ArrayList<>();

        for (int i = 0; i < population.size() - 1; i++) {
            newPopulation.add(tournament(population, i, i + 1));
        }

        return newPopulation;
    }

    /**
     * implementation of a tournament selection
     *
     * @param population      the scored population
     * @param firstCandidate  the index of the first candidate in the population
     * @param secondCandidate the index of the second candidate in the population
     * @return the winning solution
     */
    private String tournament(Map<String, Double> population, int firstCandidate, int secondCandidate) {
        String victor = "";
        String[] solutions = population.keySet().toArray(new String[0]);

        int randomInt = new Random().nextInt(100);

        if (population.get(solutions[firstCandidate]) > population.get(solutions[secondCandidate]) && randomInt < 75) {
            victor = solutions[firstCandidate];
        } else {
            victor = solutions[secondCandidate];
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
            scoredPopulation.put(population.get(i), weightedScore(i, entropy(population.get(i))));
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
        //System.out.println("Notes available: " + notes.values());
        for (int i = 0; i < SOLUTION_LENGTH; i++) {
            int noteSelector = random.nextInt(POSSIBLE_NOTES) + 1;
            solution.append(notes.get(noteSelector)).append(" ");
        }

        //System.out.println(solution.toString());
        return solution.toString();
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
    }
}
