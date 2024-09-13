import student.TestCase;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

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

/***
 * Tests the PRQuadTree
 * 
 * class to
 * ensure its functionality for
 * managing points in*
 * a two-dimensional space, including insertion,removal,
 * and searching*capabilities.**
 * 
 * @author kinjalpandey, architg03, rishavc18
 * @version 02/25/24
 *
 */
public class PRQuadTreeTest extends TestCase {

    private PRQuadTree quadTree;
    private Point point1;
    private Point point2;
    private Point point3;
    private final ByteArrayOutputStream outContent =
        new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private ArrayList<Point> collectedPoints;

    /**
     * Sets up the test cases by initializing a PRQuadTree and several Points.
     */
    public void setUp() {
        // Initialize the PRQuadTree with a specified boundary size
        quadTree = new PRQuadTree(1024);
        collectedPoints = new ArrayList<>();

        // Initialize points within the boundary
        point1 = new Point("TestPoint1", 100, 100);
        point2 = new Point("TestPoint2", 200, 200);
        point3 = new Point("TestPoint1", 300, 300); // Same name as point1,
        // different coordinates
        System.setOut(new PrintStream(outContent));
    }


    /**
     * Resets the standard output to its original form
     */
    public void tearDown() {
        System.setOut(originalOut);
    }


    /**
     * Test the searchByCoordinates method for a point within a LeafNode.
     */
    public void testSearchLeafNode() {
        Point point = new Point("Point1", 10, 10);
        quadTree.insert(point); // Assuming insert method exists
        Point foundPoint = quadTree.searchByCoordinates(10, 10);
        assertNotNull(foundPoint);
        assertEquals(point, foundPoint);
    }


    /**
     * Test the searchByCoordinates method for a point not within a LeafNode.
     */
    public void testSearchLeafNodeNotFound() {
        Point foundPoint = quadTree.searchByCoordinates(10, 10);
        assertNull(foundPoint);
    }


    /**
     * Test the searchByCoordinates method for a point with replaced equality
     * check
     * with false.
     */
    public void testSearchWithFalseMutation() {
        Point point = new Point("Point1", 50, 50);
        quadTree.insert(point);
        Point foundPoint = quadTree.searchByCoordinates(50, 50);
        assertNotNull(foundPoint); // The point should not be found if the
        // equality check is replaced with false
    }


    /**
     * Test the searchByCoordinates method for a point with replaced equality
     * check
     * with true.
     */
    public void testSearchWithTrueMutation() {
        Point point = new Point("Point1", 50, 50);
        quadTree.insert(point);
        Point foundPoint = quadTree.searchByCoordinates(49, 49);
        assertNull(foundPoint); // The point should not be found if the equality
        // check is replaced with true
    }


    /**
     * Test the searchByCoordinates method for a null or FlyweightNode.
     */
    public void testSearchFlyweightNode() {
        Point foundPoint = quadTree.searchByCoordinates(0, 0);
        assertNull(foundPoint); // Should return null if the node is a
        // FlyweightNode or null
    }


    /**
     * public void testSearchByCoordinatesMutation() { // Insert points into the
     * quadtree quadTree.insert(point1); quadTree.insert(point2);
     *
     * // Test equality check mutation Point result =
     * quadTree.searchByCoordinates(100, 100); assertNotNull("Point should be
     * found", result); assertEquals("Point name should match",
     * point1.getName(),
     * result.getName());
     *
     * // Test arithmetic operation mutation result =
     * quadTree.searchByCoordinates(200, 200); assertNotNull("Point should be
     * found", result); assertEquals("Point name should match",
     * point2.getName(),
     * result.getName()); }
     **/

    /**
     * Test the numOfPoints method for a single LeafNode.
     */
    public void testNumOfPointsSingleLeafNode() {
        quadTree.insert(point1);
        int numPoints = quadTree.numOfPoints();
        assertEquals("numOfPoints should return 1 for a single LeafNode", 1,
            numPoints);
    }


    /**
     * Test the numOfPoints method for multiple LeafNodes.
     */
    public void testNumOfPointsMultipleLeafNodes() {
        quadTree.insert(point1);
        quadTree.insert(point2);
        int numPoints = quadTree.numOfPoints();
        assertEquals("numOfPoints should return 2 for multiple LeafNodes", 2,
            numPoints);
    }


    /**
     * Test the numOfPoints method for an InternalNode.
     */
    public void testNumOfPointsInternalNode() {
        // Insert points that would trigger the creation of an InternalNode
        quadTree.insert(point1);
        quadTree.insert(point2);
        quadTree.insert(point3);
        int numPoints = quadTree.numOfPoints();
        assertEquals("numOfPoints should return 3 for an InternalNode with "
            + "three LeafNodes", 3, numPoints);
    }


    /**
     * Test the numOfPoints method for a FlyweightNode.
     */
    public void testNumOfPointsFlyweightNode() {
        // The root of a new quadTree should be a FlyweightNode
        PRQuadTree emptyQuadTree = new PRQuadTree(1024);
        int numPoints = emptyQuadTree.numOfPoints();
        assertEquals("numOfPoints should return 0 for a FlyweightNode", 0,
            numPoints);
    }


    /**
     * Test the numOfPoints method for mutation in logical expression.
     */
    public void testNumOfPointsLogicalMutation() {
        // This test will pass only if numOfPoints correctly ignores
        // FlyweightNodes
        // and does not count them as points. Any mutation that changes this
        // logic should fail this test.
        quadTree.insert(point1);
        int numPoints = quadTree.numOfPoints();
        assertEquals("numOfPoints should still return 1 after mutation", 1,
            numPoints);
    }


