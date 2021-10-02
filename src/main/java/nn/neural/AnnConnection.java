package nn.neural;

class AnnConnection {
    private double weight;
    private AnnNode in;
    private AnnNode out;

    public AnnConnection(double weight) {
        this.weight = weight;
    }

    public AnnNode getIn() {
        return in;
    }

    public void setIn(AnnNode in) {
        this.in = in;
    }

    public AnnNode getOut() {
        return out;
    }

    public void setOut(AnnNode out) {
        this.out = out;
    }

    public double getWeight() {
        return weight;
    }
}
