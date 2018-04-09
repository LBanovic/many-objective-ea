package hr.fer.zemris.zavrsni.algorithms.operators.crossover;

import hr.fer.zemris.zavrsni.algorithms.MOOPUtils;
import hr.fer.zemris.zavrsni.algorithms.operators.Crossover;
import hr.fer.zemris.zavrsni.solution.Solution;

import java.util.Random;

/**
 * Class that implements BLX-alpha crossover.
 */
public class BLXAlpha implements Crossover {

    private double alpha;
    private double[] lowerBounds;
    private double[] upperBounds;

    private Random rand = new Random();

    /**
     * Constructs new BLX-alpha crossover.
     * @param alpha parameter with which to perform the crossover
     */
    public BLXAlpha(double alpha, double[] lowerBounds, double[] upperBounds) {
        this.alpha = alpha;
        this.lowerBounds = lowerBounds;
        this.upperBounds = upperBounds;
    }

    @Override public Solution cross(Solution p1, Solution p2) {
        double[] h1 = p1.getVariables();
        double[] h2 = p2.getVariables();
        double[] c = new double[h1.length];
        for(int i = 0; i < h1.length; i++){
            double min = h1[i] < h2[i] ? h1[i] : h2[i];
            double max = h1[i] >= h2[i] ? h1[i] : h2[i];
            double I = max - min;
            c[i] = rand.nextDouble() * (max - min + 2 * I * alpha) + min - I * alpha;
            c[i] = MOOPUtils.constrainWithinInterval(c[i], lowerBounds[i], upperBounds[i]);
        }
        return new Solution(c, p1.getObjectives().length);
    }
}
