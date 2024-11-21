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

import student.TestCase;

/**
 * This class tests the methods of Rectangle class, ensuring that they work as
 * they should.
 * 
 * @author kinjalpandey, architg03, rishavc18
 * @version 02/25/24
 * 
 */
public class RectangleTest extends TestCase {
    private Rectangle rect1;
    private Rectangle rect2;

    /**
     * Initializes a rectangle object to be used for the tests.
     */
    public void setUp() {
        rect1 = new Rectangle(10, 10, 20, 20);
    }


    /**
     * Tests for Constructor and Getters
     */
    public void testConstructorAndGetters() {
        assertEquals(10, rect1.getxCoordinate());
        assertEquals(10, rect1.getyCoordinate());
        assertEquals(20, rect1.getWidth());
        assertEquals(20, rect1.getHeight());
    }


    /**
     * Tests for toString method
     */
    public void testToString() {
        String expected = "10, 10, 20, 20";
        assertEquals(expected, rect1.toString());
    }


    /**
     * Tests for isInvalid method tells if a rectangle is invalid
     */
    public void testIsInvalid() {
        assertFalse(rect1.isInvalid());
        Rectangle invalidRect = new Rectangle(0, 0, -5, -5);
        assertTrue(invalidRect.isInvalid());
    }


    /**
     * Testing Non-Intersecting Rectangles tells if the rectangles are
     * intersecting
     */
    public void testNonIntersectingRectangles() {
        rect2 = new Rectangle(50, 50, 20, 20);
        assertFalse(rect1.intersect(rect2));
    }


    /**
     * Testing Non-Intersecting Rectangles, Vertex Sharing Only
     */
    public void testVertexSharingRectangles() {
        rect2 = new Rectangle(30, 30, 20, 20);
        assertFalse(rect1.intersect(rect2));
    }


    /**
     * Testing Non-Intersecting Rectangles, Edge Sharing Only
     */
    public void testEdgeSharingRectangles() {
        rect2 = new Rectangle(30, 10, 20, 20);
        assertFalse(rect1.intersect(rect2));
    }


    /**
     * Testing Non-Intersecting Rectangles, Overlapping Rectangles
     */
    public void testOverlappingRectangles() {
        rect2 = new Rectangle(15, 15, 25, 25);
        assertTrue(rect1.intersect(rect2));
    }


    /**
     * Testing Non-Intersecting Rectangles, Different Coordinates
     */
    public void testEqualsDifferentCoordinates() {
        rect2 = new Rectangle(20, 20, 30, 30);
        assertFalse(rect1.equals(rect2));
    }


    /**
     * Testing Non-Intersecting Rectangles, Different Coordinates
     */
    public void testEqualsNonRectangle() {
        Object obj = new Object();
        assertFalse(rect1.equals(obj));
    }


    /**
     * One rectangle inside another. Rect 2 completely inside rect1
     */
    public void testOneRectangleInsideAnother() {
        rect2 = new Rectangle(15, 15, 5, 5);
        assertTrue(rect1.intersect(rect2));
    }


    /**
     * Rect1 completely inside rect2
     */
    public void testEnclosingOverlap() {
        rect2 = new Rectangle(5, 5, 30, 30);
        assertTrue(rect1.intersect(rect2));
    }


    /**
     * Partially overlapping and overlapping
     */
    public void testPartialOverlap() {
        rect2 = new Rectangle(25, 25, 20, 20);
        assertTrue(rect1.intersect(rect2));
    }


    /**
     * Sharing a common edge and overlapping
     */
    public void testCommonEdgeWithOverlap() {
        rect2 = new Rectangle(10, 20, 20, 20);
        assertTrue(rect1.intersect(rect2));
    }


    /**
     * Sharing a common vertex and overlapping
     */
    public void testCommonVertexWithOverlap() {
        rect2 = new Rectangle(20, 20, 10, 10);
        assertTrue(rect1.intersect(rect2));
    }


    /**
     * Overlapping along an edge
     */
    public void testOverlappingEdges() {
        rect2 = new Rectangle(10, 10, 20, 30);
        assertTrue(rect1.intersect(rect2));
    }


    /**
     * rect1's right edge is at 30, rect2 starts at 31
     */
    public void testRectToTheLeft() {
        rect2 = new Rectangle(31, 10, 10, 10);
        assertFalse(rect1.intersect(rect2));
    }


