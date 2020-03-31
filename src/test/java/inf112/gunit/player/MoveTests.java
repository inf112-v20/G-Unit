package inf112.gunit.player;

import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import inf112.gunit.main.GdxTestRunner;
import inf112.gunit.main.Main;
import inf112.gunit.screens.Game;
import inf112.gunit.screens.Menu;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * these test do not pass yet becuase, how the fuck do i load resources from asset folder?????
 *
 * Players start-direction is always north, see player constructor
 */
@RunWith(GdxTestRunner.class)
public class MoveTests {

    Game game;
    Robot robot;

    @Before
    public void initialise() {
        this.game = new Game(1, new TmxMapLoader().load("assets/board_new.tmx"));
        this.robot = game.getRobots()[0];
        robot.setPosition(new Vector2(0,0));
    }

    /**
     * Test if the player can move with distance = 1
     */
    @Test
    public void moveDistOne() {
        int y = (int) robot.getPositionY();
        robot.move(1);
        assertEquals(y+1, (int) robot.getPositionY());
    }

    /**
     * Test if the robot can move with distance = 2
     */
    @Test
    public void moveDistTwo() {
        int y = (int) robot.getPositionY();
        robot.move(2);
        assertEquals(y+2, (int) robot.getPositionY());
    }

    /**
     * Test that the robot cant move out of the map
     */
    @Test
    public void moveIllegalY() {
        int y = (int) robot.getPositionY();
        robot.rotate(true, 2); //rotate robot 180 degrees, to face south (position is 0,0)
        robot.move(1);
        assertEquals(y, (int) robot.getPositionY()); //check that distance is the same as when started
    }

}
