package inf112.gunit.player.card;

import java.util.Random;

/**
 * RotationCard is a ProgramCard that contains
 * the number of 90-degree rotations a player can
 * perform given the card. And whether the rotations
 * should be executed clockwise or counter-clockwise.
 */
public class RotationCard extends ProgramCard {

    private int rotations;
    private boolean clockwise;

    /**
     * The RotationCard constructor
     * @param priority the given priority
     * @param rotations number of rotations
     * @param clockwise true if rotations are clockwise, false if counter-clockwise
     */
    public RotationCard(int rotations, boolean clockwise) {
        super(CardType.ROTATION);
        this.rotations = rotations;
        this.cardName = "Rotation Card";
        this.clockwise = clockwise;
    }

    /**
     * Get number of 90-degree rotations
     * @return the amount of 90-degree rotations
     */
    public int getRotations() {
        return rotations;
    }

    /**
     * Get clockwise boolean
     * @return true if rotations are clockwise, false if counter-clockwise
     */
    public boolean isClockwise() {
        return clockwise;
    }

    @Override
    void setPriority() {
        Random r = new Random();

        if (clockwise) {
            if (rotations == 1) {
                this.priority = (r.nextInt(34) + 8) * 10;
            } else {
                this.priority = (r.nextInt(5) + 1) * 10;
            }
        } else {
            this.priority = (r.nextInt(34) + 7) * 10;
        }
    }

    @Override
    public String toString() {
        if (rotations > 1) {
            return "180 turn";
        }
        
        String dir = "";
        if (isClockwise())
            dir = "right";
        else
            dir = "left";
        
        return "90 turn to the " + dir;
    }
}
