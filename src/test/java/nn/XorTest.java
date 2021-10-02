package nn;

import nn.model.Connection;
import nn.model.Genome;
import nn.model.Node;
import nn.model.Type;
import nn.neural.NeuralNetwork;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

public class XorTest {

    @Test
    public void test() {
        double[][] trainingData = new double[][]{{0, 0, 0}, {1, 0, 1}, {0, 1, 1}, {1, 1, 0}};
        var neuralNetwork = new NeuralNetwork(xor());
        for (double[] trainingDatum : trainingData) {
            double[] calculate = neuralNetwork.calculate(new double[]{trainingDatum[0], trainingDatum[1]});
            System.out.println(String.format("XOR: %.1f, %.1f = %.1f", trainingDatum[0], trainingDatum[1], calculate[0]));
        }
    }

    private Genome xor() {
        Genome genome = new Genome(null, null, new Random(1));
        Node x1 = new Node(Type.SENSOR, 1);
        Node x2 = new Node(Type.SENSOR, 2);
        Node h1 = new Node(Type.HIDDEN, 3, -10);
        Node h2 = new Node(Type.HIDDEN, 4, 30);
        Node y = new Node(Type.OUTPUT, 5, -30);



        Connection x1h1 = new Connection(x1, h1, 1, true, 20);
        Connection x1h2 = new Connection(x1, h2, 2, true, -20);
        Connection x2h1 = new Connection(x2, h1, 3, true, 20);
        Connection x2h2 = new Connection(x2, h2, 4, true, -20);
        Connection h1y = new Connection(h1, y, 5, true, 20);
        Connection h2y = new Connection(h2, y, 6, true, 20);

        List.of(x1, x2, h1, h2, y).forEach(genome::addNode);
        List.of(x1h1, x1h2, x2h1, x2h2, h1y, h2y).forEach(genome::addConnection);
        return genome;
    }
}
