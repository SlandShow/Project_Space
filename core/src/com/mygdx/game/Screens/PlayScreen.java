package com.mygdx.game.Screens;

import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.GameLogic.GameLoader;
import com.mygdx.game.GameLogic.GameState;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Scenes.Hud;
import com.mygdx.game.Sprites.Player;
import com.mygdx.game.Tools.B2WorldCreater;


/* This class - element of GameState. It`s first level of the game */
// TODO: RENAME THIS CLASS. FROM "PlayScene" TO "FirstLevelScene"
public class PlayScreen extends GameState{

    private MyGdxGame game;
    Texture texture;
    private OrthographicCamera gameCam;
    private Viewport gamePort;
    private Hud hud;
    private TmxMapLoader mapLoader; // for downloading tiles
    private TiledMap map;
    private OrthogonalTiledMapRenderer render;
    private World world;
    private Box2DDebugRenderer b2dr;
    private Player player;

    // light
    private PointLight light;
    private RayHandler rayHandlerh;


    public PlayScreen() {

        texture = new Texture("badlogic.jpg");
        gameCam = new OrthographicCamera();
        gamePort = new FitViewport(
                MyGdxGame.V_WIDTH / MyGdxGame.PPM,
                MyGdxGame.V_HEIGHT / MyGdxGame.PPM, gameCam);
        hud = new Hud(game.batch);

        // load map
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("maps/Gortiven.tmx");
        render = new OrthogonalTiledMapRenderer(map, 1 / MyGdxGame.PPM);
        gameCam.position.set(
                gamePort.getWorldWidth() / 2,
                gamePort.getWorldHeight() / 2,
                2);

        // physics and polygon system
        world = new World(new Vector2(0, -10), true);
        b2dr = new Box2DDebugRenderer();
        new B2WorldCreater(world, map); // create polygons around the map
        player = new Player(world); // create player


        


    }

    @Override
    public void handleInput(float dt) {
        // jumping
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP))
            player.b2body.applyLinearImpulse(
                    new Vector2(0, 4f),
                    player.b2body.getWorldCenter(),
                    true);
        // turn right
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) &&
                player.b2body.getLinearVelocity().x <= 2)
            player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true);

        // turn left
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) &&
                player.b2body.getLinearVelocity().x >= -2)
            player.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2body.getWorldCenter(), true);


        // escape button - game on pause
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            GameLoader.currentIndex = GameLoader.MENU_PAUSE_STATE;
            GameLoader.gameLoader.addState(new MenuPauseScene());
            GameLoader.gameLoader.setNewState();
        }

    }

    @Override
    public void update(float dt) {
        handleInput(dt);
        world.step(1 / 60f, 6, 2);
        // change camera position
        gameCam.position.x = player.b2body.getPosition().x;
        gameCam.update();
        render.setView(gameCam);

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

        // render game map
        render.render();

        // render box2dbodies
        b2dr.render(world, gameCam.combined);

        // set batch to draw camera
        MyGdxGame.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();

    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
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
        map.dispose();
        render.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();
    }
}
