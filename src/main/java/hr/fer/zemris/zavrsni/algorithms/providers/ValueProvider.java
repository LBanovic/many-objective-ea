package hr.fer.zemris.zavrsni.algorithms.providers;

import hr.fer.zemris.zavrsni.solution.Solution;

import java.util.List;
import java.util.Map;

public interface ValueProvider<V> {
    default void provide(Map<Solution, V> map){
        throw new UnsupportedOperationException("Cannot acquire the solutions to provide the value for!");
    }
    default void provide(Map<Solution, V> map, List<Solution> solutions){
        provide(map);
    }
}
