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
            MOOPUtils.evaluatePopulation(population, problem);
            List<FitnessSolution<Double>> combined = MOOPUtils.mergePopulations(population, archive);
            archive.clear();
            speaFitnessProvider.provide(combined);
            for (FitnessSolution<Double> s : combined) {
                if (s.getFitness() < 1) archive.add(s);
            }
            if (archive.size() > archiveSize) {
                while (archive.size() > archiveSize) {
                    FitnessSolution<Double> solution = getLeastDistanceSolution(archive);
                    archive.remove(solution);
                }

            } else if (archive.size() < archiveSize) {
                Collections.sort(combined);
                for (FitnessSolution<Double> f : combined) {
                    if (f.getFitness() > 1) archive.add(f);
                    if (archive.size() == archiveSize) break;
                }
            }

            gen++;
            if (gen > maxGen) break;

            population = MOOPUtils.createNewPopulation(archive, selection,
                    crossover, mutation, allowRepetition);
        }
    }

    @SuppressWarnings("unchecked")
    private FitnessSolution<Double> getLeastDistanceSolution(List<FitnessSolution<Double>> archive) {
        List<Double>[] distances = (List<Double>[])new List[archive.size()];

        for(int i = 0; i < archive.size(); i++){
            distances[i] = new ArrayList<>(archive.size() - 1);
            FitnessSolution<Double> f = archive.get(i);
            for(FitnessSolution<Double> s : archive){
                if(f != s){
                    distances[i].add(MOOPUtils.calculateDistance(s, f));
                }
            }
            Collections.sort(distances[i]);
        }

        Set<Integer> considered = new HashSet<>();
        for(int i = 0; i < archive.size(); i++) considered.add(i);
        for(int currentIndex = 0; currentIndex < distances[0].size() && considered.size() > 1; currentIndex++) {
            double min = Double.MAX_VALUE;
            for(int i = 0; i < distances.length; i++) {
                try {
                    if (considered.contains(i)) min = Math.min(min, distances[i].get(currentIndex));
                }catch (Exception e){
                    for(List<Double> d : distances) System.out.println(d.size());
                    System.exit(0);
                }
            }
            for (int i = 0; i < distances.length; i++) {
                if(considered.contains(i) && Math.abs(distances[i].get(currentIndex) - min) > MOOPUtils.EPSILON)
                    considered.remove(i);
            }
        }

        int index = considered.toArray(new Integer[1])[0];
        return archive.get(index);
    }

    @Override
    public List<FitnessSolution<Double>> getNondominatedSolutions() {
        return archive;
    }
}
