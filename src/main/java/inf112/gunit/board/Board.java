package inf112.gunit.board;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import inf112.gunit.player.Robot;
import inf112.gunit.screens.Game;

import java.util.ArrayList;

public class Board {

    private Game game;

    public Board(Game game) {
        this.game = game;
    }

    // TODO: add rotations for corners

    /**
     * Handle mechanics for all express conveyors
     */
    public void conveyExpress() {
        for (Robot robot : game.getRobots()) {
            TiledMapTileLayer layer = (TiledMapTileLayer) game.getMap().getLayers().get("conveyors");
            TiledMapTileLayer.Cell cell = layer.getCell((int) robot.getPositionX(), (int) robot.getPositionY());

            if (cell != null && Boolean.parseBoolean(cell.getTile().getProperties().get("express").toString())) {
                robot.getLayer().setCell((int) robot.getPositionX(), (int) robot.getPositionY(), null);
                String tileDir = cell.getTile().getProperties().get("direction").toString();
                robot.setDirection(Direction.valueOf(tileDir));
                robot.move(1);
            }
        }
    }

    /**
     * Handle mechanics for all conveyors
     */
    public void convey() {
        for (Robot robot : game.getRobots()) {
            TiledMapTileLayer layer = (TiledMapTileLayer) game.getMap().getLayers().get("conveyors");
            TiledMapTileLayer.Cell cell = layer.getCell((int) robot.getPositionX(), (int) robot.getPositionY());

            if (cell != null) {
                robot.getLayer().setCell((int) robot.getPositionX(), (int) robot.getPositionY(), null);
                String tileDir = cell.getTile().getProperties().get("direction").toString();
                robot.setDirection(Direction.valueOf(tileDir));
                robot.move(1);
            }
        }
    }

    /**
     * Handle mechanics for all gears
     */
    public void rotateGears() {
        for (Robot robot : game.getRobots()) {
            TiledMapTileLayer layer = (TiledMapTileLayer) game.getMap().getLayers().get("gears");
            TiledMapTileLayer.Cell cell = layer.getCell((int) robot.getPositionX(), (int) robot.getPositionY());

            if (cell != null) {
                boolean clockwise = Boolean.parseBoolean(cell.getTile().getProperties().get("clockwise").toString());
                robot.rotate(clockwise, 1);
            }
        }
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

    public void holes() {
        TiledMapTileLayer layer = (TiledMapTileLayer) game.getMap().getLayers().get("holes");

        for (Robot robot : game.getRobots()) {
            if(layer.getCell((int) robot.getPositionX(), (int) robot.getPositionY()) != null) robot.die();
        }
    }

    public void flags() {
        TiledMapTileLayer flagsTileLayer = (TiledMapTileLayer) game.getMap().getLayers().get("flags");
        for (Robot robot : game.getRobots()) {
            if (flagsTileLayer.getCell((int) robot.getPositionX(), (int) robot.getPositionY()) == flagsTileLayer.getCell(0, 5)){
                robot.setFlagsCollected(1);
            }
            if (flagsTileLayer.getCell((int) robot.getPositionX(), (int) robot.getPositionY()) == flagsTileLayer.getCell(4, 7) && robot.getFlagsCollected() == 1){
                robot.setFlagsCollected(2);
            }
            if (flagsTileLayer.getCell((int) robot.getPositionX(), (int) robot.getPositionY()) == flagsTileLayer.getCell(8, 0) && robot.getFlagsCollected() == 2){
                robot.setFlagsCollected(3);
            }
            if (flagsTileLayer.getCell((int) robot.getPositionX(), (int) robot.getPositionY()) == flagsTileLayer.getCell(2, 2) && robot.getFlagsCollected() == 3){
                robot.setFlagsCollected(4);
                game.gameOver(robot);
            }
        }
    }
}
