package hr.fer.zemris.zavrsni.algorithms;

import hr.fer.zemris.zavrsni.algorithms.operators.Crossover;
import hr.fer.zemris.zavrsni.algorithms.operators.Mutation;
import hr.fer.zemris.zavrsni.algorithms.operators.selection.TournamentSelection;
import hr.fer.zemris.zavrsni.algorithms.providers.SPEAFitnessProvider;
import hr.fer.zemris.zavrsni.evaluator.MOOPProblem;
import hr.fer.zemris.zavrsni.solution.FitnessSolution;

import java.util.LinkedList;
import java.util.List;

public class SPEA2 extends AbstractMOOPAlgorithm<FitnessSolution<Double>>{

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
    @SuppressWarnings("unchecked")
    public void run() {
        archive = new LinkedList<>();
        int gen = 0;
        while(true){
            speaFitnessProvider.provide(population);
            for(FitnessSolution<Double> s : population){
                if(s.getFitness() < 1) archive.add(s);
            }
            if(archive.size() > archiveSize){
                //TODO truncation operator
            }else if(archive.size() < archiveSize){
                //TODO fill the rest with best solutions
            }

            if(gen > maxGen) break;

            population = MOOPUtils.createNewPopulation(archive, selection,
                    crossover, mutation, allowRepetition);
            gen++;
        }
    }

    @Override
    public List<FitnessSolution<Double>> getNondominatedSolutions() {
        return archive;
    }
}
