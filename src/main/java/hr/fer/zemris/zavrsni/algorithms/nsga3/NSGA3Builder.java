package hr.fer.zemris.zavrsni.algorithms.nsga3;

import hr.fer.zemris.zavrsni.algorithms.AlgorithmBuilder;
import hr.fer.zemris.zavrsni.algorithms.PopulationUtils;
import hr.fer.zemris.zavrsni.algorithms.operators.Crossover;
import hr.fer.zemris.zavrsni.algorithms.operators.Mutation;
import hr.fer.zemris.zavrsni.evaluator.MOOPProblem;
import hr.fer.zemris.zavrsni.solution.RegularSolutionFactory;
import hr.fer.zemris.zavrsni.solution.Solution;

import java.util.List;

public class NSGA3Builder implements AlgorithmBuilder<Solution, NSGA3>{
    private int maxGen;
    private List<Solution> population;
    private Crossover<Solution> crossover;
    private Mutation mutation;
    private MOOPProblem problem;

    private int numberOfDivisions;
    private boolean allowRepetition;

    public NSGA3Builder(MOOPProblem problem, int numberOfDivisions, boolean allowRepetition) {
        this.problem = problem;
        this.maxGen = 250;
        this.allowRepetition = allowRepetition;
        this.numberOfDivisions = numberOfDivisions;
        int popSize = NSGA3.getPreferredPopulationSize(problem.getNumberOfObjectives(), numberOfDivisions);
        this.population = PopulationUtils.generateRandomPopulation(popSize, problem, new RegularSolutionFactory());
    }

    @Override
    public NSGA3Builder setMaxIterations(int maxGen) {
        this.maxGen = maxGen;
        return this;
    }

    @Override
    public NSGA3Builder setPopulation(List<Solution> population) {
        this.population = population;
        return this;
    }

    @Override
    public NSGA3Builder setCrossover(Crossover<Solution> crossover) {
        this.crossover = crossover;
        return this;
    }

    @Override
    public NSGA3Builder setMutation(Mutation mutation) {
        this.mutation = mutation;
        return this;
    }

    @Override
    public NSGA3 getAlgorithm() {
        return new NSGA3(this, allowRepetition, numberOfDivisions);
    }

    @Override
    public MOOPProblem getProblem() {
        return problem;
    }

    @Override
    public Crossover<Solution> getCrossover() {
        return crossover;
    }

    @Override
    public Mutation getMutation() {
        return mutation;
    }

    @Override
    public List<Solution> getPopulation() {
        return population;
    }

    @Override
    public int getMaxIterations() {
        return maxGen;
    }
}