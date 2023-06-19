package com.god.fractal.Screens;

import Entities.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.video.VideoPlayer;
import com.badlogic.gdx.video.VideoPlayerCreator;
import com.god.fractal.*;

import java.io.*;
import java.io.BufferedReader;
import java.io.FileReader;

/**
 * this is the class that handles the screen for all gameplay
 */
public class PlayScreen implements Screen {
    private VideoPlayer videoPlayer; //video player for the background
    public GodFractal game; //the game itself, to access the constants and sprite batch
    private OrthographicCamera camera;
    public Viewport viewport;
    Player player; //instance of player
    Sprite ui_bg; //the UI interface
    Sprite ui_health;
    public World world; //physics and collision world
    private Box2DDebugRenderer b2dr; //renderer that shows the bounding box of all bodies
    public float PPM; // Pixel Per Meter
    public short WORLD_UI = 0x0001; //the bit for which layer that the UI is located on
    public short PLAYER_WORLD = 0x002;
    public short PLAYER_BULLETS = 0x004;
    public short ENEMY_WORLD = 0x0008;
    public short ENEMY_BULLETS = 0x0010;
    public Timeline timeline;
    Music music = Gdx.audio.newMusic(Gdx.files.internal("assets/stage1_ost.mp3"));


    public PlayScreen(GodFractal game) throws IOException {
        this.game = game;
        world = new World(new Vector2(0, 0), false); //sleep set to false since a lot of things are constantly happening
        b2dr = new Box2DDebugRenderer();
        videoPlayer = VideoPlayerCreator.createVideoPlayer();
        videoPlayer.setOnCompletionListener(new VideoPlayer.CompletionListener() {
            @Override
            public void onCompletionListener(FileHandle file) { //when the video is finished playing
                try {
                    videoPlayer.play(Gdx.files.internal("fractal.webm"));  //try to loop the playback
                } catch (FileNotFoundException e) {
                    Gdx.app.error("gdx-video", "vid not found"); //if the file somehow disappeared, uhhh nothing can be done really
                }
            }
        });
        try {
            videoPlayer.play(Gdx.files.internal("fractal.webm")); //try to play the video
        } catch (FileNotFoundException e) {
            Gdx.app.error("gdx-video", "vid not found");
        }
        ui_bg = new Sprite(new Texture("ui_bg.png")); //background of the main ui
        ui_health = new Sprite(new Texture("hp_bar.png")); //health of player

        PPM = game.PPM;

        player = new Player(new Texture("player1.png"), new Texture("hitbox.png"), this, WORLD_UI); //new instance of player


        camera = new OrthographicCamera(); //fixed camera
        camera.setToOrtho(false, game.VWidth / PPM, game.VHeight / PPM); //set a camera with height and width matching with the world

        viewport = new FitViewport(game.VWidth / PPM, game.VHeight / PPM, camera); //a viewport with a fixed aspect ratio

        camera.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0); //set the position of the camera to the middle of the world

        game.batch.setProjectionMatrix(camera.combined);

        UiCollisions(); //initialize the boundaries of the UI

        timeline = new Timeline(this, ENEMY_WORLD, ENEMY_BULLETS);
        music.play();


        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {}

