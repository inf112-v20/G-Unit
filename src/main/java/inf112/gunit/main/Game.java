package inf112.gunit.main;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;

public class Game extends InputAdapter implements ApplicationListener {
    TiledMap tiledMap;
    TiledMapTileLayer layerBoard, layerPlayer, layerHole, layerFlag;
    OrthographicCamera camera;
    OrthogonalTiledMapRenderer tileRenderer;
    TiledMapRenderer tiledMapRenderer;
    Cell playerCell, playerDiedCell, playerWonCell;
    Vector2 playerPosition;

    @Override
    public void create() {
        Gdx.input.setInputProcessor(this);

        tiledMap = new TmxMapLoader().load("src/assets/tiles_80x80.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        MapProperties props = tiledMap.getProperties();

        int tileWidth = props.get("tilewidth", Integer.class);
        int tileHeight = props.get("tileheight", Integer.class);
        int mapWidth = props.get("width", Integer.class);
        int mapHeight = props.get("height", Integer.class);

        layerBoard = (TiledMapTileLayer) tiledMap.getLayers().get("board");
        layerPlayer = (TiledMapTileLayer) tiledMap.getLayers().get("player");

        Texture playerTexture = new Texture("src/assets/player_16x16.png");
        TextureRegion[][] playerTextureSplit = TextureRegion.split(playerTexture, tileWidth, tileHeight);
        playerCell = new Cell().setTile(new StaticTiledMapTile(playerTextureSplit[0][0]));
        playerPosition = new Vector2(0, 0);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, tileWidth*mapWidth, tileHeight*mapHeight);

        camera.update();

        tileRenderer = new OrthogonalTiledMapRenderer(tiledMap, (float) 1/16*16);
        tileRenderer.setView(camera);
    }

    @Override
    public void dispose() {
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        layerPlayer.setCell((int) playerPosition.x, (int) playerPosition.y, playerCell);
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

    @Override
    public boolean keyUp(int keycode) {
        layerPlayer.setCell((int) playerPosition.x, (int) playerPosition.y, null);

        if (keycode == Input.Keys.LEFT)
            playerPosition.set(playerPosition.x - 1, playerPosition.y);
        if (keycode == Input.Keys.RIGHT)
            playerPosition.set(playerPosition.x + 1, playerPosition.y);
        if (keycode == Input.Keys.DOWN)
            playerPosition.set(playerPosition.x, playerPosition.y - 1);
        if (keycode == Input.Keys.UP)
            playerPosition.set(playerPosition.x, playerPosition.y + 1);

        return true;
    }
}
