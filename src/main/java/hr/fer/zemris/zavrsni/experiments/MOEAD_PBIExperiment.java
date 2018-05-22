package hr.fer.zemris.zavrsni.experiments;

import hr.fer.zemris.zavrsni.algorithms.AbstractMOOPAlgorithm;
import hr.fer.zemris.zavrsni.algorithms.StandardExperimentInitializer;
import hr.fer.zemris.zavrsni.algorithms.moead.MOEAD_PBI;
import hr.fer.zemris.zavrsni.evaluator.MOOPProblem;
import hr.fer.zemris.zavrsni.solution.RegularSolutionFactory;
import hr.fer.zemris.zavrsni.solution.Solution;

import java.util.LinkedList;
import java.util.List;

public class MOEAD_PBIExperiment extends Experiment<Solution>{

    @Override
    protected AbstractMOOPAlgorithm<Solution> getAlgorithm(MOOPProblem problem, List<Solution> population,
                                                           int maxGen,
                                                           String... params) {
        StandardExperimentInitializer<Solution> init = new StandardExperimentInitializer<>(problem, population,
                new RegularSolutionFactory(), maxGen);
        List<Integer> l = new LinkedList<>();
        for(int i = 1; i < params.length - 1; i++){
            l.add(Integer.parseInt(params[i]));
        }
        return new MOEAD_PBI(init.getPopulation(), init.getProblem(), Integer.parseInt(params[0]), l,
                init.getMutation(),
                init.getCrossover(),
                init.getMaxGen(), Double.parseDouble(params[params.length - 1]));
    }
}
