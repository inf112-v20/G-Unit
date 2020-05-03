package inf112.gunit.player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Vector2;
import inf112.gunit.board.Direction;
import inf112.gunit.main.Main;
import inf112.gunit.player.card.MovementCard;
import inf112.gunit.player.card.ProgramCard;
import inf112.gunit.player.card.RotationCard;
import inf112.gunit.screens.Game;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * The Robot class is used to perform all kinds of
 * robot mechanics.
 */
public class Robot extends Sprite {

    public int flagsCollected = 0;

    private final int id;

    // these to are for testing, and (probably) wont be used in the actual game
    // program stores a program to execute
    // while counter is a global variable to keep track of what card to execute
    private ProgramCard[] program;

    private ArrayList<ProgramCard> cardDeck = new ArrayList<>();
    private ArrayList<ProgramCard> programBuffer = new ArrayList<>();
    public boolean isDonePicking = false;
  
    private Vector2 prevPrevPos;
    private Vector2 prevPos;

    private Game game;
    private MapProperties props;

    // the direction the robot is facing
    private Direction dir;

    // the TiledMap layer of the robot, texture-spritesheet and position.
    private final TiledMapTileLayer layer;
    private final TextureRegion[][] textureSplit;
    private Vector2 position;

    private boolean isMoving = false;
    private boolean isRotating = false;
    private final int ANIMATION_DELTA = 30;
    private int animationTick = 0;
    private int animationTileNum;
    private Direction animationDir;

    // backupMemory is the position where the robot starts, and if he gets a flag,
    // the flags position is now the new position of the robots backupMemory.
    private Vector2 backupMemory;

    // Each robot starts out with 3 lifeTokens, where one token is subtracted
    // each time a robot dies (is killed or falls down a hole/pit).
    private int lifeTokens = 3;

    // Each player/robot starts with 0 damageMarkers, which represents health points.
    private int damageMarkers = 0;

    // Each player has one shot each "round"/phase.
    private boolean hasFired = false;

    // Each robot can search only once each "round"/phase.
    private boolean hasSearched = false;

    // The amount of damage a robots weapon takes.
    private int power = 1;

    private boolean wantsToPowerDown = false;
    private boolean poweredDown = false;

    /**
     * The Robot constructor
     * @param game takes the Game object the robot is instantiated from
     * @param id the desired identifier for the robot
     */
    public Robot(Game game, int id, Vector2 startPos) {
        super(TextureRegion.split(new Texture("assets/players_300x300.png"), 300, 300)[0][id]);

        int tileWidth = game.getMap().getProperties().get("tilewidth", Integer.class);
        int tileHeight = game.getMap().getProperties().get("tileheight", Integer.class);

        setScale((float) tileWidth/Main.HEIGHT);

        this.game = game;
        this.dir = Direction.NORTH;
        this.position = startPos;
        this.prevPos = new Vector2(100, 100);
        this.id = id;

        this.backupMemory = startPos.cpy();

        // retrieve the layer
        layer = (TiledMapTileLayer) game.getMap().getLayers().get("player_" + id);

        // load the textures
        textureSplit = TextureRegion.split(new Texture("assets/players_300x300.png"), tileWidth, tileHeight);
    }

    private void setGridPos(float x, float y) {
        this.setPosition(x, y);
        this.setX(this.getPositionX() * game.tileScale - 109);
        this.setY(this.getPositionY() * game.tileScale - 109);
    }

    private void moveAnimation(int numTiles, Direction moveDir) {
        if (moveDir == Direction.NORTH) {
            setY(getY() + animationTick * game.tileScale / (100 / numTiles));
        } else if (moveDir == Direction.EAST) {
            setX(getX() + animationTick * game.tileScale / (100 / numTiles));
        } else if (moveDir == Direction.SOUTH) {
            setY(getY() - animationTick * game.tileScale / (100 / numTiles));
        } else if (moveDir == Direction.WEST) {
            setX(getX() - animationTick * game.tileScale / (100 / numTiles));
        } else {
            System.err.println("UNKNOWN DIRECTION: " + dir);
        }
    }

