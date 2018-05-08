package hr.fer.zemris.zavrsni.algorithms.operators.selection;

import hr.fer.zemris.zavrsni.algorithms.AbstractMOOPAlgorithm;
import hr.fer.zemris.zavrsni.algorithms.operators.Selection;
import hr.fer.zemris.zavrsni.solution.FitnessSolution;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class CrowdedTournamentSelection implements Selection<FitnessSolution<Double>> {

    private AbstractMOOPAlgorithm<FitnessSolution<Double>> moop;
    private int tournamentSize;
    private Random rand = new Random();

    public CrowdedTournamentSelection(AbstractMOOPAlgorithm<FitnessSolution<Double>> moop, int tournamentSize) {
        this.tournamentSize = tournamentSize;
        this.moop = moop;
    }

    @Override
    public FitnessSolution<Double> select(List<FitnessSolution<Double>> population) {
        int[] selected = rand.ints(0, population.size()).distinct().limit(tournamentSize).toArray();
        int index = 0;
        Map<FitnessSolution<Double>, Integer> ranks = getRanks(moop.getParetoFronts());
        for (int i = 1; i < selected.length; i++) {
            if (ranks.get(population.get(selected[i])) < ranks.get(population.get(selected[index]))) index = i;
            else if (ranks.get(population.get(selected[i])).equals(ranks.get(population.get(selected[index])))) {
                if (population.get(selected[i]).getFitness() < population.get(selected[index]).getFitness()) index = i;
            }
        }
        return population.get(selected[index]);
    }

    private Map<FitnessSolution<Double>, Integer> getRanks(List<List<FitnessSolution<Double>>> fronts) {
        int i = 0;
        Map<FitnessSolution<Double>, Integer> map = new HashMap<>();
        for (List<FitnessSolution<Double>> l : fronts) {
            for (FitnessSolution<Double> f : l) {
                map.put(f, i);
            }
            i++;
        }
        return map;
    }
}
