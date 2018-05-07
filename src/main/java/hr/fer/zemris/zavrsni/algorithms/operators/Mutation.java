package hr.fer.zemris.zavrsni.algorithms.operators;

import hr.fer.zemris.zavrsni.solution.Solution;

public interface Mutation {
    /**
     * Mutates a Solution according to the implemented strategy.
     * @param child Solution to mutate
     */
    void mutate(Solution child);
}
