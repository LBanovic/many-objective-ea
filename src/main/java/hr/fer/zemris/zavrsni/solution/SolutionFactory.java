package hr.fer.zemris.zavrsni.solution;

public interface SolutionFactory<T extends Solution> {
    T create(double[] variables, int numberOfObjectives);
}
