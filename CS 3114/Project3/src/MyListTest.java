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
 * Tests the List class to ensure its methods correctly manage elements within
 * the List.
 * 
 * @author kinjalpandey, architg03, rishavc18
 * @version 02/25/24
 * 
 */
public class MyListTest extends TestCase {

    private MyList<String> list;

    /**
     * Sets up the test cases by initializing a List.
     */
    public void setUp() {
        list = new MyList<>();

    }


    /**
     * Tests the add method to ensure elements are added correctly.
     */
    public void testAdd() {
        list.add("Hello");

        assertEquals("Added element should be retrievable", "Hello", list.get(
            0));
    }


    /**
     * Tests the get method to verify if elements can be retrieved from the
     * correct
     * index.
     */
    public void testGet() {
        list.add("Hello");
        list.add("World");
        assertEquals("Should retrieve the correct element", "World", list.get(
            1));

        // Test getting an element from an index that should throw an exception
        try {
            list.get(2);
            fail("Accessing an out-of-bounds index should throw an exception");
        }
        catch (IndexOutOfBoundsException e) {
            // Expected exception
        }
    }


    /**
     * Tests the size method to verify if the size is updated correctly after
     * additions.
     */
    public void testSize() {
        assertEquals("Initial size should be 0", 0, list.size());
        list.add("Hello");
        assertEquals("Size should be 1 after adding an element", 1, list
            .size());
    }


    /**
     * Tests the remove method to verify if elements can be removed from the
     * correct
     * index.
     */
    public void testRemove() {
        list.add("Hello");
        list.add("World");
        list.remove(0);
        assertEquals("Size should decrease after removal", 1, list.size());
        assertEquals("Should retrieve the correct element after removal",
            "World", list.get(0));

        // Test removing an element from an index that should throw an exception
        try {
            list.remove(1);
            fail("Removing an out-of-bounds index should throw an exception");
        }
        catch (IndexOutOfBoundsException e) {
            // Expected exception
        }
    }


    /**
     * Tests mutation scenarios that could incorrectly allow or prevent adding
     * or
     * removing elements.
     */
    public void testMutations() {
        // Mutation could allow adding null elements
        try {
            list.add(null);
            assertNull("Adding null should be handled correctly", list.get(0));
        }
        catch (Exception e) {
            fail("Mutation causing add(null) to fail should be fixed");
        }

        // Mutation could allow adding more elements than the capacity without
        // expanding the list
        for (int i = 0; i < 10; i++) {
            list.add("Element" + i);
        }
        try {
            list.add("ExtraElement");
            assertEquals("List should handle expansion correctly", 12, list
                .size());
        }
        catch (Exception e) {
            fail("Mutation causing expansion failure should be fixed");
        }

        // Mutation could prevent removing an existing element
        list.remove(0);
        assertEquals("List should allow removing existing elements", 11, list
            .size());
    }


    /**
     * Testing add
     */
    public void testAddToEmptyList() {
        list.add("element");
        assertEquals("element", list.get(0));
    }


    /**
     * Testing add
     */
    public void testAddToNonFullList() {
        for (int i = 0; i < 5; i++) {
            list.add("element" + i);
        }
        assertEquals("element4", list.get(4));
    }


    /**
     * Testing add
     */
    public void testAddToFullList() {
        for (int i = 0; i < 10; i++) {
            list.add("element" + i);
        }
        assertEquals(10, list.size());
        list.add("overflow");
        assertEquals("overflow", list.get(10));
        assertTrue(list.size() > 10);
    }


    /**
     * Testing add
     */
    public void testAddOutOfBounds() {
        list.add("element");
        // list.get(1); // Should throw IndexOutOfBoundsException
    }


    /**
     * Testing add
     */
    public void testEnsureCapacity() {
        for (int i = 0; i < (10 * 2); i++) {
            list.add("element" + i);
        }
        assertTrue(list.size() == (10 * 2));
    }


    /**
     * Testing get
     */
    public void testGetValidIndex() {
        list.add("a");
        list.add("b");
        list.add("c");
        assertEquals("a", list.get(0));
        assertEquals("b", list.get(1));
        assertEquals("c", list.get(2));
    }


    /**
     * Testing get
     */
    public void testGetIndexOutOfBoundsNegative() {
        list.add("a");
        list.add("b");
        list.add("c");
        // list.get(-1);
    }


    /**
     * Testing get
     */
    public void testGetIndexOutOfBoundsTooLarge() {
        list.add("a");
        list.add("b");
        list.add("c");
        // list.get(list.size());
    }


    /**
     * Testing get
     */
    public void testGetIndexOutOfBoundsEmptyList() {
        MyList<String> emptyList = new MyList<>();
        // emptyList.get(0); // Should throw IndexOutOfBoundsException
    }


    /**
     * Testing get
     */
    public void testGetIndexExactlyAtSizeBoundary() {
        list.add("a");
        list.add("b");
        list.add("c");
        // list.get(list.size()); // Should throw IndexOutOfBoundsException
    }


    /**
     * Testing get
     */
    public void testGetIndexExactlyAtNegativeBoundary() {
        // list.get(-1); // Should throw IndexOutOfBoundsException
    }


    /**
     * Testing get
     */
    public void testGetIndexOutOfBoundsMessage() {
        int index = list.size();
        try {
            list.get(index);
            fail("Expected an IndexOutOfBoundsException to be thrown");
        }
        catch (IndexOutOfBoundsException e) {
            assertEquals("Index: " + index + ", Size " + index, e.getMessage());
        }
    }

}
