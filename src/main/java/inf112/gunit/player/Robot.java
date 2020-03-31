package inf112.gunit.player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Vector2;
import inf112.gunit.board.Direction;
import inf112.gunit.player.card.MovementCard;
import inf112.gunit.player.card.ProgramCard;
import inf112.gunit.player.card.RotationCard;
import inf112.gunit.screens.Game;

import java.util.Arrays;

/**
 * The Robot class is used to perform all kinds of
 * robot mechanics.
 */
public class Robot {

    public int flagsCollected = 0;

    private int id;

    // these to are for testing, and (probably) wont be used in the actual game
    // program stores a program to execute
    // while counter is a global variable to keep track of what card to execute
    private ProgramCard[] program;

    private Game game;
    private MapProperties props;

    // the direction the robot is facing
    private Direction dir;

    // the TiledMap layer of the robot, texture-spritesheet and position.
    private TiledMapTileLayer layer;
    private Cell[] textures;
    private Vector2 position;

    // backupMemory is the position where the robot starts, and if he gets a flag,
    // the flags position is now the new position of the robots backupMemory.
    private Vector2 backupMemory;

    // Each robot starts out with 3 lifeTokens, where one token is subtracted
    // each time a robot dies (is killed or falls down a hole/pit).
    private int lifeTokens = 3;

    // Each player/robot starts with 10 damageMarkers, which represents health points.
    private int damageMarkers = 10;

    /**
     * The Robot constructor
     * @param game takes the Game object the robot is instantiated from
     * @param id the desired identifier for the robot
     */
    public Robot(Game game, int id, Vector2 startPos) {
        this.game = game;
        this.props = game.getMap().getProperties();
        this.dir = Direction.NORTH;
        this.position = startPos;
        this.id = id;

        this.backupMemory = startPos.cpy();

        int tileWidth = props.get("tilewidth", Integer.class);
        int tileHeight = props.get("tileheight", Integer.class);

        // retrieve the layer
        layer = (TiledMapTileLayer) game.getMap().getLayers().get("player_" + id);

        // load the textures
        Texture texture = new Texture("assets/players_300x300.png");
        TextureRegion[][] textureSplit = TextureRegion.split(texture, tileWidth, tileHeight);

        // store the textures
        textures = new Cell[4];
        textures[0] = new Cell().setTile(new StaticTiledMapTile(textureSplit[0][0]));
        textures[1] = new Cell().setTile(new StaticTiledMapTile(textureSplit[0][1]));
        textures[2] = new Cell().setTile(new StaticTiledMapTile(textureSplit[0][2]));
        textures[3] = new Cell().setTile(new StaticTiledMapTile(textureSplit[0][3]));

        // initialise the robots texture given the id
        layer.setCell((int) getPositionX(), (int) getPositionY(), textures[id]);
    }

    /**
     * Update the robots texture, rotation and position
     */
    public void update() {
        // the NORMAL-texture is currently the only one being used
        Cell cell = textures[id];

        // set rotation according to direction
        if (dir == Direction.NORTH) {
            cell.setRotation(0);
        } else if (dir == Direction.EAST) {
            cell.setRotation(3);
        } else if (dir == Direction.SOUTH) {
            cell.setRotation(2);
        } else {
            cell.setRotation(1);
        }

        // update the tiled-map
        layer.setCell((int) getPositionX(), (int) getPositionY(), cell);
    }

    /**
     * Move the robot a given distance
     * @param distance how many tiles to move the robot
     */
    public void move(int distance) {
        int x = (int) this.getPositionX();
        int y = (int) this.getPositionY();

        switch (dir) {
            case NORTH:
                if (game.moveIsValid(dir, x, y + distance)) {
                    layer.setCell(x, y, null);
                    position.set(x, y + distance);
                }
                break;
            case EAST:
                if (game.moveIsValid(dir, x + distance, y)) {
                    layer.setCell(x, y, null);
                    position.set(x + distance, y);
                }
                break;
            case SOUTH:
                if (game.moveIsValid(dir, x, y - distance)) {
                    layer.setCell(x, y, null);
                    position.set(x, y - distance);
                }
                break;
            case WEST:
                if (game.moveIsValid(dir, x - distance, y)) {
                    layer.setCell(x, y, null);
                    position.set(x - distance, y);
                }
                break;
        }
    }

    /**
     * Rotates the robot in 90 degree intervals
     * Only updates the Direction, actual texture-rotation mechanic is handled
     * by the update()-method
     * @param clockwise true if rotation is clockwise, false if counter-clockwise
     * @param numOfRotations number of 90 degree turns
     */
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
  
    /**
     * Parent method of move() and rotate(), called with a ProgramCard
     * @param programCard the program card to execute
     */
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

    /**
     * Set a program for the current round
     * @param program the input program to run on the robot
     */
    public void setProgram(ProgramCard[] program) {
        if (program.length != 5) throw new IllegalArgumentException("Program must be of length 5");
        this.program = Arrays.copyOf(program, 5);
    }

    /**
     * Get the number of life tokes of the robot
     * @return the number of life tokens
     */
    public int getLifeTokens() {
        return lifeTokens;
    }

    /**
     * Get the number of damage markers of the robot
     * @return the number of damage markers
     */
    public int getDamageMarkers() {
        return damageMarkers;
    }

    /**
     * Get the backup memory position
     * @return the Vector2 backup memory
     */
    public Vector2 getBackupMemory() {
        return backupMemory;
    }

    /**
     * Set the position of the robot
     * @param position the desired position
     */
    public void setPosition(Vector2 position) {
        this.position = position;
    }

    /**
     * Checks to see if robot has any lifeToken left, if so,
     * removes 1 lifeToken, and then respawn player/robot at backupMemory.
     * If the player/robot has no lifeTokens left, he/she is removed from the board entirely.
     */
    public void die(){
        // Moves player/robot back to backupMemory and restores damageMarkers.
        this.lifeTokens--;
        this.position = backupMemory;
        this.damageMarkers = 10;
    }

    /**
     * Get the number of flags collected by the robot
     * @return number of flags collected
     */
    public int getFlagsCollected() {
        return flagsCollected;
    }

    /**
     * Set the number of flags collected by the robot
     * @param flagsCollected number of flags
     */
    public void setFlagsCollected(int flagsCollected) {
        this.flagsCollected = flagsCollected;
    }

    /**
     * Get the current program of the robot
     * @return the current program
     */
    public ProgramCard[] getProgram() {
        return program;
    }

    /**
     * Get the current position of the robot
     * @return the current position
     */
    public Vector2 getPosition() {
        return this.position;
    }

    /**
     * Get the x-coordinate of the robot
     * @return the robots x-position
     */
    public float getPositionX() {
        return position.x;
    }

    /**
     * Get the y-coordinate of the robot
     * @return the robots y-position
     */
    public float getPositionY() {
        return position.y;
    }

    /**
     * Get the direction the robot is currently facing
     * @return the current direction
     */
    public Direction getDirection() {
        return dir;
    }

    /**
     * Set the direction of the robot
     * @param dir the desired direction
     */
    public void setDirection(Direction dir) {
        this.dir = dir;
    }

    /**
     * Get the robots identifier
     * @return the id of the robot
     */
    public int getId() {
        return id;
    }

    /**
     * Get the TiledMapTileLayer of the robot
     * @return
     */
    public TiledMapTileLayer getLayer() {
        return layer;
    }

    @Override
    public String toString() {
        switch (id) {
            case 0:
                return "Red";
            case 1:
                return "Green";
            case 2:
                return "Yellow";
            case 3:
                return "Cyan";
            default:
                return "" + id;
        }
    }
}
