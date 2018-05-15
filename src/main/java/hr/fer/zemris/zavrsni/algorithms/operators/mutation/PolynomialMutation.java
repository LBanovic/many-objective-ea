package hr.fer.zemris.zavrsni.algorithms.operators.mutation;

import hr.fer.zemris.zavrsni.algorithms.MOOPUtils;
import hr.fer.zemris.zavrsni.algorithms.operators.Mutation;
import hr.fer.zemris.zavrsni.solution.Solution;

import java.util.Random;

/* PRILAGOĐENO IZ CPP implementacije nsga3 */
public class PolynomialMutation implements Mutation {

    private Random rand = new Random();
    private double[] lowerBound;
    private double[] upperBound;
    private double mutationChance;
    private double eta;

    public PolynomialMutation(double[] lowerBound, double[] upperBound, double mutationChance, double eta) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.mutationChance = mutationChance;
        this.eta = eta;
    }

    @Override
    public void mutate(Solution child) {
        double[] c = child.getVariables();
        for(int i = 0; i < c.length; i++){
            if(rand.nextDouble() < mutationChance){
                c[i] += generateDelta(rand.nextDouble(), c[i], lowerBound[i], upperBound[i])
                        * ((upperBound[i] - lowerBound[i]));
                c[i] = MOOPUtils.constrainWithinInterval(c[i], lowerBound[i], upperBound[i]);
            }
        }
    }

    private double generateDelta(double p, double y, double lowerBound, double upperBound){
        if(p < 0.5){
            double x = (upperBound - y) / (upperBound - lowerBound);
            double value = 2 * p + (1 - 2 * p) * Math.pow(x, eta + 1);
            return Math.pow(value, 1. / (eta + 1)) - 1;
        }else{
            double x = (y - lowerBound) / (upperBound - lowerBound);
            double value = 2 * (1 - p) + 2 * (p - 0.5) * Math.pow(x, eta + 1);
            return 1 - Math.pow(value, 1. / (eta + 1));
        }
    }
}
