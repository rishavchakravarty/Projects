import static org.junit.Assert.assertArrayEquals;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import student.TestCase;

/**
 * @author Archit Gupta, Kinjal Pandey, RIshav Chakravarty
 * @version 1.0
 */
public class MemManagerTest extends TestCase {
    private MemManager memManager;
    private static final int INITIAL_POOL_SIZE = 1024; // Example initial size

    /**
     * Sets up the tests that follow.
     */
    public void setUp() {
        memManager = new MemManager(INITIAL_POOL_SIZE);
    }


    private byte[] createRecordOfSize(int size) {
        byte[] record = new byte[size];
        for (int i = 0; i < size; i++) {
            record[i] = (byte)(i % 256);
        }
        return record;
    }

//
// public void testInsertAndRetrieve() {
// byte[] record = createRecordOfSize(100);
// Handle handle = memManager.insert(record);
// assertNotNull("Handle should not be null after insertion", handle);
// assertTrue("Record should start from the beginning"
// + " of the pool", handle
// .getStartPosition() == 0);
// byte[] retrieved = memManager.get(handle);
// assertArrayEquals("Retrieved data should"
// + " match the original", record,
// retrieved);
// }


    /**
     * Test memory expansion
     */
    public void testMemoryExpansion() {
        byte[] largeRecord = createRecordOfSize(900);
        memManager.insert(largeRecord);
        memManager.insert(createRecordOfSize(200)); // This should trigger an
                                                    // expansion

        assertTrue("Memory pool should expand", memManager
            .getMemoryPoolSize() > INITIAL_POOL_SIZE);
        assertTrue("Expansion flag should be set and then reset", memManager
            .checkAndResetExpanded());
    }

// public void testMergeBlocks() {
// byte[] record1 = createRecordOfSize(128);
// byte[] record2 = createRecordOfSize(128);
//
// Handle handle1 = memManager.insert(record1);
// Handle handle2 = memManager.insert(record2);
//
// memManager.remove(handle1);
// memManager.remove(handle2); // This should merge back to a
// single block
//
// Handle handle3 = memManager.insert(createRecordOfSize(256));
// assertEquals("Merged block should be reused", 0, handle3
// .getStartPosition());
// }


    /**
     * Test handling of multiple block sizes
     */
    public void testHandlingMultipleSizes() {
        byte[] smallRecord = createRecordOfSize(64);
        byte[] mediumRecord = createRecordOfSize(256);
        byte[] largeRecord = createRecordOfSize(512);

        Handle handleSmall = memManager.insert(smallRecord);
        Handle handleMedium = memManager.insert(mediumRecord);
        Handle handleLarge = memManager.insert(largeRecord);

        memManager.remove(handleMedium); // Remove medium to test handling of
                                         // different block sizes

        byte[] extraRecord = createRecordOfSize(256);
        Handle handleExtra = memManager.insert(extraRecord);
        assertEquals("Should reuse freed medium block", handleMedium
            .getStartPosition(), handleExtra.getStartPosition());
    }


    /**
     * Test that ensures the memory pool expands when an insertion would exceed
     * the current pool's boundary.
     */
    public void testInsertionExceedsMemoryPool() {
        byte[] largeRecord = createRecordOfSize(INITIAL_POOL_SIZE - 10);
        memManager.insert(largeRecord);

        byte[] exceedingRecord = createRecordOfSize(20);
        Handle handle = memManager.insert(exceedingRecord);

        assertTrue("Memory pool should have expanded", memManager
            .getMemoryPoolSize() > INITIAL_POOL_SIZE);
        assertNotNull("Handle should not be null for the new insertion",
            handle);

        assertTrue("New record should be placed in the expanded area", handle
            .getStartPosition() >= INITIAL_POOL_SIZE);

        assertTrue("Expansion flag should be set and then reset", memManager
            .checkAndResetExpanded());
    }


    /**
     * Test freeing blocks
     */
    public void testDumpFreeBlocks() {
        byte[] record = createRecordOfSize(500);
        Handle handle = memManager.insert(record);
        memManager.remove(handle);
        // Normally you'd capture the output from dump to verify it, but this
        // might require additional setup
        memManager.dump();
        // Verify dump output manually or with an output capture mechanism
    }

