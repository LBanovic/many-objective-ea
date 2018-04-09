package hr.fer.zemris.zavrsni.algorithms.providers;

import hr.fer.zemris.zavrsni.solution.Solution;

import java.util.Map;

public interface ValueProvider<V> {
    public void provide(Map<Solution, V> map);
}
