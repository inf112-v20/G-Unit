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
 * Tests for checking that conveyor mechanics works
 * as expected for conveyors with rotations
 */
@RunWith(GdxTestRunner.class)
public class TurningConveyorTests {

    private Robot robot;
    private Board board;

    private Vector2 expressNorthEastPos = new Vector2(6, 6);
    private Vector2 expressSouthWestPos = new Vector2(9, 8);
    private Vector2 regularWestNorthPos = new Vector2(2, 0);
    private Vector2 regularEastSouthPos = new Vector2(6, 3);

    @Before
    public void initialise() {
        Game game = new Game(1, new TmxMapLoader().load("assets/conveyor_testboard.tmx"));
        this.robot = game.getRobots()[0];
        this.board = new Board(game);
        robot.setPosition(new Vector2(0,0));
        robot.setDirection(Direction.NORTH);
    }

    @Test
    public void testExpressNorthEastConveyor() {
        robot.setPosition(expressNorthEastPos.cpy());
        int x = (int) robot.getPositionX();
        robot.setDirection(Direction.EAST);
        board.conveyExpress();
        assertEquals(x+1, (int) robot.getPositionX());
    }

    @Test
    public void testExpressSouthWestConveyor() {
        robot.setPosition(expressSouthWestPos.cpy());
        int x = (int) robot.getPositionX();
        robot.setDirection(Direction.WEST);
        board.conveyExpress();
        assertEquals(x-1, (int) robot.getPositionX());
    }

    @Test
    public void testRegularWestNorthConveyor() {
        robot.setPosition(regularWestNorthPos.cpy());
        int y = (int) robot.getPositionY();
        robot.setDirection(Direction.NORTH);
        board.conveyRegular();
        assertEquals(y+1, (int) robot.getPositionY());
    }

    @Test
    public void testRegularEastSouthConveyor() {
        robot.setPosition(regularEastSouthPos.cpy());
        int y = (int) robot.getPositionY();
        robot.setDirection(Direction.SOUTH);
        board.conveyRegular();
        assertEquals(y-1, (int) robot.getPositionY());
    }
}
