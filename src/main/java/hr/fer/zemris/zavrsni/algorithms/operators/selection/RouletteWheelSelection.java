package hr.fer.zemris.zavrsni.algorithms.operators.selection;

import hr.fer.zemris.zavrsni.algorithms.operators.Selection;
import hr.fer.zemris.zavrsni.solution.FitnessSolution;

import java.util.List;
import java.util.Random;

/**
 * Class that implements basic roulette wheel selection.
 */
public class RouletteWheelSelection implements Selection<FitnessSolution<Double>>{
    private Random rand = new Random();

    @Override public FitnessSolution<Double> select(List<FitnessSolution<Double>> population) {
        double minFitness = population.get(0).getFitness();
        double[] allFitness = new double[population.size()];

        for(int i = 0; i < population.size(); i++) {
            allFitness[i] = population.get(i).getFitness();
            if(allFitness[i] < minFitness)
                minFitness = allFitness[i];
        }

        double sumFitness = 0;

        for(int i = 0; i < allFitness.length; i++){
            allFitness[i] -= minFitness;
            sumFitness += allFitness[i];
        }

        int indexToReturn = population.size();
        double prob = rand.nextDouble() * sumFitness;
        for(int i = 0; i < allFitness.length; i++){
            prob -= allFitness[i];
            if(prob <= 0){
                indexToReturn = i;
                break;
            }
        }
        if(indexToReturn == population.size()){
            indexToReturn = population.size() - 1;
        }
        return population.get(indexToReturn);
    }
}
