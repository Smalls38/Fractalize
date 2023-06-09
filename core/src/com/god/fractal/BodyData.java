package com.god.fractal;

import com.badlogic.gdx.graphics.g2d.Sprite;

public class BodyData {
    String type;
    Sprite texture;

    public BodyData(String ty, Sprite te){
        type = ty;
        texture = te;
    }
    public String getType() {
        return type;
    }
    public Sprite getTexture(){
        return texture;
    }
}
