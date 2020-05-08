package inf112.gunit.player.card;

/**
 * ProgramCard is an abstract superclass for the different types of ProgramCards
 */
public abstract class ProgramCard implements Comparable<ProgramCard>{

    // store the type of card, priority, and the name of the card
    private final CardType type;
    protected int priority;
    protected String cardName;

    /**
     * The ProgramCard constructor
     * @param type the type of the initialised card
     */
    public ProgramCard(CardType type) {
        this.type = type;
    }

    /**
     * Get the CardType of the card
     * @return the corresponding CardType
     */
    public CardType getType() {
        return type;
    }

    /**
     * Set priority according to CardType and distance/rotation values.
     */
    abstract void setPriority();

    /**
     * Get the priority of the card
     * @return the cards priority
     */
    public int getPriority() {
        return priority;
    }

    /**
     * Overridden compareTo()-method used to determine
     * which card has the biggest priority
     * @param otherCard the card you are comparing
     * @return 1 if this card has bigger priority than the other card, 0 if equal, -1 otherwise
     */
    @Override
    public int compareTo(ProgramCard otherCard) {
        if (this.priority > otherCard.getPriority()) {
            return 1;
        } else if (this.priority == otherCard.getPriority()) {
            return 0;
        } else {
            return -1;
        }
    }

    @Override
    public String toString() {
        return cardName + " " + priority;
    }
}
