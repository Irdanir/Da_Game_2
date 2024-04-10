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
    private int jumpCounter;

    public int health = 100;
    private int direction = 1;
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
        checkUserInput();
        if (savespeed < -7.5f && body.getLinearVelocity().y == 0) {  // Коснулся земли
            health += savespeed * 0.5f; // урон от падения
            System.out.println(health);
        }
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

    private void checkUserInput() {
        velX = 0;
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            velX = 1;
            direction = 1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            velX = -1;
            direction = -1;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && jumpCounter < 2) {
            float force = body.getMass() * 5;
            body.setLinearVelocity(body.getLinearVelocity().x, 0);
            body.applyLinearImpulse(new Vector2(0, force), body.getPosition(), true);
            jumpCounter++;
            System.out.println(this.body.getMass());
        }
        if (body.getLinearVelocity().y == 0) {
            jumpCounter = 0;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.SHIFT_LEFT) && cooldown_slam >= 1200) {
            body.setLinearVelocity(body.getLinearVelocity().x, min(-10, body.getLinearVelocity().y - 10));
            //body.applyLinearImpulse(new Vector2(force, 0), body.getPosition(), true);
            cooldown_slam = 0;
            //System.out.println(stamina + " " + x + " " + y);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.CONTROL_LEFT) && cooldown_boost >= 600) {
            body.setLinearVelocity(body.getLinearVelocity().x, max(10, body.getLinearVelocity().y + 10));
            //body.applyLinearImpulse(new Vector2(force, 0), body.getPosition(), true);
            cooldown_boost = 0;
            //System.out.println(stamina + " " + x + " " + y);
        }
        body.setLinearVelocity(velX * speed, body.getLinearVelocity().y < 25 ? body.getLinearVelocity().y : 25);
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
