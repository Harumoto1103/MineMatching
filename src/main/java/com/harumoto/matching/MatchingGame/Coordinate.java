package com.harumoto.matching.MatchingGame;

import java.util.Objects;

/**
 * The Coordinate class represents a coordinate in a 2D space with optional reversal.
 */
public class Coordinate {
    private Integer x; // The x-coordinate
    private Integer y; // The y-coordinate
    private Boolean isReversed; // Indicates if the coordinate is reversed

    /**
     * Constructs a Coordinate with the specified x and y values. The coordinate is not reversed by
     * default.
     * 
     * @param x the x-coordinate
     * @param y the y-coordinate
     */
    public Coordinate(Integer x, Integer y) {
        this.x = x;
        this.y = y;
        this.isReversed = false;
    }

    /**
     * Constructs a Coordinate with the specified x, y values, and reversal state.
     * 
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @param isReversed the reversal state of the coordinate
     */
    Coordinate(Integer x, Integer y, Boolean isReversed) {
        this.x = x;
        this.y = y;
        this.isReversed = isReversed;
    }

    /**
     * Returns the x-coordinate. If the coordinate is reversed, returns the y value.
     * 
     * @return the x-coordinate or y-coordinate if reversed
     */
    public Integer x() {
        return this.isReversed ? this.y : this.x;
    }

    /**
     * Returns the y-coordinate. If the coordinate is reversed, returns the x value.
     * 
     * @return the y-coordinate or x-coordinate if reversed
     */
    public Integer y() {
        return this.isReversed ? this.x : this.y;
    }

    /**
     * Checks if this coordinate is equal to another object.
     * 
     * @param obj the object to compare with
     * @return true if the coordinates are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) { // Check if the objects are the same instance
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) { // Check if the object is null or of a
                                                           // different class
            return false;
        }
        Coordinate other = (Coordinate) obj; // Cast the object to Coordinate
        // Compare the coordinates taking reversal into account
        return Objects.equals(this.x, other.x()) && Objects.equals(this.y, other.y());
    }

    /**
     * Returns a hash code for this coordinate.
     * 
     * @return the hash code of the coordinate
     */
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
