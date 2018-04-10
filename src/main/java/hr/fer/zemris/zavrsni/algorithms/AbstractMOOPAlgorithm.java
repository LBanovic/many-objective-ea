package hr.fer.zemris.zavrsni.algorithms;

import hr.fer.zemris.zavrsni.evaluator.MOOPProblem;
import hr.fer.zemris.zavrsni.solution.Solution;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public abstract class AbstractMOOPAlgorithm {

    protected Solution[]           population;
    protected List<List<Solution>> fronts;
    protected MOOPProblem          problem;

    public AbstractMOOPAlgorithm(
        Solution[] population,
        MOOPProblem problem
    ) {
        this.population = population;
        this.problem = problem;
        this.fronts = new LinkedList<>();
    }

    /**
     * Runs the algorithm.
     */
    public abstract void run();

    /**
     * Returns the found Pareto fronts. The fronts are sorted by fitness so the first front is optimal.
     * @return all found Pareto fronts
     */
    public List<List<Solution>> paretoFronts(){
        return fronts;
    }

    public List<Solution> getNondominatedSolutions(){
        return fronts.get(0);
    }

    public int populationSize(){
        return population.length;
    }
}
