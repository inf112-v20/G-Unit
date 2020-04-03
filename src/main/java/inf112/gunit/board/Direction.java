package inf112.gunit.board;

/**
 * An enum used for storing directions of objects on the game board
 */
public enum Direction {
    NORTH,
    EAST,
    SOUTH,
    WEST,
    INVALID;

    /**
     * Safe way of getting the direction from a string
     *
     * @param str The string to be parsed as a direction
     * @return a direction enum
     */
    public static Direction lookup(String str) {
        try {
            return valueOf(str);
        }
        catch (IllegalArgumentException iae) {
            return INVALID;
        }
    }

    /**
     * Flips the direction. Ex: SOUTH returns NORTH, WEST returns EAST etc..
     *
     * @param dir The direction to flip
     * @return the opposite direction of the direction given as the argument
     */
    public static Direction flip(Direction dir) {
        switch (dir) {
            case NORTH:
                return SOUTH;
            case EAST:
                return WEST;
            case SOUTH:
                return NORTH;
            case WEST:
                return EAST;

            default:
                return INVALID;
        }
    }

    @Override
    public String toString() {
        switch (this) {
            case NORTH:
                return "North";
            case EAST:
                return "East";
            case SOUTH:
                return "South";
            case WEST:
                return "West";

            default:
                return "Invalid direction";
        }
    }
}

