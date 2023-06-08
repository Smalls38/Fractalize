package com.god.fractal;

import com.badlogic.gdx.math.CatmullRomSpline;
import com.badlogic.gdx.math.Vector2;

public abstract class Enemy extends Entity{
        public CatmullRomSpline<Vector2> Catmull;
        Vector2 out;
        float speed;
        float current;
}
