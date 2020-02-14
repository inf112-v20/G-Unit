package inf112.gunit.board;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

public class Board {

    private TiledMap tiledMap;
    private TiledMapTileLayer layer;

    public Board(TiledMap tiledMap) {
        this.tiledMap = tiledMap;

        layer = (TiledMapTileLayer) tiledMap.getLayers().get("board");
    }

    public void update() {
        //TODO
    }
}
