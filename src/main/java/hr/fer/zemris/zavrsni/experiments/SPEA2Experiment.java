package hr.fer.zemris.zavrsni.experiments;

import hr.fer.zemris.zavrsni.algorithms.AbstractMOOPAlgorithm;
import hr.fer.zemris.zavrsni.algorithms.SPEA2;
import hr.fer.zemris.zavrsni.algorithms.StandardExperimentInitializer;
import hr.fer.zemris.zavrsni.evaluator.MOOPProblem;
import hr.fer.zemris.zavrsni.solution.FitnessSolution;
import hr.fer.zemris.zavrsni.solution.FitnessSolutionFactory;
import hr.fer.zemris.zavrsni.solution.Solution;

import java.util.List;

public class SPEA2Experiment extends Experiment<FitnessSolution<Double>>{
    @Override
    protected AbstractMOOPAlgorithm<FitnessSolution<Double>> getAlgorithm(MOOPProblem problem, List<Solution> population, String... params) {
        StandardExperimentInitializer<FitnessSolution<Double>> init = new StandardExperimentInitializer<>(problem,
                FitnessSolution.<Double>encapsulateSolution(population), new FitnessSolutionFactory<>());
        return new SPEA2(init.getPopulation(), init.getProblem(), init.getMaxGen(), Integer.parseInt(params[0]),
                init.getCrossover(),
                init.getMutation(), Integer.parseInt(params[1]), Boolean.parseBoolean(params[2]));
    }
}
