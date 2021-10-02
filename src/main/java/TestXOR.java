import nn.model.Genome;
import nn.model.Population;
import nn.neural.NeuralNetwork;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class TestXOR {
    public static void main(String[] args) {
        double[][] trainingData = new double[][]{{0, 0, 0}, {1, 0, 1}, {0, 1, 1}, {1, 1, 0}};
//        double[][] trainingData = new double[][]{{0, 0, 1}, {1, 1, 1}, {1, 0, 0}, {1, 0, 0}};
        Population population = new Population(new Random(1), 1000, null);

        for (int i = 0; i < 500; i++) {
            generation(trainingData, population);
        }

        NeuralNetwork neuralNetwork = new NeuralNetwork(population.best.getGenome());
        for (double[] trainingDatum : trainingData) {
            double[] calculate = neuralNetwork.calculate(new double[]{trainingDatum[0], trainingDatum[1],0,0});
            System.out.println(String.format("XOR: %.1f, %.1f = %.1f", trainingDatum[0], trainingDatum[1], calculate[0]));
        }
        System.out.println(population.best);

    }

    private static void generation(double[][] trainingData, Population population) {
        Map<Genome, Double> genomeFitnessMap = new HashMap<>();
        List<Genome> genomes = population.getGenomes();
        for (Genome genome : genomes) {
            NeuralNetwork neuralNetwork = new NeuralNetwork(genome);
            double fitness = 0;
            try {
                for (double[] trainingDatum : trainingData) {
                    double[] calculate = neuralNetwork.calculate(new double[]{trainingDatum[0], trainingDatum[1],0,0});
                    var a = 1 - Math.abs(trainingDatum[2] - calculate[0]);
                    fitness += a;
                }
            } catch (RuntimeException e) {
                fitness = 0;
                System.out.println(e.getMessage());
            }
            if (fitness > 0) {
                fitness = Math.pow(fitness, 2);
            }

            genomeFitnessMap.put(genome, fitness);
        }
        population.evaluate(genomeFitnessMap);
    }
}
