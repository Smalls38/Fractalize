package com.god.fractal;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.god.fractal.Screens.PlayScreen;

public class Player extends Entity {
    public Sprite hitbox; //sprite of hitbox
    public Vector2 hitboxSize; //size of hitbox sprite
    public Vector2 velocity;
    public PlayerBullet  swordPhantoms;
        public float bulletVelocity = 30;
    public PlayerBullet  focusSlash;
    public float hitboxRadius = 6; //radius of hitbox
    public float maxSpeed = 18; //max speed of player and hitbox
    public float speed = 0; //speed of player
    public float health = 100; //health of player
    public boolean invincibility = false; //when player is not affacted by any collisions
    public boolean dead = false; //when player is dead
    public boolean focus = false;

    public PlayScreen screen;
    public float PPM;
    public short PLAYER_WORLD = 0x0002;
    public short PLAYER_BULLETS = 0x0003;
    public short ENEMY_WORLD = 0x0004;

    public Player(Texture img, Texture img2, PlayScreen screen){
        image = new Sprite(img);
        hitbox = new Sprite(img2);

        this.screen = screen;
        PPM = screen.game.PPM;

        //position of the sprite
        position = new Vector2(659/PPM,60/PPM);
        imageSize = new Vector2(image.getWidth()/PPM,image.getHeight()/PPM);

        hitboxSize = new Vector2(hitbox.getWidth()/PPM, hitbox.getHeight()/PPM);

        image.setSize(image.getWidth()/PPM,image.getHeight()/PPM);

        //defining the Box2d body
        gdef = new BodyDef();

        gdef.type = BodyDef.BodyType.DynamicBody;
        gdef.fixedRotation = true; //its A SPHERE IT DOESNT MATTER IF IT ROTATE
        gdef.position.set((position.x+(imageSize.x/2)-hitboxSize.x/2) + hitboxSize.x/2, (position.y+(imageSize.y/2)- hitboxSize.y/2) + hitboxSize.y/2);
        body = screen.world.createBody(gdef);

        //setting player hitbox with a circle
        CircleShape circle = new CircleShape();
        circle.setRadius(hitboxRadius/PPM);

        fdef = new FixtureDef();
        fdef.filter.categoryBits = PLAYER_WORLD;
        fdef.filter.maskBits = 0x0001;
        fdef.shape = circle;
        body.createFixture(fdef);
        circle.dispose();


        initializeBullet();
    }
    public void Update(float deltaTime){
        velocity = new Vector2(0,0);
        if(Gdx.input.isKeyPressed(Input.Keys.W)){
            velocity.y += 1;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.S)){
            velocity.y += -1;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.A)){
            velocity.x += -1;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.D)){
            velocity.x += 1;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.J)){
            swordPhantoms.makeBullet(body.getPosition(), screen, bulletVelocity);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)){
            focus = true;
            speed = maxSpeed/2;
        } else {
            focus = false;
            speed = maxSpeed;
        }
        body.setLinearVelocity(speed*velocity.x, speed*velocity.y);
        for (int i = 0; i < screen.world.getBodyCount(); i++) {
            //Body temp = screen.world.get(i);
            //hey future simon, make a first in first out data type to get rid of the bullets when its past the bounnds
        }
    }
    public void Draw(SpriteBatch batch){
        Update(Gdx.graphics.getDeltaTime());

        batch.draw(image, body.getPosition().x-imageSize.x/2, body.getPosition().y-imageSize.y/2, imageSize.x, imageSize.y );
        if (focus) {
            batch.draw(hitbox, body.getPosition().x - hitboxSize.x / 2, body.getPosition().y - hitboxSize.y / 2, hitboxSize.x, hitboxSize.y);
        }
    }
    public void initializeBullet(){
        Texture sword = new Texture("swordPhantom.png");
        swordPhantoms = new PlayerBullet(ENEMY_WORLD, PLAYER_BULLETS, BodyDef.BodyType.KinematicBody, sword,new Vector2(sword.getWidth()/PPM, sword.getHeight()/PPM));
    }

}
