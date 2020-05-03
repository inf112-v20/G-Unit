package inf112.gunit.player;

import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector;
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

    private Robot robot;

    @Before
    public void initialise() {
        Game game = new Game(1, new TmxMapLoader().load("assets/board_new.tmx"));
        this.robot = game.getRobots()[0];
    }

    /**
     * Test if the player can move with distance = 1
     */
    @Test
    public void moveDistOne() {
        robot.setPosition(new Vector2(0,0));
        int y = (int) robot.getPositionY();
        robot.move(1);
        assertEquals(y+1, (int) robot.getPositionY());
    }

    /**
     * Test if the robot can move with distance = 2
     */
    @Test
    public void moveDistTwo() {
        robot.setPosition(new Vector2(0,3));
        int y = (int) robot.getPositionY();
        robot.move(2);
        assertEquals(y+2, (int) robot.getPositionY());
    }
    
    @Test
    public void moveDistThree() {
        robot.setPosition(new Vector2(0, 3));
        int y = (int) robot.getPositionY();
        robot.move(3);
        assertEquals(y + 3, (int) robot.getPositionY());
    }

    /**
     * Test that the robot cant move out of the map
     */
    @Test
    public void moveIllegalY() {
        robot.setPosition(new Vector2(0,0));
        int y = (int) robot.getPositionY();
        robot.rotate(true, 2); //rotate robot 180 degrees, to face south (position is 0,0)
        robot.move(1);
        assertEquals(y, (int) robot.getPositionY()); //check that distance is the same as when started
    }

    @Test
    public void moveIllegalFarWallFromSouth() {
        robot.setPosition(new Vector2(1, 0));
        int y = (int) robot.getPositionY();
        robot.move(1);
        assertEquals(y, (int) robot.getPositionY());
    }

    @Test
    public void moveIllegalFarWallFromNorth() {
        robot.setPosition(new Vector2(1, 9));
        int y = (int) robot.getPositionY();
        robot.rotate(true, 2);
        robot.move(1);
        assertEquals(y, (int) robot.getPositionY());
        
        robot.setPosition(new Vector2(1, 9));
        y = (int) robot.getPositionY();
        robot.rotate(true, 2);
        robot.move(2);
        assertEquals(y, (int) robot.getPositionY());

        robot.setPosition(new Vector2(1, 9));
        y = (int) robot.getPositionY();
        robot.rotate(true, 2);
        robot.move(3);
        assertEquals(y, (int) robot.getPositionY());
    }

    @Test
    public void moveIllegalCloseWallFromSouth() {
        robot.setPosition(new Vector2(1, 8));
        int y = (int) robot.getPositionY();
        robot.move(1);
        assertEquals(y, (int) robot.getPositionY());
    }

    @Test
    public void moveIllegalCloseWallFromWest() {
        robot.setPosition(new Vector2(2, 4));
        int x = (int) robot.getPositionX();
        robot.rotate(true, 1);
        robot.move(1);
        assertEquals(x, (int) robot.getPositionX());
    }

    @Test
    public void moveIllegalFarWallFromEast() {
        robot.setPosition(new Vector2(3, 4));
        int x = (int) robot.getPositionX();
        robot.rotate(false, 1);
        robot.move(1);
        assertEquals(x, (int) robot.getPositionX());
    }

    @Test
    public void moveLegalOrthogonalWallFromEast() {
        robot.setPosition(new Vector2(0, 1));
        int x = (int) robot.getPositionX();
        robot.rotate(true, 1);
        robot.move(1);
        assertEquals(x + 1, (int) robot.getPositionX());
    }
}
