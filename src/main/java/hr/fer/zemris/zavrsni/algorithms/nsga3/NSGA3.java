package hr.fer.zemris.zavrsni.algorithms.nsga3;

import hr.fer.zemris.zavrsni.algorithms.AbstractMOOPAlgorithm;
import hr.fer.zemris.zavrsni.algorithms.MOOPUtils;
import hr.fer.zemris.zavrsni.algorithms.operators.Crossover;
import hr.fer.zemris.zavrsni.algorithms.operators.Mutation;
import hr.fer.zemris.zavrsni.algorithms.operators.Selection;
import hr.fer.zemris.zavrsni.algorithms.operators.selection.RandomSelection;
import hr.fer.zemris.zavrsni.algorithms.operators.selection.RouletteWheelSelection;
import hr.fer.zemris.zavrsni.algorithms.operators.selection.TournamentSelection;
import hr.fer.zemris.zavrsni.algorithms.providers.RankProvider;
import hr.fer.zemris.zavrsni.evaluator.MOOPProblem;
import hr.fer.zemris.zavrsni.solution.Solution;

import java.util.*;

import static hr.fer.zemris.zavrsni.algorithms.MOOPUtils.mergePopulations;

public class NSGA3 extends AbstractMOOPAlgorithm {

    private Random rand = new Random();

    //OPERATORS
    private final Selection selection;
    private final Crossover crossover;
    private final Mutation mutation;

    /*PARAMETERS*/
    private final int maxGen;
    private final boolean allowRepetition;
    private final int numberOfDivisions;

    public NSGA3(Solution[] population, MOOPProblem problem, Crossover crossover, Mutation mutation,
                 int maxGen, boolean allowRepetition, int numberOfDivisions) {
        super(population, problem);
        this.crossover = crossover;
        this.mutation = mutation;
        this.maxGen = maxGen;
        this.allowRepetition = allowRepetition;
        this.numberOfDivisions = numberOfDivisions;
        selection = new RandomSelection();
    }

    @Override
    public void run() {
        int gen = 0;
        Solution[] childPopulation = null;
        int numberOfObjectives = problem.getNumberOfObjectives();
        final int numberOfRefPoints = NSGA3Util.getNumberOfReferencePoints(numberOfObjectives, numberOfDivisions);
        List<NSGA3Util.ReferencePoint> points = new ArrayList<>(numberOfRefPoints);
        NSGA3Util.ReferencePoint start = new NSGA3Util.ReferencePoint(new double[numberOfObjectives]);
        Map<Solution, NSGA3Util.ReferencePoint> association = new HashMap<>();
        while (true) {
            System.out.println(gen);

            association.clear();
            NSGA3Util.getReferencePoints(points, start, numberOfObjectives, numberOfDivisions, numberOfDivisions, 0);

            if (gen >= maxGen) break;
            if (gen == 0) {
                MOOPUtils.evaluatePopulation(population, problem);
                MOOPUtils.nonDominatedSorting(population, fronts);
                childPopulation = MOOPUtils.createNewPopulation(population, selection, crossover, mutation, allowRepetition);
            } else {
                Solution[] combined = mergePopulations(population, childPopulation);
                Solution[] newPopulation = new Solution[population.length];
                MOOPUtils.evaluatePopulation(combined, problem);
                MOOPUtils.nonDominatedSorting(combined, fronts);
                int i;
                int currentIndex = 0;
                for (i = 0; i < fronts.size(); i++) {
                    List<Solution> l = fronts.get(i);
                    if (currentIndex + l.size() < population.length) {
                        for (Solution s : l) newPopulation[currentIndex++] = s;
                    } else break;
                }

                if(currentIndex == newPopulation.length) continue;

                //ako nova populacija nije popunjena
                List<Solution> currentFront = fronts.get(i);
                List<Solution> St = new ArrayList<>(currentIndex + currentFront.size());
                for(int j = 0; j < currentIndex; j++) St.add(newPopulation[j]);
                St.addAll(currentFront);
                normalize(St);
                double[] distances = new double[St.size()];
                associate(St, points, distances, association);
                int[] niches = nicheCount(association, points);
                niching(population.length - currentIndex, niches, association, distances,
                        points, currentFront, newPopulation, currentIndex, St);

                childPopulation = MOOPUtils.createNewPopulation(population, selection, crossover, mutation, allowRepetition);
            }
            gen++;
        }
    }

