package com.mygdx.game.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.MyGdxGame;

/**
 * Created by Admin on 30.11.2016.
 */
public abstract class Enemy extends Sprite {

    World world;
    Body b2body;


    public Enemy(World world) {
        this.world = world;
    }

    // create enemy
    public abstract void defineEnemy();

    public World getWorld() {
        return world;
    }

    public Body getB2body() {
        return b2body;
    }

    public abstract void update(float dt);
}
