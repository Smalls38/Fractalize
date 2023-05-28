package com.god.fractal.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
//import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
//import com.dongbat.jbump.Item;
//import com.dongbat.jbump.Rect;
//import com.dongbat.jbump.World;
import com.god.fractal.GodFractal;
import com.god.fractal.Player;

public class PlayScreen implements Screen {
    private GodFractal game;
    private OrthographicCamera camera;
    private Viewport viewport;
    Player player;
    Sprite ui_bg;
    private World world;
    private Box2DDebugRenderer b2dr;
    public PlayScreen(GodFractal game){
        this.game = game;

        ui_bg = new Sprite(new Texture("ui_bg.png")); //background of the main ui

        player = new Player(new Texture("player1.png"),new Texture("hitbox.png") ); //new instance of player

        camera = new OrthographicCamera(); //fixed camera

        viewport = new FitViewport(game.VWidth*game.PPM ,game.VHeight*game.PPM, camera); //a viewport with a fixed aspect ratio

        world = new World(new Vector2(0,0), false); //sleep set to false since a lot of things are constantly happening
        b2dr = new Box2DDebugRenderer();

    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
        player.Draw(game.batch);

        game.batch.draw(ui_bg, 0, 0);
        game.batch.end();

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
}
