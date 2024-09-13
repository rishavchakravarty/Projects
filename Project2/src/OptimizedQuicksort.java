import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Implements an optimized version of the quicksort algorithm. This version uses
 * a buffer pool
 * for disk I/O operations to manage large datasets that do not fit entirely in
 * memory. It employs
 * an iterative approach to reduce stack usage and includes a three-way
 * partitioning method to handle
 * duplicate elements more efficiently.
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

public class OptimizedQuicksort {

    private BufferPool bufferPool;
    private int totalRecords;

    /**
     * Constructs an OptimizedQuicksort instance with a specified buffer pool
     * and the total number
     * of records to sort. The buffer pool abstracts the disk I/O operations,
     * enabling the sorting
     * of data that exceeds the available memory.
     *
     * @param bufferPool
     *            The buffer pool used for disk I/O operations.
     * @param totalRecords
     *            The total number of records to be sorted.
     */
    public OptimizedQuicksort(BufferPool bufferPool, int totalRecords) {
        this.bufferPool = bufferPool;
        this.totalRecords = totalRecords;
    }


    /**
     * Executes the quicksort algorithm on the dataset. This method orchestrates
     * the loading of
     * data from disk into memory, sorting it, and then writing the sorted data
     * back to disk.
     * It leverages the buffer pool for efficient data management during these
     * operations.
     *
     * @throws IOException
     *             If an I/O error occurs during buffer pool operations.
     */
    public void quickSort() throws IOException {
        // Load the entire dataset into memory
        List<short[]> data = new ArrayList<>(totalRecords);
        for (int i = 0; i < totalRecords; i++) {
            data.add(bufferPool.getRecord(i));
        }

        // Convert the list to an array for sorting
        short[][] dataArray = data.toArray(new short[0][]);

        // Perform the iterative quicksort
        quickSortIterative(dataArray, 0, totalRecords - 1);

        // Write the sorted data back to disk
        for (int i = 0; i < dataArray.length; i++) {
            bufferPool.writeRecord(i, dataArray[i]);
        }
    }


    /**
     * Performs the iterative version of the quicksort algorithm on the given
     * array segment.
     * This method reduces the recursive stack depth by using a manual stack to
     * store the
     * indices of array segments to be sorted.
     *
     * @param data
     *            The array of records to sort.
     * @param low
     *            The starting index of the segment to be sorted.
     * @param high
     *            The ending index of the segment to be sorted.
     */
    private void quickSortIterative(short[][] data, int low, int high) {
        Stack<Integer> stack = new Stack<>();
        stack.push(low);
        stack.push(high);

        while (!stack.isEmpty()) {
            high = stack.pop();
            low = stack.pop();

            if (low < high) {
                short[] pivot = data[low];

                // 3-way partitioning
                int[] partitionIndices = threeWayPartition(data, low, high,
                    pivot);
                int lt = partitionIndices[0];
                int gt = partitionIndices[1];

                // Push left segment onto stack if it has more than one element
                if (lt - 1 > low) {
                    stack.push(low);
                    stack.push(lt - 1);
                }

                // Push right segment onto stack if it has more than one element
                if (gt + 1 < high) {
                    stack.push(gt + 1);
                    stack.push(high);
                }
            }
        }

    }


    /**
     * Partitions the given array segment into three parts based on their
     * comparison with the pivot:
     * elements less than the pivot, equal to the pivot, and greater than the
     * pivot. This method
     * enhances the performance of quicksort by efficiently handling duplicate
     * keys.
     *
     * @param data
     *            The array of records to partition.
     * @param low
     *            The starting index of the segment to partition.
     * @param high
     *            The ending index of the segment to partition.
     * @param pivot
     *            The pivot element for partitioning.
     * @return An array containing two integers: the index just before the start
     *         of elements equal
     *         to the pivot and the index just after the end of elements equal
     *         to the pivot.
     */
    private int[] threeWayPartition(
        short[][] data,
        int low,
        int high,
        short[] pivot) {
        int lt = low;
        int gt = high;
        int i = low + 1;
        while (i <= gt) {
            int cmp = compare(data[i], pivot);
            if (cmp < 0)
                swap(data, lt++, i++);
            else if (cmp > 0)
                swap(data, i, gt--);
            else
                i++;
        }
        return new int[] { lt, gt };
    }


    /**
     * Swaps two elements in the given array.
     *
     * @param data
     *            The array in which to swap elements.
     * @param i
     *            The index of the first element.
     * @param j
     *            The index of the second element.
     */
    private void swap(short[][] data, int i, int j) {
        short[] temp = data[i];
        data[i] = data[j];
        data[j] = temp;
    }


    /**
     * Compares two short arrays (records) based on their first element.
     * 
     * @param a
     *            The first record.
     * @param b
     *            The second record.
     * @return A negative integer, zero, or a positive integer as the first
     *         element of
     *         the first record is less than, equal to, or greater than the
     *         first element
     *         of the second record.
     */
    private int compare(short[] a, short[] b) {
        return Short.compare(a[0], b[0]);
    }

}
