import java.util.Arrays;
import java.util.Random;

public class RouletteWheelGeneticAlgorithm {

    private static final int POPULATION_SIZE = 1000;
    private static final int MAX_GENERATIONS = 1000;
    private static final double MUTATION_RATE = 0.1;
    private static final double CROSSOVER_RATE = 0.7;
    private static final double RANGE_MIN = 0;
    private static final double RANGE_MAX = 64;

    private static double targetFunction(double x) {
        return -1 * (x - 14) * (x - 21) * (x - 30) * (x - 50);
    }

    private static double[] initializePopulation() {
        double[] population = new double[POPULATION_SIZE];
        Random rand = new Random();
        for (int i = 0; i < POPULATION_SIZE; i++) {
            population[i] = RANGE_MIN + (RANGE_MAX - RANGE_MIN) * rand.nextDouble();
        }
        return population;
    }

    private static double fitness(double x) {
        return targetFunction(x);
    }

    private static double[] rouletteWheelSelection(double[] population) {
        double[] selectedIndividuals = new double[POPULATION_SIZE];

        double totalFitness = Arrays.stream(population).map(RouletteWheelGeneticAlgorithm::fitness).sum();

        double[] selectionProbabilities = new double[POPULATION_SIZE];
        for (int i = 0; i < POPULATION_SIZE; i++) {
            selectionProbabilities[i] = fitness(population[i]) / totalFitness;
        }

        for (int i = 0; i < POPULATION_SIZE; i++) {
            double rouletteValue = Math.random();
            double cumulativeProbability = 0;

            for (int j = 0; j < POPULATION_SIZE; j++) {
                cumulativeProbability += selectionProbabilities[j];
                if (rouletteValue <= cumulativeProbability) {
                    selectedIndividuals[i] = population[j];
                    break;
                }
            }
        }

        return selectedIndividuals;
    }

    private static double crossover(double parent1, double parent2) {
        Random rand = new Random();
        if (rand.nextDouble() < CROSSOVER_RATE) {
            return (parent1 + parent2) / 2.0;
        } else {
            return rand.nextDouble() < 0.5 ? parent1 : parent2;
        }
    }

    private static double mutation(double individual) {
        Random rand = new Random();
        if (rand.nextDouble() < MUTATION_RATE) {
            return RANGE_MIN + (RANGE_MAX - RANGE_MIN) * rand.nextDouble();
        } else {
            return individual;
        }
    }

    public static void main(String[] args) {
        double[] population = initializePopulation();

        for (int generation = 0; generation < MAX_GENERATIONS; generation++) {
            System.out.println("Generation: " + generation);
            double[] selectedIndividuals = rouletteWheelSelection(population);

            for (int i = 0; i < POPULATION_SIZE; i++) {
                double parent1 = selectedIndividuals[(int) (Math.random() * POPULATION_SIZE)];
                double parent2 = selectedIndividuals[(int) (Math.random() * POPULATION_SIZE)];
                double child = crossover(parent1, parent2);
                population[i] = mutation(child);
            }

            double bestIndividual = population[0];
            double bestFitness = fitness(population[0]);
            for (int i = 1; i < POPULATION_SIZE; i++) {
                double currentFitness = fitness(population[i]);
                if (currentFitness > bestFitness) {
                    bestFitness = currentFitness;
                    bestIndividual = population[i];
                }
            }
            System.out.println("Best individual: " + bestIndividual);
            System.out.println("Best fitness: " + bestFitness);
        }


    }
}
