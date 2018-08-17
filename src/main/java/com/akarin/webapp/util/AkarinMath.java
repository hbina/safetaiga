package com.akarin.webapp.util;

public class AkarinMath {

    private static double generalizedLNorm(double[] numbers, double p) {
        double result = 0f;

        for (int a = 0; a < numbers.length; a++) {
            numbers[a] = Math.abs(numbers[a]);
            result += Math.pow(numbers[a], p);
        }

        result = Math.pow(result, 1 / p);
        return result;
    }

    private static double euclideanNorm(double[] numbers) {
        return generalizedLNorm(numbers, 2);
    }

    public static double euclideanNorm(int[] numbers) {
        double[] convert = new double[numbers.length];

        for (int a = 0; a < numbers.length; a++) {
            convert[a] = (double) numbers[a];
        }
        return euclideanNorm(convert);
    }

    public static double generalizedLNorm(int[] numbers, double p) {
        double[] convert = new double[numbers.length];

        for (int a = 0; a < numbers.length; a++) {
            convert[a] = (double) numbers[a];
        }
        return generalizedLNorm(convert, p);
    }
}
