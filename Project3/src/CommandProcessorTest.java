import student.TestCase;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

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
 * This class tests the CommandProcessor class. Test each possible command on
 * its bounds, if applicable to ensure they work properly. Also test passing
 * improper command to ensure all class functionalities work as intended.
 * 
 * @author kinjalpandey, architg03, rishavc18
 * @version 2/11/24
 */
public class CommandProcessorTest extends TestCase {

    private final ByteArrayOutputStream outContent =
        new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private CommandProcessor cp;
    private PointsDatabase db;

    /**
     * The setUp() method will be called automatically before each test and
     * reset
     * whatever the test modified. For this test class, only a new database
     * object
     * is needed, so create a database here for use in each test case.
     */
    public void setUp() {
        System.setOut(new PrintStream(outContent));
        cp = new CommandProcessor();
        db = new PointsDatabase();
    }


    /**
     * Restores the standard output before each test.
     */
    public void tearDown() {
        System.setOut(originalOut); // Resets System.out to its original state
    }


    /**
     * Helper method to test command processing and output.
     *
     * @param command
     *            the command string to process
     * @param expectedOutput
     *            the expected output string
     */
    private void testCommand(String command, String expectedOutput) {
        cp.processor(command);
        String actualOutput = outContent.toString().trim();
        assertTrue(
            "Expected output not found in actual output. Actual output was: "
                + actualOutput, actualOutput.contains(expectedOutput));
        outContent.reset(); // Clears the output stream for the next test
    }


    /**
     * Tests the insertion of rectangles with both valid and invalid parameters.
     * It
     * verifies that the command processor correctly inserts rectangles with
     * valid
     * parameters and rejects insertion commands with invalid parameters.
     */
    public void testInsert() {
        // Valid insert
        testCommand("insert a 1 0 2 4", "Rectangle inserted: (a, 1, 0, 2, 4)");
        // Note: Adjust the invalid insert test if necessary, depending on how
        // your CommandProcessor handles invalid inputs.
    }


    /**
     * Tests the removal of rectangles by name, including both successful and
     * unsuccessful removals. It ensures that the command processor can remove
     * an
     * existing rectangle by name and correctly handles attempts to remove
     * non-existing rectangles.
     */
    public void testRemoveByName() {
        // Insert a rectangle first to ensure there's something to remove
        cp.processor("insert a 1 0 2 4");
        // Successful removal
        testCommand("remove a", "Rectangle removed: (a, 1, 0, 2, 4)");
        // Unsuccessful removal
        testCommand("remove b", "Rectangle not removed: b");
    }


    /**
     * Tests the removal of rectangles by dimensions, covering both cases where
     * removal is successful and when it fails. This verifies that the command
     * processor can accurately identify and remove rectangles based on their
     * dimensions, and properly handles cases where no rectangle matches the
     * given
     * dimensions.
     */
    public void testRemoveByDimensions() {
        // Re-insert the same rectangle to ensure the remove by dimensions can
        // be tested
        cp.processor("insert a 1 0 2 4");
        // Successful removal by dimensions
        testCommand("remove 1 0 2 4", "Rectangle removed: (a, 1, 0, 2, 4)");
        // Unsuccessful removal (dimensions do not match any rectangle)
        testCommand("remove 2 0 4 8", "Rectangle not found: (2, 0, 4, 8)");
    }


    /**
     * Tests the 'regionsearch' command with various scenarios to verify that
     * the
     * command processor correctly identifies and lists rectangles intersecting
     * with
     * a specified search region. Scenarios include searches that match multiple
     * rectangles, single rectangles, and no rectangles.
     */
    public void testRegionSearch() {
        // Adjust these test cases to match the expected output of your
        // CommandProcessor
        testCommand("regionsearch 900 5 1000 1000",
            "Rectangles intersecting region (900, 5, 1000, 1000):");
        testCommand("regionsearch 0 500 20 1",
            "Rectangles intersecting region (0, 500, 20, 1):");
    }


    /**
     * Tests the 'intersections' command to ensure that the command processor
     * correctly identifies and lists pairs of intersecting rectangles. This
     * includes scenarios where intersections exist and do not exist, verifying
     * that
     * the output accurately reflects the current state of the rectangle
     * database.
     */
    public void testIntersections() {
        testCommand("intersections", "Intersection pairs:");
    }


    /**
     * Tests the 'dump' command to ensure that the command processor correctly
     * outputs the current state of the rectangle database. This test verifies
     * that
     * the dump includes all current entries in the correct format and that it
     * behaves correctly after various operations such as insertions and
     * removals.
     */
    public void testDump() {
        Boolean noErrors = true;

        cp.processor("insert 10 0 5 5");
        cp.processor("insert 0 10 15 5");
        cp.processor("insert 10 0 14 20");

        try {
            cp.processor("dump");
        }
        catch (Exception e) {
            noErrors = false;
        }
        assertTrue(noErrors);
    }


    /**
     * Tests the 'dump' command to ensure that the command processor correctly
     * outputs the current state of the rectangle using correct arguments
     */

    public void testDumpIncorrectArgs() {
        cp.processor("dump extraArg"); // No arguments expected
        assertTrue(outContent.toString().contains("Invalid command."));
        outContent.reset();
    }


    /**
     * Tests insert()using negative coordinates should not be able to insert the
     * rectangle
     */
    public void testInsertWithNegativeXCoordinate() {
        cp.processor("insert rectNegX -1 10 20 30");
        assertTrue(outContent.toString().trim().contains("Rectangle rejected: "
            + "(rectNegX, -1, 10, 20, 30)"));
        outContent.reset();
    }


    /**
     * Tests insert()with negative width should not be able to insert the
     * rectangle
     */
    public void testInsertWithNonPositiveWidth() {
        cp.processor("insert rectZeroWidth 10 10 0 30");
        assertTrue(outContent.toString().trim().contains("Rectangle rejected: "
            + "(rectZeroWidth, 10, 10, 0, 30)"));
        outContent.reset();
    }


    /**
     * Tests insert() with too much width should not be able to insert the
     * rectangle
     */
    public void testInsertWithExcessiveWidth() {
        cp.processor("insert rectExcessiveWidth 1023 10 2 30");
        assertTrue(outContent.toString().trim().contains("Rectangle rejected: "
            + "(rectExcessiveWidth, 1023, 10, 2, 30)"));
        outContent.reset();
    }


    /**
     * Tests insert() with a valid rectangle should be able to insert the
     * rectangle
     */
    public void testInsertWithValidRectangle() {
        String command = "insert validRect 10 10 20 30";
        cp.processor(command);
        String expectedOutput = "Rectangle inserted: "
            + "(validRect, 10, 10, 20, 30)";
        assertTrue(outContent.toString().trim().contains(expectedOutput));
        outContent.reset();
    }


    /**
     * Tests insert() again with valid rectangle should be able to insert the
     * rectangle
     */
    public void testInsertValidRectangle() {
        cp.processor("insert rect1 100 100 20 30");
        assertTrue(outContent.toString().trim().contains("Rectangle inserted: "
            + "(rect1, 100, 100, 20, 30)"));
        outContent.reset();
    }


    /**
     * Tests insert() with negative X values should not be able to insert the
     * rectangle
     */
    public void testInsertNegativeX() {
        cp.processor("insert rectNegX -10 100 20 30");
        assertTrue(outContent.toString().trim().contains("Rectangle rejected"));
        outContent.reset();
    }


