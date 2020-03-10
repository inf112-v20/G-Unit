package inf112.gunit.player;

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

/**
 * The player class is used to perform all kinds of
 * player mechanics.
 */
public class Player {

    private int id;

    // these to are for testing, and (probably) wont be used in the actual game
    // program stores a program to execute
    // while counter is a global variable to keep track of what card to execute
    private ProgramCard[] program = new ProgramCard[5];

    // the games, tiledmap is stored here along with properties
    private TiledMap tiledMap;
    private MapProperties props;

    // the direction the player is facing
    private Direction dir;

    // the TiledMap layer of the player, texture-spritesheet and position
    private TiledMapTileLayer layer;
    private Cell[] textures;
    private Vector2 position;

    /**
     * The Player constructor
     * @param tiledMap takes the tiledMap stored in Game as reference
     */
    public Player(TiledMap tiledMap, int id) {
        this.tiledMap = tiledMap;
        this.props = tiledMap.getProperties();
        this.dir = Direction.NORTH;
        this.position = new Vector2(id,0);
        this.id = id;

        // this is for testing only
        program[0] = new MovementCard(300,2);
        program[1] = new RotationCard(300,1,true);
        program[2] = new MovementCard(300,2);
        program[3] = new RotationCard(300, 2, false);
        program[4] = new MovementCard(300, 1);

        int tileWidth = props.get("tilewidth", Integer.class);
        int tileHeight = props.get("tileheight", Integer.class);

        // retrieve the layer
        layer = (TiledMapTileLayer) tiledMap.getLayers().get("player_" + id);

        // load the textures
        Texture texture = new Texture("assets/players_300x300.png");
        TextureRegion[][] textureSplit = TextureRegion.split(texture, tileWidth, tileHeight);

        // store the textures
        textures = new Cell[4];
        textures[0] = new Cell().setTile(new StaticTiledMapTile(textureSplit[0][0]));
        textures[1] = new Cell().setTile(new StaticTiledMapTile(textureSplit[0][1]));
        textures[2] = new Cell().setTile(new StaticTiledMapTile(textureSplit[0][2]));
        textures[3] = new Cell().setTile(new StaticTiledMapTile(textureSplit[0][3]));

        // initialise the player with the NORMAL-texture
        layer.setCell((int) getPositionX(), (int) getPositionY(), textures[id]);

    }

    /**
     * Update the players texture, rotation and position
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
     * Move the player a given distance
     * @param distance how many tiles to move the player
     */
    public void move(int distance) {
        int playerX = (int) this.getPositionX();
        int playerY = (int) this.getPositionY();

        switch (dir) {
            case NORTH:
                if (moveIsValid(playerX, playerY + distance)) {
                    layer.setCell((int) playerX, (int) playerY, null);
                    position.set(playerX, playerY + distance);
                }
                break;
            case EAST:
                if (moveIsValid(playerX + distance, playerY)) {
                    layer.setCell((int) playerX, (int) playerY, null);
                    position.set(playerX + distance, playerY);
                }
                break;
            case SOUTH:
                if (moveIsValid(playerX, playerY - distance)) {
                    layer.setCell((int) playerX, (int) playerY, null);
                    position.set(playerX, playerY - distance);
                }
                break;
            case WEST:
                if (moveIsValid(playerX - distance, playerY)) {
                    layer.setCell((int) playerX, (int) playerY, null);
                    position.set(playerX - distance, playerY);
                }
                break;
        }
    }

    /**
     * Private helper-method to check if a given move is valid
     * !!Currently returns false if player actually can move, just not the distance intended to
     * @param x the desired x-position to move to
     * @param y the desired y-position to move to
     * @return true if move is possible, false otherwise
     */
    private boolean moveIsValid(int x, int y) {
        return x >= 0 && x < props.get("width", Integer.class) && y >= 0 && y < props.get("height", Integer.class);
    }

    /**
     * Rotates the player in 90 degree intervals
     * Only updates the Direction, actual rotation mechanic is handled
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
     * @param program the input program to run on the player
     */
    public void setProgram(ProgramCard[] program) {
        if (program.length != 5) throw new IllegalArgumentException("Program must be of length 5");
        System.arraycopy(program, 0, this.program, 0, program.length);
    }

    /**
     * Get the current program of the player
     * @return the current program
     */
    public ProgramCard[] getProgram() {
        return program;
    }

    /**
     * Get the current position of a player
     * @return the current position
     */
    public Vector2 getPosition() {
        return this.position;
    }

    /**
     * Get the x-coordinate of the player
     * @return the players x-position
     */
    public float getPositionX() {
        return position.x;
    }

    /**
     * Get the y-coordinate of the player
     * @return the players y-position
     */
    public float getPositionY() {
        return position.y;
    }

    /**
     * Get the direction the player is currently facing
     * @return the current direction
     */
    public Direction getDirection() {
        return dir;
    }

    /**
     * Set the direction of the player
     * @param dir the desired direction
     */
    public void setDirection(Direction dir) {
        this.dir = dir;
    }

    /**
     * Get the players identifier
     * @return the id of the player
     */
    public int getId() {
        return id;
    }
}
