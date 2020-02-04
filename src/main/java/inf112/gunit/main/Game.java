package inf112.gunit.main;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class Game implements ApplicationListener {
    TiledMap tiledMap;
    TiledMapTileLayer layerBoard, layerPlayer, layerHole, layerFlag;
    OrthographicCamera camera;
    OrthogonalTiledMapRenderer tileRenderer;
    TiledMapRenderer tiledMapRenderer;

    @Override
    public void create() {
        tiledMap = new TmxMapLoader().load("src/assets/tiles.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        MapProperties props = tiledMap.getProperties();

        int tileWidth = props.get("tilewidth", Integer.class);
        int tileHeight = props.get("tileheight", Integer.class);
        int mapWidth = props.get("width", Integer.class);
        int mapHeight = props.get("height", Integer.class);

        layerBoard = (TiledMapTileLayer) tiledMap.getLayers().get("Board");

        camera = new OrthographicCamera();
        camera.setToOrtho(false, tileWidth*mapWidth, tileHeight*mapHeight);
        camera.translate(2.5f, 0);
        camera.update();

        tileRenderer = new OrthogonalTiledMapRenderer(tiledMap, (float) 1/300*300);
        tileRenderer.setView(camera);
    }

    @Override
    public void dispose() {
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        tileRenderer.render();
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
