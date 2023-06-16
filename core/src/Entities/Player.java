package Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.CatmullRomSpline;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import com.god.fractal.ComplexNum;
import com.god.fractal.Cooldown;
import com.god.fractal.Mandelbrot;
import com.god.fractal.Screens.PlayScreen;
import com.god.fractal.BodyData;

import java.util.ArrayList;

public class Player extends Entity {
    public Sprite hitbox; //sprite of hitbox
    public Vector2 hitboxSize; //size of hitbox sprite
    public Vector2 velocity;
    public PlayerBullet swordPhantoms;
    public float bulletVelocity_swordPhantoms = 138;
    public PlayerBullet blossoms;
    public float bulletVelocity_blossoms = 38;
    public PlayerBullet mandellingbrots;
    public float hitboxRadius = 6; //radius of hitbox
    public float maxSpeed = 18; //max speed of player and hitbox
    public float speed = 0; //speed of player
    public float power = 0; //power of player, more bullets will exist if player has higher power
    public boolean invincibility = false; //when player is not affacted by any collisions
    public boolean dead = false; //when player is dead
    public boolean focus = false;
    public ArrayList<PlayerBullet> bullets = new ArrayList<>();

    public PlayScreen screen;
    public float PPM;
    public short PLAYER_WORLD = 0x0002;
    public short PLAYER_BULLETS = 0x0004;
    public short ENEMY_WORLD;
    public float unfocusBulletCooldown = 0.05f;
    public float focusBulletCooldown = 0.15f;
    public float powerGainCooldown = 0.5f;
    public Cooldown cooldowns = new Cooldown(new float[]{unfocusBulletCooldown, powerGainCooldown, focusBulletCooldown});
    Texture swordTexture = new Texture("swordPhantom.png");
    Texture blossomTexture = new Texture("blossom.png");
    Texture mandelTexture = new Texture("fractalBullet.png");
    Mandelbrot brot;

    public Player(Texture img, Texture img2, PlayScreen screen, short WORLD_UI) {
        image = new Sprite(img);
        hitbox = new Sprite(img2);
        health = 100;

        this.screen = screen;
        PPM = screen.game.PPM;

        //position of the sprite
        position = new Vector2(659 / PPM, 60 / PPM);
        imageSize = new Vector2(image.getWidth() / PPM, image.getHeight() / PPM);

        hitboxSize = new Vector2(hitbox.getWidth() / PPM, hitbox.getHeight() / PPM);

        image.setSize(image.getWidth() / PPM, image.getHeight() / PPM);

        //defining the Box2d body
        gdef = new BodyDef();

        gdef.type = BodyDef.BodyType.DynamicBody;
        gdef.fixedRotation = true; //its A SPHERE IT DOESNT MATTER IF IT ROTATE
        gdef.position.set((position.x + (imageSize.x / 2) - hitboxSize.x / 2) + hitboxSize.x / 2, (position.y + (imageSize.y / 2) - hitboxSize.y / 2) + hitboxSize.y / 2);
        body = screen.world.createBody(gdef);

        //setting player hitbox with a circle
        CircleShape circle = new CircleShape();
        circle.setRadius(hitboxRadius / PPM);

        fdef = new FixtureDef();
        fdef.filter.categoryBits = PLAYER_WORLD;
        fdef.filter.maskBits = (short) (ENEMY_WORLD | WORLD_UI); // what bodies it will collide with
        fdef.shape = circle;
        body.createFixture(fdef);
        circle.dispose(); //removing the circle from memory

        initializeBullet();

        ENEMY_WORLD = screen.ENEMY_WORLD;

        //brot = new Mandelbrot(50, 0.5f, 0.5f, 874/PPM, 1044/PPM, new Vector2(222/PPM, 18/PPM), new Vector2(-1.5f, -1.1f));
        brot = new Mandelbrot(30, 50, 1.5f, 1.5f, 874/PPM, 1044/PPM, new Vector2(222/PPM, 18/PPM), new Vector2(-1.5f, -1.1f));
        Vector2[] test = brot.mandlePoints(new ComplexNum(4.0625,4.0625));
        for (int i = 0; i < test.length; i++) {
            //System.out.println(test[i].x + " " + test[i].y);
        }
    }

