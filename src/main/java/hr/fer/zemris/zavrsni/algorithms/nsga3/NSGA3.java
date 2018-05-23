package hr.fer.zemris.zavrsni.algorithms.nsga3;

import hr.fer.zemris.zavrsni.algorithms.AbstractMOOPAlgorithm;
import hr.fer.zemris.zavrsni.algorithms.MOOPUtils;
import hr.fer.zemris.zavrsni.algorithms.OutputUtils;
import hr.fer.zemris.zavrsni.algorithms.PopulationUtils;
import hr.fer.zemris.zavrsni.algorithms.operators.Crossover;
import hr.fer.zemris.zavrsni.algorithms.operators.Mutation;
import hr.fer.zemris.zavrsni.algorithms.operators.selection.RandomSelection;
import hr.fer.zemris.zavrsni.evaluator.MOOPProblem;
import hr.fer.zemris.zavrsni.solution.Solution;

import java.util.*;

import static hr.fer.zemris.zavrsni.algorithms.PopulationUtils.mergePopulations;

public class NSGA3 extends AbstractMOOPAlgorithm<Solution> {

//    private Random rand = new Random();
//
//    /*PARAMETERS*/
//    private final boolean allowRepetition;
//    private final List<Integer> numberOfDivisions;
//
//    private List<List<Solution>> fronts;
//
//    public NSGA3(List<Solution> population, MOOPProblem problem, Crossover<Solution> crossover, Mutation mutation,
//                 int maxGen, boolean allowRepetition, List<Integer> numberOfDivisions) {
//        super(population, problem, maxGen, crossover, mutation);
//        this.allowRepetition = allowRepetition;
//        this.numberOfDivisions = numberOfDivisions;
//        selection = new RandomSelection();
//        fronts = new LinkedList<>();
//    }
//
//    public static int getPreferredPopulationSize(int numberOfObjectives, List<Integer> numberOfDivisions) {
//        int sum = 0;
//        for(int num : numberOfDivisions){
//            sum += MOOPUtils.binomialCoefficient(numberOfObjectives + num - 1, num);
//        }
//        return sum;
//    }
//
//    @Override
//    public void run() {
//        int gen = 0;
//        List<Solution> childPopulation;
//        int numberOfObjectives = problem.getNumberOfObjectives();
//        final int numberOfRefPoints = getPreferredPopulationSize(numberOfObjectives, numberOfDivisions);
//        List<MOOPUtils.ReferencePoint> points = new ArrayList<>(numberOfRefPoints);
//        NSGA3Util.generateReferencePoints(points, numberOfObjectives, numberOfDivisions);
//        while (true) {
//            System.out.println("Generation: " + gen);
//
//            if (gen >= maxGen) {
//                OutputUtils.printSolutions(this);
//                System.out.println("AFTER########################");
//                PopulationUtils.evaluatePopulation(population, problem);
//                MOOPUtils.nonDominatedSorting(population, fronts);
//                break;
//            }
//
//            childPopulation = PopulationUtils.createNewPopulation(population, selection, crossover, mutation, allowRepetition);
//
//            List<Solution> combined = mergePopulations(population, childPopulation);
//            List<Solution> newPopulation = new ArrayList<>(population.size());
//
//            PopulationUtils.evaluatePopulation(combined, problem);
//            MOOPUtils.nonDominatedSorting(combined, fronts);
//
//            int i;
//            for (i = 0; i < fronts.size(); i++) {
//                List<Solution> l = fronts.get(i);
//                if (newPopulation.size() + l.size() <= population.size()) {
//                    newPopulation.addAll(l);
//                } else break;
//            }
//
//            if (newPopulation.size() != population.size()) {
//                //ako nova populacija nije popunjena
//                List<Solution> currentFront = fronts.get(i);
//                List<Solution> St = new ArrayList<>(newPopulation);
//                St.addAll(currentFront);
//                normalize(St);
//                List<MOOPUtils.ReferencePoint> copyPoints = new ArrayList<>(points);
//                associate(copyPoints, currentFront);
//                niching(copyPoints, newPopulation);
//            }
//            population = newPopulation;
//            gen++;
//        }
//
//    }
//
//    private void normalize(List<Solution> St) {
//        double[] idealPoint = NSGA3Util.findIdealPoint(fronts.get(0), problem.getNumberOfObjectives());
//        for (Solution sol : St) {
//            sol.translateObjectives(idealPoint);
//        }
//        Solution[] extremes = NSGA3Util.extremePoints(fronts.get(0), problem.getNumberOfObjectives());
//        double[] hyperplane = NSGA3Util.constructHyperplane(extremes, St, idealPoint);
//        for (int i = 0; i < problem.getNumberOfObjectives(); i++) {
//            double intercept = Math.max(NSGA3Util.getIntercept(hyperplane, i), 10e-10);
//            for (Solution sol : St) {
//                double[] objectives = sol.getTranslatedObjectives();
//                objectives[i] /= intercept;
//            }
//        }
//    }
//
//    private void associate(List<MOOPUtils.ReferencePoint> referencePoints, List<Solution> currentFront) {
//        boolean notLastFront = true;
//        for(int j = 0; j < fronts.size() && notLastFront; j++) {
//            for(Solution t: fronts.get(j)) {
//
//                double minDistance = Double.MAX_VALUE;
//                int index = 0;
//                for (int i = 0; i < referencePoints.size(); i++) {
//                    double distance = NSGA3Util.perpendicularDistance(t, referencePoints.get(i));
//                    if (minDistance > distance) {
//                        minDistance = distance;
//                        index = i;
//                    }
//                }
//
//                if (fronts.get(j) == currentFront) {
//                    referencePoints.get(index).addPotentialMember(t, minDistance);
//                    notLastFront = false;
//                }
//                else referencePoints.get(index).addMember(t);
//            }
//        }
//    }
//
//    private MOOPUtils.ReferencePoint findNicheReferencePoint(List<MOOPUtils.ReferencePoint> points){
//        List<MOOPUtils.ReferencePoint> Jmin = new LinkedList<>();
//        int currentMin = Integer.MAX_VALUE;
//        for (MOOPUtils.ReferencePoint point : points) {
//            currentMin = Math.min(currentMin, point.getNumberOfMembers());
//        }
//        for (MOOPUtils.ReferencePoint point : points) {
//            if (point.getNumberOfMembers() == currentMin) Jmin.add(point);
//        }
//
//        MOOPUtils.ReferencePoint ref = null;
//        try {
//            ref = Jmin.get(rand.nextInt(Jmin.size()));
//        } catch (IllegalArgumentException e) {
//            System.out.println(currentMin);
//            System.exit(0);
//        }
//        return ref;
//    }
//
//    private Solution selectClusterMember(MOOPUtils.ReferencePoint ref) {
//        Solution s = null;
//        List<Solution> I = new LinkedList<>(ref.getPotentialMembers());
//        if (I.size() != 0) {
//            if (ref.getNumberOfMembers() == 0) {
//                int minIndex = 0;
//                for (int i = 0; i < I.size(); i++) {
//                    if (ref.getDistance(I.get(minIndex)) > ref.getDistance(I.get(i))) {
//                        minIndex = i;
//                    }
//                }
//                s = I.get(minIndex);
//            } else {
//                s = I.get(rand.nextInt(I.size()));
//            }
//        }
//        return s;
//    }
//
//    private void niching(List<MOOPUtils.ReferencePoint> points,
//                         List<Solution> newPopulation) {
//        while (newPopulation.size() < populationSize()) {
//            MOOPUtils.ReferencePoint ref = findNicheReferencePoint(points);
//            Solution chosen = selectClusterMember(ref);
//            if(chosen != null){
//                newPopulation.add(chosen);
//                ref.addMember(chosen);
//                ref.removePotentialMember(chosen);
//            } else {
//                points.remove(ref);
//            }
//        }
//    }
//
//    @Override
//    public List<Solution> getNondominatedSolutions() {
//        return fronts.get(0);
//    }
//
//    @Override
//    public List<List<Solution>> getParetoFronts() {
//        return fronts;
//    }
    private Random rand = new Random();

