package com.god.fractal;

/**
 * complex number class i had to write for the fractals n stuff
 * its hella barebones since i only wrote the part i need for mandelbrot
 */
public class ComplexNum {
    public final double real;
    public final double imagine;

    public ComplexNum(double real, double imagine){
        this.real = real;
        this.imagine = imagine;
    }
    public double abs(){
        return Math.sqrt(real*real + imagine*imagine); //abs value of complex number is distance from origin
    }
    public ComplexNum times(ComplexNum num){
        return new ComplexNum((real * num.real) - (imagine * num.imagine),
                (real * num.imagine) + (num.real * imagine));
    }
    public ComplexNum plus (ComplexNum num){
        return new ComplexNum(real + num.real, imagine + num.imagine);
    }
}