    public void Update(float delta) {
        cooldowns.update(delta);
        velocity = new Vector2(0, 0);
        if (cooldowns.isOver(1) && power <= 3) {
            power += 0.1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            velocity.y += 1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            velocity.y += -1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            velocity.x += -1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            velocity.x += 1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.J) && cooldowns.isOver(0)) {
            if (!Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                if (power >= 1) {
                    blossoms.makeBullet(new Vector2(body.getPosition().x, body.getPosition().y + imageSize.y), screen, new Vector2(0, bulletVelocity_blossoms));
                }
                if (power >= 2) {
                    blossoms.makeBullet(new Vector2(body.getPosition().x + imageSize.x, body.getPosition().y + imageSize.y), screen, new Vector2(bulletVelocity_blossoms / 2, bulletVelocity_blossoms));
                    blossoms.makeBullet(new Vector2(body.getPosition().x - imageSize.x, body.getPosition().y + imageSize.y), screen, new Vector2(-bulletVelocity_blossoms / 2, bulletVelocity_blossoms));
                }
                if (power >= 3) {
                    blossoms.makeBullet(new Vector2(body.getPosition().x + imageSize.x, body.getPosition().y - imageSize.y), screen, new Vector2(bulletVelocity_blossoms, bulletVelocity_blossoms));
                    blossoms.makeBullet(new Vector2(body.getPosition().x - imageSize.x, body.getPosition().y - imageSize.y), screen, new Vector2(-bulletVelocity_blossoms, bulletVelocity_blossoms));
                }
            }
            swordPhantoms.makeBullet(new Vector2(body.getPosition().x, body.getPosition().y + imageSize.y), screen, bulletVelocity_swordPhantoms);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
            focus = true;
            speed = maxSpeed / 2;
            if (Gdx.input.isKeyPressed(Input.Keys.J)&& cooldowns.isOver(2)){
                mandellingbrots.makeFractalBullet(screen, new CatmullRomSpline<>(brot.mandlePoints(new ComplexNum(body.getPosition().x, body.getPosition().y)), true), 0.2f);
                for (int i = 0; i < (int) power; i++) {
                    mandellingbrots.makeFractalBullet(screen, new CatmullRomSpline<>(brot.mandlePoints(new ComplexNum(Math.random()*38, Math.random()*38)), true), 0.1f);
                }
            }
        } else {
            focus = false;
            speed = maxSpeed;
        }
        body.setLinearVelocity(speed * velocity.x, speed * velocity.y);
    }

    public void Draw(SpriteBatch batch, float delta) {
        Update(delta);

        //System.out.println("player is at " + body.getPosition().y + "," + body.getPosition().y);
        batch.draw(image, body.getPosition().x - imageSize.x / 2, body.getPosition().y - imageSize.y / 2, imageSize.x, imageSize.y);

        if (focus) {
            batch.draw(hitbox, body.getPosition().x - hitboxSize.x / 2, body.getPosition().y - hitboxSize.y / 2, hitboxSize.x, hitboxSize.y);
        }
    }

    public void initializeBullet() {
        swordPhantoms = new PlayerBullet(ENEMY_WORLD, PLAYER_BULLETS, BodyDef.BodyType.KinematicBody, new Sprite(swordTexture), new Vector2(swordTexture.getWidth() / PPM, swordTexture.getHeight() / PPM), 50, 100);
        blossoms = new PlayerBullet(ENEMY_WORLD, PLAYER_BULLETS, BodyDef.BodyType.KinematicBody, new Sprite(blossomTexture), new Vector2(blossomTexture.getWidth() / PPM, blossomTexture.getHeight() / PPM), 25, 150);
        mandellingbrots = new PlayerBullet(ENEMY_WORLD, PLAYER_BULLETS, BodyDef.BodyType.KinematicBody, new Sprite(mandelTexture), new Vector2(mandelTexture.getWidth() / PPM, mandelTexture.getHeight() / PPM), 10, 50);
    }


}