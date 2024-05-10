package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

import helper.Textures;

public class TitleScreen extends ScreenAdapter {
    public PlatformerMain instance;
    OrthographicCamera camera;
    SpriteBatch batch;
    BitmapFont font;
    public TitleScreen(OrthographicCamera orthographicCamera, PlatformerMain instance) {
        this.camera = orthographicCamera;
        this.batch = new SpriteBatch();
        this.font = new BitmapFont(Gdx.files.internal("assets/fonnt.fnt"), false);
        this.instance = instance;
    }

    @Override
    public void show(){
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keyCode) {
                if (keyCode == Input.Keys.SPACE) {
                    instance.setScreen(new GameScreen(camera, instance));
                }
                return true;
            }
        });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, .25f, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        font.draw(batch, "Title Screen!", Gdx.graphics.getWidth() * .25f, Gdx.graphics.getHeight() * .75f);
        font.draw(batch, "Kill all the enemies to win.", Gdx.graphics.getWidth() * .25f, Gdx.graphics.getHeight() * .5f);
        font.draw(batch, "Press space to play.", Gdx.graphics.getWidth() * .25f, Gdx.graphics.getHeight() * .25f);
        batch.end();
    }

    @Override
    public void hide(){
        Gdx.input.setInputProcessor(null);
    }
}
