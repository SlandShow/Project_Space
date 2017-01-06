package com.mygdx.game.BulletsResurses;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Sprites.Player;
import com.mygdx.game.Tools.B2WorldCreater;

/**
 * Created by Admin on 31.12.2016.
 */
public class Box2DBullets extends Sprite {

    private World world;
    private Body b2body;
    private BodyDef bdef;
    private FixtureDef fdef;
    private PolygonShape shape;
    private float x;
    private float y;
    private float r;
    private Vector2 velocity;
    private float velocity_x;
    private float velocity_y;
    private boolean bulletTurnLeft;
    private boolean bulletTurnRight;
    private boolean checkTurning;
    private float leftDeltaTurning;
    private float rightDeltaTurning;


    public Box2DBullets(B2WorldCreater creater, float x, float y) {
        this.world = creater.getWorld();
        this.bdef = creater.getBdef();
        this.shape = creater.getShape();
        this.fdef = creater.getFdef();
        this.b2body = creater.getBody();
        this.x = x;
        this.y = y;
        velocity_x = 10f;
        velocity_y = 400f;
        checkTurning = false;
        defineBullet();
    }

    public boolean getBulletTurnLeft() {
        return bulletTurnLeft;
    }

    public boolean getBulletTurnRight() {
        return bulletTurnRight;
    }

    public void setBulletTurnLeft(boolean bulletTurnLeft) {

        this.bulletTurnLeft = bulletTurnLeft;
    }

    public void setBulletTurnRight(boolean bulletTurnRight) {
        this.bulletTurnRight = bulletTurnRight;
    }

    public void defineBullet() {

        //  Создание фикстуры - пули для бластера
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.position.set(x, y);
        b2body = world.createBody(bdef);
        shape.setAsBox(1 / MyGdxGame.PPM, 1 / MyGdxGame.PPM);
        fdef.shape = shape;
        bdef.bullet = true;
        b2body.setGravityScale(0);
        b2body.createFixture(fdef).setUserData("bullet");

        // Определяем смещение пули от Body в ту, или иную сторону
        leftDeltaTurning = -20f / MyGdxGame.PPM;
        rightDeltaTurning = 20f / MyGdxGame.PPM;

    }

    public void update(float dt) {
        // Единожды определяем будущее направление для каждой пули
        if (checkTurning == false) {

            // Перед импульсом у пули корректируем ее координаты (чтобы она вылетала из бластера, а не из самого игрока)
            changeVecPosition(Player.PLAYER_STATE);

            if (Player.PLAYER_STATE == Player.State.FACTING_RIGHT)
                b2body.applyLinearImpulse(new Vector2(velocity_x, 0), b2body.getWorldCenter(), true);
            if (Player.PLAYER_STATE == Player.State.FACTING_LEFT)
                b2body.applyLinearImpulse(new Vector2(-velocity_x, 0), b2body.getWorldCenter(), true);
            checkTurning = true;
        }


    }

    private void changeVecPosition(Player.State bulletState) {
        switch (bulletState) {
            case FACTING_RIGHT:
                x += rightDeltaTurning;
                break;
            case FACTING_LEFT:
                x += leftDeltaTurning;
                break;
        }

        // обновление позиции фикстуры пули
        b2body.getPosition().set(x, y);
    }

    public void kill() {
        world.destroyBody(b2body);
    }


}
