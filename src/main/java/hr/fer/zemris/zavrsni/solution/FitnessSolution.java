package hr.fer.zemris.zavrsni.solution;

public class FitnessSolution<V extends Number> extends Solution implements Comparable<FitnessSolution<V>>{

    private V fitness;

    public FitnessSolution(double[] variables, int numberOfObjectives) {
        super(variables, numberOfObjectives);
    }

    public V getFitness() {
        return fitness;
    }

    public void setFitness(V fitness) {
        this.fitness = fitness;
    }

    @Override
    public int compareTo(FitnessSolution<V> o) {
        return Double.compare(this.fitness.doubleValue(), o.getFitness().doubleValue());
    }
}
