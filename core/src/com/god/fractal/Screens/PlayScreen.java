package com.god.fractal.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.dongbat.jbump.Item;
import com.dongbat.jbump.Rect;
import com.dongbat.jbump.World;
import com.god.fractal.Entity;
import com.god.fractal.GodFractal;
import com.god.fractal.Player;

public class PlayScreen implements Screen {
    private GodFractal game;
    public World<Entity> world;
    public Item<Entity> playerItem;
    private OrthographicCamera camera;
    private Viewport viewport;
    Player player;
    Sprite ui;
    public PlayScreen(GodFractal game){
        this.game = game;
        ui = new Sprite(new Texture("ui.png"));
        player = new Player(new Texture("player1.png"),new Texture("hitbox.png") ); //new instance of player

        camera = new OrthographicCamera();
        viewport = new StretchViewport(1920,1080, camera);

        //testing for collisions rn, don't know if i want to stick with jbump
        world = new World<>();
        playerItem = new Item<Entity>(player);
        world.add(playerItem, player.position.x+(player.image.getWidth()/2)-player.hitBoxSize/2, player.position.y+(player.image.getHeight()/2)-player.hitBoxSize/2, player.hitBoxSize, player.hitBoxSize);
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
        world.update(playerItem, player.position.x+(player.image.getWidth()/2)-player.hitBoxSize/2, player.position.y+(player.image.getHeight()/2)-player.hitBoxSize/2);
        game.batch.draw(ui, 0, 0);
        game.batch.end();

        //debug, draws hitboxes (only player for now)
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        game.shapeRenderer.setColor(Color.BLUE);
        Rect rect = world.getRect(playerItem);
        game.shapeRenderer.rect(rect.x, rect.y, rect.w, rect.h);
        System.out.println(rect.x);
        game.shapeRenderer.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
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

    }
}