    // -------------------------------------------------


    /**
     * Test memory expansion when inserting records that exceed the initial pool
     * size.
     */
    public void testMemoryExpansion1() {
        byte[] largeRecord = createRecordOfSize(900);
        memManager.insert(largeRecord);
        memManager.insert(createRecordOfSize(200)); // This should trigger an
                                                    // expansion

        assertTrue("Memory pool should expand", memManager
            .getMemoryPoolSize() > INITIAL_POOL_SIZE);
        assertTrue("Expansion flag should be set and then reset", memManager
            .checkAndResetExpanded());
    }


    /**
     * Test merging of free blocks after deletion of adjacent blocks.
     */
    public void testMergeBlocks() {
        byte[] record1 = createRecordOfSize(128);
        byte[] record2 = createRecordOfSize(128);

        Handle handle1 = memManager.insert(record1);
        Handle handle2 = memManager.insert(record2);

        memManager.remove(handle1);
        memManager.remove(handle2); // This should merge back to a single block

        Handle handle3 = memManager.insert(createRecordOfSize(256));
        assertEquals("Merged block should be reused", 0, handle3
            .getStartPosition());
    }


    /**
     * Test handling multiple block sizes and ensure that blocks are reused
     * appropriately.
     */
    public void testHandlingMultipleSizes1() {
        byte[] smallRecord = createRecordOfSize(64);
        byte[] mediumRecord = createRecordOfSize(256);
        byte[] largeRecord = createRecordOfSize(512);

        Handle handleSmall = memManager.insert(smallRecord);
        Handle handleMedium = memManager.insert(mediumRecord);
        Handle handleLarge = memManager.insert(largeRecord);

        memManager.remove(handleMedium); // Remove medium to test handling of
                                         // different block sizes

        byte[] extraRecord = createRecordOfSize(256);
        Handle handleExtra = memManager.insert(extraRecord);
        assertEquals("Should reuse freed medium block", handleMedium
            .getStartPosition(), handleExtra.getStartPosition());
    }


    /**
     * Test that the memory pool expands correctly when an insertion would
     * exceed the current pool's boundary.
     */
    public void testInsertionExceedsMemoryPool1() {
        byte[] largeRecord = createRecordOfSize(INITIAL_POOL_SIZE - 10);
        memManager.insert(largeRecord);

        byte[] exceedingRecord = createRecordOfSize(20);
        Handle handle = memManager.insert(exceedingRecord);

        assertTrue("Memory pool should have expanded", memManager
            .getMemoryPoolSize() > INITIAL_POOL_SIZE);
        assertNotNull("Handle should not be null for the new insertion",
            handle);
        assertTrue("New record should be placed in the expanded area", handle
            .getStartPosition() >= INITIAL_POOL_SIZE);
        assertTrue("Expansion flag should be set and then reset", memManager
            .checkAndResetExpanded());
    }


    /**
     * Test inserting edge case sizes.
     */
    public void testEdgeCaseInsertions() {
        byte[] exactFitRecord = createRecordOfSize(INITIAL_POOL_SIZE - 32);
        Handle exactFitHandle = memManager.insert(exactFitRecord);
        assertNotNull("Handle for exactly fitting record should not be null",
            exactFitHandle);

        byte[] tooLargeRecord = createRecordOfSize(INITIAL_POOL_SIZE + 1);
        Handle tooLargeHandle = memManager.insert(tooLargeRecord);
        assertNotNull("Handle for too large record should not be null",
            tooLargeHandle);
        assertTrue("Memory should have expanded for too large record",
            memManager.getMemoryPoolSize() > INITIAL_POOL_SIZE);
    }

    /// ----------------------------------------------------------


    /**
     * Test memory expansion
     */
    public void testMemoryExpansion2() {
        byte[] largeRecord = createRecordOfSize(900);
        memManager.insert(largeRecord);
        memManager.insert(createRecordOfSize(200)); // This should trigger an
                                                    // expansion

        assertTrue("Memory pool should expand", memManager
            .getMemoryPoolSize() > INITIAL_POOL_SIZE);
        assertTrue("Expansion flag should be set and then reset", memManager
            .checkAndResetExpanded());
    }


