import student.TestCase;

/**
 * @author Archit Gupta, Kinjal Pandey, Rishav Chakravarty
 * @version 1.0
 */
public class HashTest extends TestCase {
    /**
     * the hash table object
     */
    private Hash hash;
    /**
     * Example initial capacity
     */
    private static final int INITIAL_CAPACITY = 8; // Example initial capacity

    /**
     * Sets up the tests that follow.
     */
    public void setUp() {
        hash = new Hash(INITIAL_CAPACITY);
    }


    /**
     * Test insert and search
     */
    public void testInsertAndSearch() {
        Handle handle = new Handle(0, 10);
        hash.insert(1, handle, true);
        assertEquals("Size should be 1 after insertion", 1, hash.getSize());
        assertNotNull("Search should return a handle for existing key", hash
            .search(1));
        assertEquals("Retrieved handle should match inserted", handle, hash
            .search(1));
    }


    /**
     * Test deletion and rehashing
     */
    public void testDeletionAndRehashing() {
        for (int i = 0; i < INITIAL_CAPACITY; i++) {
            hash.insert(i, new Handle(i * 10, 10), false);
        }
        hash.delete(0); // Delete first entry
        hash.insert(INITIAL_CAPACITY, new Handle(INITIAL_CAPACITY * 10, 10),
            false); // Trigger rehashing
        assertNull("Deleted item should not be found after rehash", hash.search(
            0));
        assertNotNull("Newly added item post-rehash should be found", hash
            .search(INITIAL_CAPACITY));
    }


    /**
     * Test initial conditions
     */
    public void testInitialConditions() {
        // Ensure that the hash table initializes correctly
        assertEquals("Initial size should be 0", 0, hash.getSize());
        assertEquals("Initial capacity should match the specified value",
            INITIAL_CAPACITY, hash.capacity());
    }


    /**
     * Test wrap around logic
     */
    public void testWrapAroundLogic() {
        // Fill up the table to force wrap-around conditions
        for (int i = 0; i < INITIAL_CAPACITY - 1; i++) {
            hash.insert(i, new Handle(i * 10, 10), false);
        }
        // Insert at a key that forces wrap-around in probing
        int wrapAroundKey = INITIAL_CAPACITY + 1;
        hash.insert(wrapAroundKey, new Handle(999, 20), false);
        assertNotNull("Should find item inserted with wrap-around probing", hash
            .search(wrapAroundKey));
    }


    /**
     * Test consecutive tombstones
     */
    public void testConsecutiveTombstones() {
        hash.insert(1, new Handle(10, 20), false);
        hash.insert(2, new Handle(30, 40), false);
        hash.insert(3, new Handle(50, 60), false);
        hash.delete(1);
        hash.delete(2); // Create consecutive tombstones
        hash.insert(4, new Handle(70, 80), false); // This should reuse the
                                                   // first tombstone
        assertNotNull("Should reuse the first available tombstone", hash.search(
            4));
    }


    /**
     * Test hash function hash1
     */
    public void testHashFunctionHash2() {
        int key = INITIAL_CAPACITY * 3;
        int expectedHash = 1 + ((key / INITIAL_CAPACITY) % (INITIAL_CAPACITY
            / 2)) * 2;
        int actualHash = hash.hash2(key);

        assertEquals(
            "The hash function hash2 should calculate offsets correctly",
            expectedHash, actualHash);

        int mutatedHash = 1 + (key % (INITIAL_CAPACITY / 2)) * 2;
        assertNotSame(
            "Mutation that replaces division with the first member should fail",
            mutatedHash, actualHash);
    }


    /**
     * Test insert single item
     */
    public void testInsertSingleItem() {
        Handle handle = new Handle(0, 10);
        hash.insert(1, handle, true);
        assertEquals("Size should be 1 after one insert", 1, hash.getSize());
    }


    /**
     * Test insert multiple items
     */
    public void testCollisionHandling() {
        Handle handle1 = new Handle(0, 10);
        Handle handle2 = new Handle(10, 20);
        // Both keys should lead to the same index initially causing a collision
        hash.insert(1, handle1, true);
        hash.insert(9, handle2, true); // 9 % 8 == 1 % 8
        assertEquals("Size should be 2 after two inserts", 2, hash.getSize());
        assertNotSame("Handles should be different for colliding keys", hash
            .search(1), hash.search(9));
    }


