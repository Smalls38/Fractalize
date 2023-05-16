package com.god.fractal;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.SnapshotArray;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.dongbat.jbump.Item;
import com.dongbat.jbump.Rect;
import com.dongbat.jbump.World;

public class GodFractal extends ApplicationAdapter {
	public static SpriteBatch Spritebatch;
	public static ExtendViewport viewport;
	public static OrthographicCamera camera;
	public static SnapshotArray<Entity> entities;
	public static World<Entity> world;
	
	@Override
	public void create () {
		Spritebatch = new SpriteBatch();
	}

	@Override
	public void render () {
		ScreenUtils.clear(1, 0, 0, 1);
		Spritebatch.begin();
		Spritebatch.end();
	}
	
	@Override
	public void dispose () {
		Spritebatch.dispose();
	}
}