    /**
     * Test merging of free blocks after deletion of adjacent blocks.
     */
    public void testMergeBlocks1() {
        byte[] record1 = createRecordOfSize(128);
        byte[] record2 = createRecordOfSize(128);

        Handle handle1 = memManager.insert(record1);
        Handle handle2 = memManager.insert(record2);

        memManager.remove(handle1);
        memManager.remove(handle2); // This should merge back to a single block

        Handle handle3 = memManager.insert(createRecordOfSize(256));
        assertEquals("Merged block should be reused", 0, handle3
            .getStartPosition());
    }


    /**
     * Test handling multiple block sizes and ensure that blocks are reused
     * appropriately.
     */
    public void testHandlingMultipleSizes2() {
        byte[] smallRecord = createRecordOfSize(64);
        byte[] mediumRecord = createRecordOfSize(256);
        byte[] largeRecord = createRecordOfSize(512);

        Handle handleSmall = memManager.insert(smallRecord);
        Handle handleMedium = memManager.insert(mediumRecord);
        Handle handleLarge = memManager.insert(largeRecord);

        memManager.remove(handleMedium); // Remove medium to test handling of
                                         // different block sizes

        byte[] extraRecord = createRecordOfSize(256);
        Handle handleExtra = memManager.insert(extraRecord);
        assertEquals("Should reuse freed medium block", handleMedium
            .getStartPosition(), handleExtra.getStartPosition());
    }


    /**
     * Test that the memory pool expands correctly when an insertion would
     * exceed the current pool's boundary.
     */
    public void testInsertionExceedsMemoryPool2() {
        byte[] largeRecord = createRecordOfSize(INITIAL_POOL_SIZE - 10);
        memManager.insert(largeRecord);

        byte[] exceedingRecord = createRecordOfSize(20);
        Handle handle = memManager.insert(exceedingRecord);

        assertTrue("Memory pool should have expanded", memManager
            .getMemoryPoolSize() > INITIAL_POOL_SIZE);
        assertNotNull("Handle should not be null for the new insertion",
            handle);
        assertTrue("New record should be placed in the expanded area", handle
            .getStartPosition() >= INITIAL_POOL_SIZE);
        assertTrue("Expansion flag should be set and then reset", memManager
            .checkAndResetExpanded());
    }


    /**
     * Test inserting edge case sizes.
     */
    public void testEdgeCaseInsertions1() {
        byte[] exactFitRecord = createRecordOfSize(INITIAL_POOL_SIZE - 32);
        Handle exactFitHandle = memManager.insert(exactFitRecord);
        assertNotNull("Handle for exactly fitting record should not be null",
            exactFitHandle);

        byte[] tooLargeRecord = createRecordOfSize(INITIAL_POOL_SIZE + 1);
        Handle tooLargeHandle = memManager.insert(tooLargeRecord);
        assertNotNull("Handle for too large record should not be null",
            tooLargeHandle);
        assertTrue("Memory should have expanded for too large record",
            memManager.getMemoryPoolSize() > INITIAL_POOL_SIZE);
    }


    /**
     * Test for Insert Method
     */
    public void testInsertAndRetrieve() {
        byte[] record = createRecordOfSize(128);
        Handle handle = memManager.insert(record);
        assertNotNull("Handle should not be null after insertion", handle);
        assertTrue("Record should start from the beginning of the pool", handle
            .getStartPosition() == 0);
        byte[] retrieved = memManager.get(handle);
        assertArrayEquals("Retrieved data should match the original", record,
            retrieved);
    }


    /**
     * Test memory expansion during insertions.
     */
    public void testMemoryExpansionDuringInsertions() {
        // Insert a record that fits comfortably
        byte[] record = createRecordOfSize(INITIAL_POOL_SIZE - 128);
        memManager.insert(record);
        // Now insert a record that causes expansion
        byte[] largerRecord = createRecordOfSize(200);
        Handle handle = memManager.insert(largerRecord);
        assertTrue("Memory pool should expand", memManager
            .getMemoryPoolSize() > INITIAL_POOL_SIZE);
        assertTrue("Expansion flag should be set and then reset", memManager
            .checkAndResetExpanded());
        assertNotNull("Handle should not be null after expansion", handle);
        assertTrue("Handle should point to a position within the new memory",
            handle.getStartPosition() >= INITIAL_POOL_SIZE);
    }


