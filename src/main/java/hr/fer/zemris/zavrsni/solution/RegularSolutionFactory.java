package hr.fer.zemris.zavrsni.solution;

public class RegularSolutionFactory implements SolutionFactory<Solution> {
    @Override
    public Solution create(double[] variables, int numberOfObjectives) {
        return new Solution(variables, numberOfObjectives);
    }
}
