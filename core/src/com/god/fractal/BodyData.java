package com.god.fractal;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.CatmullRomSpline;
import com.badlogic.gdx.math.Vector2;

public class BodyData {
    String type;
    Sprite texture;
    CatmullRomSpline<Vector2> path;
    float speed;
    float progress;
    Vector2 targetPosition; //the position to go to the next frame

    public BodyData(String ty, Sprite te){
        type = ty;
        texture = te;
    }
    public BodyData(String ty, Sprite te, CatmullRomSpline<Vector2> path, float s ){
        type = ty;
        texture = te;
        this.path = path;
        speed = s;
        progress = 0;
        targetPosition = new Vector2();
    }
    public void addProgress(float delta){
        this.progress += speed*delta;
    }
    public Vector2 getVelocity(Vector2 position){
        path.valueAt(targetPosition, progress);
        return new Vector2(targetPosition).sub(position);
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
}
