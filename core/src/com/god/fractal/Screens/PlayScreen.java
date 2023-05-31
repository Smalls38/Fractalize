package com.god.fractal.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
//import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
//import com.dongbat.jbump.Item;
//import com.dongbat.jbump.Rect;
//import com.dongbat.jbump.World;
import com.god.fractal.Cooldown;
import com.god.fractal.GodFractal;
import com.god.fractal.Player;

public class PlayScreen implements Screen {
    public GodFractal game;
    private OrthographicCamera camera;
    public Viewport viewport;
    Player player;
    Sprite ui_bg;
    public World world;
    private Box2DDebugRenderer b2dr;
    public float PPM;
    public short WORLD_UI = 0x0001;

    public PlayScreen(GodFractal game){
        this.game = game;
        world = new World(new Vector2(0,0), false); //sleep set to false since a lot of things are constantly happening
        b2dr = new Box2DDebugRenderer();


        ui_bg = new Sprite(new Texture("ui_bg.png")); //background of the main ui

        PPM = game.PPM;

        player = new Player(new Texture("player1.png"),new Texture("hitbox.png"), this); //new instance of player


        camera = new OrthographicCamera(); //fixed camera
        camera.setToOrtho(false, game.VWidth/PPM,  game.VHeight/PPM);

        viewport = new FitViewport(game.VWidth/PPM ,game.VHeight/PPM, camera); //a viewport with a fixed aspect ratio

        camera.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);

        game.batch.setProjectionMatrix(camera.combined);

        UiCollisions();




    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //camera.update();
        update(delta);
        game.batch.begin();
        player.Draw(game.batch);


        game.batch.draw(ui_bg, 0, 0, game.VWidth/game.PPM, game.VHeight/game.PPM );
        game.batch.end();

        //b2dr.render(world, camera.combined);


    }
    public void update(float delta){
        world.step(delta, 6, 2);
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
    public void UiCollisions(){
        //ui collisions
        Body body;
        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        fdef.filter.maskBits = 0x0002;
        PolygonShape shape = new PolygonShape();

        fdef.filter.categoryBits = WORLD_UI;
        bdef.type = BodyDef.BodyType.StaticBody;

        //left
        bdef.position.set(111/PPM,540/PPM);
        shape.setAsBox(111/PPM,540/PPM);
        fdef.shape = shape;
        body = world.createBody(bdef);
        body.createFixture(fdef);


        //top
        bdef.position.set(659/PPM,1071/PPM);
        shape.setAsBox(1096/PPM,9/PPM);
        fdef.shape = shape;
        body = world.createBody(bdef);
        body.createFixture(fdef);

        //right
        bdef.position.set(1508/PPM,540/PPM);
        shape.setAsBox(412/PPM,540/PPM);
        fdef.shape = shape;
        body = world.createBody(bdef);
        body.createFixture(fdef);

        //bottom
        bdef.position.set(659/PPM,9/PPM);
        shape.setAsBox(1096/PPM,9/PPM);
        fdef.shape = shape;
        body = world.createBody(bdef);
        body.createFixture(fdef);;

    }
}
