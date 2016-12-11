package com.mygdx.game.Tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Sprites.SimpleEnemy;

/**
 * Created by Admin on 18.11.2016.
 */
public class B2WorldCreater {

    public static Array<SimpleEnemy> enemies = new Array<SimpleEnemy>();

    public B2WorldCreater(World world, TiledMap map) {

        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;





        
        // create ground bodies/fixtures (polygon)
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
            body.createFixture(fdef);
        }


        for (MapObject obj :
                map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) obj).getRectangle();
            enemies.add(new SimpleEnemy(world, bdef, shape, rect, fdef));

        }

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

    public void dispose() {

    }
}