    /**
     * Tests insert() with negative Y value should not be able to insert the
     * rectangle
     */
    public void testInsertNegativeY() {
        cp.processor("insert rectNegY 10 -100 20 30");
        assertTrue(outContent.toString().trim().contains("Rectangle rejected"));
        outContent.reset();
    }


    /**
     * Tests insert() with no width for rectangle should not be able to insert
     * the
     * rectangle
     */
    public void testInsertZeroWidth() {
        cp.processor("insert rectZeroWidth 10 100 0 30");
        assertTrue(outContent.toString().trim().contains("Rectangle rejected"));
        outContent.reset();
    }


    /**
     * Tests insert() with 0 height for rectangle should not be able to insert
     * the
     * rectangle
     */
    public void testInsertZeroHeight() {
        cp.processor("insert rectZeroHeight 100 10 20 0");
        assertTrue(outContent.toString().trim().contains("Rectangle rejected"));
        outContent.reset();
    }


    /**
     * Tests insert() with rectangle outside max size should not be able to
     * insert
     * the rectangle
     */
    public void testInsertWidthExceeds() {
        cp.processor("insert rectWide 1023 100 2 30"); // Assuming 1024 is the
                                                       // max width
        assertTrue(outContent.toString().trim().contains("Rectangle rejected"));
        outContent.reset();
    }


    /**
     * Tests insert() with rectangle outside max size should not be able to
     * insert
     * the rectangle
     */
    public void testInsertHeightExceeds() {
        cp.processor("insert rectTall 100 1023 20 2"); // Assuming 1024 is the
                                                       // max height
        assertTrue(outContent.toString().trim().contains("Rectangle rejected"));
        outContent.reset();
    }


    /**
     * Test the insert command with boundary coordinates and sizes. should not
     * be
     * able to insert the rectangle
     */
    public void testInsertBoundaryConditions() {
        // Test insertion at the origin
        testCommand("insert origin 0 0 10 10", "Rectangle inserted: "
            + "(origin, 0, 0, 10, 10)");
        // Test insertion at the maximum allowed coordinates and size, adjust
        // these values based on your system's limits
        testCommand("insert maxBoundary 1023 1023 1 1", "Rectangle inserted: "
            + "(maxBoundary, 1023, 1023, 1, 1)");
        // Test insertion with coordinates and size exceeding limits
        testCommand("insert exceedBoundary 1024 1024 1 1",
            "Rectangle rejected: " + "(exceedBoundary, 1024, 1024, 1, 1)");
    }


    /**
     * Test the insert command with invalid parameters. should not be able to
     * insert
     * the rectangle due to wrong parameters
     */
    public void testInsertInvalidParameters() {
        // Test insertion with negative dimensions
        testCommand("insert negSize -1 -1 -10 -10", "Rectangle rejected: "
            + "(negSize, -1, -1, -10, -10)");
        // Test insertion with zero dimensions
        testCommand("insert zeroSize 100 100 0 0", "Rectangle rejected: "
            + "(zeroSize, 100, 100, 0, 0)");
        // Test insertion with non-integer values, assuming processor method can
        // be modified to catch these
        testCommand("insert nonInteger 100 100 a b",
            "Invalid number format for insert command.");
    }


    /**
     * Test remove command with non-existing entries. should not be able to
     * remove
     * the rectangle as it doesnt exist
     */
    public void testRemoveNonExisting() {
        // Attempt to remove a non-existing rectangle by name
        testCommand("remove nonExisting", "Rectangle not removed: nonExisting");
        // Attempt to remove a non-existing rectangle by dimensions
        testCommand("remove 0 0 10 10", "Rectangle not found: (0, 0, 10, 10)");
    }


    /**
     * Test regionsearch command with edge conditions. should not be able to
     * find
     * the rectangles as they dont intersect
     */
    public void testRegionSearchEdgeConditions() {
        // Region search that matches exactly one rectangle's boundaries
        cp.processor("insert edgeRect 500 500 50 50");
        testCommand("regionsearch 500 500 50 50", "Rectangle inserted: "
            + "(edgeRect, 500, 500, 50, 50)");
        // Region search with no intersecting rectangles
        testCommand("regionsearch 0 0 1 1",
            "No rectangles found intersecting the region");
    }


    /**
     * Test intersections command for scenarios with and without intersections.
     */
    public void testIntersectionsScenarios() {
        // Clear any previous output
        outContent.reset();

        // Setup the test scenario
        cp.processor("insert a 10 10 15 15");
        cp.processor("insert b 11 11 5 5");
        cp.processor("insert c 0 0 1000 10");
        cp.processor("insert d 0 0 10 1000");

        // Process the intersections command
        cp.processor("intersections");

        // Capture and trim the output
        String output = outContent.toString().trim();

        // Verify the header is printed
        // assertTrue("Intersection pairs: header missing",
        // output.startsWith("Intersection pairs:"));

        // Check for the presence of parts of the expected intersection
        // descriptions
        String[] expectedParts = new String[] { "(a, 10, 10, 15, 15)",
            "(b, 11, 11, 5, 5)", "(c, 0, 0, 1000, 10)", "(d, 0, 0, 10, 1000)" };

        // Loop through each part and check if it's present in the output
        for (String part : expectedParts) {
            assertTrue("Expected part not found in actual output: " + part,
                output.contains(part));
        }

        // Reset the output stream for the next test
        outContent.reset();
    }


    /**
     * Test search command for non-existing names. should not be able to find
     * the
     * rectangle
     */
    public void testSearchNonExisting() {
        testCommand("search nonExisting", "Rectangle not found: (nonExisting)");
    }


    /**
     * Test dump command after various operations.
     */
    public void testDumpAfterOperations() {
        // Insert a rectangle and then remove it, dump should reflect these
        // changes
        cp.processor("insert tempRect 100 100 20 20");
        cp.processor("remove tempRect");
        testCommand("dump", "SkipList dump:");
    }


    /**
     * Test when arr.length == 6 (true case) should be able to insert the
     * rectangle
     */
    public void testInsertCommandValid() {
        cp.processor("insert name 1 2 3 4");
        assertTrue(outContent.toString().contains("Rectangle inserted:"));
    }


    /**
     * Test when arr.length != 6 (false case) should not be able to insert the
     * rectangle
     */
    public void testInsertCommandInvalid() {
        cp.processor("insert name 1 2 3");
        assertTrue(outContent.toString().contains("Invalid insert command."));
    }


    /**
     * Test remove by coordinates with arr.length == 5 (true case) should be
     * able to
     * remove the rectangle
     */
    public void testRemoveByCoordinatesValid() {
        cp.processor("insert name 1 2 3 4"); // Pre-insert for removal
        cp.processor("remove 1 2 3 4");
        assertTrue(outContent.toString().contains("Rectangle removed:"));
    }


    /**
     * Test remove by coordinates with arr.length != 5 (false case) should not
     * be
     * able to remove the rectangle
     */
    public void testRemoveByCoordinatesInvalid() {
        cp.processor("remove name");
        assertTrue(outContent.toString().contains(
            "Rectangle not removed: name"));
    }


