package inf112.gunit.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import inf112.gunit.GameState;
import inf112.gunit.board.Board;
import inf112.gunit.board.Direction;
import inf112.gunit.main.Main;
import inf112.gunit.player.Robot;
import inf112.gunit.player.card.ProgramCard;
import inf112.gunit.player.card.TestPrograms;

import java.util.ArrayList;
import java.util.Collections;

/**
 * The Game class is a screen which is rendered
 * when the Play-button is pressed in menu
 */
public class Game extends InputAdapter implements Screen {

    private static final int INTERVAL = 30;

    private GameState state;
    private boolean gameIsOver = false;

    private int tick;

    private Main main;
    private TiledMap map;
    private MapProperties props;

    private Board board;

    private Robot[] robots;
    private Robot mainRobot;

    private OrthographicCamera camera;
    private OrthogonalTiledMapRenderer tileRenderer;

    private int phase;
    private int cardIdx;
    private ArrayList<ProgramCard> roundCards;

    /**
     * The Game constructor
     * @param main takes a main
     * @param numOfPlayers number of players
     */
    public Game(Main main, int numOfPlayers) {
        if (numOfPlayers > 4) {
            System.err.println("Number of players cant be greater than 4!!");
            this.dispose();
            System.exit(1);
        } else if (numOfPlayers <= 0) {
            System.err.println("Number of players cant be less than 1!!");
            this.dispose();
            System.exit(1);
        }

        this.main = main;
        this.map = new TmxMapLoader().load("assets/board_new.tmx");
        this.robots = new Robot[numOfPlayers];
        board = new Board(this);

        // currently initialising the game in this state for testing purposes
        // this should actually be initialised to GameState.SETUP
        state = GameState.PROGRAM_CARD_EXECUTION;

        phase = 0;
        cardIdx = 0;

        tick = 0;

        props = map.getProperties();
        int mapWidth = map.getProperties().get("width", Integer.class);
        int mapHeight = map.getProperties().get("height", Integer.class);
        int tileWidth = map.getProperties().get("tilewidth", Integer.class);
        int tileHeight = map.getProperties().get("tileheight", Integer.class);

        // only used for testing
        // gives each robot a program
        for (int i = 0; i < numOfPlayers; i++) {
            Robot p = new Robot(this, i, board.getStartPosition(i));
            p.setProgram(TestPrograms.getProgram(i)); // give the robots a program (for testing)
            robots[i] = p;
        }

        // set the controllable robot (for testing)
        mainRobot = robots[0];

        //set the camera accordingly
        camera = new OrthographicCamera();
        camera.setToOrtho(false, mapWidth * tileWidth, mapHeight * tileHeight);
        camera.update();

        // set the tile renderer and add the camera view to it
        tileRenderer = new OrthogonalTiledMapRenderer(map, (float) 1 / tileWidth * tileHeight);
        tileRenderer.setView(camera);

        // add this class as the input processor
        Gdx.input.setInputProcessor(this);

        // start a new game-phase
        newPhase();
    }

    /**
     * Testing constructor used by the unit-tests
     * @param numOfPlayers number of players
     */
    public Game(int numOfPlayers, TiledMap map) {
        if (numOfPlayers > 4) {
            System.err.println("Number of players cant be greater than 4!!");
            this.dispose();
            System.exit(1);
        } else if (numOfPlayers <= 0) {
            System.err.println("Number of players cant be less than 1!!");
            this.dispose();
            System.exit(1);
        }

        this.map = map;
        this.robots = new Robot[numOfPlayers];
        board = new Board(this);

        // currently initialising the game in this state for testing purposes
        // this should actually be initialised to GameState.SETUP
        state = GameState.PROGRAM_CARD_EXECUTION;

        phase = 0;
        cardIdx = 0;

        tick = 0;

        props = map.getProperties();

        // only used for testing
        // gives each robot a program
        for (int i = 0; i < numOfPlayers; i++) {
            Robot p = new Robot(this, i, board.getStartPosition(i));
            p.setProgram(TestPrograms.getProgram(i)); // give the robots a program (for testing)
            robots[i] = p;
        }

        // set the controllable robot (for testing)
        mainRobot = robots[0];

        // start a new game-phase
        newPhase();
    }

    @Override
    public void show() {

    }

