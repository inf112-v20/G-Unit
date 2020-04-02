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
public class HoleTests {

    private Robot robot;
    private Board board;

    private Vector2 holePos = new Vector2(3,3);

    @Before
    public void initialise() {
        Game game = new Game(1, new TmxMapLoader().load("assets/board_new.tmx"));
        this.robot = game.getRobots()[0];
        this.board = new Board(game);
        robot.setPosition(new Vector2(0,0));
        robot.setDirection(Direction.NORTH);
    }


    @Test
    public void fallIntoHoleReturnsToBackup() {
        Vector2 backupPos = robot.getBackupMemory().cpy();
        robot.setPosition(holePos.cpy());
        board.holes();
        assertEquals((int) backupPos.x, (int) robot.getPositionX());
        assertEquals((int) backupPos.y, (int) robot.getPositionY());
    }

    @Test
    public void fallIntoHoleRemovesOneLifeToken() {
        int lifeTokensBefore = robot.getLifeTokens();
        robot.setPosition(holePos.cpy());
        board.holes();
        assertEquals(lifeTokensBefore - 1, robot.getLifeTokens());
    }

    @Test
    public void fallIntoHolesResetDamageMarkers() {
        robot.setPosition(holePos.cpy());
        board.holes();
        assertEquals(0, robot.getDamageMarkers());
    }
}