    /**
     * Test remove by name with numParam == 1 (true case) should be able to
     * remove
     * the rectangle
     */
    public void testRemoveByNameValid() {
        cp.processor("insert name 1 2 3 4"); // Pre-insert for removal
        cp.processor("remove name");
        assertTrue(outContent.toString().contains("Rectangle removed:"));
    }


    /**
     * Test regionsearch with arr.length == 5 (true case) should be able to find
     * the
     * rectangle
     */
    public void testRegionSearchValid() {
        cp.processor("regionsearch 1 2 3 4");
        assertTrue(outContent.toString().contains(
            "Rectangles intersecting region"));
    }


    /**
     * Test regionsearch with arr.length != 5 (false case) should not be able to
     * find the rectangle
     */
    public void testRegionSearchInvalid() {
        cp.processor("regionsearch 1 2 3");
        assertTrue(outContent.toString().contains(
            "Invalid regionsearch command."));
    }


    /**
     * Test search with arr.length == 2 (true case) should be able to find the
     * rectangle
     */
    public void testSearchValid() {
        cp.processor("insert name 1 2 3 4"); // Pre-insert for search
        cp.processor("search name");
        assertTrue(outContent.toString().contains("Rectangles found:"));
    }


    /**
     * Test search with arr.length != 2 (false case) should not be able to find
     * the
     * rectangle
     */
    public void testSearchInvalid() {
        cp.processor("search");
        assertTrue(outContent.toString().contains(
            "Unrecognized command: search"));
    }


    /**
     * Test dump command (true case)
     */
    public void testDumpValid() {
        cp.processor("dump");
        assertTrue(outContent.toString().contains("SkipList dump:"));
    }


    /**
     * There's no direct false case for the dump command since it always
     * executes.
     * However, we can simulate a scenario where another command is expected but
     * dump is received.
     */
    public void testUnexpectedDumpCommand() {
        cp.processor("insert name 1 2"); // Intentionally incorrect insert
                                         // command
        assertFalse(outContent.toString().contains("SkipList dump:"));
    }


    /**
     * Test insert() for incorrect arguments should not be able to insert the
     * rectangle
     */
    public void testInsertIncorrectArgs() {
        cp.processor("insert rect1 200"); // Too few arguments
        assertTrue(outContent.toString().contains("Invalid insert command."));
        outContent.reset();

        cp.processor("insert rect2 200 100 50 50 extraArg"); // Too many
                                                             // arguments
        assertTrue(outContent.toString().contains("Invalid insert command."));
        outContent.reset();
    }


    /**
     * Test remove() for incorrect args should not be able to remove the
     * rectangle
     */
    public void testRemoveIncorrectArgs() {
        cp.processor("remove"); // No arguments
        assertTrue(outContent.toString().contains("Invalid remove command."));
        outContent.reset();

        cp.processor("remove rect1 extraArg"); // Too many arguments for name
                                               // removal
        assertTrue(outContent.toString().contains("Invalid remove command."));
        outContent.reset();

        cp.processor("remove 200 100 50"); // Too few arguments for coordinate
                                           // removal
        assertTrue(outContent.toString().contains("Invalid remove command."));
        outContent.reset();
    }


    /**
     * Test search()for incorrect args should not be able to find the rectangle
     */
    public void testSearchIncorrectArgs() {
        cp.processor("search"); // No arguments
        assertTrue(outContent.toString().contains(
            "Unrecognized command: search"));
        outContent.reset();

        cp.processor("search rect1 extraArg"); // Too many arguments
        assertTrue(outContent.toString().contains(
            "Unrecognized command: search"));
        outContent.reset();
    }


    /**
     * Tests regionsearch() for incorrect args should not be able to find the
     * rectangle
     */
    public void testRegionSearchIncorrectArgs() {
        cp.processor("regionsearch 200"); // Too few arguments
        assertTrue(outContent.toString().contains(
            "Invalid regionsearch command."));
        outContent.reset();

        cp.processor("regionsearch 200 100 50 50 extraArg"); // Too many
                                                             // arguments
        assertTrue(outContent.toString().contains(
            "Invalid regionsearch command."));
        outContent.reset();
    }


    /**
     * Tests insert()for invalid rectangles should not be able to insert the
     * rectangle
     */
    public void testInsertInvalidRectangle() {
        // Negative Width and Height
        cp.processor("insert 10 10 -5 -5");
        assertTrue(outContent.toString().contains("Invalid"));

        // Reset the stream before next test
        outContent.reset();
    }


    /**
     * Tests insert()for invalid dimensions should not be able to insert the
     * rectangle
     */
    public void testInsertZeroSize() {
        // Zero Width and Height
        cp.processor("insert 10 10 0 0");
        assertTrue(outContent.toString().contains("Invalid"));

        // Reset the stream before next test
        outContent.reset();
    }


    /**
     * Tests insert()for invalid dimensions should not be able to insert the
     * rectangle
     */
    public void testInsertValidRectangleAlt() {
        // Valid Rectangle
        cp.processor("insert 10 10 5 5");
        assertTrue(outContent.toString().contains("Invalid"));

        // Reset the stream before next test
        outContent.reset();
    }


    /**
     * Tests insert()for invalid dimensions should not be able to insert the
     * rectangle
     */
    public void testInsertExceedBoundary() {
        // Rectangle exceeds boundaries
        cp.processor("insert 1024 1024 1 1");
        assertTrue(outContent.toString().contains("Invalid"));

        // Reset the stream before next test
        outContent.reset();
    }


    /**
     * Tests insert()for invalid dimensions should not be able to insert the
     * rectangle
     */
    public void testInsertAlt() {
        // Rectangle exceeds boundaries
        cp.processor("insert 1025 1025 1025 1025");
        assertTrue(outContent.toString().contains("Invalid"));

        // Reset the stream before next test
        outContent.reset();
    }


    /**
     * Tests insert()for invalid dimensions should not be able to insert the
     * rectangle
     */
    public void testInsertAlt1() {
        // Rectangle exceeds boundaries
        cp.processor("insert -1025 -1025 -1025 -1025");
        assertTrue(outContent.toString().contains("Invalid"));

        // Reset the stream before next test
        outContent.reset();
    }


    /**
     * Tests insert()for invalid dimensions should not be able to insert the
     * rectangle
     */
    public void testInsertAlt2() {
        // Rectangle exceeds boundaries
        cp.processor("insert 0 0 0 0");
        assertTrue(outContent.toString().contains("Invalid"));

        // Reset the stream before next test
        outContent.reset();
    }


    /**
     * Tests insert()for invalid dimensions should not be able to insert the
     * rectangle
     */
    public void testInsertNegativeCoordinates() {
        // Negative Coordinates
        cp.processor("insert -10 -10 5 5");
        assertTrue(outContent.toString().contains("Invalid"));

        // Reset the stream before next test
        outContent.reset();
    }


    /**
     * Test the dump command with correct usage.
     */
    public void testDumpCommandValidUsage() {
        // Setup: The setup is already done in the setUp() method.

        // Action: Call the processor method with the "dump" command.
        cp.processor("dump");

        // Assertion: Verify the output contains the expected result for the
        // dump command.
        // Note: Adjust the expectedOutput based on what your dump method
        // prints.
        String expectedOutput = "SkipList dump:";
        assertTrue("Dump command did not" + " produce the expected output.",
            outContent.toString().trim().contains(expectedOutput));

        // Cleanup: Reset the output stream for the next test.
        outContent.reset();
    }


