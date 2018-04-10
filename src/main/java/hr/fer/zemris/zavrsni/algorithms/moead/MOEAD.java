package hr.fer.zemris.zavrsni.algorithms.moead;

import hr.fer.zemris.zavrsni.algorithms.AbstractMOOPAlgorithm;
import hr.fer.zemris.zavrsni.algorithms.nsga3.NSGA3Util;
import hr.fer.zemris.zavrsni.evaluator.MOOPProblem;
import hr.fer.zemris.zavrsni.solution.Solution;

import java.util.*;

public class MOEAD extends AbstractMOOPAlgorithm {

    private final int closestVectors;
    private final int parameterH;
    private final double[][] weights;

    private Map<Integer, int[]> neighbourhoods;


    public MOEAD(Solution[] population, MOOPProblem problem, int closestVectors, int parameterH) {
        super(population, problem);

        this.closestVectors = closestVectors;
        this.parameterH = parameterH;
        int numberOfWeights = NSGA3Util.binomialCoefficient(parameterH + problem.getNumberOfObjectives() - 1,
                            problem.getNumberOfObjectives() - 1);
        weights = new double[numberOfWeights][problem.getNumberOfObjectives()];
        initializeWeights();
        this.neighbourhoods = new HashMap<>();
        for(int i = 0; i < numberOfWeights; i++){
            int[] neighbours = new int[closestVectors];
            double lastDistance = 0;
            double currentMinDistance = Double.MAX_VALUE;
            for(int k = 0; k < closestVectors; k++) {
                int min = (lastDistance == 0 ? 1 : 0);
                for (int j = 0; j < numberOfWeights; j++) {
                    if (j != i) {
                        double dist = euclidianDistance(weights[i], weights[j]);
                        if (dist < currentMinDistance && dist > lastDistance) {
                            min = j;
                            currentMinDistance = dist;
                        }
                    }
                }
                neighbours[k] = min;
                lastDistance = currentMinDistance;
            }
            neighbourhoods.put(i, neighbours);
        }
    }

    private void initializeWeights() {
        //TODO initialize weights == use recursion ??
    }

    @Override
    public void run() {
        //TODO

    }

    private double euclidianDistance(double[] v1, double[] v2){
        double dist = 0;
        for(int i = 0; i < v1.length; i++){
            dist += (v1[i] - v2[i]) * (v1[i] - v2[i]);
        }
        return Math.sqrt(dist);
    }

    @Override
    public List<Solution> getNondominatedSolutions() {
        return super.getNondominatedSolutions();
    }

    @Override
    public List<List<Solution>> paretoFronts() {
        throw new UnsupportedOperationException("This algorithm does not employ non dominated sorting!");
    }
}