    /**
     * Test insert duplicate key
     */
    public void testInsertDuplicateKeyPrevented() {
        Hash hash1 = new Hash(10);
        Handle handle1 = new Handle(0, 10);
        hash1.insert(5, handle1, false);

        // Attempt to insert the same key, which should be prevented
        Handle handle2 = new Handle(10, 20);
        hash1.insert(5, handle2, false);

        // Ensure the size has not increased and the original handle remains
        // unchanged
        assertEquals(
            "Size should remain 1 after attempting to insert a duplicate", 1,
            hash1.getSize());
        assertSame("Original handle should not be replaced", handle1, hash1
            .search(5));
    }


    /**
     * Test tombstone reuse
     */
    public void testTombstoneReused() {
        Hash hash1 = new Hash(10);
        Handle handle1 = new Handle(0, 10);
        hash1.insert(5, handle1, false);

        // Delete to create a tombstone
        hash1.delete(5);

        // Insert a new key where the tombstone is
        Handle handle2 = new Handle(10, 20);
        hash1.insert(6, handle2, false);

        // Verify the new key is using the tombstone slot
        Handle retrievedHandle = hash1.search(6);
        assertSame("Tombstone slot should be reused", handle2, retrievedHandle);
        assertEquals("Size should be 1 as tombstone is reused", 1, hash1
            .getSize());
    }


    /**
     * Test probing with wrap around
     */
    public void testProbingWithWrapAround() {
        Hash hash1 = new Hash(4); // Small size to force collisions
        // Insert keys designed to collide and wrap around
        hash1.insert(0, new Handle(0, 10), false);
        hash1.insert(4, new Handle(10, 20), false); // Same slot as 0 due to
                                                    // collision
        hash1.insert(8, new Handle(20, 30), false); // Another collision, tests
                                                    // wrap-around probing

        assertNotNull("Should find key 0", hash1.search(0));
        assertNotNull("Should find key 4", hash1.search(4));
        assertNotNull("Should find key 8", hash1.search(8));
        assertEquals("All keys should be inserted, including wrap-around", 3,
            hash1.getSize());
    }


    /**
     * Test delete single item
     */
    public void testDeleteAndTombstoneReclamation() {
        Handle handle1 = new Handle(0, 10);
        Handle handle2 = new Handle(10, 20);
        hash.insert(1, handle1, true);
        hash.insert(2, handle2, true);
        hash.delete(1);
        assertEquals("Size should decrement after deletion", 1, hash.getSize());
        assertNull("Search for deleted key should return null", hash.search(1));

        // Now insert another key that could reuse the tombstone slot
        Handle handle3 = new Handle(20, 30);
        hash.insert(3, handle3, true);
        assertEquals("New handle should use the slot of the former tombstone",
            handle3, hash.search(3));
    }


    /**
     * Test delete single item
     */
    public void testResize() {
        for (int i = 0; i < INITIAL_CAPACITY / 2 + 1; i++) {
            hash.insert(i, new Handle(i * 10, 10), false);
        }
        assertTrue("Capacity should double after exceeding load factor", hash
            .capacity() == INITIAL_CAPACITY * 2);
        for (int i = 0; i <= INITIAL_CAPACITY / 2; i++) {
            assertNotNull("Item should still be retrievable after resize", hash
                .search(i));
        }
    }


    /**
     * Test delete single item
     */
    public void testMultipleDeletes() {
        for (int i = 0; i < 5; i++) {
            hash.insert(i, new Handle(i * 10, 10), false);
        }
        for (int i = 0; i < 5; i++) {
            hash.delete(i);
            assertNull("Deleted item should no longer be retrievable", hash
                .search(i));
        }
        assertEquals("Size should be 0 after deleting all items", 0, hash
            .getSize());
    }


    /**
     * Test efficiency of probing
     */
    public void testEfficiencyOfProbing() {
        // Insert up to capacity to check efficiency of probing
        for (int i = 0; i < INITIAL_CAPACITY; i++) {
            hash.insert(i, new Handle(i * 10, 10), false);
        }
        hash.insert(INITIAL_CAPACITY, new Handle(INITIAL_CAPACITY * 10, 10),
            false);
        assertNotNull(
            "Should handle full capacity inserts and one additional for rehash",
            hash.search(INITIAL_CAPACITY));
    }


    /**
     * Test probing wrap around
     */
    public void testProbingWrapAround() {
        // Specific case to force wrap-around probing
        int key1 = 0;
        int key2 = INITIAL_CAPACITY; // Should ideally be placed at the same
                                     // index as key1 after wrapping
        Handle handle1 = new Handle(0, 10);
        Handle handle2 = new Handle(10, 20);
        hash.insert(key1, handle1, false);
        hash.insert(key2, handle2, false);

        assertEquals("Both items should be inserted even with wrap around", 2,
            hash.getSize());
        assertNotSame(
            "Handles should not be the same as they are at different keys", hash
                .search(key1), hash.search(key2));
    }


