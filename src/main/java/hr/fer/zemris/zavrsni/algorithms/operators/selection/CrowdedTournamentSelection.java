package hr.fer.zemris.zavrsni.algorithms.operators.selection;

import hr.fer.zemris.zavrsni.algorithms.operators.Selection;
import hr.fer.zemris.zavrsni.algorithms.providers.ValueProvider;
import hr.fer.zemris.zavrsni.solution.Solution;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class CrowdedTournamentSelection implements Selection {

    private ValueProvider<Integer> rankProvider;
    private Map<Solution, Double> crowd;
    private Map<Solution, Integer> ranks;
    private int tournamentSize;
    private Solution[] oldPopulation;
    private Random rand = new Random();

    public CrowdedTournamentSelection(Map<Solution, Double> crowd, int tournamentSize) {
        this.crowd = crowd;
        this.tournamentSize = tournamentSize;
        this.ranks = new HashMap<>();
    }

    @Override public Solution select(Solution[] population) {
        if(oldPopulation != population){
            oldPopulation = population;
            rankProvider.provide(ranks);
        }
        int[] selected = rand.ints(0, population.length).distinct().limit(tournamentSize).toArray();
        int index = 0;
        for(int i = 1; i < selected.length; i++){
            if(ranks.get(population[selected[i]]) < ranks.get(population[selected[index]])) index = i;
            else if(ranks.get(population[selected[i]]).equals(ranks.get(population[selected[index]]))){
                if(crowd.get(population[selected[i]]) > crowd.get(population[selected[index]])) index = i;
            }
        }
        return population[selected[index]];
    }

    @SuppressWarnings("unchecked")
    @Override public void initializeValueProviders(ValueProvider... providers) {
        this.rankProvider = (ValueProvider<Integer>) providers[0];
    }
}
