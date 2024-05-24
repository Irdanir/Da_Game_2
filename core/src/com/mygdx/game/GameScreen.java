package com.mygdx.game;

import static java.lang.Math.abs;
import static helper.Constants.PPM;

import helper.BodyHelperService;
import helper.Textures;
import helper.Collision;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.DelayedRemovalArray;

import java.util.ArrayList;

import helper.TileMapHelper;
import objects.player.Bullet;
import objects.player.Enemy;
import objects.player.Player;

public class GameScreen extends ScreenAdapter {
    public PlatformerMain instance;
    private OrthographicCamera camera;
    private Batch batch;
    private World world;
    private Box2DDebugRenderer box2DDebugRenderer;
    private OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;
    private TileMapHelper tileMapHelper;
    private Player player;
    private DelayedRemovalArray<Enemy> enemies = new DelayedRemovalArray<>();
    private DelayedRemovalArray<Bullet> bullets = new DelayedRemovalArray<>();
    private int bulletsize = 0;
    BitmapFont foont = new BitmapFont(Gdx.files.internal("assets/fonnt.fnt"), false);
    Music gameMusic = Gdx.audio.newMusic(Gdx.files.internal("assets\\game_music.ogg"));
    public GameScreen (OrthographicCamera camera, PlatformerMain instance) {
        this.camera = camera;
        this.batch = new SpriteBatch();
        this.world = new World(new Vector2(0, -9.81f), false);
        this.box2DDebugRenderer = new Box2DDebugRenderer();
        this.tileMapHelper = new TileMapHelper();
        this.tileMapHelper.setGameScreen(this);
        this.orthogonalTiledMapRenderer = tileMapHelper.setupMap();
        this.instance = instance;

        gameMusic.setLooping(true);
        gameMusic.play();
    }
    //Добавить музыку, поворот при движении, гибель игрока, все записать в презентацию
    private void update() {
        world.step(1/60f, 6, 2);
        CameraUpdate();
        batch.setProjectionMatrix(camera.combined);
        orthogonalTiledMapRenderer.setView(camera);
        player.update();
        for (int i = 0; i < TileMapHelper.enemysize; ++i) {
            //System.out.println(TileMapHelper.enemysize);
            enemies.get(i).update(player);
            enemies.get(i).move();
            if (Collision.Body_collision(enemies.get(i), player)) {
                player.health -= 20;
                enemies.get(i).x -= 20 * enemies.get(i).direction;
            }
            for (int j = 0; j < bulletsize; ++j) {
                if (Collision.Body_collision(enemies.get(i), bullets.get(j)) == true) {
                    enemies.get(i).health -= 100;
                    //enemies.get(i).health -= 10;
                    world.destroyBody(bullets.get(j).getBody());
                    bullets.removeIndex(j);
                    bulletsize -= 1;
                }
            }
            if (enemies.get(i).health <= 0) {
                world.destroyBody(enemies.get(i).getBody());
                enemies.removeIndex(i);
                TileMapHelper.enemysize--;
            }
        }
        for (int i = 0; i < bulletsize; ++i) {
            bullets.get(i).update(player);
        }
        if (TileMapHelper.enemysize == 0) {
            gameMusic.stop();
            instance.setScreen(new EndScreen(camera, instance));
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
        if (player.direction == 1) {
            batch.draw(Textures.player_right, player.getX() - 16, player.getY() - 16, 32, 32);
        } else {
            batch.draw(Textures.player_left, player.getX() - 16, player.getY() - 16, 32, 32);
        }
        for (int i = 0; i < TileMapHelper.enemysize; ++i) {
            if (enemies.get(i).direction == 1) {
                batch.draw(Textures.goblin_right, enemies.get(i).getX() - 16, enemies.get(i).getY() - 16, 32, 32);
            } else {
                batch.draw(Textures.goblin_left, enemies.get(i).getX() - 16, enemies.get(i).getY() - 16, 32, 32);
            }
        }
        for (int i = 0; i < bulletsize; ++i) {
            batch.draw(Textures.bullet_image, bullets.get(i).getX() - 2.5f, bullets.get(i).getY() - 2.5f, 5f, 5f);
        }
        if (player.cooldown_shoot >= 180 && Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            bulletsize++;
            Body body = BodyHelperService.createBody(
                    player.getX() + (player.getWidth()) * player.direction,
                    player.getY() + player.getHeight() / 2 - 5,
                    5f, 5f, false, this.getWorld()
            );
            bullets.add(new Bullet(5f, 5f, body));
            player.cooldown_shoot = 0;
        }

        foont.getData().setScale(0.4f);
        foont.draw(batch, "slam cooldown:" + (20 - player.cooldown_slam / 60),player.getX() - 270, player.getY() + 210);
        foont.draw(batch, "boost cooldown:" + (10 - player.cooldown_boost / 60),player.getX() - 270, player.getY() + 180);
        foont.draw(batch, "shoot cooldown:" + (3 - player.cooldown_shoot / 60),player.getX() - 270, player.getY() + 150);
        foont.draw(batch, "health:" + (player.health),player.getX() - 270, player.getY() + 120);
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
        //box2DDebugRenderer.render(world, camera.combined.scl(PPM));
    }

    public World getWorld() {
        return world;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
    public void setEnemies(DelayedRemovalArray<Enemy> enemies) {
        this.enemies = enemies;
    }
}
