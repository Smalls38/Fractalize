package com.god.fractal.Screens;

import Entities.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
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
import com.god.fractal.BodyData;
import com.god.fractal.ComplexNum;
import com.god.fractal.GodFractal;
import com.god.fractal.Mandelbrot;

import java.io.FileNotFoundException;


public class PlayScreen implements Screen {
    private VideoPlayer videoPlayer;
    public GodFractal game;
    private OrthographicCamera camera;
    public Viewport viewport;
    Player player;
    Sprite ui_bg;
    public World world;
    private Box2DDebugRenderer b2dr;
    public float PPM;
    public short WORLD_UI = 0x0001;

    public PlayScreen(GodFractal game) {
        this.game = game;
        world = new World(new Vector2(0, 0), false); //sleep set to false since a lot of things are constantly happening
        b2dr = new Box2DDebugRenderer();
        videoPlayer = VideoPlayerCreator.createVideoPlayer();
        videoPlayer.setOnCompletionListener(new VideoPlayer.CompletionListener() {
            @Override
            public void onCompletionListener(FileHandle file) {
                try {
                    videoPlayer.play(Gdx.files.internal("fractal.webm"));
                } catch (FileNotFoundException e) {
                    Gdx.app.error("gdx-video", "vid not found");
                }
            }
        });
        try {
            videoPlayer.play(Gdx.files.internal("fractal.webm"));
        } catch (FileNotFoundException e) {
            Gdx.app.error("gdx-video", "vid not found");
        }
        ui_bg = new Sprite(new Texture("ui_bg.png")); //background of the main ui

        PPM = game.PPM;

        player = new Player(new Texture("player1.png"), new Texture("hitbox.png"), this, WORLD_UI); //new instance of player


        camera = new OrthographicCamera(); //fixed camera
        camera.setToOrtho(false, game.VWidth / PPM, game.VHeight / PPM);

        viewport = new FitViewport(game.VWidth / PPM, game.VHeight / PPM, camera); //a viewport with a fixed aspect ratio

        camera.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);

        game.batch.setProjectionMatrix(camera.combined);

        UiCollisions();



    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        videoPlayer.update();
        //camera.update();
        update(delta);
        game.batch.begin();

        Texture videoFrame = videoPlayer.getTexture();
        if (videoFrame != null) {
            game.batch.draw(videoFrame, 0, 0, videoFrame.getWidth() * 1.5f / PPM, videoFrame.getHeight() * 1.5f / PPM);
        }

        player.Draw(game.batch, delta);

        Array<Body> bodies = new Array<>();
        // Now fill the array with all bodies
        world.getBodies(bodies);

        for (Body b : bodies) {
            Sprite toDraw;
            BodyData data = (BodyData) b.getUserData();
            if (data != null) {
                if (data.getType().equals("unfocusBullet")) {
                    if (b.getPosition().y > viewport.getWorldHeight() * 1.2) {
                        world.destroyBody(b);
                    } else {
                        toDraw = data.getTexture();
                        game.batch.draw(toDraw, b.getPosition().x - toDraw.getWidth() / 2 / PPM, b.getPosition().y - toDraw.getHeight() / 2 / PPM,
                                toDraw.getWidth() / PPM, toDraw.getHeight() / PPM);
                    }
                } else if (data.getType().equals("focusBullet")) {
                    if (data.getProgress() >= 1 || Math.abs(b.getPosition().x) > viewport.getWorldWidth() * 3 || Math.abs(b.getPosition().y) > viewport.getWorldHeight() * 3){
                        world.destroyBody(b);

                    } else {
                        toDraw = data.getTexture();
                        b.setLinearVelocity(data.getVelocity(b.getPosition()));
                        data.addProgress(delta);
                        game.batch.draw(toDraw, b.getPosition().x - toDraw.getWidth() / 2 / PPM, b.getPosition().y - toDraw.getHeight() / 2 / PPM,
                                toDraw.getWidth() / PPM, toDraw.getHeight() / PPM);
                    }
                }
            }
        }

        game.batch.draw(ui_bg, 0, 0, game.VWidth / game.PPM, game.VHeight / game.PPM);
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

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

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
        fdef.filter.maskBits = 0x0002;
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
        ;

    }
}
