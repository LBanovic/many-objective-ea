package hr.fer.zemris.zavrsni.algorithms;

import hr.fer.zemris.zavrsni.algorithms.operators.Crossover;
import hr.fer.zemris.zavrsni.algorithms.operators.Mutation;
import hr.fer.zemris.zavrsni.algorithms.operators.selection.CrowdedTournamentSelection;
import hr.fer.zemris.zavrsni.algorithms.operators.selection.TournamentSelection;
import hr.fer.zemris.zavrsni.algorithms.providers.CrowdProvider;
import hr.fer.zemris.zavrsni.algorithms.providers.RankProvider;
import hr.fer.zemris.zavrsni.algorithms.providers.ValueProvider;
import hr.fer.zemris.zavrsni.evaluator.MOOPProblem;
import hr.fer.zemris.zavrsni.solution.FitnessSolution;
import hr.fer.zemris.zavrsni.solution.Solution;

import java.util.*;

import static hr.fer.zemris.zavrsni.algorithms.PopulationUtils.mergePopulations;

public class NSGA2 extends AbstractMOOPAlgorithm<FitnessSolution<Double>> {

    /*OPERATORS*/
    private CrowdedTournamentSelection selection;

    private ValueProvider<Double> rankProvider;
    private ValueProvider<Double> crowdProvider;

    /*PARAMETERS*/
    private boolean allowRepetition;

    private List<List<FitnessSolution<Double>>> fronts;

    public NSGA2(
            List<FitnessSolution<Double>> population,
            MOOPProblem problem,
            Crossover<FitnessSolution<Double>> crossover,
            Mutation mutation,
            int tournamentSize,
            int maxGen,
            boolean allowRepetition
    ) {
        super(population, problem, maxGen, crossover, mutation);
        this.allowRepetition = allowRepetition;
        crowdProvider = new CrowdProvider(this);
        rankProvider = new RankProvider<>( this);
        selection = new CrowdedTournamentSelection(this, tournamentSize);
        fronts = new LinkedList<>();
    }

    @Override
    public void run() {
        int gen = 0;
        TournamentSelection<Double> binary = new TournamentSelection<>(2, true);

        List<FitnessSolution<Double>> childPopulation = null;
        while (true) {
            System.out.println(gen);
            if(gen >= maxGen) break;
            else if (gen == 0) {
                PopulationUtils.evaluatePopulation(population, problem);
                MOOPUtils.nonDominatedSorting(population, fronts);
                rankProvider.provide(population);
                childPopulation = PopulationUtils.createNewPopulation(population, binary, crossover, mutation, allowRepetition);
            } else {
                List<FitnessSolution<Double>> combined = mergePopulations(population, childPopulation);
                List<FitnessSolution<Double>> newPopulation = new ArrayList<>(population.size());
                PopulationUtils.evaluatePopulation(combined, problem);
                MOOPUtils.nonDominatedSorting(combined, fronts);
                int i;
                for (i = 0; i < fronts.size(); i++) {
                    List<FitnessSolution<Double>> l = fronts.get(i);
                    if (newPopulation.size() + l.size() < population.size()) {
                        newPopulation.addAll(l);
                    } else break;
                }
                crowdProvider.provide(population);
                fronts.get(i).sort(Comparator.reverseOrder());
                int size = newPopulation.size();
                for (int j = size; j < population.size(); j++) {
                    newPopulation.add(fronts.get(i).get(j - size));
                }
                population = newPopulation;
                childPopulation = PopulationUtils.createNewPopulation(population, selection, crossover, mutation, allowRepetition);
            }
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
