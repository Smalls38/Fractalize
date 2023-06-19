package Entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public abstract class Entity {
    public Vector2 position; // Using a vector class to represent the position of the entity, it's just for convenience’s sake.
    public Sprite image; //the displayed sprite of the entity
    public Vector2 imageSize;
    public Body body; //
    public BodyDef gdef; //type of body
    public FixtureDef fdef;
    public float health ; //health of object

}
