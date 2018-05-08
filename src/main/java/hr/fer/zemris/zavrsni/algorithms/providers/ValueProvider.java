package hr.fer.zemris.zavrsni.algorithms.providers;

import hr.fer.zemris.zavrsni.solution.FitnessSolution;
import hr.fer.zemris.zavrsni.solution.Solution;

import java.util.List;
import java.util.Map;

public interface ValueProvider<V extends Number> {
    void provide(List<FitnessSolution<V>> population);
}
