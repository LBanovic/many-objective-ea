package hr.fer.zemris.zavrsni.algorithms.operators.selection;

import hr.fer.zemris.zavrsni.algorithms.FitnessObserver;
import hr.fer.zemris.zavrsni.algorithms.providers.ValueProvider;
import hr.fer.zemris.zavrsni.algorithms.operators.Selection;
import hr.fer.zemris.zavrsni.solution.Solution;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Class that implements basic roulette wheel selection.
 */
public class RouletteWheelSelection implements Selection, FitnessObserver{
    private Random rand = new Random();

    private Map<Solution, Double> dummyFitness = new HashMap<>();

    private ValueProvider<Double> provider;

    @SuppressWarnings("unchecked")
    @Override public void initializeValueProviders(ValueProvider... providers) {
        this.provider = (ValueProvider<Double>)providers[0];
    }

    @Override public Solution select(Solution[] population) {
        if(provider == null) throw new RuntimeException("Roulette selection fitness provider not initialized.");
        double minFitness = dummyFitness.get(population[0]);
        double[] allFitness = new double[population.length];

        for(int i = 0; i < population.length; i++) {
            allFitness[i] = dummyFitness.get(population[i]);
            if(allFitness[i] < minFitness)
                minFitness = allFitness[i];
        }

        double sumFitness = 0;

        for(int i = 0; i < allFitness.length; i++){
            allFitness[i] -= minFitness;
            sumFitness += allFitness[i];
        }

        int indexToReturn = population.length;
        double prob = rand.nextDouble() * sumFitness;
        for(int i = 0; i < allFitness.length; i++){
            prob -= allFitness[i];
            if(prob <= 0){
                indexToReturn = i;
                break;
            }
        }
        if(indexToReturn == population.length){
            indexToReturn = population.length - 1;
        }
        return population[indexToReturn];
    }

    @Override
    public void onFitnessChanged() {
        provider.provide(dummyFitness);
    }
}
