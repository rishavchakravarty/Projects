import java.util.ArrayList;

/**
 * Represents a PR QuadTree. This class is responsible for managing the
 * structure of the QuadTree and the operations that can be performed on it.
 * 
 * @author kinjalpandey, architg03, rishavc18
 * @version 02/25/24
 */
public class PRQuadTree {
    private QuadTreeNode root;
    private final int size;
    private int nodeCounter = 0;
    private static int regionSearchNodeCount;

    /**
     * Constructs a PRQuadTree with the specified size.
     *
     * @param size1
     *            the size of the QuadTree
     */
    public PRQuadTree(int size1) {
        this.root = FlyweightNode.getInstance();
        this.size = size1;
        this.nodeCounter = 0;
        this.regionSearchNodeCount = -2;
    }


    /**
     * Inserts a point into the QuadTree.
     *
     * @param point
     *            the point to be inserted
     */
    public void insert(Point point) {
        // System.out.println("Inserting Point: " + point.toString());
        root = root.insert(point, 0, 0, size);
    }


    /**
     * Removes a point from the QuadTree.
     *
     * @param point
     *            the point to be removed
     */
    public void remove(Point point) {
        // System.out.println("Removing Point: " + point.toString());
        root = root.remove(point, 0, 0, size);
    }


    /**
     * Removes a point from the QuadTree.
     *
     * @param x
     *            the x-coordinate of the point to be removed
     * @param y
     *            the y-coordinate of the point to be removed
     */
    public void remove(int x, int y) {
        Point point = new Point("", x, y);
        root = root.remove(point, 0, 0, size);

    }


    /**
     * Returns the number of points in the QuadTree.
     *
     * @return the number of points in the QuadTree
     */
    public int numOfPoints() {
        return numOfPoints(root);
    }


    /**
     * Returns the number of points in a node.
     *
     * @param node
     *            the node to count the points in
     * @return the number of points in the node
     */
    int numOfPoints(QuadTreeNode node) {
        if (node == null || node instanceof FlyweightNode) {
            return 0;
        }
        else if (node instanceof LeafNode) {
            return ((LeafNode)node).getPoints().size();
        }
        else if (node instanceof InternalNode) {
            InternalNode internalNode = (InternalNode)node;
            return numOfPoints(internalNode.getNw()) + numOfPoints(internalNode
                .getNe()) + numOfPoints(internalNode.getSw()) + numOfPoints(
                    internalNode.getSe());
        }
        return 0;
    }


    /**
     * Searches for points within a specified region in the QuadTree.
     *
     * @param x
     *            the x-coordinate of the top-left corner of the region
     * @param y
     *            the y-coordinate of the top-left corner of the region
     * @param width
     *            the width of the region
     * @param height
     *            the height of the region
     * @return a list of points within the specified region
     */
    public ArrayList<Point> regionSearch(int x, int y, int width, int height) {
//        regionSearchNodeCount = 0; // Reset the counter before starting the
                                   // search
        ArrayList<Point> foundPoints = root.regionSearch(x, y, width, height, 0,
            0, size);
// System.out.println((regionSearchNodeCount - 1) + " quadtree nodes visited");
// // Print the total count
        return foundPoints;
    }
 
    /**
     * Increments the region search node count.
     */
    public static void incrementRegionSearchNodeCount() {
        regionSearchNodeCount++;
    }


    /**
     * Gets the region search node count.
     *
     * @return the region search node count
     */
    public static int getRegionSearchNodeCount() {
        return regionSearchNodeCount;
    }


    /**
     * Searches for points by name in the QuadTree.
     *
     * @param name
     *            the name of the points to search for
     * @return a list of points with the specified name
     */
    public ArrayList<Point> searchByName(String name) {
        return root.search(name);
    }


    /**
     * Searches for a point by coordinates in the QuadTree.
     *
     * @param x
     *            the x-coordinate of the point
     * @param y
     *            the y-coordinate of the point
     * @return the point with the specified coordinates, or null if no such
     *         point
     *         exists
     */
    public Point searchByCoordinates(int x, int y) {
        return searchByCoordinates(root, x, y, 0, 0, size);
    }


