package inf112.gunit.card;

import inf112.gunit.player.card.MovementCard;
import inf112.gunit.player.card.RotationCard;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Random;

public class CardTests {

    private final Random r = new Random();

    /**
     * Test to check that the getDistance() method returns correct value
     */
    @Test
    public void testMovementDistanceReturnsSameValue() {
        for (int i = 0; i < 10; i++) { //run the test 10 times
            int distance = r.nextInt(9) + 1; //pick a value between 1 and 10
            MovementCard card = new MovementCard(distance);
            assertEquals(distance, card.getDistance());
        }
    }

    /**
     * Test to check that the getRotations() method returns correct value
     */
    @Test
    public void testRotationNumberReturnsSameValue() {
        for (int i = 0; i < 10; i++) {
            int rotation = r.nextInt(2);
            RotationCard card = new RotationCard(rotation, true);
            assertEquals(rotation, card.getRotations());
        }
    }
}
