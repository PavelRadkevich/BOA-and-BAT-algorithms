package org.example;

import java.util.Random;

public class Bat {
    double[] position;
    double[] velocity;
    double frequency;
    double pulseRate;
    double fitness;
    double volume;

    Bat(int dimension) {
        Random random = new Random();
        position = new double[dimension];
        velocity = new double[dimension];
        volume = random.nextDouble() + 1;
        frequency = 0.0;
        pulseRate = random.nextDouble();
        fitness = Double.MAX_VALUE;
    }
}