    /**
     * If a robot has collected all the flags, then the game is over
     * @param winner the winning player/robot
     */
    public void gameOver(Robot winner) {
        gameIsOver = true;
        state = GameState.SETUP;
        System.out.println();
        System.out.println("Game over!");
        System.out.println("Player with the " + winner + " robot won!");
        this.dispose();
        if (main != null) main.setScreen(new Menu(main));
        //System.exit(0);
    }

    /**
     * The logic-method handles all game logic
     * based on the GameState
     */
    private void logic() {

        switch (this.state) {
            // TODO: Implement setup phase, where you place flags etc...
            // if flags already is placed on board in the tiledmap file, this is not needed
            case SETUP:
                break;
            // TODO: Implement programming phase, where each player programs their robot
            // TODO: Need to implement HUD first
            case ROBOT_PROGRAMMING:
                System.out.println("programming");
                break;
            case PROGRAM_CARD_EXECUTION:
                // check if all cards this phase have been performed
                if (cardIdx >= roundCards.size()) {
                    state = GameState.CELL_MECHANIC_EXECUTION;
                } else if (tick % INTERVAL == 0) {
                    doTurn();
                }
                break;
            // TODO: finish this part (i.e. implement all mechanics)
            case CELL_MECHANIC_EXECUTION:

                if (tick % INTERVAL == 0) {
                    board.conveyExpress();
                    board.conveyRegular();
                    board.rotateGears();

                    // initialise a new phase
                    if (phase >= 4) {
                        newRound();
                    } else {
                        phase++;
                        newPhase();
                    }
                }
                break;
            default:
                System.err.println("GAME STATE NOT SET - FATAL ERROR OCCURRED.");
                System.err.println("ROBORALLY BRUH MOMENT");
                //this.dispose();
                //System.exit(1);
        }

        //handle rest of game mechanics
        board.holes();
        board.flags();

    }

    @Override
    public void render(float v) {
        Gdx.gl.glClearColor(1,0,0,1);
        tileRenderer.getBatch().begin();

        // handle the game-logic
        logic();

        // update the robot rendering
        for (Robot robot : robots) robot.update();
        tileRenderer.getBatch().end();
        // render the tile-map
        tileRenderer.setView(camera);
        tileRenderer.render();

        // increase the game tick
        tick++;
    }

    /**
     * Initialise a new round
     */
    private void newRound() {
        System.out.println("New round!");
        phase = 0;
        state = GameState.PROGRAM_CARD_EXECUTION; // here for testing
        newPhase(); // testing
        //state = GameState.ROBOT_PROGRAMMING;
    }

    /**
     * Begin a new phase.
     * Resets some variables, and retrieves cards from the robots
     */
    private void newPhase() {
        state = GameState.PROGRAM_CARD_EXECUTION;

        roundCards = new ArrayList<>();
        cardIdx = 0;

        for (Robot robot : robots) {
            roundCards.add(robot.getProgram()[phase]);
        }

        //sort cards by priority
        Collections.sort(roundCards);
        Collections.reverse(roundCards);
    }

    /**
     * Used for performing the appropriate program card in the current phase
     */
    private void doTurn() {
        // TODO: Get rid of the for-loop here by storing a 'currentRobot' as a field variable?
        ProgramCard card = roundCards.get(cardIdx);
        for (Robot robot : robots) {
            if (card.equals(robot.getProgram()[phase])) {
                System.out.println("Attempting to perform '" + card + "' on : '" + robot + "'");
                robot.doTurn(card);
                cardIdx++;
                break;
            }
        }
    }

    // key-listener currently used for testing
    @Override
    public boolean keyUp(int keyCode) {
        Vector2 position = mainRobot.getPosition();
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get("player_0");

        int x = (int) position.x;
        int y = (int) position.y;

        switch (keyCode) {
            case Input.Keys.LEFT:
                if (x - 1 < 0 || x - 1 >= props.get("width", Integer.class))
                    return false;
                else if (moveIsValid(Direction.WEST, x - 1, y)) {
                    layer.setCell((int) position.x, (int) position.y, null);
                    mainRobot.setDirection(Direction.WEST);
                    mainRobot.move(1);
                    return true;
                }

            case Input.Keys.RIGHT:
                if (x + 1 < 0 || x + 1 >= props.get("width", Integer.class))
                    return false;
                else if (moveIsValid(Direction.EAST, x + 1, y)) {
                    layer.setCell((int) position.x, (int) position.y, null);
                    mainRobot.setDirection(Direction.EAST);
                    mainRobot.move(1);
                    return true;
                }

            case Input.Keys.UP:
                if (y + 1 < 0 || y + 1 >= props.get("height", Integer.class))
                    return false;
                else if (moveIsValid(Direction.NORTH, x, y + 1)) {
                    layer.setCell((int) position.x, (int) position.y, null);
                    mainRobot.setDirection(Direction.NORTH);
                    mainRobot.move(1);
                    return true;
                }

            case Input.Keys.DOWN:
                if (y - 1 < 0 || y - 1 >= props.get("height", Integer.class))
                    return false;
                else if (moveIsValid(Direction.SOUTH, x, y - 1)) {
                    layer.setCell((int) position.x, (int) position.y, null);
                    mainRobot.setDirection(Direction.SOUTH);
                    mainRobot.move(1);
                    return true;
                }

            case Input.Keys.SPACE:
                this.doTurn();
                return true;

            default:
                return false;
        }
    }

