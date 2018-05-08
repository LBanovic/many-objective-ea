package hr.fer.zemris.zavrsni.solution;

public class FitnessSolutionFactory<T extends Number> implements SolutionFactory<FitnessSolution<T>> {
    @Override
    public FitnessSolution<T> create(double[] variables, int numberOfObjectives) {
        return new FitnessSolution<>(variables, numberOfObjectives);
    }
}
