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
 * FlyweightNode is a class that extends QuadTreeNode. It represents a node in a
 * QuadTree that doesn't hold any data.
 * 
 * @author Archit Gupta, Kinjal Pandey, Rishav Chakravarty
 * @version 1.0
 */
public class FlyweightNode extends QuadTreeNode {
    /**
     * The single instance of FlyweightNode.
     */
    private static final FlyweightNode INSTANCE = new FlyweightNode();

    /**
     * Private constructor to prevent instantiation.
     */
    private FlyweightNode() {
    }


    /**
     * Returns the single instance of FlyweightNode.
     * 
     * @return The single instance of FlyweightNode.
     */
    public static FlyweightNode getInstance() {
        return INSTANCE;
    }


    /**
     * Inserts a point into the QuadTree.
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
        LeafNode newNode = new LeafNode();
        return newNode.insert(point, x, y, size);
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
     * @return false as FlyweightNode doesn't hold any data.
     */
    @Override
    public QuadTreeNode remove(Point point, int x, int y, int size) {
        // Since this is a FlyweightNode (representing an empty area),
        // attempting to remove a point has no effect.
        return this; // No change to the node structure, so return this node
                     // itself.
    }


    /**
     * Searches for a point in the QuadTree by name.
     * 
     * @param name
     *            The name of the point to search for.
     * @return An empty list as FlyweightNode doesn't hold any data.
     */
    @Override
    public ArrayList<Point> search(String name) {
        return new ArrayList<>();
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
     * @return An empty list as FlyweightNode doesn't hold any data.
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
        return new ArrayList<>();
    }


    /**
     * Dumps the data of the node.
     * 
     * @param level
     *            The level of the node in the QuadTree.
     */
    @Override
    public void dump(int level) {
        // dump
    }
}
