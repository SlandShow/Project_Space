package com.mygdx.game.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;


/**
 * Created by Admin on 19.12.2016.
 */
public class WorldContactListener implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
      // мониторинг с помощью логов
        Gdx.app.log("Begin contact", "");
    }

    @Override
    public void endContact(Contact contact) {
        // мониторинг с помощью логов
        Gdx.app.log("End contact", "");
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    // использовать только для Rectangle!
    public static boolean checkRectangleCollision(Rectangle s1, Rectangle s2, Rectangle s3) {
        return Intersector.intersectRectangles(s1, s2, s3);
    }
}
