package objects.player;

import static helper.Constants.PPM;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.MassData;

public class Bullet extends GameEntity {

    public Bullet(float width, float height, Body body) {
        super(width, height, body);
        this.speed = 20f;
        MassData mass = new MassData();
        mass.mass = 0.02f;
        body.setMassData(mass);
    }

    @Override
    public void update() {

    }


    public void update(Player player) {
        this.x = body.getPosition().x * PPM;
        this.y = body.getPosition().y * PPM;
        body.getPosition().x = this.x / PPM;
        body.getPosition().y = this.y / PPM;
        body.setLinearVelocity(10f * player.direction, 0.0f);
    }

    @Override
    public void render(SpriteBatch batch) {

    }

    public float getY() {
        return y;
    }
    public float getX() {
        return x;
    }
    public void setY(float y) {
        this.y = y;
    }
}
