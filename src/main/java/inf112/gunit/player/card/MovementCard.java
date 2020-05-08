package inf112.gunit.player.card;

import java.util.Random;

/**
 * MovementCard is a ProgramCard that contains a distance
 * and is used to move a player the given distance
 */
public class MovementCard extends ProgramCard {

    private final int distance;

    /**
     * The MovementCard constructor
     *
     * @param distance the amount of distance
     */
    public MovementCard(int distance) {
        super(CardType.MOVEMENT);
        this.distance = distance;
        this.cardName = "Movement Card";
        this.setPriority();
    }

    /**
     * Get the cards distance
     * @return the cards distance property
     */
    public int getDistance() {
        return distance;
    }

    @Override
    void setPriority() {
        Random r = new Random();
        switch (distance) {
            case 1:
                this.priority = (r.nextInt(16) + 49) * 10;
                break;
            case 2:
                this.priority = (r.nextInt(11) + 67) * 10;
                break;
            case 3:
                this.priority = (r.nextInt(5) + 79) * 10;
                break;
            default:
                this.priority = 500;
                break;
        }
    }
}
