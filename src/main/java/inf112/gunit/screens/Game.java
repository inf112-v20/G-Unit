package inf112.gunit.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import inf112.gunit.board.Direction;
import inf112.gunit.main.Main;
import inf112.gunit.player.Player;
import inf112.gunit.player.card.TestPrograms;

/**
 * The Game class is a screen which is rendered
 * when the Play-button is pressed in menu
 */
public class Game extends InputAdapter implements Screen {

    public static TextureRegion[][] spriteSheet;

    private int tick;

    private Main main;
    private TiledMap map;
    private TiledMapTileLayer[] layers;
    private MapProperties props;

    private Player[] players;
    private Player mainPlayer;

    private OrthographicCamera camera;
    private OrthogonalTiledMapRenderer tileRenderer;

    private int cardIdx;

    /**
     * The Game constructor
     * @param main takes a main
     * @param map takes a TiledMap as the board
     */
    public Game(Main main, TiledMap map, int numOfPlayers) {
        this.main = main;
        this.map = map;
        this.players = new Player[numOfPlayers];

        cardIdx = 0;

        MapLayers mapLayers = map.getLayers();
        layers = new TiledMapTileLayer[mapLayers.size()];
        for (int i = 0; i < layers.length; i++) {
            layers[i] = (TiledMapTileLayer) mapLayers.get(i);
        }

        tick = 0;

        props = map.getProperties();
        int mapWidth = map.getProperties().get("width", Integer.class);
        int mapHeight = map.getProperties().get("height", Integer.class);
        int tileWidth = map.getProperties().get("tilewidth", Integer.class);
        int tileHeight = map.getProperties().get("tileheight", Integer.class);

        for (int i = 0; i < numOfPlayers; i++) {
            Player p = new Player(map, i);
            p.setProgram(TestPrograms.getProgram(i));
            players[i] = p;
        }

        mainPlayer = players[0];

        camera = new OrthographicCamera();
        camera.setToOrtho(false, mapWidth * tileWidth, mapHeight * tileHeight);
        camera.update();

        tileRenderer = new OrthogonalTiledMapRenderer(map, (float) 1/ tileWidth * tileHeight);
        tileRenderer.setView(camera);

        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float v) {
        Gdx.gl.glClearColor(1,0,0,1);

        // update the player
        for (Player player : players) {
            player.update();

            if (tick % 30 == 0) {
                for (int i = 0; i < layers.length; i++) {
                    if (layers[i].getName().equals("rotator_clockwise") && layers[i].getCell((int) player.getPositionX(), (int) player.getPositionY()) != null) {
                        player.rotate(true, 1);
                    } else if (layers[i].getName().equals("rotator_counter_clockwise") && layers[i].getCell((int) player.getPositionX(), (int) player.getPositionY()) != null) {
                        player.rotate(false, 1);
                    }
                }
            }
        }

        // render the tile-map
        tileRenderer.setView(camera);
        tileRenderer.render();

        // update game mechanics every 0.5 seconds (game runs at 60 fps)

        tick++; // increase the game tick
    }

    // key-listener currently used for testing
    @Override
    public boolean keyUp(int keyCode) {
        Vector2 position = mainPlayer.getPosition();
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get("player_0");
        Direction dir = mainPlayer.getDirection();

        int x = (int) position.x;
        int y = (int) position.y;

        switch (keyCode) {
            case Input.Keys.LEFT:
                if (x - 1 < 0 || x - 1 >= props.get("width", Integer.class))
                    return false;
                else {
                    layer.setCell((int) position.x, (int) position.y, null);
                    mainPlayer.setDirection(Direction.WEST);
                    position.set(x - 1, y);
                    return true;
                }

            case Input.Keys.RIGHT:
                if (x + 1 < 0 || x + 1 >= props.get("width", Integer.class))
                    return false;
                else {
                    layer.setCell((int) position.x, (int) position.y, null);
                    mainPlayer.setDirection(Direction.EAST);
                    position.set(x + 1, y);
                    return true;
                }

            case Input.Keys.UP:
                if (y + 1 < 0 || y + 1 >= props.get("height", Integer.class))
                    return false;
                else {
                    layer.setCell((int) position.x, (int) position.y, null);
                    mainPlayer.setDirection(Direction.NORTH);
                    position.set(x, y + 1);
                    return true;
                }

            case Input.Keys.DOWN:
                if (y - 1 < 0 || y - 1 >= props.get("height", Integer.class))
                    return false;
                else {
                    layer.setCell((int) position.x, (int) position.y, null);
                    mainPlayer.setDirection(Direction.SOUTH);
                    position.set(x, y - 1);
                    return true;
                }

            case Input.Keys.SPACE:
                for (Player p : players) {
                    p.doTurn(p.getProgram()[cardIdx % 5]);
                }
                cardIdx++;
                return true;

            default:
                return false;
        }
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
