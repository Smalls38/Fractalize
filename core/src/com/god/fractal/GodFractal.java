package com.god.fractal;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.SnapshotArray;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.dongbat.jbump.Item;
import com.dongbat.jbump.Rect;
import com.dongbat.jbump.World;
import com.god.fractal.Screens.PlayScreen;

public class GodFractal extends Game {
	public SpriteBatch batch;
	public float PPM = 24; //pixel per meter
	public float VWidth = 1920;
	public float VHeight = 1080;

	public SnapshotArray<Entity> entities;


	public ShapeRenderer shapeRenderer;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		setScreen(new PlayScreen(this));
		shapeRenderer = new ShapeRenderer();
	}

	@Override
	public void render () {
		ScreenUtils.clear(0, 0, 0, 1);
		if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){
			System.out.print(Gdx.input.getX()/PPM + " " + Gdx.input.getY()/PPM + ",");
		} else if (Gdx.input.isKeyJustPressed(Input.Keys.N)){
			System.out.println();
		}
		super.render();
//		batch.begin();

//		batch.end();




	}
	
	@Override
	public void dispose () {
		batch.dispose();
		shapeRenderer.dispose();
	}
}
