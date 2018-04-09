package hr.fer.zemris.zavrsni.algorithms.providers;

import hr.fer.zemris.zavrsni.algorithms.AbstractMOOPAlgorithm;
import hr.fer.zemris.zavrsni.solution.Solution;

import java.util.*;

public class CrowdProvider implements ValueProvider<Double> {

    private AbstractMOOPAlgorithm moop;

    public CrowdProvider(AbstractMOOPAlgorithm moop){
        this.moop = moop;
    }

    @Override public void provide(Map<Solution, Double> crowd) {
        crowd.clear();
        List<List<Solution>> fronts = moop.paretoFronts();
        for(List<Solution> l : fronts){
            Integer[][] objectivesSorted = new Integer[l.get(0).getObjectives().length][l.size()];
            for(int i = 0; i < objectivesSorted.length; i++){
                final int k = i;
                for(int j = 0; j < objectivesSorted[0].length; j++) objectivesSorted[i][j] = j;
                Integer[] help = Arrays.copyOf(objectivesSorted[i], objectivesSorted[i].length);
                Arrays.sort(objectivesSorted[i], Comparator.comparingDouble(o -> l.get(help[o]).getObjectives()[k]));
                Solution bottom = l.get(objectivesSorted[i][0]);
                Solution top = l.get(objectivesSorted[i][objectivesSorted[0].length - 1]);

                crowd.put(top, Double.POSITIVE_INFINITY);
                crowd.put(bottom, Double.POSITIVE_INFINITY);
                for(int j = 1; j < objectivesSorted[0].length - 1; j++){
                    Solution currentSol = l.get(objectivesSorted[i][j]);
                    double currentFitness = (crowd.get(currentSol)) == null ? 0 :
                                            crowd.get(currentSol);
                    currentFitness += (l.get(objectivesSorted[i][j + 1]).getObjectives()[i] -
                                       l.get(objectivesSorted[i][j - 1]).getObjectives()[i]) /
                                      (top.getObjectives()[i] - bottom.getObjectives()[i]);
                    crowd.put(currentSol, currentFitness);
                }
            }
        }
    }
}
