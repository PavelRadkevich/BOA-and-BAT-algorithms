package org.example;

import java.awt.*;
import java.util.Random;
import org.apache.commons.math3.special.Gamma;

public class BOAwithLevyFlightAlgorithm {
    int populationSize;
    int dimension;
    int iterations;
    double p;
    double a;
    double c;
    double lambda;
    double F;
    int CR;
    double P;
    double fMin;
    double fMax;
    Functions.function f;
    double bestFitness;
    Butterfly bestButterfly;
    Butterfly[] population;
    Random random;

    BOAwithLevyFlightAlgorithm(int populationSize,
                               double p, double c, double lambda, double F, int CR, double P, double fMin, double fMax,
                               int dimension, int iterations, Functions.function f) {
        this.populationSize = populationSize;
        this.dimension = dimension;
        this.iterations = iterations;
        this.p = p;
        this.c = c;
        this.lambda = lambda;
        this.F = F;
        this.CR = CR;
        this.P = P;
        this.fMin = fMin;
        this.fMax = fMax;
        this.f = f;
        this.bestFitness = Double.MAX_VALUE;
        random = new Random();
    }

    double levyFlight() {
        double sigma = Math.pow((Gamma.gamma(1 + lambda) * Math.sin(Math.PI * lambda / 2)) /
                (Gamma.gamma((1 + lambda) / 2) * lambda * Math.pow(2, (lambda - 1) / 2)), 1 / lambda);
        double u = random.nextGaussian() * sigma * sigma;
        double v = random.nextGaussian();
        return u / (Math.pow(Math.abs(v), 1 / lambda));
    }

    private void moveToTheBest(Butterfly butterfly, int dimension) {
        double direction = bestButterfly.position[dimension] - butterfly.position[dimension];

        double displacement = Math.abs(levyFlight()) * direction * butterfly.fragrance;

        butterfly.position[dimension] += 0.03 * displacement;

        if (butterfly.position[dimension] > fMax) butterfly.position[dimension] = fMax;
        if (butterfly.position[dimension] < fMin) butterfly.position[dimension] = fMin;
    }

    private void moveToRandom(Butterfly butterfly, int dimension, int j, int k) {
        double direction = population[j].position[dimension] - population[k].position[dimension];
        butterfly.position[dimension] += 0.03 * (Math.abs(levyFlight()) * direction * butterfly.fragrance);

        if (butterfly.position[dimension] > fMax) butterfly.position[dimension] = fMax;
        if (butterfly.position[dimension] < fMin) butterfly.position[dimension] = fMin;
    }

    private Butterfly[] initialize() {
        Butterfly[] newPopulation = new Butterfly[populationSize];
        for (int i = 0; i < populationSize; i++) {
            newPopulation[i] = new Butterfly(dimension);
            for (int j = 0; j < dimension; j++) {
                Random random = new Random();
                newPopulation[i].position[j] = random.nextDouble() * (fMax - fMin) + fMin;
            }
        }
        return newPopulation;
    }

    private void updateFragrance() {
        for (int i = 0; i < populationSize; i++) {
            population[i].fragrance = c * (Math.pow(f.count(population[i].position), a));
        }
    }

    private void updateBest() {
        double temp = Double.MAX_VALUE;
        for (int i = 0; i < populationSize; i++) {
            double fitness = f.count(population[i].position);
            if (fitness < temp) {
                bestButterfly = population[i];
                temp = fitness;
                if (fitness < bestFitness) {
                    bestFitness = fitness;
                }
            }
        }
    }

    private void selectDEPopulation(Butterfly[] boaPopulation, Butterfly[] dePopulation) {
        for (int i = 0; i < populationSize; i++) {
            if (dePopulation[i].fitness < f.count(boaPopulation[i].position)) {
                // Replace the individual in BOA population with the individual from DE
                if (dimension >= 0)
                    System.arraycopy(dePopulation[i].position, 0, boaPopulation[i].position, 0, dimension);
            }
        }
    }

    private void evolveDE(Butterfly[] dePopulation) {
        for (int i = 0; i < populationSize; i++) {
            dePopulation[i] = new Butterfly(dimension);
            for (int j = 0; j < dimension; j++) {
                dePopulation[i].position[j] = population[i].position[j];
            }
        }

        for (int i = 0; i < populationSize; i++) {
            // Select three random individuals
            int r2, r3;
            do {
                Random random = new Random();
                r2 = random.nextInt(populationSize);
                r3 = random.nextInt(populationSize);
            } while (r2 == i || r3 == i || r2 == r3);

            double[] trialVector = dePopulation[i].position;

            double r = random.nextDouble();
            if (r < P) {
                for (int j = 0; j < dimension; j++) {
                    trialVector[j] = dePopulation[i].position[j] + F * (dePopulation[r2].position[j] - dePopulation[r3].position[j]);
                }
            }


            for (int j = 0; j < dimension; j++) {
                if (random.nextDouble() < CR) {
                    dePopulation[i].position[j] = trialVector[j];
                }
            }
            dePopulation[i].fitness = f.count(dePopulation[i].position);
        }
    }



    double run() {
        population = initialize();
        for (int t = 0; t < iterations; t++) {
            this.a = 0.1 + 0.2 * t / iterations;
            updateFragrance();
            updateBest();
            for (int i = 0; i < populationSize; i++) {
                Random random = new Random();
                double r = random.nextDouble();
                if (r < p) {
                    for (int j = 0; j < dimension; j++) {
                        moveToTheBest(population[i], j);
                    }
                } else {
                    int b2, b3;
                    do {
                        Random random1 = new Random();
                        b2 = random1.nextInt(populationSize);
                        b3 = random1.nextInt(populationSize);
                    } while ((b2 == b3) || (b3 == i) || (b2 == i));

                    for (int j = 0; j < dimension; j++) {
                        moveToRandom(population[i], j, b2, b3);
                    }
                }
            }
            // Evolve DE population
            Butterfly[] dePopulation = new Butterfly[populationSize];
            evolveDE(dePopulation);
            // Select individuals from DE population to replace individuals in BOA population
            selectDEPopulation(population, dePopulation);
        }
        return bestFitness;
    }

}