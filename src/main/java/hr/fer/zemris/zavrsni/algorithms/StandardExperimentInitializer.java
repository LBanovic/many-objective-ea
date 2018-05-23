package hr.fer.zemris.zavrsni.algorithms;

import hr.fer.zemris.zavrsni.algorithms.operators.Crossover;
import hr.fer.zemris.zavrsni.algorithms.operators.Mutation;
import hr.fer.zemris.zavrsni.algorithms.operators.crossover.SBXCrossover;
import hr.fer.zemris.zavrsni.algorithms.operators.mutation.NormalDistributionMutation;
import hr.fer.zemris.zavrsni.algorithms.operators.mutation.PolynomialMutation;
import hr.fer.zemris.zavrsni.evaluator.MOOPProblem;
import hr.fer.zemris.zavrsni.solution.Solution;
import hr.fer.zemris.zavrsni.solution.SolutionFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class StandardExperimentInitializer<S extends Solution> {
    private int maxGen;
    private double eta = 30;
    private final MOOPProblem problem;
    private final List<S> population;
    private  Crossover<S> crossover;
    private  Mutation mutation;

    public StandardExperimentInitializer(MOOPProblem problem, List<S> population, SolutionFactory<S> factory) {
        this(problem, population, factory, 250);
    }

    public StandardExperimentInitializer(String problemName, Integer problemSize, List<S> population, SolutionFactory<S> factory)
            throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        this(MOOPUtils.getExample(problemName, problemSize), population, factory);
    }

    public StandardExperimentInitializer(String problemName, Integer problemSize, List<S> population, SolutionFactory<S> factory, int maxGen)
            throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        this(MOOPUtils.getExample(problemName, problemSize), population, factory, maxGen);
    }

    public StandardExperimentInitializer(MOOPProblem problem, List<S> population, SolutionFactory<S> factory, int maxGen){
        double mutationChance = 1. / problem.getNumberOfVariables();
        this.problem = problem;
        this.population = population;
        this.maxGen = maxGen;
        this.crossover = new SBXCrossover<>(factory, eta, problem.getLowerBounds(), problem.getUpperBounds());
        this.mutation = new PolynomialMutation(problem.getLowerBounds(), problem.getUpperBounds(), mutationChance, 20);
    }

    public int getMaxGen() {
        return maxGen;
    }

    public MOOPProblem getProblem() {
        return problem;
    }

    public List<S> getPopulation() {
        return population;
    }

    public Crossover<S> getCrossover() {
        return crossover;
    }

    public Mutation getMutation() {
        return mutation;
    }

    public void setMaxGen(int maxGen) {
        this.maxGen = maxGen;
    }

    public void setEta(double eta) {
        this.eta = eta;
    }

    public void setCrossover(Crossover<S> crossover) {
        this.crossover = crossover;
    }

    public void setMutation(Mutation mutation) {
        this.mutation = mutation;
    }
}
