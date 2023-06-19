package com.god.fractal.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.god.fractal.GodFractal;

public class GameOverScreen implements Screen {
    private OrthographicCamera camera;
    public Viewport viewport;
    Sprite ui_bg; //the image to display
    public float PPM; // Pixel Per Meter
    public GodFractal game; //the game itself, to access the constants and sprite batch
    public float opacity; //opacity to make the screen so it doesnt jumpscare the player

    public GameOverScreen(GodFractal game){
        this.game = game;
        ui_bg = new Sprite(new Texture("gameOver.png")); //game over screen
        PPM = game.PPM;

        camera = new OrthographicCamera(); //fixed camera
        camera.setToOrtho(false, game.VWidth / PPM, game.VHeight / PPM); //set a camera with height and width matching with the world

        viewport = new FitViewport(game.VWidth / PPM, game.VHeight / PPM, camera); //a viewport with a fixed aspect ratio

        camera.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0); //set the position of the camera to the middle of the world

        game.batch.setProjectionMatrix(camera.combined);
    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);  //set background to black
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.enableBlending(); // Enable alpha
        opacity += delta/3;
        game.batch.setColor(1,1,1, opacity);
        System.out.println("opacity: " + opacity);
        game.batch.begin();
        game.batch.draw(ui_bg, 0, 0, game.VWidth / PPM, game.VHeight / PPM);
        game.batch.end();
        game.batch.disableBlending();
    }

    @Override
    public void resize(int width, int height) {

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
