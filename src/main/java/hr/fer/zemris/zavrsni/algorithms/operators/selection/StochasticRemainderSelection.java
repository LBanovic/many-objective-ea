//package hr.fer.zemris.zavrsni.algorithms.operators.selection;
//
//import hr.fer.zemris.zavrsni.algorithms.operators.Selection;
//import hr.fer.zemris.zavrsni.solution.Solution;
//
//import java.util.Collections;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Random;
//
///**
// * Class that implements stochastic remainder selection without replacement.
// */
//public class StochasticRemainderSelection implements Selection{
//
//    private Random rand = new Random();
//
//    @Override public Solution select(Solution[] population) {
//        double minFitness = population[0].getDummyFitness();
//
//        for(int i = 1; i < population.length; i++) {
//            if(population[i].getDummyFitness() < minFitness)
//                minFitness = population[i].getDummyFitness();
//        }
//        double sumFitness = 0;
//
//        for(Solution sol : population){
//            sol.setDummyFitness(sol.getDummyFitness() + minFitness);
//            sumFitness += sol.getDummyFitness();
//        }
//
//        double[] expectance = new double[population.length];
//        for(int i = 0; i < population.length; i++){
//            expectance[i] = population[i].getDummyFitness() / sumFitness * population.length;
//        }
//
//
//        List<Solution> selectionPopulation = new LinkedList<>();
//
//        for(int i = 0; i < expectance.length; i++){
//            int repeat = (int) expectance[i];
//            double repeatMaybe = expectance[i] - repeat;
//            for(int j = 0; j < repeat; j++){
//                selectionPopulation.add(population[i]);
//            }
//            if(rand.nextFloat() < repeatMaybe) selectionPopulation.add(population[i]);
//        }
//
//        //TODO return the properly selected Solution
//        return null;
//    }
//}
