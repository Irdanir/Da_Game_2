package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.Game;

public class PlatformerMain extends Game {

    public static PlatformerMain instance;
    private int screenwidth, screenheight;
    private OrthographicCamera orthographicCamera;

    public PlatformerMain() {
        instance = this;
    }

    @Override
    public void create () {
        this.screenwidth = Gdx.graphics.getWidth();
        this.screenheight = Gdx.graphics.getHeight();
        this.orthographicCamera = new OrthographicCamera();
        this.orthographicCamera.setToOrtho(false, screenwidth, screenheight);
        setScreen(new GameScreen(orthographicCamera));
    }
}
