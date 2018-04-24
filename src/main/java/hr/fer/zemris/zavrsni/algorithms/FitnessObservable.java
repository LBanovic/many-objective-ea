package hr.fer.zemris.zavrsni.algorithms;

public interface FitnessObservable {
    public void attachObserver(FitnessObserver o);
    public void removeObserver(FitnessObserver o);
    public void fitnessChanged();
}
