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
 * The player class is used to perform all kinds of
 * player mechanics.
 */
public class Player {

    private int id;

    // these to are for testing, and (probably) wont be used in the actual game
    // program stores a program to execute
    // while counter is a global variable to keep track of what card to execute
    private ProgramCard[] program;

    private Game game;
    private MapProperties props;

    // the direction the player is facing
    private Direction dir;

    // the TiledMap layer of the player, texture-spritesheet and position.
    private TiledMapTileLayer layer;
    private Cell[] textures;
    private Vector2 position;

    // backupMemory is the position where the player starts, and if he gets a flag,
    // the flags position is now the new position of the players backupMemory.
    private Vector2 backupMemory;

    // Each player starts out with 3 lifeTokens, where one token is subtracted
    // each time a player dies (is killed or falls down a hole/pit).
    private int lifeToken = 3;

    // Each player starts with 10 damageMarkers, which represents health points.
    private int damageMarker = 10;

    /**
     * The Player constructor
     * @param game takes the Game object the player is instantiated from
     * @param id the desired identifier for the player
     */
    public Player(Game game, int id) {
        this.game = game;
        this.props = game.getMap().getProperties();
        this.dir = Direction.NORTH;
        backupMemory = position;
        this.position = new Vector2(id,0);
        this.id = id;

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
                if (game.moveIsValid(playerX, playerY + distance)) {
                    layer.setCell((int) playerX, (int) playerY, null);
                    position.set(playerX, playerY + distance);
                }
                break;
            case EAST:
                if (game.moveIsValid(playerX + distance, playerY)) {
                    layer.setCell((int) playerX, (int) playerY, null);
                    position.set(playerX + distance, playerY);
                }
                break;
            case SOUTH:
                if (game.moveIsValid(playerX, playerY - distance)) {
                    layer.setCell((int) playerX, (int) playerY, null);
                    position.set(playerX, playerY - distance);
                }
                break;
            case WEST:
                if (game.moveIsValid(playerX - distance, playerY)) {
                    layer.setCell((int) playerX, (int) playerY, null);
                    position.set(playerX - distance, playerY);
                }
                break;
        }
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
        this.program = Arrays.copyOf(program, 5);
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

    /** Checks to see if player has any lifeToken left, if so,
     * removes 1 lifeToken, and then respawn player at backupMemory.
     * If the player has no lifeTokens left, he/she is removed from the board entirely.
     */
    public void died(){
        if (this.lifeToken < 1) {
            //if player is dead and has no life tokens, this happens.
        }
        else { // Moves player back to backupMemory and restores damageMarkers.
            this.lifeToken--;
            this.position = backupMemory;
            this.damageMarker = 10;
        }
    }
}
