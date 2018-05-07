package hr.fer.zemris.zavrsni.algorithms;

public interface FitnessObservable {
    void attachObserver(FitnessObserver o);
    void removeObserver(FitnessObserver o);
    void fitnessChanged();
}
