package hr.fer.zemris.zavrsni.algorithms;

import hr.fer.zemris.zavrsni.algorithms.operators.Crossover;
import hr.fer.zemris.zavrsni.algorithms.operators.Mutation;
import hr.fer.zemris.zavrsni.algorithms.operators.Selection;
import hr.fer.zemris.zavrsni.evaluator.MOOPProblem;
import hr.fer.zemris.zavrsni.solution.Solution;

import java.util.LinkedList;
import java.util.List;

public abstract class AbstractMOOPAlgorithm {

    protected Solution[]           population;
    protected MOOPProblem          problem;
    protected List<List<Solution>> fronts;
    protected final int maxGen;
    protected final Crossover crossover;
    protected final Mutation mutation;

    protected Selection selection;

    public AbstractMOOPAlgorithm(
            Solution[] population,
            MOOPProblem problem,
            int maxGen,
            Crossover crossover,
            Mutation mutation) {
        this.population = population;
        this.problem = problem;
        this.maxGen = maxGen;
        fronts = new LinkedList<>();
        this.crossover = crossover;
        this.mutation = mutation;
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

    public Solution[] getPopulation(){return population;}
}
