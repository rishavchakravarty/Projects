import student.TestCase;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

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
 * Test class for PointsCommandProcessor.
 * 
 * @author kinjalpandey, architg03, rishavc18
 * @version 02/25/24
 */
public class PointsCommandProcessorTest extends TestCase {
    private PointsCommandProcessor processor;
    private final ByteArrayOutputStream outContent =
        new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private PointsDatabase database;

    /**
     * Sets up testing environment before each test case.
     */
    public void setUp() {
        processor = new PointsCommandProcessor();
        database = new PointsDatabase();
        System.setOut(new PrintStream(outContent));
    }


    /**
     * Resets the Original out
     */
    public void tearDown() {
        System.setOut(originalOut); // Reset System.out to its original stream
    }


    /**
     * Test the handleInsert method with correct input.
     */
    public void testHandleInsert() {
        String command = "insert Point 100 200";
        processor.processCommand(command);
        // Assertions to check if Point with name "Point" and coordinates (100,
        // 200) is inserted
    }


    /**
     * Test the handleInsert method with incorrect number of arguments (mutation
     * false).
     */
    public void testHandleInsertIncorrectArgs() {
        String command = "insert Point 100";
        processor.processCommand(command);
        // Assertions to check for appropriate error message or outcome
    }


    /**
     * Test the handleInsert method with incorrect parameters (mutation true).
     */
    public void testHandleInsertIncorrectParams() {
        String command = "insert Point abc def";
        processor.processCommand(command);
        // Assertions to check for appropriate error message or outcome
    }


    /**
     * Test the handleRemove method with a name parameter.
     */
    public void testHandleRemoveByName() {
        String command = "remove Point";
        processor.processCommand(command);
        // Assertions to check if Point with name "Point" is removed
    }


    /**
     * Test the handleRemove method with x and y parameters.
     */
    public void testHandleRemoveByCoordinates() {
        String command = "remove 100 200";
        processor.processCommand(command);
        // Assertions to check if Point at coordinates (100, 200) is removed
    }


    /**
     * Test the handleRemove method with incorrect parameters (mutation false).
     */
    public void testHandleRemoveIncorrectParams() {
        String command = "remove abc def";
        processor.processCommand(command);
        // Assertions to check for appropriate error message or outcome
    }

    /**
     * Test the handleRegionSearch method with correct input.
     */
// public void testHandleRegionSearch() {
// String command = "regionsearch 100 200 50 50";
// processor.processCommand(command);
// // Assertions to check if region search is performed with given
// // parameters
// }


    /**
     * Test the handleRegionSearch method with incorrect parameters (mutation
     * true).
     */
    public void testHandleRegionSearchIncorrectParams() {
        String command = "regionsearch abc def ghi jkl";
        processor.processCommand(command);
        // Assertions to check for appropriate error message or outcome
    }


    /**
     * Test the handleSearch method with a name parameter.
     */
    public void testHandleSearch() {
        String command = "search Point";
        processor.processCommand(command);
        // Assertions to check if search is performed for name "Point"
    }


    /**
     * Test the handleSearch method with incorrect number of arguments (mutation
     * false).
     */
    public void testHandleSearchIncorrectArgs() {
        String command = "search";
        processor.processCommand(command);
        // Assertions to check for appropriate error message or outcome
    }


    /**
     * Test insert method with negative coordinates.
     */
    public void testInsertNegativeCoordinates() {
        // Prepare
        final String pointName = "NegPoint";
        final int x = -10; // Negative x-coordinate
        final int y = -5; // Negative y-coordinate

        // Execute
        database.insert(pointName, x, y);

        // Verify
        MyList<Point> points = database.getPointsByName(pointName);
        assertTrue("Point with negative coordinates should not be inserted",
            points.isEmpty());
    }


    /**
     * Test insert method with coordinates equal to WORLD_SIZE.
     */
    public void testInsertAtWorldSizeBoundary() {
        // Prepare
        final String pointName = "BoundaryPoint";
        final int x = 1024; // x-coordinate at WORLD_SIZE
        final int y = 1024; // y-coordinate at WORLD_SIZE

        // Execute
        database.insert(pointName, x, y);

        // Verify
        MyList<Point> points = database.getPointsByName(pointName);
        assertTrue("Point at WORLD_SIZE boundary should not be inserted", points
            .isEmpty());
    }


