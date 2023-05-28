package com.god.fractal;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;

public abstract class Entity {
    public Vector2 position; // Using a vector class to represent the position of the entity, it's just for convenience’s sake.
    public Sprite image; //the displayed sprite of the entity
    public float animationTime; //how long the Entity exists for
    public boolean permanent;  //if the entity disappears after animationTime permanent
    public Body body; //
    public BodyDef def; //type of body

}
