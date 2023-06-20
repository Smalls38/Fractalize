package Entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.CatmullRomSpline;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.god.fractal.BodyData;
import com.god.fractal.Cooldown;
import com.god.fractal.Screens.PlayScreen;

public class StandardEnemy extends Enemy {

    public StandardEnemy(short enemy, float bulletCooldown, BodyDef.BodyType type,
                         Sprite img, float speed, float PPM, float health, float damage, EnemyBullet bullet) {
        out = new Vector2();
        this.speed = speed;

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(img.getWidth()/ 2 /PPM, img.getHeight() / 2/PPM);

        gdef = new BodyDef();
        fdef = new FixtureDef();
        image = img;
        gdef.type = type;
        gdef.fixedRotation = true;
        fdef.filter.categoryBits = enemy;
        System.out.println("Enemy is" + enemy);
        imageSize = new Vector2(img.getWidth()/PPM, img.getHeight()/PPM);

        fdef.shape = shape;

        this.health = health;
        this.damage = damage;

        name = "StandardEnemy";

        cooldowns = new Cooldown(new float[]{bulletCooldown});

        this.bullet = bullet;
    }

    public void makeEnemy(PlayScreen screen, CatmullRomSpline path) {

        Vector2 initialPosition = new Vector2();
        path.valueAt(initialPosition, 0);
        gdef.position.set(initialPosition);
        body = screen.world.createBody(gdef);
        body.setUserData(new BodyData("StandardEnemy", image, path, speed, health, damage, bullet, cooldowns));
        body.createFixture(fdef);
    }
    public String getName() {
        return name;
    }
}
