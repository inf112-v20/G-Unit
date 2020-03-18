package inf112.gunit.board;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Vector2;
import inf112.gunit.player.Robot;
import inf112.gunit.screens.Game;

/**
 * Implement logic and mechanics here
 */
public class Board {

    private Game game;

    public Board(Game game) {
        this.game = game;
    }

    /**
     * Moves the given robot according to the conveyor on which it stands
     * @param robot the robot to 'convey'
     * @param layer the TiledMapTileLayer corresponding to the conveyors
     * @return a new Vector2 position which is the cell the robot should move to
     */
    public Vector2 convey(Robot robot, TiledMapTileLayer layer) {
        Vector2 newPos = robot.getPosition().cpy();

        int x = (int) robot.getPositionX();
        int y = (int) robot.getPositionY();

        // get the last word in the layer name

        String name = layer.getName().substring(layer.getName().lastIndexOf("_")+1);

        // check the direction of the conveyor and set the position accordingly
        if (name.equals("north")) {
            if (game.moveIsValid(x, y+1)) {
                newPos = new Vector2();
                newPos.set(x, y+1);
            }
        } else if (name.equals("east")) {
            if (game.moveIsValid(x+1, y)) {
                newPos = new Vector2();
                newPos.set(x+1, y);
            }
        } else if (name.equals("south")) {
            if (game.moveIsValid(x, y-1)) {
                newPos = new Vector2();
                newPos.set(x, y-1);
            }
        } else if (name.equals("west")) {
            if (game.moveIsValid(x-1, y)) {
                newPos = new Vector2();
                newPos.set(x-1, y);
            }
        } else {
            System.err.println("UNKNOWN DIRECTION: " + name);
        }

        return newPos;
    }

    /**
     * Rotate the given robot according to the gear it is standing on
     * @param robot the robot to rotate
     * @param layer the given TiledMapTileLayer corresponding to the gears
     */
    public void gear(Robot robot, TiledMapTileLayer layer) {
        // get the last word in the layer name
        String name = layer.getName().substring(layer.getName().lastIndexOf("_")+1);

        if (name.equals("clockwise"))
            robot.rotate(true, 1);
        else
            robot.rotate(false, 1);
    }

    /**
     * Get the starting position of a given robot id
     * Used when initialising each robot
     * @param id the given robot id/starting position
     * @return the Vector2 starting position
     */
    public Vector2 getStartPosition(int id) {
        Vector2 pos = null;
        TiledMapTileLayer layer = (TiledMapTileLayer) game.getMap().getLayers().get("start_" + id);
        layer.setVisible(true);

        for (int x = 0; x < (game.getMap().getProperties().get("width", Integer.class)); x++) {
            for (int y = 0; y < (game.getMap().getProperties().get("height", Integer.class)); y++) {
                if (layer.getCell(x, y) != null) {
                    pos = new Vector2();
                    pos.set(x, y);
                    break;
                }
            }
        }

        return pos;
    }
}
