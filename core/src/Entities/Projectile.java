package Entities;

import com.badlogic.gdx.math.CatmullRomSpline;
import com.badlogic.gdx.math.Vector2;
import com.god.fractal.BodyData;
import com.god.fractal.Screens.PlayScreen;

public interface Projectile {
    public void makeBullet(Vector2 position, PlayScreen screen, float speed);

    public void makeBullet(Vector2 position, PlayScreen screen, Vector2 speed);
    public void makeFractalBullet(PlayScreen screen, CatmullRomSpline<Vector2> path, float speed);
}
