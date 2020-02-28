package inf112.gunit.player.card;

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
    public RotationCard(int priority, int rotations, boolean clockwise) {
        super(CardType.ROTATION, priority);
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
}
