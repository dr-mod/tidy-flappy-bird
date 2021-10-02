package nn.actions;

import nn.Constants;
import nn.model.Connection;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static nn.Constants.N_THRESHOLD;

public class GenomeDiff {

    private Set<Integer> matchingGenes;
    private Set<Integer> excessGenes;
    private Set<Integer> disjointGenes;

    private Map<Integer, Connection> mapGenome1;
    private Map<Integer, Connection> mapGenome2;

    public GenomeDiff(List<Connection> genome1, List<Connection> genome2) {
        this.mapGenome1 = genome1.stream()
                .collect(Collectors.toMap(Connection::getInnovation, Function.identity()));
        this.mapGenome2 = genome2.stream()
                .collect(Collectors.toMap(Connection::getInnovation, Function.identity()));

        Set<Integer> excess = new HashSet<>();
        Integer genome1MaxInnovation = getMaxInnovation(mapGenome1.keySet());
        Integer genome2MaxInnovation = getMaxInnovation(mapGenome2.keySet());

        if (genome1MaxInnovation > genome2MaxInnovation) {
            excess = calculateExcess(mapGenome1, genome1MaxInnovation, genome2MaxInnovation);
        } else if (genome1MaxInnovation < genome2MaxInnovation) {
            excess = calculateExcess(mapGenome2, genome2MaxInnovation, genome1MaxInnovation);
        }

        Set<Integer> matching = calculateMatching(mapGenome1, mapGenome2);

        HashSet<Integer> disjoint = new HashSet<>();
        disjoint.addAll(mapGenome1.keySet());
        disjoint.addAll(mapGenome2.keySet());
        disjoint.removeAll(matching);
        disjoint.removeAll(excess);

        matchingGenes = matching;
        excessGenes = excess;
        disjointGenes = disjoint;
    }

    public Set<Integer> getMatchingGenes() {
        return matchingGenes;
    }

    public Set<Integer> getExcessGenes() {
        return excessGenes;
    }

    public Set<Integer> getDisjointGenes() {
        return disjointGenes;
    }

    public double feta() {
        float c1 = nn.Constants.C1;
        float c2 = nn.Constants.C2;
        float c3 = nn.Constants.C3;
        float n;
        if (mapGenome1.size() <= N_THRESHOLD && mapGenome2.size() <= N_THRESHOLD) {
            n = 1;
        } else {
            n = Math.max(mapGenome1.size(), mapGenome2.size());
        }


        var e = this.excessGenes.size();
        var d = this.disjointGenes.size();
        float w = (float) this.averageWeight();


        var feta = (c1 * e / n) + (c2 * d / n) + (c3 * w);

        return feta;
    }

    double averageWeight() {
        return matchingGenes.stream()
                .mapToDouble(it -> {
                    float weight1 = mapGenome1.get(it).getWeight();
                    float weight2 = mapGenome2.get(it).getWeight();
                    return Math.abs(weight1 - weight2);
                })
                .average()
                .getAsDouble();
    }

    private Set<Integer> calculateMatching(Map<Integer, Connection> mapGenome1, Map<Integer, Connection> mapGenome2) {
        Set<Integer> matching = new HashSet<>(mapGenome1.keySet());
        matching.retainAll(mapGenome2.keySet());
        return matching;
    }


    private static Set<Integer> calculateExcess(Map<Integer, Connection> biggerGenome, Integer highestInnovation, Integer endOfInnovation) {
        return biggerGenome.keySet().stream()
                .filter(it -> it <= highestInnovation && it > endOfInnovation)
                .collect(Collectors.toSet());
    }

    private static Integer getMaxInnovation(Set<Integer> integers) {
        return Collections.max(integers);
    }
}
