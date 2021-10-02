package nn.neural;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

class AnnNode {
    private Double output = null;
    private List<AnnConnection> inputs;
    private List<AnnConnection> outputs;
    private AnnType type;
    private int id;
    private double bias;

    public AnnNode(AnnType type, int id, double bias) {
        this.id = id;
        this.type = type;
        this.inputs = new ArrayList<>();
        this.outputs = new ArrayList<>();
        this.bias = bias;
    }

    public void connectInput(AnnConnection connection) {
        inputs.add(connection);
        connection.setOut(this);
    }

    public void connectOutput(AnnConnection connection) {
        outputs.add(connection);
        connection.setIn(this);
    }

    public Double getOutput() {
        return output;
    }

    public List<AnnConnection> getInputs() {
        return inputs;
    }

    public List<AnnConnection> getOutputs() {
        return outputs;
    }

    public AnnType getType() {
        return type;
    }

    public int getId() {
        return id;
    }

    public void setOutput(Double output) {
        this.output = output;
    }

    public double calculateRecoursiveOutput(Function<Double, Double> activation, int deepCount) {
        if (deepCount == 1000) throw new RuntimeException("Recurrent");
        if (output != null) return output;
        if (type == AnnType.INPUT) throw new RuntimeException("Input node output is not set");

        double sum = inputs.stream()
                .mapToDouble(x -> x.getWeight() * x.getIn().calculateRecoursiveOutput(activation, deepCount + 1))
                .sum();
//        output = activation.apply(sum + bias);
        output = activation.apply(sum);
        return output;
    }
}
