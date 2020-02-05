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
    private TiledMap tiledMap;
    private TiledMapTileLayer layerBoard;
    private TiledMapTileLayer layerPlayer;
    private MapProperties props;

    private OrthographicCamera camera;
    private OrthogonalTiledMapRenderer tileRenderer;
    
    private TiledMapRenderer tiledMapRenderer;
    
    private Cell playerCell;
    private Vector2 playerPosition;

    @Override
    public void create() {
        Gdx.input.setInputProcessor(this);

        tiledMap = new TmxMapLoader().load("src/assets/tiles_80x80.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        props = tiledMap.getProperties();

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
        //TODO
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        layerPlayer.setCell((int) playerPosition.x, (int) playerPosition.y, playerCell);
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

    @Override
    public boolean keyDown(int keyCode) {
        layerPlayer.setCell((int) playerPosition.x, (int) playerPosition.y, null);

        int x = (int) playerPosition.x;
        int y = (int) playerPosition.y;

        switch (keyCode) {
            case Input.Keys.LEFT:
                if (x - 1 < 0 || x - 1 >= props.get("width", Integer.class)) return false;
                else { playerPosition.set(x - 1, y); return true; }

            case Input.Keys.RIGHT:
                if (x + 1 < 0 || x + 1 >= props.get("width", Integer.class)) return false;
                else { playerPosition.set(x + 1, y); return true; }

            case Input.Keys.UP:
                if (y + 1 < 0 || y + 1 >= props.get("height", Integer.class)) return false;
                else { playerPosition.set(x, y + 1); return true; }

            case Input.Keys.DOWN:
                if (y - 1 < 0 || y - 1 >= props.get("height", Integer.class)) return false;
                else { playerPosition.set(x, y - 1); return true; }

            default:
                return false;
        }
    }
}
