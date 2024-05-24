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

public class TitleScreen extends ScreenAdapter {
    public PlatformerMain instance;
    OrthographicCamera camera;
    SpriteBatch batch;
    BitmapFont font;
    Drawable startdraw = new TextureRegionDrawable(new Texture("startbutton.png"));
    Drawable exitdraw = new TextureRegionDrawable(new Texture("exitbutton.png"));
    ImageButton gamestart = new ImageButton(startdraw);
    ImageButton gameoff = new ImageButton(exitdraw);
    Stage stage = new Stage(new ScreenViewport());
    public TitleScreen(OrthographicCamera orthographicCamera, PlatformerMain instance) {
        Gdx.input.setInputProcessor(stage);
        this.camera = orthographicCamera;
        this.batch = new SpriteBatch();
        this.font = new BitmapFont(Gdx.files.internal("assets/fonnt.fnt"), false);
        this.instance = instance;
        Music menuMusic = Gdx.audio.newMusic(Gdx.files.internal("assets\\menu_music.ogg"));
        menuMusic.setLooping(true);
        menuMusic.play();
        //gamestart.setPosition( 0, 0);
        //gamestart.setPosition(Gdx.graphics.getWidth() * 0.05f, Gdx.graphics.getHeight() * 0.0f);
        //gameoff.setPosition( Gdx.graphics.getWidth() * .65f, Gdx.graphics.getHeight() * 0.0f);
        int row_height = Gdx.graphics.getWidth() / 12;
        int col_width = Gdx.graphics.getWidth() / 12;
        ImageButton button3 = new ImageButton(startdraw);
        button3.setSize(col_width*4,(float)(row_height*2));
        button3.getStyle().imageDown = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("slam_tut.png"))));
        button3.setPosition(col_width,Gdx.graphics.getHeight()-row_height*6);
        button3.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("a");
                menuMusic.stop();
                hide();
                instance.setScreen(new GameScreen(camera, instance));
                return true;
            }
            @Override
            public boolean keyDown(InputEvent event, int keyCode) {
                System.out.println("a");
                menuMusic.stop();
                hide();
                instance.setScreen(new GameScreen(camera, instance));
                return true;
            }
        });
        stage.addActor(button3);
    }
    @Override
    public void show(){
        /*Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keyCode) {
                if (keyCode == Input.Keys.SPACE) {
                    instance.setScreen(new GameScreen(camera, instance));
                }
                return true;
            }
        });*/
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, .25f, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime()); //Perform ui logic
        stage.draw(); //Draw the ui
    }

    @Override
    public void hide(){
        Gdx.input.setInputProcessor(null);
    }
}
