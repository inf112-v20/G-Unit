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
import inf112.gunit.player.card.MovementCard;
import inf112.gunit.player.card.ProgramCard;
import inf112.gunit.player.card.RotationCard;

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
        this.position = new Vector2(0,0);

        int tileWidth = props.get("tilewidth", Integer.class);
        int tileHeight = props.get("tileheight", Integer.class);

        layer = (TiledMapTileLayer) tiledMap.getLayers().get("player");
        texture = new Texture("assets/players_300x300.png");
        TextureRegion[][] textureSplit = TextureRegion.split(texture, tileWidth, tileHeight);

        cells = new Cell[3];
        cells[NORMAL] = new Cell().setTile(new StaticTiledMapTile(textureSplit[0][NORMAL]));
        cells[DIED] = new Cell().setTile(new StaticTiledMapTile(textureSplit[0][DIED]));
        cells[WON] = new Cell().setTile(new StaticTiledMapTile(textureSplit[0][WON]));

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

    public void move(int distance) {
        int x = (int) this.getPositionX();
        int y = (int) this.getPositionY();

        switch (dir) {
            case NORTH:
                if (moveIsValid(x, y + distance)) position.set(x, y + distance);
                break;
            case EAST:
                if (moveIsValid(x + distance, y)) position.set(x + distance, y);
                break;
            case SOUTH:
                if (moveIsValid(x, y - distance)) position.set(x, y - distance);
                break;
            case WEST:
                if (moveIsValid(x - distance, y)) position.set(x - distance, y);
                break;
        }
    }

    private boolean moveIsValid(int x, int y) {
        return x >= 0 && x < props.get("width", Integer.class) && y >= 0 && y < props.get("height", Integer.class);
    }

    public void rotate(boolean clockwise, int numOfRotations) {
        for (int i = 0; i < numOfRotations; i++) {
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
    }

    public void doTurn(ProgramCard programCard) {
        switch (programCard.getType()) {
            case MOVEMENT:
                this.move(((MovementCard) programCard).getDistance());
                break;
            case ROTATION:
                this.rotate(((RotationCard) programCard).isClockwise(), ((RotationCard) programCard).getRotations());
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

            default:
                return false;
        }
    }
}
