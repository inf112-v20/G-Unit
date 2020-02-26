package inf112.gunit.board;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;

public class Board {

    private TiledMap tiledMap;

    private TextureRegion[][] sprites;

    private int mapWidth;
    private int mapHeight;
    private int tileWidth;
    private int tileHeight;

    public Board(String boardSrc) {
        tiledMap = new TmxMapLoader().load(boardSrc);

        mapWidth = tiledMap.getProperties().get("width", Integer.class);
        mapHeight = tiledMap.getProperties().get("height", Integer.class);
        tileWidth = tiledMap.getProperties().get("tilewidth", Integer.class);
        tileHeight = tiledMap.getProperties().get("tileheight", Integer.class);

        TiledMapTileLayer testLayer = new TiledMapTileLayer(mapWidth,mapHeight,300,300);

        Texture texture = new Texture("src/assets/tiles.png");
        sprites = TextureRegion.split(texture, tileWidth, tileHeight);

        TiledMapTileLayer.Cell testCell = new TiledMapTileLayer.Cell().setTile(new StaticTiledMapTile(sprites[11][0]));

        testLayer.setCell(0,9,testCell);

        tiledMap.getLayers().add(testLayer);
    }

    public TiledMap getTiledMap() {
        return tiledMap;
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public int getTileWidth() {
        return tileWidth;
    }

    public int getTileHeight() {
        return tileHeight;
    }

    public void update() {
        //TODO
    }
}
