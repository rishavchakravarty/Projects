import java.io.IOException;

/**
 * SeminarDB.java
 * This class is the main class for the Seminar Database.
 * It contains the main methods for inserting, deleting, and searching
 * seminar records, as well as printing the hash table and memory manager.
 * It also contains the main method for running the Seminar Database.
 * 
 * @author Archit Gupta, Kinjal Pandey
 * @version 1.0
 */
public class SeminarDB {

    private MemManager myMemman; // Memory Manager for handling seminar records
    private Hash myHashTable; // Hash table for indexing seminars by their IDs

    /**
     * Constructs a new SeminarDB object with the given initial memory size and
     * hash size.
     * 
     * @param initMemSize
     *            The initial memory size.
     * @param initHashSize
     *            The initial hash size.
     * @throws IOException
     *             If an I/O error occurs.
     */
    public SeminarDB(int initMemSize, int initHashSize) throws IOException {
        myMemman = new MemManager(initMemSize);
        myHashTable = new Hash(initHashSize);
    }


    /**
     * Inserts a new seminar record into the database.
     * 
     * @param sID
     *            The seminar ID.
     * @param stitle
     *            The title of the seminar.
     * @param sdate
     *            The date of the seminar.
     * @param slength
     *            The length of the seminar.
     * @param sx
     *            The x-coordinate of the seminar location.
     * @param sy
     *            The y-coordinate of the seminar location.
     * @param scost
     *            The cost of the seminar.
     * @param skeywords
     *            The keywords associated with the seminar.
     * @param sdesc
     *            The description of the seminar.
     * @throws Exception
     */
    public void insert(
        int sID,
        String stitle,
        String sdate,
        int slength,
        int sx,
        int sy,
        int scost,
        String[] skeywords,
        String sdesc)
        throws Exception {
        if (myHashTable.search(sID) != null) {
            System.out.println(
                "Insert FAILED - There is already a record with ID " + sID);
            return; // Early exit if the ID already exists, no further
                    // processing
        }

        Seminar seminar = new Seminar(sID, stitle, sdate, slength, (short)sx,
            (short)sy, scost, skeywords, sdesc);
        byte[] serializedSeminar = seminar.serialize();
        Handle handle = myMemman.insert(serializedSeminar);

        // Check for memory pool expansion right after attempting to insert the
        // record.
        if (myMemman.checkAndResetExpanded()) {
            System.out.println("Memory pool expanded to " + myMemman
                .getMemoryPoolSize() + " bytes");
        }

        myHashTable.insert(sID, handle, true);

        // Check for hash table expansion after inserting the record into the
        // hash table.
        if (myHashTable.checkAndResetExpanded()) {
            System.out.println("Hash table expanded to " + myHashTable
                .capacity() + " records");
        }

        // Print success message and details after all critical operations and
        // checks.
        System.out.println("Successfully inserted record with ID " + sID);
        System.out.println(seminar);
        System.out.println("Size: " + serializedSeminar.length);
    }


    /**
     * Deletes a seminar record from the database.
     * 
     * @param sID The ID of the seminar to delete.
     * @throws IOException If an I/O error occurs.
     */
    public void delete(int sID) throws IOException {
        Handle handle = myHashTable.search(sID);
        if (handle != null) {
            myMemman.remove(handle);
            myHashTable.delete(sID);
            System.out.println("Record with ID " + sID
                + " successfully deleted from the database");
        }
        else {
            System.out.println("Delete FAILED -- There is no record with ID "
                + sID);
        }
    }


    /**
     * Searches for a seminar record in the database.
     * 
     * @param sID The ID of the seminar to search for.
     * @throws IOException If an I/O error occurs.
     * @throws Exception If an error occurs while searching for the record.
     */
    public void search(int sID) throws IOException, Exception {
        Handle handle = myHashTable.search(sID);
        if (handle != null) {
            byte[] recordData = myMemman.get(handle);
            Seminar seminar = Seminar.deserialize(recordData);
            System.out.println("Found record with ID " + sID + ":");
            System.out.println(seminar); // Print the found seminar details
        }
        else {
            System.out.println("Search FAILED -- There is no record with ID "
                + sID);
        }
    }


    /**
     * Prints the hash table and returns the number of active records.
     * 
     * @return The number of active records in the hash table.
     * @throws IOException
     */
    public int hashprint() throws IOException {
        myHashTable.printTable();
        return myHashTable.getSize(); // Return the number of active records
    }


    /**
     * Prints the free block list of the memory manager.
     */
    public void memmanprint() {
        myMemman.dump(); // Prints the free block list
    }
}
