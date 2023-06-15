package Entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.CatmullRomSpline;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.god.fractal.BodyData;
import com.god.fractal.Screens.PlayScreen;

public class StandardEnemy extends Enemy {

    public StandardEnemy(short enemy, short collisionLayer, BodyDef.BodyType type, Sprite img, Vector2[] dataSet, float speed, Vector2 spriteSize) {
        CatmullRomSpline<Vector2> myCatmull = new CatmullRomSpline<Vector2>(dataSet, true);
        out = new Vector2();
        this.speed = speed;

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(spriteSize.x / 2, spriteSize.y / 2);

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
    public void makeEnemy(Vector2 position, PlayScreen screen, float speed, CatmullRomSpline path) {
        Vector2 initialPosition = new Vector2();
        path.valueAt(initialPosition, 0);
        gdef.position.set(initialPosition);
        body = screen.world.createBody(gdef);
        body.setUserData(new BodyData("StandardEnemy", image, path, speed));
        body.createFixture(fdef);
    }
}
