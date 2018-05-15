package hr.fer.zemris.zavrsni.experiments;

import hr.fer.zemris.zavrsni.algorithms.AbstractMOOPAlgorithm;
import hr.fer.zemris.zavrsni.algorithms.NSGA2;
import hr.fer.zemris.zavrsni.algorithms.StandardExperimentInitializer;
import hr.fer.zemris.zavrsni.algorithms.moead.MOEAD_TCH;
import hr.fer.zemris.zavrsni.evaluator.MOOPProblem;
import hr.fer.zemris.zavrsni.solution.*;

import java.util.List;

public class MOEAD_TCHExperiment extends Experiment<Solution>{
    @Override
    protected AbstractMOOPAlgorithm<Solution> getAlgorithm(MOOPProblem problem, List<Solution> population, String... params) {
        StandardExperimentInitializer<Solution> init = new StandardExperimentInitializer<>(problem, population,
                new RegularSolutionFactory());
        return new MOEAD_TCH(init.getPopulation(), init.getProblem(), Integer.parseInt(params[0]),
                Integer.parseInt(params[1]),
                init.getMutation(),
                init.getCrossover(),
                init.getMaxGen());
    }
}