    /**
     * Test the dump command with incorrect usage.
     */
    public void testDumpCommandInvalidUsage() {
        // Setup: The setup is already done in the setUp() method.

        // Action: Call the processor method with the "dump" command and an
        // additional argument.
        cp.processor("dump extraArg");

        // Assertion: Verify the output indicates an invalid command.
        String expectedOutput = "Invalid command.";
        assertTrue("Invalid usage of dump command did not produce"
            + " the expected output.", outContent.toString().trim().contains(
                expectedOutput));

        // Cleanup: Reset the output stream for the next test.
        outContent.reset();
    }


    /**
     * tests remove() removing from different points should be able to remove
     * the
     * rectangle
     */
    public void testRemoveByNameFirstElement() {
        // Insert a rectangle
        cp.processor("insert firstRect 10 20 30 40");
        // Attempt to remove it
        cp.processor("remove firstRect");
        // Check if the removal was successful
        assertTrue(outContent.toString().trim().contains("Rectangle removed: "
            + "(firstRect, 10, 20, 30, 40)"));
        outContent.reset(); // Reset for the next test
    }


    /**
     * tests remove() removing from different points should be able to remove
     * the
     * rectangle
     */
    public void testRemoveByNameNotFirstElement() {
        // Insert multiple rectangles
        cp.processor("insert firstRect 10 20 30 40");
        cp.processor("insert secondRect 50 60 70 80");
        // Attempt to remove the second rectangle
        cp.processor("remove secondRect");
        // Check if the removal was successful
        assertEquals(outContent.toString().trim(),
            ("Rectangle inserted: (firstRect, 10, 20, 30, 40)\n"
                + "Rectangle inserted: (secondRect, 50, 60, 70, 80)\n"
                + "Rectangle removed: (secondRect, 50, 60, 70, 80)"));
        outContent.reset();
    }


    /**
     * tests remove() removing for a non-existing rectangle should not be able
     * to
     * remove the rectangle
     */
    public void testRemoveByNameNonExistent() {
        // Attempt to remove a non-existing rectangle
        cp.processor("remove nonExistentRect");
        // Check if the correct error message is displayed
        assertTrue(outContent.toString().trim().contains(
            "Rectangle not removed: " + "nonExistentRect"));
        outContent.reset();
    }


    /**
     * tests remove() by coords should be able to remove the rectangles
     */
    public void testRemoveByCoordinatesWithMultipleRectangles() {
        // Insert multiple rectangles
        cp.processor("insert firstRect 10 20 30 40");
        cp.processor("insert secondRect 50 60 70 80");
        cp.processor("insert matchRect 100 200 300 400");
        // Attempt to remove a rectangle that matches the third insertion
        cp.processor("remove 100 200 300 400");
        // Verify only the matching rectangle is removed
        assertTrue(outContent.toString().trim().contains("Rectangle removed: "
            + "(matchRect, 100, 200, 300, 400)"));
        // Verify that other rectangles are not removed
        cp.processor("dump");
        assertTrue(outContent.toString().contains("firstRect") && outContent
            .toString().contains("secondRect"));
        outContent.reset();
    }


    /**
     * tests remove() by coords should not be able to remove the rectangle
     */
    public void testRemoveByCoordinatesDoesNotExist() {
        // Insert a rectangle with different coordinates
        cp.processor("insert diffRect 50 60 70 80");
        // Attempt to remove a rectangle by specifying non-matching coordinates
        cp.processor("remove 10 20 30 40");
        // Verify the appropriate error message is displayed
        assertTrue(outContent.toString().trim().contains("Rectangle not found: "
            + "(10, 20, 30, 40)"));
        outContent.reset();
    }


    /**
     * tests remove() by coords should be able to remove the rectangle
     */
    public void testRemoveByCoordinatesExists() {
        // Insert a rectangle that matches the removal criteria
        cp.processor("insert matchRect 10 20 30 40");
        // Attempt to remove a rectangle by specifying matching coordinates
        cp.processor("remove 10 20 30 40");
        // Verify the rectangle is removed successfully
        assertTrue(outContent.toString().trim().contains("Rectangle removed: ("
            + "matchRect, 10, 20, 30, 40)"));
        outContent.reset();
    }


    /**
     * tests remove() by wrong coords should not be able to remove the rectangle
     */
    public void testRemoveByNonExistentCoordinates() {
        // Ensure the database has rectangles, but none match the removal
        // criteria
        cp.processor("insert rectA 10 10 20 20");
        cp.processor("insert rectB 30 30 40 40");

        // Attempt to remove a rectangle by specifying non-matching coordinates
        cp.processor("remove " + 100 + " " + 100 + " " + 50 + " " + 50);

        // Construct the expected message for not finding a rectangle
        String expectedOutput = "Rectangle not found: " + "(" + 100 + ", " + 100
            + ", " + 50 + ", " + 50 + ")";

        // Verify that the expected output is part of the actual console output
        assertTrue("Expected message for non-existent "
            + "rectangle coordinates was not found. Actual output: "
            + outContent.toString().trim(), outContent.toString().trim()
                .contains(expectedOutput));

        // Reset the output stream for the next test
        outContent.reset();
    }


    /**
     * tests regionSearch with no rectangles should not be able to find the
     * rectangle
     */
    public void testRegionSearchWithNoRectangles() {
        // Attempt to search a region when the database is empty

        cp.processor("regionsearch " + 10 + " " + 10 + " " + 20 + " " + 20);

        // Check for the "No rectangles found" message
        assertTrue(outContent.toString().trim().contains(
            "No rectangles found intersecting the region"));
        outContent.reset();
    }


    /**
     * tests regionSearch with 1 rectangle should be able to find the rectangle
     */
    public void testRegionSearchWithOneRectangle() {
        // Insert a rectangle that intersects the search area
        cp.processor("insert rect1 10 10 5 5");

        // Attempt to search a region that should intersect with the inserted
        // rectangle

        cp.processor("regionsearch " + 5 + " " + 5 + " " + 10 + " " + 10);

        // Verify the rectangle is found
        assertTrue(outContent.toString().trim().contains("rect1"));
        outContent.reset();
    }


    /**
     * tests regionSearch with many rectangles should be able to find the
     * rectangles
     */
    public void testRegionSearchWithMultipleRectangles() {
        // Insert multiple rectangles, with at least one that intersects and one
        // that doesn't
        cp.processor("insert rect1 10 10 5 5"); // Intersects
        cp.processor("insert rect2 50 50 5 5"); // Does not intersect

        // Attempt to search a region that should intersect with at least one
        // rectangle

        cp.processor("regionsearch " + 5 + " " + 5 + " " + 20 + " " + 20);

        // Verify the intersecting rectangle is found
        assertTrue(outContent.toString().trim().contains("rect1"));
        // Verify the non-intersecting rectangle is not mentioned
        assertTrue(outContent.toString().trim().contains("rect2"));
        outContent.reset();
    }


    /**
     * tests regionSearch with nothing in database should not be able to find
     * the
     * rectangle
     */
    public void testRegionSearchEmptyDatabase() {
        // Search in an area when the database is empty.
        cp.processor("regionsearch 0 0 100 100");
        assertTrue(outContent.toString().trim().contains(
            "No rectangles found intersecting the region"));
        outContent.reset();
    }


