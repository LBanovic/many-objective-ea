package hr.fer.zemris.zavrsni.experiments;

import hr.fer.zemris.zavrsni.algorithms.AbstractMOOPAlgorithm;
import hr.fer.zemris.zavrsni.algorithms.NSGA2;
import hr.fer.zemris.zavrsni.algorithms.StandardExperimentInitializer;
import hr.fer.zemris.zavrsni.algorithms.moead.MOEAD_TCH;
import hr.fer.zemris.zavrsni.evaluator.MOOPProblem;
import hr.fer.zemris.zavrsni.solution.*;

import java.util.LinkedList;
import java.util.List;

public class MOEAD_TCHExperiment extends Experiment<Solution>{
    @Override
    protected AbstractMOOPAlgorithm<Solution> getAlgorithm(MOOPProblem problem, List<Solution> population,
                                                           int maxGen,
                                                           String... params) {
        StandardExperimentInitializer<Solution> init = new StandardExperimentInitializer<>(problem, population,
                new RegularSolutionFactory(), maxGen);
        List<Integer> l = new LinkedList<>();
        for(int i = 1; i < params.length; i++){
            l.add(Integer.parseInt(params[i]));
        }
        return new MOEAD_TCH(init.getPopulation(), init.getProblem(), Integer.parseInt(params[0]),
                l,
                init.getMutation(),
                init.getCrossover(),
                init.getMaxGen());
    }
}
