package com.god.fractal;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.god.fractal.Screens.PlayScreen;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Player extends Entity {
    public Sprite hitbox;
    public Vector2 hitboxPos;
    public Vector2 hitboxSize;
    public float hitboxRadius = 6f;
    public float speed = 15;
    public boolean invincibility = false;
    public PlayScreen screen;
    public float PPM;
    public Player(Texture img, Texture img2, PlayScreen screen){
        image = new Sprite(img);
        hitbox = new Sprite(img2);

        this.screen = screen;
        PPM = screen.game.PPM;

        //position of the sprite
        position = new Vector2(659/PPM,30/PPM);
        imageSize = new Vector2(image.getWidth()/PPM,image.getHeight()/PPM);

        hitboxSize = new Vector2(hitbox.getWidth()/PPM, hitbox.getHeight()/PPM);
        hitboxPos = new Vector2((position.x+(imageSize.x/2)-hitboxSize.x/2),(position.y+(imageSize.y/2)- hitboxSize.y/2));

        image.setSize(image.getWidth()/PPM,image.getHeight()/PPM);

        //defining the Box2d body
        def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody;
        def.fixedRotation = true; //its A SPHERE IT DOESNT MATTER IF IT ROTATE
        def.position.set(hitboxPos.x + hitboxSize.x/2, hitboxPos.y + hitboxSize.y/2);
        body = screen.world.createBody(def);

        //setting player hitbox with a circle
        CircleShape circle = new CircleShape();
        circle.setRadius(hitboxRadius/PPM);

        body.createFixture(circle, 1f);
        circle.dispose();
    }
    public void Update(float deltaTime){
        if(Gdx.input.isKeyPressed(Input.Keys.W)){
            position.y+=deltaTime*speed;
            hitboxPos.y+=deltaTime*speed;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.S)){
            position.y-=deltaTime*speed;
            hitboxPos.y-=deltaTime*speed;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.A)){
            position.x-=deltaTime*speed;
            hitboxPos.x-=deltaTime*speed;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.D)){
            position.x+=deltaTime*speed;
            hitboxPos.x+=deltaTime*speed;
        }
    }
    public void Draw(SpriteBatch batch){
        Update(Gdx.graphics.getDeltaTime());
        batch.draw(image, position.x, position.y, imageSize.x, imageSize.y );
        batch.draw(hitbox, hitboxPos.x, hitboxPos.y, hitboxSize.x, hitboxSize.y);
    }

}
