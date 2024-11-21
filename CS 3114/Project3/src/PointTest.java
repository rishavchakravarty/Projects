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
 * Tests the Point class to ensure its immutability and correct behavior of its
 * constructor, accessor methods, and toString method.
 * 
 * @author kinjalpandey, architg03, rishavc18
 * @version 02/25/24
 */
public class PointTest extends TestCase {

    private Point point;

    /**
     * Sets up the test cases by initializing a Point instance.
     */
    public void setUp() {
        // Initialize a Point with a specific name and coordinates
        point = new Point("TestPoint", 50, 75);
    }


    /**
     * Tests the constructor and accessor methods.
     */
    public void testAccessors() {
        // Test getName
        assertEquals("Name should be 'TestPoint'", "TestPoint", point
            .getName());

        // Test getX
        assertEquals("X coordinate should be 50", 50, point.getX());

        // Test getY
        assertEquals("Y coordinate should be 75", 75, point.getY());
    }


    /**
     * Tests the toString method.
     */
    public void testToString() {
        // Test toString
        String expectedOutput = "TestPoint, 50, 75";
        assertEquals("toString should return the correct format",
            expectedOutput, point.toString());
    }


    /**
     * Tests the coordEquals method to ensure it correctly identifies when the x
     * and
     * y coordinates of a point match the given parameters.
     */
    public void testCoordEquals() {
        assertTrue("coordEquals should return true for matching coordinates",
            point.coordEquals(50, 75));
        assertFalse(
            "coordEquals should return false for non-matching X coordinate",
            point.coordEquals(60, 75));
        assertFalse(
            "coordEquals should return false for non-matching Y coordinate",
            point.coordEquals(50, 85));
    }


    /**
     * Mutation test to ensure that replacing equality check with false in
     * coordEquals is caught.
     */
    public void testCoordEqualsMutationFalse() {
        // Here, we are simulating a mutation where the equality check
        // in coordEquals has been replaced with false
        assertTrue("coordEquals with mutation should return false", point
            .coordEquals(50, 75));
    }


    /**
     * Mutation test to ensure that replacing equality check with true in
     * coordEquals is caught.
     */
    public void testCoordEqualsMutationTrue() {
        // Here, we are simulating a mutation where the equality check
        // in coordEquals has been replaced with true
        assertFalse("coordEquals with mutation should return true", point
            .coordEquals(10, 10));
    }


    /**
     * Tests coordEquals method against mutation where the comparison check is
     * replaced with false.
     */
    public void testCoordEqualsComparisonCheckWithFalse() {
        // Simulate mutation by assuming `coordEquals` always returns false
        Point pointToCompare = new Point("TestPoint", 50, 75);
        boolean result = point.coordEquals(pointToCompare.getX(), pointToCompare
            .getY());
        assertTrue("Mutation simulating false comparison should fail", result);
    }


    /**
     * Tests coordEquals method against mutation where the comparison check is
     * replaced with true.
     */
    public void testCoordEqualsComparisonCheckWithTrue() {
        // Simulate mutation by assuming `coordEquals` always returns true
        Point pointToCompare = new Point("TestPoint", 100, 100);
        boolean result = point.coordEquals(pointToCompare.getX(), pointToCompare
            .getY());
        assertFalse("Mutation simulating true comparison should fail", result);
    }


    /**
     * Test the coordEquals method when coordinates match.
     */
    public void testCoordEqualsMatch() {
        assertTrue("Coordinates should match", point.coordEquals(50, 75));
    }


    /**
     * Test the coordEquals method when x coordinate does not match.
     */
    public void testCoordEqualsXDoesNotMatch() {
        assertFalse("X coordinate should not match", point.coordEquals(51, 75));
    }


    /**
     * Test the coordEquals method when y coordinate does not match.
     */
    public void testCoordEqualsYDoesNotMatch() {
        assertFalse("Y coordinate should not match", point.coordEquals(50, 76));
    }


    /**
     * Tests the isValid method to ensure it correctly identifies when the x and
     * y
     * coordinates of a point are both positive.
     */
    public void testIsValidMutationWithFalse() {
        Point validPoint = new Point("ValidPoint", 10, 10);
        assertTrue("Point with positive coordinates should be valid", validPoint
            .isValid());

        Point invalidXPoint = new Point("InvalidXPoint", -10, 10);
        assertFalse("Point with negative x should be invalid", invalidXPoint
            .isValid());

        Point invalidYPoint = new Point("InvalidYPoint", 10, -10);
        assertFalse("Point with negative y should be invalid", invalidYPoint
            .isValid());

        Point invalidBothPoint = new Point("InvalidBothPoint", -10, -10);
        assertFalse("Point with negative x and y should be invalid",
            invalidBothPoint.isValid());
    }


    /**
     * Tests the isValid method to ensure it correctly identifies when the x and
     * y
     * coordinates of a point are both positive.
     */
    public void testIsValidMutationWithTrue() {
        Point validPoint = new Point("ValidPoint", 10, 10);
        assertTrue("Point with positive coordinates should be valid", validPoint
            .isValid());

        // This test case should fail if the logical expression is incorrectly
        // mutated
        // to always 'true',
        // because we expect the isValid method to return false for negative
        // coordinates.
        Point invalidXPoint = new Point("InvalidXPoint", -10, 10);
        assertFalse(
            "Mutation causing isValid to always return true is incorrect",
            invalidXPoint.isValid());
    }


    /**
     * Tests the equals method to ensure it correctly identifies when two points
     * have the same name and coordinates.
     */
    public void testEqualsMethod() {
        Point point1 = new Point("Point1", 50, 50);
        Point point1Copy = new Point("Point1", 50, 50);
        Point point2 = new Point("Point2", 50, 50);
        Point point3 = new Point("Point1", 60, 50);
        Point point4 = new Point("Point1", 50, 60);
        Point point5 = new Point("Point1", 60, 60);
        Point nullPoint = null;

        // Test for logical expression 'obj == this' replaced with 'true'
        assertTrue("A point should be equal to itself", point1.equals(point1));

        // Test for logical expression 'obj == this' replaced with 'false'
        assertTrue(
            "Two points with the same name and coordinates should be equal",
            point1.equals(point1Copy));

        // Test for logical expression 'obj == null || obj.getClass() !=
        // this.getClass()' replaced with 'false'
        assertFalse("Points with different names should not be equal", point1
            .equals(point2));

        // Test for logical expression 'obj == null || obj.getClass() !=
        // this.getClass()' replaced with 'true'
        assertFalse("Point should not be equal to null", point1.equals(
            nullPoint));

        // Test for logical expression 'name.equals(point.name) && x == point.x
        // && y ==
        // point.y' replaced with 'false'
        assertFalse("Points with different x coordinates should not be equal",
            point1.equals(point3));
        assertFalse("Points with different y coordinates should not be equal",
            point1.equals(point4));
        assertFalse(
            "Points with different x and y coordinates should not be equal",
            point1.equals(point5));

        // Test for logical expression 'name.equals(point.name) && x == point.x
        // && y ==
        // point.y' replaced with 'true'
        assertFalse("Two different objects should not be equal", point1.equals(
            new Object()));
    }
}
