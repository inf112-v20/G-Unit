package inf112.gunit.player;

import com.badlogic.gdx.math.Vector2;
import inf112.gunit.board.Direction;
import inf112.gunit.main.GdxTestRunner;
import inf112.gunit.player.card.MovementCard;
import inf112.gunit.player.card.ProgramCard;
import inf112.gunit.player.card.RotationCard;
import inf112.gunit.screens.Game;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * Test for executing program cards on a Robot
 */
@RunWith(GdxTestRunner.class)
public class ExecuteProgramCardsTests {
    Game game;
    Robot robot;

    @Before
    public void initialise() {
        this.game = new Game(1);
        this.robot = game.getRobots()[0];
        robot.setPosition(new Vector2(0,0));
        robot.setDirection(Direction.NORTH);
    }

    /**
     * Test that the robot can do a turn
     * with a movement card with a value of 1
     */
    @Test
    public void testMovementCardMoveOne() {
        int y = (int) robot.getPositionY();
        ProgramCard card = new MovementCard(100, 1);
        robot.doTurn(card);
        assertEquals(y+1, (int) robot.getPositionY());
    }

    /**
     * Test that the robot can do a turn
     * with a movement card with a value of 2
     */
    @Test
    public void testMovementCardMoveTwo() {
        int y = (int) robot.getPositionY();
        ProgramCard card = new MovementCard(100, 2);
        robot.doTurn(card);
        assertEquals(y+2, (int) robot.getPositionY());
    }

    /**
     * Test that the robot can do a turn
     * with a movement card with a value of 5
     * (There wont be a program card with value of 5 in the game)
     */
    @Test
    public void testMovementCardMoveFive() {
        int y = (int) robot.getPositionY();
        ProgramCard card = new MovementCard(100, 5);
        robot.doTurn(card);
        assertEquals(y+5, (int) robot.getPositionY());
    }

    /**
     * Test that the robot can perform a turn
     * with a 90 degree clockwise rotation with
     * the corresponding ProgramCard
     */
    @Test
    public void testRotationCardClockwiseOne() {
        ProgramCard card = new RotationCard(100, 1, true);
        robot.doTurn(card);
        assertEquals(Direction.EAST, robot.getDirection());
    }

    /**
     * Test that the robot can perform a turn
     * with a 90 degree counter-clockwise rotation with
     * the corresponding ProgramCard
     */
    @Test
    public void testRotationCardCounterClockwiseOne() {
        ProgramCard card = new RotationCard(100, 1, false);
        robot.doTurn(card);
        assertEquals(Direction.WEST, robot.getDirection());
    }

    /**
     * Test that the robot can perform a turn
     * with a 180 degree clockwise rotation with
     * the corresponding ProgramCard
     */
    @Test
    public void testRotationCardUTurn() {
        ProgramCard card = new RotationCard(100, 2, true);
        robot.doTurn(card);
        assertEquals(Direction.SOUTH, robot.getDirection());
    }


}
