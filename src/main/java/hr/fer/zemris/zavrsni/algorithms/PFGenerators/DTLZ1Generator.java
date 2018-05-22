package hr.fer.zemris.zavrsni.algorithms.PFGenerators;

import hr.fer.zemris.zavrsni.algorithms.MOOPUtils;

import java.util.LinkedList;
import java.util.List;

public class DTLZ1Generator implements PFGenerator {
    @Override
    public List<List<Double>> generatePF(List<MOOPUtils.ReferencePoint> refs, int numDivisions) {
        List<List<Double>> l = new LinkedList<>();
        for (MOOPUtils.ReferencePoint ref : refs) {
            List<Double> ext = new LinkedList<>();
            l.add(ext);
            for (int j = 0; j < ref.location.length; j++) {
                ext.add(0.5 * ref.location[j] / numDivisions);
            }
        }
        return l;
    }
}
