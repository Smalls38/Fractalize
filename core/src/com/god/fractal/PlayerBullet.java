package com.god.fractal;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.god.fractal.Screens.PlayScreen;

public class PlayerBullet extends Bullet {
    public PlayerBullet(short enemy, short collisionLayer, BodyDef.BodyType type, Texture img, Vector2 spriteSize){
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(spriteSize.x, spriteSize.y);
        gdef = new BodyDef();
        fdef = new FixtureDef();

        //gdef.type = BodyDef.BodyType.DynamicBody;
        gdef.type = type;
        gdef.fixedRotation = true;
        fdef.filter.categoryBits = collisionLayer;
        image = new Sprite(img);
        fdef.filter.maskBits = 0x0001;
        fdef.shape = shape;
    }
    public void makeBullet(Vector2 position, PlayScreen screen, float speed){
        gdef.position.set(position);
        body = screen.world.createBody(gdef);
        body.createFixture(fdef);
        body.setLinearVelocity(0,speed);

    }
}
