package objects.player;

import static helper.Constants.PPM;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.MassData;

public class Bullet extends GameEntity {
    int direction = 1;

    public Bullet(float width, float height, Body body, int direction) {
        super(width, height, body);
        this.speed = 20f;
        MassData mass = new MassData();
        mass.mass = 0.02f;
        this.direction = direction;
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
        body.setLinearVelocity(10f * direction, 0.0f);
        System.out.println(direction);
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
