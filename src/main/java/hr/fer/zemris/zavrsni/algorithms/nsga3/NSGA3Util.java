package hr.fer.zemris.zavrsni.algorithms.nsga3;

import hr.fer.zemris.zavrsni.algorithms.MOOPUtils;
import hr.fer.zemris.zavrsni.solution.Solution;
import org.apache.commons.math3.linear.*;

import java.util.*;

public class NSGA3Util {

    private NSGA3Util() {
    }

    protected static double[] findIdealPoint(List<Solution> firstFront, int numberOfObjectives) {
        double[] zmin = new double[numberOfObjectives];

        for (int i = 0; i < zmin.length; i++) {
            zmin[i] = Double.MAX_VALUE;
            for (Solution aFirstFront : firstFront) {
                zmin[i] = Math.min(zmin[i], aFirstFront.getObjectives()[i]);
            }
        }
        return zmin;
    }

    private static double achievementScalarizingFunction(Solution sol, int index) {
        final double onAxis = 1;
        final double other = 1e-6;
        double max = Double.MIN_VALUE;
        double[] objectives = sol.getObjectives();
        for (int i = 0; i < objectives.length; i++) {
            double weight;
            if (i == index) weight = onAxis;
            else weight = other;
            if (objectives[i] / weight > max) max = objectives[i] / weight;
        }
        return max;
    }

    protected static Solution[] extremePoints(List<Solution> firstFront, final int numberOfObjectives) {
        Solution[] extremePoints = new Solution[numberOfObjectives];
        for (int i = 0; i < numberOfObjectives; i++) {
            double minValue = Double.MAX_VALUE;
            Solution minSol = null;
            for (Solution sol : firstFront) {
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

    private static double[] inverseMaxObjectives(List<Solution> St, double[] idealPoint) {
        double[] maxPoint = new double[St.get(0).getObjectives().length];
        for (int i = 0; i < maxPoint.length; i++) {
            maxPoint[i] = Double.MIN_VALUE;
            for (Solution s : St) {
                maxPoint[i] = Math.max(maxPoint[i] + idealPoint[i], s.getObjectives()[i]);
            }
        }
        for(int i = 0; i < maxPoint.length; i++){
            maxPoint[i] = 1 / maxPoint[i];
        }
        return maxPoint;
    }

    protected static double[] constructHyperplane(Solution[] extremes, List<Solution> St, double[] idealPoint) {

        boolean duplicate = false;
        for(int i = 0; i < extremes.length && !duplicate; i++){
            for (Solution extreme : extremes) {
                if (extremes[i] == extreme) {
                    duplicate = true;
                    break;
                }
            }
        }

        boolean negativeIntercept = false;
        double[] params = null;

        if(!duplicate) {
            double[][] samples = new double[extremes.length][extremes.length];
            for (int k = 0; k < extremes.length; k++) {
                samples[k] = extremes[k].getObjectives();
            }
            RealMatrix A = new Array2DRowRealMatrix(samples);
            DecompositionSolver solver = new LUDecomposition(A).getSolver();
            double[] allOnes = new double[extremes.length];
            Arrays.fill(allOnes, 1.0);
            RealVector constants = new ArrayRealVector(allOnes);
            RealVector solution = solver.solve(constants);
            params = solution.toArray();
            for (double param : params) {
                if (param < 0) {
                    negativeIntercept = true;
                    break;
                }
            }
        }
        if(duplicate || negativeIntercept){
            params = inverseMaxObjectives(St, idealPoint);
        }
        return params;
    }

    protected static double getIntercept(double[] parameters, int index) {
        return 1. / parameters[index];
    }

    protected static double perpendicularDistance(Solution sol, MOOPUtils.ReferencePoint referencePoint) {
        double[] point = sol.getObjectives();
        RealVector s = new ArrayRealVector(point);
        RealVector w = new ArrayRealVector(referencePoint.location);
        return s.subtract(w.mapMultiply(w.dotProduct(s)).mapMultiply(1. / (w.getNorm() * w.getNorm()))).getNorm();
    }

    public static void recursiveReferencePoints(List<MOOPUtils.ReferencePoint> points, MOOPUtils.ReferencePoint point,
                                       int numberOfObjectives, int left, int total, int element) {
        if (element == numberOfObjectives - 1) {
            point.location[element] = (double) left / total;
            points.add(new MOOPUtils.ReferencePoint(point.location.clone()));
        } else {
            for (int i = 0; i <= left; i++) {
                point.location[element] = (double) i / total;
                recursiveReferencePoints(points, point, numberOfObjectives, left - i, total, element + 1);
            }
        }
    }

    public static void generateReferencePoints(List<MOOPUtils.ReferencePoint> points, int numberOfObjectives, List<Integer> p){
        MOOPUtils.ReferencePoint r = new MOOPUtils.ReferencePoint(new double[numberOfObjectives]);
        points.clear();
        recursiveReferencePoints(points, r, numberOfObjectives, p.get(0), p.get(0), 0);
        if(p.size() > 1){
            List<MOOPUtils.ReferencePoint> inside = new LinkedList<>();
            recursiveReferencePoints(inside, r, numberOfObjectives, p.get(1), p.get(1), 0);

            double center = 1. / numberOfObjectives;

            for(int i = 0; i < inside.size(); i++){
                for(int j = 0; j < inside.get(i).location.length; j++){
                    inside.get(i).location[j] = (center + inside.get(i).location[j]) / 2;
                }
            }

            points.addAll(inside);
        }
    }
}
