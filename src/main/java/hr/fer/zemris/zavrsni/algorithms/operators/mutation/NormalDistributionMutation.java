package hr.fer.zemris.zavrsni.algorithms.operators.mutation;

import hr.fer.zemris.zavrsni.algorithms.MOOPUtils;
import hr.fer.zemris.zavrsni.algorithms.operators.Mutation;
import hr.fer.zemris.zavrsni.solution.Solution;

import java.util.Random;

public class NormalDistributionMutation implements Mutation {

    private double[] lowerBounds;
    private double[] upperBounds;
    private double sigma;
    private double mutationChance;
    private Random rand = new Random();

    /**
     * Constructs a new NormalDistributionMutation.
     * @param lowerBounds the lower bounds of the values of variables
     * @param upperBounds the upper bounds of the values of variables
     * @param sigma changes the variance of the distribution
     * @param mutationChance the chance of a mutation occurring
     */
    public NormalDistributionMutation(double[] lowerBounds, double[] upperBounds, double sigma, double mutationChance) {
        this.lowerBounds = lowerBounds;
        this.upperBounds = upperBounds;
        this.sigma = sigma;
        this.mutationChance = mutationChance;
    }

    @Override public void mutate(Solution child) {
        double[] vars = child.getVariables();
        for(int i = 0; i < vars.length; i++){
            if(rand.nextFloat() < mutationChance) {
                vars[i] += rand.nextGaussian() * sigma;
                vars[i] = MOOPUtils.constrainWithinInterval(vars[i], lowerBounds[i], upperBounds[i]);
            }
        }
    }
}