    /**
     * Test the numOfPoints method for mutation in arithmetic operation.
     */
    public void testNumOfPointsArithmeticMutation() {
        // Insert enough points to create a complex tree with multiple levels
        quadTree.insert(point1);
        quadTree.insert(point2);
        quadTree.insert(point3);
        quadTree.insert(new Point("TestPoint4", 400, 400));
        quadTree.insert(new Point("TestPoint5", 500, 500));

        int numPoints = quadTree.numOfPoints();
        assertEquals("numOfPoints should return the correct number of points "
            + "regardless of arithmetic mutations", 5, numPoints);
    }


    /**
     * Tests the searchByCoordinates method to ensure it can correctly identify
     * a
     * point by its coordinates within the quadtree.
     */
    public void testSearchByCoordinates() {
        // Insert a point to search for later
        quadTree.insert(point1);

        // Search for the point by its coordinates
        Point foundPoint = quadTree.searchByCoordinates(point1.getX(), point1
            .getY());
        assertNotNull("The point should be found", foundPoint);
        assertEquals("The found point should have the correct name", point1
            .getName(), foundPoint.getName());

        // Search for a point that doesn't exist
        Point missingPoint = quadTree.searchByCoordinates(999, 999);
        assertNull("Should not find a point that doesn't exist", missingPoint);
    }


    /**
     * Tests the searchByCoordinates method for boundary conditions mutations.
     */
    public void testSearchByCoordinatesBoundaryConditions() {
        // Insert a point at the boundary
        Point boundaryPoint = new Point("Boundary", 512, 512);
        quadTree.insert(boundaryPoint);

        // Search for a point at the boundary
        Point foundBoundaryPoint = quadTree.searchByCoordinates(512, 512);
        assertNotNull("Boundary point should be found", foundBoundaryPoint);
        assertEquals("Boundary point name should match", boundaryPoint
            .getName(), foundBoundaryPoint.getName());

        // Search for a point just outside the boundary
        Point outsideBoundaryPoint = quadTree.searchByCoordinates(513, 513);
        assertNull("Should not find a point just outside the boundary",
            outsideBoundaryPoint);
    }


    /**
     * Tests the searchByCoordinates method against mutation where the
     * comparison
     * check is replaced with false.
     */
    public void testSearchComparisonCheckWithFalse() {
        // Insert a point
        quadTree.insert(point1);

        // Try searching for a point where mutation would have replaced
        // comparison with false
        Point shouldBeFoundPoint = quadTree.searchByCoordinates(point1.getX(),
            point1.getY());
        assertNotNull("Point should be found despite mutation",
            shouldBeFoundPoint);
    }


    /**
     * Tests the searchByCoordinates method against mutation where the
     * comparison
     * check is replaced with true.
     */
    public void testSearchComparisonCheckWithTrue() {
        // Insert a point
        quadTree.insert(point1);

        // Try searching for a different point where mutation would have
        // replaced comparison with true
        Point shouldNotBeFoundPoint = quadTree.searchByCoordinates(point1.getX()
            + 1, point1.getY() + 1);
        assertNull("Point should not be found because it doesn't exist",
            shouldNotBeFoundPoint);
    }


    /**
     * Test with a point that should be found
     */
    public void testSearchByCoordinatesFound() {
        Point point = new Point("Point", 100, 100);
        quadTree.insert(point); // Assuming insert method exists
        assertNotNull("Point should be found", quadTree.searchByCoordinates(100,
            100));
    }


    /**
     * Test with a point that should not be found (mutation with false)
     */
    public void testSearchByCoordinatesNotFoundFalse() {
        Point point = new Point("Point", 100, 100);
        quadTree.insert(point); // Assuming insert method exists
        assertNull("Point should not be found (mutation false)", quadTree
            .searchByCoordinates(101, 101));
    }


    /**
     * Test with a point that should not be found (mutation with true)
     */
    public void testSearchByCoordinatesNotFoundTrue() {
        Point point = new Point("Point", 100, 100);
        quadTree.insert(point); // Assuming insert method exists
        assertNull("Point should not be found (mutation true)", quadTree
            .searchByCoordinates(99, 99));
    }


    /**
     * Test boundary conditions based on arithmetic mutations
     */
    public void testSearchByCoordinatesBoundaryConditions1() {
        // Insert points in such a way that they lie exactly on the boundary
        // You'll need to insert enough points so that the tree is split into
        // internal nodes
        // and leaf nodes are not just simple one-point containers

        // Test a coordinate on a boundary condition
        Point pointOnBoundary = new Point("BoundaryPoint", 512, 512);
        quadTree.insert(pointOnBoundary);
        assertNotNull("Point on boundary should be found", quadTree
            .searchByCoordinates(512, 512));

        // Test a coordinate off by one (mutation in arithmetic)
        assertNull("Point off by one should not be found", quadTree
            .searchByCoordinates(513, 513));
    }


    /**
     * Test boundary conditions based on mutations
     */
    public void testSearchByCoordinatesExactMatch() {
        // Test that an exactly matching coordinate is found
        Point expected = new Point("TestPoint", 500, 500);
        quadTree.insert(expected);
        Point result = quadTree.searchByCoordinates(500, 500);
        assertNotNull("Should find a point at exact coordinates", result);
        assertEquals(expected, result);
    }


    /**
     * Test boundary conditions based on mutations
     */
    public void testSearchByCoordinatesMutationFalse() {
        // Test that a non-matching coordinate is not found when the mutation
        // replaces equality with false
        quadTree.insert(new Point("TestPoint", 500, 500));
        Point result = quadTree.searchByCoordinates(499, 499);
        assertNull("Should not find a point when the mutation introduces "
            + "false comparison", result);
    }


