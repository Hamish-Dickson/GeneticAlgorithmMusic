/**
 * @author Hamish Dickson
 */
public class Main {
    public static void main(String[] args) {
        GeneticAlgorithm ga = new GeneticAlgorithm(200);
        Result result = ga.start();
        result.writeCSV("C:\\Users\\bktzg\\multipoint");
    }
}
