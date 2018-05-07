package hr.fer.zemris.zavrsni.algorithms.operators;

import hr.fer.zemris.zavrsni.algorithms.providers.ValueProvider;
import hr.fer.zemris.zavrsni.solution.Solution;

public interface Selection {
    /**
     * Selects a unit from the population according to the implemented strategy.
     * @param population population to select from
     * @return index of the selected unit in the population
     */
    Solution select(Solution[] population);

    default void initializeValueProviders(ValueProvider... providers){

    }
}
