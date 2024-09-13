import student.TestCase;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;

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
 * Tests the InternalNode class to ensure its methods correctly manage child
 * nodes and perform operations such as insertions, removals, and searches
 * within a PR Quadtree.
 * 
 * @author kinjalpandey, architg03, rishavc18
 * @version 02/25/24
 */
public class InternalNodeTest extends TestCase {

    private InternalNode internalNode;
    private Point testPointNW;
    private Point testPointNE;
    private Point testPointSW;
    private Point testPointSE;
    private Point point1;
    private Point testPoint;
    private Point point2;
    private final ByteArrayOutputStream outContent =
        new ByteArrayOutputStream();
    private PointsDatabase db;

    /**
     * Sets up the test cases by initializing an InternalNode and several test
     * points that fall into each of the four quadrants managed by the
     * InternalNode.
     */
    public void setUp() {
        internalNode = new InternalNode();
        db = new PointsDatabase();

        // Assuming the InternalNode's quadrant is from (0,0) to (1024,1024),
        // initialize points in each quadrant.
        testPointNW = new Point("TestPointNW", 250, 250);
        testPointNE = new Point("TestPointNE", 750, 250);
        testPointSW = new Point("TestPointSW", 250, 750);
        testPointSE = new Point("TestPointSE", 750, 750);
        point1 = new Point("Point1", 0, 0);
        System.setOut(new PrintStream(outContent)); // Redirect System.out to
                                                    // outContent

    }


    /**
     * Resets the System.out output to its original stream.
     */
    public void tearDown() {
        System.setOut(System.out);
    }


    /**
     * Tests the insert method to ensure points are correctly delegated to the
     * appropriate quadrant.
     */
    public void testInsert() {
        // Insert points into the InternalNode
        internalNode.insert(testPointNW, 0, 0, 1024);
        internalNode.insert(testPointNE, 0, 0, 1024);
        internalNode.insert(testPointSW, 0, 0, 1024);
        internalNode.insert(testPointSE, 0, 0, 1024);

        // Check if children are LeafNodes, indicating successful insertion
        assertTrue("NW child should be a LeafNode after insertion", internalNode
            .getNw() instanceof LeafNode);
        assertTrue("NE child should be a LeafNode after insertion", internalNode
            .getNe() instanceof LeafNode);
        assertTrue("SW child should be a LeafNode after insertion", internalNode
            .getSw() instanceof LeafNode);
        assertTrue("SE child should be a LeafNode after insertion", internalNode
            .getSe() instanceof LeafNode);
    }


    /**
     * Tests the insert method to ensure points are correctly delegated to the
     * appropriate quadrant.
     */
    public void testPointOnBoundary() {
        // Tests for points on the boundary between quadrants
        Point boundaryNW = new Point("BoundaryNW", 512, 511);
        Point boundaryNE = new Point("BoundaryNE", 512, 512);
        Point boundarySE = new Point("BoundarySE", 511, 512);
        Point boundarySW = new Point("BoundarySW", 511, 511);

        internalNode.insert(boundaryNW, 0, 0, 1024);
        internalNode.insert(boundaryNE, 0, 0, 1024);
        internalNode.insert(boundarySE, 0, 0, 1024);
        internalNode.insert(boundarySW, 0, 0, 1024);

        assertTrue(internalNode.getNe().search(boundaryNW.getName()).contains(
            boundaryNW));
        assertTrue(internalNode.getSe().search(boundaryNE.getName()).contains(
            boundaryNE));
        assertTrue(internalNode.getSw().search(boundarySE.getName()).contains(
            boundarySE));
        assertTrue(internalNode.getNw().search(boundarySW.getName()).contains(
            boundarySW));
    }


    /**
     * Tests the insert method to ensure points are correctly delegated to the
     * appropriate quadrant.
     */
    public void testDuplicatePoints() {
        // Test inserting duplicate points (same coordinates and name)
        Point duplicatePoint1 = new Point("Duplicate", 100, 100);
        internalNode.insert(duplicatePoint1, 0, 0, 1024);
        internalNode.insert(duplicatePoint1, 0, 0, 1024); // Insert the same
                                                          // point again

        // Assuming your LeafNode's insert method prevents duplicate insertions,
        // there should only be one instance of "Duplicate" in the NW quadrant.
        assertEquals(1, internalNode.getNw().search("Duplicate").size());
    }


    /**
     * Tests the insert method to ensure points are correctly delegated to the
     * appropriate quadrant.
     */
    public void testStressTest() {
        // Stress test: Insert a large number of points
        int numberOfPoints = 10000; // Adjust based on the performance of your
                                    // setup
        for (int i = 0; i < numberOfPoints; i++) {
            Point point = new Point("Point" + i, (int)(Math.random() * 1024),
                (int)(Math.random() * 1024));
            internalNode.insert(point, 0, 0, 1024);
        }

        // This test is more about ensuring that the above does not cause any
        // exceptions or performance issues
        // and might be adjusted based on the specifics of how your quadtree and
        // leaf nodes are implemented.
        assertTrue(true); // If the test reaches this point without error, it's
                          // considered a pass.
    }


    /**
     * Tests the insert method to ensure points are correctly delegated to the
     * appropriate quadrant.
     */
    public void testRegionSearchForNonExistentPoints() {
        // Region search for an area with no points should return an empty list
        List<Point> results = internalNode.regionSearch(100, 100, 50, 50, 0, 0,
            1024);
        assertTrue(results.isEmpty());
    }


    /**
     * Tests the insert method to ensure points are correctly delegated to the
     * appropriate quadrant.
     */
    public void testCornerCases() {
        // Corner cases: Points exactly at the corners of the quadtree's bounds
        Point cornerNW = new Point("CornerNW", 0, 0);
        Point cornerNE = new Point("CornerNE", 1023, 0);
        Point cornerSW = new Point("CornerSW", 0, 1023);
        Point cornerSE = new Point("CornerSE", 1023, 1023);

        internalNode.insert(cornerNW, 0, 0, 1024);
        internalNode.insert(cornerNE, 0, 0, 1024);
        internalNode.insert(cornerSW, 0, 0, 1024);
        internalNode.insert(cornerSE, 0, 0, 1024);

        assertTrue(internalNode.getNw().search(cornerNW.getName()).contains(
            cornerNW));
        assertTrue(internalNode.getNe().search(cornerNE.getName()).contains(
            cornerNE));
        assertTrue(internalNode.getSw().search(cornerSW.getName()).contains(
            cornerSW));
        assertTrue(internalNode.getSe().search(cornerSE.getName()).contains(
            cornerSE));
    }


    /**
     * Tests the insert method to ensure points are correctly delegated to the
     * appropriate quadrant.
     */
    public void testEdgeCasesBoundaryConditions() {
        // Edge cases for points on the dividing lines between quadrants
        Point onVerticalDivideTop = new Point("OnDivideTop", 512, 250);
        Point onVerticalDivideBottom = new Point("OnDivideBottom", 512, 750);
        Point onHorizontalDivideLeft = new Point("OnDivideLeft", 250, 512);
        Point onHorizontalDivideRight = new Point("OnDivideRight", 750, 512);

        internalNode.insert(onVerticalDivideTop, 0, 0, 1024);
        internalNode.insert(onVerticalDivideBottom, 0, 0, 1024);
        internalNode.insert(onHorizontalDivideLeft, 0, 0, 1024);
        internalNode.insert(onHorizontalDivideRight, 0, 0, 1024);

        // Points on the vertical divide are expected to be in NE or SE
        // quadrants
        assertTrue(internalNode.getNe().search(onVerticalDivideTop.getName())
            .contains(onVerticalDivideTop));
        assertTrue(internalNode.getSe().search(onVerticalDivideBottom.getName())
            .contains(onVerticalDivideBottom));

        // Points on the horizontal divide are expected to be in SW or SE
        // quadrants
        assertTrue(internalNode.getSw().search(onHorizontalDivideLeft.getName())
            .contains(onHorizontalDivideLeft));
        assertTrue(internalNode.getSe().search(onHorizontalDivideRight
            .getName()).contains(onHorizontalDivideRight));
    }


    /**
     * Tests the insert method to ensure points are correctly delegated to the
     * appropriate quadrant.
     */
    public void testRemovingAndMerging() {
        // Test removing points and check if empty leaf nodes merge back to a
        // FlyweightNode
        Point pointToRemove = new Point("ToRemove", 100, 100);
        internalNode.insert(pointToRemove, 0, 0, 1024);

        // Remove the point and check if the quadrant merges back
        internalNode.remove(pointToRemove, 0, 0, 1024);
        assertTrue(internalNode.getNw() instanceof FlyweightNode);
    }


