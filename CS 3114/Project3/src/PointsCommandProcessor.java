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
 * The PointsCommandProcessor class processes commands for a PointsDatabase. It
 * reads commands from the standard input and writes results to the standard
 * output.
 * 
 * @author kinjalpandey, architg03, rishavc18
 * @version 02/25/24
 * 
 */
public class PointsCommandProcessor {
    /**
     * The PointsDatabase to process commands for.
     */
    private PointsDatabase database;

    /**
     * Constructs a new PointsCommandProcessor.
     */
    public PointsCommandProcessor() {
        database = new PointsDatabase();
    }


    /**
     * Processes a command.
     * 
     * @param line
     *            The command to process.
     */
    public void processCommand(String line) {
     // Trim the line and check if it's empty, returning early if so
        line = line.trim();
        if (line.isEmpty()) {
            return; // Skip processing for empty or whitespace-only lines
        }

        // Split the trimmed line into parts
        String[] parts = line.split("\\s+");
        String command = parts[0].toLowerCase();

        switch (command) {
            case "insert":
                handleInsert(parts);
                break;
            case "remove":
                handleRemove(parts);
                break;
            case "regionsearch":
                handleRegionSearch(parts);
                break;
            case "search":
                handleSearch(parts);
                break;
            case "duplicates":
                database.duplicates();
                break;
            case "dump":
                database.dump();
                break;
            default:
                System.out.println("Unrecognized command: " + line);
                break;
        }
    }


    /**
     * Handles the insert command.
     * 
     * @param parts
     *            The parts of the command.
     */
    private void handleInsert(String[] parts) {
        if (parts.length == 4) {
            try {
                String name = parts[1];
                int x = Integer.parseInt(parts[2]);
                int y = Integer.parseInt(parts[3]);
                database.insert(name, x, y);
            }
            catch (NumberFormatException e) {
                System.out.println("Invalid parameters for insert command.");
            }
        }
        else {
            System.out.println("Invalid insert command.");
        }
    }


    /**
     * Handles the remove command.
     * 
     * @param parts
     *            The parts of the command.
     */
    private void handleRemove(String[] parts) {
        if (parts.length == 2) {
            database.remove(parts[1]); // Remove by name
        }
        else if (parts.length == 3) {
            try {
                int x = Integer.parseInt(parts[1]);
                int y = Integer.parseInt(parts[2]);
                database.remove(x, y);
            }
            catch (NumberFormatException e) {
                System.out.println("Invalid parameters for remove command.");
            }
        }
        else {
            System.out.println("Invalid remove command.");
        }
    }


    /**
     * Handles the regionsearch command.
     * 
     * @param parts
     *            The parts of the command.
     */
    private void handleRegionSearch(String[] parts) {
        if (parts.length == 5) {
            try {
                int x = Integer.parseInt(parts[1]);
                int y = Integer.parseInt(parts[2]);
                int width = Integer.parseInt(parts[3]);
                int height = Integer.parseInt(parts[4]);
                database.regionSearch(x, y, width, height);
            }
            catch (NumberFormatException e) {
                System.out.println(
                    "Invalid parameters for regionsearch command.");
            }
        }
        else {
            System.out.println("Invalid regionsearch command.");
        }
    }


    /**
     * Handles the search command.
     * 
     * @param parts
     *            The parts of the command.
     */
    private void handleSearch(String[] parts) {
        if (parts.length == 2) {
            database.search(parts[1]);
        }
        else {
            System.out.println("Invalid search command.");
        }
    }

}
