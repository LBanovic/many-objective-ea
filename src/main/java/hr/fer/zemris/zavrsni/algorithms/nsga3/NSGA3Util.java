package hr.fer.zemris.zavrsni.algorithms.nsga3;

import hr.fer.zemris.zavrsni.solution.Solution;
import org.apache.commons.math3.linear.*;

import java.util.*;

public class NSGA3Util {

    private NSGA3Util() {
    }

    private static int factorial(int n) {
        int multiply = 1;
        for (int i = 1; i <= n; i++) {
            multiply *= i;
        }
        return multiply;
    }

    public static int binomialCoefficient(int n, int k) {
        if (n < k) throw new IllegalArgumentException("n must be greater than k");
        System.out.println(n + " " + k);
        if(k > n / 2) k = n - k;
        int mul = 1;
        for(int i = n - k + 1; i <= n; i++){
            mul *= i;
        }
        return mul / factorial(k);
    }

    protected static void getReferencePoints(List<ReferencePoint> points, ReferencePoint point,
                                           int numberOfObjectives, int left, int total, int element) {
        if (element == numberOfObjectives - 1) {
            point.location[element] = (double) left / total;
            points.add(new ReferencePoint(point.location.clone()));
        } else {
            for (int i = 0; i <= left; i++) {
                point.location[element] = (double) i / total;
                getReferencePoints(points, point, numberOfObjectives, left - i, total, element + 1);
            }
        }
    }

    protected static double[] findIdealPoint(List<Solution> St, int numberOfObjectives) {
        double[] zmin = new double[numberOfObjectives];
        for (int i = 0; i < zmin.length; i++) {
            zmin[i] = Double.MAX_VALUE;
            for (int j = 0; j < St.size(); j++) {
                double objectiveValue = St.get(j).getObjectives()[i];
                if (objectiveValue < zmin[i]) zmin[i] = objectiveValue;
            }
        }
        return zmin;
    }

    protected static void subtractIdealPoint(double[] from, double[] idealPoint){
        for(int i = 0; i < idealPoint.length; i++){
            from[i] -= idealPoint[i];
        }
    }
    private static double achievementScalarizingFunction(Solution sol, int index) {
        final double onAxis = 1.;
        final double other = 1e-10;
        double max = Double.MIN_VALUE;
        double[] objectives = sol.getObjectives();
        for (int i = 0; i < objectives.length; i++) {
            double weight;
            if (i == index) weight = onAxis;
            else weight = other;
            if(objectives[i] / weight > max) max = objectives[i] / weight;
        }
        return max;
    }

    protected static Solution[] extremePoints(List<Solution> St, final int numberOfObjectives) {
        Solution[] extremePoints = new Solution[numberOfObjectives];
        for (int i = 0; i < numberOfObjectives; i++) {
            double minValue = Double.MAX_VALUE;
            Solution minSol = null;
            for (Solution sol : St) {
                double ASF = achievementScalarizingFunction(sol, i);
                if (ASF < minValue) {
                    minValue = ASF;
                    minSol = sol;
                }
            }
            extremePoints[i] = minSol;
        }
        return extremePoints;
    }

    protected static double[] constructHyperplane(Solution[] extremes) {
        double[][] samples = new double[extremes.length][extremes.length];
        for(int k = 0; k < extremes.length; k++){
            samples[k] = extremes[k].getObjectives();
        }
        RealMatrix A = new Array2DRowRealMatrix(samples);
        DecompositionSolver solver = new SingularValueDecomposition(A).getSolver();
        double[] allOnes = new double[extremes.length];
        Arrays.fill(allOnes, 1);
        RealVector constants = new ArrayRealVector(allOnes);
        RealVector solution = solver.solve(constants);
        return solution.toArray();
    }

    protected static double getIntercept(double[] parameters, int index){
        return 1. / parameters[index];
    }

    protected static double perpendicularDistance(Solution sol, ReferencePoint referencePoint) {
        RealVector s = new ArrayRealVector(sol.getObjectives());
        RealVector w = new ArrayRealVector(referencePoint.location);
        return s.subtract(w.mapMultiply(w.dotProduct(s)).mapMultiply(1. / (w.getNorm()*w.getNorm()))).getNorm();

    }

    protected static class ReferencePoint {

        protected final double[] location;

        public ReferencePoint(double[] location) {
            this.location = location;
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

    public static int getNumberOfReferencePoints(int numberOfObjectives, int numberOfDivisions){
        return binomialCoefficient(numberOfObjectives + numberOfDivisions - 1, numberOfDivisions);
    }
}
