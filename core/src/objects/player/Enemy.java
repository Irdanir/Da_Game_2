package objects.player;

import static java.lang.Math.max;
import static helper.Constants.PPM;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class Enemy extends GameEntity {
    public int health = 100;
    public int direction = 1;
    boolean flag1 = false;
    int jumpcount = 0;

    public Enemy(float width, float height, Body body) {
        super(width, height, body);
        this.speed = 4f;
    }

    @Override
    public void update() {
        //Просто заглушка
    }

    public void update(Player player) {
        this.x = body.getPosition().x * PPM;
        this.y = body.getPosition().y * PPM;
        if (x < player.x) {
            direction = 1;
        } else if (x > player.x) {
            direction = -1;
        }
        if (health <= 0) {
            flag1 = true;
        }
        if (this.y >= 512) {

        }
    }
    public void move() {
        body.setLinearVelocity(1 * direction, body.getLinearVelocity().y);
    }
    public void jump() {
        if (jumpcount <= 2) {
            float force = body.getMass() * 5;
            body.setLinearVelocity(body.getLinearVelocity().x, 0);
            body.applyLinearImpulse(new Vector2(0, force), body.getPosition(), true);
            jumpcount++;
        }
    }
    @Override
    public void render(SpriteBatch batch) {

    }
    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
