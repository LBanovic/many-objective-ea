package hr.fer.zemris.zavrsni.algorithms.providers;

import hr.fer.zemris.zavrsni.algorithms.MOOPUtils;
import hr.fer.zemris.zavrsni.solution.FitnessSolution;
import hr.fer.zemris.zavrsni.solution.Solution;

import java.util.*;

import static hr.fer.zemris.zavrsni.algorithms.MOOPUtils.calculateDistance;

public class SPEAFitnessProvider implements ValueProvider<Double> {

    @Override
    public void provide(List<FitnessSolution<Double>> solutions) {
        int k = (int) Math.sqrt(solutions.size());

        Map<Solution, List<Solution>> dominators = new HashMap<>();
        Map<Solution, Double> map = new HashMap<>(solutions.size());

        for(FitnessSolution<Double> v : solutions) dominators.put(v, new LinkedList<>());
        //Strength value
        for(Solution s : solutions){
            double strength = 0;
            for(Solution t : solutions){
                if(MOOPUtils.dominates(s, t)){
                    strength++;
                    dominators.get(t).add(s);
                }
            }
            map.put(s, strength);
        }

        Map<Solution, Double> copy = new HashMap<>(map);

        //Raw fitness
        for(Solution s: solutions){
            double raw = 0;
            for(Solution r : dominators.get(s)){
                raw += copy.get(r);
            }
            map.put(s, raw);
        }

        for(int n = 0; n < solutions.size(); n++){
            List<Double> distances = new ArrayList<>(solutions.size() - 1);
            FitnessSolution<Double> s = solutions.get(n);
            for(int i = 0; i < solutions.size(); i++){
                if(n != i){
                    distances.add(calculateDistance(s, solutions.get(i)));
                }
            }
            Collections.sort(distances);
            double density = 1. / (distances.get(k) + 2);
            double current = map.get(s);
            s.setFitness(current + density);
        }
    }
}
