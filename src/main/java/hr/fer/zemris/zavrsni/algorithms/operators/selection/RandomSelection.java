package hr.fer.zemris.zavrsni.algorithms.operators.selection;

import hr.fer.zemris.zavrsni.algorithms.operators.Selection;
import hr.fer.zemris.zavrsni.solution.Solution;

import java.util.Random;

public class RandomSelection implements Selection {

    private Random rand = new Random();

    @Override
    public Solution select(Solution[] population) {
        return population[rand.nextInt(population.length)];
    }
}
