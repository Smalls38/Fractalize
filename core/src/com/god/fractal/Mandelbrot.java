package com.god.fractal;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class Mandelbrot {
    public int maxIterations;
    public int steps;
    public Mandelbrot(int max){
        maxIterations = max;

    }
    public Vector2[] mandlePoints(ComplexNum z0) {
        steps = 0;
        ArrayList<Vector2> points = new ArrayList<>();
        calculate(z0, z0, points);
        return points.toArray(new Vector2[steps]);
    }
    public void calculate(ComplexNum z, ComplexNum z0, ArrayList<Vector2> points){
        if (steps != maxIterations && z0.abs() < 5) {
            System.out.println("right now the imaginary is " + z0.imagine);
            steps++;
            z = z.times(z);
            z = z.plus(z0);
            points.add(new Vector2((float) z.real, (float) z.imagine));
            System.out.println(z.real + ", " + z.imagine);
            calculate(z, z0, points);
        }
    }
}
