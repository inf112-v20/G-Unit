package inf112.gunit.player.card;

import javax.smartcardio.Card;

public abstract class ProgramCard implements Comparable<ProgramCard>{

    private CardType type;
    private int priority;
    protected String cardName;

    public ProgramCard(CardType type, int priority) {
        this.type = type;
        this.priority = priority;
    }

    public CardType getType() {
        return type;
    }

    public int getPriority() {
        return priority;
    }

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
