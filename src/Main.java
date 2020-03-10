/**
 * @author Hamish Dickson
 */
public class Main {
    public static void main(String[] args) {
        GeneticAlgorithm ga = new GeneticAlgorithm(500);
        Result result = ga.start();
        result.writeCSV("C:\\Users\\bktzg\\test.csv");
    }
}