    /**
     * Check if a given position at x- and y-coordinate is free
     * (basically means that, it doesn't contain a player)
     * @param x the desired x coordinate
     * @param y the desired y coordinate
     * @return true if position has pla
     */
    private boolean positionIsFree(Direction dir, int x, int y) {
        // TODO: dont use for-loop here, find a more efficient way
        // perhaps storing player-layer id's in a global variable?

        for (int i = 0; i < robots.length; i++) {
            if (((TiledMapTileLayer) map.getLayers().get("player_" + i)).getCell(x, y) != null)
                return false;
        }

        // wallCell is the cell you are trying to move to
        // prevCell is the cell you are currently standing on,
        // aka the cell you are moving from
        TiledMapTileLayer.Cell wallCell = ((TiledMapTileLayer) map.getLayers().get("walls")).getCell(x, y);
        TiledMapTileLayer.Cell prevCell = null;

        // Gets the cell you are currently on (before moving) by flipping the direction you are
        // trying to move to, and getting the cell at those coordinates
        if (Direction.flip(dir) == Direction.NORTH)
            prevCell = ((TiledMapTileLayer) map.getLayers().get("walls")).getCell(x, y + 1);
        if (Direction.flip(dir) == Direction.EAST)
            prevCell = ((TiledMapTileLayer) map.getLayers().get("walls")).getCell(x - 1, y);
        if (Direction.flip(dir) == Direction.SOUTH)
            prevCell = ((TiledMapTileLayer) map.getLayers().get("walls")).getCell(x, y - 1);
        if (Direction.flip(dir) == Direction.WEST)
            prevCell = ((TiledMapTileLayer) map.getLayers().get("walls")).getCell(x + 1, y);

        // Gets the direction the wall is facing, if the cell you are trying to move to has a wall
        if (wallCell != null) {
           Direction wallDir = Direction.lookup(wallCell.getTile().getProperties().get("direction").toString());

           // If the wall on the cell you are trying to move to is not facing you,
            // return true. Else return false.
           return dir != Direction.flip(wallDir);
        }

        // Gets the direction the wall is facing, if the cell you are currently on has a wall
        if (prevCell != null) {
            Direction prevDir = Direction.lookup(prevCell.getTile().getProperties().get("direction").toString());

            // If the wall on the cell you are currently on is not facing you,
            // return true. Else return false.
            return dir != prevDir;
        }

        // Return true if nothing is in the way
        return true;
    }

    /**
     * Check if a move is valid, called by the player class
     * !!Currently returns false if player actually can move, just not the distance intended to
     * @param x the desired x-position to move to
     * @param y the desired y-position to move to
     * @return true if move is possible, false otherwise
     */
    public boolean moveIsValid(Direction dir, int x, int y) {
        if (x >= 0 && x < props.get("width", Integer.class) && y >= 0 && y < props.get("height", Integer.class)) {
            // TODO: revert back to only this line when not debugging
            //return positionIsFree(x, y);

            if (positionIsFree(dir, x, y)) {
                System.out.println("Success! Moving...");
                return true;
            }
        }
        System.out.println("Move is not valid!");
        return false;
    }

    /**
     * Get the players/robots currently in the game
     * @return the robots
     */
    public Robot[] getRobots() {
        return robots;
    }

    /**
     * Get the TiledMap of the board
     * @return the TiledMap of the board
     */
    public TiledMap getMap() {
        return map;
    }

    /**
     * Check if game is over
     * @return true if game is over, false otherwise
     */
    public boolean getGameOver() {
        return gameIsOver;
    }

    @Override
    public void resize(int i, int i1) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
