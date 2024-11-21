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
 * The abstract base class for a node in a PR Quadtree. This class defines the
 * common operations that both internal nodes and leaf nodes must support,
 * including insertion, removal, and searching of points within a
 * two-dimensional space.
 * 
 * @author kinjalpandey, architg03, rishavc18
 * @version 02/25/24
 * 
 */
public abstract class QuadTreeNode {
    /**
     * Inserts a point into the appropriate location within the quadtree node.
     * If
     * the node is an internal node, this method will determine the correct
     * quadrant
     * for the point and delegate the insertion to that quadrant's node. If the
     * node
     * is a leaf, the point will be added directly to it, subject to the leaf
     * node's
     * capacity and splitting behavior.
     *
     * @param point
     *            The point to insert.
     * @param x
     *            The x-coordinate of the top-left corner of the current region.
     * @param y
     *            The y-coordinate of the top-left corner of the current region.
     * @param size
     *            The size of the current region.
     * @return The updated node after insertion, which may be a new node if the
     *         structure changed.
     */
    public abstract QuadTreeNode insert(Point point, int x, int y, int size);


    /**
     * Attempts to remove a specified point from the quadtree node. If the node
     * is
     * an internal node, it will delegate the removal to the appropriate
     * quadrant's
     * node. If the node is a leaf, it will attempt to remove the point directly
     * from its collection.
     *
     * @param point
     *            The point to remove.
     * @param x
     *            The x-coordinate of the top-left corner of the current region.
     * @param y
     *            The y-coordinate of the top-left corner of the current region.
     * @param size
     *            The size of the current region.
     * @return True if the point was successfully removed, false otherwise.
     */
    public abstract QuadTreeNode remove(Point point, int x, int y, int size);


    /**
     * Searches for all points matching a given name within the quadtree node.
     * This
     * method is used to find points based on their identifier rather than their
     * spatial location.
     *
     * @param name
     *            The name of the point to search for.
     * @return A list of points matching the given name. This list may be empty
     *         if
     *         no matching points are found.
     */
    public abstract ArrayList<Point> search(String name);


    /**
     * Indents output
     * @param level of tree
     * @return String output
     */
    protected String indent(int level) {
        return "  ".repeat(level);
    }


    /**
     * Performs a search within the quadtree node for all points that fall
     * within a
     * specified rectangular region. This method is essential for spatial
     * queries,
     * allowing for efficient retrieval of points based on their location.
     *
     * @param queryX
     *            The x-coordinate of the top-left corner of the query region.
     * @param queryY
     *            The y-coordinate of the top-left corner of the query region.
     * @param width
     *            The width of the query region.
     * @param height
     *            The height of the query region.
     * @param nodeX
     *            The x-coordinate of the top-left corner of the current node's
     *            region.
     * @param nodeY
     *            The y-coordinate of the top-left corner of the current node's
     *            region.
     * @param nodeSize
     *            The size of the current node's region.
     * @return A list of points found within the specified region. This list may
     *         be
     *         empty if no points are found.
     */
    public abstract ArrayList<Point> regionSearch(
        int queryX,
        int queryY,
        int width,
        int height,
        int nodeX,
        int nodeY,
        int nodeSize);


    /**
     * dump
     * 
     * @param level
     *            lvl
     */
    public abstract void dump(int level);
}
