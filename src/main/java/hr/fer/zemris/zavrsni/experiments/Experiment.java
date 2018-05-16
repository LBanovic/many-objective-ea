package hr.fer.zemris.zavrsni.experiments;

import hr.fer.zemris.zavrsni.algorithms.AbstractMOOPAlgorithm;
import hr.fer.zemris.zavrsni.evaluator.MOOPProblem;
import hr.fer.zemris.zavrsni.solution.Solution;

import java.util.List;

public abstract class Experiment<S extends Solution> {
    public final AbstractMOOPAlgorithm<S> run(MOOPProblem problem, List<Solution> population,
                                              int maxGen, String... params){
        AbstractMOOPAlgorithm<S> moop = getAlgorithm(problem, population, maxGen, params);
        moop.run();
        return moop;
    }

    protected abstract AbstractMOOPAlgorithm<S> getAlgorithm(MOOPProblem problem, List<Solution> population,
                                                             int maxGen,
                                                             String... params);
}
