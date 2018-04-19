package hr.fer.zemris.zavrsni.algorithms.moead;

import hr.fer.zemris.zavrsni.algorithms.AbstractMOOPAlgorithm;
import hr.fer.zemris.zavrsni.algorithms.MOOPUtils;
import hr.fer.zemris.zavrsni.algorithms.nsga3.NSGA3Util;
import hr.fer.zemris.zavrsni.algorithms.operators.Crossover;
import hr.fer.zemris.zavrsni.algorithms.operators.Mutation;
import hr.fer.zemris.zavrsni.algorithms.operators.Selection;
import hr.fer.zemris.zavrsni.evaluator.MOOPProblem;
import hr.fer.zemris.zavrsni.solution.Solution;

import java.util.*;

public class MOEAD extends AbstractMOOPAlgorithm {

    private final double[][] weights;

    private Random rand = new Random();

    private Map<Integer, int[]> neighbourhoods;

    private List<Solution> externalPopulation;

    private double[] idealPoint;

    private final Mutation mutation;
    private final Crossover crossover;
    private final int maxGen;

    public MOEAD(Solution[] population, MOOPProblem problem, int closestVectors, int parameterH,
                 Mutation mutation, Crossover crossover, int maxGen) {
        super(population, problem);

        this.mutation = mutation;
        this.crossover = crossover;
        this.maxGen = maxGen;
        int numberOfWeights = NSGA3Util.binomialCoefficient(parameterH + problem.getNumberOfObjectives() - 1,
                problem.getNumberOfObjectives() - 1);
        weights = new double[numberOfWeights][problem.getNumberOfObjectives()];
        initializeWeights(problem, parameterH, weights);
        this.neighbourhoods = new HashMap<>();
        for (int i = 0; i < numberOfWeights; i++) {
            int[] neighbours = new int[closestVectors];
            double lastDistance = 0;
            neighbours[0] = i;
            for (int k = 1; k < closestVectors; k++) {
                int min = i;
                double currentMinDistance = Double.MAX_VALUE;
                for (int j = 0; j < numberOfWeights; j++) {

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
        MOOPUtils.evaluatePopulation(population, problem);
        this.idealPoint = new double[problem.getNumberOfObjectives()];
        for (int i = 0; i < idealPoint.length; i++) {
            final int k = i;
            idealPoint[i] = Collections.max(Arrays.asList(population), Comparator.comparingDouble(o -> o.getObjectives()[k])).getObjectives()[i];
        }
    }

    //TODO initialize weights == use recursion ?? prepravi ovo
    private static void recursiveWeights(List<double[]> weights, double[] weight, int element, int numberOfObjectives, int left, int total) {
        if (element == numberOfObjectives - 1) {
            weight[element] = (double) left / total;
            weights.add(weight.clone());
        } else {
            for (int i = 0; i <= left; i++) {
                weight[element] = (double) i / total;
                recursiveWeights(weights, weight, element + 1, numberOfObjectives, left - i, total);
            }
        }
    }

    private static void initializeWeights(MOOPProblem problem, int parameterH, double[][] realWeights) {
        List<double[]> weights = new LinkedList<>();
        double[] weight = new double[problem.getNumberOfObjectives()];
        recursiveWeights(weights, weight, 0, problem.getNumberOfObjectives(), parameterH, parameterH);
        for (int i = 0; i < weights.size(); i++) {
            realWeights[i] = weights.get(i);
        }
    }

    @Override
    public void run() {
        int gen = 0;
        while (true) {
            System.out.println(gen);
            for (int i = 0; i < population.length; i++) {
                int[] indices = neighbourhoods.get(i);
                int index1, index2;
                index1 = indices[rand.nextInt(indices.length)];
                do {
                    index2 = indices[rand.nextInt(indices.length)];
                } while (index1 == index2);
                Solution y = crossover.cross(population[index1], population[index2]);
                mutation.mutate(y);
                problem.evaluateObjectives(y);
                double[] obj = y.getObjectives();
                for (int j = 0; j < problem.getNumberOfObjectives(); j++) {
                    if (idealPoint[j] < obj[j]) idealPoint[j] = obj[j];
                }
                for (int j : indices) {
                    if(calculateTchebycheff(y, weights[j], idealPoint) <=
                            calculateTchebycheff(population[j], weights[j], idealPoint)){
                        population[j] = y;
                    }
                }
                for(int j = 0; j < externalPopulation.size(); j++){
                    Solution sol = externalPopulation.get(j);
                    if(MOOPUtils.dominates(y, sol)){
                        externalPopulation.remove(j);
                        j--;
                    }
                }
                boolean dominated = false;
                for(Solution sol : externalPopulation){
                    if(MOOPUtils.dominates(sol, y)){
                        dominated = true;
                        break;
                    }
                }
                if(!dominated) externalPopulation.add(y);
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

    @Override
    public List<Solution> getNondominatedSolutions() {
        return externalPopulation;
    }

    @Override
    public List<List<Solution>> paretoFronts() {
        throw new UnsupportedOperationException("This algorithm does not employ non dominated sorting!");
    }

    private double calculateTchebycheff(Solution y, double[] weights, double[] idealPoint){
        double max = Double.MIN_VALUE;
        double[] obj = y.getObjectives();
        for(int i = 0; i < obj.length; i++){
            double value = weights[i] * Math.abs(obj[i] - idealPoint[i]);
            if(value > max) max = value;
        }
        return max;
    }

//    public static void main(String[] args) {
//        MOOPProblem m = new DTLZ1(3);
//        double[][] weights = new double[351][3];
//        MOEAD.initializeWeights(m, 25, weights);
//    }
}
