package helper;

import static java.lang.Math.abs;

import com.badlogic.gdx.physics.box2d.Body;

import objects.player.GameEntity;

public class Collision {
    public static boolean Body_collision(GameEntity a, GameEntity b) {
        boolean x_collision = abs((a.getX() - b.getX())) < 20;
        boolean y_collision = abs((a.getY() - b.getY())) < 20;
        return x_collision && y_collision;
    }
}