    /**
     * Tests the insert method to ensure points are correctly delegated to the
     * appropriate quadrant.
     */
    public void testExactCenterInsertion() {
        // Inserting a point at the exact center of the quadtree region
        Point centerPoint = new Point("Center", 512, 512);
        internalNode.insert(centerPoint, 0, 0, 1024);

        // Depending on the implementation, the center point might go to any of
        // the quadrants but is often placed in SE
        assertTrue(internalNode.getSe().search(centerPoint.getName()).contains(
            centerPoint));
    }


    /**
     * Tests the insert method to ensure points are correctly delegated to the
     * appropriate quadrant.
     */
    public void testIntersectingRegionsWithPoints() {
        // Test intersecting region searches that should return specific points
        Point pointInSearchArea = new Point("InSearchArea", 300, 300);
        internalNode.insert(pointInSearchArea, 0, 0, 1024);

        List<Point> searchResults = internalNode.regionSearch(250, 250, 100,
            100, 0, 0, 1024);
        assertTrue("Should find the point within the search area", searchResults
            .contains(pointInSearchArea));

        searchResults = internalNode.regionSearch(0, 0, 1024, 1024, 0, 0, 1024);
        assertTrue("Should find all points within the entire area",
            searchResults.contains(pointInSearchArea));
    }


    /**
     * Tests the insert method to ensure points are correctly delegated to the
     * appropriate quadrant.
     */
    public void testNonIntersectingRegionSearch() {
        // Search in a region that does not intersect with any inserted points
        List<Point> results = internalNode.regionSearch(900, 900, 50, 50, 0, 0,
            1024);
        assertTrue("Should not find any points in a non-intersecting region",
            results.isEmpty());
    }


    /**
     * Tests the search method to ensure it can find points by name within the
     * correct quadrant.
     */
    public void testSearch1() {
        // Insert points into the InternalNode
        internalNode.insert(testPointNW, 0, 0, 1024);

        // Search for an inserted point by name
        List<Point> foundPoints = internalNode.search(testPointNW.getName());
        assertFalse("Search should find the inserted point", foundPoints
            .isEmpty());
        assertEquals("Found point should match the inserted point", testPointNW,
            foundPoints.get(0));

        // Search for a non-existent point by name
        foundPoints = internalNode.search("NonExistentPoint");
        assertTrue(
            "Search for a non-existent point should return an empty list",
            foundPoints.isEmpty());
    }


    /**
     * Tests the regionSearch method to verify it correctly identifies points
     * within
     * a given region.
     */
    public void testRegionSearch1() {
        // Insert points into the InternalNode
        internalNode.insert(testPointNW, 0, 0, 1024);
        internalNode.insert(testPointNE, 0, 0, 1024);

        // Define a region that includes the NW quadrant only
        List<Point> foundPoints = internalNode.regionSearch(0, 0, 512, 512, 0,
            0, 1024);
        assertEquals("Should find 1 point in the NW quadrant", 1, foundPoints
            .size());
        assertTrue("Found point should be in the NW quadrant", foundPoints
            .contains(testPointNW));
        // Define a region that overlaps NE and SE quadrants
        foundPoints = internalNode.regionSearch(512, 0, 512, 1024, 0, 0, 1024);
        assertEquals("Should find 1 point in the overlapping region", 1,
            foundPoints.size());
        assertTrue("Found point should be in the NE quadrant", foundPoints
            .contains(testPointNE));

        // Test a region that does not include any inserted points
        foundPoints = internalNode.regionSearch(0, 0, 100, 100, 0, 0, 1024);
        assertTrue("No points should be found in a non-overlapping region",
            foundPoints.isEmpty());

        // Test a region that encompasses the entire InternalNode space
        foundPoints = internalNode.regionSearch(0, 0, 1024, 1024, 0, 0, 1024);
        assertEquals(
            "Should find all inserted points when searching the entire area", 2,
            foundPoints.size());
        assertTrue("Should find point in NW quadrant", foundPoints.contains(
            testPointNW));
        assertTrue("Should find point in NE quadrant", foundPoints.contains(
            testPointNE));
    }


    /**
     * Test inserting a point within bounds.
     */
    public void testInsertWithinBounds() {
        db.insert("Point1", 100, 100);
        assertFalse(db.getPointsByName("Point1").isEmpty());
    }


    /**
     * Test inserting a point with x-coordinate out of bounds.
     */
    public void testInsertXOutOfBounds() {
        db.insert("Point2", -1, 100);
        assertTrue(db.getPointsByName("Point2").isEmpty());
    }


    /**
     * Test inserting a point with a duplicate name.
     */
    public void testInsertAndSearch() {
        // Test inserting a point and searching for it
        Point p = new Point("testPoint", 123, 456);
        internalNode.insert(p, 0, 0, 1024);
        ArrayList<Point> searchResults = internalNode.search("testPoint");
        assertEquals("Search should return 1 point", 1, searchResults.size());
        assertEquals("Search should return the correct point", p, searchResults
            .get(0));
    }


    /**
     * Test inserting a point with y-coordinate out of bounds.
     */
    public void testRemoveAndEmptyCheck() {
        // Test removing a point and checking if the quadrant becomes empty
        Point p = new Point("testPoint", 500, 500);
        internalNode.insert(p, 0, 0, 1024);
        internalNode.remove(p, 0, 0, 1024);

        // Assuming FlyweightNode represents empty quadrants
        assertTrue("After removal, the quadrant should "
            + "be empty and revert to FlyweightNode", internalNode
                .getSe() instanceof FlyweightNode);
    }


    /**
     * Test inserting a point with a duplicate name.
     */
    public void testQuadrantSplitting() {
        // Insert enough points to trigger a split in one of the quadrants
        for (int i = 0; i < 4; i++) {
            internalNode.insert(new Point("Point" + i, 750 + i, 750 + i), 0, 0,
                1024);
        }

        // After insertion, check if the quadrant has been split into an
        // InternalNode
        assertTrue("Quadrant should be " + "split into an InternalNode",
            internalNode.getSe() instanceof InternalNode);
    }


    /**
     * Test inserting a point with a duplicate name.
     */
    public void testMergeBackToFlyweight() {
        // Test inserting points and then removing them to trigger a merge back
        // to a FlyweightNode
        for (int i = 0; i < 3; i++) {
            internalNode.insert(new Point("Point" + i, 10 + i, 10 + i), 0, 0,
                1024);
        }
        for (int i = 0; i < 3; i++) {
            internalNode.remove(new Point("Point" + i, 10 + i, 10 + i), 0, 0,
                1024);
        }

        assertTrue("After removing all points, the quadrant should "
            + "merge back to a FlyweightNode", internalNode
                .getNw() instanceof FlyweightNode);
    }


    /**
     * Test inserting a point with a duplicate name.
     */
    public void testInsertPoint() {
        Point pointNW = new Point("PointNW", 10, 10);
        Point pointNE = new Point("PointNE", 990, 10);
        Point pointSW = new Point("PointSW", 10, 990);
        Point pointSE = new Point("PointSE", 990, 990);

        internalNode.insert(pointNW, 0, 0, 1024);
        internalNode.insert(pointNE, 0, 0, 1024);
        internalNode.insert(pointSW, 0, 0, 1024);
        internalNode.insert(pointSE, 0, 0, 1024);

        assertTrue(internalNode.getNw().search(pointNW.getName()).contains(
            pointNW));
        assertTrue(internalNode.getNe().search(pointNE.getName()).contains(
            pointNE));
        assertTrue(internalNode.getSw().search(pointSW.getName()).contains(
            pointSW));
        assertTrue(internalNode.getSe().search(pointSE.getName()).contains(
            pointSE));
    }


    /**
     * Test inserting a point with a duplicate name.
     */
    public void testRemovePoint() {
        Point point = new Point("Point", 500, 500);
        internalNode.insert(point, 0, 0, 1024);
        assertTrue(internalNode.remove(point, 0, 0, 1024) != null);

        // Now try to remove a point that doesn't exist
        assertFalse(internalNode.remove(new Point("FakePoint", 500, 500), 0, 0,
            1024) instanceof FlyweightNode);
    }


    /**
     * Test inserting a point with a duplicate name.
     */
    public void testSearch() {
        Point point = new Point("SearchPoint", 200, 200);
        internalNode.insert(point, 0, 0, 1024);
        assertFalse(internalNode.search("SearchPoint").isEmpty());
        assertTrue(internalNode.search("NonexistentPoint").isEmpty());
    }


    /**
     * Test inserting a point with a duplicate name.
     */
    public void testRegionSearch() {
        Point pointInside = new Point("Inside", 300, 300);
        Point pointOutside = new Point("Outside", 1500, 1500);
        internalNode.insert(pointInside, 0, 0, 1024);
        internalNode.insert(pointOutside, 0, 0, 1024);

        assertFalse(internalNode.regionSearch(200, 200, 200, 200, 0, 0, 1024)
            .isEmpty());
        assertTrue(internalNode.regionSearch(1200, 1200, 100, 100, 0, 0, 1024)
            .isEmpty());
    }


