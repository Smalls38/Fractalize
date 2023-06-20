package Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
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
    public Vector2 velocity; // more of the direction of the player's movement
    public PlayerBullet swordPhantoms; //the main bullet type that the character always have
    final float bulletVelocity_swordPhantoms = 138; //i like the number 138
    public PlayerBullet blossoms;
    final float bulletVelocity_blossoms = 38;
    public PlayerBullet mandellingbrots;
    final float hitboxRadius = 6; //radius of hitbox
    final float maxSpeed = 18; //max speed of player and hitbox
    public float speed = 0; //speed of player
    public float power = 0; //power of player, more bullets will exist if player has higher power
    public boolean invincibility = false; //when player is not affacted by any collisions
    public boolean focus = false; //whether the player is holding shift
    public PlayScreen screen;
    public float PPM;
    public short PLAYER_WORLD;
    public short PLAYER_BULLETS;
    public short ENEMY_WORLD;
    // all the cooldown time (in seconds) for the bullet shooting and power gaining
    final float unfocusBulletCooldown = 0.05f;
    final float focusBulletCooldown = 0.15f;
    final float powerGainCooldown = 0.5f;
    final float invincibilityDuration = 1;
    final float bulletSfxCooldown = 1;
    //the list of things that have cooldowns
    public Cooldown cooldowns = new Cooldown(new float[]{unfocusBulletCooldown, powerGainCooldown, focusBulletCooldown,
            invincibilityDuration, bulletSfxCooldown});
    //all the textures
    Texture swordTexture = new Texture("swordPhantom.png");
    Texture blossomTexture = new Texture("blossom.png");
    Texture mandelTexture = new Texture("fractalBullet.png");
    Sprite invincibilityPlayer = new Sprite(new Texture("player1_immune.png"));
    //the current player changes the sprite when he's hit, so I don't declare it like a final variable
    Sprite currentPlayer;
    Mandelbrot brot; //give the mandelbrot points
    //sound effects
    Sound blossomSound = Gdx.audio.newSound(Gdx.files.internal("assets/blossom_sfx.mp3"));
    Sound fractalSound = Gdx.audio.newSound(Gdx.files.internal("assets/fractal_sfx.mp3"));

    public Player(Texture img, Texture img2, PlayScreen screen) {

        //i hope these are self explanatory
        image = new Sprite(img);
        hitbox = new Sprite(img2);
        health = 100;
        currentPlayer = image;
        this.screen = screen;
        PPM = screen.game.PPM;
        PLAYER_WORLD = screen.PLAYER_WORLD;
        PLAYER_BULLETS = screen.PLAYER_BULLETS;
        ENEMY_WORLD = screen.ENEMY_WORLD;


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


        body.setUserData(new BodyData("player"));
        fdef = new FixtureDef();
        fdef.filter.categoryBits = PLAYER_WORLD;


        fdef.shape = circle;
        body.createFixture(fdef);
        circle.dispose(); //removing the circle from memory

        initializeBullet();
        /*
         the mandelbrot set
         it is supposed to be the playing field mapped to a square starting at -1.5 - 1.1i with side length 5
         */
        brot = new Mandelbrot(30, 50, 1.5f, 1.5f, 874/PPM, 1044/PPM, new Vector2(222/PPM, 18/PPM), new Vector2(-1.5f, -1.1f));
    }
    /*
    this method, unsurprisingly, checks things
     */
    public void Update(float delta) {
        if (health <= 0){
            screen.game.gameOver();
        }
        cooldowns.update(delta);
        if (cooldowns.isOver(1) && power <= 3) {
            power += 0.01;
        }
        //to have the total velocity the player should have at the end, its done this way so holding A and D cancels out movement
        velocity = new Vector2(0, 0);
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
        //if the shoot button is press and cooldown is over
        if (Gdx.input.isKeyPressed(Input.Keys.J) && cooldowns.isOver(0)) {
            if (!Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                if (power >= 1) {
                    if (cooldowns.isOver(4)) {
                        blossomSound.play(0.4f);
                    }
                    blossoms.makeBullet(new Vector2(body.getPosition().x, body.getPosition().y + imageSize.y), screen, new Vector2(0, bulletVelocity_blossoms));
                }
                if (power >= 2) {
                    //spawn bullets on the top left and right of the player
                    blossoms.makeBullet(new Vector2(body.getPosition().x + imageSize.x, body.getPosition().y + imageSize.y), screen, new Vector2(bulletVelocity_blossoms / 2, bulletVelocity_blossoms));
                    blossoms.makeBullet(new Vector2(body.getPosition().x - imageSize.x, body.getPosition().y + imageSize.y), screen, new Vector2(-bulletVelocity_blossoms / 2, bulletVelocity_blossoms));
                }
                if (power >= 3) {
                    //spawn bullets on the bottom left and right of the player
                    blossoms.makeBullet(new Vector2(body.getPosition().x + imageSize.x, body.getPosition().y - imageSize.y), screen, new Vector2(bulletVelocity_blossoms, bulletVelocity_blossoms));
                    blossoms.makeBullet(new Vector2(body.getPosition().x - imageSize.x, body.getPosition().y - imageSize.y), screen, new Vector2(-bulletVelocity_blossoms, bulletVelocity_blossoms));
                }
            }
            swordPhantoms.makeBullet(new Vector2(body.getPosition().x, body.getPosition().y + imageSize.y), screen, bulletVelocity_swordPhantoms);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
            focus = true;
            speed = maxSpeed / 2; //focus is half of max speed
            if (Gdx.input.isKeyPressed(Input.Keys.J)&& cooldowns.isOver(2)){
                if (cooldowns.isOver(4)) {
                    fractalSound.play(0.2f);
                }
                //always give a mandellingbrot to the player in focus
                mandellingbrots.makeFractalBullet(screen, new CatmullRomSpline<>(brot.mandlePoints(new ComplexNum(body.getPosition().x, body.getPosition().y)), true), 0.2f);
                //for every power (floored), give one more fractal bullet option
                for (int i = 0; i < (int) power; i++) {
                    mandellingbrots.makeFractalBullet(screen, new CatmullRomSpline<>(brot.mandlePoints(new ComplexNum(Math.random()*38, Math.random()*38)), true), 0.1f);
                }
            }
        } else {
            focus = false;
            speed = maxSpeed;
        }
        body.setLinearVelocity(speed * velocity.x, speed * velocity.y);
        //if the invincibility is over
        if(cooldowns.isOverCheckOnly(3) && invincibility){
            invincibility = false;
            currentPlayer = image;
        }
    }

    public void Draw(SpriteBatch batch, float delta) {
        Update(delta);
        batch.draw(currentPlayer, body.getPosition().x - imageSize.x / 2, body.getPosition().y - imageSize.y / 2, imageSize.x, imageSize.y);
        if (focus) {
            batch.draw(hitbox, body.getPosition().x - hitboxSize.x / 2, body.getPosition().y - hitboxSize.y / 2, hitboxSize.x, hitboxSize.y);
        }
    }

    public void initializeBullet() {
        swordPhantoms = new PlayerBullet( PLAYER_BULLETS, BodyDef.BodyType.KinematicBody, new Sprite(swordTexture), new Vector2(swordTexture.getWidth() / PPM, swordTexture.getHeight() / PPM), 38);
        blossoms = new PlayerBullet( PLAYER_BULLETS, BodyDef.BodyType.KinematicBody, new Sprite(blossomTexture), new Vector2(blossomTexture.getWidth() / PPM, blossomTexture.getHeight() / PPM), 10);
        mandellingbrots = new PlayerBullet(PLAYER_BULLETS, BodyDef.BodyType.KinematicBody, new Sprite(mandelTexture), new Vector2(mandelTexture.getWidth() / PPM, mandelTexture.getHeight() / PPM), 38);
    }

    public void immunity(){
        //if the player can enter immunity
        if(cooldowns.isOver(3)){
            invincibility = true;
            System.out.println("immunity granted");
            currentPlayer = invincibilityPlayer;
        }
    }

}