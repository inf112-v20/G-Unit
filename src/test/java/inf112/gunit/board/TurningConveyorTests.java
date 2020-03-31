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

@RunWith(GdxTestRunner.class)
public class TurningConveyorTests {

    Game game;
    Robot robot;
    Board board;

    @Before
    public void initialise() {
        this.game = new Game(1, new TmxMapLoader().load("assets/conveyor_testboard.tmx"));
        this.robot = game.getRobots()[0];
        this.board = new Board(game);
        robot.setPosition(new Vector2(0,0));
        robot.setDirection(Direction.NORTH);
    }

    @Test
    public void testRegularWestSouthConveyor() {
        robot.setPosition(new Vector2(0, 9));
        int y = (int) robot.getPositionY();
        robot.setDirection(Direction.SOUTH);
        board.conveyRegular();
        assertEquals(y-1, (int) robot.getPositionY());
    }
}
