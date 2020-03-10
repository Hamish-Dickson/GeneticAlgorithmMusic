import org.jfree.chart.plot.FastScatterPlot;
import org.jfugue.player.Player;
import org.jfree.chart.*;

import java.util.*;

/**
 * Implementation of a genetic algorithm for the generation of music
 *
 * @author Hamish Dickson
 */
class GeneticAlgorithm {
    private final Map<Integer, String> notes = new HashMap<>() {{
        put(1, "D5");
        put(2, "E5");
        put(3, "F#5");
        put(4, "G5");
        put(5, "A5");
        put(6, "B5");
        put(7, "C#5");
        put(8, "D6");
    }};//d major

    private int currGeneration = 0;
    private int maxGenerations;

    private final int POPULATION_SIZE = 100;
    private final int SOLUTION_LENGTH = 32;
    private final int POSSIBLE_NOTES = notes.size();

    private final double EVALUATION_WEIGHT = 0.8;
    private final double ENTROPY_WEIGHT = 0.2;

    private final int CROSSOVER_POINTS = 1;
    private final int MUTATION_STRENGTH = 1;
    private final int MUTATION_POINTS = 1;

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
    Result start() {
        ArrayList<String> population = generatePopulation();//initial random population to be used
        ArrayList<Double> scores = new ArrayList<>();
        ArrayList<Double> improvingScores = new ArrayList<>();
        ArrayList<Double> averageScores = new ArrayList<>();
        String eliteCandidate = " ";

        while (currGeneration < maxGenerations) {
            Collections.shuffle(population);//shuffle so as parents should not re-pair from last generation.
            scores = evaluatePopulation(population);
            improvingScores.add(Collections.max(scores));
            averageScores.add(getAverage(scores));
            eliteCandidate = elitism(population, scores);
            //printGenStats(population, scores, eliteCandidate);
            population = selection(population, scores);
            population = crossover(population);
            population = mutation(population);
            population = consolidatePopulation(population, eliteCandidate);
            currGeneration++;
        }
        //playSolution(population, scores);

        return new Result(population, scores, improvingScores, averageScores);
    }

    private Double getAverage(ArrayList<Double> scores) {
        double total = 0;
        for (Double score : scores) {
            total += score;
        }
        return total / scores.size();
    }

    private void printGenStats(ArrayList<String> population, ArrayList<Double> scores, String eliteCandidate) {
        System.out.println("Generation: " + currGeneration);
        System.out.println("Scored population: ");
        double total = 0;
        for (int i = 0; i < population.size(); i++) {
            //System.out.println(population.get(i) + "\t\t=\t\t" + scores.get(i));
            total += scores.get(i);
        }
        System.out.println("\nPopulation size is currently: " + population.size());
        System.out.println("Average score is: " + Math.round((total / population.size()) * 100) / 100);
        System.out.println("The highest score is: " + Collections.max(scores));
        System.out.println("The elite candidate this generation was: " + eliteCandidate + "\n");
    }

    /**
     * weights the evaluation according to the weightings defined
     *
     * @param evaluation the evaluation score
     * @param entropy    the entropy score
     * @return the weighted score
     */
    private double weightedScore(double evaluation, double entropy) {
        return Math.round((evaluation * EVALUATION_WEIGHT + entropy * ENTROPY_WEIGHT) * 100) / 100.0;
    }

