package hr.fer.zemris.zavrsni.algorithms.operators;

import hr.fer.zemris.zavrsni.solution.Solution;
import hr.fer.zemris.zavrsni.solution.SolutionFactory;

import java.util.List;

public abstract class Crossover<T extends Solution> {

    protected final SolutionFactory<T> factory;

    public Crossover(SolutionFactory<T> factory) {
        this.factory = factory;
    }

    /**
     * Returns a child that is generated using a crossover strategy on the parents.
     * @param p1 the first parent
     * @param p2 the second parent
     * @return a child
     */
    public abstract List<T> cross(T p1, T p2);
}
