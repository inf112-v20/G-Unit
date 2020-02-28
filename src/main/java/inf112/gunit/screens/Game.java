package inf112.gunit.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import inf112.gunit.main.Main;
import inf112.gunit.player.Player;

/**
 * The Game class is a screen which is rendered
 * when the Play-button is pressed in menu
 */
public class Game implements Screen {

    public static TextureRegion[][] spriteSheet;

    private int tick;

    private Main main;
    private TiledMap map;
    private TiledMapTileLayer[] layers;

    private Player[] players;

    private Player player;

    private OrthographicCamera camera;
    private OrthogonalTiledMapRenderer tileRenderer;

    /**
     * The Game constructor
     * @param main takes a main
     * @param map takes a TiledMap as the board
     */
    public Game(Main main, TiledMap map) {
        this.main = main;
        this.map = map;

        MapLayers mapLayers = map.getLayers();
        layers = new TiledMapTileLayer[mapLayers.size()];
        for (int i = 0; i < layers.length; i++) {
            layers[i] = (TiledMapTileLayer) mapLayers.get(i);
        }

        tick = 0;

        MapProperties props = map.getProperties();
        int mapWidth = map.getProperties().get("width", Integer.class);
        int mapHeight = map.getProperties().get("height", Integer.class);
        int tileWidth = map.getProperties().get("tilewidth", Integer.class);
        int tileHeight = map.getProperties().get("tileheight", Integer.class);

        player = new Player(map);
        Gdx.input.setInputProcessor(player);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, mapWidth * tileWidth, mapHeight * tileHeight);
        camera.update();

        tileRenderer = new OrthogonalTiledMapRenderer(map, (float) 1/ tileWidth * tileHeight);
        tileRenderer.setView(camera);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float v) {
        Gdx.gl.glClearColor(1,0,0,1);

        // update the player
        player.update();

        // render the tile-map
        tileRenderer.setView(camera);
        tileRenderer.render();

        // update game mechanics every 0.5 seconds (game runs at 60 fps)
        if (tick % 30 == 0) {
            for (int i = 0; i < layers.length; i++) {
                if (layers[i].getName().equals("rotator_clockwise") && layers[i].getCell((int) player.getPositionX(), (int) player.getPositionY()) != null) {
                    player.rotate(true, 1);
                } else if (layers[i].getName().equals("rotator_counter_clockwise") && layers[i].getCell((int) player.getPositionX(), (int) player.getPositionY()) != null) {
                    player.rotate(false, 1);
                }
            }
        }

        tick++; // increase the game tick
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
