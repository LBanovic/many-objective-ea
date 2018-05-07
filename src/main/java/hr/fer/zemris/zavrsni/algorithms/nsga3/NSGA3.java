package hr.fer.zemris.zavrsni.algorithms.nsga3;

import hr.fer.zemris.zavrsni.algorithms.AbstractMOOPAlgorithm;
import hr.fer.zemris.zavrsni.algorithms.MOOPUtils;
import hr.fer.zemris.zavrsni.algorithms.operators.Crossover;
import hr.fer.zemris.zavrsni.algorithms.operators.Mutation;
import hr.fer.zemris.zavrsni.algorithms.operators.selection.RandomSelection;
import hr.fer.zemris.zavrsni.evaluator.MOOPProblem;
import hr.fer.zemris.zavrsni.solution.Solution;

import java.util.*;

import static hr.fer.zemris.zavrsni.algorithms.MOOPUtils.mergePopulations;

//TODO proradilo, ali crasha u nekim izoliranim slucajevima => sto??
public class NSGA3 extends AbstractMOOPAlgorithm{

    private Random rand = new Random();

    /*PARAMETERS*/
    private final boolean allowRepetition;
    private final int numberOfDivisions;

    public NSGA3(Solution[] population, MOOPProblem problem, Crossover crossover, Mutation mutation,
                 int maxGen, boolean allowRepetition, int numberOfDivisions) {
        super(population, problem, maxGen, crossover, mutation);
        this.allowRepetition = allowRepetition;
        this.numberOfDivisions = numberOfDivisions;
        selection = new RandomSelection();
    }

    @Override
    public void run() {
        int gen = 0;
        Solution[] childPopulation;
        int numberOfObjectives = problem.getNumberOfObjectives();
        final int numberOfRefPoints = NSGA3Util.getNumberOfReferencePoints(numberOfObjectives, numberOfDivisions);
        List<NSGA3Util.ReferencePoint> points = new ArrayList<>(numberOfRefPoints);
        NSGA3Util.ReferencePoint start = new NSGA3Util.ReferencePoint(new double[numberOfObjectives]);
        while (true) {
            System.out.println("Generation: " + gen);

            if (gen >= maxGen){
                MOOPUtils.evaluatePopulation(population, problem);
                MOOPUtils.nonDominatedSorting(population, fronts);
                break;
            }

            childPopulation = MOOPUtils.createNewPopulation(population, selection, crossover, mutation, allowRepetition);

            Solution[] combined = mergePopulations(population, childPopulation);
            Solution[] newPopulation = new Solution[population.length];

            MOOPUtils.evaluatePopulation(combined, problem);
            MOOPUtils.nonDominatedSorting(combined, fronts);

            int i;
            int currentIndex = 0;
            for (i = 0; i < fronts.size(); i++) {
                List<Solution> l = fronts.get(i);
                if (currentIndex + l.size() <= population.length) {
                    for (Solution s : l) newPopulation[currentIndex++] = s;
                } else break;
            }

            if (currentIndex != newPopulation.length) {
                //ako nova populacija nije popunjena

                List<Solution> currentFront = fronts.get(i);
                List<Solution> St = new ArrayList<>(currentIndex + currentFront.size());
                St.addAll(Arrays.asList(newPopulation).subList(0, currentIndex));
                St.addAll(currentFront);

                normalize(St);
                points.clear();
                NSGA3Util.getReferencePoints(points, start, numberOfObjectives, numberOfDivisions, numberOfDivisions, 0);

                associate(St, points, currentFront);

                niching(population.length - currentIndex,
                        points, currentFront, newPopulation, currentIndex);
            }
            population = newPopulation;
            gen++;
        }

    }

    private void normalize(List<Solution> St) {
        double[] idealPoint = NSGA3Util.findIdealPoint(fronts.get(0), problem.getNumberOfObjectives());
        for(Solution sol : St) {
            NSGA3Util.subtractIdealPoint(sol.getObjectives(), idealPoint);
        }
        Solution[] extremes = NSGA3Util.extremePoints(fronts.get(0), problem.getNumberOfObjectives());
        double[] hyperplane = NSGA3Util.constructHyperplane(extremes, St, idealPoint);
        for (int i = 0; i < problem.getNumberOfObjectives(); i++) {
            double intercept = NSGA3Util.getIntercept(hyperplane, i);
            for (Solution sol : St) {
                double[] objectives = sol.getObjectives();
                objectives[i] /= intercept;
            }
        }
    }

    private void associate(List<Solution> St, List<NSGA3Util.ReferencePoint> referencePoints, List<Solution> currentFront) {
        for (Solution aSt : St) {
            double minDistance = Double.MAX_VALUE;
            int index = 0;
            for (int i = 0; i < referencePoints.size(); i++) {
                double distance = NSGA3Util.perpendicularDistance(aSt, referencePoints.get(i));
                if (minDistance > distance) {
                    minDistance = distance;
                    index = i;
                }
            }
            if (!currentFront.contains(aSt))
                referencePoints.get(index).addMember(aSt);
            else referencePoints.get(index).addPotentialMember(aSt, minDistance);
        }
    }

    private void niching(int numberToAdd,
                         List<NSGA3Util.ReferencePoint> points, List<Solution> currentFront,
                         Solution[] newPopulation, int currentIndex) {
        int k = 0;
        while (k < numberToAdd) {
            List<NSGA3Util.ReferencePoint> Jmin = new LinkedList<>();
            int currentMin = Integer.MAX_VALUE;
            for (NSGA3Util.ReferencePoint point : points) {
                currentMin = Math.min(currentMin, point.getNumberOfMembers());
            }
            for (NSGA3Util.ReferencePoint point : points) {
                if (point.getNumberOfMembers() == currentMin) Jmin.add(point);
            }

            NSGA3Util.ReferencePoint ref = null;
            try {
                 ref = Jmin.get(rand.nextInt(Jmin.size()));
            }catch(IllegalArgumentException e){
                System.out.println(currentMin);
                System.out.println(currentIndex);
                System.exit(0);
            }
            List<Solution> I = new LinkedList<>(ref.getPotentialMembers());

            if (I.size() != 0) {
                Solution next;
                if (ref.getNumberOfMembers() == 0) {
                    int minIndex = 0;
                    for (int i = 0; i < I.size(); i++) {
                        if (ref.getDistance(I.get(minIndex)) > ref.getDistance(I.get(i))) {
                            minIndex = i;
                        }
                    }
                    next = I.get(minIndex);
                } else {
                    next = I.get(rand.nextInt(I.size()));
                }
                newPopulation[currentIndex + k] = next;
                ref.addMember(next);
                ref.removePotentialMember(next);
                currentFront.remove(next);
                k++;
            } else {
                points.remove(ref);
            }
        }
    }

    @Override
    public List<Solution> getNondominatedSolutions() {
        return fronts.get(0);
    }

    @Override
    public List<List<Solution>> getParetoFronts() {
        return fronts;
    }
}