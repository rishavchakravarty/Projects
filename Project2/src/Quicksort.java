import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Implementation of the Quicksort algorithm optimized for large datasets that
 * do not fit entirely into main memory.
 * This class provides a disk-backed approach to sorting, utilizing a buffer
 * pool mechanism to manage data that exceeds
 * the capacity of main memory. It leverages the classic Quicksort algorithm,
 * adapted to work with virtual memory, treating
 * the disk file as an extended array for sorting operations. This approach
 * allows for efficient sorting of large files
 * by minimizing physical disk accesses and optimizing buffer usage.
 *
 * On my honor:
 * 
 * - I have not used source code obtained from another student,
 * or any other unauthorized source, either modified or
 * unmodified.
 * 
 * - All source code and documentation used in my program is
 * either my original work, or was derived by me from the
 * source code published in the textbook for this course.
 * 
 * - I have not discussed coding details about this project with
 * anyone other than my partner (in the case of a joint
 * submission), instructor, ACM/UPE tutors or the TAs assigned
 * to this course. I understand that I may discuss the concepts
 * of this program with other students, and that another student
 * may help me debug my program so long as neither of us writes
 * anything during the discussion or modifies any computer file
 * during the discussion. I have violated neither the spirit nor
 * letter of this restriction.
 * 
 * ~ Kinjal Pandey
 * 
 * @author kinjalpandey, architg03, rishavc18
 * @version 03/22/2024
 */
public class Quicksort {
    // Manages disk-backed virtual memory for sorting
    private BufferPool bufferPool;
    // Size of each record in bytes
    private static final int RECORD_SIZE = 4;
    // Block size in bytes for buffer pool
    private static final int BLOCK_SIZE = 4096;
    // Records per block
    @SuppressWarnings("unused")
    private static final int RECORDS_PER_BLOCK = BLOCK_SIZE / RECORD_SIZE;
    // Instance of Quicksort for static context usage
    private static Quicksort qs;
    // Threshold for optimizing sorting partitions
    private static final int THRESHOLD = 1500;

    /**
     * Constructs a Quicksort object configured with a specific file path and
     * cache size for the buffer pool.
     * 
     * @param filePath
     *            The path of the file to be sorted.
     * @param cacheSize
     *            The size of the cache for the buffer pool, affecting
     *            performance and efficiency.
     * @throws IOException
     *             If an I/O error occurs during buffer pool initialization.
     */
    public Quicksort(String filePath, int cacheSize) throws IOException {
        this.bufferPool = new BufferPool(filePath, cacheSize);

    }


