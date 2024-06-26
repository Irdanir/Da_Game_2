package com.mygdx.game;

import static com.badlogic.gdx.Input.Keys.R;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import javax.swing.text.View;

import objects.player.Player;

public class TitleScreen extends ScreenAdapter {
    public PlatformerMain instance;
    OrthographicCamera camera;
    SpriteBatch batch;
    BitmapFont font;
    Drawable startdraw = new TextureRegionDrawable(new Texture("start_button.png"));
    Texture background = new Texture("menu_image.jpg");
    Drawable exitdraw = new TextureRegionDrawable(new Texture("exit_button.png"));
    Stage stage = new Stage(new ScreenViewport());
    public TitleScreen(OrthographicCamera orthographicCamera, PlatformerMain instance) {
        Gdx.input.setInputProcessor(stage);
        this.camera = orthographicCamera;
        this.batch = new SpriteBatch();
        this.font = new BitmapFont(Gdx.files.internal("fonnt.fnt"), false);
        this.instance = instance;
        Music menuMusic = Gdx.audio.newMusic(Gdx.files.internal("menu_music.ogg"));
        menuMusic.setLooping(true);
        menuMusic.play();
        int row_height = Gdx.graphics.getWidth() / 12;
        int col_width = Gdx.graphics.getWidth() / 12;
        ImageButton gamestart = new ImageButton(startdraw);
        gamestart.setSize(col_width*4,(float)(row_height*2));
        gamestart.getStyle().imageDown = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("start_button.png"))));
        gamestart.setPosition(col_width,Gdx.graphics.getHeight()-row_height*6);
        gamestart.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("a");
                menuMusic.stop();
                instance.setScreen(new GameScreen(camera, instance));
                return true;
            }
            /*@Override
            public boolean keyDown(InputEvent event, int keyCode) {
                System.out.println("a");

                instance.setScreen(new GameScreen(camera, instance));
                return true;
            }*/
        });
        ImageButton gameexit = new ImageButton(exitdraw);
        gameexit.setSize(col_width*4,(float)(row_height*2));
        gameexit.getStyle().imageDown = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("exit_button.png"))));
        gameexit.setPosition(col_width * 7,Gdx.graphics.getHeight()-row_height*6);
        gameexit.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.exit();
                return true;
            }
            /*@Override
            public boolean keyDown(InputEvent event, int keyCode) {
                System.out.println("a");

                instance.setScreen(new GameScreen(camera, instance));
                return true;
            }*/
        });
        stage.addActor(gamestart);
        stage.addActor(gameexit);
    }
    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, .25f, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(background, 0 , 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();
        stage.act(Gdx.graphics.getDeltaTime()); //Perform ui logic
        stage.draw(); //Draw the ui
    }

    @Override
    public void hide(){
        Gdx.input.setInputProcessor(null);
    }
}
