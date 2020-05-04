package inf112.gunit.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import inf112.gunit.GameState;
import inf112.gunit.board.Board;
import inf112.gunit.board.Direction;
import inf112.gunit.hud.Hud;
import inf112.gunit.main.Main;
import inf112.gunit.player.Robot;
import inf112.gunit.player.card.MovementCard;
import inf112.gunit.player.card.ProgramCard;
import inf112.gunit.player.card.TestPrograms;

import java.util.ArrayList;
import java.util.Collections;

/**
 * The Game class is a screen which is rendered
 * when the Play-button is pressed in menu
 */
public class Game extends InputAdapter implements Screen {

    public float tileScale;

    public SpriteBatch batch;

    private Hud hud;

    private static final int INTERVAL = 30;

    private GameState state;
    private boolean gameIsOver = false;

    private int tick;

    private Main main;
    private TiledMap map;
    private MapProperties props;

    private Board board;

    private Robot[] robots;
    private Robot playerRobot;

    private OrthographicCamera camera;
    private OrthogonalTiledMapRenderer tileRenderer;
    
    private Music background_music;

    private int phase;
    private int cardIdx;
    private ArrayList<ProgramCard> roundCards = new ArrayList<>();

    private int numOfPlayers;

    /**
     * The Game constructor
     * @param main takes a main
     * @param numOfPlayers number of players
     */
    public Game(Main main, int numOfPlayers) {
        this.numOfPlayers = numOfPlayers;
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
        this.map = new TmxMapLoader().load("assets/robot_board.tmx");
        this.robots = new Robot[numOfPlayers];
        this.batch = new SpriteBatch();
        props = map.getProperties();
        board = new Board(this);
        phase = 0;
        cardIdx = 0;
        tick = 0;

        //initialise robots
        for (int i = 0; i < numOfPlayers; i++) {
            Robot p = new Robot(this, i, board.getStartPosition(i));
            robots[i] = p;
        }

        // set the controllable robot (for testing)
        playerRobot = robots[0];
        hud = new Hud(Main.batch, this);

        int mapWidth = props.get("width", Integer.class);
        int mapHeight = props.get("height", Integer.class);
        int tileWidth = props.get("tilewidth", Integer.class);
        int tileHeight = props.get("tileheight", Integer.class);

        tileScale = Main.HEIGHT / mapHeight;

        //set the camera accordingly
        camera = new OrthographicCamera();
        camera.setToOrtho(false, (mapWidth * tileWidth) * ((float) Main.WIDTH / (float) Main.HEIGHT), mapHeight * tileHeight);
        camera.update();

        // loads and plays background music
        /*
        background_music = Gdx.audio.newMusic(Gdx.files.internal("in_da_club.wav"));
        background_music.setLooping(true);
        background_music.setVolume(0.5f);
        background_music.play();
         */

        // set the tile renderer and add the camera view to it
        tileRenderer = new OrthogonalTiledMapRenderer(map, (float) 1 / (tileWidth) * (tileHeight));
        tileRenderer.setView(camera);

        newRound();
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
        props = map.getProperties();
        board = new Board(this);
        phase = 0;
        cardIdx = 0;
        tick = 0;

        for (int i = 0; i < numOfPlayers; i++) {
            Robot p = new Robot(this, i, board.getStartPosition(i));
            robots[i] = p;
        }

        tileScale = Main.HEIGHT / map.getProperties().get("height", Integer.class);

        playerRobot = robots[0];

        newRound();
    }

    @Override
    public void show() {

    }

    /**
     * If a robot has collected all the flags, then the game is over
     * @param winnerRobot the winning player/robot
     */
    public void gameOver(Robot winnerRobot) {
        gameIsOver = true;
        state = GameState.SETUP;
        if (background_music != null) background_music.stop();
        this.dispose();
        if (main != null) main.setScreen(new GameOver(main, winnerRobot, numOfPlayers));
        //System.exit(0);
    }