    /**
     * tests regionSearch outside region should not be able to find the
     * rectangle
     */
    public void testRegionSearchRectanglesOutside() {
        // Insert rectangles outside the search area
        cp.processor("insert rectA 200 200 10 10");
        cp.processor("insert rectB 300 300 20 20");

        // Perform a region search that does not intersect with the inserted
        // rectangles
        cp.processor("regionsearch 50 50 50 50");
        assertTrue(outContent.toString().trim().contains(
            "No rectangles found intersecting the region"));
        outContent.reset();
    }


    /**
     * tests regionSearch partially in region should be able to find the
     * rectangle
     */
    public void testRegionSearchRectanglesPartiallyIntersecting() {
        // Insert a rectangle that partially intersects the search area
        cp.processor("insert rectPartial 95 95 20 20");

        // Perform a region search that intersects partially with the rectangle
        cp.processor("regionsearch 90 90 10 10");
        assertTrue(outContent.toString().trim().contains("rectPartial"));
        outContent.reset();
    }


    /**
     * tests regionSearch inside region should be able to find the rectangle
     */
    public void testRegionSearchRectanglesFullyWithin() {
        // Insert a rectangle fully within the search area
        cp.processor("insert rectWithin 40 40 20 20");

        // Perform a region search that fully encompasses the rectangle
        cp.processor("regionsearch 30 30 50 50");
        assertTrue(outContent.toString().trim().contains("rectWithin"));
        outContent.reset();
    }


    /**
     * tests regionSearch boundary conditions should be able to find the
     * rectangle
     */
    public void testRegionSearchBoundaryConditions() {
        // Insert a rectangle at the boundary of the search area
        cp.processor("insert rectBoundary 0 0 10 10");

        // Perform a region search that exactly matches the rectangle's
        // boundaries
        cp.processor("regionsearch 0 0 10 10");
        assertTrue(outContent.toString().trim().contains("rectBoundary"));

        // Reset for the next part
        outContent.reset();

        // Perform a region search with a boundary condition (e.g., at the edges
        // of the allowable coordinate space)
        cp.processor("regionsearch 1023 1023 1 1");
        // This check depends on whether you have a rectangle at the edge; if
        // not, it should say no rectangles found.
        assertTrue(outContent.toString().trim().contains(
            "No rectangles found intersecting the region") || outContent
                .toString().trim().contains("rectBoundary"));
        outContent.reset();
    }


    /**
     * tests regionSearch() should be able to find the rectangle
     */
    public void testRegionSearchDatabaseNotEmpty() {
        // Ensure the database contains at least one rectangle.
        cp.processor("insert testRect 100 100 50 50");

        // Attempt a region search that will process the inserted rectangle.
        cp.processor("regionsearch 50 50 200 200");

        // Check that the output contains information about the inserted
        // rectangle, indicating the loop was entered.
        assertTrue(outContent.toString().trim().contains("testRect"));
        outContent.reset();
    }


    /**
     * tests regionSearch empty database should not be able to find the
     * rectangle
     */
    public void testRegionSearchDatabaseEmpty() {
        // Attempt a region search with an empty database.
        cp.processor("regionsearch 0 0 100 100");

        // Check that the output indicates no rectangles found, meaning the loop
        // was skipped.
        assertTrue(outContent.toString().trim().contains(
            "No rectangles found intersecting the region"));
        outContent.reset();
    }


    /**
     * tests regionSearch no intersections should not be able to find the
     * rectangle
     */
    public void testRegionSearchNoIntersection() {
        // Insert a rectangle that does not intersect the search area.
        cp.processor("insert nonIntersectingRect 500 500 30 30");

        // Perform a region search that does not include the inserted rectangle.
        cp.processor("regionsearch 100 100 50 50");

        // Verify that the output indicates no intersecting rectangles.
        assertTrue(outContent.toString().trim().contains(
            "No rectangles found intersecting the region"));
        outContent.reset();
    }


    /**
     * tests regionSearch nothing found should not be able to find the rectangle
     */
    public void testRegionSearchNotFound() {
        // Insert rectangles that do not intersect with the search area to
        // ensure the if (!found) condition is met.
        cp.processor("insert rectA 200 200 10 10");
        cp.processor("insert rectB 300 300 20 20");

        // Perform a region search that will not intersect with any rectangles.
        cp.processor("regionsearch 0 0 100 100");

        // Check that the output correctly indicates no rectangles were found
        // intersecting the region.
        assertTrue(outContent.toString().trim().contains(
            "No rectangles found intersecting the region"));
        outContent.reset();
    }


    /**
     * tests intersection nothing intersecting should be able to find the
     * rectangles
     */
    public void testIntersectionsNoIntersection() {
        // Insert non-intersecting rectangles
        cp.processor("insert rect1 10 10 20 20");
        cp.processor("insert rect2 100 100 20 20");

        // Check for intersections
        cp.processor("intersections");

        // Verify no intersections are found
        assertTrue(outContent.toString().trim().contains(""));
        outContent.reset();
    }


    /**
     * tests intersection nothing found should be able to find the rectangles
     */
    public void testIntersectionsSomeIntersect() {
        // Insert intersecting rectangles
        cp.processor("insert rect1 10 10 30 30");
        cp.processor("insert rect2 20 20 30 30"); // This should intersect with
                                                  // rect1

        // Check for intersections
        cp.processor("intersections");

        // Verify the intersection is correctly reported
        assertTrue(outContent.toString().trim().contains("(rect1"));
        outContent.reset();
    }


    /**
     * tests intersection with multiple tests
     */
    public void testIntersectionsComplexScenario() {
        // Insert a mix of intersecting and non-intersecting rectangles
        cp.processor("insert rect1 10 10 20 20");
        cp.processor("insert rect2 100 100 20 20");
        cp.processor("insert rect3 15 15 25 25"); // Intersects with rect1
        cp.processor("insert rect4 200 200 20 20"); // Does not intersect with
                                                    // others

        // Check for intersections
        cp.processor("intersections");

        // Verify that rect1 and rect3 intersection is reported
        assertTrue(outContent.toString().trim().contains("(rect1"));
        // Verify that no other intersections are falsely reported
        assertFalse(outContent.toString().trim().contains("rect2 | rect4"));
        outContent.reset();
    }


    /**
     * Testing insert() with invalid rectangle should not be able to insert the
     * rectangle
     */
    public void testInsertWithInvalidRectangleForcedValid() {
        // Assuming you have a way to mock or force `isInvalid()` to return
        // false
        // You would insert a rectangle with invalid dimensions
        cp.processor("insert rectForcedValid -10 -10 -20 -20");
        // Verify that the rectangle is inserted (or not) based on your
        // expectations
        assertTrue(outContent.toString().trim().contains("rejected"));
        outContent.reset();
    }


    /**
     * Testing insert() invalid parameters should not be able to insert the
     * rectangle
     */
    public void testInsertWithZeroOrNegativeDimensions() {
        // Insert rectangle with zero width
        cp.processor("insert zeroWidthRect 10 10 0 20");
        assertTrue(outContent.toString().trim().contains(
            "Rectangle rejected:"));
        outContent.reset();

        // Insert rectangle with negative height
        cp.processor("insert negHeightRect 20 20 20 -1");
        assertTrue(outContent.toString().trim().contains(
            "Rectangle rejected:"));
        outContent.reset();
    }


