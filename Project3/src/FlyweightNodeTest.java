import java.util.List;
import student.TestCase;

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
 * Tests the FlyweightNode class to ensure its methods work correctly within a
 * PR Quadtree implementation, especially focusing on the singleton pattern, and
 * the overridden methods that should perform no action or return empty results.
 * 
 * @author kinjalpandey, architg03, rishavc18
 * @version 02/25/24
 */
public class FlyweightNodeTest extends TestCase {

    private FlyweightNode flyweightNode1;
    private FlyweightNode flyweightNode2;
    private Point testPoint;

    /**
     * Sets up the test cases by retrieving instances of FlyweightNode and
     * initializing a test point.
     */
    public void setUp() {
        // FlyweightNode uses a singleton pattern, so both variables should
        // reference the same instance.
        flyweightNode1 = FlyweightNode.getInstance();
        flyweightNode2 = FlyweightNode.getInstance();

        // Initialize a test point with arbitrary values
        testPoint = new Point("TestPoint", 100, 100);
    }


    /**
     * Tests that FlyweightNode implements the singleton pattern correctly.
     */
    public void testSingletonPattern() {
        assertSame("FlyweightNode instances should be the same", flyweightNode1,
            flyweightNode2);
    }


    /**
     * Tests that the insert method returns a new LeafNode containing the
     * inserted
     * point. This method actually tests behavior that would be implemented in
     * the
     * LeafNode class, since FlyweightNode's insert method is supposed to
     * trigger
     * the creation of a LeafNode.
     */
    public void testInsert() {
        QuadTreeNode resultNode = flyweightNode1.insert(testPoint, 0, 0, 1024);

        // Verify that the result is an instance of LeafNode
        assertTrue("Insert method should return an instance of LeafNode",
            resultNode instanceof LeafNode);

        // Further tests to verify the point has been added to the returned
        // LeafNode
        // would be more appropriate in a LeafNodeTest class, as it involves
        // behavior specific to LeafNode.
    }


    /**
     * Tests that the remove method of FlyweightNode always returns false.
     */
    public void testRemove() {
        assertNotSame(
            "Remove method should always return false for FlyweightNode",
            flyweightNode1.remove(testPoint, 0, 0, 1024));
    }


    /**
     * Tests that the search method of FlyweightNode always returns an empty
     * list.
     */
    public void testSearch() {
        List<Point> searchResults = flyweightNode1.search("NonexistentPoint");
        assertTrue(searchResults.isEmpty());
    }


    /**
     * Tests that the regionSearch method of FlyweightNode always returns an
     * empty
     * list.
     */
    public void testRegionSearch() {
        List<Point> regionSearchResults = flyweightNode1.regionSearch(0, 0,
            1024, 1024, 0, 0, 1024);
        assertTrue(regionSearchResults.isEmpty());
    }
}
