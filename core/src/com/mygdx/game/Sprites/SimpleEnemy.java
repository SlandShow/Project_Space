package com.mygdx.game.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.MyGdxGame;


/**
 * Created by Admin on 30.11.2016.
 */
public class SimpleEnemy extends Enemy {

    BodyDef bdef;
    PolygonShape shape;
    Rectangle rect;
    Body body;
    FixtureDef fdef;
    public Vector2 velocity;
    public static final int DAMAGE = 10;


    public SimpleEnemy(World world, BodyDef bdef, PolygonShape shape, Rectangle rect, FixtureDef fdef) {
        super(world);
        this.bdef = bdef;
        this.shape = shape;
        this.rect = rect;
        this.fdef = fdef;
        velocity = new Vector2(-1, -2);
        defineEnemy();
    }

    @Override
    public void defineEnemy() {

        // урон при коллизии с врагом
        damage = 10;

        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.position.set(
                (rect.getX() + rect.getWidth() / 2) / MyGdxGame.PPM,
                (rect.getY() + rect.getHeight() / 2) / MyGdxGame.PPM);
        body = world.createBody(bdef);

        shape.setAsBox(rect.getWidth() / 2 / MyGdxGame.PPM, rect.getHeight() / 2 / MyGdxGame.PPM);
        fdef.shape = shape;
        body.createFixture(fdef).setUserData("simple enemy");


    }

    @Override
    public void update(float dt) {


    }
}