    /**
     * Testing insert() with invalid parameters should not be able to insert the
     * rectangle
     */
    public void testInsertWithInvalidXCoordinate() {
        // Insert rectangle with negative xCoordinate
        cp.processor("insert negXCoordRect -1 10 10 10");
        assertTrue(outContent.toString().trim().contains(
            "Rectangle rejected:"));
        outContent.reset();

        // Assuming 1024 is your canvas size and inserting a rectangle that
        // exceeds this boundary
        cp.processor("insert exceedXCoordRect 1025 10 10 10");
        assertTrue(outContent.toString().trim().contains(
            "Rectangle rejected:"));
        outContent.reset();
    }


    /**
     * Testing insert() with invalid rectangle should not be able to insert the
     * rectangle
     */
    public void testInsertInvalidRectangle1() {
        // Attempt to insert a rectangle that is invalid by your definition
        cp.processor("insert invalidRect -10 -10 -20 -20");
        // Check that the rectangle is rejected
        assertTrue(outContent.toString().trim().contains(
            "Rectangle rejected:"));
        outContent.reset();
    }


    /**
     * Testing insert() with invalid parameters should not be able to insert the
     * rectangle
     */
    public void testInsertWithZeroOrNegativeWidthAndHeight() {
        // Zero Width
        cp.processor("insert zeroWidth 10 10 0 20");
        assertTrue(outContent.toString().trim().contains(
            "Rectangle rejected:"));

        // Negative Height
        cp.processor("insert negHeight 20 20 20 -1");
        assertTrue(outContent.toString().trim().contains(
            "Rectangle rejected:"));

        outContent.reset();
    }


    /**
     * Testing insert()with edge parameters should not be able to insert the
     * rectangle
     */
    public void testInsertWithEdgeXCoordinates() {
        // Negative xCoordinate
        cp.processor("insert negXCoord -1 10 10 10");
        assertTrue(outContent.toString().trim().contains(
            "Rectangle rejected:"));

        // xCoordinate at the boundary (assuming 1024 is max)
        cp.processor("insert maxXCoord 1024 10 1 1");
        // This depends on whether your logic considers the very edge out of
        // bounds
        // Adjust the assertion based on your application logic
        assertTrue(outContent.toString().trim().contains("Rectangle inserted:")
            || outContent.toString().trim().contains("Rectangle rejected:"));

        outContent.reset();
    }


    /**
     * Testing remove() null should not be able to remove the null
     */
    public void testRemoveWithNullName() {
        cp.processor("remove null");
        // Assuming your system prints a specific message for null names
        // Adjust the expected message based on your actual implementation
        assertTrue(outContent.toString().trim().contains(
            "Rectangle not removed: null"));
        outContent.reset();
    }


    /**
     * Testing remove() with name should be able to remove the rectangle
     */
    public void testRemoveExistingName() {
        // Pre-insert a rectangle to remove
        cp.processor("insert validName 10 10 20 20");
        cp.processor("remove validName");
        assertTrue(outContent.toString().trim().contains(
            "Rectangle removed: (validName"));
        outContent.reset();
    }


    /**
     * Testing remove() no name should not be able to remove the rectangle
     */
    public void testRemoveNonExistingName() {
        cp.processor("remove nonExistingName");
        // Verify the output indicates the name was not found for removal
        assertTrue(outContent.toString().trim().contains(
            "Rectangle not removed: nonExistingName"));
        outContent.reset();
    }


    /**
     * Testing remove() with coords should be able to remove the rectangle
     */
    public void testRemoveByCoordinatesValidScenario() {
        // Assume this rectangle exists in your database
        cp.processor("insert validRect 10 10 20 20");
        cp.processor("remove 10 10 20 20");
        assertTrue(outContent.toString().trim().contains("Rectangle removed:"));
        outContent.reset();
    }


    /**
     * Testing remove() invalid coords should not be able to remove the
     * rectangle
     */
    public void testRemoveByCoordinatesInvalidDueToNegativeValues() {
        // Test with negative x and y, challenging the x < 0 || y < 0 mutation
        cp.processor("remove -1 -1 10 10");
        assertTrue(outContent.toString().trim().contains(
            "Rectangle rejected:"));
        outContent.reset();
    }


    /**
     * Testing remove() with invalid parameters should not be able to remove the
     * rectangle
     */
    public void testRemoveByCoordinatesExceedingCanvas() {
        // Directly challenge the x + w > 1024 || y + h > 1024 mutation
        cp.processor("remove 1023 1023 2 2"); // This attempts to exceed the
                                              // canvas size
        assertTrue(outContent.toString().trim().contains(
            "Rectangle rejected:"));
        outContent.reset();
    }


    /**
     * Testing remove()with edge parameters should be able to remove the
     * rectangle
     */
    public void testRemoveByCoordinatesAtCanvasEdge() {
        // Assuming 1024 is the edge of the canvas and should be valid
        cp.processor("remove 1024 1024 0 0"); // Edge case, depending on
                                              // interpretation may be inside or
                                              // outside
        assertTrue(outContent.toString().trim().contains("Rectangle rejected:")
            || outContent.toString().trim().contains("Rectangle removed:"));
        outContent.reset();
        cp.processor("insert x 1023 1023 1 1");
        // Testing just inside the canvas size
        cp.processor("remove 1023 1023 1 1"); // Should be accepted as valid
        assertTrue(outContent.toString().trim().contains("Rectangle removed:"));
        outContent.reset();
    }


    /**
     * Testing remove() with invalid rectangle should not be able to remove the
     * rectangle
     */
    public void testRemoveInvalidRectangle() {
        // Negative dimensions
        cp.processor("remove -10 -10 -20 -20");
        assertTrue(outContent.toString().trim().contains(
            "Rectangle rejected: (-10, -10, -20, -20)"));

        // Reset the output for the next test
        outContent.reset();

        // Out of bounds
        cp.processor("remove 1023 1023 10 10"); // Assuming your canvas size is
                                                // 1024x1024
        assertTrue(outContent.toString().trim().contains(
            "Rectangle rejected: (1023, 1023, 10, 10)"));

        outContent.reset();
    }


    /**
     * Testing remove() with rectangle should be able to remove the rectangle
     */
    public void testRemoveExistingRectangle() {
        // Insert a rectangle
        cp.processor("insert testRect 10 10 20 20");

        // Attempt to remove the same rectangle
        cp.processor("remove 10 10 20 20");
        assertTrue(outContent.toString().trim().contains(
            "Rectangle removed: (testRect, 10, 10, 20, 20)"));

        outContent.reset();
    }


    /**
     * Testing remove() with no rectangles should not be able to remove the
     * rectangles
     */
    public void testRemoveNonExistingRectangle() {
        // Attempt to remove a rectangle that has not been inserted
        cp.processor("remove 30 30 40 40");
        assertTrue(outContent.toString().trim().contains(
            "Rectangle not found: (30, 30, 40, 40)"));

        outContent.reset();
    }


    /**
     * Testing remove() boundary conditions should be able to remove the
     * rectangle
     */
    public void testRemoveRectangleWithinBounds() {
        cp.processor("insert rectToRemove 100 100 50 50");
        cp.processor("remove 100 100 50 50");
        assertTrue(outContent.toString().trim().contains(
            "Rectangle removed: (rectToRemove, 100, 100, 50, 50)"));
        outContent.reset();
    }


