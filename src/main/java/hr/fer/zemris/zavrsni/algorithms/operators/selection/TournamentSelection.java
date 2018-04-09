package hr.fer.zemris.zavrsni.algorithms.operators.selection;

import hr.fer.zemris.zavrsni.algorithms.providers.ValueProvider;
import hr.fer.zemris.zavrsni.algorithms.operators.Selection;
import hr.fer.zemris.zavrsni.solution.Solution;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class TournamentSelection<T extends Number> implements Selection {

    private int k;
    private ValueProvider<T> provider;
    private Solution[] oldPopulation;
    private Random rand = new Random();
    private Map<Solution, T> values;
    private boolean minimization;

    public TournamentSelection(int k, boolean minimization) {
        this.k = k;
        this.minimization = minimization;
        values = new HashMap<>();
    }

    @SuppressWarnings("unchecked")
    @Override public void initializeValueProviders(ValueProvider... providers) {
        this.provider = (ValueProvider<T>) providers[0];
    }

    @Override public Solution select(Solution[] population) {
        if(oldPopulation != population){
            oldPopulation = population;
            provider.provide(values);
        }
        int[] selected = rand.ints(0, population.length).distinct().limit(k).toArray();
        int index = 0;
        for(int i = 0; i < selected.length; i++){
            double current = values.get(population[selected[i]]).doubleValue();
            double former = values.get(population[selected[index]]).doubleValue();
            if(minimization && current < former) index = i;
            if(!minimization && current > former) index = i;
        }
        return population[index];
    }
}
