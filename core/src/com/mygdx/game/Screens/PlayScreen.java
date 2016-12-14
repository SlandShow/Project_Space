package com.mygdx.game.Screens;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
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
import com.mygdx.game.Sprites.Enemy;
import com.mygdx.game.Sprites.Player;
import com.mygdx.game.Sprites.SimpleEnemy;
import com.mygdx.game.Tools.B2WorldCreater;


/* This class - element of GameState. It`s first level of the game */
// TODO: RENAME THIS CLASS. FROM "PlayScene" TO "FirstLevelScene"
public class PlayScreen extends GameState {

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
    private Enemy enemy;
    private Texture img;
    private SpriteBatch batch;

    // light sky
    private PointLight light;
    private RayHandler rayHandlerh;

    // player flash light
    private PointLight flashLight;
    private RayHandler flashHandler;

    public PlayScreen() {


        gameCam = new OrthographicCamera();
        gamePort = new FitViewport(
                MyGdxGame.V_WIDTH / MyGdxGame.PPM,
                MyGdxGame.V_HEIGHT / MyGdxGame.PPM, gameCam);
        hud = new Hud(game.batch);

        // load map
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("maps/SpaceShipLevel.tmx");
        render = new OrthogonalTiledMapRenderer(map, 1 / MyGdxGame.PPM);
        gameCam.position.set(
                gamePort.getWorldWidth() / 2,
                gamePort.getWorldHeight() / 2,
                2);

        // для рисования
        batch = MyGdxGame.batch;

        // physics and polygon system
        world = new World(new Vector2(0, -10), true);
        b2dr = new Box2DDebugRenderer();
        new B2WorldCreater(world, map); // create polygons around the map

        player = new Player(world, this); // create player
        player.setBatch(batch); // даем возможность рисовать

        // make light
        rayHandlerh = new RayHandler(world);
        rayHandlerh.setAmbientLight(.5f);
        light = new PointLight(rayHandlerh, 200, Color.PURPLE, 0.35f, 10, 10); // light = new PointLight(rayHandlerh, 45, Color.ORANGE, 1.6f, 10, 10);
       // light.attachToBody(player.getB2body());
        light.setXray(false);


    }

    // public TextureAtlas getAtlas() {
    //return atlas;
    //}

    @Override
    public void handleInput(float dt) {

        // do nothing
        player.setStay(true);

        // jumping
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP) ) {
            player.b2body.applyLinearImpulse(new Vector2(0, 4f) , player.b2body.getWorldCenter(), true);
            player.setJump(true);
            player.setStay(false);
            light.setPosition( player.b2body.getLinearVelocity().x , player.b2body.getLinearVelocity().y );
        }
        // turn right
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) &&
                player.b2body.getLinearVelocity().x <= 2) {
            player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true);
           // player.getVelocity().x += Player.SPEED;
            player.setMoveRight(true);
            player.setMoveleft(false);
            player.setStay(false);
        }
        // turn left
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) &&
                player.b2body.getLinearVelocity().x >= -2) {
            player.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2body.getWorldCenter(), true);
            //player.getVelocity().x =- Player.SPEED;
            player.setMoveRight(false);
            player.setMoveleft(true);
            player.setStay(false);
        }

        // escape button - game on pause
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            GameLoader.currentIndex = GameLoader.MENU_PAUSE_STATE;
            GameLoader.gameLoader.addState(new MenuPauseScene());
            GameLoader.gameLoader.setNewState();
        }


    }

    @Override
    public void update(float dt) {

        // обновление ввода
        handleInput(dt);

        // корректируем время
        world.step(1 / 60f, 6, 2);

        // change camera position
        gameCam.position.x = player.b2body.getPosition().x;
        gameCam.update();
        render.setView(gameCam);



        // обновление и рендеринг света
        rayHandlerh.updateAndRender();
        //rayHandlerh.setCombinedMatrix(gameCam.combined.cpy().scl(MyGdxGame.PPM));
        light.setXray(false);
        light.setPosition(player.b2body.getPosition().x / MyGdxGame.PPM, player.b2body.getPosition().y / MyGdxGame.PPM ); // свет"следует" за игроком

        // обновление позиции игрока и проч.
        player.update(dt);

        // обновление врага
        for (Enemy enemy : B2WorldCreater.enemies)
            enemy.update(dt);


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
        render.setView(gameCam);


        // render box2dbodies
        b2dr.render(world, gameCam.combined);



        batch.begin();
        player.render(batch, delta);
        batch.end();


       // set batch to draw camera
        batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();


        // light system update
        rayHandlerh.render();



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
        rayHandlerh.dispose();
        batch.dispose();

    }
}
