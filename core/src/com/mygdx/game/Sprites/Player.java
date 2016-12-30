package com.mygdx.game.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g3d.particles.values.MeshSpawnShapeValue;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Screens.PlayScreen;
import com.mygdx.game.Tools.B2WorldCreater;
import com.mygdx.game.Tools.WorldContactListener;

public class Player extends Sprite {


    public enum State {
        NONE, WALK, DEAD, JUMPING
    }

    public World world;
    public Body b2body;
    private SpriteBatch batch;

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

    public Player(World world, PlayScreen screen) {
        this.world = world;
        definePlayer();
    }


    // create player
    public void definePlayer() {

        // физика
        velocity = new Vector2();
        position = new Vector2();

        // структура игрока на основе движка BOX2D

        // часть 1
        BodyDef bdef = new BodyDef();
        bdef.position.set(80 / MyGdxGame.PPM, 200 / MyGdxGame.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
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
        poly.setAsBox(18 / MyGdxGame.PPM, 25 / MyGdxGame.PPM);
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


    }

    public void render(SpriteBatch batch, float dt) {

        // анимация
        stateTime += Gdx.graphics.getDeltaTime();

        if (stay) {
            currentFrame = stayAnimation.getKeyFrame(stateTime, true);
            batch.draw(currentFrame, (getX() - 87 / MyGdxGame.PPM), getY() - 0.8f, 180f / MyGdxGame.PPM, 180f / MyGdxGame.PPM);
        }

        if (moveRight) {
            currentFrame = walkAnimation.getKeyFrame(stateTime, true);
            batch.draw(currentFrame, (getX() - 87 / MyGdxGame.PPM), getY() - 0.8f, 180f / MyGdxGame.PPM, 180f / MyGdxGame.PPM);
        }

        if (moveleft) {
            currentFrame = walkAnimation.getKeyFrame(stateTime, true);
            batch.draw(currentFrame, (getX() - 87 / MyGdxGame.PPM), getY() - 0.8f, 180f / MyGdxGame.PPM, 180f / MyGdxGame.PPM);
        }

        if (isJump) {
            currentFrame = walkAnimation.getKeyFrame(stateTime, true);
            batch.draw(currentFrame, (getX() - 0.8f), getY() - 0.8f, 180f / MyGdxGame.PPM, 180f / MyGdxGame.PPM);
        }

        // Система HP


    }

    public TextureRegion getTextureGame() {
        return currentFrame;
    }

    public void hit(int damage) {
        hp -= damage;
    }

    private void checkLive() {
        if (hp <= 0) isAlive = false;
        if (!isAlive) System.exit(-1); // TODO: СДЕЛАТЬ ЭКРАН ПЕРЕЗАПУСКА ИГРЫ
    }


    public int getHp() {
        return hp;
    }


    public void update(float dt) {
        // для корректной отрисовки текстуры
        setPosition((b2body.getPosition().x - getWidth() / 2), (b2body.getPosition().y - getHeight() / 2));
        // проверка HP игрока
        checkLive();
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
