package inf112.gunit.board;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
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
    public void conveyExpress() {
        for (Robot robot : game.getRobots()) {
            TiledMapTileLayer layer = (TiledMapTileLayer) game.getMap().getLayers().get("conveyors");
            TiledMapTileLayer.Cell cell = layer.getCell((int) robot.getPositionX(), (int) robot.getPositionY());

            if (cell != null && Boolean.parseBoolean(cell.getTile().getProperties().get("express").toString())) {
                robot.getLayer().setCell((int) robot.getPositionX(), (int) robot.getPositionY(), null);
                String tileDir = cell.getTile().getProperties().get("direction").toString();
                robot.setDirection(Direction.valueOf(tileDir));
                robot.move(1);
                System.out.println("Moving robot " + robot + " express");
            }
        }
    }

    public void convey() {
        for (Robot robot : game.getRobots()) {
            TiledMapTileLayer layer = (TiledMapTileLayer) game.getMap().getLayers().get("conveyors");
            TiledMapTileLayer.Cell cell = layer.getCell((int) robot.getPositionX(), (int) robot.getPositionY());

            if (cell != null) {
                robot.getLayer().setCell((int) robot.getPositionX(), (int) robot.getPositionY(), null);
                String tileDir = cell.getTile().getProperties().get("direction").toString();
                robot.setDirection(Direction.valueOf(tileDir));
                robot.move(1);
                System.out.println("Moving robot " + robot + " regular");
            }
        }
    }

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

    public int getNumberOfFlags() {
        ArrayList<TiledMapTileLayer> layers = new ArrayList<>();

        for (MapLayer layer : game.getMap().getLayers()) {
            if (layer.getName().contains("flag")) layers.add((TiledMapTileLayer) layer);
        }

        return layers.size();
    }
}