    /**
     * calculates the average "distance" between notes
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

        return entropy / solutionNotes.length;
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
     * @param population a pre-evaluated population set
     * @return the elite candidate
     */
    private String elitism(ArrayList<String> population, ArrayList<Double> scores) {
        String bestSolution = "";
        double bestScore = 0;

        for (Double score : scores) {
            if (score > bestScore) {
                bestSolution = population.get(scores.indexOf(score));
                bestScore = score;
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

        for (int i = 0; i < population.size(); i += 1) {
            StringBuilder firstCandidate = new StringBuilder(), secondCandidate = new StringBuilder();
            String[] firstNotes = population.get(i).split(" ");
            String[] secondNotes;

            if (i == population.size() - 1) {
                secondNotes = population.get(0).split(" ");
            } else {
                secondNotes = population.get(i + 1).split(" ");
            }

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
        ArrayList<Integer> posToChange = new ArrayList<>();

        Random random = new Random();

        for (int i = 0; i < MUTATION_POINTS; i++) {
            posToChange.add(random.nextInt(solutionNotes.length));
        }

        boolean increase = random.nextBoolean();

        ArrayList<String> notesToAugment = new ArrayList<>();
        for (int i = 0; i < MUTATION_POINTS; i++) {
            notesToAugment.add(solutionNotes[posToChange.get(i)]);
        }

        int keyToAugment = 1;

        for (String noteToAugment : notesToAugment) {
            //lookup the key of the note to augment
            for (Integer key : notes.keySet()) {
                if (notes.get(key).equals(noteToAugment)) {
                    keyToAugment = key;
                }
            }

            for (int i = 0; i < MUTATION_STRENGTH; i++) {
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
            }
            solutionNotes[posToChange.get(notesToAugment.indexOf(noteToAugment))] = notes.get(keyToAugment);
        }
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
    private ArrayList<String> selection(ArrayList<String> population, ArrayList<Double> scores) {
        ArrayList<String> newPopulation = new ArrayList<>();

        for (int i = 0; i < population.size() - 1; i += 2) {
            newPopulation.add(tournament(population, scores, i, i + 1));
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
    private String tournament(ArrayList<String> population, ArrayList<Double> scores, int firstCandidate, int secondCandidate) {
        String victor = "";

        int randomInt = new Random().nextInt(100);

        if (scores.get(firstCandidate) > scores.get(secondCandidate) && randomInt < 75) {
            victor = population.get(firstCandidate);
        } else {
            victor = population.get(secondCandidate);
        }

        return victor;
    }

    /**
     * evaluates the given population
     *
     * @param population the population of tunes to be evaluated
     * @return a set of tunes with their associated scores
     */
    private ArrayList<Double> evaluatePopulation(ArrayList<String> population) {
        ArrayList<Double> scoredPopulation = new ArrayList<>();

        //test scoring system so i can develop rest of GA
        for (String s : population) {
            scoredPopulation.add(weightedScore(evaluateTune(s), entropy(s)));
        }

        return scoredPopulation;
    }

    private double evaluateTune(String s) {
        double fitness = 0;

        boolean chord1 = false, chord2 = false, chord3, chord4 = false, chord5 = false, chord6 = false;

        //Solution contains notes for chord I
        if ((s.contains(notes.get(1)) || s.contains(notes.get(8))) && s.contains(notes.get(3)) && s.contains(notes.get(5))) {
            fitness += 10;
            chord1 = true;
        }
        //solution contains notes for chord VI
        if ((s.contains(notes.get(1)) || s.contains(notes.get(8))) && s.contains(notes.get(3)) && s.contains(notes.get(6))) {
            fitness += 8;
            chord6 = true;
        }
        //solution contains notes for chord V
        if (s.contains(notes.get(2)) && s.contains(notes.get(5)) && s.contains(notes.get(7))) {
            fitness += 8;
            chord5 = true;
        }
        //solution contains notes for chord IV
        if (s.contains(notes.get(4)) && s.contains(notes.get(6)) && (s.contains(notes.get(8)) || s.contains(notes.get(1)))) {
            fitness += 6;
            chord4 = true;
        }
        //solution contains notes for chord II
        if (s.contains(notes.get(2)) && s.contains(notes.get(4)) && s.contains(notes.get(6))) {
            fitness += 5;
            chord2 = true;
        }
        //solution contains notes for chord III
        if (s.contains(notes.get(3)) && s.contains(notes.get(5)) && s.contains(notes.get(7))) {
            fitness += 4;
            chord3 = true;
        }
        //solution contains notes for chord VII dim
        if (s.contains(notes.get(7)) && s.contains(notes.get(2)) && s.contains(notes.get(4))) {
            fitness += 2;
        }
        //solution contains notes for chord VII min 7th
        if (s.contains(notes.get(7)) && s.contains(notes.get(2)) && s.contains(notes.get(4)) && s.contains(notes.get(6))) {
            fitness += 4;
        }

        if (chord1 && chord6 && chord4 && chord5) {
            fitness += 10;
        } else if (chord1 && chord4 && chord5) {
            fitness += 6;
        } else if (chord2 && chord5 && chord1) {
            fitness += 3;
        }
        return fitness;
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
    private void playSolution(ArrayList<String> population, ArrayList<Double> scores) {
        ArrayList<String> solutions = getBest(population, scores);

        for (String solution : solutions) {
            Player player = new Player();
            player.play(solution);
        }
    }

    /**
     * retrieves the best solutions from the population so they can be played
     *
     * @param population the population
     * @return the best tunes
     */
    private ArrayList<String> getBest(ArrayList<String> population, ArrayList<Double> scores) {
        ArrayList<String> best = new ArrayList<>();

        while (best.size() < 3) {
            String bestSolution = elitism(population, scores);
            best.add(bestSolution);
            scores.remove(population.indexOf(bestSolution));
            population.remove(bestSolution);
        }

        return best;
    }
}