    /**
     * Overlapping on X-axis
     */
    public void testXAxisOverlap() {
        rect2 = new Rectangle(15, 15, 20, 20);
        assertTrue(rect1.intersect(rect2));
    }


    /**
     * rect1's left edge is at 10, rect2 ends at 9
     */
    public void testRectToTheRight() {
        rect2 = new Rectangle(0, 10, 9, 10);
        assertFalse(rect1.intersect(rect2));
    }


    /**
     * rect1's bottom edge is at 30, rect2 starts at 31
     */
    public void testRectAbove() {
        rect2 = new Rectangle(10, 31, 10, 10);
        assertFalse(rect1.intersect(rect2));
    }


    /**
     * rect1's top edge is at 10, rect2 ends at 9
     */
    public void testRectBelow() {
        rect2 = new Rectangle(10, 0, 10, 9);
        assertFalse(rect1.intersect(rect2));
    }


    /**
     * Overlapping on Y-axis
     */
    public void testYAxisOverlap() {
        rect2 = new Rectangle(15, 15, 20, 20);
        assertTrue(rect1.intersect(rect2));
    }


    /**
     * Tests for isInvalid method with zero width and positive height
     */
    public void testIsInvalidWithZeroWidth() {
        Rectangle zeroWidthRect = new Rectangle(10, 10, 0, 20);
        assertTrue(zeroWidthRect.isInvalid());
    }


    /**
     * Tests for isInvalid method with positive width and zero height
     */
    public void testIsInvalidWithZeroHeight() {
        Rectangle zeroHeightRect = new Rectangle(10, 10, 20, 0);
        assertTrue(zeroHeightRect.isInvalid());
    }


    /**
     * Tests for isInvalid method with negative width and positive height
     */
    public void testIsInvalidWithNegativeWidth() {
        Rectangle negWidthRect = new Rectangle(10, 10, -20, 20);
        assertTrue(negWidthRect.isInvalid());
    }


    /**
     * Tests for isInvalid method with positive width and negative height
     */
    public void testIsInvalidWithNegativeHeight() {
        Rectangle negHeightRect = new Rectangle(10, 10, 20, -20);
        assertTrue(negHeightRect.isInvalid());
    }


    /**
     * Tests for isInvalid method with zero width and height
     */
    public void testIsInvalidWithZeroWidthAndHeight() {
        Rectangle zeroWidthHeightRect = new Rectangle(10, 10, 0, 0);
        assertTrue(zeroWidthHeightRect.isInvalid());
    }


    /**
     * Test when xCoordinate matches but yCoordinate does not
     */
    public void testEqualsWithMatchingXCoordinateOnly() {
        rect2 = new Rectangle(10, 20, 20, 20);
        assertFalse(
            "Rectangles with only matching xCoordinate should not be equal",
            rect1.equals(rect2));
    }


    /**
     * Test when yCoordinate matches but xCoordinate does not
     */
    public void testEqualsWithMatchingYCoordinateOnly() {
        rect2 = new Rectangle(20, 10, 20, 20); // y matches, x does not
        assertFalse(
            "Rectangles with only matching yCoordinate should not be equal",
            rect1.equals(rect2));
    }


    /**
     * testing equals() should not be equal to null
     */
    public void testEqualsWithNull() {
        // Testing against null should always return false
        assertFalse(rect1.equals(null));
    }


    /**
     * testing equals() should not be equal
     */
    public void testEqualsWithDifferentClass() {
        // Testing against an object of a different class should return false
        Object diffClassObj = new Object();
        assertFalse(rect1.equals(diffClassObj));
    }


    /**
     * testing equals() should not be equal
     */
    public void testEqualsWithDifferentYCoordinate() {
        Rectangle differentY = new Rectangle(10, 20, 20, 20);
        assertFalse(rect1.equals(differentY));
    }


    /**
     * testing equals() should not be equal
     */
    public void testEqualsWithDifferentWidth() {
        Rectangle differentWidth = new Rectangle(10, 10, 30, 20);
        assertFalse(rect1.equals(differentWidth));
    }


    /**
     * testing equals() should not be equal
     */
    public void testEqualsWithDifferentHeight() {
        Rectangle differentHeight = new Rectangle(10, 10, 20, 30);
        assertFalse(rect1.equals(differentHeight));
    }

}
