package nn.model.gene;

import nn.actions.Mating;
import nn.model.*;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MatingTest {

    Random random = new Random(1);

    @Test
    public void crossover() {
        Genome parent1 = parent1();
        Genome parent2 = parent2();
        Mating mating = new Mating(random, new NodeInnovator(), new ConnectionInnovator());
        Genome crossover = mating.crossover(new FitnessGenome(parent1, 1), new FitnessGenome(parent2, 2));

        Genome genome = getResultGenome();

        assertEquals(new HashSet<>(genome.getConnections()), new HashSet<>(crossover.getConnections()));

        assertEquals(genome.getNodes().size(), crossover.getNodes().size());
        for (Node node : genome.getNodes()) {
            assertTrue(crossover.getNodes().contains(node));
        }

       assertTrue(crossover.getConnections().containsAll(genome.getConnections()) && genome.getConnections().containsAll(crossover.getConnections()));
    }

    private Genome getResultGenome() {
        Genome genome = new Genome(null, null, random);
        Node node1 = new Node(Type.SENSOR, 1);
        Node node4 = new Node(Type.OUTPUT, 4);
        Node node2 = new Node(Type.SENSOR, 2);
        Node node3 = new Node(Type.SENSOR, 3);
        Node node5 = new Node(Type.HIDDEN, 5);
        Node node6 = new Node(Type.HIDDEN, 6);

        genome.addNode(node1);
        genome.addNode(node2);
        genome.addNode(node3);
        genome.addNode(node4);
        genome.addNode(node5);
        genome.addNode(node6);
        genome.addConnection(new Connection(node1, node4, 1, true, 1.0f));
        genome.addConnection(new Connection(node2, node4, 2, false, 1.0f));
        genome.addConnection(new Connection(node3, node4, 3, true, 1.0f));
        genome.addConnection(new Connection(node2, node5, 4, true, 1.0f));
        genome.addConnection(new Connection(node5, node4, 5, true, 1.0f));
        genome.addConnection(new Connection(node3, node5, 9, true, 1.0f));
        genome.addConnection(new Connection(node5, node6, 6, true, 1.0f));
        genome.addConnection(new Connection(node6, node4, 7, true, 1.0f));
        genome.addConnection(new Connection(node1, node6, 10, true, 1.0f));
        return genome;
    }

    private Genome parent1() {
        Genome genome = new Genome(null, null, random);
        Node node1 = new Node(Type.SENSOR, 1);
        Node node2 = new Node(Type.SENSOR, 2);
        Node node3 = new Node(Type.SENSOR, 3);
        Node node4 = new Node(Type.OUTPUT, 4);
        Node node5 = new Node(Type.HIDDEN, 5);


        Connection connection14 = new Connection(node1, node4, 1, true, 1f);
        Connection connection24 = new Connection(node2, node4, 2, false, 1f);
        Connection connection34 = new Connection(node3, node4, 3, true, 1f);
        Connection connection25 = new Connection(node2, node5, 4, true, 1f);
        Connection connection54 = new Connection(node5, node4, 5, true, 1f);
        Connection connection15 = new Connection(node1, node5, 8, true, 1f);

        List.of(node1, node2, node3, node4, node5).forEach(genome::addNode);
        List.of(connection14, connection24, connection34, connection25, connection54, connection15).forEach(genome::addConnection);
        return genome;
    }

    private Genome parent2() {
        Genome genome = new Genome(null, null, random);
        Node node1 = new Node(Type.SENSOR, 1);
        Node node2 = new Node(Type.SENSOR, 2);
        Node node3 = new Node(Type.SENSOR, 3);
        Node node4 = new Node(Type.OUTPUT, 4);
        Node node5 = new Node(Type.HIDDEN, 5);
        Node node6 = new Node(Type.HIDDEN, 6);


        Connection connection14 = new Connection(node1, node4, 1, true, 1f);
        Connection connection24 = new Connection(node2, node4, 2, false, 1f);
        Connection connection34 = new Connection(node3, node4, 3, true, 1f);
        Connection connection25 = new Connection(node2, node5, 4, true, 1f);
        Connection connection54 = new Connection(node5, node4, 5, false, 1f);
        Connection connection56 = new Connection(node5, node6, 6, true, 1f);
        Connection connection64 = new Connection(node6, node4, 7, true, 1f);
        Connection connection35 = new Connection(node3, node5, 9, true, 1f);
        Connection connection16 = new Connection(node1, node6, 10, true, 1f);


        List.of(node1, node2, node3, node4, node5, node6).forEach(genome::addNode);
        List.of(connection14, connection24, connection34, connection25, connection54, connection56, connection64, connection35, connection16).forEach(genome::addConnection);
        return genome;
    }

}