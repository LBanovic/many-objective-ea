package hr.fer.zemris.zavrsni.algorithms;

import hr.fer.zemris.zavrsni.algorithms.operators.Crossover;
import hr.fer.zemris.zavrsni.algorithms.operators.Mutation;
import hr.fer.zemris.zavrsni.algorithms.operators.Selection;
import hr.fer.zemris.zavrsni.evaluator.MOOPProblem;
import hr.fer.zemris.zavrsni.solution.Solution;

import java.util.List;

public abstract class AbstractMOOPAlgorithm<T extends Solution> {

    protected List<T> population;
    protected MOOPProblem problem;
    protected final int maxGen;
    protected final Crossover<T> crossover;
    protected final Mutation mutation;

    protected Selection<T> selection;

    public AbstractMOOPAlgorithm(
            List<T> population,
            MOOPProblem problem,
            int maxGen,
            Crossover<T> crossover,
            Mutation mutation) {
        this.population = population;
        this.problem = problem;
        this.maxGen = maxGen;
        this.crossover = crossover;
        this.mutation = mutation;
    }

    /**
     * Runs the algorithm.
     */
    public abstract void run();

    public abstract List<T> getNondominatedSolutions();

    public int populationSize() {
        return population.size();
    }

    public List<T> getPopulation() {
        return population;
    }

    public List<List<T>> getParetoFronts() {
        throw new UnsupportedOperationException("This algorithm does not employ non-dominated sorting!");
    }
}
