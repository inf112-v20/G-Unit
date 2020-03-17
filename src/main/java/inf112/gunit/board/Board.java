package inf112.gunit.board;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayers;
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

    public Vector2 convey(Robot robot, TiledMapTileLayer layer) {
        Vector2 newPos = robot.getPosition().cpy();

        int x = (int) robot.getPositionX();
        int y = (int) robot.getPositionY();

        String name = layer.getName().substring(layer.getName().lastIndexOf("_")+1);

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

    public void gear(Robot robot, TiledMapTileLayer layer) {
        String name = layer.getName().substring(layer.getName().lastIndexOf("_")+1);

        if (name.equals("clockwise"))
            robot.rotate(true, 1);
        else
            robot.rotate(false, 1);
    }
}
