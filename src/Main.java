/**
 * @author Hamish Dickson
 */
public class Main {
    public static void main(String[] args) {
        test();
    }

    private static void test() {
        int iterations = 100;
        int generations = 200;

        GeneticAlgorithm ga = new GeneticAlgorithm(iterations, generations, 0.8, 0.2, 1, 1, 1);
        Result result = ga.start();
        result.writeCSV("C:\\Users\\bktzg\\baseline");

        ga = new GeneticAlgorithm(iterations, generations, 0.8, 0.2, 2, 1, 1);
        result = ga.start();
        result.writeCSV("C:\\Users\\bktzg\\twopoint");

        ga = new GeneticAlgorithm(iterations, generations, 0.8, 0.2, 1, 3, 1);
        result = ga.start();
        result.writeCSV("C:\\Users\\bktzg\\aggressive");

        ga = new GeneticAlgorithm(iterations, generations, 0.8, 0.2, 1, 1, 2);
        result = ga.start();
        result.writeCSV("C:\\Users\\bktzg\\multipoint");

        ga = new GeneticAlgorithm(iterations, generations, 1, 0, 1, 1, 1);
        result = ga.start();
        result.writeCSV("C:\\Users\\bktzg\\1000");

        ga = new GeneticAlgorithm(iterations, generations, 0.9, 0.1, 1, 1, 1);
        result = ga.start();
        result.writeCSV("C:\\Users\\bktzg\\9010");

        ga = new GeneticAlgorithm(iterations, generations, 0.8, 0.2, 1, 1, 1);
        result = ga.start();
        result.writeCSV("C:\\Users\\bktzg\\8020");

        ga = new GeneticAlgorithm(iterations, generations, 0.7, 0.3, 1, 1, 1);
        result = ga.start();
        result.writeCSV("C:\\Users\\bktzg\\7030");

        ga = new GeneticAlgorithm(iterations, generations, 0.6, 0.4, 1, 1, 1);
        result = ga.start();
        result.writeCSV("C:\\Users\\bktzg\\6040");

        ga = new GeneticAlgorithm(iterations, generations, 0.5, 0.5, 1, 1, 1);
        result = ga.start();
        result.writeCSV("C:\\Users\\bktzg\\5050");

        ga = new GeneticAlgorithm(iterations, generations, 0.4, 0.6, 1, 1, 1);
        result = ga.start();
        result.writeCSV("C:\\Users\\bktzg\\4060");

        ga = new GeneticAlgorithm(iterations, generations, 0.3, 0.7, 1, 1, 1);
        result = ga.start();
        result.writeCSV("C:\\Users\\bktzg\\3070");

        ga = new GeneticAlgorithm(iterations, generations, 0.2, 0.8, 1, 1, 1);
        result = ga.start();
        result.writeCSV("C:\\Users\\bktzg\\2080");

        ga = new GeneticAlgorithm(iterations, generations, 0.1, 0.9, 1, 1, 1);
        result = ga.start();
        result.writeCSV("C:\\Users\\bktzg\\1090");

        ga = new GeneticAlgorithm(iterations, generations, 0, 1, 1, 1, 1);
        result = ga.start();
        result.writeCSV("C:\\Users\\bktzg\\0100");
    }
}