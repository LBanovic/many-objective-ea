package hr.fer.zemris.zavrsni.algorithms;

import hr.fer.zemris.zavrsni.algorithms.operators.Crossover;
import hr.fer.zemris.zavrsni.algorithms.operators.Mutation;
import hr.fer.zemris.zavrsni.algorithms.operators.selection.TournamentSelection;
import hr.fer.zemris.zavrsni.algorithms.providers.SPEAFitnessProvider;
import hr.fer.zemris.zavrsni.evaluator.MOOPProblem;
import hr.fer.zemris.zavrsni.solution.FitnessSolution;

import java.util.*;

public class SPEA2 extends AbstractMOOPAlgorithm<FitnessSolution<Double>> {

    private List<FitnessSolution<Double>> archive;

    private final int archiveSize;
    private SPEAFitnessProvider speaFitnessProvider;

    private boolean allowRepetition;

    public SPEA2(List<FitnessSolution<Double>> population, MOOPProblem problem, int maxGen, int archiveSize,
                 Crossover<FitnessSolution<Double>> crossover, Mutation mutation,
                 int tournamentSize, boolean allowRepetition) {
        super(population, problem, maxGen, crossover, mutation);
        this.archiveSize = archiveSize;
        this.allowRepetition = allowRepetition;
        selection = new TournamentSelection<>(tournamentSize, true);
        speaFitnessProvider = new SPEAFitnessProvider();
    }

    @Override
    public void run() {
        archive = new LinkedList<>();
        int gen = 0;
        while (true) {
            System.out.println("Generation: " + gen);
            PopulationUtils.evaluatePopulation(population, problem);
            List<FitnessSolution<Double>> combined = PopulationUtils.mergePopulations(population, archive);
            archive.clear();
            speaFitnessProvider.provide(combined);
            for (FitnessSolution<Double> s : combined) {
                if (s.getFitness() < 1) archive.add(s);
            }
            if (archive.size() > archiveSize) {
                MOOPUtils.removeExcessSolutions(archive, archiveSize);
            } else if (archive.size() < archiveSize) {
                Collections.sort(combined);
                for (FitnessSolution<Double> f : combined) {
                    if (f.getFitness() > 1) archive.add(f);
                    if (archive.size() == archiveSize) break;
                }
            }

            gen++;
            if (gen > maxGen) break;

            population = PopulationUtils.createNewPopulation(archive, selection,
                    crossover, mutation, allowRepetition);
        }
    }

    @Override
    public List<FitnessSolution<Double>> getNondominatedSolutions() {
        return archive;
    }
}
