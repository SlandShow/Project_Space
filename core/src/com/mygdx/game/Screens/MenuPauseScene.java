package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.mygdx.game.GameLogic.GameLoader;
import com.mygdx.game.GameLogic.GameState;
import com.mygdx.game.MyGdxGame;


/* This class - element of GameState. It`s Menu Pause scene.
 When user press "Exit Button" in N level, we must draw pause scene.
 Here we can resume game, change settings and go to Main Menu */
public class MenuPauseScene extends GameState {


    private BitmapFont font;
    private GameLoader load;

    public MenuPauseScene() {
        load = GameLoader.gameLoader;
        font = new BitmapFont();
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        //
        update(delta);

        // clear game screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // draw menu
        MyGdxGame.batch.begin();
        font.draw(MyGdxGame.batch, "Game on Pause", 250, 250);
        MyGdxGame.batch.end();

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public void handleInput(float dt) {

        // TODO: MAKE NORMAL PAUSE SCENE!

        // reset game
        if (Gdx.input.isKeyPressed(Input.Keys.R)) {
            GameLoader.currentIndex = GameLoader.LVL_1;
            GameLoader.gameLoader.setNewState();
        }

        // go to game menu
        if (Gdx.input.isKeyPressed(Input.Keys.E)) {
            GameLoader.currentIndex = GameLoader.MENU_STATE;
            GameLoader.gameLoader.setNewState();
        }

    }

    @Override
    public void update(float dt) {
        System.out.println("Game on Pause!");

        handleInput(dt);

    }
}
