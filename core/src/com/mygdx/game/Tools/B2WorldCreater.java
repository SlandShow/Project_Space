package com.mygdx.game.Tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.BulletsResurses.Box2DBullets;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Sprites.Player;
import com.mygdx.game.Sprites.SimpleEnemy;

/**
 * Created by Admin on 18.11.2016.
 */
public class B2WorldCreater {

    public static Array<SimpleEnemy> enemies = new Array<SimpleEnemy>();
    private BodyDef bdef = new BodyDef();
    private PolygonShape shape = new PolygonShape();
    private FixtureDef fdef = new FixtureDef();
    private Body body;

    public World getWorld() {
        return world;
    }

    public Body getBody() {
        return body;
    }

    public FixtureDef getFdef() {
        return fdef;
    }

    public PolygonShape getShape() {
        return shape;
    }

    public BodyDef getBdef() {
        return bdef;
    }

    private World world;

    public B2WorldCreater(World world, TiledMap map) {

       this.world = world;

        // create ground bodies/fixtures (polygon) - платформы
        for (MapObject obj :
                map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) obj).getRectangle();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set(
                    (rect.getX() + rect.getWidth() / 2) / MyGdxGame.PPM,
                    (rect.getY() + rect.getHeight() / 2) / MyGdxGame.PPM);
            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2 / MyGdxGame.PPM, rect.getHeight() / 2 / MyGdxGame.PPM);
            fdef.shape = shape;
            body.createFixture(fdef).setUserData("platform");
        }


        // враги
        for (MapObject obj :
                map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) obj).getRectangle();
            enemies.add(new SimpleEnemy(world, bdef, shape, rect, fdef));

        }



        for (MapObject obj :
                map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) obj).getRectangle();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set(
                    (rect.getX() + rect.getWidth() / 2) / MyGdxGame.PPM,
                    (rect.getY() + rect.getHeight() / 2) / MyGdxGame.PPM);
            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2 / MyGdxGame.PPM, rect.getHeight() / 2 / MyGdxGame.PPM);
            fdef.shape = shape;
            body.createFixture(fdef);
        }

        for (MapObject obj :
                map.getLayers().get(1).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) obj).getRectangle();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set(
                    (rect.getX() + rect.getWidth() / 2) / MyGdxGame.PPM,
                    (rect.getY() + rect.getHeight() / 2) / MyGdxGame.PPM);
            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2 / MyGdxGame.PPM, rect.getHeight() / 2 / MyGdxGame.PPM);
            fdef.shape = shape;
            body.createFixture(fdef);
        }

        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.position.set(1, 1);
        body = world.createBody(bdef);
        shape.setAsBox(1 / MyGdxGame.PPM, 1 / MyGdxGame.PPM);
        fdef.shape = shape;
        bdef.bullet = true;
        body.createFixture(fdef).setUserData("bullet");
        body.applyLinearImpulse(new Vector2(4f, 0f), new Vector2(4f, 0f), true);


 /*
        // create pipe bodies
        for (MapObject obj :
                map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) obj).getRectangle();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set(
                    (rect.getX() + rect.getWidth() / 2) / MyGdxGame.PPM,
                    (rect.getY() + rect.getHeight() / 2) / MyGdxGame.PPM);
            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2 / MyGdxGame.PPM, rect.getHeight() / 2 / MyGdxGame.PPM);
            fdef.shape = shape;
            body.createFixture(fdef);
        }

        // create bricks
        for (MapObject obj :
                map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) obj).getRectangle();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set(
                    (rect.getX() + rect.getWidth() / 2) / MyGdxGame.PPM,
                    (rect.getY() + rect.getHeight() / 2) / MyGdxGame.PPM);
            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2 / MyGdxGame.PPM, rect.getHeight() / 2 / MyGdxGame.PPM);
            fdef.shape = shape;
            body.createFixture(fdef);
        }

        // create coins
        for (MapObject obj :
                map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) obj).getRectangle();
            new Coin(world, map, rect);
        }*/


    }

    public void update(float dt) {
       /* bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.position.set(0.5f, 0.5f);
        body = world.createBody(bdef);
        shape.setAsBox(1 / MyGdxGame.PPM, 1 / MyGdxGame.PPM);
        fdef.shape = shape;
        bdef.bullet = true;
        body.createFixture(fdef).setUserData("bullet");
        body.applyLinearImpulse(new Vector2(4f, 0f), new Vector2(4f, 0f), true);*/
    }

    public void dispose() {

    }
}
