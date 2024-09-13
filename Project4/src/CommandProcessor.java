// import java.util.regex.Pattern;

/**
 * The purpose of this class is to parse a text file into its appropriate, line
 * by line commands for the format specified in the project spec.
 * 
 * @author CS Staff
 * 
 * @version 2024-01-22
 */
public class CommandProcessor {

    // the database object to manipulate the
    // commands that the command processor
    // feeds to it
    private Database data;

    /**
     * The constructor for the command processor requires a database instance to
     * exist, so the only constructor takes a database class object to feed
     * commands to.
     * 
     */
    public CommandProcessor() {
        data = new Database();
    }


    /**
     * This method parses keywords in the line and calls methods in the
     * database as required. Each line command will be specified by one of the
     * keywords to perform the actions.
     * These actions are performed on specified objects and include insert,
     * remove,
     * regionsearch, search, and dump. If the command in the file line is not
     * one of these, an appropriate message will be written in the console. This
     * processor method is called for each line in the file. Note that the
     * methods called will themselves write to the console, this method does
     * not, only calling methods that do.
     * 
     * @param line
     *            a single line from the text file
     */
    public void processor(String line) {
        // converts the string of the line into an
        // array of its space (" ") delimited elements
        String[] arr = line.split("\\s{1,}");
        String command = arr[0]; // the command will be the first of these
                                 // elements
        // calls the insert function and passes the correct
        // parameters by converting the string integers into
        // their Integer equivalent, trimming the whitespace
        if (command.equals("insert")) {
            if (arr.length == 6) { // Command plus five parameters for insert
                try {
                    String name = arr[1];
                    int x = Integer.parseInt(arr[2]);
                    int y = Integer.parseInt(arr[3]);
                    int w = Integer.parseInt(arr[4]);
                    int h = Integer.parseInt(arr[5]);
                    Rectangle rect = new Rectangle(x, y, w, h);
                    KVPair<String, Rectangle> pair = new KVPair<>(name, rect);
                    data.insert(pair);
                }
                catch (NumberFormatException e) {
                    System.out.println(
                        "Invalid number format for insert command.");
                }
            }
            else {
                System.out.println("Invalid insert command.");
            }

        }
        // calls the remove function and passes the correct
        // parameters by converting the string integers into
        else if (command.equals("remove")) {
            if (arr.length == 2) { // Remove by name
                String name = arr[1];
                data.remove(name);
            }
            else if (arr.length == 5) { // Remove by coordinates
                try {
                    int x = Integer.parseInt(arr[1]);
                    int y = Integer.parseInt(arr[2]);
                    int w = Integer.parseInt(arr[3]);
                    int h = Integer.parseInt(arr[4]);
                    data.remove(x, y, w, h);
                }
                catch (NumberFormatException e) {
                    System.out.println(
                        "Invalid number format for remove command.");
                }
            }
            else {
                System.out.println("Invalid remove command.");
            }

        }
        else if (command.equals("regionsearch")) {
            if (arr.length == 5) {
                try {
                    int x = Integer.parseInt(arr[1]);
                    int y = Integer.parseInt(arr[2]);
                    int w = Integer.parseInt(arr[3]);
                    int h = Integer.parseInt(arr[4]);
                    data.regionsearch(x, y, w, h);
                }
                catch (NumberFormatException e) {
                    System.out.println(
                        "Invalid number format for regionsearch command.");
                }
            }
            else {
                System.out.println("Invalid regionsearch command.");
            }

        }
        else if (command.equals("intersections")) {
            data.intersections();

        }
        else if (command.equals("search")) {
            if (arr.length == 2) {
                String name = arr[1];
                data.search(name);
            }
            else {
                System.out.println("Unrecognized command: " + line);
            }

        }
        else if (command.equals("dump")) {
            if (arr.length == 1) {
                data.dump();
            }
            else {
                System.out.println("Invalid command.");
            }

        }
        else {
            // the first white space delimited string in the line is not
            // one of the commands which can manipulate the database,
            // a message will be written to the console
            System.out.println("Unrecognized command.");
        }
    }

}
