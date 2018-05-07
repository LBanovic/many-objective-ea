package hr.fer.zemris.zavrsni.algorithms;

import hr.fer.zemris.zavrsni.algorithms.operators.Crossover;
import hr.fer.zemris.zavrsni.algorithms.operators.Mutation;
import hr.fer.zemris.zavrsni.algorithms.operators.selection.TournamentSelection;
import hr.fer.zemris.zavrsni.algorithms.providers.SPEAFitnessProvider;
import hr.fer.zemris.zavrsni.evaluator.MOOPProblem;
import hr.fer.zemris.zavrsni.solution.Solution;

import java.util.*;

public class SPEA2 extends AbstractMOOPAlgorithm implements FitnessObservable{

    private List<Solution> archive;

    private List<FitnessObserver> observers;

    private final int archiveSize;
    private TournamentSelection<Double> tournamentSelection;
    private SPEAFitnessProvider speaFitnessProvider;

    private Map<Solution, Double> fitnessMap;

    private boolean allowRepetition;

    public SPEA2(Solution[] population, MOOPProblem problem, int maxGen, int archiveSize,
                 Crossover crossover, Mutation mutation, int tournamentSize, boolean allowRepetition) {
        super(population, problem, maxGen, crossover, mutation);
        this.archiveSize = archiveSize;
        this.allowRepetition = allowRepetition;
        this.observers = new LinkedList<>();
        selection = this.tournamentSelection = new TournamentSelection<>(tournamentSize, true);
        attachObserver(tournamentSelection);
        this.fitnessMap = new HashMap<>();
        speaFitnessProvider = new SPEAFitnessProvider();
    }

    @Override
    public void run() {
        archive = new LinkedList<>();
        int gen = 0;
        while(true){
            List<Solution> fitnessPopulation = Arrays.asList(population);
            fitnessPopulation.addAll(archive);
            archive.clear();
            speaFitnessProvider.provide(fitnessMap, fitnessPopulation);
            for(Solution s : fitnessPopulation){
                if(fitnessMap.get(s) < 1) archive.add(s);
            }
            if(archive.size() > archiveSize){
                //TODO truncation operator
            }else if(archive.size() < archiveSize){
                //TODO fill the rest with best solutions
            }

            if(gen > maxGen) break;

            population = MOOPUtils.createNewPopulation(archive.toArray(new Solution[1]), selection,
                    crossover, mutation, allowRepetition);
            gen++;
        }
    }

    @Override
    public List<Solution> getNondominatedSolutions() {
        return archive;
    }

    @Override
    public List<List<Solution>> getParetoFronts() {
        throw new UnsupportedOperationException("This algorithm does not employ non-dominated sorting!");
    }

    @Override
    public void attachObserver(FitnessObserver o) {
        this.observers.add(o);
    }

    @Override
    public void removeObserver(FitnessObserver o) {
        this.observers.remove(o);
    }

    @Override
    public void fitnessChanged() {
        for(FitnessObserver o : observers){
            o.onFitnessChanged();
        }
    }
}