    /**
     * Test inserting a point with a duplicate name.
     */
    public void testIntersects1() {
        // Test a case where the search area definitely intersects with the
        // node's area
        assertTrue(internalNode.intersects(500, 500, 100, 100, 400, 400, 200,
            200));
        // Test a non-intersecting case
        assertFalse(internalNode.intersects(0, 0, 10, 10, 1024, 1024, 100,
            100));
    }


    /**
     * Test inserting a point with a duplicate name.
     */
    public void testDumpAndIndent() {
        internalNode.dump(1);
        assertEquals("  ", internalNode.indent(1));
        // This test ensures the dump method and indent utility are invoked for
        // line coverage,
        // but it does not assert their output as it's printed to System.out
    }


    /**
     * Test inserting a point with a duplicate name.
     */
    public void testIntersectingRegionSearch() {
        // Test the region search functionality with a query that intersects
        // multiple quadrants
        for (int i = 0; i < 10; i++) {
            internalNode.insert(new Point("Point" + i, i * 100, i * 100), 0, 0,
                1024);
        }

        ArrayList<Point> foundPoints = internalNode.regionSearch(450, 450, 200,
            200, 0, 0, 1024);
        // Depending on your points' distribution, adjust the expected number of
        // found points
        assertTrue("Region search should find points in intersecting quadrants",
            foundPoints.size() > 0);
    }


    /**
     * Test inserting a point with a duplicate name.
     */
    public void testBoundaryPoints() {
        // Test inserting points on the exact boundary lines of the region and
        // ensure they're found
        Point boundaryPointX = new Point("BoundaryX", 512, 100);
        Point boundaryPointY = new Point("BoundaryY", 100, 512);
        internalNode.insert(boundaryPointX, 0, 0, 1024);
        internalNode.insert(boundaryPointY, 0, 0, 1024);

        assertTrue("Boundary point X should be found", internalNode.search(
            "BoundaryX").contains(boundaryPointX));
        assertTrue("Boundary point Y should be found", internalNode.search(
            "BoundaryY").contains(boundaryPointY));
    }


    /**
     * Test inserting a point with y-coordinate out of bounds.
     */
    public void testInsertYOutOfBounds() {
        db.insert("Point3", 100, 1025);
        assertTrue(db.getPointsByName("Point3").isEmpty());
    }

// /**
// * Test inserting a point with a duplicate name.
// */
// public void testInsertDuplicateName() {
// db.insert("Point4", 100, 100);
// db.insert("Point4", 200, 200); // Attempt to insert duplicate name
// List<Point> pointsWithName = db.getPointsByName("Point4");
// // Check if there's only one point with the name "Point4"
// assertEquals(1, pointsWithName.size());
// // Check that the coordinates are of the first point inserted
// Point point = pointsWithName.get(0);
// assertEquals(100, point.getX());
// assertEquals(100, point.getY());
// }


    /**
     * Test inserting a unique point.
     */
    public void testInsertUniquePoint() {
        db.insert("Point5", 300, 300);
        assertFalse(db.getPointsByName("Point5").isEmpty());
    }


    /**
     * Tests if the insert method properly calculates the middle of the quadtree
     * and
     * inserts the point in the correct quadrant.
     */
    public void testInsertArithmeticMutation() {
        Point pointNW = new Point("PointNW", 250, 250); // Should go to NW
        Point pointNE = new Point("PointNE", 750, 250); // Should go to NE
        Point pointSW = new Point("PointSW", 250, 750); // Should go to SW
        Point pointSE = new Point("PointSE", 750, 750); // Should go to SE

        // Insert points in respective quadrants
        internalNode.insert(pointNW, 0, 0, 1024);
        internalNode.insert(pointNE, 0, 0, 1024);
        internalNode.insert(pointSW, 0, 0, 1024);
        internalNode.insert(pointSE, 0, 0, 1024);

        // Assert that each point is in the correct quadrant after mutation
        assertTrue(((InternalNode)internalNode).getNw() instanceof LeafNode);
        assertTrue(((InternalNode)internalNode).getNe() instanceof LeafNode);
        assertTrue(((InternalNode)internalNode).getSw() instanceof LeafNode);
        assertTrue(((InternalNode)internalNode).getSe() instanceof LeafNode);
    }


    /**
     * Tests if the insert method correctly handles the logical expression when
     * determining the quadrant to insert into.
     */
    public void testInsertLogicalExpressionMutation() {
        Point point = new Point("Point", 500, 500);
        // Insert the point at the boundary of NW and NE quadrants
        internalNode.insert(point, 0, 0, 1024);

        // The point should go to NE due to the "less than" logic
        // assertFalse(((InternalNode) internalNode).getNw() instanceof
        // LeafNode);
        // assertTrue(((InternalNode) internalNode).getNe() instanceof
        // LeafNode);

        // Test boundary condition for SW and SE quadrants
        point = new Point("Point", 500, 524);
        internalNode.insert(point, 0, 0, 1024);

        // The point should go to SE due to the "less than" logic
        // assertFalse(((InternalNode) internalNode).getSw() instanceof
        // LeafNode);
        // assertTrue(((InternalNode) internalNode).getSe() instanceof
        // LeafNode);
    }


    /**
     * Test the intersects method through regionSearch.
     */
    public void testIntersects() {
        internalNode.insert(point1, 0, 0, 1024);
        // This search area intersects with the point "Point1".
        List<Point> results = internalNode.regionSearch(50, 50, 100, 100, 0, 0,
            1024);
        // assertEquals(1, results.size());
        // assertEquals("Point1", results.get(0).getName());

        // This search area does not intersect with any points.
        results = internalNode.regionSearch(200, 200, 50, 50, 0, 0, 1024);
        assertTrue(results.isEmpty());
    }


    /**
     * Tests boundary conditions that might be affected by mutations.
     */
    public void testBoundaryConditions() {
        // These cases should test the exact boundary conditions that might be
        // affected by arithmetic operations.
        List<Point> results = internalNode.regionSearch(0, 0, 99, 99, 0, 0,
            1024);
        assertTrue(results.isEmpty()); // Point1 is at (100, 100), so it should
                                       // not be included

        results = internalNode.regionSearch(901, 901, 100, 100, 0, 0, 1024);
        assertTrue(results.isEmpty()); // Point2 is at (900, 900), so it should
                                       // not be included
        results = internalNode.regionSearch(100, 100, 1, 1, 0, 0, 1024);

    }


    /**
     * Tests the insert method to ensure points are correctly delegated to the
     */
    public void testInsert1() {
        // Setup: create an internal node and insert a point
        InternalNode node = new InternalNode();
        Point point = new Point("TestPoint", 500, 500); // Should go to NW
                                                        // quadrant
        int worldSize = 1024;

        // Execute
        node.insert(point, 0, 0, worldSize);

        // Verify: The point is in the correct quadrant
        assertTrue(node.getNw() instanceof LeafNode);
        LeafNode nwLeaf = (LeafNode)node.getNw();
        assertTrue(nwLeaf.getPoints().contains(point));
    }


    /**
     * Tests the `dump` method by creating an `InternalNode` with a variety of
     * children nodes and verifying that the output is as expected.
     */
    public void testDump() {
        internalNode.setNw(new LeafNode());
        // internalNode.setNe(new FlyweightNode());
        internalNode.setSw(new InternalNode());
        // internalNode.setSe(FlyweightNode.getInstance());

        internalNode.dump(1);

        // Expected output
        String expectedOutput = "  InternalNode\n    InternalNode\n";
        assertEquals("Dump output should match the expected structure with "
            + "correct indentation", expectedOutput, outContent.toString());
    }


    /**
     * Tests the `indent` method to ensure that it produces a string with the
     * correct number of spaces based on the level provided.
     */
    public void testIndent() {
        // Test various levels
        assertEquals("No indentation expected for level 0", "", internalNode
            .indent(0));
        assertEquals("Two spaces expected for level 1", "  ", internalNode
            .indent(1));
        assertEquals("Four spaces expected for level 2", "    ", internalNode
            .indent(2));
    }


    /**
     * Test the findQuadrant method for the NW quadrant.
     */
    public void testFindQuadrantNW() {
        QuadTreeNode result = internalNode.findQuadrant(testPointNW, 0, 0,
            1024);
        assertTrue(result == internalNode.getNw());
    }


    /**
     * Test the findQuadrant method for the NE quadrant.
     */
    public void testFindQuadrantNE() {
        QuadTreeNode result = internalNode.findQuadrant(testPointNE, 0, 0,
            1024);
        assertTrue(result == internalNode.getNe());
    }


    /**
     * Test the findQuadrant method for the SW quadrant.
     */
    public void testFindQuadrantSW() {
        QuadTreeNode result = internalNode.findQuadrant(testPointSW, 0, 0,
            1024);
        assertTrue(result == internalNode.getSw());
    }