    private void normalize(List<Solution> St) {
        NSGA3Util.translateObjectives(St, problem.getNumberOfObjectives());
        Solution[] extremes = NSGA3Util.extremePoints(St, problem.getNumberOfObjectives());
        double[] hyperplane = NSGA3Util.constructHyperplane(extremes);
        for(int i = 0; i < problem.getNumberOfObjectives(); i++){
            for (Solution sol: St) {
                double[] objectives = sol.getObjectives();
                objectives[i] = objectives[i] / NSGA3Util.getIntercept(hyperplane, i);
            }
        }
    }

    private void associate(List<Solution> St, List<NSGA3Util.ReferencePoint> referencePoints, double[] distances,
                           Map<Solution, NSGA3Util.ReferencePoint> association){
        for(Solution sol : St){
            double minDistance = Double.MAX_VALUE;
            int index = 0;
            for(int i = 0; i < referencePoints.size(); i++) {
                double distance = NSGA3Util.perpendicularDistance(sol, referencePoints.get(i));
                if (minDistance > distance){
                    minDistance = distance;
                    index = i;
                }
            }
            association.put(sol, referencePoints.get(index));
            distances[St.indexOf(sol)] = minDistance;
        }
    }

    private int[] nicheCount(Map<Solution, NSGA3Util.ReferencePoint> association, List<NSGA3Util.ReferencePoint> referencePoints){
        int[] count = new int[referencePoints.size()];
        for(int i = 0; i < referencePoints.size(); i++){
            for(Map.Entry<Solution, NSGA3Util.ReferencePoint> entry : association.entrySet()){
                if(entry.getValue() == referencePoints.get(i)) count[i]++;
            }
        }
        return count;
    }

    private void niching(int numberToAdd, int[] nicheCount, Map<Solution, NSGA3Util.ReferencePoint> association,
                         double[] distances,
                         List<NSGA3Util.ReferencePoint> points, List<Solution> currentFront,
                         Solution[] newPopulation, int currentIndex, List<Solution> St){
        int k = 0;
        while (k < numberToAdd){
            List<NSGA3Util.ReferencePoint> Jmin = new LinkedList<>();
            int currentMin = Integer.MAX_VALUE;
            for(int i = 0; i < nicheCount.length; i++){
                if(currentMin > nicheCount[i]){
                    currentMin = nicheCount[i];
                }
            }
            for(int i = 0; i < nicheCount.length; i++){
                if(nicheCount[i] == currentMin) Jmin.add(points.get(i));
            }
            NSGA3Util.ReferencePoint ref = Jmin.get(rand.nextInt(Jmin.size()));
            List<Solution> I = new LinkedList<>();
            for(Map.Entry<Solution, NSGA3Util.ReferencePoint> entry : association.entrySet()){
                if(entry.getValue().equals(ref) && currentFront.contains(entry.getKey()))
                    I.add(entry.getKey());
            }
            if(I.size() != 0){
                if(nicheCount[points.indexOf(ref)] == 0){
                    int minIndex = 0;
                    for(int i = 0; i < I.size(); i++){
                        if(distances[St.indexOf(I.get(minIndex))] > distances[St.indexOf(I.get(i))]){
                            minIndex = i;
                        }
                    }
                    newPopulation[currentIndex + k] = I.get(minIndex);
                }
                else{
                    newPopulation[currentIndex + k] = I.get(rand.nextInt(I.size()));
                }
                nicheCount[points.indexOf(ref)] += 1;
                currentFront.remove(newPopulation[currentIndex + k]);
                k++;
            }else{
                int[] newNicheCount = new int[nicheCount.length - 1];
                for(int i = 0; i < newNicheCount.length; i++){
                    if(points.indexOf(ref) <= i) newNicheCount[i] = nicheCount[i + 1];
                    else newNicheCount[i] = nicheCount[i];
                }
                nicheCount = newNicheCount;
                points.remove(ref);
            }
        }
    }
}