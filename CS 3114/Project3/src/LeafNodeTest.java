import student.TestCase;
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

/**
 * Tests the LeafNode class for its ability to manage points, including
 * insertion, removal, and search functionalities within a PR Quadtree.
 * 
 * @author kinjalpandey, architg03, rishavc18
 * @version 02/25/24
 */
public class LeafNodeTest extends TestCase {

    private LeafNode leafNode;
    private Point point1;
    private Point point2;
    private Point point3; // Point with the same name as point1 but different
                          // coordinates
    private Point pointDuplicate;

    /**
     * Sets up the test cases by initializing a LeafNode and several Points.
     */
    public void setUp() {
        leafNode = new LeafNode();

        // Initialize points with unique and duplicate names
        point1 = new Point("Point1", 100, 100);
        point2 = new Point("Point2", 200, 200);
        point3 = new Point("Point1", 300, 300);
        pointDuplicate = new Point("PointDuplicate", 100, 100);
    }


    /**
     * Tests inserting points into the leaf node.
     */
    public void testInsert() {
        // Insert a single point
        QuadTreeNode resultNode = leafNode.insert(point1, 0, 0, 1024);
        assertTrue("LeafNode should remain after inserting one point",
            resultNode instanceof LeafNode);

        // Insert another point and test for split
        resultNode = resultNode.insert(point2, 0, 0, 1024);
        // assertTrue("Should return an InternalNode after inserting a second
        // point",resultNode instanceof InternalNode);
    }

// /**
// * Tests removing points from the leaf node.
// */
// public void testRemove() {
// // Insert a point and remove it
// leafNode.insert(point1, 0, 0, 1024);
// QuadTreeNode removed = leafNode.remove(point1, 0, 0, 1024);
// assertEquals("Point should be removed successfully", removed);
//
// // Attempt to remove a non-existent point
// removed = leafNode.remove(point2, 0, 0, 1024);
// assertNotSame("Attempt to remove a non-existent point should fail",
// removed);
// }


    /**
     * Tests searching for points by name within the leaf node.
     */
    public void testSearchByName() {
        // Insert points with unique and duplicate names
        leafNode.insert(point1, 0, 0, 1024);
        leafNode.insert(point3, 0, 0, 1024); // Same name as point1
    }


    /**
     * Tests the region search functionality within the leaf node.
     */
    public void testRegionSearch() {
        // Insert points into the LeafNode
        leafNode.insert(point1, 0, 0, 1024);
        leafNode.insert(point2, 0, 0, 1024);

        // Define a region that includes only point1
        List<Point> foundPoints = leafNode.regionSearch(50, 50, 100, 100, 0, 0,
            1024);
        // assertEquals("Should find 1 point in the specified region", 1,
        // foundPoints.size());
        // assertTrue("Found point should be point1",
        // foundPoints.contains(point1));

        // Test a region that does not include any points
        foundPoints = leafNode.regionSearch(400, 400, 100, 100, 0, 0, 1024);
        assertTrue("No points should be found in a non-overlapping region",
            foundPoints.isEmpty());
    }


    /**
     * Tests inserting points into the leaf node, including cases that should
     * cause
     * a split.
     */
    public void testInsert1() {
        // Insert a single point
        QuadTreeNode resultNode = leafNode.insert(point1, 0, 0, 1024);
        assertTrue("LeafNode should remain after inserting one point",
            resultNode instanceof LeafNode);

        // Insert a point with the same coordinates should not cause a split
        resultNode = resultNode.insert(pointDuplicate, 0, 0, 1024);
        assertTrue("LeafNode should remain after inserting a point"
            + " with duplicate coordinates", resultNode instanceof LeafNode);

        // Insert another point with different coordinates should cause a split
        resultNode = resultNode.insert(point2, 0, 0, 1024);
        /**
         * assertTrue("Should return an InternalNode after inserting a" + "
         * second point
         * with different coordinates", resultNode instanceof InternalNode);
         **/
    }

    /**
     * Tests mutation where logical expressions in the insert method may be
     * altered.
     */
    /**
     * public void testInsertMutation() { // Mutation could alter the logical
     * expression to incorrectly cause/not // cause a split
     * leafNode.insert(point1,
     * 0, 0, 1024); QuadTreeNode resultNode = leafNode.insert(pointDuplicate, 0,
     * 0,
     * 1024); assertFalse("Mutation causing incorrect split should fail this
     * test",
     * resultNode instanceof InternalNode);
     * 
     * resultNode = leafNode.insert(point2, 0, 0, 1024); assertTrue("Mutation
     * preventing correct split should fail this test", resultNode instanceof
     * InternalNode); }
     **/

// /**
// * Tests removing points from the leaf node, including handling of duplicate
// * points.
// */
// public void testRemove1() {
// // Insert the same point twice
// leafNode.insert(point1, 0, 0, 1024);
// leafNode.insert(pointDuplicate, 0, 0, 1024);
// assertEquals("LeafNode should have two points before removal", 2,
// leafNode.getPoints().size());
//
// // Remove one instance of the duplicate point
// QuadTreeNode removed = leafNode.remove(point1, 0, 0, 1024);
// assertEquals("One instance of the duplicate point should be removed"
// + " successfully", removed);
// assertEquals("LeafNode should have one point after removal", 1, leafNode
// .getPoints().size());
//
// // Remove the second instance of the duplicate point
// removed = leafNode.remove(pointDuplicate, 0, 0, 1024);
// assertEquals("The second instance of the duplicate point"
// + " should be removed successfully", removed);
// assertTrue("LeafNode should have no points after removing "
// + "duplicate point", leafNode.getPoints().isEmpty());
//
// // Attempt to remove a point that no longer exists
// removed = leafNode.remove(point1, 0, 0, 1024);
// assertNotSame(
// "Attempt to remove a point that no longer exists should fail",
// removed);
// assertTrue("LeafNode should be empty after failed removal attempt",
// leafNode.getPoints().isEmpty());
// }

// /**
// * Tests mutations in the remove method that could incorrectly allow or
// * prevent
// * removal.
// */
// public void testRemoveMutation() {
// // Insert a point
// leafNode.insert(point1, 0, 0, 1024);
//
// // Mutation could allow removal of a point not present
// Point nonExistentPoint = new Point("NonExistent", 200, 200);
// QuadTreeNode removed = leafNode.remove(nonExistentPoint, 0, 0, 1024);
// assertNotSame("Mutation allowing removal of non-existent point should"
// + " fail this test", removed);
//
// // Mutation could prevent removal of an existing point
// removed = leafNode.remove(point1, 0, 0, 1024);
// assertEquals("Mutation preventing removal of existing point should fail"
// + " this test", removed);
// }

}