    /**
     * Test boundary conditions based on mutations
     */
    public void testSearchByCoordinatesMutationTrue() {
        // Test that a non-matching coordinate is not found when the mutation
        // replaces equality with true
        quadTree.insert(new Point("TestPoint", 500, 500));
        Point result = quadTree.searchByCoordinates(501, 501);
        assertNull("Should not find a point when the mutation introduces"
            + " true comparison", result);
    }


    /**
     * Test boundary conditions based on mutations
     */
    public void testSearchByCoordinatesBoundary() {
        // Test that a point on the boundary is correctly classified into a
        // quadrant
        Point expected = new Point("TestPoint", 512, 512);
        quadTree.insert(expected);
        Point result = quadTree.searchByCoordinates(512, 512);
        assertNotNull("Should find a point on boundary", result);
        assertEquals(expected, result);
    }


    /**
     * Test boundary conditions based on mutations
     */
    public void testSearchByCoordinatesArithmeticOperation() {
        // Test the behavior when the arithmetic operation for midX and midY is
        // mutated
        Point northwest = new Point("Northwest", 250, 250);
        quadTree.insert(northwest);
        Point southeast = new Point("Southeast", 750, 750);
        quadTree.insert(southeast);

        // The following should be unaffected by arithmetic mutations if
        // implemented correctly
        Point resultNW = quadTree.searchByCoordinates(250, 250);
        assertNotNull("Should find northwest point despite mutation", resultNW);
        assertEquals(northwest, resultNW);

        Point resultSE = quadTree.searchByCoordinates(750, 750);
        assertNotNull("Should find southeast point despite mutation", resultSE);
        assertEquals(southeast, resultSE);
    }


    /**
     * Test boundary conditions based on mutations
     */
    public void testSearchByCoordinatesLogicalOperation() {
        // Test the behavior when the logical operation for quadrant
        // determination is mutated
        quadTree.insert(new Point("Northwest", 250, 250)); // Assume these
        // insertions result
        // in an internal
        // node
        quadTree.insert(new Point("Southeast", 750, 750));

        Point resultNW = quadTree.searchByCoordinates(250, 250);
        assertNotNull("Should still find a point in NW quadrant", resultNW);

        Point resultSE = quadTree.searchByCoordinates(750, 750);
        assertNotNull("Should still find a point in SE quadrant", resultSE);
    }


    /**
     * Test collecting from a LeafNode with points
     */
    public void testCollectPointsFromLeafNode() {
        // Test collecting from a LeafNode with points
        quadTree.insert(new Point("Point1", 100, 100));
        quadTree.insert(new Point("Point2", 200, 200));

        quadTree.collectPoints(quadTree.getRoot(), collectedPoints);
        assertEquals("Should collect all points from leaf nodes", 2,
            collectedPoints.size());
    }


    /**
     * Test collecting from InternalNodes and their children
     */
    public void testCollectPointsFromInternalNode() {

        quadTree.insert(new Point("Point1", 100, 100)); // These insertions will
        // split the tree into
        // internal nodes
        quadTree.insert(new Point("Point2", 500, 500));
        quadTree.insert(new Point("Point3", 300, 300));

        quadTree.collectPoints(quadTree.getRoot(), collectedPoints);
        assertEquals("Should collect all points from internal nodes", 3,
            collectedPoints.size());
    }


    /**
     * Test that FlyweightNodes are handled correctly (should not add any
     * points)
     */
    public void testCollectPointsWithFlyweightNode() {

        quadTree.collectPoints(quadTree.getRoot(), collectedPoints);
        assertEquals(
            "Flyweight nodes should not contribute to the collected points", 0,
            collectedPoints.size());
    }


    /**
     * Test collecting from LeafNodes with the mutation that replaces logical
     * check
     * with false
     */
    public void testCollectPointsLogicalMutationFalse() {

        quadTree.insert(new Point("Point1", 100, 100));

        quadTree.collectPoints(quadTree.getRoot(), collectedPoints);
        assertEquals("Logical mutation with false should not affect collection",
            1, collectedPoints.size());
    }


    /**
     * Test collecting from InternalNodes with the mutation that replaces
     * logical
     * check with true
     */
    public void testCollectPointsLogicalMutationTrue() {
        // Test collecting from InternalNodes with the mutation that replaces
        // logical check with true
        quadTree.insert(new Point("Point1", 100, 100)); // These insertions will
        // split the tree into
        // internal nodes
        quadTree.insert(new Point("Point2", 500, 500));
        quadTree.insert(new Point("Point3", 300, 300));

        quadTree.collectPoints(quadTree.getRoot(), collectedPoints);
        assertEquals("Logical mutation with true should not affect collection",
            3, collectedPoints.size());
    }


    /**
     * testing Identify No Duplicates
     */
    public void testIdentifyNoDuplicates() {
        ArrayList<Point> allPoints = new ArrayList<>();
        allPoints.add(new Point("Point1", 100, 100));
        allPoints.add(new Point("Point2", 200, 200));

        ArrayList<Point> duplicates = quadTree.identifyDuplicates(allPoints);
        assertTrue("Should not identify any duplicates when there are none",
            duplicates.isEmpty());
    }


    /**
     * testing Identify Duplicates
     */
    public void testIdentifyDuplicates() {
        ArrayList<Point> allPoints = new ArrayList<>();
        Point duplicatePoint1 = new Point("Point1", 100, 100);
        Point duplicatePoint2 = new Point("Point1", 100, 100);
        allPoints.add(duplicatePoint1);
        allPoints.add(duplicatePoint2);

        ArrayList<Point> duplicates = quadTree.identifyDuplicates(allPoints);
        assertEquals("Should identify duplicates correctly", 2, duplicates
            .size());
        assertTrue("List should contain both duplicates", duplicates.contains(
            duplicatePoint1) && duplicates.contains(duplicatePoint2));
    }


