package Entities;

import com.badlogic.gdx.math.CatmullRomSpline;
import com.badlogic.gdx.math.Vector2;
import com.god.fractal.BodyData;
import com.god.fractal.Screens.PlayScreen;

public interface Projectile {
    void makeBullet(Vector2 position, PlayScreen screen, float speed);

    void makeBullet(Vector2 position, PlayScreen screen, Vector2 speed);
    void makeFractalBullet(PlayScreen screen, CatmullRomSpline<Vector2> path, float speed);
}
