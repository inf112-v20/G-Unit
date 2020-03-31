package inf112.gunit.player;

import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import inf112.gunit.board.Direction;
import inf112.gunit.main.GdxTestRunner;
import inf112.gunit.screens.Game;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * Tests for rotation of the Robot
 */
@RunWith(GdxTestRunner.class)
public class RotationTests {

    Game game;
    Robot robot;

    @Before
    public void initialise() {
        this.game = new Game(1, new TmxMapLoader().load("assets/board_new.tmx"));
        this.robot = game.getRobots()[0];
        robot.setPosition(new Vector2(0,0));
        robot.setDirection(Direction.NORTH);
    }
    
    /**
     * Rotate 90 degrees clockwise
     */
    @Test
    public void testPlayerRotateClockwiseOnce() {
        robot.rotate(true, 1);
        assertEquals(Direction.EAST, robot.getDirection());
    }


    /**
     * Rotate 180 degrees clockwise
     */
    @Test
    public void testPlayerRotateClockwiseTwice() {
        robot.rotate(true, 2);
        assertEquals(Direction.SOUTH, robot.getDirection());
    }

    /**
     * Rotate 90 degrees counter clockwise
     */
    @Test
    public void testPlayerRotateCounterClockwiseOnce() {
        robot.rotate(false, 1);
        assertEquals(Direction.WEST, robot.getDirection());
    }

    /**
     * Rotate 180 degrees counter clockwise
     */
    @Test
    public void testPlayerRotateCounterClockwiseTwice() {
        robot.rotate(false, 2);
        assertEquals(Direction.SOUTH, robot.getDirection());
    }

}
