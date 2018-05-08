package hr.fer.zemris.zavrsni.algorithms.providers;

import hr.fer.zemris.zavrsni.algorithms.AbstractMOOPAlgorithm;
import hr.fer.zemris.zavrsni.solution.FitnessSolution;

import java.util.*;

public class CrowdProvider implements ValueProvider<Double> {

    private AbstractMOOPAlgorithm<FitnessSolution<Double>> moop;

    public CrowdProvider(AbstractMOOPAlgorithm<FitnessSolution<Double>> moop){
        this.moop = moop;
    }

    @Override
    public void provide(List<FitnessSolution<Double>> population) {
        List<List<FitnessSolution<Double>>> fronts = moop.getParetoFronts();
        for(List<FitnessSolution<Double>> l : fronts){

            int numberOfObjectives = population.get(0).getObjectives().length;

            for(FitnessSolution<Double> f : l){
                f.setFitness(0.);
            }

            for(int i = 0; i < numberOfObjectives; i++){
                Integer[] indices = new Integer[l.size()];
                final int k = i;
                for(int j = 0; j < indices.length; j++) indices[j] = j;
                Integer[] help = Arrays.copyOf(indices, indices.length);
                Arrays.sort(indices, Comparator.comparingDouble(o -> l.get(help[o]).getObjectives()[k]));
                FitnessSolution<Double> bottom = l.get(indices[0]);
                FitnessSolution<Double> top = l.get(indices[indices.length - 1]);

                top.setFitness(Double.MAX_VALUE);
                bottom.setFitness(Double.MAX_VALUE);

                for(int j = 1; j < indices.length - 1; j++){
                    FitnessSolution<Double> currentSol = l.get(indices[j]);
                    double currentFitness = currentSol.getFitness();
                    currentFitness += (l.get(indices[j + 1]).getObjectives()[i] -
                            l.get(indices[j - 1]).getObjectives()[i]) /
                            (top.getObjectives()[i] - bottom.getObjectives()[i]);
                    currentSol.setFitness(currentFitness);
                }
            }
        }
    }
}
