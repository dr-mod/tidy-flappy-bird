package nn.model;

import java.util.*;

public class Specieses {
    private List<Species> specieses;

    public Specieses() {
        this.specieses = new ArrayList<>();
    }

    public Specieses(List<Species> specieses) {
        this.specieses = specieses;
    }

    public void add(FitnessGenome genome, Species species) {
        species.addGenome(genome);
    }

    public void newSpecies(FitnessGenome genome) {
        Species species = new Species(genome);
        specieses.add(species);
    }

    public Species get(Genome genome) {
        for (Species speciese : specieses) {
            for (FitnessGenome specieseGenome : speciese.getGenomes()) {
                if (specieseGenome.getGenome().equals(genome)) return speciese;
            }
        }
        throw new RuntimeException("genome doesn't belong to any species");
    }

    public List<Species> getSpecieces() {
        return specieses;
    }

    public Specieses whittleDown() {
        List<Species> speciesSet = new ArrayList<>();
        for (Species oneSpecies : specieses) {
            speciesSet.add(new Species(oneSpecies.getBestGenome(), oneSpecies.getStaleness()));
        }
        return new Specieses(speciesSet);
    }
}
