package com.god.fractal;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.god.fractal.Screens.PlayScreen;

import static com.badlogic.gdx.graphics.Colors.reset;

public class PlayerBullet extends Bullet {
    public PlayerBullet(short enemy, short collisionLayer, BodyDef.BodyType type, Sprite img, Vector2 spriteSize, float damage, float lifetime){
        this.dmg = damage;
        this.lifeTime =  lifetime;

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(spriteSize.x/2, spriteSize.y/2);

        gdef = new BodyDef();
        fdef = new FixtureDef();
        image = img;
        gdef.type = type;
        gdef.fixedRotation = true;
        fdef.filter.categoryBits = collisionLayer;
        imageSize = new Vector2(spriteSize.x, spriteSize.y);

        fdef.filter.maskBits = enemy;
        fdef.shape = shape;

    }
    public PlayerBullet makeBullet(Vector2 position, PlayScreen screen, float speed){
        gdef.position.set(position);
        body = screen.world.createBody(gdef);
        body.setUserData(image);
        body.createFixture(fdef);
        body.setLinearVelocity(0,speed);
        return this;
    }
    public PlayerBullet makeBullet(Vector2 position, PlayScreen screen, Vector2 speed){
        gdef.position.set(position);
        body = screen.world.createBody(gdef);
        body.setUserData(image);
        body.createFixture(fdef);
        body.setLinearVelocity(speed.x,speed.y);
        return this;
    }
    public boolean draw(SpriteBatch batch) {
        if (body != null) {
            batch.draw(image, body.getPosition().x - imageSize.x / 2, body.getPosition().y - imageSize.y / 2,
                    imageSize.x, imageSize.y);
            return true;
        }
        return false;
    }

}
