package objects.player;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static helper.Constants.PPM;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.MassData;

public class Player extends GameEntity {
    public int jumpCounter;

    public int health = 100;
    public int direction = 1;
    public int cooldown_boost = 0;
    public int cooldown_slam = 0;
    public int cooldown_shoot = 0;

    public Player(float width, float height, Body body) {
        super(width, height, body);
        this.speed = 4f;
        this.jumpCounter = 0;
        MassData mass = new MassData();
        mass.mass = 2f;
        body.setMassData(mass);
    }

    float savespeed;

    @Override
    public void update() {
        x = body.getPosition().x * PPM;
        y = body.getPosition().y * PPM;
        if (body.getLinearVelocity().y == 0) {
            jumpCounter = 0;
        }
        body.setLinearVelocity(velX * speed, body.getLinearVelocity().y < 25 ? body.getLinearVelocity().y : 25);
        if (health <= 0) {
            health = 0;
            //text.display("You've lost");
        }
        savespeed = body.getLinearVelocity().y;
        if (cooldown_boost <= 600) {
            cooldown_boost++;
        }
        if (cooldown_slam <= 1200) {
            cooldown_slam++;
        }
        if (cooldown_shoot <= 180) {
            cooldown_shoot++;
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

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
}
