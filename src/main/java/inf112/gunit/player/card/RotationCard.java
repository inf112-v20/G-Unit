package inf112.gunit.player.card;

public class RotationCard extends ProgramCard {

    private int rotations;
    private boolean clockwise;

    public RotationCard(int priority, int rotations, boolean clockwise) {
        super(CardType.ROTATION, priority);
        this.rotations = rotations;
        this.cardName = "Rotation Card";
        this.clockwise = clockwise;
    }

    public int getRotations() {
        return rotations;
    }

    public boolean isClockwise() {
        return clockwise;
    }
}
