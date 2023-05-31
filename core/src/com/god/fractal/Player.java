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
import com.badlogic.gdx.utils.Array;
import com.god.fractal.Screens.PlayScreen;

import java.util.ArrayList;

public class Player extends Entity {
    public Sprite hitbox; //sprite of hitbox
    public Vector2 hitboxSize; //size of hitbox sprite
    public Vector2 velocity;
    public PlayerBullet  swordPhantoms;
    public float bulletVelocity = 138;
    public PlayerBullet focusSlash;
    public float hitboxRadius = 6; //radius of hitbox
    public float maxSpeed = 18; //max speed of player and hitbox
    public float speed = 0; //speed of player
    public float health = 100; //health of player
    public boolean invincibility = false; //when player is not affacted by any collisions
    public boolean dead = false; //when player is dead
    public boolean focus = false;
    public ArrayList<PlayerBullet> bullets = new ArrayList<>();

    public PlayScreen screen;
    public float PPM;
    public short PLAYER_WORLD = 0x0002;
    public short PLAYER_BULLETS = 0x0003;
    public short ENEMY_WORLD = 0x0004;
    Texture swordTexture = new Texture("swordPhantom.png");
    public Cooldown cooldowns = new Cooldown(new float[]{0.05f});

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
        cooldowns.update(deltaTime);
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
        if(Gdx.input.isKeyPressed(Input.Keys.J) && cooldowns.isOver(0)){
            bullets.add(swordPhantoms.makeBullet(body.getPosition(), screen, bulletVelocity));
        }
        if(Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)){
            focus = true;
            speed = maxSpeed/2;
        } else {
            focus = false;
            speed = maxSpeed;
        }
        body.setLinearVelocity(speed*velocity.x, speed*velocity.y);
    }
    public void Draw(SpriteBatch batch){
        Update(Gdx.graphics.getDeltaTime());

        Array<Body> bodies = new Array<Body>();
        // Now fill the array with all bodies
        screen.world.getBodies(bodies);

        for (Body b : bodies) {
            // Get the body's user data - in this example, our user
            // data is an instance of the Entity class
            Sprite sprite = (Sprite) b.getUserData();
            if (b.getPosition().y > screen.viewport.getWorldHeight() * 2){
                screen.world.destroyBody(b);
            }
            if (sprite != null) {
                batch.draw(sprite, b.getPosition().x-swordPhantoms.imageSize.x/2, b.getPosition().y-swordPhantoms.imageSize.y/2,
                        swordPhantoms.imageSize.x, swordPhantoms.imageSize.y);
            }
        }

        batch.draw(image, body.getPosition().x-imageSize.x/2, body.getPosition().y-imageSize.y/2, imageSize.x, imageSize.y);

        if (focus) {
            batch.draw(hitbox, body.getPosition().x - hitboxSize.x / 2, body.getPosition().y - hitboxSize.y / 2, hitboxSize.x, hitboxSize.y);
        }
    }
    public void initializeBullet(){
        swordPhantoms = new PlayerBullet(ENEMY_WORLD, PLAYER_BULLETS, BodyDef.BodyType.KinematicBody, new Sprite(swordTexture),new Vector2(swordTexture.getWidth()/PPM, swordTexture.getHeight()/PPM));
    }

}