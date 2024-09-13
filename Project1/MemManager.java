import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible for managing the memory pool and the free lists.
 * It is responsible for inserting and removing records from the memory pool.
 * It also handles expanding the memory pool when necessary.
 *
 * @author {Archit Gupta, Kinjal Pandey, Rishav Chakravarty}
 * @version {1.1}
 */
public class MemManager {

    /**
     * The memory pool that stores the data.
     */
    private byte[] memoryPool;

    /**
     * The free lists for each block size.
     */
    private List<List<Integer>> freeLists;

    /**
     * Create a new MemManager object with the given pool size.
     * 
     * @param poolSize
     *            The size of the memory pool.
     */
    public MemManager(int poolSize) {
        this.memoryPool = new byte[poolSize];
        this.freeLists = initializeFreeLists(poolSize);
    }


    /**
     * Get the size of the memory pool.
     * 
     * @return The size of the memory pool.
     */
    public int getMemoryPoolSize() {
        return memoryPool.length;
    }


    /**
     * Get the data from the memory pool at the given handle.
     * 
     * @param handle
     *            The handle for the data.
     * @return The data from the memory pool.
     */
    public byte[] get(Handle handle) {
        byte[] data = new byte[handle.getSize()];
        System.arraycopy(memoryPool, handle.getStartPosition(), data, 0, handle
            .getSize());
        return data;
    }


    /**
     * Initialize the free lists for the given pool size.
     * 
     * @param poolSize
     *            The size of the memory pool.
     * @return The free lists for each block size.
     */
    private List<List<Integer>> initializeFreeLists(int poolSize) {
        // Initialize free lists for each possible block size
        List<List<Integer>> lists = new ArrayList<>();
        int size = (int)(Math.log(poolSize) / Math.log(2));
        for (int i = 0; i <= size; i++) {
            lists.add(new ArrayList<>());
        }
        lists.get(size).add(0); // Start with one large free block
        return lists;
    }


    /**
     * Insert the given record into the memory pool.
     * 
     * @param record
     *            The record to insert.
     * @return The handle for the inserted record.
     */
    public Handle insert(byte[] record) {
        int requiredSize = record.length;
        int index = findFreeBlock(requiredSize);
        if (index == -1) {
            expandMemory(requiredSize);
            return insert(record);
        }

        int blockSize = 1 << index;
        int startPos = freeLists.get(index).remove(0);
        if (startPos + record.length > memoryPool.length) {
            expandMemory(requiredSize);
            return insert(record);
        }

        System.arraycopy(record, 0, memoryPool, startPos, record.length);
        return new Handle(startPos, blockSize);
    }


    /**
     * Find a free block of memory that is large enough to store the given
     * record.
     * 
     * @param requiredSize
     *            The size of the record to store.
     * @return The index of the free block size that can store the record.
     */
    private int findFreeBlock(int requiredSize) {
        int k = (int)Math.ceil(Math.log(requiredSize) / Math.log(2));
        int actualSize = (1 << k);

        for (int i = k; i < freeLists.size(); i++) {
            if (!freeLists.get(i).isEmpty() && (1 << i) >= actualSize) {
                // Found a block large enough
                splitBlocksToFit(i, k);
                return k;
            }
        }
        return -1;
    }


    /**
     * Split blocks to fit the required size.
     * 
     * @param currentIndex
     *            The current index of the free block.
     * @param targetIndex
     *            The target index of the free block.
     */
    private void splitBlocksToFit(int currentIndex, int targetIndex) {
        while (currentIndex > targetIndex) {
            int startPos = freeLists.get(currentIndex).remove(0);
            int newSize = 1 << (currentIndex - 1);
            freeLists.get(currentIndex - 1).add(startPos);
            freeLists.get(currentIndex - 1).add(startPos + newSize);
            currentIndex--;
        }
    }

    /**
     * Whether the memory pool has been expanded.
     */
    private boolean expanded = false;

    /**
     * Expand the memory pool.
     */
    private void expandMemory(int requiredSize) {
        int oldSize = memoryPool.length;
        int newSize = Math.max(oldSize * 2, oldSize + requiredSize);
        byte[] newMemoryPool = new byte[newSize];
        System.arraycopy(memoryPool, 0, newMemoryPool, 0, oldSize);
        memoryPool = newMemoryPool;

        int newFreeBlockStart = oldSize;
        freeLists.get(freeLists.size() - 1).add(newFreeBlockStart);
        freeLists.add(new ArrayList<>());
        expanded = true;
    }


    /**
     * Check if the memory pool has been expanded and reset the expanded flag.
     * 
     * @return Whether the memory pool has been expanded.
     */
    public boolean checkAndResetExpanded() {
        if (expanded) {
            expanded = false;
            return true;
        }
        return false;
    }


    /**
     * Remove the record at the given handle from the memory pool.
     * 
     * @param handle
     *            The handle for the record to remove.
     */
    public void remove(Handle handle) {
        int blockStart = handle.getStartPosition();
        int blockSize = handle.getSize();
        int blockIndex = (int)(Math.log(blockSize) / Math.log(2));
        mergeBlocks(blockStart, blockSize);
    }


    /**
     * Merge blocks recursively if possible.
     * 
     * @param start
     *            The starting position of the block to merge.
     * @param size
     *            The size of the block to merge.
     */
    private void mergeBlocks(int start, int size) {
        int buddyAddress = getBuddyAddress(start, size);
        int blockIndex = (int)(Math.log(size) / Math.log(2));
        List<Integer> currentList = freeLists.get(blockIndex);

        // Look for buddy in the current list
        int buddyIndex = currentList.indexOf(buddyAddress);

        if (buddyIndex != -1) {
            currentList.remove(buddyIndex); // Remove buddy from the list
            int mergedStart = Math.min(start, buddyAddress);
            int mergedSize = size * 2;
            mergeBlocks(mergedStart, mergedSize);
        }
        else {
            // Add the block to the free list
            currentList.add(start);
            currentList.sort(Integer::compareTo); // Keep the list sorted
        }
    }


    /**
     * Get the address of the buddy block.
     * 
     * @param start
     *            The starting position of the block.
     * @param size
     *            The size of the block.
     * @return The address of the buddy block.
     */
    private int getBuddyAddress(int start, int size) {
        return start ^ size;
    }


    /**
     * Dump the free block list.
     */
    public void dump() {
        System.out.println("Freeblock List:");
        boolean anyFree = false;
        for (int i = 0; i < freeLists.size(); i++) {
            if (!freeLists.get(i).isEmpty()) {
                anyFree = true;
                System.out.println((1 << i) + ": " + freeLists.get(i).toString()
                    .replace("[", "").replace("]", "").replace(",", ""));
            }
        }
        if (!anyFree) {
            System.out.println("There are no freeblocks in the memory pool");
        }
    }
}
