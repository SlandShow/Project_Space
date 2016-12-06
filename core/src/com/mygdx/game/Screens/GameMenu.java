package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.GameLogic.GameLoader;
import com.mygdx.game.GameLogic.GameState;
import com.mygdx.game.MyGdxGame;

/* This class - element of GameState. It`s Main Menu, when user can load game, create new game, go to settings and exit game */
public class GameMenu extends GameState {

    private BitmapFont font;
    private OrthographicCamera cam;
    private GameLoader load;


    public GameMenu() {
        load = GameLoader.gameLoader;
        font = new BitmapFont();
        cam = new OrthographicCamera();
        // cam.position.set(50, 50, 2);

    }


    @Override
    public void handleInput(float dt) {
        // TODO: MAKE NORMAL MENU WITH BUTTONS!
        if (Gdx.input.isTouched()) {

            // чистим экран
           // Gdx.gl.glClearColor(0, 0, 0, 1);
            //Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            GameLoader.currentIndex = GameLoader.LVL_1;
            load.addState(new PlayScreen());
            load.setNewState();
        }
    }

    @Override
    public void update(float dt) {
        handleInput(dt);
        System.out.println("Game menu!");
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
        font.draw(MyGdxGame.batch, "Tap anywhere to begin", (MyGdxGame.V_WIDTH / 2), (MyGdxGame.V_HEIGHT / 2) );
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
        font.dispose();
    }
}