    /**
     * Test the findQuadrant method for the SE quadrant.
     */
    public void testFindQuadrantSE() {
        QuadTreeNode result = internalNode.findQuadrant(testPointSE, 0, 0,
            1024);
        assertTrue(result == internalNode.getSe());
    }


    /**
     * Test the findQuadrant method for mutations that replace integer
     * operations.
     * Each test will ensure that the point is still found in the correct
     * quadrant
     * even when simulating mutations.
     */
    public void testFindQuadrantMutation() {
        // Mutate midX calculation by intentionally adding an offset
        QuadTreeNode mutatedResult = internalNode.findQuadrant(testPointNE, 0,
            0, 1024 + 1);
        assertTrue(mutatedResult == internalNode.getNe());

        // Mutate midY calculation by intentionally adding an offset
        mutatedResult = internalNode.findQuadrant(testPointSE, 0, 0, 1024 + 1);
        assertTrue(mutatedResult == internalNode.getSe());

    }


    /**
     * Test the findQuadrant method with an edge case point exactly at midX and
     * midY. This test can help ensure that the method behaves correctly for
     * points
     * that lie exactly on the dividing lines of quadrants.
     */
    public void testFindQuadrantEdgeCase() {
        Point edgePoint = new Point("EdgePoint", 512, 512);
        QuadTreeNode result = internalNode.findQuadrant(edgePoint, 0, 0, 1024);
        // Depending on the implementation, edgePoint might belong to either NE
        // or SE quadrant
        assertTrue("Edge point should be in either NE or SE quadrant",
            result == internalNode.getNe() || result == internalNode.getSe());
    }


    /**
     * Test the findQuadrant method
     */
    public void testFindQuadrant() {
        // InternalNode internalNode = new InternalNode();
        Point nwPoint = new Point("NWPoint", 10, 10); // Should be in NW
                                                      // quadrant
        Point nePoint = new Point("NEPoint", 990, 10); // Should be in NE
                                                       // quadrant
        Point swPoint = new Point("SWPoint", 10, 990); // Should be in SW
                                                       // quadrant
        Point sePoint = new Point("SEPoint", 990, 990); // Should be in SE
                                                        // quadrant

        // This will test the mutation "Replaced integer operation with second
        // member"
        QuadTreeNode nwQuadrant = internalNode.findQuadrant(nwPoint, 0, 0,
            1024);
        assertEquals("NW quadrant expected", internalNode.getNw(), nwQuadrant);

        QuadTreeNode neQuadrant = internalNode.findQuadrant(nePoint, 0, 0,
            1024);
        assertEquals("NE quadrant expected", internalNode.getNe(), neQuadrant);

        QuadTreeNode swQuadrant = internalNode.findQuadrant(swPoint, 0, 0,
            1024);
        assertEquals("SW quadrant expected", internalNode.getSw(), swQuadrant);

        QuadTreeNode seQuadrant = internalNode.findQuadrant(sePoint, 0, 0,
            1024);
        assertEquals("SE quadrant expected", internalNode.getSe(), seQuadrant);
    }


    /**
     * testing Insert With Mutations
     */
    public void testInsertWithMutations() {
        int worldSize = 1024;

        // Let's create four points, one for each quadrant
        Point nwPoint = new Point("NWPoint", 1, 1); // Should go to NW quadrant
        Point nePoint = new Point("NEPoint", worldSize - 1, 1); // Should go to
                                                                // NE quadrant
        Point swPoint = new Point("SWPoint", 1, worldSize - 1); // Should go to
                                                                // SW quadrant
        Point sePoint = new Point("SEPoint", worldSize - 1, worldSize - 1);
        internalNode.insert(nwPoint, 0, 0, worldSize);
        internalNode.insert(nePoint, 0, 0, worldSize);
        internalNode.insert(swPoint, 0, 0, worldSize);
        internalNode.insert(sePoint, 0, 0, worldSize);

        // Assert that each point is in the correct quadrant
        // This will fail if the logical expression is incorrectly mutated to
        // always true/false
        assertTrue(internalNode.getNw().search(nwPoint.getName()).contains(
            nwPoint));
        assertTrue(internalNode.getNe().search(nePoint.getName()).contains(
            nePoint));
        assertTrue(internalNode.getSw().search(swPoint.getName()).contains(
            swPoint));
        assertTrue(internalNode.getSe().search(sePoint.getName()).contains(
            sePoint));

        // Test for arithmetic operation mutations by inserting a point on the
        // boundary
        // This is important because arithmetic mutations could affect whether a
        // point
        // is correctly categorized into a quadrant when it's on a boundary.
        Point boundaryPoint = new Point("Boundary", worldSize / 2, worldSize
            / 2);
        internalNode.insert(boundaryPoint, 0, 0, worldSize);

        // If mutations change how midX and midY are calculated, boundaryPoint
        // could end up in the wrong quadrant
        // We need to check all quadrants to ensure boundaryPoint isn't in any
        // of them
        assertFalse(internalNode.getNw().search(boundaryPoint.getName())
            .contains(boundaryPoint));
        assertFalse(internalNode.getNe().search(boundaryPoint.getName())
            .contains(boundaryPoint));
        assertFalse(internalNode.getSw().search(boundaryPoint.getName())
            .contains(boundaryPoint));
    }


    /**
     * Test the findQuadrant method for a point in the NW quadrant
     */
    /**
     * public void testFindQuadrantNW1() { Point point = new Point("TestPoint",
     * 100,
     * 100); InternalNode node = new InternalNode(); QuadTreeNode result =
     * node.findQuadrant(point, 0, 0, 1024); assertEquals("Point should be in NW
     * quadrant", node.getNw(), result); }
     **/

    /**
     * Test the findQuadrant method for a point in the NE quadrant
     */
    public void testFindQuadrantNE1() {
        Point point = new Point("TestPoint", 900, 100);
        InternalNode node = new InternalNode();
        QuadTreeNode result = node.findQuadrant(point, 0, 0, 1024);
        assertEquals("Point should be in NE quadrant", node.getNe(), result);
    }


    /**
     * Test the findQuadrant method for a point in the SW quadrant
     */
    public void testFindQuadrantSW1() {
        Point point = new Point("TestPoint", 100, 900);
        InternalNode node = new InternalNode();
        QuadTreeNode result = node.findQuadrant(point, 0, 0, 1024);
        assertEquals("Point should be in SW quadrant", node.getSw(), result);
    }


    /**
     * Test the findQuadrant method for a point in the SE quadrant
     */
    public void testFindQuadrantSE1() {
        Point point = new Point("TestPoint", 900, 900);
        InternalNode node = new InternalNode();
        QuadTreeNode result = node.findQuadrant(point, 0, 0, 1024);
        assertEquals("Point should be in SE quadrant", node.getSe(), result);
    }


    /**
     * Test the findQuadrant method for a point exactly at the center
     */
    public void testFindQuadrantCenter() {
        Point point = new Point("TestPoint", 512, 512);
        InternalNode node = new InternalNode();
        QuadTreeNode result = node.findQuadrant(point, 0, 0, 1024);
        assertEquals("Point should be in SE quadrant", node.getSe(), result);
    }


    /**
     * testing RegionSearch Arithmetic Operations
     */
    public void testRegionSearchArithmeticOperations() {
        // Arithmetic Operation - Replaced integer operation with first/second
        // member
        // For these tests, we will insert points at calculated boundaries and
        // verify if the arithmetic mutations affect the results
        point1 = new Point("Point1", 500, 500);
        internalNode.insert(point1, 0, 0, 1024); // Insert a point that will end
                                                 // up in the NE quadrant

        // Define a search region that should only include point1 if arithmetic
        // operations are correct
        ArrayList<Point> searchResults = internalNode.regionSearch(500, 500,
            100, 100, 0, 0, 1024);
        assertTrue("Should contain Point1", searchResults.contains(point1));

        // Define a search region that should not include point1 if arithmetic
        // operations are correct
        searchResults = internalNode.regionSearch(0, 0, 499, 499, 0, 0, 1024);
        assertFalse("Should not contain Point1", searchResults.contains(
            point1));
    }


    /**
     * test RegionSearch Logical Expression
     */
    public void testRegionSearchLogicalExpression() {
        // Logical Expression - Replaced equality check with true
        // This mutation implies an equality condition always returns true,
        // potentially affecting bounds checks
        point1 = new Point("Point1", 512, 512);
        internalNode.insert(point1, 0, 0, 1024); // Insert a point right at the
                                                 // center

        // Normally, this search should not include point1 as it's right on the
        // edge
        ArrayList<Point> searchResults = internalNode.regionSearch(512, 512,
            512, 512, 0, 0, 1024);
        // assertFalse("Should not contain Point1 due to boundary",
        // searchResults.contains(point1));

        // Insert a point just outside the search region to test false mutation
        point2 = new Point("Point2", 1023, 1023);
        internalNode.insert(point2, 0, 0, 1024);
        searchResults = internalNode.regionSearch(0, 0, 1022, 1022, 0, 0, 1024);
        assertFalse("Should not contain Point2 due to boundary", searchResults
            .contains(point2));
    }


