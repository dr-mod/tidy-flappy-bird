package nn;

public class Constants {
    public static float NODE_PERTURBATION_PROBABILITY = 0.9f;
    public static float NODE_MAX_PERTURBATION_VALUE = 0.2f;
    public static float MUTATION_NODE_BIAS_PROBABILITY = 0.80f;
    public static float NODE_BIAS_RANGE = 60f;
    public static float MATCHING_GENES_DISABLED_PROBABILITY = 0.75f;
    public static float CONNECTION_PERTURBATION_PROBABILITY = 0.9f;
    public static float CONNECTION_MAX_PERTURBATION_VALUE = 0.2f;
    public static float MUTATION_CONNECTION_WEIGHTS = 0.80f;
    public static float MUTATION_CONNECTION_ENABLE = 0.1f;
    public static float MUTATION_CONNECTION_TOGGLE = 0.1f;
    public static float MUTATION_ADD_CONNECTION = 0.3f;
    public static float MUTATION_ADD_NODE = 0.3f;
    public static float C1 = 1f;
    public static float C2 = 1f;
    public static float C3 = 0.4f;
    public static float N_THRESHOLD = 20;
    public static float PHETA_THRESHOLD = 3f;
    public static float COEFFICIENT_OF_BAD_PERFORMERS_TO_REMOVE = 0.1f;
    public static float ABSTINENCE_PROBABILITY = 0.25f;
    public static int STALENESS = 20;
}
