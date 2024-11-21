//
//// On my honor:
//// - I have not used source code obtained from another student,
//// or any other unauthorized source, either modified or unmodified.
//// - All source code and documentation used in my program is
//// either my original work, or was derived by me from the
//// source code published in the textbook for this course.
//// - I have not discussed coding details about this project with
//// anyone other than the my partner, instructor, ACM/UPE tutors
//// or the TAs assigned to this course.
//// I understand that I may discuss the concepts
//// of this program with other students, and that another student
//// may help me debug my program so long as neither of us writes
//// anything during the discussion or modifies any computer file
//// during the discussion. I have violated neither the spirit nor
//// letter of this restriction.
//
//// import java.io.FileWriter;
//// import java.io.IOException;
//// import java.nio.file.Files;
//// import java.nio.file.Path;
// import java.io.ByteArrayOutputStream;
// import java.io.File;
// import java.io.FileNotFoundException;
// import java.io.PrintStream;
// import java.util.Scanner;
// import student.TestCase;
//
/// **
// * This class tests the methods of SkipListProject class
// * which serves as the entry point of the command line
// * program.
// *
// * @author kinjalpandey, architg03, rishavc18
// * @version 02/25/24
// *
// */
// public class SkipListProjectTest extends TestCase {
//
// private final ByteArrayOutputStream outContent =
// new ByteArrayOutputStream();
// private final PrintStream originalOut = System.out;
//
// /**
// * Sets up the tests
// */
// public void setUp() throws Exception {
// System.setOut(new PrintStream(outContent)); // Redirects System.out to
// // capture console output.
// super.setUp();
// }
//
//
// /**
// * Clears for new test
// */
// public void tearDown() throws Exception {
// System.setOut(originalOut); // Resets System.out to its original state.
// outContent.reset();
// super.tearDown();
// }
//
//
// /**
// * Test running the main method with a file that contains valid commands.
// */
// public void testMainWithInvalidCommands() {
// String[] testInput = { "failedTest.txt" }; // Update path accordingly
// SkipListProject.main(testInput);
// String output = outContent.toString().trim();
//
// // Assert that the output contains error messages or specific handling
// // for invalid commands
// assertTrue(output.contains("Invalid file"));
// }
//
//
// /**
// * Test running the main method with no arguments, expecting an error
// * message.
// */
// public void testMainNoArgs() {
// String[] testInput = {};
// SkipListProject.main(testInput);
// String output = outContent.toString().trim();
// assertTrue(output.contains(
// "Invalid file. No filename in command line arguments"));
// }
//
//
// /**
// * Testing main() with valid commands
// */
// public void testMainWithValidCommands() {
// CommandProcessor cmdProc = new CommandProcessor();
// String filename = "SyntaxTest2.txt";
// File file = new File(filename);
// boolean noErrors = true;
//
// try {
// Scanner scanner = new Scanner(file);
// while (scanner.hasNextLine() && noErrors) {
// String line = scanner.nextLine();
// try {
// // Process each line with the commandProcessor
// cmdProc.processor(line.trim());
// }
// catch (Exception e) {
// noErrors = false;
// }
// }
// scanner.close();
// }
// catch (FileNotFoundException e) {
// noErrors = false;
// }
//
// // If there were no errors during processing, the test should pass
// // assertTrue(noErrors);
// }
//
//
// /**
// * testing main() with whitespaces
// */
// public void testMainWithEmpty() {
// String[] args = { "faeinmfiae.txt" };
// SkipListProject.main(args);
// // Expecting no output or a specific message indicating file is empty
// String output = outContent.toString().trim();
// assertEquals("Invalid file", output.trim());
// }
//
//
// /**
// * testing main() trailing spaces before newline
// */
// public void testMainWithTrailingNL() {
// String[] args = { "src/trailingNewline.txt" };
// SkipListProject.main(args);
// boolean noErrors = true;
// try {
// @SuppressWarnings("unused")
// String output = outContent.toString().trim();
// // System.out.println(output);
// }
// // Expecting no output or a specific message indicating file is empty
// catch (Exception e) {
// noErrors = false;
// }
//
// // If there were no errors during processing, the test should pass
// assertTrue(noErrors);
// }
//
//// /**
//// * Testing with file with one command
//// */
//// public void testSingleLineFile() {
//// String[] args = { "command.txt" }; // Ensure this file exists and
//// // has exactly one line
//// SkipListProject.main(args);
//// String output = outContent.toString().trim();
//// // Verify that some expected output is present, indicating
//// // cmdProc.processor was called once
//// assertFalse(output.contains("Invalid file"));
//// }
//
//
// /**
// * Testing with file multiple commands
// */
// public void testFileWithMultipleCommands() {
// String[] args = { "src/P1test1.txt" }; // Ensure this file has several
// // commands.
// SkipListProject.main(args);
// String output = outContent.toString().trim();
// assertFalse("Output should not be empty", output.isEmpty());
// }
//
//
// /**
// * Testing with file with lines with no whitespaces
// */
// public void testFileWithNonEmptyLines() {
// String[] args = { "src/nowhitespacecomm.txt" };
// SkipListProject.main(args);
// assertFalse("Output should indicate processing of commands", outContent
// .toString().trim().isEmpty());
// }
//
//// /**
//// * Testing with file with just whitespaces
//// */
//// public void testFileWithWhitespaceOnlyLines() {
//// String[] args = { "src/whitespaceLines.txt" }; // This file should
//// // contain lines with
//// // spaces or tabs but no
//// // commands.
//// SkipListProject.main(args);
//// assertTrue(
//// "Output should ideally be empty or indicate no commands processed",
//// outContent.toString().trim().isEmpty());
//// }
//
// }