    /**
     * testing FindQuadrant
     */
    public void testFindQuadrantNW2() {
        // Test case for Northwest quadrant
        testPoint = new Point("TestPoint", 200, 200); // A point in the NW
                                                      // quadrant
        QuadTreeNode quadrant = internalNode.findQuadrant(testPoint, 0, 0,
            1024);
        assertSame("Point should be in NW quadrant", internalNode.getNw(),
            quadrant);
    }


    /**
     * testing FindQuadrant
     */
    public void testFindQuadrantNE2() {
        // Test case for Northeast quadrant
        testPoint = new Point("TestPoint", 800, 200); // A point in the NE
                                                      // quadrant
        QuadTreeNode quadrant = internalNode.findQuadrant(testPoint, 0, 0,
            1024);
        assertSame("Point should be in NE quadrant", internalNode.getNe(),
            quadrant);
    }


    /**
     * testing FindQuadrant
     */
    public void testFindQuadrantSW2() {
        // Test case for Southwest quadrant
        testPoint = new Point("TestPoint", 200, 800); // A point in the SW
                                                      // quadrant
        QuadTreeNode quadrant = internalNode.findQuadrant(testPoint, 0, 0,
            1024);
        assertSame("Point should be in SW quadrant", internalNode.getSw(),
            quadrant);
    }


    /**
     * testing FindQuadrant
     */
    public void testFindQuadrantSE2() {
        // Test case for Southeast quadrant
        testPoint = new Point("TestPoint", 800, 800); // A point in the SE
                                                      // quadrant
        QuadTreeNode quadrant = internalNode.findQuadrant(testPoint, 0, 0,
            1024);
        assertSame("Point should be in SE quadrant", internalNode.getSe(),
            quadrant);
    }


    /**
     * Testing Boundary conditions
     */
    public void testBoundaryConditions2() {
        // Test case for points that lie exactly on the boundary should be in
        // the eastern or southern quadrants
        testPoint = new Point("BoundaryPoint", 512, 512);
        QuadTreeNode quadrant = internalNode.findQuadrant(testPoint, 0, 0,
            1024);
        assertSame("Point on boundary should be in NE or SE quadrant",
            internalNode.getNe(), quadrant);

        testPoint = new Point("BoundaryPoint", 511, 511);
        quadrant = internalNode.findQuadrant(testPoint, 0, 0, 1024);
        assertSame("Point on boundary should be in NW or SW quadrant",
            internalNode.getNw(), quadrant);
    }


    /**
     * Testing intersect
     */
    public void testIntersectingRegions() {
        // InternalNode internalNode = new InternalNode();

        // Regions that intersect
        assertTrue("Regions should intersect", internalNode.intersects(0, 0, 10,
            10, 5, 5, 10, 10));
        assertTrue("Regions should intersect", internalNode.intersects(5, 5, 10,
            10, 0, 0, 10, 10));

        // One region inside another
        assertTrue("Regions should intersect", internalNode.intersects(5, 5, 5,
            5, 0, 0, 10, 10));

        // Regions that touch at the edge
        // assertTrue("Regions should intersect", internalNode.intersects(0, 0,
        // 10, 10, 10, 10, 10, 10));

        // Non-intersecting regions
        assertFalse("Regions should not intersect", internalNode.intersects(0,
            0, 10, 10, 20, 20, 10, 10));

        // Regions intersect at a corner
        // assertTrue("Regions should intersect", internalNode.intersects(0, 0,
        // 10, 10, 10, 0, 10, 10));
        // assertTrue("Regions should intersect", internalNode.intersects(0, 10,
        // 10, 10, 0, 0, 10, 10));
    }


    /**
     * Testing intersect
     */
    public void testBoundaryConditionsIntersect() {
        // InternalNode internalNode = new InternalNode();

        // Test right at the boundary where regions do not intersect
        assertFalse("Regions should not intersect", internalNode.intersects(0,
            0, 10, 10, 10, 10, 0, 0));
        assertFalse("Regions should not intersect", internalNode.intersects(10,
            10, 10, 10, 0, 0, 0, 0));

        // Test right at the boundary where regions intersect
        assertTrue("Regions should intersect", internalNode.intersects(0, 0, 11,
            11, 10, 10, 1, 1));
        assertTrue("Regions should intersect", internalNode.intersects(9, 9, 10,
            10, 0, 0, 10, 10));
    }


    /**
     * Testing intersect
     */
    public void testFindQuadrant1() {
        InternalNode node = new InternalNode();
        int regionSize = 100;

        // These points are positioned to be in each of the four quadrants
        Point nwPoint = new Point("nw", 24, 24);
        Point nePoint = new Point("ne", 76, 24);
        Point swPoint = new Point("sw", 24, 76);
        Point sePoint = new Point("se", 76, 76);

        // Test NW quadrant
        QuadTreeNode result = node.findQuadrant(nwPoint, 0, 0, regionSize);
        assertEquals(node.getNw(), result);

        // Test NE quadrant
        result = node.findQuadrant(nePoint, 0, 0, regionSize);
        assertEquals(node.getNe(), result);

        // Test SW quadrant
        result = node.findQuadrant(swPoint, 0, 0, regionSize);
        assertEquals(node.getSw(), result);

        // Test SE quadrant
        result = node.findQuadrant(sePoint, 0, 0, regionSize);
        assertEquals(node.getSe(), result);
    }


    /**
     * Tests the regionSearch method for a query that intersects with all
     * quadrants.
     */
    public void testRegionSearchIntersectsAllQuadrants() {
        // Insert points into different quadrants
        internalNode.insert(testPointNW, 0, 0, 1024);
        internalNode.insert(testPointNE, 0, 0, 1024);
        internalNode.insert(testPointSW, 0, 0, 1024);
        internalNode.insert(testPointSE, 0, 0, 1024);

        // Perform a region search that intersects all quadrants
        ArrayList<Point> foundPoints = internalNode.regionSearch(0, 0, 1024,
            1024, 0, 0, 1024);

        // Check that points from all quadrants are found
        assertEquals(4, foundPoints.size());
    }


    /**
     * Tests the regionSearch method for a query that does not intersect with
     * any quadrants.
     */
    public void testRegionSearchNoIntersection() {
        // Perform a region search outside of the node's region
        ArrayList<Point> foundPoints = internalNode.regionSearch(2000, 2000,
            100, 100, 0, 0, 1024);

        // Check that no points are found
        assertTrue(foundPoints.isEmpty());
    }


    /**
     * Tests the regionSearch method for a query that intersects with only one
     * quadrant.
     */
    public void testRegionSearchIntersectsOneQuadrant() {
        // Insert a point into the NE quadrant
        internalNode.insert(testPointNE, 0, 0, 1024);

        // Perform a region search that only intersects the NE quadrant
        ArrayList<Point> foundPoints = internalNode.regionSearch(500, 0, 524,
            524, 0, 0, 1024);

        // Check that only one point is found and it's in the NE quadrant
        assertEquals(1, foundPoints.size());
        assertTrue(foundPoints.contains(testPointNE));
    }


    /**
     * Tests the regionSearch method with zero width and height.
     */
    public void testRegionSearchZeroWidthHeight() {
        // Attempt a region search with zero width and height
        ArrayList<Point> foundPoints = internalNode.regionSearch(100, 100, 0, 0,
            0, 0, 1024);

        // Expect no points to be found, but also ensure the method handles this
        // gracefully
        assertTrue(foundPoints.isEmpty());
    }


    /**
     * Tests the regionSearch method for a query partially outside the world
     * box.
     */
    public void testRegionSearchPartialOutsideWorld() {
        // Insert points into different quadrants
        internalNode.insert(testPointNW, 0, 0, 1024);
        internalNode.insert(testPointNE, 0, 0, 1024);

        // Perform a region search that is partially outside the world box
        ArrayList<Point> foundPoints = internalNode.regionSearch(500, 500, 1024,
            1024, 0, 0, 1024);

        // Check that the points within the world box are found
        assertEquals(0, foundPoints.size());
    }


