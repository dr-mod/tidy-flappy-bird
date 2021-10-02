package nn.model;

import nn.actions.GenomeDiff;
import nn.actions.Mating;

import java.util.*;
import java.util.stream.Collectors;

import static nn.Constants.*;

public class Population {

    private List<Genome> genomes;
    private Evaluatable evaluator;
    private Random random;
    private int populationSize;
    private Specieses specieses;
    public FitnessGenome best;

    //TODO this is awful
    ConnectionInnovator connectionInnovator;
    NodeInnovator nodeInnovator;

    public Population(Random random, int size, Evaluatable evaluator) {
        this.genomes = new ArrayList<>(size);
//        this.evaluator = evaluator;
        this.specieses = new Specieses();
        this.random = random;
        this.populationSize = size;
        connectionInnovator = new ConnectionInnovator(10);
        nodeInnovator = new NodeInnovator(10);
        for (int i = 0; i < size; i++) {
            Genome genome = new Genome(nodeInnovator, connectionInnovator, random);
            Node node1 = new Node(Type.SENSOR, 1, 0);
            Node node2 = new Node(Type.SENSOR, 2, 0);
            Node node3 = new Node(Type.SENSOR, 3, 0);
            Node node4 = new Node(Type.SENSOR, 4, 0);
            Node node5 = new Node(Type.OUTPUT, 5, 0);

            Connection c1 = new Connection(node1, node5, 1, true, random.nextFloat() * 2f - 1f);
            Connection c2 = new Connection(node2, node5, 2, true, random.nextFloat() * 2f - 1f);
            Connection c3 = new Connection(node3, node5, 3, true, random.nextFloat() * 2f - 1f);
            Connection c4 = new Connection(node4, node5, 4, true, random.nextFloat() * 2f - 1f);

            List.of(node1, node2, node3, node4, node5).forEach(genome::addNode);
            List.of(c1, c2, c3, c4).forEach(genome::addConnection);
//            List.of(node1, node2, node5).forEach(genome::addNode);
//            List.of(c1, c2).forEach(genome::addConnection);
            genomes.add(genome);
        }

    }

    public void evaluate(Map<Genome, Double> genomeFitnessMap) {
        this.evaluator = genomeFitnessMap::get;
        List<Genome> newGeneration = new ArrayList<>(populationSize);


        List<FitnessGenome> evaluatedGenomes = this.genomes.stream()
                .map(genome -> new FitnessGenome(genome, evaluator.evaluate(genome)))
                .sorted((o1, o2) -> o1.compareTo(o2) * -1)
                .collect(Collectors.toList());
//        for (FitnessGenome genome : evaluatedGenomes) {
//            System.out.print(genome.getFitness() + " ");
//            System.out.print(genome.getGenome().getNodes() + " ");
//            System.out.println(genome.getGenome().getConnections());
//        }
        best = new FitnessGenome(evaluatedGenomes.get(0).getGenome().copy(), evaluatedGenomes.get(0).getFitness());
        System.out.println("Best fitness: " + best.getFitness());


        newGeneration.add(best.getGenome().copy());
        newGeneration.add(mutateGenome(best.getGenome().copy()));

        Specieses breakedIntoSPecies = breakIntoSpecies(evaluatedGenomes, this.specieses);
        Specieses noWeakInSpecies = killWorstPerformers(deleteStaleSpecieses(breakedIntoSPecies));

        System.out.println("Species: " + noWeakInSpecies.getSpecieces().size());

        double sum = noWeakInSpecies.getSpecieces().stream()
                .mapToDouble(Species::calculateAverageSharedFitness)
                .sum();

        for (Species speciece : noWeakInSpecies.getSpecieces()) {
            int offspringNumber = (int) (speciece.calculateAverageSharedFitness() / sum * populationSize - 5);
            for (int i = 0; i <= offspringNumber; i++) {
                FitnessGenome firstSelectedGenome = biasedRandomGenome(speciece.getGenomes());
                if (random.nextFloat() < ABSTINENCE_PROBABILITY) {
                    newGeneration.add(mutateGenome(firstSelectedGenome.getGenome().copy()));
                } else {
                    FitnessGenome secondSelectedGenome = biasedRandomGenome(speciece.getGenomes());
                    Mating mating = new Mating(random, nodeInnovator, connectionInnovator);
                    Genome child = mating.crossover(firstSelectedGenome, secondSelectedGenome);
                    if (firstSelectedGenome.equals(secondSelectedGenome) || (random.nextFloat() < ABSTINENCE_PROBABILITY)) {
                        child = mutateGenome(child);
                    }
                    newGeneration.add(child);
                }
            }
        }


        List<FitnessGenome> allLeftGenomes = noWeakInSpecies.getSpecieces().stream()
                .flatMap(it -> it.getGenomes().stream())
                .sorted((o1, o2) -> o1.compareTo(o2) * -1)
                .collect(Collectors.toList());

        while (newGeneration.size() < populationSize) {
            FitnessGenome firstSelectedGenome = biasedRandomGenome(allLeftGenomes);
            FitnessGenome secondSelectedGenome = noWeakInSpecies.getSpecieces().get((random.nextInt(noWeakInSpecies.getSpecieces().size()))).getBestGenome();
//            FitnessGenome secondSelectedGenome = biasedRandomGenome(allLeftGenomes);
            Mating mating = new Mating(random, nodeInnovator, connectionInnovator);
            Genome child = mating.crossover(firstSelectedGenome, secondSelectedGenome);
            if (firstSelectedGenome.equals(secondSelectedGenome) || (random.nextFloat() < ABSTINENCE_PROBABILITY)) {
                child = mutateGenome(child);
            }
            newGeneration.add(child);
        }

        this.specieses = noWeakInSpecies.whittleDown();
        this.genomes = newGeneration;
    }

