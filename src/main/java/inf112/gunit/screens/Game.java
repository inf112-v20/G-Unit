package inf112.gunit.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import inf112.gunit.main.Main;
import inf112.gunit.player.Player;

public class Game implements Screen {

    private int tick;

    private Main game;
    private TiledMap map;

    private Player[] players;

    private MapProperties props;

    private int mapWidth;
    private int mapHeight;
    private int tileWidth;
    private int tileHeight;

    private OrthographicCamera camera;
    private OrthogonalTiledMapRenderer tileRenderer;

    public Game(Main game, TiledMap map) {
        this.game = game;
        this.map = map;

        tick = 0;

        props = map.getProperties();
        mapWidth = map.getProperties().get("width", Integer.class);
        mapHeight = map.getProperties().get("height", Integer.class);
        tileWidth = map.getProperties().get("tilewidth", Integer.class);
        tileHeight = map.getProperties().get("tileheight", Integer.class);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, mapWidth*tileWidth, mapHeight*tileHeight);
        camera.update();

        tileRenderer = new OrthogonalTiledMapRenderer(map, (float) 1/tileWidth*tileHeight);
        tileRenderer.setView(camera);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float v) {
        Gdx.gl.glClearColor(1,0,0,1);
        tileRenderer.setView(camera);
        tileRenderer.render();

        tick++;


    }

    @Override
    public void resize(int i, int i1) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
