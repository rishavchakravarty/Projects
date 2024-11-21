
import student.TestCase;

/**
 * This class tests the KVPair class so that the member methods work properly
 * and that the expected behavior occurs.
 * 
 * @author CS Staff
 * 
 * @version 2024.1
 */
public class KVPairTest extends TestCase {

    private KVPair<String, Rectangle> rectangle1;
    private KVPair<String, Rectangle> rectangle2;

    /**
     * Sets up the test cases
     */
    public void setUp() {
        // Initialize two KVPair instances with different keys and values
        Rectangle r1 = new Rectangle(1, 1, 1, 1); // Assuming Rectangle is
                                                  // defined elsewhere
        Rectangle r2 = new Rectangle(2, 2, 2, 2);
        rectangle1 = new KVPair<>("rectangle1", r1);
        rectangle2 = new KVPair<>("rectangle2", r2);
    }


    /**
     * Test the getKey method.
     * should return the correct key
     */
    public void testGetKey() {
        assertEquals("rectangle1", rectangle1.getKey());
        assertEquals("rectangle2", rectangle2.getKey());
    }


    /**
     * Test the getValue method.
     * should return the correct value
     */
    public void testGetValue() {
        // Directly comparing Rectangle objects assuming the equals method is
        // properly overridden.
        assertEquals(new Rectangle(1, 1, 1, 1), rectangle1.getValue());
        assertEquals(new Rectangle(2, 2, 2, 2), rectangle2.getValue());
    }


    /**
     * Test the toString method.
     */
    public void testToString() {
        // Directly using Rectangle's toString output for comparison
        String expectedOutput1 = "(rectangle1, 1, 1, 1, 1)";
        String expectedOutput2 = "(rectangle2, 2, 2, 2, 2)";
        assertEquals(expectedOutput1, rectangle1.toString());
        assertEquals(expectedOutput2, rectangle2.toString());
    }


    /**
     * Test Rectangle equality.
     * rectangles with same dimensions should be equal
     */
    public void testRectangleEquality() {
        Rectangle r1 = rectangle1.getValue();
        Rectangle r2 = new Rectangle(1, 1, 1, 1);
        Rectangle r3 = new Rectangle(2, 2, 2, 2);

        assertTrue(r1.equals(r2));
        assertFalse(r1.equals(r3));
    }


    /**
     * Test Rectangle intersection.
     */
    public void testRectangleIntersection() {
        Rectangle r1 = rectangle1.getValue();
        Rectangle r2 = rectangle2.getValue();
        Rectangle r3 = new Rectangle(0, 0, 3, 3); // Overlaps with r1

        assertTrue(r1.intersect(r3)); // r1 intersects with r3
        assertFalse(r1.intersect(r2)); // r1 does not intersect with r2
    }


    /**
     * Test Rectangle invalidity.
     * tests all different types for invalid rectangles
     */
    public void testRectangleInvalidity() {
        Rectangle validRectangle = new Rectangle(0, 0, 10, 10);
        Rectangle invalidRectangle = new Rectangle(0, 0, -1, -1);

        assertFalse(validRectangle.isInvalid());
        assertTrue(invalidRectangle.isInvalid());
    }
}