    private Specieses killWorstPerformers(Specieses breakedIntoSPecies) {
        List<Species> newList = new ArrayList<>();
        for (Species speciece : breakedIntoSPecies.getSpecieces()) {

            List<FitnessGenome> fitnessGenomes = new ArrayList<>(speciece.getGenomes());
            fitnessGenomes.sort((o1, o2) -> o1.compareTo(o2) * -1);
            if (fitnessGenomes.size() < 5) continue;
            int toCutOff = (int) (fitnessGenomes.size() * nn.Constants.COEFFICIENT_OF_BAD_PERFORMERS_TO_REMOVE);
            List<FitnessGenome> noWeak = fitnessGenomes.subList(0, fitnessGenomes.size() - toCutOff);

            newList.add(new Species(noWeak, speciece.getStaleness()));
        }
        return new Specieses(newList);
    }

//    public void evaluate(Map<Genome, Double> genomeFitnessMap) {
//        this.evaluator = genomeFitnessMap::get;
//        List<Genome> newGeneration = new ArrayList<>(populationSize);
//
//
//        List<FitnessGenome> evaluatedGenomes = this.genomes.stream()
//                .map(genome -> new FitnessGenome(genome, evaluator.evaluate(genome)))
//                .sorted((o1, o2) -> o1.compareTo(o2) * -1)
//                .collect(Collectors.toList());
//        for (FitnessGenome genome : evaluatedGenomes) {
//            System.out.print(genome.getFitness() + " ");
//            System.out.print(genome.getGenome().getNodes() + " ");
//            System.out.println(genome.getGenome().getConnections());
//        }
//        best = new FitnessGenome(evaluatedGenomes.get(0).getGenome().copy(), evaluatedGenomes.get(0).getFitness());
//        newGeneration.add(best.getGenome().copy());
//
//        Specieses breakedIntoSPecies = breakIntoSpecies(evaluatedGenomes, this.specieses);
//        Specieses cleanedSpeceses = deleteStaleSpecieses(breakedIntoSPecies);
//
//        List<FitnessGenome> genomesSharedFitness = calculateFitnessGenomes(cleanedSpeceses);
//
////        newGeneration.add(best.getGenome().copy());
//        for (Species speciece : cleanedSpeceses.getSpecieces()) {
//            newGeneration.add(speciece.getBestGenome().getGenome());
//        }
//
//        while (newGeneration.size() < populationSize) {
//            //TODO This is awful
////            Species selectedSpecies = biasedRandomSpecies(fitnessGenomesNoBadPerformers, specieses);
////            FitnessGenome selectedGenome = biasedRandomGenome(speciesToFitessGenomes(fitnessGenomesNoBadPerformers, selectedSpecies));
//
//            FitnessGenome biasedRandomGenome = biasedRandomGenome(genomesSharedFitness);
//            var firstSelectedGenome = biasedRandomGenome(cleanedSpeceses.get(biasedRandomGenome.getGenome()).getGenomes());
//
//            if (random.nextFloat() < ABSTINENCE_PROBABILITY) {
//                newGeneration.add(mutateGenome(firstSelectedGenome.getGenome().copy()));
//            } else {
//                FitnessGenome secondSelectedGenome;
//                if(random.nextFloat() < 0.25) {
//                    FitnessGenome gen = biasedRandomGenome(genomesSharedFitness);
//                    secondSelectedGenome = biasedRandomGenome(cleanedSpeceses.get(gen.getGenome()).getGenomes());
//                } else {
//                    List<FitnessGenome> currentSpecieceWithFitness = new ArrayList<>(cleanedSpeceses.get(firstSelectedGenome.getGenome()).getGenomes());
//                    currentSpecieceWithFitness.sort((o1, o2) -> o1.compareTo(o2) * -1);
//                    secondSelectedGenome = biasedRandomGenome(currentSpecieceWithFitness);
//                }
//
//                Mating mating = new Mating(random, nodeInnovator, connectionInnovator);
//                Genome child = mating.crossover(firstSelectedGenome, secondSelectedGenome);
//                newGeneration.add(mutateGenome(child));
//            }
//        }
//
//        System.out.println("Species: " + cleanedSpeceses.getSpecieces().size());
//        System.out.println("Best fitness: " + best.getFitness());
//
//        this.specieses = cleanedSpeceses.whittleDown();
//        this.genomes = newGeneration;
//    }

