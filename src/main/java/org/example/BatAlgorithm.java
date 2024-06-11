package org.example;

import java.util.Random;

public class BatAlgorithm {
    int populationSize;
    int dimension;
    int iterations;
    double minFrequency;
    double maxFrequency;
    double alpha;
    double gamma;
    double fMin;
    double fMax;
    double rMax;
    double rMin;
    double velocityMax;
    double velocityMin;
    double bestbestFitness;
    Bat bestBat;
    Bat[] population;
    Random random;
    Functions.function f;

    BatAlgorithm(int populationSize, double minFrequency, double maxFrequency,
                 double alpha, double gamma, double rMax, double rMin, double fMin, double fMax,
                 int dimension, int iterations, Functions.function f) {
        this.populationSize = populationSize;
        this.dimension = dimension;
        this.iterations = iterations;
        this.minFrequency = minFrequency;
        this.maxFrequency = maxFrequency;
        this.alpha = alpha;
        this.gamma = gamma;
        this.fMin = fMin;
        this.fMax = fMax;
        this.rMax = rMax;
        this.rMin = rMin;
        this.velocityMin = -1 * (fMax - fMin) / 10;
        this.velocityMax = (fMax - fMin) / 10;
        this.bestbestFitness = Double.MAX_VALUE;
        this.f = f;
        population = new Bat[populationSize];
        random = new Random();
    }

    void initializePopulation() {
        for (int i = 0; i < populationSize; i++) {
            population[i] = new Bat(dimension);
            for (int j = 0; j < dimension; j++) {
                if (i == 0) {
                    bestBat = population[i];
                }
                population[i].position[j] = random.nextDouble(fMax - fMin + 1) + fMin; // Initialize position randomly
                population[i].velocity[j] = 0;
                calculateFitness(population[i]);
                checkPosition(population[i]);
            }
        }
    }

    void updateBat(Bat bat, int iteration) {
        bat.frequency = minFrequency + (maxFrequency - minFrequency) * random.nextDouble();

        // Update bat's position
        for (int i = 0; i < dimension; i++) {
            if (bat.position[i] > 0 & bestBat.position[i] > 0)  bat.velocity[i] += (bat.position[i] - bestBat.position[i]) * bat.frequency;
            else if (bat.position[i] > 0 & bestBat.position[i] < 0) bat.velocity[i] += ((bat.position[i] * -1) - bestBat.position[i]) * bat.frequency;
            else if (bat.position[i] < 0 & bestBat.position[i] > 0) bat.velocity[i] += (bat.position[i] - bestBat.position[i]) * -1 * bat.frequency;
            else if (bat.position[i] < 0 & bestBat.position[i] < 0) bat.velocity[i] += (bat.position[i] - (bestBat.position[i] * -1)) * bat.frequency;
            if (bat.velocity[i] > velocityMax) bat.velocity[i] = velocityMax;
            if (bat.velocity[i] < velocityMin) bat.velocity[i] = velocityMin;

            bat.position[i] += bat.velocity[i];
            if (bat.position[i] > fMax) bat.position[i] = fMax;
            if (bat.position[i] < fMin) bat.position[i] = fMin;
        }

        Random random = new Random();
        if (random.nextDouble() < bat.pulseRate) {
            for (int i = 0; i < dimension; i++) {
                bat.position[i] += bat.position[i] + ((random.nextDouble() * 2 - 1) * calculateAverageVolume());
            }
        }

        calculateFitness(bat);
        if (random.nextDouble() < bat.volume & bat.fitness < bestBat.fitness) {
            bat.volume = alpha * bat.volume;
            bat.pulseRate = random.nextDouble() * (1 - Math.exp(-1 * gamma * iteration));
            calculateFitness(bat);
        }

        checkPosition(bat);
    }

    double calculateAverageVolume () {
        double res = 0;
        for (int i = 0; i < populationSize; i++) {
            res += population[i].volume;
        }
        return  res / populationSize;
    }
    void calculateFitness (Bat bat) {
        bat.fitness = f.count(bat.position);
    }

    void checkPosition(Bat bat) {
        if (bat.fitness < bestBat.fitness) {
            bestBat = bat;
        }
        if (bat.fitness < bestbestFitness) {
            bestbestFitness = bat.fitness;
        }
    }

    double run() {
        initializePopulation();

        for (int t = 0; t < iterations; t++) {
            for (int i = 0; i < populationSize; i++) {
                updateBat(population[i], t);
            }
        }
        return bestbestFitness;
    }
}

