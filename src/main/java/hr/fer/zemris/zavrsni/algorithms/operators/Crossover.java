package hr.fer.zemris.zavrsni.algorithms.operators;

import hr.fer.zemris.zavrsni.solution.Solution;

public interface Crossover {

    /**
     * Returns a child that is generated using a crossover strategy on the parents.
     * @param p1 the first parent
     * @param p2 the second parent
     * @return a child
     */
    public Solution cross(Solution p1, Solution p2);
}
