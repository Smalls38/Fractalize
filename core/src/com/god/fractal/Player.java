package com.god.fractal;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Player extends Entity {
    public Sprite hitbox;
    public float speed = 500;
    public boolean invincibility = false;
    public Player(Texture img, Texture img2){
        image = new Sprite(img);
        hitbox = new Sprite(img2);
        position = new Vector2(Gdx.graphics.getWidth()/2,0);
    }
    public void Update(float deltaTime){
        if(Gdx.input.isKeyPressed(Input.Keys.W)){
            position.y+=deltaTime*speed;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.S)){
            position.y-=deltaTime*speed;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.A)){
            position.x-=deltaTime*speed;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.D)){
            position.x+=deltaTime*speed;
        }
    }
    public void Draw(SpriteBatch batch){
        Update(Gdx.graphics.getDeltaTime());
        image.setPosition(position.x, position.y);
        hitbox.setPosition(position.x+(image.getWidth()/2)-hitbox.getWidth()/2, position.y+(image.getHeight()/2)- hitbox.getHeight()/2);
        image.draw(batch);
        hitbox.draw(batch);
    }

}
