package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.GameLogic.GameLoader;
import com.mygdx.game.GameLogic.GameState;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Scenes.GameStarEffect;


/* This class - element of GameState. It`s Menu Pause scene.
 When user press "Exit Button" in N level, we must draw pause scene.
 Here we can resume game, change settings and go to Main Menu */
public class MenuPauseScene extends GameState {


    private OrthographicCamera cam;
    private Viewport viewport;
    private SpriteBatch batch;
    private ShapeRenderer sr;
    private BitmapFont font;
    private GameLoader load;
    private Array<GameStarEffect> starts; // визуальный эффект как в Star Wars
    private float speed = 90f;
    private int numOfStars = 500;

    public MenuPauseScene() {
        load = GameLoader.gameLoader;
        font = new BitmapFont();

        cam = new OrthographicCamera();
        viewport = new FitViewport(MyGdxGame.V_WIDTH, MyGdxGame.V_HEIGHT, cam);
        viewport.apply();
        cam.position.set(MyGdxGame.V_WIDTH / 2, MyGdxGame.V_HEIGHT / 2, 0);

        sr = new ShapeRenderer();
        batch = new SpriteBatch();
        starts = new Array<GameStarEffect>();

        for (int i = 0; i < numOfStars ; i++) {
            starts.add(new GameStarEffect());
        }
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {



        // clear game screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(cam.combined);
        cam.update();


        // draw menu
        batch.begin();
        //font.draw(MyGdxGame.batch, "Game on Pause", (MyGdxGame.V_WIDTH / 2), (MyGdxGame.V_HEIGHT / 2));
        batch.end();

        sr.begin(ShapeRenderer.ShapeType.Filled);
        for (GameStarEffect start : starts) {
            start.update(speed);
            start.draw(sr);
        }
        sr.end();

        update(delta);


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
        sr.dispose();
        batch.dispose();

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

            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

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
