import java.util.ArrayList;
import java.util.Iterator;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import student.TestCase;
import student.TestableRandom;

/**
 * This class tests the methods of SkipList class
 * 
 * @author CS Staff
 * 
 * @version 2024-01-22
 */

public class SkipListTest extends TestCase {

    private final ByteArrayOutputStream outContent =
        new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private SkipList<String, Rectangle> sl;
    private KVPair<String, Rectangle> rectangle1;
    private KVPair<String, Rectangle> rectangle2;
    private Rectangle r1;
    private Rectangle r2;

    /**
     * Sets up the test cases
     */
    public void setUp() {
        System.setOut(new PrintStream(outContent));
        sl = new SkipList<String, Rectangle>();
        r1 = new Rectangle(1, 1, 1, 1);
        r2 = new Rectangle(2, 2, 2, 2);
        rectangle1 = new KVPair<>("rectangle1", r1);
        rectangle2 = new KVPair<>("rectangle2", r2);

    }


    /**
     * Clears for new test
     */
    public void tearDown() {
        System.setOut(originalOut);
    }


    /**
     * Testing remove() function
     * should be able to remove key
     */
    public void testRemoveExistingKey() {
        sl.insert(rectangle1);
        assertEquals(1, sl.size());
        assertNotNull(sl.remove("rectangle1"));
        assertEquals(0, sl.size());
    }


    /**
     * Testing remove() function
     * should not be able to remove key
     */
    public void testRemoveNonExistingKey() {
        sl.insert(rectangle1);
        assertEquals(1, sl.size());
        assertNull(sl.remove("NonExistingKey"));
        assertEquals(1, sl.size());
    }


    /**
     * Testing removeByValue() function
     * should be able to remove rectangle
     */
    public void testRemoveByValueExisting() {
        sl.insert(rectangle1);
        assertEquals(1, sl.size());
        assertNotNull(sl.removeByValue(r1));
        assertEquals(0, sl.size());
    }


    /**
     * Testing iterator() function
     */
    public void testIteratorSequence() {
        sl.insert(rectangle1);
        sl.insert(rectangle2);
        Iterator<KVPair<String, Rectangle>> it = sl.iterator();

        assertTrue(it.hasNext());
        assertEquals(rectangle1, it.next());
        assertTrue(it.hasNext());
        assertEquals(rectangle2, it.next());
        assertFalse(it.hasNext());
    }


    /**
     * Testing randomLetter() function
     */
    public void testRandomLevel() {
        TestableRandom.setNextBooleans(true, true, true, false); // Expect level
                                                                 // 4
        assertEquals(4, sl.randomLevel());
        TestableRandom.setNextBooleans(false); // Expect level 1
        assertEquals(1, sl.randomLevel());
    }


    /**
     * Testing search() function
     */
    public void testInsertAndSearch() {
        sl.insert(rectangle1);
        sl.insert(rectangle2);
        assertEquals(2, sl.size());

        ArrayList<KVPair<String, Rectangle>> searchResult1 = sl.search(
            "rectangle1");
        assertEquals(1, searchResult1.size());
        assertTrue(searchResult1.contains(rectangle1));

        ArrayList<KVPair<String, Rectangle>> searchResult2 = sl.search(
            "rectangle2");
        assertEquals(1, searchResult2.size());
        assertTrue(searchResult2.contains(rectangle2));

        assertTrue(sl.search("Rectangle_3").isEmpty());
    }


    /**
     * Testing remove() function
     * should be able to remove rectangles
     */
    public void testRemove() {

        assertNull(sl.remove("rectangle1"));

        sl.insert(rectangle1);
        sl.insert(rectangle2);

        assertNotNull(sl.remove("rectangle1"));
        assertEquals(1, sl.size());

        assertNull(sl.remove("Rectangle_3"));
        assertEquals(1, sl.size());
    }


    /**
     * Testing removeByValue() function
     * should be able to remove rectangle
     */
    public void testRemoveByValue() {

        sl.insert(rectangle1);
        sl.insert(new KVPair<>("rectangle1_dup", r1));

        assertNotNull(sl.removeByValue(r1));
        assertEquals(1, sl.size());

        assertNull(sl.removeByValue(new Rectangle(10, 10, 10, 10)));
    }


    /**
     * Testing dump() function
     */
    public void testDump() {
        sl.insert(rectangle1);
        sl.dump();
        String output = outContent.toString();
        assertTrue("Expected output to contain 'rectangle1'", output.contains(
            "rectangle1"));
    }


    /**
     * Testing size() function
     * should return size accurately
     */
    public void testSize() {
        assertEquals(0, sl.size());
        sl.insert(rectangle1);
        assertEquals(1, sl.size());
        sl.insert(rectangle2);
        assertEquals(2, sl.size());
        sl.remove("rectangle1");
        assertEquals(1, sl.size());
    }


