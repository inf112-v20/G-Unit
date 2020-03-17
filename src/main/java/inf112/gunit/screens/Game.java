package inf112.gunit.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
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

    public static TextureRegion[][] spriteSheet;

    private int tick;

    private Main main;
    private TiledMap map;
    private TiledMapTileLayer[] layers;
    private MapProperties props;

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
     * @param map takes a TiledMap as the board
     */
    public Game(Main main, TiledMap map, int numOfPlayers) {
        this.main = main;
        this.map = map;
        this.robots = new Robot[numOfPlayers];

        phase = -1;
        cardIdx = 0;

        MapLayers mapLayers = map.getLayers();
        layers = new TiledMapTileLayer[mapLayers.size()];
        for (int i = 0; i < layers.length; i++) {
            layers[i] = (TiledMapTileLayer) mapLayers.get(i);
        }

        tick = 0;

        props = map.getProperties();
        int mapWidth = map.getProperties().get("width", Integer.class);
        int mapHeight = map.getProperties().get("height", Integer.class);
        int tileWidth = map.getProperties().get("tilewidth", Integer.class);
        int tileHeight = map.getProperties().get("tileheight", Integer.class);

        for (int i = 0; i < numOfPlayers; i++) {
            Robot p = new Robot(this, i);
            p.setProgram(TestPrograms.getProgram(i)); // give the robots a program (for testing)
            robots[i] = p;
        }

        mainRobot = robots[0];

        camera = new OrthographicCamera();
        camera.setToOrtho(false, mapWidth * tileWidth, mapHeight * tileHeight);
        camera.update();

        tileRenderer = new OrthogonalTiledMapRenderer(map, (float) 1 / tileWidth * tileHeight);
        tileRenderer.setView(camera);

        Gdx.input.setInputProcessor(this);

        newPhase();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float v) {
        Gdx.gl.glClearColor(1,0,0,1);

        // update the player
        for (Robot robot : robots) {
            robot.update();

            if (tick % 30 == 0) {
                for (int i = 0; i < layers.length; i++) {
                    if (layers[i].getName().equals("rotator_clockwise") && layers[i].getCell((int) robot.getPositionX(), (int) robot.getPositionY()) != null) {
                        robot.rotate(true, 1);
                    } else if (layers[i].getName().equals("rotator_counter_clockwise") && layers[i].getCell((int) robot.getPositionX(), (int) robot.getPositionY()) != null) {
                        robot.rotate(false, 1);
                    }
                }
            }
        }
    
        // render the tile-map
        tileRenderer.setView(camera);
        tileRenderer.render();

        tick++; // increase the game tick
    }

    /**
     * Initialise a new round
     */
    private void newRound() {
        phase = 0;
    }

    /**
     * Begin a new phase.
     * Resets some variables, and retrieves cards from the robots
     */
    private void newPhase() {
        phase++;

        if (phase >= 5) {
            newRound();
            System.out.println("New round!");
        }

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
        System.out.println();

        // check if  all program cards have been executed
        if (cardIdx >= roundCards.size()) {
            newPhase();
            System.out.println("New phase!");
            return; // maybe remove this?
        }

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
                else {
                    layer.setCell((int) position.x, (int) position.y, null);
                    mainRobot.setDirection(Direction.WEST);
                    position.set(x - 1, y);
                    return true;
                }

            case Input.Keys.RIGHT:
                if (x + 1 < 0 || x + 1 >= props.get("width", Integer.class))
                    return false;
                else {
                    layer.setCell((int) position.x, (int) position.y, null);
                    mainRobot.setDirection(Direction.EAST);
                    position.set(x + 1, y);
                    return true;
                }

            case Input.Keys.UP:
                if (y + 1 < 0 || y + 1 >= props.get("height", Integer.class))
                    return false;
                else {
                    layer.setCell((int) position.x, (int) position.y, null);
                    mainRobot.setDirection(Direction.NORTH);
                    position.set(x, y + 1);
                    return true;
                }

            case Input.Keys.DOWN:
                if (y - 1 < 0 || y - 1 >= props.get("height", Integer.class))
                    return false;
                else {
                    layer.setCell((int) position.x, (int) position.y, null);
                    mainRobot.setDirection(Direction.SOUTH);
                    position.set(x, y - 1);
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
    private boolean positionIsFree(int x, int y) {
        // TODO: dont use for-loop here, find a more efficient way
        // perhaps storing player-layer id's in a global variable?
        for (int i = 0; i < robots.length; i++) {
            if (((TiledMapTileLayer) map.getLayers().get("player_" + i)).getCell(x, y) != null)
                return false;
        }
        return true;
    }

    /**
     * Check if a move is valid, called by the player class
     * !!Currently returns false if player actually can move, just not the distance intended to
     * @param x the desired x-position to move to
     * @param y the desired y-position to move to
     * @return true if move is possible, false otherwise
     */
    public boolean moveIsValid(int x, int y) {
        if (x >= 0 && x < props.get("width", Integer.class) && y >= 0 && y < props.get("height", Integer.class)) {
            // TODO: revert back to only this line when not debugging
            //return positionIsFree(x, y);

            if (positionIsFree(x, y)) {
                System.out.println("Success! Moving...");
                return true;
            }
        }
        System.out.println("Move is not valid!");
        return false;
    }

    public TiledMap getMap() {
        return map;
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