    /*PARAMETERS*/
    private final boolean allowRepetition;
    private final List<Integer> numberOfDivisions;

    private List<List<Solution>> fronts;

    public NSGA3(List<Solution> population, MOOPProblem problem, Crossover<Solution> crossover, Mutation mutation,
                 int maxGen, boolean allowRepetition, List<Integer> numberOfDivisions) {
        super(population, problem, maxGen, crossover, mutation);
        this.allowRepetition = allowRepetition;
        this.numberOfDivisions = numberOfDivisions;
        selection = new RandomSelection();
        fronts = new LinkedList<>();
    }

    public static int getPreferredPopulationSize(int numberOfObjectives, List<Integer> numberOfDivisions) {
        int sum = 0;
        for(int p: numberOfDivisions){
            sum += MOOPUtils.binomialCoefficient(numberOfObjectives + p - 1, p);
        }
        return sum;
    }

    @Override
    public void run() {
        int gen = 0;
        List<Solution> childPopulation;
        int numberOfObjectives = problem.getNumberOfObjectives();
        final int numberOfRefPoints = getPreferredPopulationSize(numberOfObjectives, numberOfDivisions);
        List<MOOPUtils.ReferencePoint> points = new ArrayList<>(numberOfRefPoints);
        while (true) {
            System.out.println("Generation: " + gen);

            if (gen >= maxGen){
                PopulationUtils.evaluatePopulation(population, problem);
                MOOPUtils.nonDominatedSorting(population, fronts);
                break;
            }

            childPopulation = PopulationUtils.createNewPopulation(population, selection, crossover, mutation, allowRepetition);

            List<Solution> combined = mergePopulations(population, childPopulation);
            List<Solution> newPopulation = new ArrayList<>(population.size());

            PopulationUtils.evaluatePopulation(combined, problem);
            MOOPUtils.nonDominatedSorting(combined, fronts);

            int i;
            for (i = 0; i < fronts.size(); i++) {
                List<Solution> l = fronts.get(i);
                if (newPopulation.size() + l.size() <= population.size()) {
                    newPopulation.addAll(l);
                } else break;
            }

            if (newPopulation.size() != population.size()) {
                //ako nova populacija nije popunjena

                List<Solution> currentFront = fronts.get(i);
                List<Solution> St = new ArrayList<>(newPopulation);
                St.addAll(currentFront);

                normalize(St);
                points.clear();
                NSGA3Util.generateReferencePoints(points, numberOfObjectives, numberOfDivisions);

                associate(St, points, currentFront);

                niching(population.size() - newPopulation.size(),
                        points, currentFront, newPopulation);
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

    private void associate(List<Solution> St, List<MOOPUtils.ReferencePoint> referencePoints, List<Solution> currentFront) {
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

            boolean contains = false;
            for(Solution s : currentFront){
                if(s == aSt){
                    referencePoints.get(index).addPotentialMember(aSt, minDistance);
                    contains = true;
                    break;
                }
            }
            if (!contains)
                referencePoints.get(index).addMember(aSt);
        }
    }

    private void niching(int numberToAdd,
                         List<MOOPUtils.ReferencePoint> points, List<Solution> currentFront,
                         List<Solution> newPopulation) {
        int k = 0;
        while (k < numberToAdd) {
            List<MOOPUtils.ReferencePoint> Jmin = new LinkedList<>();
            int currentMin = Integer.MAX_VALUE;
            for (MOOPUtils.ReferencePoint point : points) {
                currentMin = Math.min(currentMin, point.getNumberOfMembers());
            }
            for (MOOPUtils.ReferencePoint point : points) {
                if (point.getNumberOfMembers() == currentMin) Jmin.add(point);
            }

            MOOPUtils.ReferencePoint ref = null;
            try {
                ref = Jmin.get(rand.nextInt(Jmin.size()));
            }catch(IllegalArgumentException e){
                System.out.println(currentMin);
                System.out.println(newPopulation.size());
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
                newPopulation.add(next);
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