    /**
     * Test insert method with coordinates equal to WORLD_SIZE.
     */
    public void testInsertMutationWithTrue() {
        PointsDatabase db = new PointsDatabase();
        // This point should be valid and thus inserted.
        db.insert("Point", 10, 10);
        // Assertion to check if the point is inserted despite the mutation
        assertFalse(db.getPointsByName("Point").isEmpty());

        // This point should not be inserted due to out-of-bounds coordinates.
        db.insert("Point", -1, -1);
        // Assertion to check if the mutation falsely inserts an invalid point
        // assertTrue(db.getPointsByName("Point").isEmpty());
    }


    /**
     * Test insert method with valid coordinates.
     */
    public void testInsertValidPoint() {
        // Test insertion of a valid point
        database.insert("Point", 100, 100);
        assertFalse("Point should be inserted", database.getPointsByName(
            "Point").isEmpty());
    }


    /**
     * Test insert method with invalid coordinates.
     */
    public void testInsertInvalidPoint() {
        // Test insertion of an invalid point, expecting no insertion
        database.insert("Point", -100, -100);
        assertTrue("Invalid point should not be inserted", database
            .getPointsByName("Point").isEmpty());
    }


    /**
     * Test main method with no file
     */
    public void testMainFileNotFound() {
        // Setup: An array with a non-existing file name
        String[] args = { "nonexistingfile.txt" };

        System.setOut(new PrintStream(outContent));

        // Action
        PointsDatabase.main(args);

        // Verify: Check for the correct output indicating the file was not
        // found
        assertTrue(outContent.toString().contains(
            "Command file not found: nonexistingfile.txt"));

        // Reset System.out to its original stream
        System.setOut(System.out);
    }


    /**
     * Test main method with file
     */
    public void testMainValidFile() {

        String[] args = { "SyntaxTest1.txt" };
        System.setOut(new PrintStream(outContent));

        PointsDatabase.main(args);

        System.setOut(System.out);
    }


    /**
     * Test remove method
     */
    public void testRemoveNegativeCoordinates() {
        System.setOut(new PrintStream(outContent));

        database.remove(-1, -1);

        String expectedOutput = "Point rejected: (-1, -1)\n";
        assertEquals(expectedOutput, outContent.toString());

        System.setOut(System.out);
    }


    /**
     * Test remove method
     */
    public void testRemovePointNotFound() {

        System.setOut(new PrintStream(outContent));

        database.remove(10, 10);

        String expectedOutput = "Point not found: (10, 10)\n";
        assertEquals(expectedOutput, outContent.toString());

        System.setOut(System.out);
    }


    /**
     * Test remove method
     */
    public void testRemoveInvalidPoint() {
        System.setOut(new PrintStream(outContent));

        database.remove(10, 10);

        String expectedOutput = "not found";
        assertTrue(outContent.toString().contains(expectedOutput));

        System.setOut(System.out);
    }


    /**
     * Test remove method
     */
    public void testRemoveValidPoint() {

        System.setOut(new PrintStream(outContent));

        database.insert("point1", 100, 100);
        database.remove(100, 100);

        // Check if the output indicates the point was successfully removed.
        // The exact output will depend on the point's name and coordinates.
        assertTrue(outContent.toString().contains("Point removed: ("));

        System.setOut(System.out);
    }


    /**
     * Test RegionSearch
     */
    public void testRegionSearchNegativeDimensions() {
        System.setOut(new PrintStream(outContent));

        database.regionSearch(10, 10, -5, -5);

        String expectedOutput = "Rectangle rejected: (10, 10, -5, -5)\n";
        assertEquals(expectedOutput, outContent.toString());

        System.setOut(System.out);
    }


    /**
     * Test RegionSearch
     */
    public void testRegionSearchZeroWidth() {

        System.setOut(new PrintStream(outContent));

        database.insert("point1", 0, 0);
        database.regionSearch(20, 20, 0, 10);

        String expectedOutput = "Rectangle rejected";
        assertTrue(outContent.toString().contains(expectedOutput));

        System.setOut(System.out);
    }


