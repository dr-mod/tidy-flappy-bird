package nn.neural;

import nn.model.Connection;
import nn.model.Genome;

import java.util.*;
import java.util.function.Function;

public class NeuralNetwork {
    private Set<AnnNode> inputNodes = new TreeSet<>(Comparator.comparingInt(AnnNode::getId));
    private Set<AnnNode> outputNodes = new TreeSet<>(Comparator.comparingInt(AnnNode::getId));
    private Set<AnnNode> hiddenNodes = new HashSet<>();
    ;

    public NeuralNetwork(Genome genome) {
        List<Connection> connections = new ArrayList<>(genome.getConnections());

        NodeProxier nodeProxier = new NodeProxier();

        for (Connection connection : connections) {
            AnnNode input = nodeProxier.convert(connection.getIn());
            AnnNode output = nodeProxier.convert(connection.getOut());
            if (!connection.isExpressed()) continue;
            AnnConnection annConnection = new AnnConnection(connection.getWeight());
            input.connectOutput(annConnection);
            output.connectInput(annConnection);
        }

        for (AnnNode node : nodeProxier.getAllNodes()) {
            switch (node.getType()) {
                case INPUT:
                    this.inputNodes.add(node);
                    break;
                case HIDDEN:
                    this.hiddenNodes.add(node);
                    break;
                case OUTPUT:
                    this.outputNodes.add(node);
                    break;
                default:
                    throw new RuntimeException();
            }
        }
    }

    public double[] calculate(double[] inputs) {
        if (inputs.length != inputNodes.size()) {
            throw new RuntimeException("number of inputs is wrong");
        }

        inputNodes.forEach(it -> it.setOutput(null));
        outputNodes.forEach(it -> it.setOutput(null));
        hiddenNodes.forEach(it -> it.setOutput(null));

        int i = 0;
        for (AnnNode inputNode : inputNodes) {
            inputNode.setOutput(inputs[i++]);
        }

        Function<Double, Double> activation = x -> 1d / (1 + Math.exp(-x));
//        Function<Double, Double> activation = x -> 1d / (1 + Math.exp(-x*4.9));
//        Function<Double, Double> activation = x -> Math.max(0, x);
//        Function<Double, Double> sigmoid = x -> (1d / (1 + Math.exp(-x))) * 2 - 1;
//        Function<Double, Double> activation = x -> x;

        double[] result = new double[outputNodes.size()];
        i = 0;
        for (AnnNode outputNode : outputNodes) {
            result[i] = outputNode.calculateRecoursiveOutput(activation, 0);
            i++;
        }

        return result;

    }

}

