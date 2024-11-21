/**
 * Hash table implementation for the SeminarDB database.
 * *
 * 
 * @author {Archit Gupta, Kinjal Pandey, RIshav Chakravarty}
 * @version {1.0}
 */
public class Hash {
    /**
     * Entry class represents an entry in the hash table. It contains the key
     * and the handle of the record.
     */
    private static final int TOMBSTONE = -1;
    /**
     * The table of entries.
     */
    private Entry[] table;
    /**
     * The number of active records.
     */
    private int size; // number of active records
    /**
     * The capacity of the table.
     */
    private int capacity;

    /**
     * Constructs a new hash table with the given initial capacity.
     * 
     * @param initialCapacity
     *            The initial capacity of the hash table.
     */
    public Hash(int initialCapacity) {
        this.capacity = initialCapacity;
        this.table = new Entry[capacity];
        this.size = 0;
    }

    /**
     * Entry class represents an entry in the hash table. It contains the key
     * and the handle of the record.
     */
    private class Entry {
        private int key;
        private Handle handle;

        Entry(int key, Handle handle) {
            this.key = key;
            this.handle = handle;
        }
    }

    /**
     * Hash function 1.
     * 
     * @param key
     *            The key to hash.
     * @return The hash value.
     */
    public int hash1(int key) {
        return key % capacity;
    }


    /**
     * Hash function 2.
     * 
     * @param key
     *            The key to hash.
     * @return The hash value.
     */
    public int hash2(int key) {
        return 1 + ((key / capacity) % (capacity / 2)) * 2;
    }


    /**
     * Insert the given key and handle into the hash table.
     * 
     * @param key
     *            The key to insert.
     * @param handle
     *            The handle to insert.
     * @param shouldPrint
     *            Whether to print messages.
     */
    public void insert(int key, Handle handle, boolean shouldPrint) {
        if (size >= capacity / 2) {
            resize(capacity * 2);
        }

        int probe = hash1(key);
        int offset = hash2(key);
        int tombstoneIndex = -1;

        while (table[probe] != null && table[probe].key != key) {
            if (table[probe].key == TOMBSTONE && tombstoneIndex == -1) {
                tombstoneIndex = probe;
            }
            probe = (probe + offset) % capacity;
        }

        if (table[probe] != null && table[probe].key == key) {
            if (shouldPrint) {
// System.out.println(
// "Insert FAILED - There is already a record with ID " + key);
            }
            return;
        }

        if (tombstoneIndex != -1) {
            probe = tombstoneIndex;
        }

        table[probe] = new Entry(key, handle);
        size++;
// if (shouldPrint) {
// System.out.println("Successfully inserted record with ID " + key);
// }
    }


    /**
     * Resize the hash table to the new capacity.
     */
    void resize(int newCapacity) {
        Entry[] oldTable = table;
        table = new Entry[newCapacity];
        capacity = newCapacity;
        size = 0; // Reset size to reinsert all non-tombstone entries

        for (Entry entry : oldTable) {
            if (entry != null && entry.key != TOMBSTONE) {
                insert(entry.key, entry.handle, false);
            }
        }
        System.out.println("Hash table expanded to " + capacity + " records");
    }


    /**
     * Delete the record with the given key from the hash table.
     * 
     * @param key
     *            The key to delete.
     */
    public void delete(int key) {
        int probe = hash1(key);
        int offset = hash2(key);

        while (table[probe] != null) {
            if (table[probe].key == key && table[probe].key != TOMBSTONE) {
                table[probe].key = TOMBSTONE; // Mark as deleted
                size--;
// System.out.println("Record with ID " + key
// + " successfully deleted from the database");
                return;
            }
            probe = (probe + offset) % capacity;
        }
// System.out.println("Delete FAILED -- There is no record with ID "
// + key);
    }


    /**
     * Get the capacity of the hash table.
     * 
     * @return The capacity of the hash table.
     */
    public int capacity() {
        return capacity;
    }


    /**
     * Search for the record with the given key in the hash table.
     * 
     * @param key
     *            The key to search for.
     * @return The handle of the record with the given key, or null if not
     *         found.
     */
    public Handle search(int key) {
        int probe = hash1(key);
        int offset = hash2(key);

        while (table[probe] != null) {
            if (table[probe].key == key) {
                return table[probe].handle;
            }
            probe = (probe + offset) % capacity;
        }
// System.out.println("Search FAILED -- There is no record with ID "
// + key);
        return null;
    }

    /**
     * Resize the hash table to the new capacity.
     * 
     * @param newCapacity
     *            The new capacity of the hash table.
     */
    private boolean resized = false;

    /**
     * Check if the hash table has been resized and reset the resized flag.
     * 
     * @return Whether the hash table has been resized.
     */
    public boolean checkAndResetExpanded() {
        if (resized) {
            resized = false;
            return true;
        }
        return false;
    }


    /**
     * Print the hash table.
     */
    public void printTable() {
        System.out.println("Hashtable:");
        for (int i = 0; i < capacity; i++) {
            if (table[i] != null && table[i].key != TOMBSTONE) {
                System.out.println(i + ": " + table[i].key);
            }
            else if (table[i] != null && table[i].key == TOMBSTONE) {
                System.out.println(i + ": TOMBSTONE");
            }
        }
        System.out.println("total records: " + size);
    }


    /**
     * Get the number of active records in the hash table.
     * 
     * @return The number of active records in the hash table.
     */
    public int getSize() {
        return size;
    }
}
