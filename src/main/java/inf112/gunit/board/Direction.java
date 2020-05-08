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
     * Calculate degrees corresponding to a given Direction
     * @param dir the Direction to calcualte
     * @return amount in degrees
     */
    public static int toDegrees(Direction dir) {
        if (dir == Direction.NORTH) {
            return 0;
        } else if (dir == Direction.EAST) {
            return 270;
        } else if (dir == Direction.SOUTH) {
            return 180;
        } else if (dir == Direction.WEST) {
            return 90;
        }
        return 0;
    }

    /**
     * Calculates the difference in degrees between two directions.
     * Used for animation purposes.
     *
     * @param from a direction
     * @param to another direction
     * @return the difference in degrees between the two directions
     */
    public static int calcDegDiff(Direction from, Direction to) {
        int degrees = 0;

        while (!from.equals(to)) {
            from = getClockwiseDirection(from);
            degrees += 90;
        }

        if (degrees == 270) return -90;
        else return degrees;
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

    /**
     * Gets the direction which is clockwise to the direction given as the argument.
     *
     * @param dir a direction
     * @return the clockwise direction of the given direction
     */
    public static Direction getClockwiseDirection(Direction dir) {
        if (dir == Direction.NORTH)
            return Direction.EAST;
        else if (dir == Direction.EAST)
            return Direction.SOUTH;
        else if (dir == Direction.SOUTH)
            return Direction.WEST;
        else if (dir == Direction.WEST)
            return Direction.NORTH;
        
        return Direction.INVALID;
    }

    /**
     * Gets the direction which is anti clockwise to the direction given as the argument.
     *
     * @param dir a direction
     * @return the anti clockwise direction of the given direction
     */
    public static Direction getAntiClockwiseDirection(Direction dir) {
        if (dir == Direction.NORTH)
            return Direction.WEST;
        else if (dir == Direction.EAST)
            return Direction.NORTH;
        else if (dir == Direction.SOUTH)
            return Direction.EAST;
        else if (dir == Direction.WEST)
            return Direction.SOUTH;

        return Direction.INVALID;
    }

    /**
     * Converts a Direction to a string.
     *
     * @return a string representation of the direction
     */
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
