package com.god.fractal;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class Mandelbrot {
    public int maxIterations; // the max number of times it can go through the formula
    public double maxDistance; // the max distance the point can be from 0 + 0i

    public Vector2 scaling; // the vertical and horizontal scaling to have initial rectangle to be the same size as the final
    public Vector2 initialDisplacement; // the displacement required to have the points to be anchored in the bottom left corner of the initial rectangle
    public Vector2 finalDisplacement; // the displacement required to have the points to be anchored in the bottom left corner of the final rectangle

    /**
     * class for iterating complex numbers according to the mandelbrot formula with mapping cordinates from a rectangle to an area on the mandelbrot set.
     * tho I will be not using the mapping properly because I want somewhat random behavior, mapping it properly sometimes gives too chaotic or calm behavior.
     * the box2d library can't really handle lightspeed objects so, I will just have the parameters so that it's random and chaotic enough but not accurate to how it
     * should be
     */
    public Mandelbrot(int maxDistance, int maxIteration,
                      float finalWidth, float finalHeight,
                      float initialWidth, float initialHeight,
                      Vector2 initialDisplacement, Vector2 finalDisplacement){
        this.maxIterations = maxIteration; // set the max iterations
        this.maxDistance = maxDistance;
        scaling = new Vector2(initialWidth/finalWidth, initialHeight/finalHeight);
        this.initialDisplacement = initialDisplacement;
        this.finalDisplacement = finalDisplacement;
    }
    public Vector2[] mandlePoints(ComplexNum z0) {
        int steps = 0;
        ArrayList<Vector2> points = new ArrayList<>(); //arraylist to store the points
        //add the first point to the list, not going to add transformations so even if i change the parameters it will start wherever i want it to be
        points.add(new Vector2((float) z0.real, (float) z0.imagine));
        z0 = new ComplexNum((z0.real - initialDisplacement.x)/scaling.x + finalDisplacement.x, (z0.imagine - initialDisplacement.y )/scaling.y + finalDisplacement.y);
        calculate(z0, z0, points, steps); // calculate all the points using recursion, it can be easily done with a forloop but nawwwww
        Vector2[] result = new Vector2[points.size()];
        result = points.toArray(result); //put all the points into an array so it can become a spline path
        return result;
    }
    public void calculate(ComplexNum z, ComplexNum z0, ArrayList<Vector2> points, int steps){
        if (steps != maxIterations && z.abs() < maxDistance) { //if the point is not iterating beyond the max
            steps++; //increment the point
            z = z.times(z); // do the mandelbrot :emoji of my head exploding from the sheer amount of information:
            z = z.plus(z0);
            //put the point into the thingy
            points.add(new Vector2(((float) z.real - finalDisplacement.x) * scaling.x + initialDisplacement.x, ((float) z.imagine - finalDisplacement.y) * scaling.y + initialDisplacement.y));
            calculate(z, z0, points, steps); //iterate
        }
    }
}
