package hr.fer.zemris.zavrsni.algorithms;

import hr.fer.zemris.zavrsni.algorithms.operators.Crossover;
import hr.fer.zemris.zavrsni.algorithms.operators.Mutation;
import hr.fer.zemris.zavrsni.algorithms.operators.Selection;
import hr.fer.zemris.zavrsni.algorithms.providers.DummyFitnessProvider;
import hr.fer.zemris.zavrsni.evaluator.MOOPProblem;
import hr.fer.zemris.zavrsni.solution.Solution;

import java.util.LinkedList;
import java.util.List;

public class NSGA extends AbstractMOOPAlgorithm implements FitnessObservable{

    /*OPERATORS*/
    private Crossover crossover;
    private Selection selection;
    private Mutation  mutation;

    /*PARAMETERS*/
    private int     maxGen;
    private boolean allowRepetition;

    /*OBSERVERS*/
    private List<FitnessObserver> observers;

    private List<List<Solution>> fronts;

    public NSGA(
        Solution[] population,
        MOOPProblem problem,
        Crossover crossover,
        Selection selection,
        Mutation mutation,
        int maxGen,
        boolean allowRepetition,
        double epsilon,
        double sigmaShare,
        double alpha
    ) {
        super(population, problem);
        this.crossover = crossover;
        this.selection = selection;
        this.mutation = mutation;
        this.maxGen = maxGen;
        this.allowRepetition = allowRepetition;

        observers = new LinkedList<>();

        selection.initializeValueProviders(new DummyFitnessProvider(problem.getLowerBounds(), problem.getUpperBounds(),
                                                                   epsilon, sigmaShare, alpha, this));
        this.fronts = new LinkedList<>();
    }

    @Override public void run() {
        int gen = 0;

        while (true) {
            MOOPUtils.evaluatePopulation(population, problem);
            MOOPUtils.nonDominatedSorting(population, fronts);
            fitnessChanged();
            System.out.println(gen);
            if (gen >= maxGen) {
                break;
            }

            population = MOOPUtils.createNewPopulation(population, selection, crossover, mutation, allowRepetition);
            gen++;
        }
    }

    @Override
    public void attachObserver(FitnessObserver o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(FitnessObserver o) {
        observers.remove(o);
    }

    @Override
    public void fitnessChanged() {
        for(FitnessObserver o : observers){
            o.onFitnessChanged();
        }
    }

    @Override
    public List<Solution> getNondominatedSolutions() {
        return fronts.get(0);
    }
}
