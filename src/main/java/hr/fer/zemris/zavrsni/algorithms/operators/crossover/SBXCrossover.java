package hr.fer.zemris.zavrsni.algorithms.operators.crossover;

import hr.fer.zemris.zavrsni.algorithms.MOOPUtils;
import hr.fer.zemris.zavrsni.algorithms.operators.Crossover;
import hr.fer.zemris.zavrsni.solution.Solution;

import java.util.Random;

public class SBXCrossover implements Crossover {

    private final int n;
    private double[] lowerBounds;
    private double[] upperBounds;
    private Random rand = new Random();


    public SBXCrossover(int n, double[] lowerBounds, double[] upperBounds){
        this.n = n;
        this.lowerBounds = lowerBounds;
        this.upperBounds = upperBounds;
    }

    private double generateBeta(){
        double u = rand.nextDouble();
        if (u > 0.5){
            u -= 0.5;
            u = 1 - 2 * u;
            return Math.pow(u, 1 / (-n - 1.));
        }
        return Math.pow(u * 2, 1 / (n + 1.));
    }

    @Override public Solution cross(Solution p1, Solution p2) {
        double[] h1 = p1.getVariables();
        double[] h2 = p2.getVariables();
        double[] c1 = new double[h1.length];
        double[] c2 = new double[h2.length];
        double beta = generateBeta();
        for(int i = 0; i < c1.length; i++){
            double mean = (h1[i] + h2[i]) / 2;
            c1[i] = mean - 0.5 * beta * (h2[i] - h1[i]);
            c2[i] = mean + 0.5 * beta * (h2[i] - h1[i]);
            c1[i] = MOOPUtils.constrainWithinInterval(c1[i], upperBounds[i], lowerBounds[i]);
            c2[i] = MOOPUtils.constrainWithinInterval(c2[i], upperBounds[i], lowerBounds[i]);
        }
        if(rand.nextBoolean())
            return new Solution(c1, p1.getObjectives().length);
        return new Solution(c2, p2.getObjectives().length);
    }
}
