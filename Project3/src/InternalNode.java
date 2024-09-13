import java.util.ArrayList;

/**
 * Represents an internal node in a PR Quadtree. Internal nodes do not hold data
 * themselves but have references to four child nodes that represent the NW, NE,
 * SW, and SE quadrants of the space the internal node occupies.
 * 
 * @author kinjalpandey, architg03, rishavc18
 * @version 02/25/24
 */
public class InternalNode extends QuadTreeNode {

    private QuadTreeNode nw;
    private QuadTreeNode ne;
    private QuadTreeNode sw;
    private QuadTreeNode se;

    /**
     * Constructs an InternalNode with all child nodes initialized to the
     * FlyweightNode instance, representing empty quadrants.
     */
    public InternalNode() {
        super();
        nw = FlyweightNode.getInstance();
        ne = FlyweightNode.getInstance();
        sw = FlyweightNode.getInstance();
        se = FlyweightNode.getInstance();
    }


    /**
     * Finds the quadrant a point belongs to.
     *
     * @param point
     *            The point to find.
     * @param x
     *            The x-coordinate of the upper left corner of the current
     *            region.
     * @param y
     *            The y-coordinate of the upper left corner of the current
     *            region.
     * @param size
     *            The size of the current region.
     * @return The quadrant the point belongs to.
     */
    public QuadTreeNode findQuadrant(Point point, int x, int y, int size) {
        int halfSize = size / 2;
        int midX = x + halfSize;
        int midY = y + halfSize;

        // Determine which quadrant the point belongs to.
        if (point.getX() < midX) {
            if (point.getY() < midY) {
                return nw; // NW quadrant
            }
            else {
                return sw; // SW quadrant
            }
        }
        else {
            if (point.getY() < midY) {
                return ne; // NE quadrant
            }
            else {
                return se; // SE quadrant 
            }
        }
    }


    /**
     * Inserts a point into the appropriate quadrant. If the insertion leads to
     * a
     * full quadrant, it considers converting the child node to an InternalNode.
     *
     * @param point
     *            The point to insert.
     * @param x
     *            The x-coordinate of the upper left corner of the current
     *            region.
     * @param y
     *            The y-coordinate of the upper left corner of the current
     *            region.
     * @param size
     *            The size of the current region.
     * @return The updated node after insertion, which may be a new node if the
     *         structure changed.
     */
    @Override
    public QuadTreeNode insert(Point point, int x, int y, int size) {
        QuadTreeNode quadrant = findQuadrant(point, x, y, size);

        // Check if the point already exists in the quadrant.
        if (quadrant.search(point.getName()).contains(point)) {
            return this; // Return this internal node without inserting the
                         // point.
        }

        // Insert the point if it does not exist.
        int halfSize = size / 2;
        int midX = x + halfSize;
        int midY = y + halfSize;

        if (point.getX() < midX) {
            if (point.getY() < midY) {
                nw = nw.insert(point, x, y, halfSize); // NW quadrant
            }
            else {
                sw = sw.insert(point, x, midY, halfSize); // SW quadrant
            }
        }
        else {
            if (point.getY() < midY) {
                ne = ne.insert(point, midX, y, halfSize); // NE quadrant
            }
            else {
                se = se.insert(point, midX, midY, halfSize); // SE quadrant
            }
        }

        return this; // Return this internal node after the insertion.
    }


    /**
     * Removes a point from the appropriate quadrant. If the removal leads to an
     * empty quadrant, it considers converting the child node back to a
     * FlyweightNode.
     *
     * @param point
     *            The point to remove.
     * @param x
     *            The x-coordinate of the upper left corner of the current
     *            region.
     * @param y
     *            The y-coordinate of the upper left corner of the current
     *            region.
     * @param size
     *            The size of the current region.
     * @return true if the point was removed; false otherwise.
     */
    @Override
    public QuadTreeNode remove(Point point, int x, int y, int size) {
        int halfSize = size / 2;
        int midX = x + halfSize;
        int midY = y + halfSize;

        // Determine the correct quadrant and remove the point from it.
        if (point.getX() < midX) {
            if (point.getY() < midY) {
                nw = nw.remove(point, x, y, halfSize); // NW quadrant
            }
            else {
                sw = sw.remove(point, x, midY, halfSize); // SW quadrant
            }
        }
        else {
            if (point.getY() < midY) {
                ne = ne.remove(point, midX, y, halfSize); // NE quadrant
            }
            else {
                se = se.remove(point, midX, midY, halfSize); // SE quadrant
            }
        }

        // Attempt to merge after removal.
        return attemptMerge();
    }


    /**
     * Attempts to merge nodes
     * 
     * @return QuadTreeNode merged nodes
     */
    public QuadTreeNode attemptMerge() {
        // Check if all children are either leaf nodes or flyweights.
        ArrayList<Point> combinedPoints = new ArrayList<>();
        QuadTreeNode[] children = { nw, ne, sw, se };
        for (QuadTreeNode child : children) {
            if (child instanceof LeafNode) {
                combinedPoints.addAll(((LeafNode)child).getPoints());
            }
            else if (!(child instanceof FlyweightNode)) {
                // If any child is not a leaf node or flyweight, merging is not
                // possible.
                return this;
            }
        }

        // If combined points do not exceed the limit and either all points are
        // at the same position
        // or there are three or fewer points, merge into a single leaf node.
        if (combinedPoints.size() <= 3 || allPointsHaveSameCoordinates(
            combinedPoints)) {
            LeafNode mergedLeaf = new LeafNode();
            for (Point point : combinedPoints) {
                mergedLeaf.insert(point, 0, 0, 0); // Size and coordinates are
                                                   // not used in LeafNode
                                                   // insert.
            }
            return mergedLeaf;
        }

        // No merging occurred, return this node.
        return this;
    }


