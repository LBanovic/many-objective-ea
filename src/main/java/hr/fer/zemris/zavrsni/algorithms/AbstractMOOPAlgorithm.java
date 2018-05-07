package hr.fer.zemris.zavrsni.algorithms;

import hr.fer.zemris.zavrsni.evaluator.MOOPProblem;
import hr.fer.zemris.zavrsni.solution.Solution;

import java.util.LinkedList;
import java.util.List;

public abstract class AbstractMOOPAlgorithm {

    protected Solution[]           population;
    protected MOOPProblem          problem;
    protected List<List<Solution>> fronts;

    public AbstractMOOPAlgorithm(
        Solution[] population,
        MOOPProblem problem
    ) {
        this.population = population;
        this.problem = problem;
        fronts = new LinkedList<>();
    }

    /**
     * Runs the algorithm.
     */
    public abstract void run();

    public abstract List<Solution> getNondominatedSolutions();

    public int populationSize(){
        return population.length;
    }

    public List<List<Solution>> getParetoFronts(){
        return fronts;
    }
}
