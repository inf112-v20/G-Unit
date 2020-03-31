package inf112.gunit.board;

import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import inf112.gunit.main.GdxTestRunner;
import inf112.gunit.player.Robot;
import inf112.gunit.screens.Game;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * Tests for checking that conveyor mechanics works as expected
 */
// TODO: check that tile matches desired conveyor
@RunWith(GdxTestRunner.class)
public class LinearConveyorTests {

    Game game;
    Robot robot;
    Board board;

    // setting the positions for the conveyors according to the tiled-map
    Vector2 expressNorthPos = new Vector2(8, 6);
    Vector2 regularNorthPos = new Vector2(0, 1);

    Vector2 expressEastPos = new Vector2(1, 3);
    Vector2 regularEastPos = new Vector2(2, 6);

    Vector2 expressSouthPos = new Vector2(4, 4);
    Vector2 regularSouthPos = new Vector2(3, 6);

    Vector2 expressWestPos = new Vector2(5, 7);
    Vector2 regularWestPos = new Vector2(6, 7);

    @Before
    public void initialise() {
        this.game = new Game(1, new TmxMapLoader().load("assets/conveyor_testboard.tmx"));
        this.robot = game.getRobots()[0];
        this.board = new Board(game);
        robot.setPosition(new Vector2(0,0));
        robot.setDirection(Direction.NORTH);
    }

    /**
     * Test that regular conveyor with direction north
     * moves player accordingly
     */
    @Test
    public void testRegularConveyorNorthMovesRobot() {
        robot.setPosition(regularNorthPos.cpy());
        int y = (int) robot.getPositionY();
        board.conveyRegular();
        assertEquals(y+1, (int) robot.getPositionY());
    }

    /**
     * Test that regular conveyor with direction east
     * moves player accordingly
     */
    @Test
    public void testRegularConveyorEastMovesRobot() {
        robot.setPosition(regularEastPos.cpy());
        int x = (int) robot.getPositionX();
        board.conveyRegular();
        assertEquals(x+1, (int) robot.getPositionX());
    }

    /**
     * Test that regular conveyor with direction south
     * moves player accordingly
     */
    @Test
    public void testRegularConveyorSouthMovesRobot() {
        robot.setPosition(regularSouthPos.cpy());
        int y = (int) robot.getPositionY();
        board.conveyRegular();
        assertEquals(y-1, (int) robot.getPositionY());
    }

    /**
     * Test that regular conveyor with direction west
     * moves player accordingly
     */
    @Test
    public void testRegularConveyorWestMovesRobot() {
        robot.setPosition(regularWestPos.cpy());
        int x = (int) robot.getPositionX();
        board.conveyRegular();
        assertEquals(x-1, (int) robot.getPositionX());
    }

    /**
     * Test that express conveyor with direction north
     * moves player accordingly
     */
    @Test
    public void testExpressConveyorNorthMovesRobot() {
        robot.setPosition(expressNorthPos.cpy());
        int y = (int) robot.getPositionY();
        board.conveyExpress();
        assertEquals(y+1, (int) robot.getPositionY());
    }

    /**
     * Test that express conveyor with direction east
     * moves player accordingly
     */
    @Test
    public void testExpressConveyorEastMovesRobot() {
        robot.setPosition(expressEastPos.cpy());
        int x = (int) robot.getPositionX();
        board.conveyExpress();
        assertEquals(x+1, (int) robot.getPositionX());
    }

    /**
     * Test that express conveyor with direction south
     * moves player accordingly
     */
    @Test
    public void testExpressConveyorSouthMovesRobot() {
        robot.setPosition(expressSouthPos.cpy());
        int y = (int) robot.getPositionY();
        board.conveyExpress();
        assertEquals(y-1, (int) robot.getPositionY());
    }

    /**
     * Test that express conveyor with direction west
     * moves player accordingly
     */
    @Test
    public void testExpressConveyorWestMovesRobot() {
        robot.setPosition(expressWestPos.cpy());
        int x = (int) robot.getPositionX();
        board.conveyExpress();
        assertEquals(x-1, (int) robot.getPositionX());
    }
}