    /**
     * The logic-method handles all game logic
     * based on the GameState
     */
    private void logic() {

        switch (this.state) {
            case SETUP:
                for (Robot r : robots) {
                    r.dealCards();
                    if (r != playerRobot) {
                        ProgramCard[] p = new ProgramCard[5];
                        ArrayList<ProgramCard> p_list = r.hardAI(board.getFlagPositions());
                        
                        for (int i = 0; i < p_list.size(); i++) {
                            p[i] = p_list.get(i);
                            System.out.println(p[i].toString());
                        }
                        
                        r.setProgram(p);
                    }
                }
                if (!playerRobot.isPoweredDown()) {
                    hud.updateCards();
                } else System.out.println("not updating cards");
                state = GameState.ROBOT_PROGRAMMING;
                break;
            case ROBOT_PROGRAMMING:
                if (!playerRobot.isPoweredDown()) {
                    if (playerRobot.isDonePicking) {
                        ProgramCard[] program = playerRobot.getProgramBuffer().toArray(new ProgramCard[5]);
                        playerRobot.setProgram(program);
                        playerRobot.isDonePicking = false;
                        hud.clearCards();
                        newPhase();
                    }
                } else newPhase();
                break;
            case PROGRAM_CARD_EXECUTION:
                // check if all cards this phase have been performed
                if (cardIdx >= roundCards.size()) {
                    state = GameState.CELL_MECHANIC_EXECUTION;
                } else if (tick % INTERVAL == 0) {
                    doTurn();
                }
                break;
            case CELL_MECHANIC_EXECUTION:
                map.getLayers().get("laser_beams").setVisible(true);

                if (tick % INTERVAL == 0) {
                    board.conveyExpress();
                    board.conveyRegular();
                    board.rotateGears();
                    board.holes();
                    board.lasersFire();
                    board.robotsFire();

                    for (Robot robot : robots){
                        if (robot.getDamageMarkers() >= 10)
                            robot.die();
                    }

                    board.flags();

                    // initialise a new phase
                    if (phase >= 4) {
                        newRound();
                    } else {
                        phase++;
                        newPhase();
                    }
                    map.getLayers().get("laser_beams").setVisible(false);
                }
                break;
            default:
                System.err.println("GAME STATE NOT SET - FATAL ERROR OCCURRED.");
                System.err.println("ROBORALLY BRUH MOMENT");
                this.dispose();
                Gdx.app.exit();
                System.exit(69);
        }

    }

    public boolean fallIntoHole(Robot robot) {
        TiledMapTileLayer holes = (TiledMapTileLayer) map.getLayers().get("holes");

        if (holes.getCell((int) robot.getPositionX(), (int) robot.getPositionY()) != null) {
            board.holes();
            return true;
        }
        return false;
    }

    @Override
    public void render(float v) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        tileRenderer.getBatch().begin();

        // handle the game-logic
        logic();

        // update the robot rendering
        for (Robot robot : robots) robot.update();

        tileRenderer.getBatch().end();
        // render the tile-map
        tileRenderer.setView(camera);
        tileRenderer.render();

        Main.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.draw();

        batch.begin();
        for (Robot r : robots) {
            r.draw(batch);
        }
        batch.end();

