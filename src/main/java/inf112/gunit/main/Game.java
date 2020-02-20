package inf112.gunit.main;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import inf112.gunit.board.Board;
import inf112.gunit.player.Player;

public class Game extends InputAdapter implements ApplicationListener {

    private TiledMap tiledMap;

    private Player player;
    private Board board;

    private MapProperties props;

    private OrthographicCamera camera;
    private OrthogonalTiledMapRenderer tileRenderer;

    @Override
    public void create() {
        tiledMap = new TmxMapLoader().load("src/assets/tile_map_3Kx3K_full.tmx");

        props = tiledMap.getProperties();
        int tileWidth = props.get("tilewidth", Integer.class);
        int tileHeight = props.get("tileheight", Integer.class);
        int mapWidth = props.get("width", Integer.class);
        int mapHeight = props.get("height", Integer.class);

        player = new Player(tiledMap, props);
        Gdx.input.setInputProcessor(player);
        board = new Board(tiledMap);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, tileWidth*mapWidth, tileHeight*mapHeight);
        camera.update();

        tileRenderer = new OrthogonalTiledMapRenderer(tiledMap, (float) 1/tileWidth*tileHeight);
        tileRenderer.setView(camera);
    }

    @Override
    public void dispose() {
        //TODO
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 0, 0, 1);

        player.update();

        tileRenderer.render();
    }

    @Override
    public void resize(int width, int height) {
        //TODO
    }

    @Override
    public void pause() {
        //TODO
    }

    @Override
    public void resume() {
        //TODO
    }
}
