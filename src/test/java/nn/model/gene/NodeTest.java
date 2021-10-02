package nn.model.gene;

import nn.model.*;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NodeTest {

    @Test
    void isConnectedTo() {
        Random random = new Random(1);

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

        assertTrue(node1.isConnectedTo(node4));
        assertFalse(node1.isConnectedTo(node2));
    }
}