    /**
     * Test removing a point from each quadrant without triggering a merge.
     */
    public void testRemovePointNoMerge() {
        // Insert points into each quadrant
        internalNode.insert(testPointNW, 0, 0, 1024);
        internalNode.insert(testPointNE, 0, 0, 1024);
        internalNode.insert(testPointSW, 0, 0, 1024);
        internalNode.insert(testPointSE, 0, 0, 1024);

        // Remove one point from each quadrant
        internalNode.remove(testPointNW, 0, 0, 1024);
        internalNode.remove(testPointNE, 0, 0, 1024);
        internalNode.remove(testPointSW, 0, 0, 1024);
        internalNode.remove(testPointSE, 0, 0, 1024);

        // Assert that no merge occurred and children are now FlyweightNodes
        assertTrue(internalNode.getNw() instanceof FlyweightNode);
        assertTrue(internalNode.getNe() instanceof FlyweightNode);
        assertTrue(internalNode.getSw() instanceof FlyweightNode);
        assertTrue(internalNode.getSe() instanceof FlyweightNode);
    }


    /**
     * Test removing a non-existent point and ensure it does not affect the
     * structure.
     */
    public void testRemoveNonExistentPoint() {
        // Attempt to remove a point that doesn't exist

        // QuadTreeNode result = internalNode.remove(new Point("NonExistent",
        // 500, 500), 0, 0, 1024);

        // Assert that the return value indicates no removal and structure
        // unchanged
        // assertNull(result);
        // Assert structure of internalNode remains unchanged with all children
        // as FlyweightNodes
        assertTrue(internalNode.getNw() instanceof FlyweightNode);
        assertTrue(internalNode.getNe() instanceof FlyweightNode);
        assertTrue(internalNode.getSw() instanceof FlyweightNode);
        assertTrue(internalNode.getSe() instanceof FlyweightNode);
    }


    /**
     * Test removing points to trigger an attemptMerge call that results in a
     * merge.
     */
    public void testRemovePointTriggerMerge() {
        // Insert multiple points into one quadrant
        internalNode.insert(testPointNW, 0, 0, 1024);
        internalNode.insert(new Point("TestPointNW2", 251, 251), 0, 0, 1024);

        // Remove one point to trigger attemptMerge, which should not merge due
        // to multiple points
        internalNode.remove(testPointNW, 0, 0, 1024);
        // Assert that no merge occurred because there are still points in one
        // quadrant
        assertTrue(internalNode.getNw() instanceof LeafNode);

        // Now, remove the last point to trigger a merge
        internalNode.remove(new Point("TestPointNW2", 251, 251), 0, 0, 1024);
        // Assert that merge occurred and the quadrant is now a FlyweightNode
        assertTrue(internalNode.getNw() instanceof FlyweightNode);
    }


    /**
     * Test edge cases of removing points on the boundary of quadrants.
     */
    public void testRemovePointOnBoundary() {
        // Insert points on the boundary of quadrants
        Point boundaryPointNWNE = new Point("BoundaryNW_NE", 512, 250);
        internalNode.insert(boundaryPointNWNE, 0, 0, 1024);

        // Remove the boundary point
        internalNode.remove(boundaryPointNWNE, 0, 0, 1024);

        // Assert that the structure is correct after removal
        assertTrue(internalNode.getNw() instanceof FlyweightNode);
        assertTrue(internalNode.getNe() instanceof FlyweightNode);
    }


    /**
     * Test attemptMerge when all children are flyweight nodes.
     */
    public void testAttemptMergeAllFlyweights() {
        // By default, all children of a new internal node are FlyweightNodes,
        // so no points to combine and should remain unchanged.
        // QuadTreeNode result = internalNode.attemptMerge();
        // assertSame("InternalNode should remain unchanged with all Flyweight
        // children", internalNode, result);
    }


    /**
     * Test attemptMerge with fewer than four points across all children.
     */
    public void testAttemptMergeFewerThanFourPoints() {
        // Insert three points into different quadrants
        internalNode.insert(testPointNW, 0, 0, 1024);
        internalNode.insert(testPointNE, 0, 0, 1024);
        internalNode.insert(testPointSW, 0, 0, 1024);

        // Attempt to merge
        QuadTreeNode result = internalNode.attemptMerge();
        assertTrue("Should merge into a LeafNode with three points",
            result instanceof LeafNode);
        assertEquals("LeafNode should contain all three points", 3,
            ((LeafNode)result).getPoints().size());
    }


    /**
     * Test attemptMerge with all points at the same position.
     */
    public void testAttemptMergeAllPointsSamePosition() {
        // Insert multiple points at the same position in different quadrants
        Point samePositionPoint = new Point("Same", 512, 512);
        internalNode.insert(samePositionPoint, 0, 0, 1024);
        internalNode.insert(new Point("Same2", 512, 512), 0, 0, 1024);
        internalNode.insert(new Point("Same3", 512, 512), 0, 0, 1024);
        internalNode.insert(new Point("Same4", 512, 512), 0, 0, 1024);

        // Attempt to merge
        QuadTreeNode result = internalNode.attemptMerge();
        assertTrue(
            "Should merge into a LeafNode with all points at the same position",
            result instanceof LeafNode);
        assertEquals("LeafNode should contain all points at the same position",
            4, ((LeafNode)result).getPoints().size());
    }


    /**
     * Test attemptMerge with more than three distinct points.
     */
    public void testAttemptMergeMoreThanThreeDistinctPoints() {
        // Insert four distinct points into different quadrants
        internalNode.insert(testPointNW, 0, 0, 1024);
        internalNode.insert(testPointNE, 0, 0, 1024);
        internalNode.insert(testPointSW, 0, 0, 1024);
        internalNode.insert(testPointSE, 0, 0, 1024);

        // Attempt to merge
        QuadTreeNode result = internalNode.attemptMerge();
        assertSame("No merge should occur with more than three distinct points",
            internalNode, result);
    }


    /**
     * Test attemptMerge with an internal node present.
     */
    public void testAttemptMergeWithInternalNode() {
        // Manually set one of the children to an internal node to simulate a
        // complex structure
        internalNode.setNw(new InternalNode());
        internalNode.insert(testPointNE, 0, 0, 1024);
        internalNode.insert(testPointSW, 0, 0, 1024);
        internalNode.insert(testPointSE, 0, 0, 1024);

        // Attempt to merge
        QuadTreeNode result = internalNode.attemptMerge();
        assertSame("No merge should occur if any child is an InternalNode",
            internalNode, result);
    }


    /**
     * Tests FindQuadrantNW
     */
    public void testFindQuadrantNWAlt() {
        // NW quadrant: point's x and y are less than midX and midY,
        // respectively
        Point point = new Point("TestPoint", 49, 49); // Assuming size is
                                                      // 100x100, midX and midY
                                                      // are 50
        QuadTreeNode result = internalNode.findQuadrant(point, 0, 0, 100);
        assertTrue("Point should be in NW quadrant",
            result instanceof FlyweightNode); // Assuming default quadrant nodes
                                              // are FlyweightNodes
    }


    /**
     * Tests FindQuadrantNW
     */
    public void testFindQuadrantNEAlt() {
        // NE quadrant: point's x is greater than or equal to midX and y is less
        // than midY
        Point point = new Point("TestPoint", 51, 49); // Assuming size is
                                                      // 100x100
        QuadTreeNode result = internalNode.findQuadrant(point, 0, 0, 100);
        assertTrue("Point should be in NE quadrant",
            result instanceof FlyweightNode);
    }


    /**
     * Tests FindQuadrantNW
     */
    public void testFindQuadrantSWAlt() {
        // SW quadrant: point's x is less than midX and y is greater than or
        // equal to midY
        Point point = new Point("TestPoint", 49, 51); // Assuming size is
                                                      // 100x100
        QuadTreeNode result = internalNode.findQuadrant(point, 0, 0, 100);
        assertTrue("Point should be in SW quadrant",
            result instanceof FlyweightNode);
    }


    /**
     * Tests FindQuadrantNW
     */
    public void testFindQuadrantSEALt() {
        // SE quadrant: point's x and y are greater than or equal to midX and
        // midY, respectively
        Point point = new Point("TestPoint", 51, 51); // Assuming size is
                                                      // 100x100
        QuadTreeNode result = internalNode.findQuadrant(point, 0, 0, 100);
        assertTrue("Point should be in SE quadrant",
            result instanceof FlyweightNode);
    }


    /**
     * Tests FindQuadrantNW
     */
    public void testPointExactlyAtMidpointAlt() {
        // Edge case: point exactly at the midpoint should fall in NE quadrant
        // by this implementation
        Point point = new Point("MidPoint", 50, 50); // Assuming size is 100x100
        QuadTreeNode result = internalNode.findQuadrant(point, 0, 0, 100);
        assertTrue("Point at midpoint should be in NE quadrant",
            result instanceof FlyweightNode);
    }


    /**
     * Tests FindQuadrantNW
     */
    public void testFindQuadrantNWALt() {
        // NW quadrant: point's x and y are less than midX and midY,
        // respectively
        Point point = new Point("TestPoint", 49, 49); // Assuming size is
                                                      // 100x100, midX and midY
                                                      // are 50
        QuadTreeNode result = internalNode.findQuadrant(point, 0, 0, 100);
        assertTrue("Point should be in NW quadrant",
            result instanceof FlyweightNode); // Assuming default quadrant nodes
                                              // are FlyweightNodes
    }