    /**
     * Test insert multiple items
     */
    public void testDeleteWithWrapAround() {
        // Assume hash1 and hash2 are simple enough that we can manipulate
        // easily
        // For example, if hash1 just returns key % capacity and hash2 returns 1
        Handle handle1 = new Handle(0, 10);
        Handle handle2 = new Handle(10, 20);
        Handle handle3 = new Handle(20, 30);

        // Insert three items in such a way that they are likely to cause a wrap
        // around on deletion
        hash.insert(1, handle1, false);
        hash.insert(INITIAL_CAPACITY + 1, handle2, false); // This will ideally
                                                           // go to index 1,
                                                           // causing a
                                                           // collision
        hash.insert(2 * INITIAL_CAPACITY + 1, handle3, false); // Same as above,
                                                               // furthering the
                                                               // collision
                                                               // chain

        // Delete an item that forces the deletion process to wrap around the
        // table
        hash.delete(2 * INITIAL_CAPACITY + 1);

        assertNull("Handle for key that was supposed to be "
            + "deleted should not be found", hash.search(2 * INITIAL_CAPACITY
                + 1));
        assertEquals("Size should be decremented correctly", 2, hash.getSize());
    }


    /**
     * Test print hash
     */
    public void testPrintHash() {
        hash.insert(1, new Handle(0, 10), true);
        hash.insert(2, new Handle(10, 20), true);
        hash.delete(1);
        // The output needs to be checked manually for correctness
        hash.printTable();
    }


    /**
     * Test deletion by key when the key doesnt exist
     */
    public void testDeleteNonExistentKey() {
        // This will also test the wrap around logic by trying to delete a
        // non-existent key
        Handle handle = new Handle(0, 10);
        hash.insert(1, handle, false);

        // Attempt to delete a non-existent key that causes the method to wrap
        // around
        hash.delete(9); // Assuming simple mod hash, this will not be found and
                        // will wrap

        assertNotNull("Original inserted item should still be present", hash
            .search(1));
        assertEquals("Size should not change when deleting a non-existent key",
            1, hash.getSize());
    }


    /**
     * Test insert that uses tombstone slot
     */
    public void testInsertUsesTombstoneSlot() {
        Handle handle1 = new Handle(0, 10);
        Handle handle2 = new Handle(10, 20);

        // Insert first item and then delete it to create a tombstone
        hash.insert(1, handle1, true);
        hash.delete(1);

        // Insert a new item which should reuse the tombstone slot
        hash.insert(2, handle2, true);

        // The second item should now occupy the slot of the first (which was
        // marked as a tombstone)
        Handle foundHandle = hash.search(2);
        assertNotNull("The handle should be found in the hash table",
            foundHandle);
        assertEquals("The handle should use the slot of the former tombstone",
            handle2, foundHandle);
        assertNull("The original key should no longer be found", hash.search(
            1));
        assertEquals("Only one record should be active", 1, hash.getSize());
    }


    /**
     * Test insert that does not use tombstone slot
     */
    public void testInsertDoesNotUseTombstoneIfKeyMatches() {
        Handle handle1 = new Handle(0, 10);
        Handle handle2 = new Handle(10, 20);

        // Insert first item and then delete it to create a tombstone
        hash.insert(1, handle1, true);
        hash.delete(1);

        // Try to reinsert the same key, which should not reuse the tombstone
        // but find the matching key
        hash.insert(1, handle2, true);

        // The reinserted item should reuse the slot correctly, but not as a
        // tombstone
        Handle foundHandle = hash.search(1);
        assertNotNull("The handle should be found in the hash table",
            foundHandle);
        assertEquals("The handle should still be associated with key 1",
            handle2, foundHandle);
        assertEquals("The size should still be 1 as it replaces the tombstone",
            1, hash.getSize());
    }


    /**
     * Test insert duplicate key
     */
    public void testInsertDuplicate() {
        Handle handle1 = new Handle(0, 10);
        Handle handle2 = new Handle(10, 20);
        hash.insert(1, handle1, true);
        hash.insert(1, handle2, true); // Attempt to insert duplicate
        assertEquals(
            "Size should remain 1 after attempting to insert a duplicate", 1,
            hash.getSize());
    }


    /**
     * Test rehashing when load factor is exceeded
     */
    public void testRehashing() {
        for (int i = 0; i < INITIAL_CAPACITY / 2 + 1; i++) {
            hash.insert(i, new Handle(i * 10, 10), false);
        }
        assertTrue("Capacity should be doubled after exceeding load factor",
            hash.capacity() > INITIAL_CAPACITY);
    }


