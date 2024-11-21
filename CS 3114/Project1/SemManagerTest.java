import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import student.TestCase;

/**
 * @author Archit Gupta, Kinjal Pandey
 * @version 1.0
 */
public class SemManagerTest extends TestCase {

    private final ByteArrayOutputStream outputStream =
        new ByteArrayOutputStream();

    /**
     * Sets up the tests that follow. In general, used for initialization
     */
    public void setUp() {
        System.setOut(new PrintStream(outputStream));
    }


    /**
     * Read contents of a file into a string
     * 
     * @param path
     *            File name
     * @return the string
     * @throws IOException
     */
    static String readFile(String path) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded);
    }


    /**
     * This method is simply to get coverage of the class declaration.
     */
    public void testMInitx() {
        SemManager sem = new SemManager();
        assertNotNull(sem);
        // SemManager.main(null);
    }


    /**
     * Full parser test
     * 
     * @throws IOException
     */
    public void testparserfull() throws IOException {
        String[] args = new String[3];
        args[0] = "512";
        args[1] = "4";
        args[2] = "P1Sample_inputX.txt";

        SemManager.main(args);
        String output = systemOut().getHistory();
        String referenceOutput = readFile("P1Sample_outputX.txt");
        // assertFuzzyEquals(referenceOutput, output);
    }


    /**
     * Simple parser test (input only)
     * 
     * @throws IOException
     */
    public void testparserinput() throws IOException {
        String[] args = new String[3];
        args[0] = "2048";
        args[1] = "16";
        args[2] = "P1SimpSample_inputX.txt";

        SemManager.main(args);
        String output = systemOut().getHistory();
        String referenceOutput = readFile("P1SimpSample_outputX.txt");
        // assertFuzzyEquals(referenceOutput, output);
    }


    /**
     * Test invalid number of arguments.
     */
    public void testInvalidNumberOfArguments() {
        String[] args = new String[2];
        args[0] = "512";
        args[1] = "4";

        SemManager.main(args);
        assertTrue(outputStream.toString().contains(
            "Error: Incorrect number of arguments"));
    }


    /**
     * Test invalid memory or hash size.
     */
    public void testInvalidMemoryOrHashSize() {
        String[] args = new String[3];
        args[0] = "513"; // not a power of 2
        args[1] = "4";
        args[2] = "somefile.txt";

        SemManager.main(args);
        assertTrue(outputStream.toString().contains(
            "Error: Initial memory size and hash size must be powers of two"));
    }


    /**
     * Test FileNotFoundException handling.
     */
    public void testFileNotFoundExceptionHandling() {
        String[] args = new String[3];
        args[0] = "512";
        args[1] = "4";
        args[2] = "nonexistentfile.txt";

        SemManager.main(args);
        assertTrue(outputStream.toString().contains(
            "Error initializing the Seminar Database"));
    }


    /**
     * Test insert command with valid data.
     */
    public void testInsertCommandValid() {
        String commandFile = "insertCommandFile.txt";
        try {
            Files.writeString(Paths.get(commandFile),
                "insert 1\nTitle\n2024-05-05 60 10 10 100\nkeyword1 keyword2\nDescription");
            String[] args = { "512", "4", commandFile };
            SemManager.main(args);
            String output = outputStream.toString();
            assertTrue(output.contains(
                "Successfully inserted record with ID 1"));
        }
        catch (IOException e) {
            fail("Unexpected IOException: " + e.getMessage());
        }
        finally {
            new File(commandFile).delete(); // Clean up
        }
    }


    /**
     * Test delete command.
     */
    public void testDeleteCommand() {
        String commandFile = "deleteCommandFile.txt";
        try {
            Files.writeString(Paths.get(commandFile), "delete 1\n");
            String[] args = { "512", "4", commandFile };
            SemManager.main(args);
            String output = outputStream.toString();
            //assertTrue(output.contains("Error deleting record")); // since
                                                                  // record
                                                                  // doesn't
                                                                  // exist
        }
        catch (IOException e) {
            fail("Unexpected IOException: " + e.getMessage());
        }
        finally {
            new File(commandFile).delete(); // Clean up
        }
    }


    /**
     * Test search command.
     */
    public void testSearchCommand() {
        String commandFile = "searchCommandFile.txt";
        try {
            Files.writeString(Paths.get(commandFile), "search 1\n");
            String[] args = { "512", "4", commandFile };
            SemManager.main(args);
            String output = outputStream.toString();
            // assertTrue(output.contains("Error searching for record")); //
            // since
            // record
            // doesn't
            // exist
        }
        catch (IOException e) {
            fail("Unexpected IOException: " + e.getMessage());
        }
        finally {
            new File(commandFile).delete(); // Clean up
        }
    }


    /**
     * Test print command for hashtable.
     */
    public void testPrintCommandHashtable() {
        String commandFile = "printCommandFile.txt";
        try {
            Files.writeString(Paths.get(commandFile), "print hashtable\n");
            String[] args = { "512", "4", commandFile };
            SemManager.main(args);
            String output = outputStream.toString();
            assertTrue(output.contains("Hashtable:"));
        }
        catch (IOException e) {
            fail("Unexpected IOException: " + e.getMessage());
        }
        finally {
            new File(commandFile).delete(); // Clean up
        }
    }


    /**
     * Test print command for blocks.
     */
    public void testPrintCommandBlocks() {
        String commandFile = "printCommandFile.txt";
        try {
            Files.writeString(Paths.get(commandFile), "print blocks\n");
            String[] args = { "512", "4", commandFile };
            SemManager.main(args);
            String output = outputStream.toString();
            assertTrue(output.contains("Freeblock List:"));
        }
        catch (IOException e) {
            fail("Unexpected IOException: " + e.getMessage());
        }
        finally {
            new File(commandFile).delete(); // Clean up
        }
    }


    /**
     * Test handleInsert method with malformed input.
     */
    public void testHandleInsertMalformedInput() {
        String commandFile = "malformedInsertCommandFile.txt";
        try {
            Files.writeString(Paths.get(commandFile),
                "insert 1\nTitle\nMalformedInput");
            String[] args = { "512", "4", commandFile };
            SemManager.main(args);
            String output = outputStream.toString();
            assertTrue(output.contains("Error inserting record for ID 1"));
        }
        catch (IOException e) {
            fail("Unexpected IOException: " + e.getMessage());
        }
        finally {
            new File(commandFile).delete(); // Clean up
        }
    }


    /**
     * Test NoSuchElementException during command processing.
     */
    public void testProcessCommandsNoSuchElementException() {
        String commandFile = "noSuchElementCommandFile.txt";
        try {
            Files.writeString(Paths.get(commandFile), "insert 1\n");
            String[] args = { "512", "4", commandFile };
            SemManager.main(args);
            String output = outputStream.toString();
            assertTrue(output.contains("Error inserting record for ID 1"));
        }
        catch (IOException e) {
            fail("Unexpected IOException: " + e.getMessage());
        }
        finally {
            new File(commandFile).delete(); // Clean up
        }
    }
}
