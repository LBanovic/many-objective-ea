package hr.fer.zemris.zavrsni.algorithms;

import hr.fer.zemris.zavrsni.algorithms.PFGenerators.DTLZ1Generator;
import hr.fer.zemris.zavrsni.algorithms.PFGenerators.PFGenerator;
import hr.fer.zemris.zavrsni.algorithms.PFGenerators.SphereGenerator;
import hr.fer.zemris.zavrsni.evaluator.MOOPProblem;
import hr.fer.zemris.zavrsni.solution.Solution;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public final class MOOPUtils {

    public static final double EPSILON = 1e-9;

    //So it cannot be instantiated.
    private MOOPUtils() {
    }


    /**
     * Performs non dominated sorting on the given population and stores the resulting Pareto fronts
     * in the provided List.
     *
     * @param population
     * @param fronts
     */
    @SuppressWarnings("unchecked")
    public static <T extends Solution> void nonDominatedSorting(List<T> population,
                                                                List<List<T>> fronts) {
        double[] dominatedBy = new double[population.size()];
        List<Integer>[] dominatesOver = (List<Integer>[]) new LinkedList[population.size()];
        fronts.clear();

        List<List<Integer>> indexFronts = new LinkedList<>();
        List<Integer> firstFront = new LinkedList<>();
        indexFronts.add(firstFront);

        for (int i = 0; i < population.size(); i++) {
            dominatedBy[i] = 0;
            dominatesOver[i] = new LinkedList<>();
            for (int j = 0; j < population.size(); j++) {
                if (i != j) {
                    if (dominates(population.get(i), population.get(j))) {
                        dominatesOver[i].add(j);
                    } else if (dominates(population.get(j), population.get(i))) {
                        dominatedBy[i]++;
                    }
                }
            }
            if (dominatedBy[i] == 0) {
                firstFront.add(i);
            }
        }

        int frontCounter = 0;
        while (true) {
            List<Integer> nextFront = new LinkedList<>();
            for (int index : indexFronts.get(frontCounter)) {
                for (int j : dominatesOver[index]) {
                    dominatedBy[j]--;
                    if (dominatedBy[j] == 0) {
                        nextFront.add(j);
                    }
                }
            }
            if (nextFront.size() == 0) {
                break;
            }
            frontCounter++;
            indexFronts.add(nextFront);
        }

        for (List<Integer> front : indexFronts) {
            List<T> solutionFront = new ArrayList<>(front.size());
            for (int i : front) {
                solutionFront.add(population.get(i));
            }
            fronts.add(solutionFront);
        }
    }

    public static <T extends Solution> boolean dominates(T s1, T s2) {
        double[] obj1 = s1.getObjectives();
        double[] obj2 = s2.getObjectives();

        for (int i = 0; i < obj1.length; i++) {
            if (obj1[i] > obj2[i]) {
                return false;
            }
        }

        for (int i = 0; i < obj1.length; i++) {
            if (obj1[i] < obj2[i]) {
                return true;
            }
        }

        return false;
    }

    /**
     * Compares the given number with the bounds and returns the according number.
     *
     * @param toConstraint number to compare; returned if within bounds
     * @param lowerBound   lower bound; returned if the compared number is smaller than it
     * @param upperBound   upper bound; returned if the compared number is greater than it
     * @return
     */
    public static double constrainWithinInterval(double toConstraint, double lowerBound, double upperBound) {
        if (toConstraint < lowerBound) {
            return lowerBound;
        }
        if (toConstraint > upperBound) {
            return upperBound;
        }
        return toConstraint;
    }


    public static double calculateDistance(Solution s, Solution t) {
        return calculateDistance(s.getObjectives(), t.getObjectives());
    }

    public static double calculateDistance(double[] s1, double[] s2) {
        double sum = 0;
        for (int i = 0; i < s1.length; i++) {
            double dif = s1[i] - s2[i];
            sum += dif * dif;
        }
        return Math.sqrt(sum);
    }

    private static double[] convertList(List<Double> s) {
        double[] next = new double[s.size()];
        for (int i = 0; i < next.length; i++) {
            next[i] = s.get(i);
        }
        return next;
    }

    public static int binomialCoefficient(int n, int k) {
        if (n < k) throw new IllegalArgumentException("n must be greater than k");
        if (k == 0 || k == n) return 1;
        return binomialCoefficient(n - 1, k - 1) + binomialCoefficient(n - 1, k);
    }

    public static MOOPProblem getExample(String exampleName, int exampleSize) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        return (MOOPProblem) Class.forName("hr.fer.zemris.zavrsni.evaluator.examples." + exampleName).
                getDeclaredConstructor(Integer.class).newInstance(exampleSize);
    }

    public static void getPFReferences(List<ReferencePoint> points, ReferencePoint point,
                                       int numberOfObjectives, int left, int total, int element) {
        if (element == numberOfObjectives - 1) {
            point.location[element] = (double) left;
            points.add(new ReferencePoint(point.location.clone()));
        } else {
            for (int i = 0; i <= left; i++) {
                point.location[element] = (double) i;
                getPFReferences(points, point, numberOfObjectives, left - i, total, element + 1);
            }
        }
    }

    public static List<List<Double>> generateOneLayer(int numberOfObjectives, int numberOfDivisions,
                                                      PFGenerator pfgen) {
        List<ReferencePoint> refs = new LinkedList<>();
        getPFReferences(refs, new ReferencePoint(new double[numberOfObjectives]),
                numberOfObjectives, numberOfDivisions, numberOfDivisions, 0);
        return pfgen.generatePF(refs, numberOfDivisions);
    }


    public static List<List<Double>> generateTwoLayer(int numberOfObjectives, int numberOfDivisions,
                                                      int numberOfOuterDivisions,
                                                      PFGenerator pfgen) {
        List<List<Double>> l = generateOneLayer(numberOfObjectives, numberOfOuterDivisions, pfgen);
        List<ReferencePoint> refs = new LinkedList<>();
        getPFReferences(refs, new ReferencePoint(new double[numberOfObjectives]),
                numberOfObjectives, numberOfDivisions, numberOfDivisions, 0);

        for (int i = 0; i < refs.size(); i++) {
            for (int j = 0; j < refs.get(i).location.length; j++) {
                refs.get(i).location[j] = (((double) numberOfDivisions) / numberOfObjectives +
                        refs.get(i).location[j]) / 2.;
            }
        }
        l.addAll(pfgen.generatePF(refs, numberOfDivisions));
        return l;
    }

    public static <T extends Solution> double calculateIGD(List<T> solutions, String problemName,
                                                           int problemSize) {
        List<List<Double>> pf = loadOptimalPF(problemName, problemSize);
        double sum = 0;
        for (List<Double> coord : pf) {
            double min = Double.MAX_VALUE;
            for (Solution s : solutions) {
                min = Math.min(min, calculateDistance(convertList(coord), s.getObjectives()));
            }
            sum += min;
        }
        return sum / pf.size();
    }

    private static List<List<Double>> loadOptimalPF(String problemName, int problemSize) {
        return loadOptimalPF(problemName, problemSize, true);
    }

    private static List<List<Double>> loadOptimalPF(String problemName, int problemSize,
                                                    boolean minimization) {
        String fileName = "Pareto fronts/PF-" + problemName + "(" + problemSize + ").txt";
        List<List<Double>> pf = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader
                (new FileInputStream(fileName)))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] split = line.split(" ");
                List<Double> coord = new ArrayList<>(split.length);
                pf.add(coord);
                for (String s : split) {
                    coord.add((minimization ? 1 : -1) * Double.parseDouble(s));
                }
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Such a problem of given size does not exist.");
        }
        return pf;
    }

    @SuppressWarnings("unchecked")
    public static <T extends Solution> void removeExcessSolutions(List<T> archive, int archiveSize) {
        List<List<Pair<T, Double>>> distances = new ArrayList<>(archive.size());
        Pair<T, Double> testPair = new Pair<>(null, 0.);
        for (int i = 0; i < archive.size(); i++) {
            List<Pair<T,Double>> help = new LinkedList<>();
            T f = archive.get(i);
            for (T s : archive) {
                if (f != s) {
                    help.add(new Pair<>(s, calculateDistance(s, f)));
                }
            }
            Collections.sort(help);
            distances.add(help);
        }

        while (archive.size() > archiveSize) {
            Set<Integer> considered = new HashSet<>();
            for (int i = 0; i < archive.size(); i++) considered.add(i);
            for (int currentIndex = 0; currentIndex < distances.get(0).size() && considered.size() > 1; currentIndex++) {
                double min = Double.MAX_VALUE;
                for (int i = 0; i < distances.size(); i++) {
                    if (considered.contains(i)) min = Math.min(min, distances.get(i).get(currentIndex).value);
                }
                for (int i = 0; i < distances.size(); i++) {
                    if (considered.contains(i) && Math.abs(distances.get(i).get(currentIndex).value - min) > EPSILON)
                        considered.remove(i);
                }
            }

            int index = considered.toArray(new Integer[1])[0];
            testPair.key = archive.remove(index);
            distances.remove(index);
            for(List<Pair<T, Double>> l : distances){
                l.remove(testPair);
            }
        }
    }

    private static class Pair<K, V extends Number> implements Comparable<Pair<K, V>> {
        K key;
        V value;

        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Pair<?, ?> pair = (Pair<?, ?>) o;
            return Objects.equals(key, pair.key);
        }

        @Override
        public int hashCode() {
            return Objects.hash(key);
        }

        @Override
        public int compareTo(Pair<K, V> o) {
            return Double.compare(value.doubleValue(), o.value.doubleValue());
        }
    }

    public static class ReferencePoint {

        public final double[] location;
        private List<Solution> members;
        private Map<Solution, Double> potentialMembers;

        public ReferencePoint(double[] location) {
            this.location = location;
            members = new LinkedList<>();
            potentialMembers = new HashMap<>();
        }

        public int getNumberOfMembers() {
            return members.size();
        }

        public void addMember(Solution s) {
            members.add(s);
        }

        public void addPotentialMember(Solution s, double distance) {
            potentialMembers.put(s, distance);
        }

        public void removePotentialMember(Solution s) {
            potentialMembers.remove(s);
        }

        public double getDistance(Solution s) {
            return potentialMembers.get(s);
        }

        public Set<Solution> getPotentialMembers() {
            return potentialMembers.keySet();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ReferencePoint that = (ReferencePoint) o;
            return Arrays.equals(location, that.location);
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(location);
        }

        @Override
        public String toString() {
            return "ReferencePoint{" +
                    "location=" + Arrays.toString(location) +
                    '}';
        }
    }

    public static void main(String[] args) {
        String[] problems = {"DTLZ1", "DTLZ2", "DTLZ3", "DTLZ4"};
        int[] numberOfObjectives = {2, 3, 5, 8, 10};
        int[] numberOfDivisions = {100, 12, 6};
        int inner = 2;
        int outer = 3;

        for (String problem : problems) {
            PFGenerator pfgen;
            if (problem.equals("DTLZ1")) pfgen = new DTLZ1Generator();
            else pfgen = new SphereGenerator();
            for (int i = 0; i < numberOfObjectives.length; i++) {
                List<List<Double>> s;
                if (numberOfObjectives[i] <= 5) {
                    s = generateOneLayer(numberOfObjectives[i], numberOfDivisions[i], pfgen);
                } else {
                    s = generateTwoLayer(numberOfObjectives[i], inner, outer, pfgen);
                }
                try (FileOutputStream fos = new FileOutputStream("Pareto fronts/PF-" + problem +
                        "(" + numberOfObjectives[i] + ").txt");
                     BufferedOutputStream bw = new BufferedOutputStream(fos);
                     PrintStream ps = new PrintStream(bw, true)) {
                    OutputUtils.printNumberList(ps, s);
                } catch (IOException e) {
                    System.out.println("here");
                }
            }
        }
    }
}
