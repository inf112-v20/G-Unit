package inf112.gunit.player.card;

public class RotationCard extends ProgramCard {

    private int rotations;

    public RotationCard(int priority, int rotations) {
        super(priority);
        this.rotations = rotations;
        this.cardName = "Rotation Card";
    }

    public int getRotations() {
        return rotations;
    }
}