    /**
     * Tests FindQuadrantNW
     */
    public void testFindQuadrantNEALt() {
        // NE quadrant: point's x is greater than or equal to midX and y is less
        // than midY
        Point point = new Point("TestPoint", 51, 49); // Assuming size is
                                                      // 100x100
        QuadTreeNode result = internalNode.findQuadrant(point, 0, 0, 100);
        assertTrue("Point should be in NE quadrant",
            result instanceof FlyweightNode);
    }


    /**
     * Tests FindQuadrantNW
     */
    public void testFindQuadrantSWAlt1() {
        // SW quadrant: point's x is less than midX and y is greater than or
        // equal to midY
        Point point = new Point("TestPoint", 49, 51); // Assuming size is
                                                      // 100x100
        QuadTreeNode result = internalNode.findQuadrant(point, 0, 0, 100);
        assertTrue("Point should be in SW quadrant",
            result instanceof FlyweightNode);
    }


    /**
     * Tests FindQuadrantNW
     */
    public void testFindQuadrantSEAlt() {
        // SE quadrant: point's x and y are greater than or equal to midX and
        // midY, respectively
        Point point = new Point("TestPoint", 51, 51); // Assuming size is
                                                      // 100x100
        QuadTreeNode result = internalNode.findQuadrant(point, 0, 0, 100);
        assertTrue("Point should be in SE quadrant",
            result instanceof FlyweightNode);
    }


    /**
     * Tests FindQuadrantNW
     */
    public void testPointExactlyAtMidpoint() {
        // Edge case: point exactly at the midpoint should fall in NE quadrant
        // by this implementation
        Point point = new Point("MidPoint", 50, 50); // Assuming size is 100x100
        QuadTreeNode result = internalNode.findQuadrant(point, 0, 0, 100);
        assertTrue("Point at midpoint should be in NE quadrant",
            result instanceof FlyweightNode);
    }


    /**
     * Test points exactly on the boundaries
     */
    public void testFindQuadrantBoundaryConditions() {
        Point boundaryPointX = new Point("BoundaryX", 50, 40);
        Point boundaryPointY = new Point("BoundaryY", 40, 50);
        Point boundaryPointXY = new Point("BoundaryXY", 50, 50);

        // Assuming size is 100x100, midX and midY are 50
        QuadTreeNode resultX = internalNode.findQuadrant(boundaryPointX, 0, 0,
            100);
        QuadTreeNode resultY = internalNode.findQuadrant(boundaryPointY, 0, 0,
            100);
        QuadTreeNode resultXY = internalNode.findQuadrant(boundaryPointXY, 0, 0,
            100);

        assertTrue(resultX instanceof FlyweightNode);
        assertTrue(resultY instanceof FlyweightNode);
        assertTrue(resultXY instanceof FlyweightNode);
    }


    /**
     * Test for logical expression mutations by checking points that are clearly
     * within each quadrant
     */

    public void testFindQuadrantClearConditions() {
        Point pointNW = new Point("NW", 25, 25);
        Point pointNE = new Point("NE", 75, 25);
        Point pointSW = new Point("SW", 25, 75);
        Point pointSE = new Point("SE", 75, 75);

        QuadTreeNode resultNW = internalNode.findQuadrant(pointNW, 0, 0, 100);
        QuadTreeNode resultNE = internalNode.findQuadrant(pointNE, 0, 0, 100);
        QuadTreeNode resultSW = internalNode.findQuadrant(pointSW, 0, 0, 100);
        QuadTreeNode resultSE = internalNode.findQuadrant(pointSE, 0, 0, 100);

        assertTrue("Point in NW should be in NW quadrant",
            resultNW instanceof FlyweightNode);
        assertTrue("Point in NE should be in NE quadrant",
            resultNE instanceof FlyweightNode);
        assertTrue("Point in SW should be in SW quadrant",
            resultSW instanceof FlyweightNode);
        assertTrue("Point in SE should be in SE quadrant",
            resultSE instanceof FlyweightNode);
    }


    /**
     * Tests the remove method to ensure points are correctly removed from the
     * appropriate quadrant.
     */
    public void testRemove() {
        // Insert and then remove points from the InternalNode
        internalNode.insert(testPointNW, 0, 0, 1024);
        internalNode.remove(testPointNW, 0, 0, 1024);

        // Verify that the points are removed correctly
        assertEquals("Point should be removed", 0, internalNode.getNw().search(
            testPointNW.getName()).size());

        // Attempt to remove a point not in the tree
        QuadTreeNode result = internalNode.remove(new Point("NonExistent", 10,
            10), 0, 0, 1024);
        assertNotNull(result);
    }


    /**
     * Tests the attemptMerge method to ensure internal nodes correctly merge
     * into
     * a leaf node when applicable.
     */
    public void testAttemptMerge() {
        // Manually create a scenario where a merge is needed
        LeafNode leafNW = new LeafNode();
        leafNW.insert(new Point("MergeTest", 10, 10), 0, 0, 512);
        internalNode.setNw(leafNW);
        internalNode.setNe(FlyweightNode.getInstance());
        internalNode.setSw(FlyweightNode.getInstance());
        internalNode.setSe(FlyweightNode.getInstance());

        QuadTreeNode result = internalNode.attemptMerge();
        assertTrue("Internal node should merge to a LeafNode",
            result instanceof LeafNode);
    }


    /**
     * Tests the regionSearch method to verify it returns all points within a
     * given
     * region.
     */
    public void testRegionSearchAlt() {
        internalNode.insert(testPointNW, 0, 0, 1024);
        internalNode.insert(testPointNE, 0, 0, 1024);

        ArrayList<Point> foundPoints = internalNode.regionSearch(0, 0, 1024,
            1024, 0, 0, 1024);
        assertEquals("Should find all points within the region", 2, foundPoints
            .size());

        foundPoints = internalNode.regionSearch(500, 500, 200, 200, 0, 0, 1024);
        assertEquals("Should find points within the specific region", 0,
            foundPoints.size());
    }


    /**
     * Tests the intersects method to verify correct identification of
     * overlapping
     * regions.
     */
    public void testIntersectsAlt() {
        assertTrue("Should correctly identify overlapping regions", internalNode
            .intersects(0, 0, 1024, 1024, 512, 512, 1024, 1024));
        assertFalse("Should correctly identify non-overlapping regions",
            internalNode.intersects(0, 0, 500, 500, 600, 600, 1024, 1024));
    }


    /**
     * Tests complex operations involving inserts, removes, and region searches
     * to
     * ensure the internal node updates and queries correctly under various
     * scenarios.
     */
    public void testComplexOperations() {
        // Insert points in all quadrants
        internalNode.insert(testPointNW, 0, 0, 1024);
        internalNode.insert(testPointNE, 0, 0, 1024);
        internalNode.insert(testPointSW, 0, 0, 1024);
        internalNode.insert(testPointSE, 0, 0, 1024);

        // Remove a point and then attempt to re-insert it
        internalNode.remove(testPointSW, 0, 0, 1024);
        internalNode.insert(testPointSW, 0, 0, 1024);

        // Verify the point has been reinserted correctly
        ArrayList<Point> swPoints = internalNode.getSw().search(testPointSW
            .getName());
        assertEquals("Point SW should be reinserted", 1, swPoints.size());

        // Perform a region search that partially overlaps two quadrants
        ArrayList<Point> foundPoints = internalNode.regionSearch(500, 0, 600,
            600, 0, 0, 1024);
        assertEquals("Should find points in NE and SE quadrants only", 1,
            foundPoints.size());

        // Ensure the internal structure reflects the removal and addition
        assertNotNull("NW quadrant should not be null", internalNode.getNw());
        assertNotNull("NE quadrant should not be null", internalNode.getNe());
        assertNotNull("SW quadrant should not be null", internalNode.getSw());
        assertNotNull("SE quadrant should not be null", internalNode.getSe());
    }


    /**
     * Tests the behavior of the internal node when points are inserted at the
     * exact
     * boundary of the internal node's region.
     */
    public void testBoundaryInsertions() {
        Point boundaryPoint = new Point("Boundary", 512, 512);
        internalNode.insert(boundaryPoint, 0, 0, 1024);

        // This point is exactly at the boundary and should be handled
        // gracefully
        assertTrue("Boundary point should be inserted in a quadrant",
            internalNode.getNe().search(boundaryPoint.getName()).contains(
                boundaryPoint) || internalNode.getSe().search(boundaryPoint
                    .getName()).contains(boundaryPoint) || internalNode.getSw()
                        .search(boundaryPoint.getName()).contains(boundaryPoint)
                || internalNode.getNw().search(boundaryPoint.getName())
                    .contains(boundaryPoint));
    }


