package hr.fer.zemris.zavrsni.experiments;

import hr.fer.zemris.zavrsni.algorithms.AbstractMOOPAlgorithm;
import hr.fer.zemris.zavrsni.algorithms.MOOPUtils;
import hr.fer.zemris.zavrsni.algorithms.OutputUtils;
import hr.fer.zemris.zavrsni.algorithms.PopulationUtils;
import hr.fer.zemris.zavrsni.evaluator.MOOPProblem;
import hr.fer.zemris.zavrsni.solution.Solution;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ExperimentParser {
    public static void main(String[] args) {
        List<String> all = null;
        try {
            all = Files.readAllLines(Paths.get(args[0]));
        } catch (IOException e) {
            System.err.println("File not found.");
            System.exit(0);
        }

        while (all.get(0).startsWith("#")) all.remove(0);

        int maxGen = Integer.parseInt(all.get(0).substring("Max generations:".length()).trim());
        all.remove(0);
        int populationSize = Integer.parseInt(all.get(0).substring("Population size:".length()).trim());

        String alg[] = all.get(1).split(":");
        String name = alg[1].split("\\s+")[1];
        String paramString = alg[1].split("\\s+")[2];
        String[] params = paramString.substring(paramString.indexOf("{") + 1, paramString.indexOf("}")).
                split(",\\s*");

        String prob[] = all.get(2).split(":");
        String probName = prob[1].split("\\s+")[1];
        String sizeString = prob[1].split("\\s+")[2];
        int probSize = Integer.parseInt
                (sizeString.substring(sizeString.indexOf("{") + 1, sizeString.indexOf("}")));

        Experiment<?> e = null;
        MOOPProblem problem = null;
        try {
            e = (Experiment<?>) Class.forName("hr.fer.zemris.zavrsni.experiments." + name + "Experiment").
                    getDeclaredConstructor().newInstance();
            problem = MOOPUtils.getExample(probName, probSize);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException | ClassNotFoundException e1) {
            System.out.println("Invalid algorithm or problem.");
            e1.printStackTrace();
            System.exit(0);
        }
        Map<AbstractMOOPAlgorithm<?>, Double> igdMap = new HashMap<>();

        for(int i = 0; i < 20; i++){

            try {
                List<Solution> population = PopulationUtils.generateRandomPopulation(populationSize, problem);
                AbstractMOOPAlgorithm<?> a = e.run(problem, population, maxGen, params);
                double igd = MOOPUtils.calculateIGD(a.getNondominatedSolutions(), probName, probSize);
                igdMap.put(a, igd);
            }catch(RuntimeException r){
                i--;
            }
        }

        List<AbstractMOOPAlgorithm<?>> sorted =
                igdMap.keySet().stream().sorted(Comparator.comparing(igdMap::get)).collect(Collectors.toList());

        String dir = "Results/" + name + "/" + probName + " (" + probSize + ")";
        try{
            Files.createDirectories(Paths.get(dir));
        }catch (IOException g){
            System.err.println("FAILED HERE");
        }

        for(int i = 0; i < sorted.size(); i++){
            String s = dir + "/" + (i + 1) + ".res";
            try(PrintStream ps = new PrintStream(new FileOutputStream(s))){
                AbstractMOOPAlgorithm<?> a = sorted.get(i);
                ps.println(name + "_" + probName + "_" + probSize);
                ps.println("IGD: " + igdMap.get(a));
                OutputUtils.printSolutions(a, ps);
            }catch(IOException exc){
                System.err.println("FAILED");
            }
        }
        //TODO napisati experimente za sve algoritme i ovdje pokrenuti 20 puta svaki, zapisati IGD na vrh datoteke
    }
}
