package de.etgramli.battlebros.util;

/**
 * Used to make a Game observable.
 */
public interface IObservable {
    void addObserver(IObserver observer);

    void removeObservers();

    void notifyObservers();
}
