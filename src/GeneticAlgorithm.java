import java.util.*;

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

    private final int POPULATION_SIZE = 6;
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
            scoredPopulation = evaluatePopulation(population);
            eliteCandidate = elitism(scoredPopulation);
            population = selection(scoredPopulation);
            population = crossover(population);
            population = mutation(population);
            population = consolidatePopulation(population, eliteCandidate);
            currGeneration++;
            System.out.println("Scored Population: " + scoredPopulation);
        }
        //System.out.println("The Weighted Population: " + scoredPopulation);
        //System.out.println("The Elite Candidate: " + eliteCandidate);
        //playSolution(population);
    }

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

    private ArrayList<String> mutation(ArrayList<String> population) {
        ArrayList<String> newPopulation = new ArrayList<>();

        for (String member : population) {
            newPopulation.add(augment(member));
        }

        return population;
    }

    private String augment(String solution) {
        StringBuilder mutatedSolution = new StringBuilder();

        Random random = new Random();
        int posToChange = random.nextInt(solution.split(" ").length);
        boolean increase = random.nextBoolean();

        String[] solutionNotes = solution.split(" ");
        String noteToAugment = solutionNotes[posToChange];
        Integer keyToAugment = 0;


        for (Integer key : notes.keySet()) {
            if (notes.get(key).equals(noteToAugment)) {
                keyToAugment = key;
            }
        }

        if (increase) {
            if (keyToAugment == notes.size()) {
                keyToAugment = 0;
            } else {
                keyToAugment++;
            }
        } else {
            if (keyToAugment == 0) {
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

        //TODO implement selection of population
        for (int i = 0; i < population.size() - 1; i++) {
            newPopulation.add(tournament(population, i, i + 1));
        }

        return newPopulation;
    }

    /**
     * implementation of a tournament selection
     *
     * @param population
     * @param firstCandidate
     * @param secondCandidate
     * @return
     */
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
