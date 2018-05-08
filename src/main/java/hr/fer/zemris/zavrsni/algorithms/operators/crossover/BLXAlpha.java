package hr.fer.zemris.zavrsni.algorithms.operators.crossover;

import hr.fer.zemris.zavrsni.algorithms.MOOPUtils;
import hr.fer.zemris.zavrsni.algorithms.operators.Crossover;
import hr.fer.zemris.zavrsni.solution.Solution;
import hr.fer.zemris.zavrsni.solution.SolutionFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Class that implements BLX-alpha crossover.
 */
public class BLXAlpha<T extends Solution> extends Crossover<T> {

    private double alpha;
    private double[] lowerBounds;
    private double[] upperBounds;

    private Random rand = new Random();

    /**
     * Constructs new BLX-alpha crossover.
     * @param alpha parameter with which to perform the crossover
     */
    public BLXAlpha(SolutionFactory<T> factory, double alpha, double[] lowerBounds, double[] upperBounds) {
        super(factory);
        this.alpha = alpha;
        this.lowerBounds = lowerBounds;
        this.upperBounds = upperBounds;
    }

    @Override public List<T> cross(T p1, T p2) {
        double[] h1 = p1.getVariables();
        double[] h2 = p2.getVariables();
        double[] c = new double[h1.length];
        List<T> solutions = new LinkedList<>();
        for(int i = 0; i < h1.length; i++){
            double min = h1[i] < h2[i] ? h1[i] : h2[i];
            double max = h1[i] >= h2[i] ? h1[i] : h2[i];
            double I = max - min;
            c[i] = rand.nextDouble() * (max - min + 2 * I * alpha) + min - I * alpha;
            c[i] = MOOPUtils.constrainWithinInterval(c[i], lowerBounds[i], upperBounds[i]);
        }
        solutions.add(factory.create(c, p1.getObjectives().length));
        return solutions;
    }
}
