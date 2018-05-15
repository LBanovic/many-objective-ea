package hr.fer.zemris.zavrsni.solution;

import java.util.ArrayList;
import java.util.List;

public class FitnessSolution<V extends Number> extends Solution implements Comparable<FitnessSolution<V>>{

    private V fitness;

    public FitnessSolution(double[] variables, int numberOfObjectives) {
        super(variables, numberOfObjectives);
    }

    private FitnessSolution(Solution s){
        super(s);
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

    public static <S extends Number> List<FitnessSolution<S>> encapsulateSolution
            (List<? extends Solution> population){
        List<FitnessSolution<S>> newList = new ArrayList<>(population.size());
        for (Solution s :
                population) {
            newList.add(new FitnessSolution<>(s));
        }
        return newList;
    }
}
