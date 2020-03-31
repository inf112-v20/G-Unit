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
import static org.junit.Assert.assertTrue;

@RunWith(GdxTestRunner.class)
public class FlagTests {

    Game game;
    Robot robot;
    Board board;

    @Before
    public void initialise() {
        this.game = new Game(1, new TmxMapLoader().load("assets/board_new.tmx"));
        this.robot = game.getRobots()[0];
        this.board = new Board(game);
        robot.setPosition(new Vector2(0,0));
        robot.setDirection(Direction.NORTH);
    }

    /**
     * Check that robot can pick up first flag
     */
    @Test
    public void collectOneFlagUpdatesRobotNumOfFlags() {
        robot.setPosition(board.getFlagPositions().get(0).cpy());
        board.flags();
        assertEquals(1, robot.getFlagsCollected());
    }

    /**
     * Check that robot cant pick up second flag when
     * first flag is not picked up
     */
    @Test
    public void collectWrongFlagDoesntWork() {
        robot.setPosition(board.getFlagPositions().get(1).cpy());
        board.flags();
        assertEquals(0, robot.getFlagsCollected());
    }

    /**
     * Check that collecting all flags triggers gameOver
     */
    @Test
    public void collectFlagsInOrder() {
        robot.setPosition(board.getFlagPositions().get(0).cpy());
        board.flags();
        robot.setPosition(board.getFlagPositions().get(1).cpy());
        board.flags();
        robot.setPosition(board.getFlagPositions().get(2).cpy());
        board.flags();
        robot.setPosition(board.getFlagPositions().get(3).cpy());
        board.flags();
        assertTrue(game.getGameOver());
    }
}
