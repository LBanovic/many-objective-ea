package hr.fer.zemris.zavrsni.algorithms.providers;

import hr.fer.zemris.zavrsni.algorithms.AbstractMOOPAlgorithm;
import hr.fer.zemris.zavrsni.solution.Solution;

import java.util.List;
import java.util.Map;

public class RankProvider implements ValueProvider<Integer> {

    private AbstractMOOPAlgorithm moop;

    public RankProvider(AbstractMOOPAlgorithm moop) {
        this.moop = moop;
    }

    @Override public void provide(Map<Solution, Integer> ranks) {
        List<List<Solution>> fronts = moop.paretoFronts();
        ranks.clear();
        int i = 0;
        for(List<Solution> l : fronts){
            for(Solution s: l){
                ranks.put(s, i);
            }
            i++;
        }
    }
}
