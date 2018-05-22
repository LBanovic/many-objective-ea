package hr.fer.zemris.zavrsni.algorithms.PFGenerators;

import hr.fer.zemris.zavrsni.algorithms.MOOPUtils;

import java.util.List;

public interface PFGenerator {
    List<List<Double>> generatePF(List<MOOPUtils.ReferencePoint> refs, int numDivisions);
}
