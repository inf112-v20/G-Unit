package inf112.gunit.hud;

import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import inf112.gunit.board.Board;
import inf112.gunit.board.Direction;
import inf112.gunit.main.GdxTestRunner;
import inf112.gunit.player.Robot;
import inf112.gunit.player.card.ProgramCard;
import inf112.gunit.screens.Game;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Random;

import static org.junit.Assert.assertEquals;

@RunWith(GdxTestRunner.class)
public class PowerDownTests {

    private final Random r = new Random();

    private Game game;
    private Robot robot;
    private Board board;

    @Before
    public void initialise() {
        game = new Game(1, new TmxMapLoader().load("assets/conveyor_testboard.tmx"));
        this.robot = game.getPlayerRobot();
        this.board = new Board(game);
        robot.setPosition(new Vector2(0,0));
        robot.setDirection(Direction.NORTH);
    }

    @Test
    public void poweringDownSetsBooleanAccordingly() {
        boolean before = robot.getPowerDownDesire();
        robot.updatePowerDownDesire();
        assertEquals(before, !robot.getPowerDownDesire());

        before = robot.getPowerDownDesire();
        robot.updatePowerDownDesire();
        assertEquals(before, !robot.getPowerDownDesire());
    }

    @Test
    public void poweredDownRobotDoesntMove() {
        robot.setPoweredDown(true);
        Vector2 posBefore = robot.getPosition().cpy();
        Direction dirBefore = robot.getDirection();
        ProgramCard card = robot.getProgram()[0];
        robot.doTurn(card);

        assertEquals(posBefore, robot.getPosition());
        assertEquals(dirBefore, robot.getDirection());
    }

    @Test
    public void robotClearsDamageTokensWhenPowerDown() {
        int n = r.nextInt(9) + 1;
        robot.handleDamage(n);
        assertEquals(n, robot.getDamageMarkers());
        robot.setPoweredDown(true);
        assertEquals(0, robot.getDamageMarkers());
    }

    @Test
    public void conveyorMovesPoweredDownRobot() {
        robot.setPosition(new Vector2(0,3));
        robot.setDirection(Direction.EAST);
        robot.setPoweredDown(true);

        board.conveyExpress();
        board.conveyRegular();

        assertEquals(new Vector2(2,3), robot.getPosition());
        assertEquals(Direction.NORTH, robot.getDirection());
    }

    @Test
    public void gearRotatesPoweredDownRobot() {
        Game newGame = new Game(1, new TmxMapLoader().load("assets/board_new.tmx"));
        Board newBoard = new Board(newGame);
        Robot newRobot = newGame.getPlayerRobot();

        newRobot.setPosition(new Vector2(1,6));
        Direction before = newRobot.getDirection();

        newBoard.rotateGears();
        newBoard.rotateGears();

        assertEquals(Direction.flip(before), newRobot.getDirection());
    }

}
