package Entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.CatmullRomSpline;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.god.fractal.BodyData;
import com.god.fractal.Screens.PlayScreen;

public class EnemyBullet extends Bullet implements Projectile {
    float bulletSpeed;
    public EnemyBullet(short collisionLayer, BodyDef.BodyType type, Sprite img, Vector2 spriteSize,
                       float damage, float bulletSpeed){
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
        this.bulletSpeed = bulletSpeed;
    }

    public void makeBullet(Vector2 position, PlayScreen screen, float speed) {
        gdef.position.set(position);
        body = screen.world.createBody(gdef);
        body.setUserData(new BodyData("enemyBullet", image, 1, dmg));
        body.createFixture(fdef);
        body.setLinearVelocity(0,speed);
    }


    public void makeBullet(Vector2 position, PlayScreen screen, Vector2 speed) {
        gdef.position.set(position);
        body = screen.world.createBody(gdef);
        body.setUserData(new BodyData("enemyBullet", image, 1, dmg));
        body.createFixture(fdef);
        speed.nor();
        speed.scl(bulletSpeed, bulletSpeed);
        body.setLinearVelocity(speed.x,speed.y);
    }

}