    /**
     * Searches for a point by coordinates in the QuadTree.
     *
     * @param node
     *            the node to start the search from
     * @param x
     *            the x-coordinate of the point
     * @param y
     *            the y-coordinate of the point
     * @param startX
     *            the x-coordinate of the starting point of the search region
     * @param startY
     *            the y-coordinate of the starting point of the search region
     * @param regionSize
     *            the size of the search region
     * @return the point with the specified coordinates, or null if no such
     *         point
     *         exists
     */
    public Point searchByCoordinates(
        QuadTreeNode node,
        int x,
        int y,
        int startX,
        int startY,
        int regionSize) {
        if (node == null || node instanceof FlyweightNode) {
            return null;
        }
        else if (node instanceof LeafNode) {
            Point point = ((LeafNode)node).getPoint(x, y);
            if (point != null && point.getX() == x && point.getY() == y) {
                return point;
            }
            return null;
        }
        else if (node instanceof InternalNode) {
            int halfSize = regionSize / 2;
            int midX = startX + halfSize;
            int midY = startY + halfSize;

            if (x < midX) {
                return y < midY
                    ? searchByCoordinates(((InternalNode)node).getNw(), x, y,
                        startX, startY, halfSize)
                    : searchByCoordinates(((InternalNode)node).getSw(), x, y,
                        startX, midY, halfSize);
            }
            else {
                return y < midY
                    ? searchByCoordinates(((InternalNode)node).getNe(), x, y,
                        midX, startY, halfSize)
                    : searchByCoordinates(((InternalNode)node).getSe(), x, y,
                        midX, midY, halfSize);
            }
        }
        return null;
    }


    /**
     * Collects all points in the QuadTree.
     *
     * @param node
     *            the node to start the collection from
     * @param allPoints
     *            the list to store the collected points
     */
    void collectPoints(QuadTreeNode node, ArrayList<Point> allPoints) {
        if (node == null || node instanceof FlyweightNode) {
            return;
        }
        else if (node instanceof LeafNode) {
            allPoints.addAll(((LeafNode)node).getPoints());
        }
        else if (node instanceof InternalNode) {
            collectPoints(((InternalNode)node).getNw(), allPoints);
            collectPoints(((InternalNode)node).getNe(), allPoints);
            collectPoints(((InternalNode)node).getSw(), allPoints);
            collectPoints(((InternalNode)node).getSe(), allPoints);
        }
    }


    /**
     * Identifies duplicate points in a list of points.
     *
     * @param allPoints
     *            the list of points to check for duplicates
     * @return a list of duplicate points
     */
    ArrayList<Point> identifyDuplicates(ArrayList<Point> allPoints) {
        ArrayList<Point> duplicates = new ArrayList<>();
        for (int i = 0; i < allPoints.size(); i++) {
            Point currentPoint = allPoints.get(i);
            for (int j = i + 1; j < allPoints.size(); j++) {
                Point comparePoint = allPoints.get(j);
                if (currentPoint.getX() == comparePoint.getX() && currentPoint
                    .getY() == comparePoint.getY() && !duplicates.contains(
                        currentPoint)) {
                    duplicates.add(currentPoint);
                    duplicates.add(comparePoint);
                }
            }
        }
        return duplicates;
    }


    /**
     * Prints a number of indents.
     *
     * @param depth
     *            the number of indents to print
     */
    @SuppressWarnings("unused")
    private void printIndent(int depth) {
        for (int i = 0; i < depth; i++) {
            System.out.print("  ");
        }
    }


    /**
     * It returns the root of the tree
     *
     * @return root root of tree
     */
    public QuadTreeNode getRoot() {
        return this.root;
    }


    /**
     * Prints the structure of a node in the QuadTree.
     */
    public void dump() {
        System.out.println("QuadTree dump:");
        nodeCounter = 0; // Reset the counter before starting the dump
        dump(root, 0, 0, size, 0); // Start the recursive dump
        System.out.println(nodeCounter + " quadtree nodes printed"); // Print
                                                                     // the
                                                                     // total
                                                                     // count
    }


