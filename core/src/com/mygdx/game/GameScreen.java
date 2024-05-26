package com.mygdx.game;

import static java.lang.Math.abs;
import static helper.Constants.PPM;

import helper.BodyHelperService;
import helper.Textures;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

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
    protected Player player;
    private DelayedRemovalArray<Enemy> enemies = new DelayedRemovalArray<>();
    private DelayedRemovalArray<Bullet> bullets = new DelayedRemovalArray<>();
    private int bulletsize = 0;
    BitmapFont foont = new BitmapFont(Gdx.files.internal("fonnt.fnt"), false);
    Music gameMusic = Gdx.audio.newMusic(Gdx.files.internal("game_music.ogg"));
    Stage stage;
    ImageButton jumpbutton;
    ImageButton leftbutton;
    ImageButton rightbutton;
    ImageButton shootbutton;
    int row_height = Gdx.graphics.getWidth() / 12;
    int col_width = Gdx.graphics.getWidth() / 12;

    public GameScreen (OrthographicCamera camera, PlatformerMain instance) {
        this.camera = camera;
        camera.zoom -= 0.65f;
        this.batch = new SpriteBatch();
        this.world = new World(new Vector2(0, -9.81f), false);
        this.box2DDebugRenderer = new Box2DDebugRenderer();
        this.tileMapHelper = new TileMapHelper();
        this.tileMapHelper.setGameScreen(this);
        this.orthogonalTiledMapRenderer = tileMapHelper.setupMap();
        this.instance = instance;
        stage = new Stage(new ScreenViewport());
    }
    @Override
    public void show () {
        gameMusic.setLooping(true);
        gameMusic.play();
        Gdx.input.setInputProcessor(stage);
        Drawable rightdraw = new TextureRegionDrawable(new Texture("right_arrow_red.png"));
        rightbutton = new ImageButton(rightdraw);
        rightbutton.setSize(col_width*1,(float)(row_height*2));
        rightbutton.getStyle().imageDown = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("right_arrow_red.png"))));
        rightbutton.setPosition(col_width * 1f,Gdx.graphics.getHeight()-row_height*6);
        rightbutton.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                player.velX = 1;
                player.direction = 1;
                return true;
            }
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                player.velX = 0;
            }

        });
        Drawable leftdraw = new TextureRegionDrawable(new Texture("left_arrow_red.png"));
        leftbutton = new ImageButton(leftdraw);
        leftbutton.setSize(col_width*1,(float)(row_height*2));
        leftbutton.getStyle().imageDown = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("left_arrow_red.png"))));
        leftbutton.setPosition(0,Gdx.graphics.getHeight()-row_height*6);
        leftbutton.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                player.velX = -1;
                player.direction = -1;
                return true;
            }
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                player.velX = 0;
            }

        });
        Drawable shootdraw = new TextureRegionDrawable(new Texture("pointer.jpg"));
        shootbutton = new ImageButton(shootdraw);
        shootbutton.setSize(col_width*1,(float)(row_height*2));
        shootbutton.getStyle().imageDown = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("pointer.jpg"))));
        shootbutton.setPosition(col_width * 2f,Gdx.graphics.getHeight()-row_height*6);
        shootbutton.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (player.cooldown_shoot >= 180) {
                    bulletsize++;
                    Body body = BodyHelperService.createBody(
                            player.getX() + (player.getWidth()) * player.direction,
                            player.getY() + player.getHeight() / 2 - 5,
                            5f, 5f, false, world);
                    bullets.add(new Bullet(5f, 5f, body, player.direction));
                    player.cooldown_shoot = 0;
                    player.health -= 5;
                }
                return true;
            }
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {}

        });
        Drawable jumpdraw = new TextureRegionDrawable(new Texture("up_arrow_red.png"));
        jumpbutton = new ImageButton(jumpdraw);
        jumpbutton.setSize(col_width*1, (row_height*1.75f));
        jumpbutton.getStyle().imageDown = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("up_arrow_red.png"))));
        jumpbutton.setPosition(col_width * 3f,Gdx.graphics.getHeight()-row_height*6);
        jumpbutton.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (player.jumpCounter < 2) {
                    float force = player.body.getMass() * 5;
                    player.body.setLinearVelocity(player.body.getLinearVelocity().x, 0);
                    player.body.applyLinearImpulse(new Vector2(0, force), player.body.getPosition(), true);
                    player.jumpCounter++;
                }
                return true;
            }
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {}

        });
        stage.addActor(rightbutton);
        stage.addActor(leftbutton);
        stage.addActor(shootbutton);
        stage.addActor(jumpbutton);
    }
    private void update() {
        world.step(1/60f, 6, 2);
        CameraUpdate();
        batch.setProjectionMatrix(camera.combined);
        orthogonalTiledMapRenderer.setView(camera);
        player.update();
        leftbutton.setPosition(col_width * 0f,Gdx.graphics.getHeight()-row_height*6);
        rightbutton.setPosition(col_width * 1f,Gdx.graphics.getHeight()-row_height*6);
        shootbutton.setPosition(col_width * 10f,Gdx.graphics.getHeight()-row_height*6f);
        jumpbutton.setPosition(col_width * 11f,Gdx.graphics.getHeight()-row_height*6f);
        Rectangle player_rect = new Rectangle(player.getX(), player.getY(), player.getWidth(), player.getHeight());
        for (int i = 0; i < TileMapHelper.enemysize; ++i) {
            Enemy enemy = enemies.get(i);
            enemies.get(i).update(player);
            enemies.get(i).move();
            Rectangle enemy_rect = new Rectangle(enemy.getX(), enemy.getY(), enemy.getWidth(), enemy.getHeight());
            if (enemy_rect.overlaps(player_rect)) {
                player.health = 0;
            }
            for (int j = 0; j < bulletsize; ++j) {
                Bullet bullet = bullets.get(j);
                Rectangle bullet_rect = new Rectangle(bullet.getX(), bullet.getY(), bullet.getWidth(), bullet.getHeight());
                if (enemy_rect.overlaps(bullet_rect) || bullets.get(j).delete == true) {
                    enemies.get(i).health -= 100;
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
            hide();
            instance.setScreen(new WinScreen(camera, instance, player));
        }
        if (player.health == 0) {
            gameMusic.stop();
            hide();
            instance.setScreen(new LoseScreen(camera, instance, TileMapHelper.enemysize));
        }
    }
    @Override
    public void hide () {
        TileMapHelper.enemysize = 0;
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
        stage.act(); //Perform ui logic
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
        foont.getData().setScale(0.4f);
        foont.draw(batch, "shoot cooldown:" + (3 - player.cooldown_shoot / 60),player.getX() - 270, player.getY() + 90);
        foont.draw(batch, "health:" + (player.health),player.getX() - 270, player.getY() + 60);
        if (abs(player.getX() - 100) < 20) {
            batch.draw(Textures.jump_tut, 100, 0, 96, 48);
        }
        if (abs(player.getX() - 250) < 20) {
            batch.draw(Textures.double_tut, 250, 0, 128, 48);
        }
        batch.end();
        stage.draw(); //Draw the ui
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
