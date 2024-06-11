package org.example;

public class Butterfly {
    double fragrance;
    double fitness;
    double previousFitness;
    double[] position;

    public Butterfly(int dimensions) {
        this.position = new double[dimensions];
    }
}