    /**
     * Test RegionSearch
     */
    public void testRegionSearchZeroHeight() {
        System.setOut(new PrintStream(outContent));

        database.insert("point1", 0, 0);
        database.regionSearch(30, 30, 10, 0);

        String expectedOutput = "Rectangle rejected";
        assertTrue(outContent.toString().contains(expectedOutput));

        System.setOut(System.out);
    }


    /**
     * Test RegionSearch
     */
    public void testRegionSearchValidDimensions() {

        System.setOut(new PrintStream(outContent));

        database.insert("point1", 40, 40);
        database.insert("point2", 30, 30);
        database.insert("point3", 20, 20);
        database.regionSearch(40, 40, 20, 20);

        assertTrue(outContent.toString().contains("Point found: "));

        System.setOut(System.out);
    }


    private int countOccurrences(String haystack, String needle) {
        return haystack.split(needle, -1).length - 1;
    }


    /**
     * Test Duplicates
     */
    public void testDuplicatesNoDuplicates() {

        System.setOut(new PrintStream(outContent));

        database.insert("point1", 40, 40);
        database.insert("point2", 30, 30);
        database.insert("point3", 20, 20);
        database.duplicates();

        String expectedOutput = "Duplicate points:";
        assertTrue(outContent.toString().contains(expectedOutput));

        System.setOut(System.out);
    }


    /**
     * Test Duplicates
     */
    public void testDuplicatesDistinctCoordinates() {

        System.setOut(new PrintStream(outContent));

        database.insert("point1", 400, 400);
        database.insert("point2", 30, 30);
        database.insert("point1", 20, 20);
        database.duplicates();

        // Expected: Each set of duplicate coordinates printed once.
        // The exact assertion depends on the mock data you provide.
        assertTrue(outContent.toString().contains("Duplicate points:"));
        assertTrue(outContent.toString().contains("(point1, 400, 400)"));
        assertTrue(outContent.toString().contains("(point1, 20, 20)"));

        System.setOut(System.out);
    }


    /**
     * Test Duplicates
     */
    public void testDuplicatesIdenticalCoordinates() {

        System.setOut(new PrintStream(outContent));

        // Setup: Create a list with multiple duplicate points having the same
        // coordinates
        // Ensure quadTree.findDuplicates() returns this list.
        // This setup might involve mocking quadTree to return a predefined
        // MyList<Point>

        database.insert("point1", 400, 400);
        database.insert("point3", 20, 20);
        database.insert("point2", 30, 30);
        database.insert("point1", 400, 400);
        database.insert("point3", 20, 20);
        database.duplicates();

        // Check: Coordinates for identical duplicates printed only once.
        // Adjust assertion to match mock data.
        assertTrue(outContent.toString().contains("Duplicate points:"));
        assertEquals(2, countOccurrences(outContent.toString(), "(400, 400)"));

        System.setOut(System.out);
    }

    /**
     * Tests command files
     */
// public void testCommandFiles() {
// String[] inputFiles = { "./src/BadPoint.txt", "./src/SyntaxTest1.txt" };
// String[] outputFiles = { "./src/BadPointOut.txt",
// "./src/SyntaxTest1Out.txt" };
//
// for (int i = 0; i < inputFiles.length; i++) {
// try {
// List<String> commands = Files.readAllLines(Paths.get(
// inputFiles[i]));
//
// // Process each command
// for (String command : commands) {
// processor.processCommand(command);
// }
//
// String output = outContent.toString().trim();
// outContent.reset(); // Clear the output stream for the next test
//
// // Read the expected output
// String expectedOutput = new String(Files.readAllBytes(Paths.get(
// outputFiles[i]))).trim();
//
// // Compare the output with the expected output
// assertEquals("The output from processing " + inputFiles[i]
// + " does not match the expected output.", expectedOutput,
// output);
// }
// catch (IOException e) {
// fail("An IOException occurred while reading files: " + e
// .getMessage());
// }
// }
// }

}
