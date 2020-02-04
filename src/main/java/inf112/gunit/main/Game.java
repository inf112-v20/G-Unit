package inf112.gunit.main;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class Game implements ApplicationListener {
    TiledMap tiledMap;
    TiledMapTileLayer Board, Player, Hole, Flag;
    OrthographicCamera camera;
    OrthogonalTiledMapRenderer tilerender;
    TiledMapRenderer tiledMapRenderer;

    @Override
    public void create() {
        tiledMap = new TmxMapLoader().load("src/assets/tiles.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        Board = (TiledMapTileLayer) tiledMap.getLayers().get("Board");
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1500, 1500);
        camera.translate(2.5f, 0);
        camera.update();

        tilerender = new OrthogonalTiledMapRenderer(tiledMap, (float) 1/300*300);

        tilerender.setView(camera);
    }

    @Override
    public void dispose() {
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 0, 0, 1);

        tilerender.render();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }
}
