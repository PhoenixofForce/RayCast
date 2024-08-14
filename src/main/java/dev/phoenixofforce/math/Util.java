package dev.phoenixofforce.math;

public class Util {

    public static double map(double a, double f1, double t1, double f2, double t2) {
        return ((a - f1) / (t1 - f1)) * (t2 - f2) + f2;
    }

}
