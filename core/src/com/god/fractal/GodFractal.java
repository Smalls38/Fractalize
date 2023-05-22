package com.god.fractal;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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
	public static Item<Entity> playerItem;
	Player player;
	Sprite ui ;
	private ShapeRenderer shapeRenderer;
	
	@Override
	public void create () {
		Spritebatch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		player = new Player(new Texture("player1.png"),new Texture("hitbox.png") );
		ui = new Sprite(new Texture("ui.png"));
		world = new World<>();
		playerItem = new Item<Entity>(player);
		world.add(playerItem, player.position.x+(player.image.getWidth()/2)-player.hitBoxSize/2, player.position.y+(player.image.getHeight()/2)-player.hitBoxSize/2, player.hitBoxSize, player.hitBoxSize);
	}

	@Override
	public void render () {
		ScreenUtils.clear(0, 0, 0, 1);


		Spritebatch.begin();
		player.Draw(Spritebatch);
		world.update(playerItem, player.position.x+(player.image.getWidth()/2)-player.hitBoxSize/2, player.position.y+(player.image.getHeight()/2)-player.hitBoxSize/2);
		ui.draw(Spritebatch);
		Spritebatch.end();

		//debug, draws hitboxes (only player for now)
		shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
		shapeRenderer.setColor(Color.BLUE);
		Rect rect = world.getRect(playerItem);
		shapeRenderer.rect(rect.x, rect.y, rect.w, rect.h);
		System.out.println(rect.x);
		shapeRenderer.end();


	}
	
	@Override
	public void dispose () {
		Spritebatch.dispose();
		shapeRenderer.dispose();
	}
}