            @Override
            public void endContact(Contact contact) {}

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {
                float totalDamage = 0;
                short firstBit = contact.getFixtureA().getFilterData().categoryBits;
                short secondBit = contact.getFixtureB().getFilterData().categoryBits;
                //System.out.println("checking " + firstBit + " " + secondBit);
                BodyData dataA = (BodyData) contact.getFixtureA().getBody().getUserData();
                BodyData dataB = (BodyData) contact.getFixtureB().getBody().getUserData();
                int orResult = firstBit | secondBit;
                if ((orResult) == (ENEMY_WORLD | PLAYER_WORLD)) {
                    System.out.println("Contact " + firstBit + " " + secondBit);
                    System.out.println(ENEMY_WORLD | PLAYER_WORLD);
                    contact.setEnabled(false);
                    if (dataA != null) {
                        totalDamage += dataA.getDamage();
                    } else {
                        totalDamage += dataB.getDamage();
                    }
                    contact.setEnabled(false);

                } else if ((orResult) == (ENEMY_WORLD | WORLD_UI)){
                    contact.setEnabled(false);
                } else if ((orResult) == (PLAYER_BULLETS | WORLD_UI)){
                    System.out.println("Contact " + firstBit + " " + secondBit);
                    //contact.setEnabled(false);
                } else if ((orResult) == (PLAYER_BULLETS | ENEMY_WORLD)){
                    dataA.takeDamage(dataB.getDamage());
                    dataB.takeDamage(dataA.getDamage());
                    contact.setEnabled(false);
                } else if ((orResult) == (ENEMY_WORLD | ENEMY_WORLD)){
                    contact.setEnabled(false);
                } else if ((orResult) == (PLAYER_BULLETS | PLAYER_WORLD)){
                    contact.setEnabled(false);
                }
                if (!player.invincibility){
                    if (totalDamage > 0) {
                        player.health -= totalDamage;
                        System.out.println("player health is at " + player.health);
                        player.immunity();
                    }
                }
            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {}
        });

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);  //set background to black
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        videoPlayer.update(); // advance the frame of the video player
        timeline.update(delta); // update the timeline
        update(delta);
        game.batch.begin();

        Texture videoFrame = videoPlayer.getTexture();
        if (videoFrame != null) {
            game.batch.draw(videoFrame, 0, 0, videoFrame.getWidth()/ PPM, videoFrame.getHeight() / PPM);
        }

        player.Draw(game.batch, delta);

        Array<Body> bodies = new Array<>(); //array to contain all the bodies currently present in the world
        // Now fill the array with all bodies
        world.getBodies(bodies);
        int enemyCount = 0;
        for (Body b : bodies) {
            Sprite toDraw;
            BodyData data = (BodyData) b.getUserData();
            if (data != null) {
                if (data.getType().equals("StandardEnemy")){
                    if ( data.getProgress() < 1 && data.getHealth() > 0) {
                        enemyCount++;
                        data.getTimer().update(delta);
                        //the bullet will be aimed at a random direction or at the player with 50/50 chances
                        if (data.getTimer().isOver(0)) {
                            System.out.println("enemy tried to shoot");
                            if (Math.random() > 0.5) {
                                data.getBulletType().makeBullet(b.getPosition(), this,
                                        new Vector2((float) Math.random(), (float) Math.random()));
                            } else {
                                data.getBulletType().makeBullet(b.getPosition(), this,
                                        player.body.getPosition().sub(b.getPosition()));
                            }
                        } else {
                            System.out.println(data.getTimer().get(0));
                        }
                        toDraw = data.getTexture();
                        b.setLinearVelocity(data.getVelocity(b.getPosition()));
                        data.addProgress(delta);
                        game.batch.draw(toDraw, b.getPosition().x - toDraw.getWidth() / 2 / PPM, b.getPosition().y - toDraw.getHeight() / 2 / PPM,
                                toDraw.getWidth() / PPM, toDraw.getHeight() / PPM);
                    }  else {
                        world.destroyBody(b);
                    }
                }else if (data.getType().equals("unfocusBullet")) {
                    if (b.getPosition().y > viewport.getWorldHeight() * 1.2 || data.getHealth() <= 0) {
                        world.destroyBody(b);
                    } else {
                        toDraw = data.getTexture();
                        game.batch.draw(toDraw, b.getPosition().x - toDraw.getWidth() / 2 / PPM, b.getPosition().y - toDraw.getHeight() / 2 / PPM,
                                toDraw.getWidth() / PPM, toDraw.getHeight() / PPM);
                    }
                } else if (data.getType().equals("focusBullet")) {
                    if (data.getProgress() >= 1 ||
                            Math.abs(b.getPosition().x) > viewport.getWorldWidth() * 3 ||
                            Math.abs(b.getPosition().y) > viewport.getWorldHeight() * 3 ||
                            (Math.abs(b.getLinearVelocity().x) <= 0.01 && Math.abs(b.getLinearVelocity().y) <= 0.01 && data.getProgress() > 0.02) ||
                            data.getHealth() <= 0){
                        world.destroyBody(b);
                        //System.out.println("velocity is " + b.getLinearVelocity().x + b.getLinearVelocity().y );

                    } else {
                        toDraw = data.getTexture();
                        b.setLinearVelocity(data.getVelocity(b.getPosition()));
                        data.addProgress(delta);
                        game.batch.draw(toDraw, b.getPosition().x - toDraw.getWidth() / 2 / PPM, b.getPosition().y - toDraw.getHeight() / 2 / PPM,
                                toDraw.getWidth() / PPM, toDraw.getHeight() / PPM);
                    }
                } else if (data.getType().equals("enemyBullet")){
                    if (b.getPosition().x < 0 || b.getPosition().x > viewport.getWorldWidth() ||b.getPosition().y < 0 ||
                            b.getPosition().y > viewport.getWorldHeight()){
                        world.destroyBody(b);
                    } else {
                        toDraw = data.getTexture();
                        game.batch.draw(toDraw, b.getPosition().x - toDraw.getWidth() / 2 / PPM, b.getPosition().y - toDraw.getHeight() / 2 / PPM,
                                toDraw.getWidth() / PPM, toDraw.getHeight() / PPM);
                    }
                }
            }
            if (enemyCount == 0){
                timeline.end();
            }
        }

        game.batch.draw(ui_bg, 0, 0, game.VWidth / PPM, game.VHeight / PPM);
        game.batch.draw(ui_health, 105/PPM, 114/PPM, ui_health.getWidth()/ PPM, ui_health.getHeight() / game.PPM * (Math.max(0, player.health)/100));
        game.batch.end();

        b2dr.render(world, camera.combined);


    }

    public void update(float delta) {
        world.step(delta, 18, 18); //step the physics simulation


    }
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height); //updates the actual size of the window so nothing can be stretched
    }
    public void killAllEnemies(){
        Array<Body> bodies = new Array<>(); //array to contain all the bodies currently present in the world
        // Now fill the array with all bodies
        world.getBodies(bodies);
        for (Body b : bodies) {
            BodyData data = (BodyData) b.getUserData();
            if (data != null) {
                if (data.getType().equals("StandardEnemy")) {
                    world.destroyBody(b);
                }
            }
        }
    }

    @Override
    public void dispose() {
        world.dispose();
        b2dr.dispose();

    }

    public void UiCollisions() {
        //ui collisions
        Body body;
        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();

        PolygonShape shape = new PolygonShape();

        fdef.filter.categoryBits = WORLD_UI;
        bdef.type = BodyDef.BodyType.StaticBody;

        //left
        bdef.position.set(111 / PPM, 540 / PPM);
        shape.setAsBox(111 / PPM, 540 / PPM);
        fdef.shape = shape;
        body = world.createBody(bdef);
        body.createFixture(fdef);


        //top
        bdef.position.set(659 / PPM, 1071 / PPM);
        shape.setAsBox(1096 / PPM, 9 / PPM);
        fdef.shape = shape;
        body = world.createBody(bdef);
        body.createFixture(fdef);

        //right
        bdef.position.set(1508 / PPM, 540 / PPM);
        shape.setAsBox(412 / PPM, 540 / PPM);
        fdef.shape = shape;
        body = world.createBody(bdef);
        body.createFixture(fdef);

        //bottom
        bdef.position.set(659 / PPM, 9 / PPM);
        shape.setAsBox(1096 / PPM, 9 / PPM);
        fdef.shape = shape;
        body = world.createBody(bdef);
        body.createFixture(fdef);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }
}

