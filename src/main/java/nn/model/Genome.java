package nn.model;

import java.io.Serializable;
import java.util.*;

import static nn.Constants.*;

public class Genome implements Serializable {

    private List<Node> nodes;
    private List<Connection> connections;
    transient private NodeInnovator nodeInnovator;
    transient private ConnectionInnovator connectionInnovator;
    transient private Random random;


    public Genome(NodeInnovator nodeInnovator, ConnectionInnovator connectionInnovator, Random random) {
        this.nodeInnovator = nodeInnovator;
        this.connectionInnovator = connectionInnovator;
        this.nodes = new ArrayList<>();
        this.connections = new ArrayList<>();
        this.random = random;
    }

    public void addNode(Node node) {
        nodes.add(node);
    }

    public void addConnection(Connection connection) {
        connections.add(connection);
        connection.getIn().addConnection(connection);
        connection.getOut().addConnection(connection);
    }

    public Connection newConnection(Node inNode, Node outNode) {
        float weight = randomWeight();
        return newConnection(inNode, outNode, weight);
    }

    public Connection newConnection(Node inNode, Node outNode, float weight) {
        var connection = new Connection(inNode, outNode, connectionInnovator.nextInnovation(), true, weight);
        connections.add(connection);
        inNode.addConnection(connection);
        outNode.addConnection(connection);
        return connection;
    }

    public void makeConnectionMutation() {
        Node node = this.nodes.get(random.nextInt(nodes.size()));
        Node node2 = this.nodes.get(random.nextInt(nodes.size()));

        //We shouldn't connect same node types unless their type is HIDDEN
        if (node.getType() == node2.getType() && node.getType() != Type.HIDDEN) return;
//        if (node.getType() == node2.getType()) return;
        if (node.getInnovation() == node2.getInnovation()) return;
        //We should connect only unconnected nodes
        if (node.isConnectedTo(node2)) return;
        if (node.getType().equals(Type.SENSOR) || node2.getType().equals(Type.OUTPUT)) {
            newConnection(node, node2);
        } else {
            newConnection(node2, node);
        }
    }

    public void makeConnectionWeightMutation() {
        for(Connection connection: connections) {
            if (random.nextFloat() <= CONNECTION_PERTURBATION_PROBABILITY) {
                float perturbation = ((float) random.nextGaussian() * CONNECTION_MAX_PERTURBATION_VALUE);
                connection.setWeight(connection.getWeight() + perturbation);
            } else {
                connection.setWeight(randomWeight());
            }
        }
    }

    public void makeNodeBiasMutation() {
        for(Node node : nodes) {
            if (random.nextFloat() <= NODE_PERTURBATION_PROBABILITY) {
                float perturbation = ((float) random.nextGaussian() * NODE_MAX_PERTURBATION_VALUE);
                node.setBias(node.getBias() + perturbation);
            } else {
                node.setBias(randomBias());
            }
        }
    }

    public void enableConnectionMutation() {
        Optional<Connection> disabledConnection = connections.stream().filter(it -> !it.isExpressed()).findFirst();
        disabledConnection.ifPresent(connection -> connection.setExpressed(true));
    }

    public void toggleConnectionMutation() {
        Connection connectionToToggle = connections.get(random.nextInt(connections.size()));
        connectionToToggle.setExpressed(!connectionToToggle.isExpressed());
    }

    public Node makeNodeMutation() {
        Connection oldConnection = connections.get(random.nextInt(connections.size()));
        if (!oldConnection.isExpressed()) return null;
        oldConnection.setExpressed(false);


        Node newNode = new Node(Type.HIDDEN, nodeInnovator.nextInnovation(), 0);
        this.nodes.add(newNode);
        newConnection(oldConnection.getIn(), newNode, 1f);
        newConnection(newNode, oldConnection.getOut(), oldConnection.getWeight());
        return newNode;
    }

    @Override
    public String toString() {
        return "Genome{" +
                "nodes=" + nodes +
                ", connections=" + connections +
                '}';
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public List<Connection> getConnections() {
        return connections;
    }

    public void setRandom(Random random) {
        this.random = random;
    }

    //TODO Find a better place for this
    public float randomWeight() {
        return random.nextFloat() * 2f - 1f;
    }

    //TODO Find a better place for this
    public float randomBias() {
        return (random.nextFloat() * NODE_BIAS_RANGE) - (NODE_BIAS_RANGE / 2);
    }


    public Genome copy() {
        Map<Node, Node> offspringNodes = new HashMap<>();
        Genome genome = new Genome(nodeInnovator, connectionInnovator, random);
        for (Connection connection : this.connections) {
            addNodesAndConnections(genome, offspringNodes, connection);
        }
        return genome;
    }

    private static void addNodesAndConnections(Genome offspring, Map<Node, Node> offspringNodes, Connection connection) {
        Node inClone = newNodeMatching(offspring, offspringNodes, connection.getIn());
        Node outClone = newNodeMatching(offspring, offspringNodes, connection.getOut());
        offspring.addNode(inClone);
        offspring.addNode(outClone);
        Connection connectionClone = new Connection(inClone, outClone, connection.getInnovation(), connection.isExpressed(), connection.getWeight());
        offspring.addConnection(connectionClone);
    }

    private static Node newNodeMatching(Genome offspring, Map<Node, Node> offspringNodes, Node oldNode) {
        return offspringNodes.computeIfAbsent(oldNode, Node::cloneNoConnections);
    }
}