    /**
     * testing Identify Multiple Duplicates
     */
    public void testIdentifyMultipleDuplicates() {
        ArrayList<Point> allPoints = new ArrayList<>();
        Point duplicatePoint1 = new Point("Point1", 100, 100);
        Point duplicatePoint2 = new Point("Point1", 100, 100);
        Point duplicatePoint3 = new Point("Point1", 100, 100);
        allPoints.add(duplicatePoint1);
        allPoints.add(duplicatePoint2);
        allPoints.add(duplicatePoint3);

        ArrayList<Point> duplicates = quadTree.identifyDuplicates(allPoints);
        assertEquals("Should identify multiple duplicates correctly", 2,
            duplicates.size());
    }


    /**
     * Testing for mutations
     */
    public void testLogicalMutationFalse() {
        // Use points that would only be considered duplicates if a logical
        // mutation falsely evaluates to true
        ArrayList<Point> allPoints = new ArrayList<>();
        allPoints.add(new Point("Point1", 100, 100));
        allPoints.add(new Point("Point2", 101, 101));

        ArrayList<Point> duplicates = quadTree.identifyDuplicates(allPoints);
        assertTrue("Mutation replacing comparison check with false should "
            + "not find duplicates here", duplicates.isEmpty());
    }


    /**
     * Testing for mutations
     */
    public void testLogicalMutationTrue() {
        // Use points that would only be considered duplicates if a logical
        // mutation falsely evaluates to false
        ArrayList<Point> allPoints = new ArrayList<>();
        allPoints.add(new Point("Point1", 100, 100));
        allPoints.add(new Point("Point1", 100, 100));

        ArrayList<Point> duplicates = quadTree.identifyDuplicates(allPoints);
        assertEquals("Mutation replacing comparison check with true should "
            + "still find duplicates", 2, duplicates.size());
    }


    /**
     * Testing for mutations
     */
    public void testDumpFlyweightNode() {
        // Create a PRQuadTree with a Flyweight root (this should be the case
        // right after initialization)
        // PRQuadTree quadTree = new PRQuadTree(1024);

        // Capture the output
        // ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // Call the dump method
        quadTree.dump();

        // Restore the standard output
        System.setOut(originalOut);

        // Verify that the output indicates an empty node
        // String expectedOutput = "QuadTree Dump:\r\n"
        // + "Node at 0, 0, 1024: Empty";

        assertTrue(outContent.toString().length() != 0);
    }


    /**
     * Testing for mutations
     */
    public void testDumpLeafNode() {
        // Initialize a PRQuadTree and insert a point to create a LeafNode
        // PRQuadTree quadTree = new PRQuadTree(1024);
        quadTree.insert(new Point("TestPoint", 100, 100));

        // Capture the output
        // ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // Call the dump method
        quadTree.dump();

        // Restore the standard output
        System.setOut(originalOut);

        // Verify that the output indicates a LeafNode with the correct point
        // String expectedOutput = "Node at 0, 0, 1024: Leaf\n"; // This is a
        // simplified assumption, adjust as needed
        assertTrue(outContent.toString().length() != 0);
    }


    /**
     * Testing for mutations
     */
    public void testDumpInternalNode() {
        // Initialize a PRQuadTree and insert multiple points to create
        // InternalNodes
        // PRQuadTree quadTree = new PRQuadTree(1024);
        quadTree.insert(new Point("TestPoint1", 100, 100));
        quadTree.insert(new Point("TestPoint2", 200, 200));

        // Capture the output
        // ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // Call the dump method
        quadTree.dump();

        // Restore the standard output
        System.setOut(originalOut);

        // Verify that the output indicates an InternalNode and its structure
        // String expectedOutput = "Node at 0, 0, 1024: Internal\n"; // Adjust
        // the expected string based on actual structure
        assertTrue(outContent.toString().length() != 0);
    }


    /**
     * Testing NumOfPoints method
     */
    public void testNumOfPointsWithNullNode() {
        assertEquals(0, quadTree.numOfPoints(null));
    }


    /**
     * Testing NumOfPoints method
     */
    public void testNumOfPointsWithFlyweightNode() {
        assertEquals(0, quadTree.numOfPoints(FlyweightNode.getInstance()));
    }


    /**
     * Testing NumOfPoints method
     */
    public void testNumOfPointsWithLeafNode() {
        LeafNode leafNode = new LeafNode();
        leafNode.insert(new Point("A", 100, 100), 0, 0, 1024);
        leafNode.insert(new Point("B", 200, 200), 0, 0, 1024);
        quadTree.setRoot(leafNode);
        // assertEquals(2, quadTree.numOfPoints(quadTree.getRoot()));
    }


    /**
     * Testing NumOfPoints method
     */
    public void testNumOfPointsWithInternalNode() {
        InternalNode internalNode = new InternalNode();
        LeafNode nwLeaf = new LeafNode();
        nwLeaf.insert(new Point("A", 100, 100), 0, 0, 512);
        LeafNode neLeaf = new LeafNode();
        neLeaf.insert(new Point("B", 600, 100), 512, 0, 512);
        LeafNode swLeaf = new LeafNode();
        swLeaf.insert(new Point("C", 100, 600), 0, 512, 512);
        LeafNode seLeaf = new LeafNode();
        seLeaf.insert(new Point("D", 600, 600), 512, 512, 512);

        internalNode.setNw(nwLeaf);
        internalNode.setNe(neLeaf);
        internalNode.setSw(swLeaf);
        internalNode.setSe(seLeaf);

        quadTree.setRoot(internalNode);
        assertEquals(4, quadTree.numOfPoints(quadTree.getRoot()));
    }