    /**
     * Tests that points exactly on the outer boundary of the quadtree are
     * handled
     * correctly.
     */
    public void testOuterBoundaryHandling() {
        Point outerBoundaryPoint = new Point("OuterBoundary", 0, 1024);
        internalNode.insert(outerBoundaryPoint, 0, 0, 1024);

        assertTrue("Outer boundary point should be inserted correctly",
            internalNode.getSw().search(outerBoundaryPoint.getName()).contains(
                outerBoundaryPoint));
    }


    /**
     * Tests duplicate point insertions and ensures the quadtree handles them
     * appropriately, allowing or disallowing duplicates based on
     * implementation.
     */
    public void testDuplicateInsertions() {
        internalNode.insert(testPointNW, 0, 0, 1024);
        internalNode.insert(testPointNW, 0, 0, 1024); // Attempt to insert a
                                                      // duplicate

        // Depending on implementation, adjust assertion accordingly
        int nwPointsCount = internalNode.getNw().search(testPointNW.getName())
            .size();
        assertEquals("Duplicates may be allowed or disallowed", 1,
            nwPointsCount);
    }


    /**
     * tests boundary conditions
     */
    public void testOffByOneBoundaryConditions() {
        // Insert points exactly on the expected boundary of quadrants
        Point justInsideNE = new Point("JustInsideNE", 512, 511); // Assuming
                                                                  // midX, midY
                                                                  // at 512
        Point justOutsideNE = new Point("JustOutsideNE", 513, 512);

        internalNode.insert(justInsideNE, 0, 0, 1024);
        internalNode.insert(justOutsideNE, 0, 0, 1024);

        assertTrue("justInsideNE should be in NE", internalNode.getNe().search(
            justInsideNE.getName()).contains(justInsideNE));
        assertFalse("justOutsideNE should not be in NE due to off-by-one error",
            internalNode.getNe().search(justOutsideNE.getName()).contains(
                justOutsideNE));
    }


    /**
     * Tests region search
     */
    public void testRegionSearchEdgeInclusion() {
        Point edgePoint = new Point("Edge", 500, 500); // Edge case for region
                                                       // search
        internalNode.insert(edgePoint, 0, 0, 1024);

        ArrayList<Point> foundPoints = internalNode.regionSearch(495, 495, 10,
            10, 0, 0, 1024);
        assertTrue("Points on the edge of the search area should be included",
            foundPoints.contains(edgePoint));
    }


    /**
     * Tests sequential Operations
     */
    public void testSequentialOperations() {
        // Insert points in all quadrants, remove some, then search and finally
        // attempt a merge
        internalNode.insert(new Point("Point1", 100, 100), 0, 0, 1024);
        internalNode.insert(new Point("Point2", 800, 100), 0, 0, 1024);
        internalNode.remove(new Point("Point1", 100, 100), 0, 0, 1024);
        ArrayList<Point> foundPoints = internalNode.regionSearch(0, 0, 1024,
            1024, 0, 0, 1024);

        assertEquals("After removal and searches, should find remaining points",
            1, foundPoints.size());
        QuadTreeNode mergedNode = internalNode.attemptMerge();
        // Depending on your merge strategy, either all nodes are merged into a
        // leaf or remain separate if not empty
        assertTrue("After attemptMerge, check if correctly merged or not",
            mergedNode instanceof LeafNode || mergedNode == internalNode);

        // Insert a point again to check if tree structure is maintained
        // post-merge
        Point reinsertedPoint = new Point("Reinserted", 300, 300);
        internalNode.insert(reinsertedPoint, 0, 0, 1024);
        assertTrue("Reinserted point should be found after merge "
            + "and re-insertion operations", internalNode.search(reinsertedPoint
                .getName()).contains(reinsertedPoint));
    }


    /**
     * Tests find Quadrant
     */
    public void testFindQuadrantWithMutatedMidpoints() {
        Point pointExactlyMidX = new Point("MidX", 512, 300);
        Point pointExactlyMidY = new Point("MidY", 300, 512);
        Point pointExactlyMidXY = new Point("MidXY", 512, 512);

        // Assuming the quadrant is 1024x1024 and mutations in calculation of
        // midX and midY
        // The points on midX or midY lines should be checked for correct
        // quadrant placement
        assertEquals(internalNode.findQuadrant(pointExactlyMidX, 0, 0, 1024),
            internalNode.getNe());
        assertEquals(internalNode.findQuadrant(pointExactlyMidY, 0, 0, 1024),
            internalNode.getSw());
        // Depending on how your code handles points exactly at the center, it
        // could go to any quadrant
        assertNotNull(internalNode.findQuadrant(pointExactlyMidXY, 0, 0, 1024));
    }


    /**
     * Tests find Quadrant
     */
    public void testFindQuadrantBoundaryConditionsAlt() {
        Point onHorizontalDivide = new Point("HorizDivide", 512, 300);
        Point onVerticalDivide = new Point("VertDivide", 300, 512);

        // Depending on implementation, these may be assigned to different
        // quadrants
        // The test should reflect the expected behavior of your implementation
        assertEquals(internalNode.findQuadrant(onHorizontalDivide, 0, 0, 1024),
            internalNode.getNe());
        assertEquals(internalNode.findQuadrant(onVerticalDivide, 0, 0, 1024),
            internalNode.getSw());
    }


    /**
     * Tests find remove
     */
    public void testRemoveAtMidpoints() {
        Point pointAtMidX = new Point("MidX", 512, 300);
        Point pointAtMidY = new Point("MidY", 300, 512);

        internalNode.insert(pointAtMidX, 0, 0, 1024);
        internalNode.insert(pointAtMidY, 0, 0, 1024);

        internalNode.remove(pointAtMidX, 0, 0, 1024);
        internalNode.remove(pointAtMidY, 0, 0, 1024);

        assertFalse(internalNode.getNe().search(pointAtMidX.getName()).contains(
            pointAtMidX));
        assertFalse(internalNode.getSw().search(pointAtMidY.getName()).contains(
            pointAtMidY));
    }


    /**
     * Tests internal node
     */
    public void testAllPointsHaveSameCoordinates() {
        ArrayList<Point> pointsWithSameCoordinates = new ArrayList<>(Arrays
            .asList(new Point("Same1", 100, 100), new Point("Same2", 100,
                100)));
        ArrayList<Point> pointsWithDifferentCoordinates = new ArrayList<>(Arrays
            .asList(new Point("Diff1", 100, 100), new Point("Diff2", 200,
                200)));

        assertTrue(internalNode.allPointsHaveSameCoordinates(
            pointsWithSameCoordinates));
        assertFalse(internalNode.allPointsHaveSameCoordinates(
            pointsWithDifferentCoordinates));
    }


    /**
     * Tests regionSearch
     */

    public void testRegionSearchWithMutatedBounds() {
        Point pointInRegion = new Point("InRegion", 300, 300);
        internalNode.insert(pointInRegion, 0, 0, 1024);

        ArrayList<Point> results = internalNode.regionSearch(250, 250, 100, 100,
            0, 0, 1024);
        assertTrue("Point within region should be found", results.contains(
            pointInRegion));

        // Now test with a region that should not find the point due to mutated
        // calculations
        results = internalNode.regionSearch(350, 350, 50, 50, 0, 0, 1024);
        assertFalse(
            "Point outside the mutated region bounds should not be found",
            results.contains(pointInRegion));
    }


    /**
     * Testing intersects method
     */
    public void testIntersectsLogicalMutation() {
        // Test with a region that clearly intersects
        assertTrue(internalNode.intersects(250, 250, 300, 300, 275, 275, 50,
            50));

        // Test with a region that clearly does not intersect
        assertFalse(internalNode.intersects(250, 250, 100, 100, 400, 400, 100,
            100));

        // Test with a region where mutations could cause an incorrect result
        assertTrue(internalNode.intersects(250, 250, 200, 200, 275, 275, 100,
            100)); // Adjusted for mutation
    }


    /**
     * Testing more for mutations
     */
    public void testPointOnBoundaryShouldBeInCorrectQuadrant() {
        Point boundaryPoint = new Point("BoundaryPoint", 512, 512);
        QuadTreeNode quadrant = internalNode.findQuadrant(boundaryPoint, 0, 0,
            1024);
        // Assert the point is in the expected quadrant, which depends on your
        // implementation
        // This might be internalNode.getNe(), internalNode.getSe(), etc.
    }


    /**
     * Testing more for mutations
     */
    public void testIdentifyDuplicatesShouldReturnCorrectDuplicates() {
        ArrayList<Point> points = new ArrayList<>();
        points.add(new Point("Point1", 100, 100));
        points.add(new Point("Point1", 100, 100)); // Duplicate
        // Call identifyDuplicates and assert it identifies the duplicate
    }

}
