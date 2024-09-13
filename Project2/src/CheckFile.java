import java.io.*;

/**
 * Provides functionality to check if a binary file containing pairs of short
 * integers is sorted based on the first integer (key) of each pair.
 * This class is designed to work with files where records are stored
 * sequentially as pairs of short integers,
 * and it primarily checks for ascending order sorting of these records by their
 * key values.
 * 
 * Usage involves calling the static method {@code check} with the filename to
 * be checked. The file is expected to exist and be readable.
 * 
 * @author CS3114 Instructors and TAs
 * @version 03/22/2024
 */

public class CheckFile {

    /**
     * This method checks a file to see if it is properly sorted.
     *
     * @param filename
     *            a string containing the name of the file to check
     * @return true if the file is sorted, false otherwise
     * @throws Exception
     *             either an IOException or a FileNotFoundException
     */
    public static boolean check(String filename) throws Exception {
        DataInputStream dis;
        dis = new DataInputStream(new BufferedInputStream(new FileInputStream(
            filename)));

        boolean isError = false;
        int reccnt = 0;

        // smallest short possible, nothing is less than it:
        short prev = Short.MIN_VALUE;
        short curr;

        try {
            while (true) {
                reccnt++;
                curr = dis.readShort(); // reads the key from file.
                dis.readShort(); // reads and ignores value.
                // or we could do: dis.skipBytes(2);
                if (prev > curr) {
                    isError = true;
                }
                prev = curr; // gets ready for next comparison
            }
        }
        catch (EOFException e) {
            System.out.println(reccnt + " records processed");
        }
        dis.close();
        return !isError;
    }
}
