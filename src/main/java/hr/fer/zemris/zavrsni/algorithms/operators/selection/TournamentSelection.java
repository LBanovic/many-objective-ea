package hr.fer.zemris.zavrsni.algorithms.operators.selection;

import hr.fer.zemris.zavrsni.algorithms.operators.Selection;
import hr.fer.zemris.zavrsni.solution.FitnessSolution;

import java.util.List;
import java.util.Random;

public class TournamentSelection<T extends Number> implements Selection<FitnessSolution<T>> {

    private int k;
    private Random rand = new Random();
    private boolean minimization;

    public TournamentSelection(int k, boolean minimization) {
        this.k = k;
        this.minimization = minimization;
    }

    @Override public FitnessSolution<T> select(List<FitnessSolution<T>> population) {
        int[] selected = rand.ints(0, population.size()).distinct().limit(k).toArray();
        int index = 0;
        for(int i = 0; i < selected.length; i++){
            double current = population.get(selected[i]).getFitness().doubleValue();
            double former = population.get(selected[index]).getFitness().doubleValue();
            if(current == former) index = (rand.nextBoolean() ? i : index);
            if(minimization && current < former) index = i;
            if(!minimization && current > former) index = i;
        }
        return population.get(index);
    }
}
