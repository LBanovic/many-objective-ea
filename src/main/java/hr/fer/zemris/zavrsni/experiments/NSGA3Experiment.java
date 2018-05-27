package hr.fer.zemris.zavrsni.experiments;

import hr.fer.zemris.zavrsni.algorithms.AbstractMOOPAlgorithm;
import hr.fer.zemris.zavrsni.algorithms.StandardExperimentInitializer;
import hr.fer.zemris.zavrsni.algorithms.nsga3.NSGA3;
import hr.fer.zemris.zavrsni.evaluator.MOOPProblem;
import hr.fer.zemris.zavrsni.solution.RegularSolutionFactory;
import hr.fer.zemris.zavrsni.solution.Solution;

import java.util.ArrayList;
import java.util.List;

public class NSGA3Experiment extends Experiment<Solution>{
    @Override
    protected AbstractMOOPAlgorithm<Solution> getAlgorithm(MOOPProblem problem, List<Solution> population,
                                                           int maxGen,
                                                           String... params) {
        StandardExperimentInitializer<Solution> init = new StandardExperimentInitializer<>(problem, population,
                new RegularSolutionFactory(), maxGen);
        List<Integer> divisions = new ArrayList<>();
        for(int i = 0; i < params.length - 1; i++){
            divisions.add(Integer.parseInt(params[i]));
        }
        init.setEta(30);
        return new NSGA3(init.getPopulation(), init.getProblem(), init.getCrossover(),
                init.getMutation(), init.getMaxGen(), Boolean.parseBoolean(params[params.length - 1]), divisions);
    }
}