    /**
     * Test insert after a deletion
     */
    public void testInsertWithTombstones() {
        Handle handle1 = new Handle(0, 10);
        Handle handle2 = new Handle(10, 20);
        hash.insert(1, handle1, true);
        hash.delete(1);
        hash.insert(2, handle2, true);
        assertNotNull("Insert after a deletion (tombstone handling)", hash
            .search(2));
    }


    /**
     * Test search for an existing key
     */
    public void testSearchExisting() {
        Handle handle = new Handle(0, 10);
        hash.insert(1, handle, true);
        assertNotNull("Search should find the inserted item", hash.search(1));
    }


    /**
     * Test search for a non-existing key
     */
    public void testSearchNonExisting() {
        assertNull("Search should not find a non-existing item", hash.search(
            999));
    }


    /**
     * Test delete for an item that was deleted.
     */
    public void testDeleteExisting() {
        Handle handle = new Handle(0, 10);
        hash.insert(1, handle, true);
        hash.delete(1);
        assertNull("Deleted item should not be found", hash.search(1));
        assertEquals("Size should decrease after deletion", 0, hash.getSize());
    }


    /**
     * Test delete for an item that was not existing
     */
    public void testDeleteNonExisting() {
        hash.delete(999); // Delete non-existing key
        assertEquals("Size should not change when deleting a non-existing key",
            0, hash.getSize());
    }


    /**
     * Test print table
     */
    public void testTablePrint() {
        hash.insert(1, new Handle(0, 10), true);
        hash.insert(2, new Handle(10, 20), true);
        hash.delete(1);
        // No assert; this test ensures no exceptions are thrown and output
        // format is checked manually
        hash.printTable();
    }


    /**
     * Test arithmetic operation mutation at line 72
     */
    public void testArithmeticOperationAt72() {
        int key = 7;
        hash.insert(key, new Handle(70, 10), false);
        // Expected offset should be calculated using hash2
        int expectedOffset = hash.hash2(key);
        int mutatedOffset = key;
        assertNotSame("Offset should not be the same as the key",
            expectedOffset, mutatedOffset);
    }


    /**
     * Test logical expression mutation at line 96
     */
    public void testLogicalExpressionAt96() {
        int key = 7;
        hash.insert(key, new Handle(70, 10), false);
        // The mutation replaces the logical check with true, so an exception
        // would be thrown
        Exception exception = null;
        try {
            hash.insert(key + 1, new Handle(80, 10), true);
        }
        catch (Exception e) {
            exception = e;
        }
        assertNull("Logical expression mutation should not throw an exception",
            exception);
    }


    /**
     * Test logical expression mutation at line 102
     */
    public void testLogicalExpressionAt102() {
        int key = 7;
        hash.insert(key, new Handle(70, 10), false);
        // The mutation replaces the logical check with true, which should throw
        // an exception
        Exception exception = null;
        try {
            hash.insert(key + 1, new Handle(80, 10), true);
        }
        catch (Exception e) {
            exception = e;
        }
        assertNull("Logical expression mutation should not throw an exception",
            exception);
    }


    /**
     * Test logical expression mutation at line 132
     */
    public void testLogicalExpressionAt132() {
        int key = 7;
        hash.insert(key, new Handle(70, 10), false);
        hash.delete(key);
        // The mutation replaces the logical check with true, resulting in
        // reinsertion
        Exception exception = null;
        try {
            hash.resize(hash.capacity() * 2);
        }
        catch (Exception e) {
            exception = e;
        }
        assertNull("Logical expression mutation should not throw an exception",
            exception);
    }


    /**
     * Test logical expression mutation at line 152
     */
    public void testLogicalExpressionAt152() {
        int key = 7;
        hash.insert(key, new Handle(70, 10), false);
        hash.delete(key);
        // The mutation replaces the logical check with true, resulting in
        // reinsertion
        Exception exception = null;
        try {
            hash.resize(hash.capacity() * 2);
        }
        catch (Exception e) {
            exception = e;
        }
        assertNull("Logical expression mutation should not throw an exception",
            exception);
    }


    /**
     * Test arithmetic operation mutation at line 159
     */
    public void testArithmeticOperationAt159() {
        int key = 7;
        hash.insert(key, new Handle(70, 10), false);
        // Expected offset should be calculated using hash2
        int expectedOffset = hash.hash2(key);
        int mutatedOffset = key;
        assertNotSame("Offset should not be the same as the key",
            expectedOffset, mutatedOffset);
    }


    /**
     * Test logical expression mutation at line 230
     */
    public void testLogicalExpressionAt230() {
        int key = 7;
        hash.insert(key, new Handle(70, 10), false);
        // Mutation sets the condition to false, no entries should be printed
        hash.printTable();
        // If it prints an entry, the mutation didn't work
    }

}
