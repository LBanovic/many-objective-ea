package hr.fer.zemris.zavrsni.algorithms;

import hr.fer.zemris.zavrsni.algorithms.operators.Crossover;
import hr.fer.zemris.zavrsni.algorithms.operators.Mutation;
import hr.fer.zemris.zavrsni.algorithms.operators.Selection;
import hr.fer.zemris.zavrsni.algorithms.operators.selection.CrowdedTournamentSelection;
import hr.fer.zemris.zavrsni.algorithms.operators.selection.TournamentSelection;
import hr.fer.zemris.zavrsni.algorithms.providers.CrowdProvider;
import hr.fer.zemris.zavrsni.algorithms.providers.RankProvider;
import hr.fer.zemris.zavrsni.algorithms.providers.ValueProvider;
import hr.fer.zemris.zavrsni.evaluator.MOOPProblem;
import hr.fer.zemris.zavrsni.solution.Solution;

import java.util.*;

import static hr.fer.zemris.zavrsni.algorithms.MOOPUtils.mergePopulations;

public class NSGA2 extends AbstractMOOPAlgorithm {

    /*OPERATORS*/
    private Crossover crossover;
    private Selection selection;
    private Mutation mutation;

    private ValueProvider<Integer> rankProvider;
    private ValueProvider<Double> crowdProvider;

    private Map<Solution, Double> crowd;

    /*PARAMETERS*/
    private int maxGen;
    private boolean allowRepetition;

    public NSGA2(
            Solution[] population,
            MOOPProblem problem,
            Crossover crossover,
            Mutation mutation,
            int tournamentSize,
            int maxGen,
            boolean allowRepetition
    ) {
        super(population, problem);
        this.crossover = crossover;
        this.mutation = mutation;
        this.maxGen = maxGen;
        this.allowRepetition = allowRepetition;

        rankProvider = new RankProvider(this);
        crowdProvider = new CrowdProvider(this);
        crowd = new HashMap<>();
        selection = new CrowdedTournamentSelection(crowd, tournamentSize);
        selection.initializeValueProviders(rankProvider);
    }

    @Override
    public void run() {
        int gen = 0;
        Selection binary = new TournamentSelection<Integer>(2, true);
        binary.initializeValueProviders(rankProvider);
        Solution[] childPopulation = null;
        while (true) {
            System.out.println(gen);
            if (gen >= maxGen) break;
            if (gen == 0) {
                MOOPUtils.evaluatePopulation(population, problem);
                MOOPUtils.nonDominatedSorting(population, fronts);
                childPopulation = MOOPUtils.createNewPopulation(population, binary, crossover, mutation, allowRepetition);
            } else {
                Solution[] combined = mergePopulations(population, childPopulation);
                Solution[] newPopulation = new Solution[population.length];
                MOOPUtils.evaluatePopulation(combined, problem);
                MOOPUtils.nonDominatedSorting(combined, fronts);
                crowdProvider.provide(crowd);
                int i;
                int currentIndex = 0;
                for (i = 0; i < fronts.size(); i++) {
                    List<Solution> l = fronts.get(i);
                    if (currentIndex + l.size() < population.length) {
                        for (Solution s : l) newPopulation[currentIndex++] = s;
                    } else break;
                }
                fronts.get(i).sort(Comparator.comparingDouble(o -> crowd.get(o)));
                for (int j = currentIndex; j < population.length; j++) {
                    newPopulation[j] = fronts.get(i).get(j - currentIndex);
                }
                population = newPopulation;
                childPopulation = MOOPUtils.createNewPopulation(population, selection, crossover, mutation, allowRepetition);
            }
            gen++;
        }
    }
}
