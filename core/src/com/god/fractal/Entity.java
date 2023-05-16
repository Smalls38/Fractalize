package com.god.fractal;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector2;

public abstract class Entity {
    public Vector2 position; // Using a vector class to represent the position of the entity, it's just for convenience’s sake.
    public Sprite image; //the displayed sprite of the entity
    public float animationTime;

}
