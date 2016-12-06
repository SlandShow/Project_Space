package com.mygdx.game.Scenes;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

public class GameStarEffect {

    private Vector3 position, velocity;
    private float width, height;
    private float dept_end = 1000;
    private float dept_start = 100;
    private float velocity_min = 0.5f;
    private float velocity_max = 5f;
    private float maxRadius = 5f;

    public GameStarEffect() {
        position = new Vector3();
        velocity = new Vector3();
        init();
    }

    public void init() {
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        position.x = MathUtils.random(-(width), width);
        position.y = MathUtils.random(-(height), height);
        position.z = MathUtils.random(dept_start, dept_end);
        velocity.z = MathUtils.random(velocity_min, velocity_max);
    }

    public float getColor() {
        return ((1.0f - ((position.x * 1.0f) * .001f)) * velocity.z) * 0.2f;
    }

    public float getRandomColor() {
        return MathUtils.random(0.1f, 1f);
    }

    public void update(float speed) {
        if (position.z < 0 || position.z > dept_end || position.y > height || position.x > width)
            init();

        float t = Gdx.graphics.getDeltaTime();
        sub(velocity.x * speed * t, velocity.y * speed * t, velocity.z * speed * t);
    }

    public void draw(ShapeRenderer sr) {
        float c = getColor();
        float x = ((position.x / position.z) * 100) + (width * .5f);
        float y = ((position.y / position.z) * 100) + (height * .5f);
        float r = ((maxRadius - ((position.z * maxRadius) * .001f)) * velocity.z) * 0.2f;

        sr.setColor(c, c, c, 1f);
        sr.circle(x, y, r);
    }

    public void sub(float x, float y, float z) {
        position.sub(x, y, z);
    }

}
