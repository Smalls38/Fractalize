package Entities;
import com.badlogic.gdx.math.Vector2;
import com.god.fractal.Cooldown;

public abstract class Enemy extends Entity{
        Vector2 out;
        float speed;
        String name;
        float damage;
        public Cooldown cooldowns;
        EnemyBullet bullet;
}