    /**
     * Helper function to test all points being the same node
     * @param points1 points in tree
     * @return true if same
     */
    public boolean allPointsHaveSameCoordinates(ArrayList<Point> points1) {
        if (points1.isEmpty()) {
            return true;
        }
        Point first = points1.get(0);
        return points1.stream().allMatch(point -> point.getX() == first.getX()
            && point.getY() == first.getY());
    }


    /**
     * Searches for all points matching a given name within the quadrants of
     * this
     * internal node.
     *
     * @param name
     *            The name of the points to search for.
     * @return A list of points matching the given name.
     */
    @Override
    public ArrayList<Point> search(String name) {
        ArrayList<Point> foundPoints = new ArrayList<>();
        foundPoints.addAll(nw.search(name));
        foundPoints.addAll(ne.search(name));
        foundPoints.addAll(sw.search(name));
        foundPoints.addAll(se.search(name));
        return foundPoints;
    }


    /**
     * Performs a region search to find all points within a specified
     * rectangular
     * area.
     *
     * @param queryX
     *            The x-coordinate of the upper left corner of the query
     *            region.
     * @param queryY
     *            The y-coordinate of the upper left corner of the query
     *            region.
     * @param width
     *            The width of the query region.
     * @param height
     *            The height of the query region.
     * @param nodeX
     *            The x-coordinate of the upper left corner of the current
     *            node's region.
     * @param nodeY
     *            The y-coordinate of the upper left corner of the current
     *            node's region.
     * @param nodeSize
     *            The size of the current node's region.
     * @return A list of points found within the query region.
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
        int midX = nodeX + nodeSize / 2;
        int midY = nodeY + nodeSize / 2;

        if (intersects(queryX, queryY, width, height, nodeX, nodeY, nodeSize,
            nodeSize)) {
            foundPoints.addAll(nw.regionSearch(queryX, queryY, width, height,
                nodeX, nodeY, nodeSize / 2));
            foundPoints.addAll(ne.regionSearch(queryX, queryY, width, height,
                midX, nodeY, nodeSize / 2));
            foundPoints.addAll(sw.regionSearch(queryX, queryY, width, height,
                nodeX, midY, nodeSize / 2));
            foundPoints.addAll(se.regionSearch(queryX, queryY, width, height,
                midX, midY, nodeSize / 2));
        }
        return foundPoints;
    }


    /**
     * 
     * @param queryX
     *            The coordinates and dimensions of the search area.
     * @param queryY
     *            The coordinates and dimensions of the search area.
     * @param width
     *            The coordinates and dimensions of the search area.
     * @param height
     *            The coordinates and dimensions of the search area.
     * @param nodeX
     *            The coordinates and dimensions of the node's quadrant.
     * @param nodeY
     *            The coordinates and dimensions of the node's quadrant.
     * @param nodeWidth
     *            The coordinates and dimensions of the node's quadrant.
     * @param nodeHeight
     *            The coordinates and dimensions of the node's quadrant.
     * @return boolean true is intersects
     */
    public boolean intersects(
        int queryX,
        int queryY,
        int width,
        int height,
        int nodeX,
        int nodeY,
        int nodeWidth,
        int nodeHeight) {
        return queryX < nodeX + nodeWidth && queryX + width > nodeX
            && queryY < nodeY + nodeHeight && queryY + height > nodeY;
    }


    /**
     * Getter for nw
     * 
     * @return nw
     */
    public QuadTreeNode getNw() {
        return nw;
    }


    /**
     * Setter for nw
     * 
     * @param parNW
     *            QuadTreeNode
     * @return nw
     */
    public QuadTreeNode setNw(QuadTreeNode parNW) {
        this.nw = parNW;
        return parNW;
    }


    /**
     * Getter for ne
     * 
     * @return ne
     */
    public QuadTreeNode getNe() {
        return ne;
    }


    /**
     * Setter for ne
     * 
     * @param parNE
     *            QuadTreeNode
     * @return ne
     */
    public QuadTreeNode setNe(QuadTreeNode parNE) {
        this.ne = parNE;
        return parNE;
    }


    /**
     * Getter for sw
     * 
     * @return sw
     */
    public QuadTreeNode getSw() {
        return sw;
    }


    /**
     * Setter for sw
     * 
     * @param parSW
     *            QuadTreeNode
     * @return sw
     */
    public QuadTreeNode setSw(QuadTreeNode parSW) {
        this.sw = parSW;
        return parSW;
    }


    /**
     * Getter for se
     * 
     * @return se
     */
    public QuadTreeNode getSe() {
        return se;
    }


    /**
     * Setter for se
     * 
     * @param parSE
     *            QuadTreeNode
     * @return se
     */
    public QuadTreeNode setSe(QuadTreeNode parSE) {
        this.se = parSE;
        return parSE;

    }


    /**
     * Dump Method
     * 
     * @param level
     *            lvl
     */
    public void dump(int level) {
        System.out.println(indent(level) + "InternalNode");
        nw.dump(level + 1);
        ne.dump(level + 1);
        sw.dump(level + 1);
        se.dump(level + 1);
    }


    /**
     * Helper method for indentation
     * 
     * @param level
     *            level of tree
     * @return String representation
     */
    public String indent(int level) {
        return " ".repeat(level * 2);
    }
}
