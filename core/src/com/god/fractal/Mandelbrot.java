package com.god.fractal;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class Mandelbrot {
    public int maxIterations;
    public int steps;
    public Vector2 scaling;
    public Vector2 initialDisplacement;
    public Vector2 finalDisplacement;

    /**
     * why did I not use vector2 class this for the method parameters? i dont know to be honest now seeing how every thing i name sucks
     * @param max
     * @param finalWidth
     * @param finalHeight
     * @param initialWidth
     * @param initialHeight
     */
    public Mandelbrot(int max, float finalWidth, float finalHeight, float initialWidth, float initialHeight, Vector2 initialDisplacement, Vector2 finalDisplacement){
        maxIterations = max;
        scaling = new Vector2(initialWidth/finalWidth, initialHeight/finalHeight);
        this.initialDisplacement = initialDisplacement;
        this.finalDisplacement = finalDisplacement;
    }
    public Vector2[] mandlePoints(ComplexNum z0) {
        System.out.println("complex number is now " + z0.real + " " + z0.imagine);
        steps = 0;
        ArrayList<Vector2> points = new ArrayList<>();
        z0 = new ComplexNum((z0.real - initialDisplacement.x)/scaling.x + finalDisplacement.x, (z0.imagine - initialDisplacement.y )/scaling.y + finalDisplacement.y);
        points.add(new Vector2((float) z0.real, (float) z0.imagine));
        System.out.println("scaling is " + scaling.x + "," + scaling.y);
        System.out.println("complex number is now " + z0.real + " " + z0.imagine);
        calculate(z0, z0, points);
        Vector2[] result = new Vector2[points.size()];
        result = points.toArray(result);
        return result;
    }
    public void calculate(ComplexNum z, ComplexNum z0, ArrayList<Vector2> points){
        if (steps != maxIterations && z.abs() < 30) {
            System.out.println("z abs is " +  z.abs());
            steps++;
            z = z.times(z);
            z = z.plus(z0);
            points.add(new Vector2((float) z.real * 5, (float) z.imagine * 5));
            System.out.println(((float) z.real - finalDisplacement.x) * scaling.x + initialDisplacement.x + " " + ((float) z.imagine - finalDisplacement.y) * scaling.y + initialDisplacement.y);
            calculate(z, z0, points);
        }
    }
}
