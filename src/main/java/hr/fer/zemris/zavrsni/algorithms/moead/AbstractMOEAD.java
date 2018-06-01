package hr.fer.zemris.zavrsni.algorithms.moead;

import hr.fer.zemris.zavrsni.algorithms.AbstractMOOPAlgorithm;
import hr.fer.zemris.zavrsni.algorithms.MOOPUtils;
import hr.fer.zemris.zavrsni.algorithms.PopulationUtils;
import hr.fer.zemris.zavrsni.algorithms.nsga3.NSGA3;
import hr.fer.zemris.zavrsni.algorithms.nsga3.NSGA3Util;
import hr.fer.zemris.zavrsni.algorithms.operators.Crossover;
import hr.fer.zemris.zavrsni.algorithms.operators.Mutation;
import hr.fer.zemris.zavrsni.evaluator.MOOPProblem;
import hr.fer.zemris.zavrsni.solution.Solution;

import java.util.*;

public abstract class AbstractMOEAD extends AbstractMOOPAlgorithm<Solution> {

    private double[][] weights;

    private Random rand = new Random();

    private Map<Integer, int[]> neighbourhoods;

    private List<Solution> externalPopulation;

    private double[] idealPoint;

    private int archiveSize;

    public AbstractMOEAD(List<Solution> population, MOOPProblem problem, int closestVectors, List<Integer> parameterH,
                         Mutation mutation, Crossover<Solution> crossover, int maxGen) {
        super(population, problem, maxGen, crossover, mutation);
        archiveSize = populationSize();
        int numberOfWeights = NSGA3.getPreferredPopulationSize(problem.getNumberOfObjectives(), parameterH);
        if(numberOfWeights < population.size())
            throw new IllegalArgumentException("Not enough weights for the population!");
        weights = new double[numberOfWeights][problem.getNumberOfObjectives()];
        initializeWeights(problem.getNumberOfObjectives(), parameterH, weights);
        weights = Arrays.copyOf(weights, population.size());
        this.neighbourhoods = new HashMap<>();
        for (int i = 0; i < weights.length; i++) {
            int[] neighbours = new int[closestVectors];
            double lastDistance = 0;
            neighbours[0] = i;
            for (int k = 1; k < closestVectors; k++) {
                int min = i;
                double currentMinDistance = Double.MAX_VALUE;
                for (int j = 0; j < weights.length; j++) {

                    double dist = euclidianDistance(weights[i], weights[j]);
                    if (dist < currentMinDistance && dist > lastDistance) {
                        min = j;
                        currentMinDistance = dist;
                    }

                }
                neighbours[k] = min;
                lastDistance = currentMinDistance;
            }
            neighbourhoods.put(i, neighbours);
        }
        externalPopulation = new ArrayList<>();
        PopulationUtils.evaluatePopulation(population, problem);
        this.idealPoint = new double[problem.getNumberOfObjectives()];
        for (int i = 0; i < idealPoint.length; i++) {
            final int k = i;
            idealPoint[i] = Collections.min(population, Comparator.comparingDouble(o -> o.getObjectives()[k])).getObjectives()[i];
        }
    }

    private static void initializeWeights(int numberOfObjectives, List<Integer> parameterH, double[][] realWeights) {
        List<MOOPUtils.ReferencePoint> l = new LinkedList<>();
        NSGA3Util.generateReferencePoints(l, numberOfObjectives, parameterH);
        for(int i = 0; i < realWeights.length; i++){
            realWeights[i] = l.get(i).location;
        }
    }

    public static int getPreferredPopulationSize(int H, int numberOfObjectives) {
        return MOOPUtils.binomialCoefficient(H + numberOfObjectives - 1, numberOfObjectives - 1);
    }

    @Override
    public void run() {
        int gen = 0;
        while (true) {
            System.out.println(gen);
            for (int i = 0; i < population.size(); i++) {
                int[] indices = neighbourhoods.get(i);
                int index1, index2;
                index1 = indices[rand.nextInt(indices.length)];
                do {
                    index2 = indices[rand.nextInt(indices.length)];
                } while (index1 == index2);
                Solution y = crossover.cross(population.get(index1), population.get(index2)).get(0);
                mutation.mutate(y);
                problem.evaluateObjectives(y);
                double[] obj = y.getObjectives();
                for (int j = 0; j < problem.getNumberOfObjectives(); j++) {
                    if (idealPoint[j] > obj[j]) idealPoint[j] = obj[j];
                }
                for (int j : indices) {
                    if (scalarizationFunction(y, weights[j], idealPoint) <=
                            scalarizationFunction(population.get(j), weights[j], idealPoint)) {
                        population.set(j, y);
                    }
                }
                for (int j = 0; j < externalPopulation.size(); j++) {
                    Solution sol = externalPopulation.get(j);
                    if (MOOPUtils.dominates(y, sol)) {
                        externalPopulation.remove(j);
                        j--;
                    }
                }
                boolean dominated = false;
                for (Solution sol : externalPopulation) {
                    if (MOOPUtils.dominates(sol, y)) {
                        dominated = true;
                        break;
                    }
                }
                if (!dominated && externalPopulation.size() < archiveSize) {
                    externalPopulation.add(y);
                }
            }
            gen++;
            if (gen > maxGen) break;
        }

    }

    private double euclidianDistance(double[] v1, double[] v2) {
        double dist = 0;
        for (int i = 0; i < v1.length; i++) {
            dist += (v1[i] - v2[i]) * (v1[i] - v2[i]);
        }
        return Math.sqrt(dist);
    }

    protected abstract double scalarizationFunction(Solution s, double[] weights, double[] ideal);

    @Override
    public List<Solution> getNondominatedSolutions() {
        return externalPopulation;
    }

    @Override
    public List<List<Solution>> getParetoFronts() {
        throw new UnsupportedOperationException("This algorithm does not employ non-dominated sorting!");
    }
}
