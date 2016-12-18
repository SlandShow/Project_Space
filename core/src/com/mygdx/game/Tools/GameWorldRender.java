package com.mygdx.game.Tools;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

/**
 * Created by Admin on 13.12.2016.
 */
public class GameWorldRender {

    private OrthogonalTiledMapRenderer gameRender;
    private TiledMap map;
    private float size;

    public GameWorldRender(TiledMap map, float size) {
        this.size = size;
        this.map = map;
        gameRender = new OrthogonalTiledMapRenderer(map, size);
    }

    public void changeSize(float size) {
        this.size = size;
        gameRender = new OrthogonalTiledMapRenderer(map, size);
    }

    public void render(float dt) {
        gameRender.render();
    }

    public OrthogonalTiledMapRenderer getMapRender() {
        return gameRender;
    }

    public void dispose() {
        gameRender.dispose();
        map.dispose();
    }

}
