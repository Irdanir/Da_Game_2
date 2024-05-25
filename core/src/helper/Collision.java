package helper;

import static java.lang.Math.abs;

import com.badlogic.gdx.physics.box2d.Body;

import objects.player.GameEntity;

public class Collision {
    public static boolean Body_collision(GameEntity a, GameEntity b) {
        float ax = a.getBody().getPosition().x;
        float bx = b.getBody().getPosition().x;
        float ay = a.getBody().getPosition().y;
        float by = b.getBody().getPosition().y;
        boolean x_collision = abs(ax - bx) < 30;
        boolean y_collision = abs(ay - by) < 30;
        return x_collision && y_collision;
    }
}
