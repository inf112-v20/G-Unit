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

    public static Direction lookup(String str) {
        try {
            return valueOf(str);
        }
        catch (IllegalArgumentException iae) {
            return INVALID;
        }
    }

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

