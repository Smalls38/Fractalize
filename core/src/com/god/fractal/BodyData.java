package com.god.fractal;

import Entities.EnemyBullet;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.CatmullRomSpline;
import com.badlogic.gdx.math.Vector2;

/**
 * class object to store every information needed for a body to be drawn and move n all
 */

public class BodyData {
    String type; //the type of the body, so I know when to dispose of it and how to move it
    Sprite texture; //the image of the body woaw!
    CatmullRomSpline<Vector2> path; //path of the body to follow, if its null then the body will just be removed after it reached a point
    float speed; // how fast the body should complete the path
    float progress; //how much of the path it has finished
    float health;
    float damage;
    Vector2 targetPosition; //the position to go to the next frame
    EnemyBullet bulletType;
    Cooldown timer;

    public BodyData(String ty, Sprite te, float hp, float dmg){
        type = ty;
        texture = te;
        health = hp;
        this.damage = dmg;

    }
    public BodyData(String ty){
        type = ty;
    }
    public BodyData(String ty, Sprite te, CatmullRomSpline<Vector2> path, float s, float hp, float dmg){
        type = ty;
        texture = te;
        this.path = path;
        speed = s;
        progress = 0;
        targetPosition = new Vector2();
        health = hp;
        this.damage = dmg;
    }
    public BodyData(String ty, Sprite te, CatmullRomSpline<Vector2> path, float s, float hp, float dmg,
                    EnemyBullet bulletType, Cooldown timer){
        type = ty;
        texture = te;
        this.path = path;
        speed = s;
        progress = 0;
        targetPosition = new Vector2();
        health = hp;
        this.damage = dmg;
        this.bulletType = bulletType;
        this.timer = timer;
    }
    public void addProgress(float delta){
        this.progress += speed*delta;
    }
    public Vector2 getVelocity(Vector2 position){
        path.valueAt(targetPosition, progress);
        return new Vector2(targetPosition).sub(position);
    }
    public void takeDamage(float damage){
        health -= damage;
    }
    public String getType() {
        return type;
    }
    public Sprite getTexture(){
        return texture;
    }
    public CatmullRomSpline<Vector2> getPath() {
        return path;
    }


    public float getSpeed() {
        return speed;
    }

    public float getProgress() {
        return progress;
    }

    public float getHealth() {
        return health;
    }

    public float getDamage() {
        return damage;
    }

    public Vector2 getTargetPosition() {
        return targetPosition;
    }

    public EnemyBullet getBulletType() {
        return bulletType;
    }

    public Cooldown getTimer() {
        return timer;
    }
}