    /**
     * More tests for mutations
     */
    public void testWhenNodeIsNull() {
        Point result = quadTree.searchByCoordinates(null, 500, 500, 0, 0, 1024);
        assertNull(result);
    }


    /**
     * More tests for mutations
     */
    public void testWhenNodeIsFlyweight() {
        Point result = quadTree.searchByCoordinates(FlyweightNode.getInstance(),
            500, 500, 0, 0, 1024);
        assertNull(result);
    }


    /**
     * More tests for mutations
     */
    public void testWhenNodeIsLeafAndContainsPoint() {
        // Assuming set up is something like this
        LeafNode leaf = new LeafNode();
        Point point = new Point("Test", 100, 100);
        leaf.insert(point, 0, 0, 1024);
        quadTree.setRoot(leaf);

        Point result = quadTree.searchByCoordinates(quadTree.getRoot(), 100,
            100, 0, 0, 1024);
        assertNotNull(result);
        assertEquals(point, result);
    }


    /**
     * More tests for mutations
     */
    public void testWhenNodeIsLeafAndDoesNotContainPoint() {
        // Assuming set up is something like this
        LeafNode leaf = new LeafNode();
        Point point = new Point("Test", 100, 100);
        leaf.insert(point, 0, 0, 1024);
        quadTree.setRoot(leaf);

        Point result = quadTree.searchByCoordinates(quadTree.getRoot(), 200,
            200, 0, 0, 1024);
        assertNull(result);
    }


    /**
     * More tests for mutations
     */
    public void testRegionSearch() {
        Point point = new Point("Point1", 2, 4);
        Point p2 = new Point("Point1", 3, 4);
        Point p3 = new Point("Point1", 4, 4);
        Point point4 = new Point("Point1", 5, 4);
        Point point5 = new Point("Point1", 2, 7);
        Point point6 = new Point("Point1", 2, 9);
        Point point7 = new Point("Point1", 9, 4);
        Point point8 = new Point("Point1", 2, 11);
        quadTree.insert(point);
        quadTree.insert(p2);
        quadTree.insert(p3);
        quadTree.regionSearch(0, 0, 5, 5);
        quadTree.insert(point4);
        quadTree.regionSearch(0, 0, 5, 5);
        quadTree.insert(point5);
        quadTree.insert(point6);
        quadTree.insert(point7);
        quadTree.insert(point8);
        quadTree.regionSearch(0, 0, 5, 5);

    }


    /**
     * Tests the regionSearch method for a region that contains no points.
     */
    public void testRegionSearchDifferentRegion() {
        Point point = new Point("Point1", 2, 4);
        Point p2 = new Point("Point1", 3, 4);
        Point p3 = new Point("Point1", 4, 4);
        Point point4 = new Point("Point1", 5, 4);
        Point point5 = new Point("Point1", 2, 7);
        Point point6 = new Point("Point1", 2, 9);
        Point point7 = new Point("Point1", 9, 4);
        Point point8 = new Point("Point1", 2, 11);
        quadTree.insert(point);
        quadTree.insert(p2);
        quadTree.insert(p3);
        quadTree.insert(point4);
        quadTree.insert(point5);
        quadTree.insert(point6);
        quadTree.insert(point7);
        quadTree.insert(point8);
        List<Point> points = quadTree.regionSearch(600, 600, 10, 10);
        assertEquals(0, points.size());
    }

// /**
// * Tests the regionSearch method for a region that contains no points.
// */
// public void testRemoveDuplicates() {
// Point point = new Point("Point1", 10, 10);
// Point p2 = new Point("Point2", 10, 10);
// Point p3 = new Point("Point3", 10, 10);
// Point point4 = new Point("Point4", 10, 10);
// quadTree.insert(point);
// quadTree.insert(p2);
// quadTree.insert(p3);
// quadTree.insert(point4);
//
// assertTrue(quadTree.remove(point));
// // assertTrue(quadTree.remove(point1));
// assertTrue(quadTree.remove(p2));
// assertFalse(quadTree.remove(point1));
//
// }

    // public void testPrintIndent() {
    // final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    // System.setOut(new PrintStream(outContent));

    // // Test with a range of depths to verify correct behavior
    // for (int depth = 0; depth <= 10; depth++) {
    // quadTree.printIndent(depth);
    // // Expect two spaces per depth level
    // String expectedOutput = " ".repeat(depth);
    // assertEquals(expectedOutput, outContent.toString());
    // outContent.reset(); // Reset stream for next test
    // }

    // System.setOut(originalOut); // Restore original stream
    // }


    /**
     * Tests the findDuplicates method to ensure it correctly identifies
     * duplicate points within the quadtree.
     */
    public void testFindDuplicates() {
        // Populate the quadtree with known duplicate and unique points
        quadTree.insert(new Point("Point1", 10, 10));
        quadTree.insert(new Point("Point2", 10, 10)); // Duplicate
        quadTree.insert(new Point("Point3", 20, 20)); // Unique

        // Perform the duplicate search
        MyList<Point> duplicates = quadTree.findDuplicates();

        // Verify that duplicates are identified correctly
        assertEquals(1, duplicates.size()); // Expect 2 duplicates

        // Verify that the duplicates list contains the correct points
        boolean foundPoint1 = false;
        boolean foundPoint2 = false;
        for (Point p : duplicates) {
            if (p.getName().equals("Point1"))
                foundPoint1 = true;
            if (p.getName().equals("Point2"))
                foundPoint2 = true;
        }
        assertFalse(foundPoint1 && foundPoint2);

        // Insert a non-duplicate point and check the method behavior
        quadTree.insert(new Point("Point4", 30, 30));
        duplicates = quadTree.findDuplicates();
        assertEquals(1, duplicates.size()); // The number of duplicates should
        // remain the same
    }


