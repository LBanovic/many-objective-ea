package hr.fer.zemris.zavrsni.algorithms.operators.selection;

import hr.fer.zemris.zavrsni.algorithms.operators.Selection;
import hr.fer.zemris.zavrsni.solution.Solution;

import java.util.List;
import java.util.Random;

public class RandomSelection implements Selection<Solution> {

    private Random rand = new Random();

    @Override
    public Solution select(List<Solution> population) {
        return population.get(rand.nextInt(population.size()));
    }
}
