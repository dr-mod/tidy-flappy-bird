package nn.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Node implements Serializable {

    private final Type type;
    private final int innovation;
    private List<Connection> connections;
    private double bias;

    public Node(Type type, int innovation) {
        this(type, innovation, 0);
    }

    public Node(Type type, int innovation, double bias) {
        this.type = type;
        this.innovation = innovation;
        this.bias = bias;
        connections = new ArrayList<>();
    }

    public void addConnection(Connection connection) {
        this.connections.add(connection);
    }

    public boolean isConnectedTo(Node otherNode) {
        //TODO Bloom filter can be user
        for (Connection connection: this.connections) {
            //TODO Do we need to exclude disabled connections?
            if (otherNode.equals(connection.getIn()) || otherNode.equals(connection.getOut())) {
                return true;
            }
        }
        return false;
    }

    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        return innovation + " " + type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return innovation == node.innovation &&
                type == node.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, innovation);
    }

    public Node cloneNoConnections() {
        return new Node(type, innovation, bias);
    }

    //TODO I believe this can be removed when that matching thing is out of the picture
    public int getInnovation() {
        return innovation;
    }

    public String toSomeSgring() {
        return "Node node " + innovation + " = new Node(Type." + this.type + ", " + innovation + ");";
    }

    public double getBias() {
        return bias;
    }

    public void setBias(double bias) {
        this.bias = bias;
    }
}
