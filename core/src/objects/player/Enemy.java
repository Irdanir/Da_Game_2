package objects.player;

import static java.lang.Math.max;
import static helper.Constants.PPM;

import helper.MyInputProcessor;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class Enemy extends GameEntity {
    private int health = 100;
    public int direction = 1;
    boolean flag1 = false;

    public Enemy(float width, float height, Body body) {
        super(width, height, body);
        this.speed = 4f;
    }

    @Override
    public void update() {
        //Просто заглушка
    }

    public void update(Player player) {
        x = body.getPosition().x * PPM;
        y = body.getPosition().y * PPM;
        if (x < player.x) {
            direction = 1;
        } else if (x > player.x) {
            direction = -1;
        }
        if (health <= 0) {
            flag1 = true;
        }
    }
    public void move() {
        body.setLinearVelocity(1 * direction, body.getLinearVelocity().y);
    }
    public void jump() {
        float force = body.getMass() * 5;
        body.setLinearVelocity(body.getLinearVelocity().x, 0);
        body.applyLinearImpulse(new Vector2(0, force), body.getPosition(), true);
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
