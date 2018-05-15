package hr.fer.zemris.zavrsni.algorithms;

import hr.fer.zemris.zavrsni.algorithms.operators.Crossover;
import hr.fer.zemris.zavrsni.algorithms.operators.Mutation;
import hr.fer.zemris.zavrsni.algorithms.operators.Selection;
import hr.fer.zemris.zavrsni.algorithms.operators.selection.RouletteWheelSelection;
import hr.fer.zemris.zavrsni.algorithms.providers.DummyFitnessProvider;
import hr.fer.zemris.zavrsni.algorithms.providers.ValueProvider;
import hr.fer.zemris.zavrsni.evaluator.MOOPProblem;
import hr.fer.zemris.zavrsni.solution.FitnessSolution;

import java.util.LinkedList;
import java.util.List;

public class NSGA extends AbstractMOOPAlgorithm<FitnessSolution<Double>> {

    /*PARAMETERS*/
    private boolean allowRepetition;

    private ValueProvider<Double> provider;

    private List<List<FitnessSolution<Double>>> fronts;
    public NSGA(
            List<FitnessSolution<Double>> population,
            MOOPProblem problem,
            Crossover<FitnessSolution<Double>> crossover,
            Mutation mutation,
            int maxGen,
            boolean allowRepetition,
            double epsilon,
            double sigmaShare,
            double alpha
    ) {
        super(population, problem, maxGen, crossover, mutation);
        this.selection = new RouletteWheelSelection();
        this.allowRepetition = allowRepetition;
        provider = new DummyFitnessProvider(problem.getLowerBounds(), problem.getUpperBounds(),
                                                                   epsilon, sigmaShare, alpha, this);
        fronts = new LinkedList<>();
    }

    @Override public void run() {
        int gen = 0;

        while (true) {
            PopulationUtils.evaluatePopulation(population, problem);
            MOOPUtils.nonDominatedSorting(population, fronts);
            System.out.println(gen);
            if (gen >= maxGen) {
                break;
            }
            provider.provide(population);
            population = PopulationUtils.createNewPopulation(population, selection, crossover, mutation, allowRepetition);
            gen++;
        }
    }

    @Override
    public List<FitnessSolution<Double>> getNondominatedSolutions() {
        return fronts.get(0);
    }

    @Override
    public List<List<FitnessSolution<Double>>> getParetoFronts() {
        return fronts;
    }
}
