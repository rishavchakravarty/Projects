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
 * This class holds a generic Key-value pair implementation. The purpose of this
 * class is to hold generic KVPair object which will be stored in the SkipList.
 * There is also a toString method for easily translating the objects contained
 * in the KVPair into a human readable string.
 * 
 * @author CS Staff
 * 
 * @version 2024-01-22
 * @param <K>
 *            Key to be used
 * @param <V>
 *            Value to be associated with the key
 */

// public class KVPair<K, V> implements Comparable<KVPair<K, V>> {
// Another Implementation choice is to require K to implement Comparable not
// KVPair
public class KVPair<K extends Comparable<? super K>, V> {

    // the object to be a key
    private K key;
    // the object to be the value at the key
    private V value;

    /**
     * The constructor assigns value to the key and value fields from user
     * specified
     * objects.
     * 
     * @param strKey
     *            the key for the KVPair
     * @param val
     *            the value for the KVPair
     */
    public KVPair(K strKey, V val) {
        this.key = strKey;
        this.value = val;
    }


    /**
     * Returns the key of this KVPair
     *
     * @return the key of the KVPair
     */
    public K getKey() {
        return key;
    }


    /**
     * Returns the value of this KVPair
     *
     * @return the value that the KVPair holds
     */
    public V getValue() {
        return value;
    }


    /**
     * Returns the KVPair in a human readable format.
     *
     * @return A human readable string representing the KVPair object
     */
    public String toString() {
        return "(" + key + ", " + value + ")";
    }
}
