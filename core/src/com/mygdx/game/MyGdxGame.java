package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.game.GameLogic.GameLoader;
import com.mygdx.game.GameLogic.GameState;
import com.mygdx.game.Screens.PlayScreen;

/* Start all game here */
public class MyGdxGame extends Game {
    public static SpriteBatch batch;
    Texture img;
    public static final int V_WIDTH = 400;
    public static final int V_HEIGHT = 208;
    public static final float PPM = 100;
    private GameLoader load;


    @Override
    public void create() {
        batch = new SpriteBatch();
        load = new GameLoader(this);
        setScreen(load.getCurrentState());

    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
        img.dispose();
    }

}
