package hr.fer.zemris.zavrsni.algorithms.PFGenerators;

import hr.fer.zemris.zavrsni.algorithms.MOOPUtils;

import java.util.LinkedList;
import java.util.List;

public class SphereGenerator implements PFGenerator {
    @Override
    public List<List<Double>> generatePF(List<MOOPUtils.ReferencePoint> refs, int numDivisions) {
        List<List<Double>> l = new LinkedList<>();
        for (MOOPUtils.ReferencePoint ref : refs) {
            List<Double> ext = new LinkedList<>();
            l.add(ext);

            double sum = 0;

            for(int j = 0; j < ref.location.length; j++){
                sum += ref.location[j] * ref.location[j];
            }

            double k = Math.sqrt(1. / sum);

            for (int j = 0; j < ref.location.length; j++) {
                ext.add(k * ref.location[j]);
            }
        }
        return l;
    }
}
