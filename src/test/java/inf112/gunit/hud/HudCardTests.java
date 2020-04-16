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

import java.util.ArrayList;
import java.util.Random;

import static org.junit.Assert.assertEquals;

@RunWith(GdxTestRunner.class)
public class HudCardTests {

    private final Random r = new Random();

    private Robot robot;
    private Board board;

    @Before
    public void initialise() {
        Game game = new Game(1, new TmxMapLoader().load("assets/board_new.tmx"));
        this.robot = game.getPlayerRobot();
        this.board = new Board(game);
        robot.setPosition(new Vector2(0,0));
        robot.setDirection(Direction.NORTH);
    }

    @Test
    public void dealCardsAppropriateToDamageTokens() {
        int n = r.nextInt(4) + 1;
        robot.handleDamage(n);
        robot.dealCards();

        assertEquals(5 + (4 - n), robot.getCardDeck().size());
    }

    @Test
    public void pickedCardsCorrespondToRobotProgram() {
        robot.dealCards();
        ArrayList<ProgramCard> buffer = robot.getCardDeck();
        ArrayList<ProgramCard> pickedCards = new ArrayList<>();

        //pick a random card from the buffer (excluding already picked cards)
        for (int i = 0; i < 5; i++) {
            pickedCards.add(buffer.get(r.nextInt(9 - i) + i));
        }

        //add pickedcards to robots programbuffer
        for (ProgramCard card : pickedCards) {
            robot.addBufferCard(card);
        }

        //set actual program according to buffer
        robot.setProgram(robot.getProgramBuffer().toArray(new ProgramCard[5]));

        ProgramCard[] picked = pickedCards.toArray(new ProgramCard[5]);
        ProgramCard[] actual = robot.getProgram();

        //compare length
        assertEquals(picked.length, actual.length);
        for (int i = 0; i < picked.length; i++) {
            assertEquals(picked[i], actual[i]); //compare each card
        }
    }

}
