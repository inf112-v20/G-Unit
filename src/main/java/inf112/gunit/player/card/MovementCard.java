package inf112.gunit.player.card;

public class MovementCard extends ProgramCard {

    private int distance;

    public MovementCard(int priority, int distance) {
        super(priority);
        this.distance = distance;
        this.cardName = "Movement Card";
    }

    public int getDistance() {
        return distance;
    }

}
