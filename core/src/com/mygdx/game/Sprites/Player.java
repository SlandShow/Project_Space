package com.mygdx.game.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Screens.PlayScreen;

public class Player extends Sprite {

    public enum State {
        NONE, WALK, DEAD, JUMPING
    }

    public World world;
    public Body b2body;
    private SpriteBatch batch;

    // для анимации
    Animation bobLeft;
    Animation bobRight;
    Animation bobJumpLeft;
    Animation bobJumpRight;
    Animation bobIdleLeft;
    Animation bobIdleRight;
    Animation bobDead;
    int[][] blocks;
    TextureRegion tile;

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
    private TextureRegion walk1;
    private TextureRegion walk2;

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
        // super(screen.getAtlas().findRegion("sprites/player/walk/WalkAnimarion.png")); // !
        this.world = world;
        definePlayer();
        // animationFrames = new TextureRegion(getTexture(), 0, 0, 22, 46);
        // setBounds(0, 0, 22 / MyGdxGame.PPM, 46 / MyGdxGame.PPM);
        // setRegion(animationFrames);


    }


    // create player
    public void definePlayer() {

        // физика
        velocity = new Vector2();
        position = new Vector2();

        BodyDef bdef = new BodyDef();
        bdef.position.set(80 / MyGdxGame.PPM, 200 / MyGdxGame.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(5 / MyGdxGame.PPM);

        fdef.shape = shape;
        b2body.createFixture(fdef);


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

        stayAnimation = new Animation(0.5f, stayFrames);

    }

    public void render(SpriteBatch batch, float dt) {
        stateTime += Gdx.graphics.getDeltaTime();

        if (stay) {
            currentFrame = stayAnimation.getKeyFrame(stateTime, true);
            batch.draw(currentFrame, getX() + 170 , getY() + 0, 300f, 300f );
        }

        if (moveRight) {
            currentFrame = walkAnimation.getKeyFrame(stateTime, true);
            batch.draw(currentFrame, getX() + 170 , getY() + 0, 300f, 300f );
        }

        if (moveleft) {
            currentFrame = walkAnimation.getKeyFrame(stateTime, true);
            batch.draw(currentFrame, getX() + 170 , getY() + 0, 300f, 300f );
        }

        if (isJump) {
            currentFrame = walkAnimation.getKeyFrame(stateTime, true);
            batch.draw(currentFrame, getX() + 170 , getY() + 0, 300f, 300f );
        }



        //batch.draw(anim.getKeyFrame(0, loop), b2body.getPosition().x - 20 / MyGdxGame.PPM, b2body.getPosition().y - 10 / MyGdxGame.PPM, 0.5f, 0.5f);

    }


    public void update(float dt) {

        setPosition((b2body.getPosition().x - getWidth() / 2 ) *1, (b2body.getPosition().y - getHeight() / 2 ) * MyGdxGame.PPM);
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
