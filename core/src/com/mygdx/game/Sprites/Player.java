package com.mygdx.game.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g3d.particles.values.MeshSpawnShapeValue;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.BulletsResurses.Box2DBullets;
import com.mygdx.game.BulletsResurses.GameBullets;
import com.mygdx.game.GameLogic.GameLoader;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Screens.MenuPauseScene;
import com.mygdx.game.Screens.PlayScreen;
import com.mygdx.game.Tools.B2WorldCreater;
import com.mygdx.game.Tools.WorldContactListener;

import java.util.ArrayList;

public class Player extends Sprite {


    public enum State {
        NONE, WALK, DEAD, JUMPING, FACTING_LEFT, FACTING_RIGHT
    }

    // основа для использования движка Box2D
    private B2WorldCreater creater;

    // для физики
    public World world;
    public Body b2body;
    private SpriteBatch batch;
    private BodyDef bdef;
    private FixtureDef fdef;


    // для анимации
    int FRAME_COLS = 2;
    int FRAME_ROWS = 2;
    Animation walkAnimation;
    Texture walkSheet;
    TextureRegion[] walkFrames;
    TextureRegion currentFrame;
    Animation stayAnimation;
    Texture staySheet;
    TextureRegion[] stayFrames;
    float stateTime;

    // для физики движения
    private boolean moveRight;
    private boolean moveleft;
    public final static float MAX_VELOCITY = 3f;
    public final static float SPEED = 5f;
    public final static float SIZE = 0.8f;
    private Vector2 velocity;
    private Vector2 position;
    private boolean isJump;
    private boolean stay;

    // для реализации сенсера
    protected Fixture fixture;
    private Rectangle rect;

    // для системы XP
    private int hp;
    private BitmapFont font;
    private boolean isAlive;
    private Texture hp_texture;

    // пули
    public ArrayList<GameBullets> bullets = new ArrayList<GameBullets>();
    public static ArrayList<Box2DBullets> bullets_list = new ArrayList<Box2DBullets>();
    private boolean shoot;
    public static State PLAYER_STATE;


    public Vector2 getVelocity() {
        return velocity;
    }

    public Vector2 getPositionVector() {
        return position;
    }

    public void setBatch(SpriteBatch batch) {
        this.batch = batch;
    }

    public void setMoveRight(boolean moveRight) {
        this.moveRight = moveRight;
    }

    public void setMoveleft(boolean moveleft) {
        this.moveleft = moveleft;
    }

    public Player(World world, PlayScreen screen, B2WorldCreater creater) {
        this.creater = creater;
        this.world = world;
        PLAYER_STATE = State.FACTING_RIGHT; // по умолчянию игрок стоит и смотрит направо
        definePlayer();
    }


