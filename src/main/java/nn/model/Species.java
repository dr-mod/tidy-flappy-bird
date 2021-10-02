package nn.model;

import java.util.ArrayList;
import java.util.List;

public class Species {

    private List<FitnessGenome> genomes;
    private FitnessGenome bestGenome;
    private int staleness;


    public Species(FitnessGenome bestGenome) {
        this(bestGenome, 0);
    }

    public Species(FitnessGenome bestGenome, int staleness) {
        this.genomes = new ArrayList<>();
        addGenome(bestGenome);
        this.bestGenome = bestGenome;
        this.staleness = staleness;
    }

    public Species(List<FitnessGenome> genomes, int staleness) {
        this.genomes = genomes;
        this.bestGenome = genomes.get(0);
        this.staleness = staleness;
    }

    public void addGenome(FitnessGenome genome) {
        this.genomes.add(genome);
    }

    public FitnessGenome getBestGenome() {
        return bestGenome;
    }

    public List<FitnessGenome> getGenomes() {
        return genomes;
    }

    public void assignBestGenome() {
        FitnessGenome fittestGenome = genomes.stream().max(FitnessGenome::compareTo).orElse(null);
        if (bestGenome.getFitness() < fittestGenome.getFitness()) {
            bestGenome = fittestGenome;
            staleness = 0;
        } else {
            staleness++;
        }
    }

    public int getStaleness() {
        return staleness;
    }

    public double calculateAverageSharedFitness() {
        return genomes.stream()
                .mapToDouble(FitnessGenome::getFitness)
                .map(fitness -> fitness / genomes.size())
                .average()
                .orElse(0);
    }
}
