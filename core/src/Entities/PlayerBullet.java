package Entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.CatmullRomSpline;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.god.fractal.Screens.PlayScreen;
import com.god.fractal.BodyData;

public class PlayerBullet extends Bullet implements Projectile{
    public PlayerBullet(short collisionLayer, BodyDef.BodyType type, Sprite img, Vector2 spriteSize, float damage){
        this.dmg = damage;

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(spriteSize.x/2, spriteSize.y/2);

        gdef = new BodyDef();
        fdef = new FixtureDef();
        image = img;
        gdef.type = type;
        gdef.fixedRotation = true;
        fdef.filter.categoryBits = collisionLayer;
        imageSize = new Vector2(spriteSize.x, spriteSize.y);

        System.out.println(fdef.filter.maskBits);
        fdef.shape = shape;
    }
    public void makeBullet(Vector2 position, PlayScreen screen, float speed){
        gdef.position.set(position);
        body = screen.world.createBody(gdef);
        body.setUserData(new BodyData("unfocusBullet", image, 1, dmg));
        body.createFixture(fdef);
        body.setLinearVelocity(0,speed);
    }
    public void makeBullet(Vector2 position, PlayScreen screen, Vector2 speed){
        gdef.position.set(position);
        body = screen.world.createBody(gdef);
        body.setUserData(new BodyData("unfocusBullet", image, 1, dmg));
        body.createFixture(fdef);
        body.setLinearVelocity(speed.x,speed.y);
    }
    public void makeFractalBullet(PlayScreen screen, CatmullRomSpline<Vector2> path, float speed){
        Vector2 initialPosition = new Vector2();
        path.valueAt(initialPosition, 0);
        gdef.position.set(initialPosition);
        body = screen.world.createBody(gdef);
        body.setUserData(new BodyData("focusBullet", image, path, speed, 1 , dmg));
        body.createFixture(fdef);

    }
}
