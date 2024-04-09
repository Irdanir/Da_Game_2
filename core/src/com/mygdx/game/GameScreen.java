package com.mygdx.game;

import static java.lang.Math.abs;
import static helper.Constants.PPM;
import helper.Textures;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;

import helper.TileMapHelper;
import objects.player.Enemy;
import objects.player.Player;

public class GameScreen extends ScreenAdapter {
    private OrthographicCamera camera;
    private Batch batch;
    private World world;
    private Box2DDebugRenderer box2DDebugRenderer;
    private OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;
    private TileMapHelper tileMapHelper;
    private Player player;
    private ArrayList<Enemy> enemies = new ArrayList<>();
    private int enemyjumpcount = 0;
    private boolean flag = false;
    BitmapFont foont = new BitmapFont(Gdx.files.internal("assets/fonnt.fnt"), false);
    public GameScreen (OrthographicCamera camera) {
        this.camera = camera;
        this.batch = new SpriteBatch();
        this.world = new World(new Vector2(0, -9.81f), false);
        this.box2DDebugRenderer = new Box2DDebugRenderer();
        this.tileMapHelper = new TileMapHelper(this);
        this.orthogonalTiledMapRenderer = tileMapHelper.setupMap();
    }
    private void update() {
        world.step(1/60f, 6, 2);
        CameraUpdate();
        batch.setProjectionMatrix(camera.combined);
        orthogonalTiledMapRenderer.setView(camera);
        player.update();
        for (int i = 0; i < enemies.size(); ++i) {
            enemies.get(i).update();
            enemies.get(i).move();
            if (player.getY() > enemies.get(i).getY()) {
                //enemies.get(i).jump();
            }
            batch.begin();
            batch.draw(Textures.player_image, enemies.get(i).getX() - 16, enemies.get(i).getY() - 16, 32, 32);
            batch.end();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
    }

    private void CameraUpdate() {
        Vector3 position = camera.position;
        position.x = Math.round(player.getBody().getPosition().x * PPM * 10) / 10f;
        position.y = Math.round(player.getBody().getPosition().y * PPM * 10) / 10f;
        camera.position.set(position);
        camera.update();
    }

    @Override
    public void render (float delta) {
        this.update();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        orthogonalTiledMapRenderer.render();

        batch.begin();
        batch.draw(Textures.player_image, player.getX() - 16, player.getY() - 16, 32, 32);
        if (player.cooldown_swing >= 180 && Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            flag = true;
            while (flag && player.cooldown_swing >= 120) {
                player.cooldown_swing--;
                batch.draw(Textures.swing_image, player.getX() + 16, player.getY() + 4, 32, 8);
            }
            flag = false;
            player.cooldown_swing = 0;
        }

        foont.getData().setScale(0.4f);
        foont.draw(batch, "seconds before slam:" + (20 - player.cooldown_slam / 60),player.getX() + 60, player.getY() + 60);
        foont.draw(batch, "seconds before boost:" + (10 - player.cooldown_boost / 60),player.getX() + 60, player.getY() + 45);
        foont.draw(batch, "seconds before swing:" + (3 - player.cooldown_swing / 60),player.getX() + 60, player.getY() + 30);
        if (abs(player.getX() - 100) < 20) {
            batch.draw(Textures.jump_tut, 100, 0, 96, 48);
        }
        if (abs(player.getX() - 250) < 20) {
            batch.draw(Textures.double_tut, 250, 0, 128, 48);
        }
        if (abs(player.getX() - 525) < 20) {
            batch.draw(Textures.boost_tut, 450, 0, 128, 48);
        }
        if (abs(player.getX() - 540) < 20) {
            batch.draw(Textures.slam_tut, 600, 0, 128, 48);
        }
        batch.end();
        box2DDebugRenderer.render(world, camera.combined.scl(PPM));
    }

    public World getWorld() {
        return world;
    }
    public void setPlayer(Player player) {
        this.player = player;
    }
    public void setEnemies(ArrayList<Enemy> enemies) {
        this.enemies = enemies;
    }
}
