package hr.fer.zemris.zavrsni.algorithms.providers;

import hr.fer.zemris.zavrsni.algorithms.AbstractMOOPAlgorithm;
import hr.fer.zemris.zavrsni.solution.FitnessSolution;

import java.util.List;

public class RankProvider<T extends Number> implements ValueProvider<T> {

    private AbstractMOOPAlgorithm<FitnessSolution<T>> moop;

    public RankProvider(AbstractMOOPAlgorithm<FitnessSolution<T>> moop) {
        this.moop = moop;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void provide(List<FitnessSolution<T>> population) {
        List<List<FitnessSolution<T>>> fronts = moop.getParetoFronts();
        int i = 0;
        for(List<FitnessSolution<T>> f : fronts){
            for(FitnessSolution s : f){
                s.setFitness(i);
            }
            i++;
        }
    }
}
