package hr.fer.zemris.zavrsni.algorithms.operators.crossover;

import hr.fer.zemris.zavrsni.algorithms.MOOPUtils;
import hr.fer.zemris.zavrsni.algorithms.operators.Crossover;
import hr.fer.zemris.zavrsni.solution.Solution;
import hr.fer.zemris.zavrsni.solution.SolutionFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

// ----------------------------------------------------------------------
// The implementation was adapted from the code of function realcross() in crossover.c
// http://www.iitk.ac.in/kangal/codes/nsga2/nsga2-gnuplot-v1.1.6.tar.gz
//
// ref: http://www.slideshare.net/paskorn/simulated-binary-crossover-presentation#
// ----------------------------------------------------------------------

public class SBXCrossover<T extends Solution> extends Crossover<T> {

    private final double eta;
    private double[] lowerBounds;
    private double[] upperBounds;
    private Random rand = new Random();

    public SBXCrossover(SolutionFactory<T> factory, double eta, double[] lowerBounds, double[] upperBounds) {
        super(factory);
        this.eta = eta;
        this.lowerBounds = lowerBounds;
        this.upperBounds = upperBounds;
    }

    private double generateBeta(double r, double alpha){
        double beta;
        if(r <= 1. / alpha) {
            beta = Math.pow(r * alpha, 1. / (eta + 1.));
        }
        else{
            beta = Math.pow(1./ (2. - r * alpha), 1. / (eta + 1.));
        }
        return beta;
    }

    @Override public List<T> cross(T p1, T p2) {
        double[] h1 = p1.getVariables();
        double[] h2 = p2.getVariables();
        double[] c1 = h1.clone();
        double[] c2 = h2.clone();
        for(int i = 0; i < c1.length; i++){
            if(rand.nextBoolean()) continue;
            if(Math.abs(h1[i] - h2[i]) < MOOPUtils.EPSILON) continue;
            double y1 = Math.min(h1[i], h2[i]);
            double y2 = Math.max(h1[i], h2[i]);

            double r = rand.nextDouble();

            //CHILD 1
            double beta = 1.0 + (2.0*(y1-lowerBounds[i])/(y2-y1)),
                    alpha = 2.0 - Math.pow(beta, -(eta+1.0));
            double betaq = generateBeta(r, alpha);
            c1[i] = 0.5*((y1+y2)-betaq*(y2-y1));

            //CHILD 2
            beta = 1.0 + (2.0*(upperBounds[i]-y2)/(y2-y1));
            alpha = 2.0 - Math.pow(beta, -(eta+1.0));
            betaq = generateBeta(r, alpha);
            c2[i] = 0.5*((y1+y2)+betaq*(y2-y1));

            c1[i] = MOOPUtils.constrainWithinInterval(c1[i], lowerBounds[i], upperBounds[i]);
            c2[i] = MOOPUtils.constrainWithinInterval(c2[i], lowerBounds[i], upperBounds[i]);

            if (rand.nextBoolean()){
                double help = c1[i];
                c1[i] = c2[i];
                c2[i] = help;
            }
        }
        return Arrays.asList(factory.create(c1, p1.getObjectives().length),
                factory.create(c2, p1.getObjectives().length));
    }
}