    private void rotationAnimation(Direction animationDir) {

        if (animationDir == Direction.NORTH) {
            setRotation(getRotation() - (Direction.calcDegDiff(Direction.NORTH, dir) / 15));
        } else if (animationDir == Direction.EAST) {
            setRotation(getRotation() - (Direction.calcDegDiff(Direction.EAST, dir) / 15));
        } else if (animationDir == Direction.SOUTH) {
            setRotation(getRotation() - (Direction.calcDegDiff(Direction.SOUTH, dir) / 15));
        } else if (animationDir == Direction.WEST) {
            setRotation(getRotation() - (Direction.calcDegDiff(Direction.WEST, dir) / 15));
        } else {
            System.err.println("UNKNOWN DIRECTION: " + animationDir);
        }
    }

    private void animate() {
        if (animationTick == 15) {
            isMoving = false;
            isRotating = false;
            this.animationTick = 0;
            return;
        }

        if (isRotating) rotationAnimation(animationDir);
        if (isMoving) moveAnimation(animationTileNum, animationDir);

        animationTick++;
    }

    /**
     * Update the robots texture, rotation and position
     */
    public void update() {
        if (isRotating) this.animate();
        if (isMoving) this.animate();
        else this.setGridPos(this.getPositionX(), this.getPositionY());
    }

    /**
     * Move the robot a given distance
     * @param distance how many tiles to move the robot
     */
    public void move(int distance) {
        move(distance, null);
    }

    /**
     * Move the robot a given distance in a given direction
     * @param distance how many tiles to move the robot
     * @param dir the direction to move the robot
     */
    public void move(int distance, Direction dir) {
        Direction direction;

        if (dir != null)
            direction = dir;
        else
            direction = this.dir;

        int x;
        int y;

        animationDir = direction;

        if (direction == Direction.NORTH) {
            for (int i = 1; i <= distance; i++) {
                x = (int) this.getPositionX();
                y = (int) this.getPositionY();

                if (game.moveIsValid(Direction.NORTH, x, y + 1)) {
                    position.set(x, y + 1);
                    setProperRotation();
                    isMoving = true;
                    animationTileNum = distance;
                }
            }
        }
        else if (direction == Direction.EAST) {
            for (int i = 1; i <= distance; i++) {
                x = (int) this.getPositionX();
                y = (int) this.getPositionY();

                if (game.moveIsValid(Direction.EAST, x + 1, y)) {
                    position.set(x + 1, y);
                    setProperRotation();
                    isMoving = true;
                    animationTileNum = distance;
                }
            }
        }
        else if (direction == Direction.SOUTH) {
            for (int i = 1; i <= distance; i++) {
                x = (int) this.getPositionX();
                y = (int) this.getPositionY();

                if (game.moveIsValid(Direction.SOUTH, x, y - 1)) {
                    position.set(x, y - 1);
                    setProperRotation();
                    isMoving = true;
                    animationTileNum = distance;
                }
            }
        }
        else if (direction == Direction.WEST) {
            for (int i = 1; i <= distance; i++) {
                x = (int) this.getPositionX();
                y = (int) this.getPositionY();

                if (game.moveIsValid(Direction.WEST, x - 1, y)) {
                    position.set(x - 1, y);
                    setProperRotation();
                    isMoving = true;
                    animationTileNum = distance;
                }
            }
        }
        else {
            System.err.println("Invalid direction: " + direction + "!");
            System.err.println("Not moving!");
        }
    }

    /**
     * Set rotation according current tile
     */
    private void setProperRotation() {
        TiledMapTileLayer.Cell cell = ((TiledMapTileLayer) game.getMap().getLayers().get("conveyors")).getCell((int) this.getPositionX(), (int) this.getPositionY());
        if (cell == null) return;

        TiledMapTile tile = cell.getTile();

        if (Boolean.parseBoolean(tile.getProperties().get("rotation").toString()))
            this.setDirection(Direction.lookup(tile.getProperties().get("direction").toString()));
    }

