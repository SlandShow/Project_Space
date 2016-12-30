package com.mygdx.game.BulletsResurses;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.mygdx.game.MyGdxGame;

/**
 * Created by Admin on 30.12.2016.
 */
public class GameBullets {

    private float x;
    private float y;
    private float speed;
    private ShapeRenderer render;
    private Texture img;

    public GameBullets(float x, float y) {
        this.x = x;
        this.y = y;
        speed = 100 / MyGdxGame.PPM;
        img = new Texture("huds/blastt.png");
    }

    public void draw(SpriteBatch batch) {
        batch.draw(img, x, y, 50 / MyGdxGame.PPM, 50 / MyGdxGame.PPM);
    }

    public void update() {
        x += speed;
    }
}
