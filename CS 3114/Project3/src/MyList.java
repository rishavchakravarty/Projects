import java.util.Arrays;
import java.util.Iterator;

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
 * Represents a List. This class is responsible for managing the structure of
 * the List and the operations that can be performed on it.
 * 
 * @param <T>
 * 
 * @author kinjalpandey, architg03, rishavc18
 * @version 02/25/24
 */
public class MyList<T> implements Iterable<T> {
    /**
     * The size of the List (number of elements it contains).
     */
    private int size = 0;

    /**
     * The default capacity of the List.
     */
    private static final int DEFAULT_CAPACITY = 10;

    /**
     * The array used to store the elements in the List.
     */
    private Object[] elements;

    /**
     * Constructs an empty List with an initial capacity of ten.
     */
    public MyList() {
        elements = new Object[DEFAULT_CAPACITY];
    }


    /**
     * Adds the specified element to the end of this List.
     * 
     * @param e
     *            element to be appended to this List
     */
    public void add(T e) {
        if (size == elements.length) {
            ensureCapacity();
        }
        elements[size++] = e;
    }


    /**
     * Returns the element at the specified position in this List.
     * 
     * @param i
     *            index of the element to return
     * @return the element at the specified position in this List
     * @throws IndexOutOfBoundsException
     *             if the index is out of range
     */
    @SuppressWarnings("unchecked")
    public T get(int i) {
        if (i >= size || i < 0) {
            throw new IndexOutOfBoundsException("Index: " + i + ", Size " + i);
        }
        return (T)elements[i];
    }


    /**
     * Returns the number of elements in this List.
     * 
     * @return the number of elements in this List
     */
    public int size() {
        return size;
    }


    /**
     * Removes the element at the specified position in this List. Shifts any
     * subsequent elements to the left (subtracts one from their indices).
     * 
     * @param i
     *            the index of the element to be removed
     * @throws IndexOutOfBoundsException
     *             if the index is out of range
     */
    public void remove(int i) {
        if (i >= size || i < 0) {
            throw new IndexOutOfBoundsException("Index: " + i + ", Size " + i);
        }
        // Object temp = elements[i];
        int numElts = elements.length - (i + 1);
        System.arraycopy(elements, i + 1, elements, i, numElts);
        size--;
    }


    /**
     * Increases the capacity of this List instance, if necessary, to ensure
     * that it
     * can hold at least the number of elements specified by the current size.
     */
    private void ensureCapacity() {
        int newSize = elements.length * 2;
        elements = Arrays.copyOf(elements, newSize);
    }


    /**
     * Returns True if empty
     * 
     * @return boolean true/false
     */
    public boolean isEmpty() {

        return (this.size == 0);
    }


    /**
     * Implement the Iterable interface
     * 
     * @return Iterator<T> iterable
     */
    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return currentIndex < size && elements[currentIndex] != null;
            }


            @SuppressWarnings("unchecked")
            @Override
            public T next() {
                return (T)elements[currentIndex++];
            }


            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