    private void dump(QuadTreeNode node, int x, int y, int size2, int depth) {
        String indent = " ".repeat(depth * 2); // Indentation based on the depth
        if (node instanceof FlyweightNode) {
            System.out.println(indent + "Node at " + x + ", " + y + ", " + size2
                + ": Empty");
        }
        else if (node instanceof LeafNode) {
            LeafNode leaf = (LeafNode)node;
            System.out.println(indent + "Node at " + x + ", " + y + ", " + size2
                + ":");
            for (Point p : leaf.getPoints()) {
                System.out.println(indent + "(" + p.toString() + ")");
            }
        }
        else if (node instanceof InternalNode) {
            System.out.println(indent + "Node at " + x + ", " + y + ", " + size2
                + ": Internal");
        }
        // Regardless of the node type, increment the counter
        nodeCounter++;

        // If it's an internal node, continue the recursive dump for its
        // children
        if (node instanceof InternalNode) {
            InternalNode internal = (InternalNode)node;
            int newSize = size2 / 2;
            dump(internal.getNw(), x, y, newSize, depth + 1);
            dump(internal.getNe(), x + newSize, y, newSize, depth + 1);
            dump(internal.getSw(), x, y + newSize, newSize, depth + 1);
            dump(internal.getSe(), x + newSize, y + newSize, newSize, depth
                + 1);
        }
    }


    /**
     * FInds duplicates in tree
     *
     * @return List of duplicates
     */
    public MyList<Point> findDuplicates() {
        MyList<Point> allPoints = collectPoints(root);
        MyList<Point> duplicates = new MyList<>();

        for (int i = 0; i < allPoints.size(); i++) {
            Point pointA = allPoints.get(i);
            for (int j = i + 1; j < allPoints.size(); j++) {
                Point pointB = allPoints.get(j);
                if (pointA.getX() == pointB.getX() && pointA.getY() == pointB
                    .getY()) {
                    // Check if this duplicate has already been found
                    boolean isDuplicateAlreadyFound = false;
                    for (int k = 0; k < duplicates.size(); k++) {
                        Point dup = duplicates.get(k);
                        if (dup.getX() == pointA.getX() && dup.getY() == pointA
                            .getY()) {
                            isDuplicateAlreadyFound = true;
                            break;
                        }
                    }
                    if (!isDuplicateAlreadyFound) {
                        duplicates.add(pointA);
                    }
                }
            }
        }
        return duplicates;
    }


    // Helper method to collect points from the QuadTree
    private MyList<Point> collectPoints(QuadTreeNode node) {
        MyList<Point> points = new MyList<>();
        if (node instanceof LeafNode) {
            LeafNode leaf = (LeafNode)node;
            for (int i = 0; i < leaf.getPoints().size(); i++) {
                points.add(leaf.getPoints().get(i));
            }
        }
        else if (node instanceof InternalNode) {
            InternalNode internal = (InternalNode)node;
            MyList<Point> nwPoints = collectPoints(internal.getNw());
            MyList<Point> nePoints = collectPoints(internal.getNe());
            MyList<Point> swPoints = collectPoints(internal.getSw());
            MyList<Point> sePoints = collectPoints(internal.getSe());
            for (int i = 0; i < nwPoints.size(); i++)
                points.add(nwPoints.get(i));
            for (int i = 0; i < nePoints.size(); i++)
                points.add(nePoints.get(i));
            for (int i = 0; i < swPoints.size(); i++)
                points.add(swPoints.get(i));
            for (int i = 0; i < sePoints.size(); i++)
                points.add(sePoints.get(i));
        }
        return points;
    }


    /**
     * Sets root
     *
     * @param leafNode
     *            root
     */
    public void setRoot(LeafNode leafNode) {
        this.root = leafNode;

    }


    /**
     * Sets root
     *
     * @param internalNode
     *            root
     */
    public void setRoot(InternalNode internalNode) {
        this.root = internalNode;

    }


    /**
     * Sets root
     *
     * @param instance
     *            root
     */
    public void setRoot(FlyweightNode instance) {
        this.root = instance;
        
    }

}
