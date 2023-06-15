package com.god.fractal;

import Entities.Entity;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.SnapshotArray;
import com.god.fractal.Screens.PlayScreen;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * i think i have to explain what the classes do now because there are a lot of classes for this project, this class is technically the main class?
 * it would hold and change all the screens but i might not have enough time for any of that
 * it also holds objects and constants that are used across all the screens
 */
public class GodFractal extends Game {
	public SpriteBatch batch; // batch that will draw all the sprites
	public float PPM = 24; //pixel per meter
	public float VWidth = 1920; //virtual width
	public float VHeight = 1080; //virtual height
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		try {
			setScreen(new PlayScreen(this));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void render () {
		ScreenUtils.clear(0, 0, 0, 1);
		update();
		super.render(); //runs the current screen's render method with delta time passed into it
	}
	public void update(){
		//this just outputs the mouse position on the screen and outputs it to the console
		//its like a very budget stage creator, this is for the points for the splines
		if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){
			System.out.print(Gdx.input.getX()/PPM + " " + Gdx.input.getY()/PPM + ",");
		} else if (Gdx.input.isKeyJustPressed(Input.Keys.N)){
			System.out.println(); // new line if the n key is pressed
		}
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