    /**
     * Testing iterator() function
     */
    public void testIterator() {
        sl.insert(rectangle1);
        sl.insert(rectangle2);
        Iterator<KVPair<String, Rectangle>> it = sl.iterator();

        assertTrue(it.hasNext());
        KVPair<String, Rectangle> next = it.next();
        assertNotNull(next);

        int count = 0;
        while (it.hasNext()) {
            it.next();
            count++;
        }
        assertEquals(1, count);
    }


    /**
     * Testing iterator() function
     */
    public void testIteratorNoNext() {
        Iterator<KVPair<String, Rectangle>> it = sl.iterator();
        assertFalse(it.hasNext());
    }


    /**
     * Testing insert() function
     */
    public void testInsert() {
        sl.insert(rectangle1);
        assertEquals(1, sl.size());
        sl.insert(rectangle2);
        assertEquals(2, sl.size());
        assertEquals(rectangle1, sl.search("rectangle1").get(0));
    }


    /**
     * Testing search() function
     */
    public void testSearch() {
        sl.insert(rectangle1);
        sl.insert(rectangle2);
        ArrayList<KVPair<String, Rectangle>> result = sl.search("rectangle1");
        // assertEquals(1, result.size());
        assertEquals("rectangle1", result.get(0).getKey());
    }


    /**
     * Testing insert() function
     */
    public void testMultipleInsertionsWithSameKey() {
        sl.insert(rectangle1);
        sl.insert(new KVPair<>("rectangle1", r2));
        assertEquals(2, sl.size());
        ArrayList<KVPair<String, Rectangle>> results = sl.search("rectangle1");
        assertEquals(2, results.size());
    }


    /**
     * Testing insert() function
     */
    public void testSizeAfterMultipleOperations() {
        sl.insert(rectangle1);
        sl.insert(rectangle2);
        sl.remove("rectangle1");
        sl.remove("rectangle2");
        assertEquals(0, sl.size());
    }


    /**
     * Testing remove() function
     */
    public void testRemoveNullKey() {
        assertNull(sl.remove(null));
    }


    /**
     * Testing removeByValue() function
     */
    public void testSearchForNonexistentElements() {
        sl.insert(new KVPair<>("A", new Rectangle(1, 1, 1, 1)));
        ArrayList<KVPair<String, Rectangle>> result = sl.search("B");
        assertTrue("Search for a non-existing key should return an empty list",
            result.isEmpty());
    }


    /**
     * Testing remove() function
     */
    public void testRemoveWhileLoopVariants() {
        assertNull(sl.remove("NonExistingKey"));
    }


    /**
     * Testing remove() function
     */
    public void testRemovePreviousNull() {
        sl.insert(rectangle1);
        assertNotNull(sl.remove("rectangle1"));
    }


    /**
     * Testing removeByValue() function
     */
    public void testRemoveByValueNull() {
        assertNull(sl.removeByValue(null));
    }


    /**
     * Testing removeByValue() function
     */
    public void testRemoveByValueNull2() {
        sl.insert(null);
        assertNull(sl.removeByValue(null));
    }


    /**
     * Testing removeByValue() function
     */
    public void testRemoveByValueWhileLoopVariants() {
        assertNull(sl.removeByValue(new Rectangle(99, 99, 99, 99)));
    }


    /**
     * Testing remove() function
     */
    public void testRemoveLastElement() {
        sl.insert(new KVPair<>("A", new Rectangle(1, 1, 1, 1)));
        KVPair<String, Rectangle> removed = sl.remove("A");
        assertNotNull("Removed element should not be null", removed);
        assertEquals(
            "SkipList size after removing the last element should be 0", 0, sl
                .size());
    }


    /**
     * Testing removeByValue() function
     */
    public void testRemoveByValuePreviousNull() {
        sl.insert(rectangle1);
        assertNotNull(sl.removeByValue((new Rectangle(1, 1, 1, 1))));
    }


    /**
     * Testing insert() function
     */
    public void testInsertAtEnd() {
        sl.insert(rectangle1);
        sl.insert(new KVPair<>("B", new Rectangle(3, 3, 3, 3)));
        assertEquals(2, sl.size());
    }


    /**
     * Testing insert() function
     */
    public void testInsertBeforeExisting() {
        sl.insert(rectangle2);
        sl.insert(rectangle1);
        assertEquals(2, sl.size());
    }


    /**
     * Testing remove() function
     */
    public void testRemoveExisting() {
        sl.insert(rectangle1);
        assertNotNull(sl.remove("rectangle1"));
        assertEquals(0, sl.size());
    }


    /**
     * Testing remove() function
     */
    public void testRemoveNonExisting() {
        sl.insert(rectangle1);
        assertNull(sl.remove("NonExisting"));
        assertEquals(1, sl.size());
    }


