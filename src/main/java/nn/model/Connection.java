package nn.model;

import java.io.Serializable;
import java.util.Objects;

public class Connection implements Serializable {

    private final Node in;
    private final Node out;
    private float weight;
    private boolean expressed;
    private final int innovation;

    public Connection(Node in, Node out, int innovation, boolean expressed, float weight) {
        this.in = in;
        this.out = out;
        this.weight = weight;
        this.expressed = expressed;
        this.innovation = innovation;
    }

    public Node getIn() {
        return in;
    }

    public Node getOut() {
        return out;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public boolean isExpressed() {
        return expressed;
    }

    public void setExpressed(boolean expressed) {
        this.expressed = expressed;
    }

    public int getInnovation() {
        return innovation;
    }

    public Connection cloneNoNodes() {
        return new Connection(null, null, this.innovation, this.expressed, this.weight);
    }

    @Override
    public String toString() {
        return "Connection{" +
                "weight=" + weight +
                ", expressed=" + expressed +
                ", innovation=" + innovation +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Connection that = (Connection) o;
        return Float.compare(that.weight, weight) == 0 &&
                expressed == that.expressed &&
                innovation == that.innovation;
    }

    @Override
    public int hashCode() {
        return Objects.hash(weight, expressed, innovation);
    }
}
