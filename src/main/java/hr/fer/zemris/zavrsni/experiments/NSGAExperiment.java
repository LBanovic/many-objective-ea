package hr.fer.zemris.zavrsni.experiments;

import hr.fer.zemris.zavrsni.algorithms.AbstractMOOPAlgorithm;
import hr.fer.zemris.zavrsni.algorithms.NSGA;
import hr.fer.zemris.zavrsni.algorithms.StandardExperimentInitializer;
import hr.fer.zemris.zavrsni.evaluator.MOOPProblem;
import hr.fer.zemris.zavrsni.solution.FitnessSolution;
import hr.fer.zemris.zavrsni.solution.FitnessSolutionFactory;
import hr.fer.zemris.zavrsni.solution.Solution;

import java.util.List;

public class NSGAExperiment extends Experiment<FitnessSolution<Double>>{
    @Override
    protected AbstractMOOPAlgorithm<FitnessSolution<Double>> getAlgorithm(MOOPProblem problem,
                                                                          List<Solution> population,
                                                                          int maxGen,
                                                                          String... params) {
        StandardExperimentInitializer<FitnessSolution<Double>> init = new StandardExperimentInitializer<>(problem,
                FitnessSolution.<Double>encapsulateSolution(population), new FitnessSolutionFactory<>(), maxGen);
        return new NSGA(init.getPopulation(), init.getProblem(), init.getCrossover(),
                init.getMutation(), init.getMaxGen(), Boolean.parseBoolean(params[3]),
                Double.parseDouble(params[0]), Double.parseDouble(params[1]), Double.parseDouble(params[2]));
    }
}
