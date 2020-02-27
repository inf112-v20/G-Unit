package inf112.gunit.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import inf112.gunit.main.Main;
import inf112.gunit.player.Player;

import java.util.ArrayList;

public class Game implements Screen {

    public static TextureRegion[][] spriteSheet;

    private int tick;

    private Main game;
    private TiledMap map;
    private TiledMapTileLayer[] layers;

    private Player[] players;

    private Player player;

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

        MapLayers mapLayers = map.getLayers();
        layers = new TiledMapTileLayer[mapLayers.size()];
        for (int i = 0; i < layers.length; i++) {
            layers[i] = (TiledMapTileLayer) mapLayers.get(i);
        }

        tick = 0;

        props = map.getProperties();
        mapWidth = map.getProperties().get("width", Integer.class);
        mapHeight = map.getProperties().get("height", Integer.class);
        tileWidth = map.getProperties().get("tilewidth", Integer.class);
        tileHeight = map.getProperties().get("tileheight", Integer.class);

        player = new Player(map, props);
        Gdx.input.setInputProcessor(player);

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

        player.update();
        tileRenderer.setView(camera);
        tileRenderer.render();

        if (tick % 30 == 0) {
            for (int i = 0; i < layers.length; i++) {
                if (layers[i].getName().equals("rotator_clockwise") && layers[i].getCell((int) player.getPositionX(), (int) player.getPositionY()) != null) {
                    player.rotate(true, 1);
                } else if (layers[i].getName().equals("rotator_counter_clockwise") && layers[i].getCell((int) player.getPositionX(), (int) player.getPositionY()) != null) {
                    player.rotate(false, 1);
                }
            }
        }

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
