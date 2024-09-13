
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

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * The main class for the Seminar Manager program.
 * This class processes the commands in the given command file.
 * It creates a SeminarDB instance to manage the seminar database.
 * The main method reads the command file and processes the commands.
 * The commands include insert, delete, search, and print.
 * 
 * @author Archit Gupta, Kinjal Pandey
 * @version 1.0
 */
public class SemManager {

    /**
     * The SeminarDB instance that manages the seminar database.
     */
    private static SeminarDB database;

    /**
     * Main method for the Seminar Manager program.
     * 
     * @param args
     *            The command line arguments.
     */
    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println(
                "Error: Incorrect number of arguments. Expected 3 arguments.");
            return;
        }

        int initialMemorySize = Integer.parseInt(args[0]);
        int initialHashSize = Integer.parseInt(args[1]);
        String commandFile = args[2];

        if (Integer.bitCount(initialMemorySize) != 1 || Integer.bitCount(
            initialHashSize) != 1) {
            System.out.println("Error: Initial memory size and "
                + "hash size must be powers of two.");
            return;
        }

        try {
            database = new SeminarDB(initialMemorySize, initialHashSize);
            processCommands(new File(commandFile));
        }
        catch (Exception e) {
            System.out.println("Error initializing the Seminar Database: " + e
                .getMessage());
            e.printStackTrace();
        }
    }


    /**
     * Process the commands in the given command file.
     * 
     * @param file
     *            The command file to process.
     * @throws FileNotFoundException
     */
    private static void processCommands(File file)
        throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.startsWith("insert")) {
                handleInsert(scanner, Integer.parseInt(line.split("\\s+")[1]));
            }
            else if (line.startsWith("delete")) {
                int id = Integer.parseInt(line.split("\\s+")[1].trim());
                try {
                    database.delete(id);
                }
                catch (Exception e) {
                    System.out.println("Error deleting record: " + e
                        .getMessage());
                }
            }
            else if (line.startsWith("search")) {
                int id = Integer.parseInt(line.split("\\s+")[1].trim());
                try {
                    database.search(id);
                }
                catch (Exception e) {
                    System.out.println("Error searching for record: " + e
                        .getMessage());
                }
            }
            else if (line.startsWith("print")) {
                handlePrint(line.split("\\s+")[1].trim());
            }
        }
        scanner.close();
    }


    /**
     * Handle the insert command.
     * 
     * @param scanner
     *            The scanner to read the insert command from.
     * @param id
     *            The ID of the record to insert.
     */
    private static void handleInsert(Scanner scanner, int id) {
        try {
            String title = scanner.nextLine().trim();
            String[] details = scanner.nextLine().trim().split("\\s+");
            String date = details[0];
            int length = Integer.parseInt(details[1]);
            int x = Integer.parseInt(details[2]);
            int y = Integer.parseInt(details[3]);
            int cost = Integer.parseInt(details[4]);
            String keywordsLine = scanner.nextLine().trim();
            String[] keywords = keywordsLine.split("\\s+");
            String description = scanner.nextLine().trim();

            database.insert(id, title, date, length, x, y, cost, keywords,
                description);
        }
        catch (Exception e) {
            System.out.println("Error inserting record for ID " + id + ": " + e
                .getMessage());
        }
    }


    /**
     * Handle the print command.
     * 
     * @param command
     *            The print command to handle.
     */
    private static void handlePrint(String command) {
        try {
            if ("hashtable".equals(command)) {
                database.hashprint();
            }
            else if ("blocks".equals(command)) {
                database.memmanprint();
            }
        }
        catch (Exception e) {
            System.out.println("Error printing: " + e.getMessage());
        }
    }
}