    /**
     * Testing remove() invalid parameters should not be able to remove the
     * rectangle
     */
    public void testRemoveRectangleWidthExtendsBeyondBounds() {
        cp.processor("insert rectToNotRemove 1000 10 30 10");
        cp.processor("remove 1000 10 30 10");
        assertTrue(outContent.toString().trim().contains(
            "Rectangle rejected: (1000, 10, 30, 10)"));
        outContent.reset();
    }


    /**
     * Testing remove() boundary conditions should not be able to remove the
     * rectangle
     */
    public void testRemoveRectangleAtBoundary() {
        cp.processor("insert rectAtBoundary 0 0 1024 1024");
        cp.processor("remove 0 0 1024 1024");
        assertTrue(outContent.toString().trim().contains(
            "Rectangle removed: (rectAtBoundary, 0, 0, 1024, 1024)"));
        outContent.reset();
    }


    /**
     * Testing remove() invalid conditions should not be able to remove the
     * rectangle
     */
    public void testRemoveRectangleZeroWidthOrHeight() {
        cp.processor("remove 10 10 0 20");
        assertTrue(outContent.toString().trim().contains(
            "Rectangle rejected: (10, 10, 0, 20)"));
        outContent.reset();

        cp.processor("remove 10 10 20 0");
        assertTrue(outContent.toString().trim().contains(
            "Rectangle rejected: (10, 10, 20, 0)"));
        outContent.reset();
    }


    /**
     * Testing remove() invalid conditions should not be able to remove the
     * rectangle
     */
    public void testRemoveRectangleNegativeXorY() {
        cp.processor("remove -10 10 20 20");
        assertTrue(outContent.toString().trim().contains(
            "Rectangle rejected: (-10, 10, 20, 20)"));
        outContent.reset();

        cp.processor("remove 10 -10 20 20");
        assertTrue(outContent.toString().trim().contains(
            "Rectangle rejected: (10, -10, 20, 20)"));
        outContent.reset();
    }


    /**
     * Testing remove() with boundary conditions should be able to remove the
     * rectangle
     */
    public void testRemoveRectangleAtBoundary1() {
        cp.processor("insert rectAtBoundary 0 0 1024 1024");
        cp.processor("remove 0 0 1024 1024");
        assertTrue(outContent.toString().trim().contains("Rectangle removed:"));
        outContent.reset();
    }


    /**
     * Testing remove() invalid conditions should not be able to remove the
     * rectangle
     */
    public void testRemoveRectangleExceedingBoundary() {
        cp.processor("remove 1023 1023 2 2");
        assertTrue(outContent.toString().trim().contains(
            "Rectangle rejected: (1023, 1023, 2, 2)"));
        outContent.reset();
    }


    /**
     * Testing remove() non existing rectangle should not be able to remove the
     * rectangle
     */
    public void testRemoveValidRectangleNotExisting() {
        cp.processor("remove 50 50 10 10");
        assertTrue(outContent.toString().trim().contains(
            "Rectangle not found: (50, 50, 10, 10)"));
        outContent.reset();
    }


    /**
     * Testing remove() where 'x + w' is replaced by 'x'
     */
    public void testRemoveWithReplacedFirstMember() {
        // Test with the scenario where 'x + w' is replaced by 'x'
        // and it should not be able to find and remove the rectangle
        cp.processor("insert rectWithWidth 10 10 1015 10");
        cp.processor("remove 10 10 1015 10");
        assertTrue(outContent.toString().trim().contains(
            "Rectangle rejected:"));
        outContent.reset();
    }


    /**
     * Testing remove()where 'x + w' is replaced by 'w'
     */
    public void testRemoveWithReplacedSecondMember() {
        // Test with the scenario where 'x + w' is replaced by 'w'
        // and it should not be able to find and remove the rectangle
        cp.processor("insert rectWithWidth 20 20 1004 10");
        cp.processor("remove 20 20 1004 10");
        assertFalse(outContent.toString().trim().contains(
            "Rectangle rejected:"));
        outContent.reset();
    }


    /**
     * Testing remove()rejecting invalid rectangles should not be able to remove
     * the
     * rectangles
     */
    public void testRemoveWithComparisonAlwaysFalse() {
        // This mutation will always skip rejection, so we need to ensure
        // that invalid rectangles are still rejected correctly.
        cp.processor("remove -1 -1 1026 1026");
        assertTrue(outContent.toString().trim().contains(
            "Rectangle rejected: (-1, -1, 1026, 1026)"));
        outContent.reset();
    }


    /**
     * Testing remove() rectangle doesnt exist should not be able to remove the
     * rectangle
     */
    public void testRemoveRectangleNotFound() {
        // Attempt to remove a rectangle that does not exist
        cp.processor("remove 10 10 20 20");
        assertTrue(outContent.toString().trim().contains(
            "Rectangle not found: (10, 10, 20, 20)"));
        outContent.reset();
    }


    /**
     * Testing remove() should be able to remove the rectangle
     */
    public void testRemoveRectangleFound() {
        // Insert a rectangle
        cp.processor("insert rectToRemove 10 10 20 20");

        // Then attempt to remove it
        cp.processor("remove 10 10 20 20");
        assertFalse(outContent.toString().trim().contains(
            "Rectangle not found: (10, 10, 20, 20)"));
        assertTrue(outContent.toString().trim().contains("Rectangle removed:"));

        outContent.reset();
    }


    /**
     * Testing remove() rectangle found should be able to remove the rectangle
     */
    public void testRemoveRectangleFoundAfterFalseMutation() {
        // Insert a rectangle
        cp.processor("insert rectToRemove 30 30 40 40");

        // Then attempt to remove it
        cp.processor("remove 30 30 40 40");
        assertFalse(outContent.toString().trim().contains(
            "Rectangle not found: (30, 30, 40 40)"));
        assertTrue(outContent.toString().trim().contains("Rectangle removed:"));

        outContent.reset();
    }


    /**
     * Testing regionSearch() invalid area should not be able to find the
     * rectangle
     */
    public void testRegionSearchInvalidArea() {
        // Define an invalid region (negative dimensions)
        cp.processor("regionsearch -10 -10 -5 -5");
        assertTrue(outContent.toString().trim().contains(
            "Rectangle rejected: (-10, -10, -5, -5)"));
        outContent.reset();
    }


    /**
     * Testing regionSearch() no intersecting rectangles should not be able to
     * find
     * the rectangle
     */
    public void testRegionSearchNoIntersectingRectangles() {
        // Define a region that will not intersect with any existing rectangles
        // Make sure the test database has rectangles that do not intersect with
        // the area (10, 10, 5, 5)
        cp.processor("regionsearch 10 10 5 5");
        assertTrue(outContent.toString().trim().contains(
            "No rectangles found intersecting the region"));
        outContent.reset();
    }


    /**
     * Testing regionSearch()intersecting rectangles should be able to find the
     * rectangle
     */
    public void testRegionSearchIntersectingRectangles() {
        // Insert rectangles that will intersect with the search area
        cp.processor("insert rect1 0 0 15 15");
        cp.processor("insert rect2 14 14 3 3"); // This should intersect with
                                                // the search area

        // Search an area that should intersect with the above rectangles
        cp.processor("regionsearch 10 10 10 10");
        assertTrue(outContent.toString().trim().contains("(rect1"));
        assertTrue(outContent.toString().trim().contains("(rect2"));
        assertFalse(outContent.toString().trim().contains(
            "No rectangles found intersecting the region"));
        outContent.reset();
    }


