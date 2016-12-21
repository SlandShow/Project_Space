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
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
import com.mygdx.game.Tools.GameWorldRender;
import com.mygdx.game.Tools.WorldContactListener;


/* This class - element of GameState. It`s first level of the game */
// TODO: RENAME THIS CLASS. FROM "PlayScene" TO "FirstLevelScene"
public class PlayScreen extends GameState {

    private MyGdxGame game;
    private OrthographicCamera gameCam;
    private Viewport gamePort;
    private Hud hud;
    private TmxMapLoader mapLoader; // for downloading tiles
    private TiledMap map;
    private OrthogonalTiledMapRenderer render;
    private OrthographicCamera mapCam;
    private World world;
    private Box2DDebugRenderer b2dr;
    private Player player;
    private Enemy enemy;
    private Texture img;
    private SpriteBatch batch;
    private GameWorldRender gameWorldRender; // для отрисовки карты

    // light sky
    private PointLight light;
    private RayHandler rayHandlerh;

    // player flash light
    private PointLight flashLight;
    private RayHandler flashHandler;

    public PlayScreen() {

        img = new Texture(Gdx.files.internal("sprites/player/stay/hero_stay.png"));

        // настройка ортографической камеры
        gameCam = new OrthographicCamera((MyGdxGame.V_WIDTH / MyGdxGame.PPM) * 2, (MyGdxGame.V_HEIGHT / MyGdxGame.PPM) * 2);
        // gamePort = new FitViewport(
        // MyGdxGame.V_WIDTH / MyGdxGame.PPM,
        //  MyGdxGame.V_HEIGHT / MyGdxGame.PPM, gameCam);
        // hud = new Hud(game.batch);


        // load map
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("maps/SpaceShipLevel.tmx");
        gameWorldRender = new GameWorldRender(map, 1 / MyGdxGame.PPM);
        //render = new OrthogonalTiledMapRenderer(map, 1 / MyGdxGame.PPM);


        // для рисования
        batch = MyGdxGame.batch;

        // physics and polygon system
        world = new World(new Vector2(0, -10), true);
        b2dr = new Box2DDebugRenderer();
        new B2WorldCreater(world, map); // create polygons around the map

        player = new Player(world, this); // create player
        player.setBatch(batch); // даем возможность рисовать
        gameCam.position.set(
                player.b2body.getPosition().x,
                player.b2body.getPosition().y,
                0);


        // make light
        rayHandlerh = new RayHandler(world);
        rayHandlerh.setAmbientLight(.5f);
        light = new PointLight(rayHandlerh, 1000, Color.PURPLE, 0.5f, 10, 10); // light = new PointLight(rayHandlerh, 45, Color.ORANGE, 1.6f, 10, 10);
        // light.attachToBody(player.getB2body());
        light.setXray(false);

        flashLight = new PointLight(rayHandlerh, 200, Color.DARK_GRAY, 1f, 10, 10);
        flashLight.setXray(false);

        // закрепляем обработчик коллизий на мир 1-го уровня
        world.setContactListener(new WorldContactListener());

    }


    @Override
    public void handleInput(float dt) {

        // do nothing
        player.setStay(true);
        player.setMoveleft(false);
        player.setMoveRight(false);
        player.setJump(false);

        // jumping
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            player.b2body.applyLinearImpulse(new Vector2(0, 4f), player.b2body.getWorldCenter(), true);
            player.setJump(true);
            player.setStay(false);

            //light.setPosition( player.b2body.getLinearVelocity().x , player.b2body.getLinearVelocity().y );
        }
        // turn right
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) &&
                player.b2body.getLinearVelocity().x <= 2) {
            player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true);
            // player.getVelocity().x += Player.SPEED;
            player.setMoveRight(true);
            player.setMoveleft(false);
            player.setStay(false);
            //  gameCam.translate(5 / MyGdxGame.PPM, 0, 0);
        }
        // turn left
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) &&
                player.b2body.getLinearVelocity().x >= -2) {
            player.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2body.getWorldCenter(), true);
            //player.getVelocity().x =- Player.SPEED;
            player.setMoveRight(false);
            player.setMoveleft(true);
            player.setStay(false);
            // gameCam.translate(-5 / MyGdxGame.PPM, 0, 0);
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
        //  gameCam.position.x = player.b2body.getPosition().x;


        // обновление и рендеринг света
        rayHandlerh.updateAndRender();
        light.setXray(false);
        light.setPosition((player.b2body.getPosition().x - player.getWidth() / 2), (player.b2body.getPosition().y - player.getHeight() / 2) + 10 / MyGdxGame.PPM); // свет"следует" за игроком

        flashLight.setPosition(50 / MyGdxGame.PPM, 200 / MyGdxGame.PPM);

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

        // обновление глав. игровой камеры
        gameCam.position.x = player.b2body.getPosition().x;
        gameCam.position.y = player.b2body.getPosition().y / 2;
        gameCam.update();
        batch.setProjectionMatrix(gameCam.combined);

        // render game map
        gameWorldRender.getMapRender().setView(gameCam);
        gameWorldRender.render(delta);


        // render box2dbodies - сделано для отладки Fixtures!
        b2dr.render(world, gameCam.combined);


        // set batch to draw camera
//        hud.stage.draw();
        // текстура игрока
        //batch.setProjectionMatrix(gameCam.combined);
        batch.begin();
        player.render(batch, delta);
        //batch.draw(img, 0, 0);
        batch.end();


        // light system update
        rayHandlerh.setCombinedMatrix(gameCam);
        rayHandlerh.render();


    }

    @Override
    public void resize(int width, int height) {
//        gamePort.update(width, height);
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