    /**
     * The main entry point of the application. Validates command line arguments
     * and initiates the sorting process.
     * It demonstrates how to use the Quicksort class for sorting a file based
     * on disk-backed storage.
     * 
     * @param args
     *            Command line arguments: [0] - data file name, [1] - number of
     *            buffers, [2] - statistics file name.
     */
    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Usage: Quicksort <data-file-name> "
                + "<numb-buffers> <stat-file-name>");
            return;
        }
        String filename = args[0];
        int numbBuffers = Integer.parseInt(args[1]);
        String statFileName = args[2];
        try {
            long startTime = System.currentTimeMillis();

            BufferPool bufferPool = new BufferPool(filename, numbBuffers);
            long fileSize = new File(filename).length();
            int totalRecords = (int)(fileSize / RECORD_SIZE);

            // Updated to match the new constructor signature
            OptimizedQuicksort optimizedQuicksort = new OptimizedQuicksort(
                bufferPool, totalRecords);

            // Now calling quickSort without parameters
            optimizedQuicksort.quickSort();

            bufferPool.flush(); // Flush changes after sorting

            long endTime = System.currentTimeMillis();
            qs.writeStatistics(filename, numbBuffers, statFileName, endTime
                - startTime);

            System.out.println("File has been sorted.");
        }
        catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }


    /**
     * Initiates the sorting of the specified file using a disk-backed Quicksort
     * algorithm.
     * This method sets up the buffer pool with the specified number of buffers
     * and performs
     * the sorting operation on the data file represented on the disk. The
     * sorted data
     * is written back to the same file, effectively modifying its contents to
     * be in sorted order.
     *
     * @param filename
     *            The path to the data file that needs to be sorted.
     * @param numbBuffers
     *            The number of buffers to be allocated for the buffer pool,
     *            affecting the efficiency and performance of the sorting
     *            operation.
     * @throws Exception
     *             If any error occurs during file processing, including I/O
     *             errors
     *             or issues initializing the buffer pool.
     */
    public static void sortFile(String filename, int numbBuffers)
        throws Exception {
        qs = new Quicksort(filename, numbBuffers);
        long fileSize = new File(filename).length();
        int totalRecords = (int)(fileSize / RECORD_SIZE);
        qs.quickSort(0, totalRecords - 1);
        qs.finalizeSorting();
    }


    /**
     * Checks if the file is binary
     * 
     * @param path
     *            to the file
     * @return true or false
     * @throws IOException
     *             exception
     */
    public static boolean isBinaryFile(String path) throws IOException {
        File file = new File(path);
        if (!file.exists())
            return false; // or throw an exception

        try (BufferedInputStream in = new BufferedInputStream(
            new FileInputStream(file))) {
            int size = Math.min(1024, (int)file.length());
            byte[] data = new byte[size];
            in.read(data);
            return containsNonPrintableCharacters(data);
        }
    }


    /**
     * Determines if the provided byte array contains any non-printable
     * characters.
     * Non-printable characters are identified based on their ASCII values.
     * Characters
     * considered non-printable include those outside the range of typical ASCII
     * printable
     * characters (0x20 to 0x7E) and also excluding common control characters
     * (tab, newline, etc.).
     *
     * @param data
     *            The byte array to check for non-printable characters.
     * @return true if any non-printable characters are found, false otherwise.
     */
    private static boolean containsNonPrintableCharacters(byte[] data) {
        for (byte b : data) {
            if (b < 0x09 || (b > 0x0D && b < 0x20) || b > 0x7E) {
                return true;
            }
        }
        return false;
    }


    /**
     * Performs the Quicksort algorithm on a virtual array represented by the
     * data file on disk.
     * This method recursively sorts the segment of the array between the
     * specified indices
     * using the Quicksort algorithm, with modifications to operate over a
     * disk-backed storage
     * system through the buffer pool.
     *
     * @param low
     *            The starting index of the segment of the array to be sorted.
     * @param high
     *            The ending index of the segment of the array to be sorted.
     * @throws Exception
     *             If any error occurs during the sorting process, including
     *             issues
     *             accessing or modifying the data through the buffer pool.
     */
    public void quickSort(int low, int high) throws Exception {
        if (low < high) {
            if (high - low > THRESHOLD) {
                // Use three-way partitioning for larger segments
                int[] partitionIndices = threeWayPartition(low, high);
                quickSort(low, partitionIndices[0] - 1); // Sort elements less
                                                         // than pivot
                quickSort(partitionIndices[1] + 1, high); // Sort elements
                                                          // greater than pivot
            }
            else {
                // Use traditional partitioning for smaller segments
                int pi = partition(low, high);
                quickSort(low, pi - 1);
                quickSort(pi + 1, high);
            }
        }
    }


    /**
     * Calculates the median-of-three pivot index for the Quicksort algorithm.
     * This method
     * selects the median value among the first, middle, and last elements of
     * the portion
     * of the array currently being sorted to improve the efficiency of the
     * partitioning step.
     *
     * @param low
     *            The starting index of the array segment to sort.
     * @param high
     *            The ending index of the array segment to sort.
     * @return The index of the median value among the first, middle, and last
     *         elements.
     * @throws Exception
     *             If an error occurs while accessing the buffer pool.
     */
    private int medianOfThreePivotIndex(int low, int high) throws Exception {
        int mid = low + (high - low) / 2;
        short[] start = bufferPool.getRecord(low);
        short[] middle = bufferPool.getRecord(mid);
        short[] end = bufferPool.getRecord(high);

        // Ordering the start, middle, end to find the median
        if (start[0] > end[0]) {
            short[] temp = start;
            start = end;
            end = temp;
        }
        if (middle[0] > end[0]) {
            mid = high;
        }
        else if (middle[0] > start[0]) {
            mid = low;
        }

        return mid;
    }


    /**
     * Partitions the array segment between two indices for the Quicksort
     * algorithm.
     * This method reorganizes the elements in the array segment so that all
     * elements
     * less than a chosen pivot are moved before the pivot, and all elements
     * greater
     * are moved after it.
     *
     * @param low
     *            The starting index of the array segment to sort.
     * @param high
     *            The ending index of the array segment to sort.
     * @return The final index position of the chosen pivot.
     * @throws Exception
     *             If an error occurs during partitioning, including accessing
     *             the buffer pool.
     */
    private int partition(int low, int high) throws Exception {
        int pivotIndex = medianOfThreePivotIndex(low, high);
        // Swap pivot with high to use existing partition logic
        swapRecords(pivotIndex, high);
        short[] pivot = bufferPool.getRecord(high);

        int i = low - 1;
        for (int j = low; j < high; j++) {
            short[] record = bufferPool.getRecord(j);
            if (record[0] < pivot[0]) {
                i++;
                swapRecords(i, j);
            }
        }
        swapRecords(i + 1, high);
        return i + 1;
    }


    /**
     * Swaps two records in the virtual array represented by the disk file.
     * This operation is essential for the sorting algorithms to reorder
     * elements.
     *
     * @param index1
     *            The index of the first record to swap.
     * @param index2
     *            The index of the second record to swap.
     * @throws Exception
     *             If an error occurs while writing to or reading from the
     *             buffer pool.
     */
    private void swapRecords(int index1, int index2) throws Exception {
        short[] record1 = bufferPool.getRecord(index1);
        short[] record2 = bufferPool.getRecord(index2);

        bufferPool.writeRecord(index1, record2);
        bufferPool.writeRecord(index2, record1);
    }


    /**
     * Calculates the median-of-three pivot index among three specified indices
     * for the Quicksort algorithm. This method
     * aims to improve Quicksort's performance by choosing a better pivot,
     * especially in cases where the elements at the
     * beginning, middle, or end of the array segment might not be
     * representative of the array's overall distribution.
     * The median-of-three method chooses the median value among the elements at
     * the provided low, mid, and high indices
     * as the pivot, which is more likely to be closer to the median of the
     * entire segment being sorted.
     * 
     * Note: This method signature seems to overlap with another similar method,
     * suggesting a potential code redundancy
     * or an error in method overloading. Ensure that each overloaded method has
     * a distinct and clear purpose or consolidate
     * them if they serve the same role.
     *
     * @param low
     *            The index of the first element in the array segment.
     * @param mid
     *            The index of the middle element in the array segment.
     * @param high
     *            The index of the last element in the array segment.
     * @return The index of the element among the low, mid, and high indices
     *         whose value is the median. This index is used as the pivot
     *         position for sorting.
     * @throws Exception
     *             If an error occurs while accessing the buffer pool to
     *             retrieve records.
     */
    private int medianOfThreePivotIndex(int low, int mid, int high)
        throws Exception {
        short[] start = bufferPool.getRecord(low);
        short[] middle = bufferPool.getRecord(mid);
        short[] end = bufferPool.getRecord(high);

        // Ordering the start, middle, end to find the median
        if (start[0] > end[0]) {
            short[] temp = start;
            start = end;
            end = temp;
        }
        if (middle[0] > end[0]) {
            mid = high;
        }
        else if (middle[0] > start[0]) {
            mid = low;
        }

        return mid;
    }


    /**
     * Performs a three-way partitioning for the Quicksort algorithm. This
     * method
     * is designed to efficiently handle arrays with many duplicate keys by
     * partitioning
     * the array segment into three parts: elements less than the pivot, equal
     * to the pivot,
     * and greater than the pivot. This can significantly reduce the sort time
     * for such arrays.
     *
     * @param low
     *            The starting index of the array segment to sort.
     * @param high
     *            The ending index of the array segment to sort.
     * @return An array of two integers where the first element is the boundary
     *         for elements less than
     *         the pivot, and the second element is the boundary for elements
     *         greater than the pivot.
     * @throws Exception
     *             If an error occurs during the partitioning process
     * 
     *             * including accessing the buffer pool.
     */
    private int[] threeWayPartition(int low, int high) throws Exception {
        int pivotIndex = medianOfThreePivotIndex(low, (low + high) / 2, high);
        swapRecords(low, pivotIndex); // Move pivot to start for simplicity

        short[] pivot = bufferPool.getRecord(low);
        int lt = low;
        int gt = high;
        int i = low + 1;
        while (i <= gt) {
            short[] current = bufferPool.getRecord(i);
            if (current[0] < pivot[0]) {
                swapRecords(lt++, i++);
            }
            else if (current[0] > pivot[0]) {
                swapRecords(i, gt--);
            }
            else {
                i++;
            }
        }
        return new int[] { lt, gt }; // Return indices bounding elements equal
                                     // to pivot
    }


    /**
     * Writes the statistics of the sorting operation to a specified file. The
     * statistics
     * include the number of cache hits, disk reads, and disk writes encountered
     * during
     * the sorting process, as well as the total time taken to complete the
     * sort.
     * This information is appended to the specified statistics file, allowing
     * for
     * multiple runs to be documented consecutively.
     *
     * @param filename
     *            The name of the data file that was sorted.
     * @param numbBuffers
     *            The number of buffers used in the buffer pool for the sorting
     *            operation.
     * @param statFileName
     *            The path to the file where the statistics should be written.
     * @param sortTime
     *            The total time taken to sort the data file, in milliseconds.
     * @throws IOException
     *             If an I/O error occurs while writing to the statistics file.
     */
    public void writeStatistics(
        String filename,
        int numbBuffers,
        String statFileName,
        long sortTime)
        throws IOException {
        try (FileWriter fw = new FileWriter(statFileName, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw)) {
            out.println("Standard sort on " + filename);
            out.println("Cache Hits: " + this.bufferPool.getCacheHits());
            out.println("Disk Reads: " + this.bufferPool.getDiskReads());
            out.println("Disk Writes: " + this.bufferPool.getDiskWrites());
            out.println("Sort Time: " + sortTime + "ms");
        }
    }


    /**
     * Finalizes the sorting operation by ensuring all modified data blocks in
     * the buffer pool
     * are written back to the disk. This method should be called after the
     * sorting algorithm
     * completes to ensure the data file on disk is fully updated and reflects
     * the sorted order
     * of the records. It guarantees the integrity and persistence of the sorted
     * data.
     *
     * @throws Exception
     *             If any error occurs during the flushing process, including
     *             I/O errors
     *             writing the buffered data to disk.
     */
    public void finalizeSorting() throws Exception {
        bufferPool.flush();
    }

}