    // create player
    public void definePlayer() {

        // физика
        velocity = new Vector2();
        position = new Vector2();

        // структура игрока на основе движка BOX2D

        // часть 1
        bdef = new BodyDef();
        bdef.position.set(80 / MyGdxGame.PPM, 200 / MyGdxGame.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(25 / MyGdxGame.PPM);

        fdef.shape = shape;
        b2body.createFixture(fdef);

        // часть 2

        rect = new Rectangle();
        rect.setPosition(100 / MyGdxGame.PPM, 190 / MyGdxGame.PPM);

        bdef.position.set(rect.getX(), rect.getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        PolygonShape poly = new PolygonShape();
        poly.setAsBox(8 / MyGdxGame.PPM, 8 / MyGdxGame.PPM);
        fdef.shape = poly;
        b2body.createFixture(fdef).setUserData("player");


        // создаем сенсер для игрока (система обработки коллизий)
        /*EdgeShape head = new EdgeShape();
        head.set(new Vector2(-3 / MyGdxGame.PPM, -10 / MyGdxGame.PPM),
                new Vector2(35 / MyGdxGame.PPM, -10 / MyGdxGame.PPM));
        fdef.shape = head;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData("head");*/

        // инициализация спрайтовой анимации
        walkSheet = new Texture(Gdx.files.internal("sprites/player/walk/Hero2.png"));
        TextureRegion[][] tmp = TextureRegion.split(walkSheet, walkSheet.getWidth() / FRAME_COLS, walkSheet.getHeight() / FRAME_ROWS);
        walkFrames = new TextureRegion[FRAME_ROWS * FRAME_COLS];
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                walkFrames[index++] = tmp[i][j];
            }
        }

        walkAnimation = new Animation(0.3f, walkFrames);
        stateTime = 0f;

        staySheet = new Texture(Gdx.files.internal("sprites/player/stay/hero_stay.png"));
        tmp = TextureRegion.split(staySheet, staySheet.getWidth() / FRAME_COLS, staySheet.getHeight() / FRAME_ROWS);
        stayFrames = new TextureRegion[FRAME_ROWS * FRAME_COLS];
        index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                stayFrames[index++] = tmp[i][j];
            }
        }

        stayAnimation = new Animation(0.3f, stayFrames);

        // HP система
        hp = 100;
        isAlive = true;
        hp_texture = new Texture("huds/heart.png");
        font = new BitmapFont();


    }


    public void handleInput(float dt) {
        // do nothing
        setStay(true);
        setMoveleft(false);
        setMoveRight(false);
        setJump(false);

        // jumping
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            b2body.applyLinearImpulse(new Vector2(0, 4f), b2body.getWorldCenter(), true);
            setJump(true);
            setStay(false);

            //light.setPosition( player.b2body.getLinearVelocity().x , player.b2body.getLinearVelocity().y );
        }
        // turn right
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) &&
                b2body.getLinearVelocity().x <= 2) {
            b2body.applyLinearImpulse(new Vector2(0.1f, 0), b2body.getWorldCenter(), true);
            // player.getVelocity().x += Player.SPEED;
            setMoveRight(true);
            setMoveleft(false);
            setStay(false);
            //  gameCam.translate(5 / MyGdxGame.PPM, 0, 0);
        }
        // turn left
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) &&
                b2body.getLinearVelocity().x >= -2) {
            b2body.applyLinearImpulse(new Vector2(-0.1f, 0), b2body.getWorldCenter(), true);
            //player.getVelocity().x =- Player.SPEED;
            setMoveRight(false);
            setMoveleft(true);
            setStay(false);
            // gameCam.translate(-5 / MyGdxGame.PPM, 0, 0);
        }

        // escape button - game on pause
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            GameLoader.currentIndex = GameLoader.MENU_PAUSE_STATE;
            GameLoader.gameLoader.addState(new MenuPauseScene());
            GameLoader.gameLoader.setNewState();
        }

        // стрельба
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            shoot = true;
        }
    }


    public void render(SpriteBatch batch, float dt) {

        // анимация
        stateTime += Gdx.graphics.getDeltaTime();

        // движение
        if (stay) {
            currentFrame = stayAnimation.getKeyFrame(stateTime, true);
            batch.draw(currentFrame, (getX() - 22 / MyGdxGame.PPM ), getY() - 25 / MyGdxGame.PPM, 50f / MyGdxGame.PPM, 50f / MyGdxGame.PPM);
        }

        if (moveRight) {
            currentFrame = walkAnimation.getKeyFrame(stateTime, true);
            batch.draw(currentFrame, (getX() - 30 / MyGdxGame.PPM ), getY() - 30 / MyGdxGame.PPM, 70f / MyGdxGame.PPM, 70f / MyGdxGame.PPM);

            PLAYER_STATE = State.FACTING_RIGHT;

            // поворот для пуль
            for (Box2DBullets bullet : bullets_list) {
                bullet.setBulletTurnRight(true);
                bullet.setBulletTurnLeft(false);
                System.out.println(bullet.getBulletTurnLeft() + " " + bullet.getBulletTurnRight());
            }
        }

        if (moveleft) {
            currentFrame = walkAnimation.getKeyFrame(stateTime, true);
            batch.draw(currentFrame, (getX() - 30 / MyGdxGame.PPM ), getY() - 30 / MyGdxGame.PPM, 70f / MyGdxGame.PPM, 70f / MyGdxGame.PPM);

            PLAYER_STATE = State.FACTING_LEFT;

            // поворот для пуль
            for (Box2DBullets bullet : bullets_list) {
                bullet.setBulletTurnRight(false);
                bullet.setBulletTurnLeft(true);
                System.out.println(bullet.getBulletTurnLeft() + " " + bullet.getBulletTurnRight());
            }
        }

        if (isJump) {
            currentFrame = walkAnimation.getKeyFrame(stateTime, true);
            batch.draw(currentFrame, (getX() - 30 / MyGdxGame.PPM ), getY() - 30 / MyGdxGame.PPM, 70f / MyGdxGame.PPM, 70f / MyGdxGame.PPM);
        }

        // Система HP
        batch.draw(hp_texture, b2body.getPosition().x - 1, b2body.getPosition().y + 1, 50 / MyGdxGame.PPM, 50 / MyGdxGame.PPM);
        // font.draw(batch, "Player HP: " + hp, b2body.getPosition().x / MyGdxGame.PPM, b2body.getPosition().y, 0, 0, true);
        for (GameBullets bull : bullets) {
            bull.update();
            bull.draw(batch);
        }

        // стрельба
        if (shoot) {
            bullets_list.add(new Box2DBullets(creater, b2body.getPosition().x, b2body.getPosition().y));
            shoot = false;
        }


    }

    public TextureRegion getTextureGame() {
        return currentFrame;
    }

    public void hit(int damage) {
        hp -= damage;
    }

    private void checkLive() {
        if (hp <= 0) isAlive = false;
        if (!isAlive) ; // TODO: СДЕЛАТЬ ЭКРАН ПЕРЕЗАПУСКА ИГРЫ
    }


    public int getHp() {
        return hp;
    }


    public void update(float dt) {
        // для корректной отрисовки текстуры
        setPosition((b2body.getPosition().x - getWidth() / 2), (b2body.getPosition().y - getHeight() / 2));
        // проверка HP игрока
        checkLive();

        // обновление пуль
        for (Box2DBullets bullet : bullets_list) {
            bullet.update(dt);
        }

    }

    public void setJump(boolean b) {
        isJump = b;

    }

    public void setStay(boolean b) {
        stay = b;
    }

    public void resetVelocity() {
        getVelocity().x = 0;
        getVelocity().y = 0;
    }

    public Body getBodyPlayer() {
        return b2body;
    }

    public Vector2 getPosition() {
        return b2body.getPosition();
    }


    public void dispose() {

    }

    // для реализации света
    public Body getB2body() {
        return b2body;
    }
}
