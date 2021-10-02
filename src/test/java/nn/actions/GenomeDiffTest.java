package nn.actions;

import nn.actions.GenomeDiff;
import nn.model.Connection;
import nn.model.Genome;
import nn.model.Node;
import nn.model.Type;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class GenomeDiffTest {

    @Test
    public void serialization() throws IOException, ClassNotFoundException {
        FileOutputStream fileOutputStream
                = new FileOutputStream("yourfile.txt");
        ObjectOutputStream objectOutputStream
                = new ObjectOutputStream(fileOutputStream);
        Genome genome = parent1();
        objectOutputStream.writeObject(genome);
        objectOutputStream.flush();
        objectOutputStream.close();


        FileInputStream fileInputStream
                = new FileInputStream("yourfile.txt");
        ObjectInputStream objectInputStream
                = new ObjectInputStream(fileInputStream);
        Genome genome2 = (Genome) objectInputStream.readObject();
        objectInputStream.close();

        assertEquals(genome, genome2);
    }

    @Test
    public void compare() {
        GenomeDiff compare = new GenomeDiff(parent1().getConnections(), parent2().getConnections());

        assertEquals(Set.of(9, 10), compare.getExcessGenes());
        assertEquals(Set.of(1,2,3,4,5), compare.getMatchingGenes());
        assertEquals(Set.of(6,7,8), compare.getDisjointGenes());
    }

    @Test
    public void avg() {
        GenomeDiff compare = new GenomeDiff(parent1().getConnections(), parent2().getConnections());
        assertEquals(0, compare.averageWeight());

        Genome parent2 = parent2();
        parent2.getConnections().forEach( it -> it.setWeight(0));
        GenomeDiff compare2 = new GenomeDiff(parent1().getConnections(), parent2.getConnections());

        assertEquals(1, compare2.averageWeight());
    }

    private Genome parent1() {
        Genome genome = new Genome(null, null, new Random(1));
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
        Genome genome = new Genome(null, null, new Random(1));
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