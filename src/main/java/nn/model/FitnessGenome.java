package nn.model;

public class FitnessGenome implements Comparable<FitnessGenome> {

    private Genome genome;
    private double fitness;

    public FitnessGenome(Genome genome, double fitness) {
        this.genome = genome;
        this.fitness = fitness;
    }

    @Override
    public int compareTo(FitnessGenome o) {
        return Double.compare(this.fitness, o.fitness);
    }

    public Genome getGenome() {
        return genome;
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public FitnessGenome copy() {
        return new FitnessGenome(genome.copy(), fitness);
    }

    @Override
    public String toString() {
        return "FitnessGenome{" +
                "fitness=" + fitness +
                ", genome=" + genome +
                '}';
    }
}
