package hr.fer.zemris.zavrsni.algorithms;

import hr.fer.zemris.zavrsni.algorithms.operators.Crossover;
import hr.fer.zemris.zavrsni.algorithms.operators.Mutation;
import hr.fer.zemris.zavrsni.algorithms.operators.Selection;
import hr.fer.zemris.zavrsni.evaluator.MOOPProblem;
import hr.fer.zemris.zavrsni.solution.Solution;

import java.util.List;

public interface AlgorithmBuilder<V extends Solution, T extends AbstractMOOPAlgorithm<V>> {
    default AlgorithmBuilder<V, T> setMaxIterations(int maxGen){return this;}
    default AlgorithmBuilder<V, T> setPopulation(List<V> population){return this;}
    default AlgorithmBuilder<V, T> setCrossover(Crossover<V> crossover){return this;}
    default AlgorithmBuilder<V, T> setMutation(Mutation mutation){return this;}
    default AlgorithmBuilder<V, T> setSelection(Selection<V> selection){return this;}

    T getAlgorithm();

    default MOOPProblem getProblem(){return null;}
    default Selection<V> getSelection(){return null;};
    default Crossover<V> getCrossover(){return null;};
    default Mutation getMutation(){return null;};
    default List<V> getPopulation(){return null;};
    default int getMaxIterations(){return 0;}

}
