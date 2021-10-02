package nn.actions;

import nn.Constants;
import nn.model.*;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class Mating {

    private final Random random;
    private final NodeInnovator nodeInnovator;
    private final ConnectionInnovator connectionInnovator;

    public Mating(Random random, NodeInnovator nodeInnovator, ConnectionInnovator connectionInnovator) {
        this.random = random;
        this.nodeInnovator = nodeInnovator;
        this.connectionInnovator = connectionInnovator;
    }

    public Genome crossover(FitnessGenome fitnessGenome, FitnessGenome fitnessGenome2) {
        List<Connection> moreFitConnections;
        List<Connection> lessFitConnections;
        if (fitnessGenome.compareTo(fitnessGenome2) > 0) {
            moreFitConnections = fitnessGenome.getGenome().getConnections();
            lessFitConnections = fitnessGenome2.getGenome().getConnections();
        } else {
            moreFitConnections = fitnessGenome2.getGenome().getConnections();
            lessFitConnections = fitnessGenome.getGenome().getConnections();
        }

        Map<Integer, Connection> moreFitConnectionsMap = moreFitConnections.stream()
                .collect(Collectors.toMap(Connection::getInnovation, Function.identity()));
        Map<Integer, Connection> lessFitConnectionsMap = lessFitConnections.stream()
                .collect(Collectors.toMap(Connection::getInnovation, Function.identity()));

        GenomeDiff genomeDiff = new GenomeDiff(moreFitConnections, lessFitConnections);

        Genome offspring = new Genome(nodeInnovator, connectionInnovator, random);
        Map<Node, Node> offspringNodes = new HashMap<>();
        List<ConnectionPair> connectionPairs = genomeDiff.getMatchingGenes().stream()
                .map(it -> new ConnectionPair(moreFitConnectionsMap.get(it), lessFitConnectionsMap.get(it)))
                .collect(toList());


        Set<Connection> excessAndDisjoint = Stream.concat(genomeDiff.getDisjointGenes().stream(),
                genomeDiff.getExcessGenes().stream())
                .filter(moreFitConnectionsMap::containsKey)
                .map(moreFitConnectionsMap::get)
                .collect(Collectors.toSet());


        for (ConnectionPair connectionPair : connectionPairs) {
            //if one of the connections is disabled there is a chance that the new connection is going to be disabled
            if ((connectionPair.getConnection1().isExpressed() != connectionPair.getConnection2().isExpressed())) {
                boolean disabled = random.nextFloat() < nn.Constants.MATCHING_GENES_DISABLED_PROBABILITY;
                addNodesAndConnections(offspring, offspringNodes, random.nextBoolean() ? connectionPair.getConnection1() : connectionPair.getConnection2(), disabled);
            } else {
                addNodesAndConnections(offspring, offspringNodes, random.nextBoolean() ? connectionPair.getConnection1() : connectionPair.getConnection2());
            }
        }

        for (Connection connection : excessAndDisjoint) {
            addNodesAndConnections(offspring, offspringNodes, connection);
        }

        return offspring;
    }

    private void addNodesAndConnections(Genome offspring, Map<Node, Node> offspringNodes, Connection connection) {
        Node inClone = newNodeMatching(offspring, offspringNodes, connection.getIn());
        Node outClone = newNodeMatching(offspring, offspringNodes, connection.getOut());
        Connection connectionClone = new Connection(inClone, outClone, connection.getInnovation(), connection.isExpressed(), connection.getWeight());
        offspring.addConnection(connectionClone);
    }

    private Node newNodeMatching(Genome offspring, Map<Node, Node> offspringNodes, Node oldNode) {
        return offspringNodes.computeIfAbsent(oldNode, it -> {
            Node clone = it.cloneNoConnections();
            offspring.addNode(clone);
            return clone;
        });
    }

    //TODO redo this
    private void addNodesAndConnections(Genome offspring, Map<Node, Node> offspringNodes, Connection connection, boolean expressed) {
        Node inClone = newNodeMatching(offspring, offspringNodes, connection.getIn());
        Node outClone = newNodeMatching(offspring, offspringNodes, connection.getOut());
        Connection connectionClone = new Connection(inClone, outClone, connection.getInnovation(), expressed, connection.getWeight());
        offspring.addConnection(connectionClone);
    }
}

class ConnectionPair {
    private Connection connection1;
    private Connection connection2;

    public ConnectionPair(Connection connection1, Connection connection2) {
        this.connection1 = connection1;
        this.connection2 = connection2;
    }

    public Connection getConnection1() {
        return connection1;
    }

    public Connection getConnection2() {
        return connection2;
    }
}