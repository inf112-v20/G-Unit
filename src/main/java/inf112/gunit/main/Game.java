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
    private TiledMapTileLayer layerHole;
    private TiledMapTileLayer layerFlag1;
    private TiledMapTileLayer layerFlag2;
    private TiledMapTileLayer layerFlag3;
    private TiledMapTileLayer layerFlag4;

    private MapProperties props;

    private OrthographicCamera camera;
    private OrthogonalTiledMapRenderer tileRenderer;
    
    private Cell playerCell;
    private Cell playerDiedCell;
    private Cell playerWonCell;

    private Vector2 playerPosition;

    @Override
    public void create() {
        Gdx.input.setInputProcessor(this);

        tiledMap = new TmxMapLoader().load("src/assets/tiles_80x80.tmx");

        props = tiledMap.getProperties();
        int tileWidth = props.get("tilewidth", Integer.class);
        int tileHeight = props.get("tileheight", Integer.class);
        int mapWidth = props.get("width", Integer.class);
        int mapHeight = props.get("height", Integer.class);

        layerBoard = (TiledMapTileLayer) tiledMap.getLayers().get("board");
        layerPlayer = (TiledMapTileLayer) tiledMap.getLayers().get("player");
        layerHole = (TiledMapTileLayer) tiledMap.getLayers().get("hole");
        layerFlag1 = (TiledMapTileLayer) tiledMap.getLayers().get("flag1");
        layerFlag2 = (TiledMapTileLayer) tiledMap.getLayers().get("flag2");
        layerFlag3 = (TiledMapTileLayer) tiledMap.getLayers().get("flag3");
        layerFlag4 = (TiledMapTileLayer) tiledMap.getLayers().get("flag4");

        Texture playerTexture = new Texture("src/assets/players_16x16.png");
        TextureRegion[][] playerTextureSplit = TextureRegion.split(playerTexture, tileWidth, tileHeight);

        playerCell = new Cell().setTile(new StaticTiledMapTile(playerTextureSplit[0][3]));
        playerDiedCell = new Cell().setTile(new StaticTiledMapTile(playerTextureSplit[0][0]));
        playerWonCell = new Cell().setTile(new StaticTiledMapTile(playerTextureSplit[0][1]));

        playerPosition = new Vector2(0, 0);

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

        if (layerHole.getCell((int) playerPosition.x, (int) playerPosition.y) != null) {
            layerPlayer.setCell((int) playerPosition.x, (int) playerPosition.y, playerDiedCell);
        } else if (layerFlag1.getCell((int) playerPosition.x, (int) playerPosition.y) != null ||
                layerFlag2.getCell((int) playerPosition.x, (int) playerPosition.y) != null ||
                layerFlag3.getCell((int) playerPosition.x, (int) playerPosition.y) != null ||
                layerFlag4.getCell((int) playerPosition.x, (int) playerPosition.y) != null) {
            layerPlayer.setCell((int) playerPosition.x, (int) playerPosition.y, playerWonCell);
        } else {
            layerPlayer.setCell((int) playerPosition.x, (int) playerPosition.y, playerCell);
        }

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
    public boolean keyUp(int keyCode) {
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