    /**
     * Rotates the robot in 90 degree intervals
     * Only updates the Direction, actual texture-rotation mechanic is handled
     * by the update()-method
     * @param clockwise true if rotation is clockwise, false if counter-clockwise
     * @param numOfRotations number of 90 degree turns
     */
    public void rotate(boolean clockwise, int numOfRotations) {
        animationDir = dir;

        for (int i = 0; i < numOfRotations; i++) {
            switch (dir) {
                case NORTH:
                    dir = (clockwise) ? Direction.EAST : Direction.WEST;
                    isRotating = true;
                    break;
                case EAST:
                    dir = (clockwise) ? Direction.SOUTH : Direction.NORTH;
                    isRotating = true;
                    break;
                case SOUTH:
                    dir = (clockwise) ? Direction.WEST : Direction.EAST;
                    isRotating = true;
                    break;
                case WEST:
                    dir = (clockwise) ? Direction.NORTH : Direction.SOUTH;
                    isRotating = true;
                    break;
            }
        }
    }
  
    /**
     * Parent method of move() and rotate(), called with a ProgramCard
     * @param programCard the program card to execute
     */
    public void doTurn(ProgramCard programCard) {
        if (!poweredDown) {
            switch (programCard.getType()) {
                case MOVEMENT:
                    this.move(((MovementCard) programCard).getDistance());
                    break;
                case ROTATION:
                    this.rotate(((RotationCard) programCard).isClockwise(), ((RotationCard) programCard).getRotations());
                    break;
            }
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
     * Add a card to the program buffer
     * @param card the card to add
     */
    public void addBufferCard(ProgramCard card) {
        programBuffer.add(card);
    }

    /**
     * Get the program buffer
     * @return the program buffer
     */
    public ArrayList<ProgramCard> getProgramBuffer() {
        return programBuffer;
    }

    /**
     * At the start of each phase, deal random program cards to each player
     */
    public void dealCards() {
        Random r = new Random();
        cardDeck = new ArrayList<>();
        programBuffer = new ArrayList<>();

        //compute number of cards to be dealt based on damage tokens
        int numOfCards = (damageMarkers >= 4) ? 5 : 9 - damageMarkers;
        for (int i = 0; i < numOfCards; i++) {
            int isMoveCard = r.nextInt(3); // 67% chance of getting a movement card per card
            int priority = (r.nextInt(70) + 10) * 10;

            if (isMoveCard > 0) {
                int distance = r.nextInt(3) + 1;
                cardDeck.add(new MovementCard(priority, distance));
            } else {
                boolean clockwise = r.nextBoolean();
                int rotations = r.nextInt(2) + 1;
                cardDeck.add(new RotationCard(priority, rotations, clockwise));
            }
        }
    }
    
    public ArrayList<ProgramCard> hardAI(ArrayList<Vector2> flagPos) {
        if (flagsCollected == 4)
            return new ArrayList<ProgramCard>();
        
        Vector2 nextFlagPos = flagPos.get(flagsCollected);

        ArrayList<ProgramCard> newCards = pathTo(new ArrayList<ProgramCard>(), position, nextFlagPos, dir);
        if (position.equals(prevPos)) {
            System.out.println("adding spice");

            prevPos = position.cpy();

            return addSomeSpice(newCards);
        }

        prevPos = position.cpy();

        return newCards;
    }
    
    public ArrayList<ProgramCard> addSomeSpice(ArrayList<ProgramCard> cards) {
        Random rand = new Random();
        cards.set(rand.nextInt(cards.size()), new MovementCard(1000, 2));
        cards.set(rand.nextInt(cards.size()), new RotationCard(1000, rand.nextInt(3) + 1, rand.nextBoolean()));

        return cards;
    }
    
    public ArrayList<ProgramCard> pathTo(ArrayList<ProgramCard> currentPath, Vector2 from, Vector2 to, Direction curDir) {
        if (currentPath.size() == 5) {
            System.out.println("Got path with five cards!");
            return currentPath;
        }
        
        float deltaX = from.x - to.x;
        //System.out.println("deltaX: " + deltaX + ", from.x: " + from.x + ", to.x: " + to.x);
        float deltaY = from.y - to.y;
        System.out.println("deltaY: " + deltaY + ", from.y: " + from.y + ", to.y: " + to.y);

        if (deltaX > 0) {
            //System.out.println("deltaX > 0");
            if (curDir == Direction.WEST) {
                int extraDist = 0;
                TiledMapTileLayer.Cell convCellOne = ((TiledMapTileLayer) game.getMap().getLayers().get("conveyors")).getCell((int) from.x - 1, (int) from.y);
                TiledMapTileLayer.Cell convCellTwo = ((TiledMapTileLayer) game.getMap().getLayers().get("conveyors")).getCell((int) from.x - 2, (int) from.y);

                if (convCellOne != null) {
                    Direction cellOneDir = Direction.lookup(convCellOne.getTile().getProperties().get("direction").toString());
                    if (Direction.flip(cellOneDir) == curDir)
                        extraDist++;
                    System.out.println(cellOneDir);
                }
                else if (convCellTwo != null) {
                    Direction cellTwoDir = Direction.lookup(convCellTwo.getTile().getProperties().get("direction").toString());
                    if (Direction.flip(cellTwoDir) == curDir)
                        extraDist++;
                    System.out.println(cellTwoDir);
                }

                if (deltaX >= 3) {
                    currentPath.add(new MovementCard(1000, 3));
                    return pathTo(currentPath, new Vector2(from.x - 3, from.y), to, curDir);
                }    
                else if (deltaX >= 2) {
                    currentPath.add(new MovementCard(1000, 2 + extraDist));
                    return pathTo(currentPath, new Vector2(from.x - 2 - extraDist, from.y), to, curDir);
                }    
                else if (deltaX >= 1) {
                    currentPath.add(new MovementCard(1000, 1 + extraDist));
                    return pathTo(currentPath, new Vector2(from.x - 1 - extraDist, from.y), to, curDir);
                }    
            }
            else if (curDir == Direction.SOUTH) {
                currentPath.add(new RotationCard(1000, 1, true));
                return pathTo(currentPath, from, to, Direction.getClockwiseDirection(curDir));
            }
            else if (curDir == Direction.EAST) {
                currentPath.add(new RotationCard(1000, 2, true));
                return pathTo(currentPath, from, to, Direction.flip(curDir));
            }
            else if (curDir == Direction.NORTH) {
                currentPath.add(new RotationCard(1000, 1, false));
                return pathTo(currentPath, from, to, Direction.getAntiClockwiseDirection(curDir));
            }
        }
        else if (deltaX < 0) {
            if (curDir == Direction.EAST) {
                int extraDist = 0;
                TiledMapTileLayer.Cell convCellOne = ((TiledMapTileLayer) game.getMap().getLayers().get("conveyors")).getCell((int) from.x + 1, (int) from.y);
                TiledMapTileLayer.Cell convCellTwo = ((TiledMapTileLayer) game.getMap().getLayers().get("conveyors")).getCell((int) from.x + 2, (int) from.y);

                if (convCellOne != null) {
                    Direction cellOneDir = Direction.lookup(convCellOne.getTile().getProperties().get("direction").toString());
                    if (Direction.flip(cellOneDir) == curDir)
                        extraDist++;
                    System.out.println(cellOneDir);
                }
                else if (convCellTwo != null) {
                    Direction cellTwoDir = Direction.lookup(convCellTwo.getTile().getProperties().get("direction").toString());
                    if (Direction.flip(cellTwoDir) == curDir)
                        extraDist++;
                    System.out.println(cellTwoDir);
                }

                if (deltaX <= -3) {
                    currentPath.add(new MovementCard(1000, 3));
                    return pathTo(currentPath, new Vector2(from.x + 3, from.y), to, curDir);
                }
                else if (deltaX <= -2) {
                    currentPath.add(new MovementCard(1000, 2 + extraDist));
                    return pathTo(currentPath, new Vector2(from.x + 2 + extraDist, from.y), to, curDir);
                }
                else if (deltaX <= -1) {
                    currentPath.add(new MovementCard(1000, 1 + extraDist));
                    return pathTo(currentPath, new Vector2(from.x + 1 + extraDist, from.y), to, curDir);
                }
            }
            else if (curDir == Direction.SOUTH) {
                currentPath.add(new RotationCard(1000, 1, false));
                return pathTo(currentPath, from, to, Direction.getAntiClockwiseDirection(curDir));
            }
            else if (curDir == Direction.WEST) {
                currentPath.add(new RotationCard(1000, 2, true));
                return pathTo(currentPath, from, to, Direction.flip(curDir));
            }
            else if (curDir == Direction.NORTH) {
                currentPath.add(new RotationCard(1000, 1, true));
                return pathTo(currentPath, from, to, Direction.getClockwiseDirection(curDir));
            }
        }
         if (deltaY > 0) {
            if (curDir == Direction.SOUTH) {
                int extraDist = 0;
                TiledMapTileLayer.Cell convCellOne = ((TiledMapTileLayer) game.getMap().getLayers().get("conveyors")).getCell((int) from.x, (int) from.y - 1);
                TiledMapTileLayer.Cell convCellTwo = ((TiledMapTileLayer) game.getMap().getLayers().get("conveyors")).getCell((int) from.x, (int) from.y - 2);

                if (convCellOne != null) {
                    Direction cellOneDir = Direction.lookup(convCellOne.getTile().getProperties().get("direction").toString());
                    if (Direction.flip(cellOneDir) == curDir)
                        extraDist++;
                    System.out.println(cellOneDir);
                }
                else if (convCellTwo != null) {
                    Direction cellTwoDir = Direction.lookup(convCellTwo.getTile().getProperties().get("direction").toString());
                    if (Direction.flip(cellTwoDir) == curDir)
                        extraDist++;
                    System.out.println(cellTwoDir);
                }

                if (deltaY >= 3) {
                    currentPath.add(new MovementCard(1000, 3));
                    return pathTo(currentPath, new Vector2(from.x, from.y - 3), to, curDir);
                }
                else if (deltaY <= 2) {
                    currentPath.add(new MovementCard(1000, 2 + extraDist));
                    return pathTo(currentPath, new Vector2(from.x, from.y - 2 - extraDist), to, curDir);
                }
                else if (deltaY >= 1) {
                    currentPath.add(new MovementCard(1000, 1 + extraDist));
                    return pathTo(currentPath, new Vector2(from.x, from.y - 1 - extraDist), to, curDir);
                }
            }
            else if (curDir == Direction.WEST) {
                currentPath.add(new RotationCard(1000, 1, false));
                return pathTo(currentPath, from, to, Direction.getAntiClockwiseDirection(curDir));
            }
            else if (curDir == Direction.NORTH) {
                currentPath.add(new RotationCard(1000, 2, true));
                return pathTo(currentPath, from, to, Direction.flip(curDir));
            }
            else if (curDir == Direction.EAST) {
                currentPath.add(new RotationCard(1000, 1, true));
                return pathTo(currentPath, from, to, Direction.getClockwiseDirection(curDir));
            }
        }
        if (deltaY < 0) {
            if (curDir == Direction.NORTH) {
                int extraDist = 0;
                TiledMapTileLayer.Cell convCellOne = ((TiledMapTileLayer) game.getMap().getLayers().get("conveyors")).getCell((int) from.x, (int) from.y + 1);
                TiledMapTileLayer.Cell convCellTwo = ((TiledMapTileLayer) game.getMap().getLayers().get("conveyors")).getCell((int) from.x, (int) from.y + 2);

                if (convCellOne != null) {
                    Direction cellOneDir = Direction.lookup(convCellOne.getTile().getProperties().get("direction").toString());
                    if (Direction.flip(cellOneDir) == curDir)
                        extraDist++;
                    System.out.println(cellOneDir);
                }
                else if (convCellTwo != null) {
                    Direction cellTwoDir = Direction.lookup(convCellTwo.getTile().getProperties().get("direction").toString());
                    if (Direction.flip(cellTwoDir) == curDir)
                        extraDist++;
                    System.out.println(cellTwoDir);
                }

                if (deltaY <= -3) {
                    currentPath.add(new MovementCard(1000, 3));
                    return pathTo(currentPath, new Vector2(from.x, from.y + 3), to, curDir);
                }
                else if (deltaY <= -2) {
                    currentPath.add(new MovementCard(1000, 2 + extraDist));
                    return pathTo(currentPath, new Vector2(from.x, from.y + 2 + extraDist), to, curDir);
                }
                else if (deltaY <= -1) {
                    currentPath.add(new MovementCard(1000, 1 + extraDist));
                    return pathTo(currentPath, new Vector2(from.x, from.y + 1 + extraDist), to, curDir);
                }
            }
            else if (curDir == Direction.EAST) {
                currentPath.add(new RotationCard(1000, 1, false));
                return pathTo(currentPath, from, to, Direction.getAntiClockwiseDirection(curDir));
            }
            else if (curDir == Direction.SOUTH) {
                currentPath.add(new RotationCard(1000, 2, true));
                return pathTo(currentPath, from, to, Direction.flip(curDir));
            }
            else if (curDir == Direction.WEST) {
                currentPath.add(new RotationCard(1000, 1, true));
                return pathTo(currentPath, from, to, Direction.getClockwiseDirection(curDir));
            }
        }

        System.out.println("adding random cards");
        int rem = 5 - currentPath.size();
        for (int i = 0; i < rem; i++) {
            currentPath.add(cardDeck.get(i));
        }
        
        return currentPath;
    }


    public float getScore(ArrayList<ProgramCard> cards, Vector2 robotPosition) {
        Vector2 newPosition = robotPosition.cpy();
        Direction newDir = dir;

        for (ProgramCard card : cards) {
            if (card instanceof MovementCard) {
                if (newDir == Direction.NORTH) {
                    newPosition.set(newPosition.x, newPosition.y + ((MovementCard) card).getDistance());
                }
                else if (newDir == Direction.EAST) {
                    newPosition.set(newPosition.x + ((MovementCard) card).getDistance(), newPosition.y);
                }
                else if (newDir == Direction.SOUTH) {
                    newPosition.set(newPosition.x, newPosition.y - ((MovementCard) card).getDistance());
                }
                else if (newDir == Direction.WEST) {
                    newPosition.set(newPosition.x - ((MovementCard) card).getDistance(), newPosition.y);
                }
            }
            else {
                for (int i = 0; i < ((RotationCard) card).getRotations(); i++) {
                    if (((RotationCard) card).isClockwise())
                        newDir = Direction.getClockwiseDirection(newDir);
                    else
                        newDir = Direction.getAntiClockwiseDirection(newDir);
                }
            }
        }
        
        return newPosition.dst(robotPosition);
    }

    /**
     * Get the robots card deck
     * @return the card deck
     */
    public ArrayList<ProgramCard> getCardDeck() {
        return cardDeck;
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
        layer.setCell((int) this.getPositionX(), (int) this.getPositionY(), null);

        this.lifeTokens--;
        this.position = backupMemory.cpy();
        this.damageMarkers = 0;
        if (this.lifeTokens <= 0){
            // TODO : Remove/dispose robots, that have zero lifeTokens and zero damageMarkers, from the game.
        }
    }

    /**
     * Update the powerdown status. Called by the HUD
     */
    public void updatePowerDownDesire() {
        wantsToPowerDown = !wantsToPowerDown;
        if (wantsToPowerDown) System.out.println(this + " wants to powerdown");
        else System.out.println(this + " doesn't want to powerdown anymore");
    }

    /**
     * Get the powerdown status.
     */
    public boolean getPowerDownDesire() {
        return wantsToPowerDown;
    }

    /**
     * Get the power status of the robot.
     */
    public boolean isPoweredDown() {
        return poweredDown;
    }

    /**
     * Set the power status of the robot
     */
    public void setPoweredDown(boolean onOff) {
        poweredDown = onOff;
        if (onOff) {
            this.damageMarkers = 0;
            program = new ProgramCard[]{
                    new MovementCard(-1, 1),
                    new MovementCard(-1, 1),
                    new MovementCard(-1, 1),
                    new MovementCard(-1, 1),
                    new MovementCard(-1, 1)
            };
        }
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

    public TextureRegion getTextureRegion() {
        return textureSplit[0][id];
    }

    /**
     * Get the TiledMapTileLayer of the robot
     * @return the layer of the robot
     */
    public TiledMapTileLayer getLayer() {
        return layer;
    }

    /**
     * Set hasFired to true or false.
     * @param hasFired boolean
     */
    public void setHasFired(boolean hasFired) {
        this.hasFired = hasFired;
    }

    /**
     * Set hasSearched to true or false.
     * @param hasSearched boolean
     */
    public void setHasSearched(boolean hasSearched) {
        this.hasSearched = hasSearched;
    }

    /**
     * This is called on a robot that is taking damage.
     * @param power is the amount of damage taken.
     */
    public void handleDamage(int power){
        this.damageMarkers += power;
    }

    /**
     * Get a robots power.
     * @return power
     */
    public int getPower() {
        return power;
    }

    public void setBackupMemory(Vector2 backupMemory) {
        this.backupMemory = backupMemory;
    }

    /**
     * This method is called when it's time for the robots to shoot.
     */
    public void fire() {
        int x = (int) this.getPositionX();
        int y = (int) this.getPositionY();
        Direction direction = this.getDirection();

        // Check which direction this robot is facing.
        switch (direction){
            case NORTH:
                //Checks how many cells are left on the board from the robot to the edge of the board
                for (int i = 0; i < (game.getMap().getProperties().get("height", Integer.class) - (y - 1)); i++) {
                    // See if this robot has already searched or shot this round.
                    if (!this.hasSearched && !this.hasFired) {
                        // Call searchAndDestroy which deals with damage.
                        game.searchAndDestroy(x, y + i + 1, this);
                    }
                }
                // After searching set hasSearched to true.
                this.setHasSearched(true);
                break;
            case SOUTH:
                for (int i = 0; i < game.getMap().getProperties().get("height", Integer.class) - (game.getMap().getProperties().get("height", Integer.class) - (y + 1)); i++) {
                    if (!this.hasSearched && !this.hasFired) {
                        game.searchAndDestroy(x, y - i - 1, this);
                    }
                }
                this.setHasSearched(true);
                break;
            case EAST:
                for (int i = 0; i < (game.getMap().getProperties().get("width", Integer.class) - (x + 1)); i++) {
                    if (!this.hasSearched && !this.hasFired) {
                        game.searchAndDestroy(x + i + 1, y, this);
                    }
                }
                this.setHasSearched(true);
                break;
            case WEST:
                for (int i = 0; i < game.getMap().getProperties().get("width", Integer.class) - (game.getMap().getProperties().get("width", Integer.class) - (x + 1)); i++) {
                    if (!this.hasSearched && !this.hasFired) {
                        game.searchAndDestroy(x - i - 1, y, this);
                    }
                }
                this.setHasSearched(true);
                break;
            default:
                System.err.println(this + " is facing an invalid direction. Can't fire!");
                break;
        }
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