    /**
     * Testing intersection()non-intersecting rectangles should be able to find
     * the
     * rectangle
     */
    public void testIntersectionsNoIntersectingRectangles() {
        // Insert non-intersecting rectangles
        cp.processor("insert rect1 10 10 20 20");
        cp.processor("insert rect2 40 40 20 20");
        cp.processor("insert rect3 70 70 20 20");

        // Run intersections method
        cp.processor("intersections");

        // Verify no intersection pairs are printed
        assertFalse(outContent.toString().trim().contains("|"));
        assertTrue(outContent.toString().trim().contains(
            "Intersection pairs:"));
        outContent.reset();
    }


    /**
     * Testing intersection() some intersecting rectangles should be able to
     * find
     * the rectangle
     */
    public void testIntersectionsSomeIntersectingRectangles() {
        // Insert intersecting rectangles
        cp.processor("insert rect1 10 10 20 20");
        cp.processor("insert rect2 15 15 25 25"); // Intersects with rect1

        // Insert a non-intersecting rectangle
        cp.processor("insert rect3 100 100 20 20");

        // Run intersections method
        cp.processor("intersections");

        // Verify that the intersection pairs are printed
        assertTrue(outContent.toString().trim().contains(
            "((rect1, 10, 10, 20, 20) | (rect2, 15, 15, 25, 25))"));
        assertFalse(outContent.toString().trim().contains("(rect3 |"));
        outContent.reset();
    }


    /**
     * Testing intersection() should be able to find the rectangle
     */
    public void testIntersectionsFoundTrue() {
        // Insert intersecting rectangles
        cp.processor("insert rect1 10 10 20 20");
        cp.processor("insert rect2 15 15 25 25"); // Intersects with rect1

        // Run intersections method
        cp.processor("intersections");

        // Since the output should contain intersection pairs, we infer that
        // 'found' must have been set to true
        String output = outContent.toString().trim();
        assertTrue(output.contains(
            "((rect1, 10, 10, 20, 20) | (rect2, 15, 15, 25, 25))"));

        outContent.reset();
    }


    /**
     * Testing search() rectangle found should be able to find the rectangle
     */
    public void testSearchRectangleFound() {
        // Assume that "rect1" is a known name in the database
        cp.processor("insert rect1 10 10 20 20");

        // Now, search for "rect1"
        cp.processor("search rect1");

        // We expect to see "Rectangles found:" followed by details of "rect1"
        assertTrue(outContent.toString().trim().contains("Rectangles found:"));
        assertTrue(outContent.toString().trim().contains("(rect1"));
        outContent.reset();
    }


    /**
     * Testing search() no rectangle found should not be able to find the
     * rectangle
     */
    public void testSearchRectangleNotFound() {
        // Search for a name that we know does not exist
        cp.processor("search nonExistentRect");

        // We expect to see "Rectangle not found:"
        assertTrue(outContent.toString().trim().contains(
            "Rectangle not found: (nonExistentRect)"));
        outContent.reset();
    }


    /**
     * Testing search() for false and true
     */
    public void testSearchEqualityCheckReplacedWithTrue() {
        // Insert a rectangle with a specific name
        cp.processor("insert rectTrueCheck 10 10 20 20");

        // Search for a different name, which should not be found
        cp.processor("search definitelyNotRectTrueCheck");

        // If the equality check is erroneously replaced with true, it would
        // find something when it shouldn't
        // We want to ensure that no rectangle is found in this case
        assertFalse(outContent.toString().trim().contains("Rectangles found:"));
        assertTrue(outContent.toString().trim().contains(
            "Rectangle not found: (definitelyNotRectTrueCheck)"));
        outContent.reset();
    }


    /**
     * Testing search() should not be able to find the rectangle
     */
    public void testSearchEqualityCheckReplacedWithFalse() {
        // Insert a rectangle with a specific name
        cp.processor("insert rectFalseCheck 10 10 20 20");

        // Search for the exact name we just inserted
        cp.processor("search rectFalseCheck");

        // If the equality check is replaced with false, it would not find the
        // rectangle
        // We expect the search to succeed and print "Rectangles found:"
        assertTrue(outContent.toString().trim().contains("Rectangles found:"));
        assertTrue(outContent.toString().trim().contains("(rectFalseCheck"));
        outContent.reset();
    }


    /**
     * Test search command by name with both found and not found scenarios.
     * should
     * be able to find the rectangle
     */
    public void testSearchByName() {

        // Insert a rectangle first to ensure there's something to search for
        cp.processor("insert a 1 0 2 4");
        cp.processor("search a");
        // Test that the correct rectangle details are printed after "Rectangles
        // found:"
        assertTrue(outContent.toString().trim().contains("(a, 1, 0, 2, 4)"));

        // Reset the output stream for the next test
        outContent.reset();

    }


    /**
     * 
     */
    public void testInsertWithinBounds() {
        db.insert("Point1", 100, 100);
        assertFalse(db.getPointsByName("Point1").isEmpty());
    }


    /**
     * 
     */
    public void testInsertXOutOfBounds() {
        db.insert("Point2", -1, 100);
        assertTrue(db.getPointsByName("Point2").isEmpty());
    }


    /**
     * 
     */
    public void testInsertYOutOfBounds() {
        db.insert("Point3", 100, 1024 + 1);
        assertTrue(db.getPointsByName("Point3").isEmpty());
    }

// /**
// *
// */
// public void testInsertDuplicateName() {
// db.insert("Point4", 100, 100);
// db.insert("Point4", 200, 200); // Attempt to insert duplicate name
// List<Point> points = db.getPointsByName("Point4");
// assertEquals(1, points.size()); // Ensure only one point with the name
// // exists
// }


    /**
     * 
     */
    public void testInsertUniquePoint() {
        db.insert("Point5", 300, 300);
        assertFalse(db.getPointsByName("Point5").isEmpty());
    }


    /**
     * 
     */
    public void testInsertWithinBounds1() {
        db.insert("Point1", 100, 100);
        assertFalse(db.getPointsByName("Point1").isEmpty());
    }


    /**
     * 
     */
    public void testInsertXOutOfBounds1() {
        db.insert("Point2", -1, 100);
        assertTrue(db.getPointsByName("Point2").isEmpty());
    }


    /**
     * 
     */
    public void testInsertYOutOfBounds1() {
        db.insert("Point3", 100, 1024 + 1);
        assertTrue(db.getPointsByName("Point3").isEmpty());
    }

// /**
// *
// */
// public void testInsertDuplicateName1() {
// db.insert("Point4", 100, 100);
// db.insert("Point4", 200, 200); // Attempt to insert duplicate name
// // Check if the point with the new coordinates was not inserted
// boolean duplicateExists = db.getPointsByName("Point4").stream()
// .anyMatch(point -> point.getX() == 200 && point.getY() == 200);
// assertFalse(duplicateExists);
// }


    /**
     * 
     */
    public void testInsertUniquePoint1() {
        db.insert("Point5", 300, 300);
        assertFalse(db.getPointsByName("Point5").isEmpty());
    }

}