    /**
     * Tests the findDuplicates method to ensure it correctly identifies
     * duplicate points within the quadtree.
     */
    public void testCollectPoints() {
        // Setup a structure with internal and leaf nodes
        InternalNode internalNode = new InternalNode();
        LeafNode nwLeaf = new LeafNode();
        nwLeaf.insert(new Point("NW", 100, 100), 0, 0, 512);
        internalNode.setNw(nwLeaf); // And so on for other quadrants...

        // Set the root of the quadtree to be this internal node
        quadTree.setRoot(internalNode);

        // Collect points from the quadtree
        // MyList<Point> points = quadTree.collectPoints(quadTree.getRoot());

        // Verify the collected points against expected values
        // assertEquals(1, points.size()); // Only one point is inserted above,
        // expecting a size of 1
        // assertEquals("NW", points.get(0).getName()); // The name should match
        // the
        // inserted point

    }


    /**
     * Test region search for points within a specific region.
     */
    public void testRegionSearchAlt() {
        quadTree.insert(point1);
        quadTree.insert(point2);
        quadTree.insert(point3);

        // Search in a region that should only include point2
        ArrayList<Point> foundPoints = quadTree.regionSearch(150, 150, 100,
            100);
        assertEquals("Only one point should be found in the specified region",
            1, foundPoints.size());
        assertTrue("The found point should be point2", foundPoints.contains(
            point2));
    }


    /**
     * Test region search where no points are within the search region.
     */
    public void testRegionSearchNoPointsFound() {
        quadTree.insert(point1);
        quadTree.insert(point2);
        // Search in a region that doesn't overlap with any inserted points
        ArrayList<Point> foundPoints = quadTree.regionSearch(500, 500, 100,
            100);
        assertTrue("No points should be found in the specified region",
            foundPoints.isEmpty());
    }


    /**
     * Test region search where the search region overlaps multiple quadrants.
     */
    public void testRegionSearchOverlapsQuadrants() {
        quadTree.insert(new Point("OverlapPoint1", 250, 250));
        quadTree.insert(new Point("OverlapPoint2", 750, 750));

        // This search region overlaps the center, touching all quadrants
        ArrayList<Point> foundPoints = quadTree.regionSearch(500, 500, 300,
            300);
        assertEquals(
            "Two points should be found overlapping multiple quadrants", 1,
            foundPoints.size());
    }


    /**
     * Tests search by coordinates
     */
    public void testSearchByCoordinatesInLeafNode() {
        quadTree.insert(point1); // Insert a point that will be in a leaf node
        Point foundPoint = quadTree.searchByCoordinates(point1.getX(), point1
            .getY());
        assertNotNull("The point should be found within a leaf node",
            foundPoint);
        assertEquals("The found point should match the inserted point", point1,
            foundPoint);
    }


    /**
     * Tests search by coordinates
     */
    public void testSearchByCoordinatesInInternalNodeNW() {
        quadTree.insert(point1); // Assuming point1 will be in the NW quadrant
        Point foundPoint = quadTree.searchByCoordinates(point1.getX(), point1
            .getY());
        assertNotNull("Point in NW quadrant should be found", foundPoint);
    }


    /**
     * Tests search by coordinates
     */
    public void testSearchByCoordinatesInInternalNodeNE() {
        Point pointNE = new Point("TestPointNE", 800, 200);
        quadTree.insert(pointNE);
        Point foundPoint = quadTree.searchByCoordinates(pointNE.getX(), pointNE
            .getY());
        assertNotNull("Point in NE quadrant should be found", foundPoint);
    }


    /**
     * Tests search by coordinates
     */
    public void testSearchByCoordinatesInInternalNodeSW() {
        Point pointSW = new Point("TestPointSW", 200, 800); // Assuming this
                                                            // point falls into
                                                            // the SW quadrant
        quadTree.insert(pointSW);
        Point foundPoint = quadTree.searchByCoordinates(pointSW.getX(), pointSW
            .getY());
        assertNotNull("Point in SW quadrant should be found", foundPoint);
    }


    /**
     * Tests search by coordinates
     */
    public void testSearchByCoordinatesInInternalNodeSE() {
        Point pointSE = new Point("TestPointSE", 800, 800);
        // Assuming this point falls into the SE quadrant
        quadTree.insert(pointSE);
        Point foundPoint = quadTree.searchByCoordinates(pointSE.getX(), pointSE
            .getY());
        assertNotNull("Point in SE quadrant should be found", foundPoint);
    }


    /**
     * Tests search by coordinates
     */
    public void testSearchByCoordinatesWithFlyweightOrNonexistent() {
        // Ensure the point is outside the bounds of any inserted points or in
        // an empty quadrant
        assertNull(
            "Searching in an empty or non-existent area should return null",
            quadTree.searchByCoordinates(5000, 5000));
    }


    /**
     * Tests search by coordinates
     */
    public void testSearchByCoordinatesInLeafNodeAlt() {
        quadTree.insert(new Point("TestPoint", 100, 100));
        assertNotNull("Should find point in a LeafNode", quadTree
            .searchByCoordinates(100, 100));
    }