        // increase the game tick
        tick++;
    }

    /**
     * Initialise a new round
     */
    private void newRound() {
        System.out.println("New round!");

        for (Robot robot : robots) {
            if (robot.isPoweredDown()) {
                robot.setPoweredDown(false);
                if (robot.getPowerDownDesire()) robot.updatePowerDownDesire();
                hud.resetPowerDownButton();
            }
            if (robot.getPowerDownDesire()) robot.setPoweredDown(true);
        }

        phase = 0;
        state = GameState.SETUP;
    }

    /**
     * Begin a new phase.
     * Resets some variables, and retrieves cards from the robots
     */
    private void newPhase() {
        roundCards = new ArrayList<>();
        cardIdx = 0;

        for (Robot robot : robots) {
            roundCards.add(robot.getProgram()[phase]);
            // When new phase starts, robots will be able to search and shoot again.
            robot.setHasFired(false);
            robot.setHasSearched(false);
        }

        //sort cards by priority
        Collections.sort(roundCards);
        Collections.reverse(roundCards);

        state = GameState.PROGRAM_CARD_EXECUTION;
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
        // prevCell is the cell you are currently standing on, aka the cell you are moving from
        // Also checks for lasers, since they are also walls
        TiledMapTileLayer.Cell wallCell = ((TiledMapTileLayer) map.getLayers().get("walls")).getCell(x, y);
        TiledMapTileLayer.Cell laserCell = ((TiledMapTileLayer) map.getLayers().get("lasers")).getCell(x, y);
        TiledMapTileLayer.Cell prevCell = null;
        TiledMapTileLayer.Cell prevLaserCell = null;

        // Gets the cell you are currently on (before moving) by flipping the direction you are
        // trying to move to, and getting the cell at those coordinates
        if (Direction.flip(dir) == Direction.NORTH) {
            prevCell = ((TiledMapTileLayer) map.getLayers().get("walls")).getCell(x, y + 1);
            prevLaserCell = ((TiledMapTileLayer) map.getLayers().get("lasers")).getCell(x, y + 1);
        }
        else if (Direction.flip(dir) == Direction.EAST) {
            prevCell = ((TiledMapTileLayer) map.getLayers().get("walls")).getCell(x + 1, y);
            prevLaserCell = ((TiledMapTileLayer) map.getLayers().get("lasers")).getCell(x + 1, y);
        }
        else if (Direction.flip(dir) == Direction.SOUTH) {
            prevCell = ((TiledMapTileLayer) map.getLayers().get("walls")).getCell(x, y - 1);
            prevLaserCell = ((TiledMapTileLayer) map.getLayers().get("lasers")).getCell(x, y - 1);
        }
        else if (Direction.flip(dir) == Direction.WEST) {
            prevCell = ((TiledMapTileLayer) map.getLayers().get("walls")).getCell(x - 1, y);
            prevLaserCell = ((TiledMapTileLayer) map.getLayers().get("lasers")).getCell(x - 1, y);
        }
        // Gets the direction the wall is facing, if the cell you are trying to move to has a wall
        if (wallCell != null) {
           Direction wallDir = Direction.lookup(wallCell.getTile().getProperties().get("direction").toString());
           // If the wall on the cell you are trying to move to is not facing you,
            // return true. Else return false.
           return dir != Direction.flip(wallDir);
        }

        if (laserCell != null) {
            // The direction of a laser is in the direction it shoots, so the actual wall is in the opposite direction
            // of the direction property of the laser
            Direction laserDir = Direction.lookup(laserCell.getTile().getProperties().get("direction").toString());

            return dir != laserDir;
        }

        // Gets the direction the wall is facing, if the cell you are currently on has a wall
        if (prevCell != null) {
            Direction prevDir = Direction.lookup(prevCell.getTile().getProperties().get("direction").toString());
            // If the wall on the cell you are currently on is not facing you,
            // return true. Else return false.
            return dir != prevDir;
        }

        if (prevLaserCell != null) {
            Direction prevLaserDir = Direction.lookup(prevLaserCell.getTile().getProperties().get("direction").toString());

            return dir != Direction.flip(prevLaserDir);
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
            return positionIsFree(dir, x, y);
        }
        return false;
    }

    /**
     * Searches for robots in the direction the Robot shooter is facing.
     * Calls handleDamage() on Robot target if there is a target, with shooter.getPower() as the amount of damage.
     * After that it sets hasFired and hasSearched to true on Robot shooter.
     * @param x is the targets x position.
     * @param y is the targets y position.
     * @param shooter is the robot that is doing the shooting.
     */
    public void searchAndDestroy(int x, int y, Robot shooter){
        for (Robot target : robots) {
            if ((int) target.getPositionX() == x && (int) target.getPositionY() == y) {
                target.handleDamage(shooter.getPower());
                shooter.setHasFired(true);
                shooter.setHasSearched(true);

                System.out.println("The " + shooter.toString() + " robot" +
                        shooter.getPosition() + " shot the " +
                        target.toString() + " robot" + target.getPosition()
                        + " from " + shooter.getDirection() + ".");
            }
        }
    }

    /**
     * Get the players/robots currently in the game
     * @return the robots
     */
    public Robot[] getRobots() {
        return robots;
    }

    /**
     * Get the local players robot
     * @return the local players robot
     */
    public Robot getPlayerRobot() {
        return playerRobot;
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

    /**
     * Get the current state of the game
     * @return the current game state
     */
    public GameState getState() {
        return state;
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
