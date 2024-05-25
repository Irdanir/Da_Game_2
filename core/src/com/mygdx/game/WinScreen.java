package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import objects.player.Player;

public class WinScreen extends ScreenAdapter {
    public PlatformerMain instance;
    OrthographicCamera camera;
    SpriteBatch batch;
    BitmapFont font;
    Player player;
    public WinScreen(OrthographicCamera orthographicCamera, PlatformerMain instance, Player player) {
        this.camera = orthographicCamera;
        this.batch = new SpriteBatch();
        this.font = new BitmapFont(Gdx.files.internal("fonnt.fnt"), false);
        font.getData().setScale(1, 1);
        this.instance = instance;
        this.player = player;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keyCode) {
                if (keyCode == Input.Keys.SPACE) {
                    instance.setScreen(new TitleScreen(camera, instance));
                }
                if (keyCode == Input.Keys.ESCAPE) {
                    Gdx.app.exit();
                }
                return true;
            }
        });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(.25f, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        font.draw(batch, "You win!!", Gdx.graphics.getWidth() * .25f, Gdx.graphics.getHeight() * .75f);
        font.draw(batch, "Health left: " + player.health, Gdx.graphics.getWidth() * .25f, Gdx.graphics.getHeight() * .65f);
        font.draw(batch, "Press 'restart' to restart the game.", Gdx.graphics.getWidth() * .25f, Gdx.graphics.getHeight() * .25f);
        font.draw(batch, "Press 'exit' to close the app.", Gdx.graphics.getWidth() * .25f, Gdx.graphics.getHeight() * .50f);
        font.draw(batch, "Data is not saved when you close.", Gdx.graphics.getWidth() * .25f, Gdx.graphics.getHeight() * 0.1f);
        batch.end();
    }

    @Override
    public void hide(){
        Gdx.input.setInputProcessor(null);
    }
}