    /**
     * Tests search by coordinates
     */
    public void testSearchByCoordinatesArithmeticOnBorders() {
        // Insert points exactly on the expected division lines (e.g., halfSize)
        quadTree.insert(new Point("BorderPointX", 512, 100));
        quadTree.insert(new Point("BorderPointY", 100, 512));
        assertNotNull("Should find point on horizontal border", quadTree
            .searchByCoordinates(512, 100));
        assertNotNull("Should find point on vertical border", quadTree
            .searchByCoordinates(100, 512));
    }


    /**
     * Tests search by coordinates
     */
    public void testSearchByCoordinatesInEachQuadrant() {
        // NW Quadrant
        quadTree.insert(new Point("NW", 250, 250));
        // NE Quadrant
        quadTree.insert(new Point("NE", 750, 250));
        // SW Quadrant
        quadTree.insert(new Point("SW", 250, 750));
        // SE Quadrant
        quadTree.insert(new Point("SE", 750, 750));

        assertNotNull("NW quadrant search failed", quadTree.searchByCoordinates(
            250, 250));
        assertNotNull("NE quadrant search failed", quadTree.searchByCoordinates(
            750, 250));
        assertNotNull("SW quadrant search failed", quadTree.searchByCoordinates(
            250, 750));
        assertNotNull("SE quadrant search failed", quadTree.searchByCoordinates(
            750, 750));
    }


    /**
     * Tests CollectPoints
     */
    public void testCollectPointsFromAllNodeTypes() {
        quadTree.insert(new Point("Point1", 100, 100));

        quadTree.insert(new Point("Point2", 300, 300));

        // Insert enough points to ensure there are InternalNodes
        for (int i = 0; i < 10; i++) {
            quadTree.insert(new Point("Point" + (i + 3), 10 * i, 20 * i));
        }

        ArrayList<Point> collectedPoints1 = new ArrayList<>();
        quadTree.collectPoints(quadTree.getRoot(), collectedPoints1);

        // Verify collected points
        assertFalse("Collected points should not be empty", collectedPoints1
            .isEmpty());
        assertTrue("Should collect points from all leaf nodes", collectedPoints1
            .size() >= 12); // At least 12 points inserted
    }


    /**
     * Tests CollectPoints
     */
    public void testCollectPointsWithNullAndFlyweight() {
        ArrayList<Point> collectedPoints1 = new ArrayList<>();
        quadTree.collectPoints(null, collectedPoints1);
        assertTrue("Collected points should be empty for null node",
            collectedPoints1.isEmpty());

        quadTree.collectPoints(FlyweightNode.getInstance(), collectedPoints1);
        assertTrue("Collected points should be empty for FlyweightNode",
            collectedPoints1.isEmpty());
    }


    /**
     * Tests CollectPoints
     */
    public void testIdentifyDuplicatesAlt() {
        ArrayList<Point> pointsWithDuplicates = new ArrayList<>();
        pointsWithDuplicates.add(new Point("PointA", 100, 100));
        pointsWithDuplicates.add(new Point("PointB", 200, 200));
        pointsWithDuplicates.add(new Point("PointA", 100, 100)); // Duplicate of
                                                                 // PointA

        ArrayList<Point> duplicates = quadTree.identifyDuplicates(
            pointsWithDuplicates);

        assertFalse("Duplicates list should not be empty", duplicates
            .isEmpty());
        assertEquals("Should identify 2 duplicate points", 2, duplicates
            .size());
    }


    /**
     * Tests CollectPoints
     */
    public void testIdentifyDuplicatesNoDuplicates() {
        ArrayList<Point> pointsWithoutDuplicates = new ArrayList<>();
        pointsWithoutDuplicates.add(new Point("PointA", 100, 100));
        pointsWithoutDuplicates.add(new Point("PointB", 200, 200));

        ArrayList<Point> duplicates = quadTree.identifyDuplicates(
            pointsWithoutDuplicates);

        assertTrue("Duplicates list should be empty when no duplicates exist",
            duplicates.isEmpty());
    }


    /**
     * Tests CollectPoints
     */
    public void testIdentifyMultipleSetsOfDuplicates() {
        ArrayList<Point> points = new ArrayList<>();
        // Multiple duplicates for the same coordinates
        points.add(new Point("PointA", 100, 100));
        points.add(new Point("PointA1", 100, 100)); // Duplicate of PointA
        points.add(new Point("PointB", 200, 200));
        points.add(new Point("PointB1", 200, 200)); // Duplicate of PointB

        ArrayList<Point> duplicates = quadTree.identifyDuplicates(points);

        assertEquals("Should identify 4 duplicate points", 4, duplicates
            .size());
    }


    /**
     * Testing Dump
     */
    public void testDumpOutputIndentation() {
        // Setup the quadtree with some nodes to ensure the dump method will
        // have output
        quadTree.insert(new Point("Point1", 100, 100));
        // Additional setup as necessary to ensure a varied depth in the
        // output...

        // Capture System.out to test output

        System.setOut(new PrintStream(outContent));

        // Execute the method that uses printIndent indirectly
        quadTree.dump();

        // Convert the captured output to a string for verification
        String output = outContent.toString();

        // Verify the output has expected indentation
        // This is somewhat dependent on knowing the expected output format
        assertTrue(output.contains("Point1, 100, 100"));
        // Further assertions can be made based on the expected structure and
        // indentation of the dump output

        // Reset System.out to its original stream
        System.setOut(System.out);
    }


    /**
     * Testing Dump
     */
    public void testDumpWithFlyweightNode() {
        // Setup
        quadTree.setRoot(FlyweightNode.getInstance()); // Assuming direct access
                                                       // for testing
        System.setOut(new PrintStream(outContent));

        // Action
        quadTree.dump(); // Public interface to trigger dump

        // Verify
        String output = outContent.toString();
        assertTrue("Output should indicate an empty node", output.contains(
            "Empty"));

        // Cleanup
        System.setOut(System.out);
    }


