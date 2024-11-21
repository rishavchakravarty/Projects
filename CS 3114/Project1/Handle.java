/**
 * Handle class represents a handle for a record in the heap file. It contains
 * the start position of the record in the heap file and the size of the record.
 *
 * @author {Archit Gupta, Kinjal Pandey, Rishav Chakravarty}
 * @version {1.0}
 */
public class Handle {
    /**
     * The start position of the record in the heap file.
     */
    private int startPosition;
    /**
     * The size of the record in the heap file.
     */
    private int size;
    
    /**
     * Constructs a new handle with the given start position and size.
     * 
     * @param startPosition
     *            The start position of the record.
     * @param size
     *            The size of the record.
     */
    public Handle(int startPosition, int size) {
        this.startPosition = startPosition;
        this.size = size;
    }


    /**
     * Get the start position of the record.
     * 
     * @return The start position of the record.
     */
    public int getStartPosition() {
        return startPosition;
    }


    /**
     * Get the size of the record.
     * 
     * @return The size of the record.
     */
    public int getSize() {
        return size;
    }
}