    /**
     * Testing remove() function
     */
    public void testRemoveWithTraversal() {

        sl.insert(rectangle1);
        sl.insert(rectangle2);
        assertNotNull(sl.remove("rectangle2"));

    }


    /**
     * Testing remove() function
     */
    public void testRemoveFirstNode() {
        sl.insert(rectangle1);
        assertNotNull(sl.remove("rectangle1"));

    }


    /**
     * Testing remove() function
     */
    public void testRemoveDirectlyAfterHead() {
        sl.insert(rectangle1);
        assertNotNull(sl.remove("rectangle1"));
        assertEquals(0, sl.size());
    }


    /**
     * Testing remove() function
     */
    public void testRemoveByValueNonExisting() {
        sl.insert(rectangle1);
        assertNull(sl.removeByValue(new Rectangle(10, 10, 10, 10)));
        assertEquals(1, sl.size());
    }


    /**
     * Testing remove() function
     */
    public void testRemoveByValueWithTraversal() {
        sl.insert(rectangle1);
        sl.insert(new KVPair<>("rectangle1_dup", r1));
        assertNotNull(sl.removeByValue(r1));
        assertEquals(1, sl.size());
    }


    /**
     * Testing remove() function
     */
    public void testRemoveByValueDirectlyAfterHead() {
        sl.insert(rectangle1);
        assertNotNull(sl.removeByValue(r1));
        assertEquals(0, sl.size());
    }


    /**
     * Testing other scenarios
     */
    public void testInsertComplexScenario() {
        // Prepopulate the skip list with elements to create a complex structure
        sl.insert(new KVPair<>("M", new Rectangle(10, 10, 5, 5)));
        sl.insert(new KVPair<>("X", new Rectangle(20, 20, 5, 5)));
        sl.insert(new KVPair<>("A", new Rectangle(5, 5, 2, 2)));
        sl.insert(new KVPair<>("J", new Rectangle(15, 15, 3, 3)));

        assertEquals(4, sl.size());
        ArrayList<KVPair<String, Rectangle>> resultJ = sl.search("J");
        assertEquals(1, resultJ.size()); // Ensure "J" was found
        assertEquals(new Rectangle(15, 15, 3, 3), resultJ.get(0).getValue());
    }


    /**
     * 
     */
    public void testRemoveNullKeyAlt() {
        sl.insert(rectangle1);
        KVPair<String, Rectangle> result = sl.remove(null);
        assertNull(result);
        assertEquals(1, sl.size());
    }


    /**
     * Testing insert()
     */
    public void testInsertWithMultipleLevels() {
        // Pre-set the RNG to generate a predictable level increase for testing
        TestableRandom.setNextBooleans(true, true, false); // Ensures a level 3
                                                           // insert
        KVPair<String, Rectangle> highLevelPair = new KVPair<>("HighLevel",
            new Rectangle(5, 5, 5, 5));
        sl.insert(highLevelPair);

        // Insert another KVPair to test proper traversal and insertion
        KVPair<String, Rectangle> newPair = new KVPair<>("Test", new Rectangle(
            10, 10, 10, 10));
        sl.insert(newPair);

        // Verify by searching for the newPair ensures it was inserted correctly
        assertFalse(sl.search("Test").isEmpty());
        assertEquals("Test", sl.search("Test").get(0).getKey());
    }


    /**
     * Testing remove()
     */
    public void testRemoveAdjustsHeadLevel() {
        TestableRandom.setNextBooleans(true, true, true, false); // Setup for a
                                                                 // higher level
                                                                 // insert
        sl.insert(rectangle1);
        // Assume rectangle1 has a higher level due to RNG setup
        sl.remove("rectangle1");

        // Attempt to insert another KVPair and ensure it is inserted at the
        // base level
        sl.insert(rectangle2);
        // Search to verify insertion and indirectly check head level adjustment
        assertEquals("rectangle2", sl.search("rectangle2").get(0).getKey());
    }


    /**
     * Testing remove()
     */
    public void testRemoveUpdatesForwardPointers() {
        // Pre-set RNG for predictable level assignment
        TestableRandom.setNextBooleans(true, false); // Ensures a level 2 insert
                                                     // for rectangle1
        sl.insert(rectangle1);
        TestableRandom.setNextBooleans(false); // Ensures a base level insert
                                               // for rectangle2
        sl.insert(rectangle2);

        sl.remove("rectangle1");
        // Verify rectangle2 is still reachable, indicating forward pointers
        // were updated correctly
        assertFalse(sl.search("rectangle2").isEmpty());
    }


    /**
     * Testing remove()
     */
    public void testRemoveByValueFirstElement() {
        sl.insert(rectangle1);
        assertNotNull(sl.removeByValue(r1));
        // After removal, SkipList size should be zero, indicating the first
        // element was removed correctly
        assertEquals(0, sl.size());
    }

}
