package inf112.gunit.player.card;

/**
 * MovementCard is a ProgramCard that contains a distance
 * and is used to move a player the given distance
 */
public class MovementCard extends ProgramCard {

    private int distance;

    /**
     * The MovementCard constructor
     * @param priority the given priority
     * @param distance the amount of distance
     */
    public MovementCard(int priority, int distance) {
        super(CardType.MOVEMENT, priority);
        this.distance = distance;
        this.cardName = "Movement Card";
    }

    /**
     * Get the cards distance
     * @return the cards distance property
     */
    public int getDistance() {
        return distance;
    }

}
