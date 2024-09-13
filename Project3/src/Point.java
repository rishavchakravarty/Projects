// On my honor:
// - I have not used source code obtained from another student,
// or any other unauthorized source, either modified or unmodified.
// - All source code and documentation used in my program is
// either my original work, or was derived by me from the
// source code published in the textbook for this course.
// - I have not discussed coding details about this project with
// anyone other than the my partner, instructor, ACM/UPE tutors
// or the TAs assigned to this course.
// I understand that I may discuss the concepts
// of this program with other students, and that another student
// may help me debug my program so long as neither of us writes
// anything during the discussion or modifies any computer file
// during the discussion. I have violated neither the spirit nor
// letter of this restriction.

/**
 * Represents a point in a two-dimensional space with a name identifier. This
 * class is immutable, meaning once an instance is created, its state cannot be
 * changed.
 * 
 * @author kinjalpandey, architg03, rishavc18
 * @version 02/25/24
 */
public class Point {
    /**
     * The name identifier for the point.
     */
    private final String name;

    /**
     * The x-coordinate of the point.
     */
    private final int x;

    /**
     * The y-coordinate of the point.
     */
    private final int y;

    /**
     * Constructs a new Point with the specified name, x-coordinate, and
     * y-coordinate.
     *
     * @param name
     *            The name identifier for the point.
     * @param x
     *            The x-coordinate of the point.
     * @param y
     *            The y-coordinate of the point.
     */
    public Point(String name, int x, int y) {
        this.name = name;
        this.x = x;
        this.y = y;
        // System.out.println("Creating Point: " + this.toString());
    }


    /**
     * Returns the name of the point.
     *
     * @return The name identifier of the point.
     */
    public String getName() {
        return name;
    }


    /**
     * Returns the x-coordinate of the point.
     *
     * @return The x-coordinate.
     */
    public int getX() {
        return x;
    }


    /**
     * Returns the y-coordinate of the point.
     *
     * @return The y-coordinate.
     */
    public int getY() {
        return y;
    }


    /**
     * Returns a string representation of the point in the format "name (x, y)".
     *
     * @return The string representation of the point.
     */
    @Override
    public String toString() {
        return name + ", " + x + ", " + y;
    }


    /**
     * Returns whether the point is valid or not
     * 
     * @return true if the point is valid, false otherwise
     */
    public boolean isValid() {
        if (x < 0 || y < 0) {
            return false;
        }
        else {
            return true;
        }
    }


    /**
     * Returns whether the point is valid or not
     * 
     * @return true if the point is valid, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        Point point = (Point)obj;
        return name.equals(point.name) && x == point.x && y == point.y;
    }


    /**
     * Returns whether the point is valid or not
     * 
     * @return true if the point is valid, false otherwise
     * @param xCoord
     *            The x-coordinate to compare.
     * @param yCoord
     *            The y-coordinate to compare.
     */
    public boolean coordEquals(int xCoord, int yCoord) {
        return this.x == xCoord && this.y == yCoord;
    }
}
