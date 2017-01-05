package com.mygdx.game.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.Sprites.Player;
import com.mygdx.game.Sprites.SimpleEnemy;


/**
 * Created by Admin on 19.12.2016.
 */
public class WorldContactListener implements ContactListener {

    private Player player;

    public WorldContactListener(Player player) {
        this.player = player;
}

    @Override
    public void beginContact(Contact contact) {
        // мониторинг с помощью логов
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        if (fixA.getUserData() != null && fixB.getUserData() != null) {
            Gdx.app.log("Collision ", fixA.getUserData().toString());
            Gdx.app.log("Collision ", fixB.getUserData().toString());

            // обработка коллизии игрока с врагом
            if (fixA.getUserData().equals("simple enemy") || fixB.getUserData().equals("simple enemy")) player.hit(SimpleEnemy.DAMAGE);

            if (fixA.getUserData().equals("bullet") || fixB.getUserData().equals("bullet"))
                Gdx.app.log("Collision", "with bullets");

        }



    }

    @Override
    public void endContact(Contact contact) {
        // мониторинг с помощью логов
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();



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

    public static boolean checkRectangleCollision(Rectangle obj1, Rectangle obj2) {

        return ((obj1.x < obj2.x + obj2.width) && (obj2.x < obj1.x + obj1.width) &&
                (obj1.y < obj2.y + obj2.height) && (obj2.y < obj1.y + obj1.height));

    }
}
