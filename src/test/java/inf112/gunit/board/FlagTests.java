package inf112.gunit.board;

import com.badlogic.gdx.math.Vector2;
import inf112.gunit.main.GdxTestRunner;
import inf112.gunit.player.Robot;
import inf112.gunit.screens.Game;
import org.junit.Before;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class FlagTests {

    Game game;
    Robot robot;
    Board board;

    Vector2 flag1Pos = new Vector2(0,5);
    Vector2 flag2Pos = new Vector2(3,3);
    Vector2 flag3Pos = new Vector2(3,3);
    Vector2 flag4Pos = new Vector2(3,3);

    @Before
    public void initialise() {
        this.game = new Game(1);
        this.robot = game.getRobots()[0];
        this.board = new Board(game);
        robot.setPosition(new Vector2(0,0));
        robot.setDirection(Direction.NORTH);
    }
}
