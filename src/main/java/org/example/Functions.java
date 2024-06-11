package org.example;

import java.util.ArrayList;

public class Functions {
    public interface function {
        double count (double[] x);
    }

    public interface functionOne {
        double count (double x);
    }

    public static double Sphere_Function(double[] x) {                     //globalny minimum: 0
        double res = 0;
        for (int i = 0; i < x.length; i++) {
            res += Math.pow(x[i], 2);
        }
        return res;
    }

    public static double Sphere_Function(double x) {
        return Math.pow(x, 2);
    }

    public static double Ackley_Function(double[] x) {                         //globalny minimum: 0
        double res = 0;
        double n = x.length;
        double secondPart = 0;
        for (int i = 0; i < x.length; i++) {
            secondPart += Math.cos(2 * Math.PI * x[i]);
        }

        res = -20 * Math.exp(-0.2 * Math.sqrt(1/n * Sphere_Function(x))) - Math.exp(1/n * secondPart) + 20 + Math.E;

        return res;
    }

    public static double Ackley_Function(Double x) {
        double res = 0;
        double n = 1;
        double secondPart = Math.cos(2 * Math.PI * x);

        res = -20 * Math.exp(-0.2 * Math.sqrt(1/n * Sphere_Function(x))) - Math.exp(1/n * secondPart) + 20 + Math.E;

        return res;
    }

    public static double Cigar(double[] x) {
        double res = 0;
        for (int i = 1; i < x.length; i++) {
            res += Math.pow(x[i], 2);
        }
        return Math.pow(x[0], 2) + (1000000 * res);
    }

    public static double Cigar(Double x) {
        return Math.pow(x, 2);
    }

    public static double Schwefel_Function(double[] x) {
        double res1 = Math.abs(x[0]);
        double res2 = Math.abs(x[0]);
        for (int i = 1; i < x.length; i++) {
            res1 += Math.abs(x[i]);
            res2 *= Math.abs(x[i]);
        }
        return res1 + res2;
    }

    public static double Schwefel_Function(Double x) {
        return x;
    }

    public static double Zakharov_Function(double[] x) {                           //globalny minimu: 0
        double res1 = 0, res2 = 0, res3 = 0;
        for (int i = 0; i < x.length; i++) {
            res1 += Math.pow(x[i], 2);
            res2 += (double) i / 2 * x[i];
            res3 += (double) i / 2 * x[i];
        }
        return res1 + Math.pow(res2, 2) + Math.pow(res3, 4);
    }

    public static double Zakharov_Function(Double x) {
        double res1 = 0, res2 = 0, res3 = 0;
        res1 += Math.pow(x, 2);
        res2 += (double) 0 / 2 * x;
        res3 += (double) 0 / 2 * x;
        return -res1 + Math.pow(res2, 2) + Math.pow(res3, 4);
    }

    public static double Step_Function(double[] x) {
        double res = 0;
        for (int i = 0; i < x.length; i++) {
            res += Math.pow(x[i] + 0.5, 2);
        }
        return res;
    }

    public static double Step_Function(Double x) {
        return Math.pow(x + 0.5, 2);
    }
}