    /**
     * Test inserting a record that exactly fits the remaining memory.
     */
    public void testExactFitInsertion() {
        byte[] record = createRecordOfSize(INITIAL_POOL_SIZE);
        Handle handle = memManager.insert(record);
        assertNotNull("Handle should not be null for exact fit", handle);
        assertTrue("Handle should start at position 0", handle
            .getStartPosition() == 0);
        assertTrue("Memory pool should not have expanded", memManager
            .getMemoryPoolSize() == INITIAL_POOL_SIZE);
    }


    /**
     * Test inserting records and then removing them.
     */
    public void testInsertAndRemove() {
        byte[] record1 = createRecordOfSize(100);
        byte[] record2 = createRecordOfSize(200);
        Handle handle1 = memManager.insert(record1);
        Handle handle2 = memManager.insert(record2);
        assertEquals("Memory pool size should remain the same",
            INITIAL_POOL_SIZE, memManager.getMemoryPoolSize());

        memManager.remove(handle1);
        memManager.remove(handle2);
        // Insert another record that fits in the previously removed space
        byte[] record3 = createRecordOfSize(300);
        Handle handle3 = memManager.insert(record3);
        assertNotNull("Handle should not be null after insertion", handle3);
    }


    /**
     * Test inserting and removing multiple records to check if free blocks
     * merge properly.
     */
    public void testMultipleInsertAndRemove() {
        byte[] record1 = createRecordOfSize(100);
        byte[] record2 = createRecordOfSize(200);
        byte[] record3 = createRecordOfSize(50);
        Handle handle1 = memManager.insert(record1);
        Handle handle2 = memManager.insert(record2);
        Handle handle3 = memManager.insert(record3);

        memManager.remove(handle1);
        memManager.remove(handle2);
        memManager.remove(handle3);

        Handle handle4 = memManager.insert(createRecordOfSize(350));
        assertEquals("Merged block should be reused", 0, handle4
            .getStartPosition());
    }


    /**
     * Test inserting and then expanding the memory.
     */
    public void testInsertWithExpansion() {
        byte[] record1 = createRecordOfSize(INITIAL_POOL_SIZE - 128);
        memManager.insert(record1);
        byte[] record2 = createRecordOfSize(200);
        Handle handle = memManager.insert(record2);
        assertNotNull("Handle should not be null after expansion", handle);
        assertTrue("Memory pool should have expanded", memManager
            .getMemoryPoolSize() > INITIAL_POOL_SIZE);
    }


    /**
     * Test the get method with various record sizes.
     */
    public void testGetMethodWithDifferentSizes() {
        byte[] smallRecord = createRecordOfSize(64);
        Handle smallHandle = memManager.insert(smallRecord);
        assertArrayEquals("Should return the same small record", smallRecord,
            memManager.get(smallHandle));

        byte[] mediumRecord = createRecordOfSize(256);
        Handle mediumHandle = memManager.insert(mediumRecord);
        assertArrayEquals("Should return the same medium record", mediumRecord,
            memManager.get(mediumHandle));

        byte[] largeRecord = createRecordOfSize(512);
        Handle largeHandle = memManager.insert(largeRecord);
        assertArrayEquals("Should return the same large record", largeRecord,
            memManager.get(largeHandle));
    }


    /**
     * Test removing non-existent records.
     */
    public void testRemoveNonExistentRecords() {
        byte[] record = createRecordOfSize(100);
        Handle handle = memManager.insert(record);
        Handle nonExistentHandle = new Handle(1000, 100);
        memManager.remove(nonExistentHandle);
        assertNotNull(
            "Removing non-existent handle should not affect existing handle",
            memManager.get(handle));
    }


    /**
     * Test for correct management of buddy blocks during merges.
     */
    public void testBuddyBlockMerging() {
        byte[] record1 = createRecordOfSize(256);
        byte[] record2 = createRecordOfSize(256);
        Handle handle1 = memManager.insert(record1);
        Handle handle2 = memManager.insert(record2);
        memManager.remove(handle1);
        memManager.remove(handle2);
        Handle handle3 = memManager.insert(createRecordOfSize(512));
        assertEquals("Merged buddy blocks should be reused", 0, handle3
            .getStartPosition());
    }
}
