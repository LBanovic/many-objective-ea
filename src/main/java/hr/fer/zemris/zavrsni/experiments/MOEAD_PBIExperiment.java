package hr.fer.zemris.zavrsni.experiments;

import hr.fer.zemris.zavrsni.algorithms.AbstractMOOPAlgorithm;
import hr.fer.zemris.zavrsni.algorithms.StandardExperimentInitializer;
import hr.fer.zemris.zavrsni.algorithms.moead.MOEAD_PBI;
import hr.fer.zemris.zavrsni.evaluator.MOOPProblem;
import hr.fer.zemris.zavrsni.solution.RegularSolutionFactory;
import hr.fer.zemris.zavrsni.solution.Solution;

import java.util.Arrays;
import java.util.List;

public class MOEAD_PBIExperiment extends Experiment<Solution>{

    @Override
    protected AbstractMOOPAlgorithm<Solution> getAlgorithm(MOOPProblem problem, List<Solution> population, String... params) {
        StandardExperimentInitializer<Solution> init = new StandardExperimentInitializer<>(problem, population,
                new RegularSolutionFactory());
        return new MOEAD_PBI(init.getPopulation(), init.getProblem(), Integer.parseInt(params[0]), Integer.parseInt(params[1]),
                init.getMutation(),
                init.getCrossover(),
                init.getMaxGen(), Double.parseDouble(params[2]));
    }
}
