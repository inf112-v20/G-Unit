package inf112.gunit.player;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Vector2;
import inf112.gunit.board.Direction;

public class Player extends InputAdapter {

    // Different texture the player can have
    private static final int NORMAL = 0;
    private static final int DIED = 1;
    private static final int WON = 2;

    private TiledMap tiledMap;
    private MapProperties props;

    private Direction dir;

    private TiledMapTileLayer layer;
    private Texture texture;
    private Cell[] cells;
    private Vector2 position;

    public Player(TiledMap tiledMap, MapProperties props) {
        this.tiledMap = tiledMap;
        this.props = props;
        this.dir = Direction.NORTH;

        // Temporary variables for spliting textures
        int tileWidth = props.get("tilewidth", Integer.class);
        int tileHeight = props.get("tileheight", Integer.class);

        layer = (TiledMapTileLayer) tiledMap.getLayers().get("player");
        texture = new Texture("assets/players_300x300.png");
        TextureRegion[][] textureSplit = TextureRegion.split(texture, tileWidth, tileHeight);

        cells = new Cell[3];
        cells[NORMAL] = new Cell().setTile(new StaticTiledMapTile(textureSplit[0][NORMAL]));
        cells[DIED] = new Cell().setTile(new StaticTiledMapTile(textureSplit[0][DIED]));
        cells[WON] = new Cell().setTile(new StaticTiledMapTile(textureSplit[0][WON]));

        position = new Vector2(0,0);
        layer.setCell((int) getPositionX(), (int) getPositionY(), cells[NORMAL]);
    }

    public TiledMapTileLayer getLayer() {
        return layer;
    }

    public float getPositionX() {
        return position.x;
    }

    public float getPositionY() {
        return position.y;
    }

    public void update() {
        Cell cell = cells[NORMAL];

        if (dir == Direction.NORTH) {
            cell.setRotation(0);
        } else if (dir == Direction.EAST) {
            cell.setRotation(3);
        } else if (dir == Direction.SOUTH) {
            cell.setRotation(2);
        } else {
            cell.setRotation(1);
        }

        layer.setCell((int) getPositionX(), (int) getPositionY(), cell);
    }

    public Direction getDirection() {
        return dir;
    }

    public void rotate(boolean clockwise) {
        switch (dir) {
            case NORTH:
                dir = (clockwise) ? Direction.EAST : Direction.WEST;
                break;
            case EAST:
                dir = (clockwise) ? Direction.SOUTH : Direction.NORTH;
                break;
            case SOUTH:
                dir = (clockwise) ? Direction.WEST : Direction.EAST;
                break;
            case WEST:
                dir = (clockwise) ? Direction.NORTH : Direction.SOUTH;
                break;
        }
    }

    public void move() {
        int x = (int) position.x;
        int y = (int) position.y;

        switch (dir) {
            case NORTH:
                position.set(x, y + 1);
                break;
            case EAST:
                position.set(x + 1, y);
                break;
            case SOUTH:
                position.set(x, y - 1);
                break;
            case WEST:
                position.set(x - 1, y);
                break;
        }
    }

    @Override
    public boolean keyUp(int keyCode) {
        layer.setCell((int) position.x, (int) position.y, null);

        int x = (int) position.x;
        int y = (int) position.y;

        switch (keyCode) {
            case Input.Keys.LEFT:
                if (x - 1 < 0 || x - 1 >= props.get("width", Integer.class))
                    return false;
                else {
                    dir = Direction.WEST;
                    position.set(x - 1, y); return true;
                }

            case Input.Keys.RIGHT:
                if (x + 1 < 0 || x + 1 >= props.get("width", Integer.class))
                    return false;
                else {
                    dir = Direction.EAST;
                    position.set(x + 1, y); return true;
                }

            case Input.Keys.UP:
                if (y + 1 < 0 || y + 1 >= props.get("height", Integer.class))
                    return false;
                else {
                    dir = Direction.NORTH;
                    position.set(x, y + 1); return true;
                }

            case Input.Keys.DOWN:
                if (y - 1 < 0 || y - 1 >= props.get("height", Integer.class))
                    return false;
                else {
                    dir = Direction.SOUTH;
                    position.set(x, y - 1); return true;
                }

            case Input.Keys.SPACE:
                move();
                return true;

            default:
                return false;
        }
    }
}
