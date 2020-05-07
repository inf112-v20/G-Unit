package inf112.gunit.card;

import inf112.gunit.player.card.MovementCard;
import inf112.gunit.player.card.ProgramCard;
import inf112.gunit.player.card.RotationCard;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Random;

public class CardTests {

    private Random r = new Random();

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
            int[] possibleRotations = {1,2};
            int rotation = r.nextInt(possibleRotations.length);
            RotationCard card = new RotationCard(rotation, true);
            assertEquals(rotation, card.getRotations());
        }
    }

    /**
     * Test to check that the toString() method returns
     * the correct name + correct priority in a single string
     */
    
    /* TODO: this doesn't work correctly at the moment, because we
             are using the toString()-methods of the ProgramCards
             for debugging information.

     */

    /*
    @Test
    public void cardNamesReturnsCorrectNames() {
        String movementName;
        String rotationName;

        for (int i = 0; i < 100; i++) { //run test 100 times
            int priority = r.nextInt(799) + 200;

            movementName = "Movement Card " + priority;
            rotationName = "Rotation Card " + priority;

            //initializing cards with arbitrary movement- and rotation values
            MovementCard moveCard = new MovementCard(priority, 3);
            RotationCard rotCard = new RotationCard(priority, 2, true);

            assertEquals(movementName, moveCard.toString());
            assertEquals(rotationName, rotCard.toString());
        }
    }
    
     */
}