    private Specieses deleteStaleSpecieses(Specieses specieses) {
        List<Species> speciesSet = new ArrayList<>(specieses.getSpecieces());

        Iterator<Species> iterator = speciesSet.iterator();
        while (iterator.hasNext()) {
            Species next = iterator.next();
            next.assignBestGenome();
            if (next.getStaleness() >= STALENESS ) {
                iterator.remove();
                System.out.println("Species removed");
            }
        }
        return new Specieses(speciesSet);
    }


//    public void evaluate() {
//        List<Genome> newGeneration = new ArrayList<>(populationSize);
//
//        Specieses specieses = breakIntoSpecies(this.genomes);
//        List<FitnessGenome> fitnessGenomes = calculateFitnessGenomes(specieses);
//        List<FitnessGenome> fitnessGenomesNoBadPerformers = removeBadPerformers(fitnessGenomes);
//
//
////        newGeneration.add(fitnessGenomesNoBadPerformers.get(0).getGenome().copy());
//        best = new FitnessGenome(fitnessGenomesNoBadPerformers.get(0).getGenome().copy(), fitnessGenomesNoBadPerformers.get(0).getFitness());
//
//        while (newGeneration.size() < populationSize) {
//            FitnessGenome selectedGenome = biasedRandomGenome(fitnessGenomesNoBadPerformers);
//            if (random.nextFloat() < ABSTINENCE_PROBABILITY) {
//                newGeneration.add(mutateGenome(selectedGenome));
//            } else {
//                List<FitnessGenome> currentSpecieceWithFitness = getSpecieceFitnessGenomes(specieses, fitnessGenomesNoBadPerformers, selectedGenome);
//                FitnessGenome fitnessGenome = biasedRandomGenome(currentSpecieceWithFitness);
//                Mating mating = new Mating(random, nodeInnovator, connectionInnovator);
//                Genome child = mating.crossover(selectedGenome, fitnessGenome);
//                newGeneration.add(child);
//            }
//        }
//
//        System.out.println("Species: " + specieses.getSpecieces().size());
//        System.out.println("Best fitness: " + best.getFitness());
//        this.genomes = newGeneration;
//    }

    public List<Genome> getGenomes() {
        return genomes;
    }

//    private List<FitnessGenome> getSpecieseOf(Specieses specieses, List<FitnessGenome> fitnessGenomesNoBadPerformers, FitnessGenome selectedGenome) {
//        Species species = specieses.get(selectedGenome.getGenome());
//        return speciesToFitessGenomes(fitnessGenomesNoBadPerformers, species);
//    }

//    private List<FitnessGenome> speciesToFitessGenomes(List<FitnessGenome> fitnessGenomesNoBadPerformers, Species species) {
//        //TODO this is not supposed to be randomised, need to search for a partner based on fitness
//        List<FitnessGenome> speciesWithFitness = species.getGenomes().stream()
//                .map(it -> {
//                    return fitnessGenomesNoBadPerformers.stream()
//                            .filter(fit -> fit.getGenome().equals(it))
//                            .findAny()
//                            .orElse(null);
//                })
//                .filter(Objects::nonNull)
//                .collect(Collectors.toList());
//
//        List<FitnessGenome> speciesWithFitness2 = new ArrayList<>(speciesWithFitness);
//        speciesWithFitness2.sort((o1, o2) -> o1.compareTo(o2) * -1);
//        return speciesWithFitness2;
//    }

