package com.god.fractal;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.CatmullRomSpline;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class EnemyConstruct extends Enemy{

    public EnemyConstruct(short enemy, short collisionLayer, BodyDef.BodyType type, Sprite img, Vector2[] dataSet, float speed, Vector2 spriteSize){
        CatmullRomSpline<Vector2> myCatmull = new CatmullRomSpline<Vector2>(dataSet, true);
        Vector2 out = new Vector2();
        this.speed = speed;

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
}
