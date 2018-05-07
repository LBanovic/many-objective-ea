package hr.fer.zemris.zavrsni.algorithms.providers;

import hr.fer.zemris.zavrsni.algorithms.MOOPUtils;
import hr.fer.zemris.zavrsni.solution.Solution;

import java.util.*;

public class SPEAFitnessProvider implements ValueProvider<Double> {

    private Map<Solution, Double> fitnessMap;

    @Override
    public void provide(Map<Solution, Double> map, List<Solution> solutions) {
        fitnessMap = map;
        int k = (int) Math.sqrt(solutions.size());

        map.clear();

        Map<Solution, List<Solution>> dominators = new HashMap<>();

        //Strength value
        for(Solution s : solutions){
            double strength = 0;
            dominators.put(s, new LinkedList<>());
            for(Solution t : solutions){
                if(MOOPUtils.dominates(s, t)){
                    strength++;
                    dominators.get(s).add(t);
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
            for(int i = 0; i < solutions.size(); i++){
                if(n != i){
                    distances.add(calculateDistance(solutions.get(n), solutions.get(i)));
                }
            }
            Collections.sort(distances);
            Solution s = solutions.get(n);
            double density = 1. / (distances.get(k) + 2);
            double current = map.get(s);
            map.put(s, current + density);
        }
    }

    @Override
    public void provide(Map<Solution, Double> map) {
        map.clear();
        map.putAll(fitnessMap);
    }

    private double calculateDistance(Solution s, Solution t) {
        double sum = 0;
        double[] obj1 = s.getObjectives();
        double[] obj2 = t.getObjectives();
        for(int i = 0; i < obj1.length; i++){
            double dif = obj1[i] - obj2[i];
            sum += dif * dif;
        }
        return Math.sqrt(sum);
    }
}
