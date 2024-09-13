import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;
import java.io.File;

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
 * This be the javadoc to be done
 * 
 * @author kinjalpandey, architg03, rishavc18
 * @version 02/25/24
 * 
 */
public class PointsDatabase {
    private SkipList<String, Point> skipList;
    private PRQuadTree quadTree;
    private static final int WORLD_SIZE = 1024;

    /**
     * init
     */
    public PointsDatabase() {
        this.skipList = new SkipList<>();
        this.quadTree = new PRQuadTree(WORLD_SIZE);
    }


    /**
     * The entry point of the program which initializes the command processor
     * and
     * processes commands from the specified file.
     * 
     * @param args
     *            Command line arguments, expecting the name of the command file
     *            as
     *            the first argument.
     */
    public static void main(String[] args) {
        // Check if the command line argument for the command file is provided
        if (args.length < 1) {
            System.out.println("No command file provided.");
            return;
        }

        String commandFileName = args[0];
        PointsCommandProcessor commandProcessor = new PointsCommandProcessor();

        // Attempt to open and process the command file
        try (Scanner scanner = new Scanner(new File(commandFileName))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (!line.isEmpty()) {
                    commandProcessor.processCommand(line);
                }
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("Command file not found: " + commandFileName);
        }
    }


    /**
     * gets Points From SkipList
     * 
     * @param name
     *            name of list
     * @return List<Point> list
     */
    public MyList<Point> getPointsFromSkipList(String name) {
        List<KVPair<String, Point>> pairs = skipList.search(name);

        MyList<Point> points = new MyList<>();
        for (KVPair<String, Point> pair : pairs) {
            points.add(pair.getValue());
        }
        return points;
    }


    /**
     * gets Points From SkipList
     * 
     * @param name
     *            name of list
     * @return List<Point> list
     */
    public MyList<Point> getPointsByName(String name) {
        List<KVPair<String, Point>> searchResults = skipList.search(name);
        MyList<Point> points = new MyList<>();
        for (KVPair<String, Point> pair : searchResults) {
            points.add(pair.getValue());
        }
        return points;
    }


    /**
     * insert
     * 
     * @param name
     *            of rectangle
     * @param x
     *            coord
     * @param y
     *            coord
     */
    public void insert(String name, int x, int y) {
        if (x < 0 || y < 0 || x >= WORLD_SIZE || y >= WORLD_SIZE) {
            System.out.println("Point rejected: (" + name + ", " + x + ", " + y
                + ")");
            return;
        }

        Point point = new Point(name, x, y);
        // Create a KVPair object for the point insertion
        KVPair<String, Point> pair = new KVPair<>(name, point);
        skipList.insert(pair); // Now we're passing a KVPair as expected by the
                               // SkipList.insert method
        quadTree.insert(point);
        System.out.println("Point inserted: " + "(" + name + ", " + x + ", " + y
            + ")");
        // System.out.println(quadTree.numOfPoints());
    }


    /**
     * remove
     * 
     * @param name
     *            name
     */
    public void remove(String name) {
        MyList<Point> points = getPointsFromSkipList(name); // Use the utility
                                                            // // method
        if (points == null) {
            System.out.println("Point not found: " + name);
            return;
        }
        if (points.isEmpty()) {
            System.out.println("Point not removed: " + name);
            return;
        }

        Point pointToRemove = points.get(0);
        skipList.remove(name); // Assuming removal by key
        quadTree.remove(pointToRemove); // Assuming direct removal by Point
                                        // object is supported
        System.out.println("Point removed: (" + pointToRemove.getName() + ", "
            + pointToRemove.getX() + ", " + pointToRemove.getY() + ")");
    }


    /**
     * remove
     * 
     * @param x
     *            coord
     * @param y
     *            coord
     */
    public void remove(int x, int y) {
        // Assuming PRQuadTree provides a method to directly find points by
        // coordinates
        if (x < 0 || y < 0) {
            System.out.println("Point rejected: (" + x + ", " + y + ")");
            return;
        }
        Point point = quadTree.searchByCoordinates(x, y);
        if (point == null) {
            System.out.println("Point not found: (" + x + ", " + y + ")");
            return;
        }
        if (!point.isValid()) {
            System.out.println("Point rejected (" + x + ", " + y + ")");
            return;
        }
        // Assuming we're dealing with unique points or just removing the first
        // found
        quadTree.remove(point); // Adjusted for hypothetical direct removal
        skipList.remove(point.getName());
        System.out.println("Point removed: (" + point + ")");
    }


    /**
     * Region Search
     * 
     * @param x
     *            coord
     * @param y
     *            coord
     * @param width
     *            coord
     * @param height
     *            coord
     */
    public void regionSearch(int x, int y, int width, int height) {
        if (width <= 0 || height <= 0) {
            System.out.println("Rectangle rejected: (" + x + ", " + y + ", "
                + width + ", " + height + ")");
            return;
        }
        Rectangle region = new Rectangle(x, y, width, height);
        List<Point> points = quadTree.regionSearch(x, y, width, height);
        System.out.println("Points intersecting region (" + region.toString()
            + "):");
        for (Point point : points) {
            System.out.println("Point found: " + "(" + point + ")");
        }
        System.out.println(PRQuadTree.getRegionSearchNodeCount() + 1
            + " quadtree nodes visited");
    }


    /**
     * dupe
     */
    /**
     * dupe Prints only the coordinates of the duplicate points.
     */
    /**
     * dupe Prints the coordinates of duplicate points, each coordinate only
     * once.
     */
    public void duplicates() {
        MyList<Point> duplicates = quadTree.findDuplicates();
        if (duplicates.isEmpty()) {
            System.out.println("Duplicate points:");
            return;
        }

        // Temporary variables to track the last printed coordinates
        int lastX = Integer.MIN_VALUE;
        int lastY = Integer.MIN_VALUE;

        System.out.println("Duplicate points:");
        for (Point duplicate : duplicates) {
            // Check if this coordinate is different from the last one printed
            if (duplicate.getX() != lastX || duplicate.getY() != lastY) {
                // Update lastX and lastY to the current coordinate
                lastX = duplicate.getX();
                lastY = duplicate.getY();

                // Print the current unique duplicate coordinate
                System.out.println("(" + lastX + ", " + lastY + ")");
            }
        }
    }


    /**
     * Search
     * 
     * @param name
     *            rec
     */
    public void search(String name) {
        boolean found = false;
        for (KVPair<String, Point> pair : skipList.search(name)) {
            System.out.println("Found (" + name + ", " + pair.getValue().getX()
                + ", " + pair.getValue().getY() + ")");
            found = true;
        }

        if (!found) {
            System.out.println("Point not found: " + name);
        }
    }


    /**
     * Dumps
     */
    public void dump() {
        skipList.dump();
        quadTree.dump();
    }
}
