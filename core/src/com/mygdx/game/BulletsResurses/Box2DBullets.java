package com.mygdx.game.BulletsResurses;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.MyGdxGame;
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
    private boolean bulletTurnLeft;
    private boolean bulletTurnRight;


    public Box2DBullets(B2WorldCreater creater, float x, float y) {
        this.world = creater.getWorld();
        this.bdef = creater.getBdef();
        this.shape = creater.getShape();
        this.fdef = creater.getFdef();
        this.b2body = creater.getBody();
        this.x = x;
        this.y = y;
        velocity_x = 4f;
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
        shape.setAsBox(2 / MyGdxGame.PPM, 2 / MyGdxGame.PPM);
        fdef.shape = shape;
        bdef.bullet = true;

        b2body.createFixture(fdef).setUserData("bullet");


    }

    public void update(float dt) {

        if (bulletTurnLeft) b2body.applyLinearImpulse(new Vector2(-velocity_x, 0), new Vector2(velocity_x, 0), false);
        else b2body.applyLinearImpulse(new Vector2(velocity_x, 0), b2body.getWorldCenter(), false);
        
    }

    public void kill() {

    }


}