    private Genome mutateGenome(Genome selectedGenome) {
        Genome genome = selectedGenome.copy();
        if (random.nextFloat() < MUTATION_ADD_CONNECTION) {
            genome.makeConnectionMutation();
        } else if (random.nextFloat() < MUTATION_ADD_NODE) {
            genome.makeNodeMutation();
        }

        if (random.nextFloat() < MUTATION_CONNECTION_WEIGHTS) {
            genome.makeConnectionWeightMutation();
        }
        if (random.nextFloat() < MUTATION_CONNECTION_ENABLE) {
            genome.enableConnectionMutation();
        }
        if (random.nextFloat() < MUTATION_CONNECTION_TOGGLE) {
            genome.toggleConnectionMutation();
        }
        if (random.nextFloat() < MUTATION_NODE_BIAS_PROBABILITY) {
            genome.makeNodeBiasMutation();
        }
        return genome;
    }

    private Species biasedRandomSpecies(List<FitnessGenome> fitnessGenomes, Specieses specieses) {
        HashMap<Species, Double> speciesDouble = new HashMap<>();
        for (Species species : specieses.getSpecieces()) {
            double averageFitness = species.getGenomes().stream()
                    .map(map -> fitnessGenomes.stream()
                            .filter(it -> it.getGenome().equals(map))
                            .findAny()
                            .orElse(null))
                    .filter(Objects::nonNull)
                    .mapToDouble(FitnessGenome::getFitness)
                    .average()
                    .orElse(0);
            speciesDouble.put(species, averageFitness);
        }

        double maxRandom = speciesDouble.values().stream().mapToDouble(x -> x).sum();
        double randomSegment = random.nextFloat() * maxRandom;
        double accumulator = 0;
        Species selectedSpecies = speciesDouble.keySet().iterator().next();
        for (Map.Entry<Species, Double> entry : speciesDouble.entrySet()) {
            accumulator += entry.getValue();
            if (accumulator >= randomSegment) {
                selectedSpecies = entry.getKey();
                break;
            }
        }
        return selectedSpecies;
    }

    private FitnessGenome biasedRandomGenome(List<FitnessGenome> fitnessGenomes) {
        double sum = fitnessGenomes.stream()
                .mapToDouble(FitnessGenome::getFitness).sum();
        double randomSegment = random.nextFloat() * sum;
        double accumulator = 0;
        FitnessGenome selectedGenome = fitnessGenomes.get(0);
        for (FitnessGenome fitnessGenome : fitnessGenomes) {
            accumulator += fitnessGenome.getFitness();
            if (accumulator >= randomSegment) {
                selectedGenome = fitnessGenome;
                break;
            }
        }
        return selectedGenome;
    }

    private List<FitnessGenome> calculateFitnessGenomes(Specieses specieses) {
        List<FitnessGenome> fitnessGenomes = new ArrayList<>();
        for (Species species : specieses.getSpecieces()) {
            for (FitnessGenome genome : species.getGenomes()) {
                double adjustedFitness = genome.getFitness() / species.getGenomes().size();
                fitnessGenomes.add(new FitnessGenome(genome.getGenome(), adjustedFitness));
            }
        }
        fitnessGenomes.sort((o1, o2) -> o1.compareTo(o2) * -1);
        return fitnessGenomes;
    }

    private Specieses breakIntoSpecies(List<FitnessGenome> genomes, Specieses specieses) {
        for (FitnessGenome genome : genomes) {
            Species suitableSpecies = null;
            double bestPheta = Float.MAX_VALUE;
            for (Species oneSpecies : specieses.getSpecieces()) {
                FitnessGenome representative = oneSpecies.getBestGenome();
                GenomeDiff genomeDiff = new GenomeDiff(genome.getGenome().getConnections(), representative.getGenome().getConnections());
                double feta = genomeDiff.feta();
                if (PHETA_THRESHOLD > feta && feta < bestPheta) {
                    suitableSpecies = oneSpecies;
                    bestPheta = feta;
                }
            }

            if (Objects.nonNull(suitableSpecies)) {
                specieses.add(genome, suitableSpecies);
            } else {
                specieses.newSpecies(genome);
            }
        }
        return specieses;
    }
}
