import java.util.ArrayList;

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
 * Represents a LeafNode in a PR QuadTree. This class is responsible for
 * managing the structure of the LeafNode and the operations that can be
 * performed on it.
 * 
 * @author kinjalpandey, architg03, rishavc18
 * @version 02/25/24
 */
public class LeafNode extends QuadTreeNode {
    // private static final int MAX_POINTS = 1;
    private ArrayList<Point> points = new ArrayList<>();

    /**
     * Gets all points stored in this leaf node.
     * 
     * @return A list of all points.
     */
    public ArrayList<Point> getPoints() {
        return new ArrayList<>(points);
    }


    /**
     * Inserts a point into the QuadTree. If the node is full, it creates a new
     * InternalNode and reinserts all points.
     * 
     * @param point
     *            The point to insert.
     * @param x
     *            The x-coordinate of the node.
     * @param y
     *            The y-coordinate of the node.
     * @param size
     *            The size of the node.
     * @return The node after insertion.
     */
    @Override
    public QuadTreeNode insert(Point point, int x, int y, int size) {
        // Check for duplicate point at the same location (not allowed by
        // project spec)
        for (Point existingPoint : points) {
            if (existingPoint.equals(point)) {
                return this; // Do not insert duplicates
            }
        }

        points.add(point);

        if (points.size() > 3 && !allPointsHaveSameCoordinates(points)) {
            InternalNode internalNode = new InternalNode();
            for (Point existingPoint : points) {
                internalNode.insert(existingPoint, x, y, size);
            }
            return internalNode;
        }
        return this;
    }


    private boolean allPointsHaveSameCoordinates(ArrayList<Point> points1) {
        Point first = points1.get(0);
        return points1.stream().allMatch(p -> p.getX() == first.getX() && p
            .getY() == first.getY());
    }


    /**
     * Removes a point from the QuadTree.
     * 
     * @param point
     *            The point to remove.
     * @param x
     *            The x-coordinate of the node.
     * @param y
     *            The y-coordinate of the node.
     * @param size
     *            The size of the node.
     * @return true if the point was removed, false otherwise.
     */
    @Override
    public QuadTreeNode remove(Point point, int x, int y, int size) {
        // Attempt to remove the point
        points.removeIf(p -> p.equals(point));

        // Check if the LeafNode still contains any points after the removal
        if (points.isEmpty()) {
            // If no points are left, return a FlyweightNode instance to
            // indicate that this node can be collapsed
            return FlyweightNode.getInstance();
        }
        else {
            // If there are still points present, return this LeafNode
            return this;
        }
    }


    /**
     * Searches for a point in the QuadTree by name.
     * 
     * @param name
     *            The name of the point to search for.
     * @return A list of points that match the given name.
     */
    @Override
    public ArrayList<Point> search(String name) {
        ArrayList<Point> matchingPoints = new ArrayList<>();
        for (Point p : points) {
            if (p.getName().equals(name)) {
                matchingPoints.add(p);
            }
        }
        return matchingPoints;
    }


    /**
     * Searches for points in a specific region of the QuadTree.
     * 
     * @param queryX
     *            The x-coordinate of the query region.
     * @param queryY
     *            The y-coordinate of the query region.
     * @param width
     *            The width of the query region.
     * @param height
     *            The height of the query region.
     * @param nodeX
     *            The x-coordinate of the node.
     * @param nodeY
     *            The y-coordinate of the node.
     * @param nodeSize
     *            The size of the node.
     * @return A list of points that are within the specified region.
     */
    @Override
    public ArrayList<Point> regionSearch(
        int queryX,
        int queryY,
        int width,
        int height,
        int nodeX,
        int nodeY,
        int nodeSize) {
        PRQuadTree.incrementRegionSearchNodeCount();
        ArrayList<Point> foundPoints = new ArrayList<>();
        for (Point p : points) {
            if (p.getX() >= queryX && p.getX() <= queryX + width && p
                .getY() >= queryY && p.getY() <= queryY + height) {
                foundPoints.add(p);
            }
        }
        return foundPoints;
    }


    /**
     * Dumps the data of the node.
     * 
     * @param level
     *            The level of the node in the QuadTree.
     */
    @Override
    public void dump(int level) {
        // Implementation needed for dumping the tree structure
    }


    /**
     * Returns the point at the specified coordinates.
     * 
     * @param x
     *            The x-coordinate of the point.
     * @param y
     *            The y-coordinate of the point.
     * @return The point at the specified coordinates, or null if no such point
     *         exists.
     */
    public Point getPoint(int x, int y) {
        for (Point p : points) {
            if (p.getX() == x && p.getY() == y) {
                return p;
            }
        }
        return null;
    }
}