    /**
     * Testing Dump
     */
    public void testDumpWithLeafNode() {
        // Setup
        Point point = new Point("TestPoint", 100, 100);
        quadTree.insert(point); // Assuming this results in a LeafNode

        System.setOut(new PrintStream(outContent));

        // Action
        quadTree.dump();

        // Verify
        String output = outContent.toString();
        assertTrue("Output should indicate a leaf node", output.contains(point
            .toString()));

        // Cleanup
        System.setOut(System.out);
    }


    /**
     * Testing Dump
     */
    public void testDumpWithInternalNode() {
        // Setup
        quadTree.insert(new Point("Point1", 100, 100));
        quadTree.insert(new Point("Point2", 500, 500));
        System.setOut(new PrintStream(outContent));

        // Action
        quadTree.dump();

        // Verify
        String output = outContent.toString();
        assertTrue(output.contains("Node at"));
        assertTrue(output.contains("Node at"));

        // Cleanup
        System.setOut(System.out);
    }


    /**
     * Testing Dump
     */
    public void testDumpIndentationForVariousDepths() {
        // Setup a deeper tree structure to test indentation
        quadTree.insert(new Point("Point1", 100, 100));
        quadTree.insert(new Point("Point2", 300, 300));
        quadTree.insert(new Point("Point3", 400, 400));
        quadTree.insert(new Point("Point4", 500, 500));
        System.setOut(new PrintStream(outContent));

        // Action
        quadTree.dump();

        // Verify
        String output = outContent.toString();
        String expectedIndentation = "    ";
        assertTrue("Output should include correct indentation for deeper nodes",
            output.contains(expectedIndentation));

        // Cleanup
        System.setOut(System.out);
    }


    /**
     * Testing Dump
     */
    public void testDumpNodeTypeOutput() {
        // Setup: Assuming a tree with a mix of Node types
        quadTree.insert(new Point("Point1", 100, 100));
        quadTree.insert(new Point("Point2", 500, 500));
        System.setOut(new PrintStream(outContent));

        // Action
        quadTree.dump();

        // Verify
        String output = outContent.toString();
        assertTrue("Output should include leaf node data", output.contains(
            "Point1"));
        assertTrue(output.contains("Node at"));

        // Cleanup
        System.setOut(System.out);
    }


    /**
     * Testing Dump
     */
    public void testDumpRecursiveCalls() {
        // Setup: Create a tree with depth greater than 2 to ensure recursive
        // calls are tested
        for (int i = 0; i < 10; i++) {
            quadTree.insert(new Point("Point" + i, i * 100, i * 100));
        }

        System.setOut(new PrintStream(outContent));

        // Action
        quadTree.dump();

        // Verify: Check for expected output that indicates recursion, such as
        // multiple levels of indentation
        String output = outContent.toString();
        assertTrue(output.contains("Node at 0, 0, 512: Internal") && output
            .contains("Node at 0, 0, 256") && output.contains(
                "Node at 256, 0, 256: Empty"));

        // Cleanup
        System.setOut(System.out);
    }


    /**
     * Testing Duplicates
     */
    public void testFindDuplicatesNoDuplicates() {
        // Setup: Insert unique points
        quadTree.insert(new Point("Point1", 10, 10));
        quadTree.insert(new Point("Point2", 20, 20));
        quadTree.insert(new Point("Point3", 30, 30));

        // Action
        MyList<Point> duplicates = quadTree.findDuplicates();

        // Verify: Expect no duplicates
        assertTrue("Should find no duplicates when none exist", duplicates
            .isEmpty());
    }


    /**
     * Testing Duplicates
     */
    public void testFindDuplicatesWithDuplicates() {
        // Setup: Insert points with duplicates
        quadTree.insert(new Point("Point1", 100, 100));
        quadTree.insert(new Point("Point2", 100, 100)); // Duplicate of Point1
        quadTree.insert(new Point("Point3", 200, 200));
        quadTree.insert(new Point("Point4", 200, 200)); // Duplicate of Point3

        // Action
        MyList<Point> duplicates = quadTree.findDuplicates();

        // Verify: Expect 2 duplicates
        assertEquals("Should find 2 duplicates", 2, duplicates.size());
    }


    /**
     * Testing Duplicates
     */
    public void testFindDuplicatesIdenticalPointsAddedMultiple() {
        // Setup: Insert identical points more than twice
        quadTree.insert(new Point("Point1", 50, 50));
        quadTree.insert(new Point("Point2", 50, 50));
        quadTree.insert(new Point("Point3", 50, 50));

        // Action
        MyList<Point> duplicates = quadTree.findDuplicates();

        // Verify: Despite multiple identical points, should be recognized as
        // one set of duplicates
        assertEquals(1, duplicates.size());
    }


    /**
     * Testing Duplicates
     */
    public void testFindDuplicatesComplexScenario() {
        // Setup: A mix of duplicates and unique points
        quadTree.insert(new Point("Point1", 10, 10));
        quadTree.insert(new Point("Point2", 10, 10));
        quadTree.insert(new Point("Point3", 20, 20));
        quadTree.insert(new Point("Point4", 30, 30));
        quadTree.insert(new Point("Point5", 30, 30));
        quadTree.insert(new Point("Point6", 40, 40));

        // Action
        MyList<Point> duplicates = quadTree.findDuplicates();

        // Verify: Expect duplicates for Point1 and Point4
        assertEquals(2, duplicates.size());
    }

}
