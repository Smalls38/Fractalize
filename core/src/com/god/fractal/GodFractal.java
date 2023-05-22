package com.god.fractal;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
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
	Player player;
	Sprite ui ;
	
	@Override
	public void create () {
		Spritebatch = new SpriteBatch();
		player = new Player(new Texture("player1.png"),new Texture("hitbox.png") );
		ui = new Sprite(new Texture("ui.png"));
		ui.setSize(1600,900);
	}

	@Override
	public void render () {
		ScreenUtils.clear(0, 0, 0, 1);
		Spritebatch.begin();
		player.Draw(Spritebatch);
		ui.draw(Spritebatch);
		Spritebatch.end();
	}
	
	@Override
	public void dispose () {
		Spritebatch.dispose();
	}
}
