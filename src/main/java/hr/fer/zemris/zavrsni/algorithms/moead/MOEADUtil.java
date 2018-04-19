package hr.fer.zemris.zavrsni.algorithms.moead;

import hr.fer.zemris.zavrsni.algorithms.nsga3.NSGA3Util;

public class MOEADUtil {
    public static int getSizeOfPopulation(int H, int numberOfObjectives){
        return NSGA3Util.binomialCoefficient(H + numberOfObjectives - 1, numberOfObjectives - 1);
    }
}
