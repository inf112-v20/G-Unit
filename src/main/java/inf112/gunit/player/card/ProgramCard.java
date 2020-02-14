package inf112.gunit.player.card;

public class ProgramCard implements Comparable<ProgramCard>{

    private int priority;
    protected String cardName;

    public ProgramCard(int priority) {
        this.priority = priority;
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
