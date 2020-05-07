package inf112.gunit.board;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import inf112.gunit.player.Robot;
import inf112.gunit.screens.Game;

import java.util.ArrayList;

public class Board {

    private Game game;

    private int width;
    private int height;

    private ArrayList<Vector2> flagPositions = new ArrayList<>();

    public Board(Game game) {
        this.game = game;
        this.width = game.getMap().getProperties().get("width", Integer.class);
        this.height = game.getMap().getProperties().get("height", Integer.class);
        this.flagPositions = loadFlagPositions();
    }

    /**
     * Private helper method that conveys a robot
     * called by conveyExpress and conveyRegular
     * @param robot the given robot to convey
     * @param cell the given cell the robot is standing on
     */
    private void convey(Robot robot, TiledMapTileLayer.Cell cell) {
        String tileDir = cell.getTile().getProperties().get("direction").toString();
        robot.move(1, Direction.lookup(tileDir));
    }

    /**
     * Handle mechanics for all express conveyors
     */
    public void conveyExpress() {
        for (Robot robot : game.getRobots()) {
            TiledMapTileLayer layer = (TiledMapTileLayer) game.getMap().getLayers().get("conveyors");
            TiledMapTileLayer.Cell cell = layer.getCell((int) robot.getPositionX(), (int) robot.getPositionY());

            if (cell != null && Boolean.parseBoolean(cell.getTile().getProperties().get("express").toString()))
                convey(robot, cell);
        }
    }

    /**
     * Handle mechanics for all conveyors
     */
    public void conveyRegular() {
        for (Robot robot : game.getRobots()) {
            TiledMapTileLayer layer = (TiledMapTileLayer) game.getMap().getLayers().get("conveyors");
            TiledMapTileLayer.Cell cell = layer.getCell((int) robot.getPositionX(), (int) robot.getPositionY());

            if (cell != null) convey(robot,cell);
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

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (layer.getCell(x, y) != null) {
                    pos = new Vector2();
                    pos.set(x, y);
                    break;
                }
            }
        }

        return pos;
    }

    /**
     * Perform hole mechanics
     */
    public void holes() {
        TiledMapTileLayer holes = (TiledMapTileLayer) game.getMap().getLayers().get("holes");

        for (Robot robot : game.getRobots()) if(holes.getCell((int) robot.getPositionX(), (int) robot.getPositionY()) != null) robot.die();
    }

    /**
     * Perform flag mechanics
     */
    public void flags() {
        TiledMapTileLayer layer = (TiledMapTileLayer) game.getMap().getLayers().get("flags");
        for (Robot robot : game.getRobots()) {
            TiledMapTileLayer.Cell cell = layer.getCell((int) robot.getPositionX(), (int) robot.getPositionY());

            if (cell != null) {
                robot.setBackupMemory(robot.getPosition().cpy());

                int flagNum = (int) cell.getTile().getProperties().get("num");
                if (flagNum == robot.flagsCollected + 1) {
                    robot.setFlagsCollected(robot.getFlagsCollected() + 1);
                    robot.repair();
                    System.out.println(robot + " picked up flag number " + flagNum);
                }
            }

            if (robot.getFlagsCollected() >= 4) game.gameOver(robot);
        }
    }

    private boolean fireLaser(int x, int y, int damage) {
        TiledMapTileLayer walls = (TiledMapTileLayer) game.getMap().getLayers().get("walls");

        if (walls.getCell(x, y) != null) {
            if (Direction.lookup(walls.getCell(x, y).getTile().getProperties().get("direction", String.class)) == Direction.WEST) {
                return false;
            }

            for (Robot robot : game.getRobots()) {
                if (robot.getPositionX() == x && robot.getPositionY() == y) {
                    robot.handleDamage(damage);
                    return false;
                }
            }

            return Direction.lookup(walls.getCell(x, y).getTile().getProperties().get("direction", String.class)) != Direction.EAST;
        } else {
            for (Robot robot : game.getRobots()) {
                if (robot.getPositionX() == x && robot.getPositionY() == y) {
                    robot.handleDamage(damage);
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Fire lasers on board
     */
    public void lasersFire() {
        TiledMapTileLayer lasers = (TiledMapTileLayer) game.getMap().getLayers().get("lasers");

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (lasers.getCell(x, y) != null) {
                    Direction dir = Direction.lookup(lasers.getCell(x, y).getTile().getProperties().get("direction", String.class));
                    int damage = lasers.getCell(x, y).getTile().getProperties().get("damage", Integer.class);

                    if (dir == Direction.NORTH) {
                        for (int i = y; i < height; i++) {
                            if (!fireLaser(x, i, damage)) break;
                        }
                    } else if (dir == Direction.EAST) {
                        for (int i = x; i < width; i++) {
                            if (!fireLaser(i, y, damage)) break;
                        }
                    } else if (dir == Direction.SOUTH) {
                        for (int i = y; i >= 0; i--) {
                            if (!fireLaser(x, i, damage)) break;
                        }
                    } else if (dir == Direction.WEST) {
                        for (int i = x; i >= 0; i--) {
                            if (!fireLaser(i, y, damage)) break;
                        }
                    } else {
                        System.err.println("fatal error oh shit");
                    }
                }
            }
        }
    }

    /**
     * Kills a robot if it is outside of the map.
     */
    public void outOfMapTrigger() {
        for (Robot r : game.getRobots()) {
            if (r.getPositionX() < 0 || r.getPositionX() >= width || r.getPositionY() < 0 || r.getPositionY() >= height) {
                r.die();
            }
        }
    }

    /**
     * Fires the lasers of all the robots.
     */
    public void robotsFire(){
        for (Robot robot : game.getRobots()) {
            robot.fire();
        }
    }

    /**
     * Load flag positions into an ArrayList
     * @return an ArrayList containing all flags corresponding Vector2-positions
     */
    private ArrayList<Vector2> loadFlagPositions() {
        Vector2[] positions = new Vector2[4];
        TiledMapTileLayer layer = (TiledMapTileLayer) game.getMap().getLayers().get("flags");

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (layer.getCell(x, y) != null) {
                    int n = (int) layer.getCell(x, y).getTile().getProperties().get("num");

                    switch (n) {
                        case 1:
                            positions[0]= new Vector2(x, y);
                            break;
                        case 2:
                            positions[1]= new Vector2(x, y);
                            break;
                        case 3:
                            positions[2]= new Vector2(x, y);
                            break;
                        case 4:
                            positions[3]= new Vector2(x, y);
                            break;
                        default:
                            System.err.println("Unknown flag id " + n);
                            break;
                    }
                }
            }
        }

        ArrayList<Vector2> result = new ArrayList<>();
        for (Vector2 pos : positions) {
            if (pos != null) result.add(pos);
        }

        return result;
    }

    /**
     * Get all flag positions
     * @return an ArrayList containing all flags corresponding Vector2-positions
     */
    public ArrayList<Vector2> getFlagPositions() {
        return flagPositions;
    }
}
