package nn.model.gene;

import nn.model.*;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class GenomeTest {

    Random random = new Random(1);

    @Test
    public void addConnectionMutation() {
        NodeInnovator nodeInnovator = new NodeInnovator(5);
        ConnectionInnovator connectionInnovator = new ConnectionInnovator(4);
        Genome genome = new Genome(nodeInnovator, connectionInnovator, random);
        Node node1 = new Node(Type.SENSOR, 1, 0);
        Node node2 = new Node(Type.SENSOR, 2, 0);
        Node node3 = new Node(Type.SENSOR, 3, 0);
        Node node4 = new Node(Type.OUTPUT, 4, 0);
        genome.addNode(node1);
        genome.addNode(node2);
        genome.addNode(node3);
        genome.addNode(node4);
        Connection node1ToNode4 = genome.newConnection(node1, node4);
        Connection node2ToNode4 = genome.newConnection(node2, node4);
        Connection node3ToNode4 = genome.newConnection(node3, node4);


        int size = genome.getConnections().size();
        for (int i = 0; i < 10; i++) {
            genome.makeConnectionMutation();
        }
        assertEquals(size, genome.getConnections().size());

    }

    @Test
    public void addConnectionMutationChangeExpected() {
        NodeInnovator nodeInnovator = new NodeInnovator(7);
        ConnectionInnovator connectionInnovator = new ConnectionInnovator(7);
        Genome genome = new Genome(nodeInnovator, connectionInnovator, random);
        Node node1 = new Node(Type.SENSOR, 1, 0);
        Node node2 = new Node(Type.SENSOR, 2, 0);
        Node node3 = new Node(Type.SENSOR, 3, 0);
        Node node4 = new Node(Type.OUTPUT, 4, 0);
        Node node5 = new Node(Type.HIDDEN, 5, 0);
        Node node6 = new Node(Type.HIDDEN, 6, 0);
        genome.addNode(node1);
        genome.addNode(node2);
        genome.addNode(node3);
        genome.addNode(node4);
        genome.addNode(node5);
        genome.addNode(node6);
        genome.newConnection(node1, node4);
        genome.newConnection(node2, node4);
        genome.newConnection(node3, node4);
        genome.newConnection(node1, node5);
        genome.newConnection(node5, node6);
        genome.newConnection(node6, node4);

        int size = genome.getConnections().size();
        for (int i = 0; i < 10; i++) {
            genome.makeConnectionMutation();
        }
        assertTrue(size < genome.getConnections().size());

    }

    @Test
    public void makeNewNodeConnection() {
        NodeInnovator nodeInnovator = new NodeInnovator();
        ConnectionInnovator connectionInnovator = new ConnectionInnovator();
        Genome genome = new Genome(nodeInnovator, connectionInnovator, random);
        Node node1 = new Node(Type.SENSOR, 1, 0);
        Node node2 = new Node(Type.SENSOR, 2, 0);
        Node node3 = new Node(Type.SENSOR, 3, 0);
        Node node4 = new Node(Type.OUTPUT, 4, 0);
        genome.addNode(node1);
        genome.addNode(node2);
        genome.addNode(node3);
        genome.addNode(node4);
        Connection node1ToNode4 = genome.newConnection(node1, node4);
        Connection node2ToNode4 = genome.newConnection(node2, node4);
        Connection node3ToNode4 = genome.newConnection(node3, node4);

        genome.makeNodeMutation();



    }
}