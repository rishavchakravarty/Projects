import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import student.TestableRandom;

/**
 * This class implements SkipList data structure and contains an inner SkipNode
 * class which the SkipList will make an array of to store data.
 * 
 * @author CS Staff
 * 
 * @version 2024-01-22
 * @param <K>
 *            Key
 * @param <V>
 *            Value
 */
public class SkipList<K extends Comparable<? super K>, V>
    implements Iterable<KVPair<K, V>> {
    private SkipNode head; // First element (Sentinel Node)
    private int size; // number of entries in the Skip List
    private Random rng;

    /**
     * Initializes the fields head, size and level
     */
    public SkipList() {
        head = new SkipNode(null, 0);
        size = 0;
        this.rng = new TestableRandom();
    }


    /**
     * returns a random level (using geometric distribution), minimum of 1
     * keep this method private. Since, we do not have any methods to call
     * this method at this time, we keep this publicly accessible and testable.
     * 
     * @return int level of skiplist
     */

    public int randomLevel() {
        int level = 1;
        while (rng.nextBoolean())
            level++;
        return level;
    }


    /**
     * Searches for the KVPair using the key which is a Comparable object.
     * 
     * @param key
     *            key to be searched for
     * @return ArrayList<KVPair<K, V>>
     *         KVPair sol to search query
     */
    public ArrayList<KVPair<K, V>> search(K key) {
        ArrayList<KVPair<K, V>> result = new ArrayList<>();
        SkipNode current = head;
        for (int i = head.level; i >= 0; i--) {
            while (current.forward[i] != null && current.forward[i].element()
                .getKey().compareTo(key) < 0) {
                current = current.forward[i];
            }
        }
        current = current.forward[0];
        while (current != null && current.element().getKey().compareTo(
            key) == 0) {
            result.add(current.element());
            current = current.forward[0];
        }
        return result;
    }


    /**
     * @return the size of the SkipList
     */
    public int size() {
        return size;
    }


    /**
     * Inserts the KVPair in the SkipList at its appropriate spot as designated
     * by its lexicoragraphical order.
     * 
     * @param it
     *            the KVPair to be inserted
     */
    @SuppressWarnings("unchecked")
    public void insert(KVPair<K, V> it) {
        int newLevel = randomLevel();
        if (newLevel > head.level) {
            adjustHead(newLevel);
        }
        SkipNode[] update = (SkipNode[])Array.newInstance(
            SkipList.SkipNode.class, head.level + 1);
        SkipNode x = head;
        for (int i = head.level; i >= 0; i--) {
            while (x.forward[i] != null && x.forward[i].element().getKey()
                .compareTo(it.getKey()) < 0) {
                x = x.forward[i];
            }
            update[i] = x;
        }
        x = new SkipNode(it, newLevel);
        for (int i = 0; i <= newLevel; i++) {
            x.forward[i] = update[i].forward[i];
            update[i].forward[i] = x;
        }
        size++;
    }


    /**
     * Increases the number of levels in head so that no element has more
     * indices than the head.
     * 
     * @param newLevel
     *            the number of levels to be added to head
     */
    public void adjustHead(int newLevel) {
        SkipNode temp = head;
        head = new SkipNode(null, newLevel);
        for (int i = 0; i <= temp.level; i++) {
            head.forward[i] = temp.forward[i];
        }
        head.level = newLevel;
    }


    /**
     * Removes the KVPair that is associated with the key passed in as a
     * parameter.
     * Returns the removed KVPair if the pair was valid (i.e., existed in the
     * data
     * structure)
     * and null if the pair was not found or invalid.
     *
     * @param key
     *            Key of the KVPair to remove.
     * @return the removed KVPair if the pair was valid, or null if it was not
     *         found
     *         or invalid.
     */
    @SuppressWarnings("unchecked")
    public KVPair<K, V> remove(K key) {
        if (key == null) {
            return null;
        }

        // Use Object array for update references
        Object[] update = new Object[head.level + 1];
        SkipNode current = head;

        // Traverse the list to find the target node and keep track of the
        // update path
        for (int i = head.level; i >= 0; i--) {
            while (current.forward[i] != null && current.forward[i].element()
                .getKey().compareTo(key) < 0) {
                current = current.forward[i];
            }
            update[i] = current;
        }

        // Move to the next node, which might be the node to remove
        current = current.forward[0];

        // If it's the target node, update the forward references of the nodes
        // in the update path
        if (current != null && current.element().getKey().compareTo(key) == 0) {
            for (int i = 0; i <= head.level; i++) {
                if (((SkipNode)update[i]).forward[i] != current)
                    break;
                ((SkipNode)update[i]).forward[i] = current.forward[i];
            }

            // Adjust the head level if necessary
            while (head.level > 0 && head.forward[head.level] == null) {
                head.level--;
            }
            size--;
            return current.element();
        }

        return null;
    }


    /**
     * Removes a KVPair with the specified value.
     * 
     * @param val
     *            the value of the KVPair to be removed
     * @return returns true if the removal was successful
     */
    public KVPair<K, V> removeByValue(V val) {
        if (val == null) {
            return null;
        }

        SkipNode previous = null;
        SkipNode current = head.forward[0]; // Start from the first actual node
                                            // after head

        while (current != null) {
            // Check if the current node's value matches the value to be removed
            if (current.element().getValue().equals(val)) {
                // Found the node to remove
                if (previous == null) {
                    // If the matching node is the first node after head
                    head.forward[0] = current.forward[0];
                }
                else {
                    // If the matching node is not the first node
                    previous.forward[0] = current.forward[0];
                }
                size--; // Decrement the size of the skip list
                return current.element(); // Return the removed KVPair
            }
            // Move to the next node
            previous = current;
            current = current.forward[0];
        }

        // If no matching value is found
        return null;
    }


    /**
     * Prints out the SkipList in a human readable format to the console.
     */
    public void dump() {
        System.out.println("SkipList dump:");
        System.out.println("Node with depth " + (head.level + 1)
            + ", Value null");
        if (size == 0) {
            System.out.println("SkipList size is: 0");
            return;
        }
        SkipNode current = head.forward[0];
        while (current != null) {
            System.out.println("Node with depth " + (current.level + 1)
                + ", value " + current.element());
            current = current.forward[0];
        }
        System.out.println("SkipList size is: " + size);
    }

    /**
     * This class implements a SkipNode for the SkipList data structure.
     * 
     * @author CS Staff
     * 
     * @version 2016-01-30
     */
    private class SkipNode {

        // the KVPair to hold
        private KVPair<K, V> pair;
        // An array of pointers to subsequent nodes
        private SkipNode[] forward;
        // the level of the node
        private int level;

        /**
         * Initializes the fields with the required KVPair and the number of
         * levels from the random level method in the SkipList.
         * 
         * @param tempPair
         *            the KVPair to be inserted
         * @param level
         *            the number of levels that the SkipNode should have
         */
        @SuppressWarnings("unchecked")
        public SkipNode(KVPair<K, V> tempPair, int level) {
            pair = tempPair;
            forward = (SkipNode[])Array.newInstance(SkipList.SkipNode.class,
                level + 1);
            this.level = level;
        }


        /**
         * Returns the KVPair stored in the SkipList.
         * 
         * @return the KVPair
         */
        public KVPair<K, V> element() {
            return pair;
        }

    }


    private class SkipListIterator implements Iterator<KVPair<K, V>> {
        private SkipNode current;

        public SkipListIterator() {
            current = head;
        }


        @Override
        public boolean hasNext() {

            return current.forward[0] != null;
        }


        @Override
        public KVPair<K, V> next() {

            KVPair<K, V> elem = current.forward[0].element();
            current = current.forward[0];
            return elem;
        }
    }

    @Override
    public Iterator<KVPair<K, V>> iterator() {

        return new SkipListIterator();
    }

}
