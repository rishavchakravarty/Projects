import java.util.Iterator;

/**
 * This class is responsible for interfacing between the command processor and
 * the SkipList. The responsibility of this class is to further interpret
 * variations of commands and do some error checking of those commands. This
 * class further interpreting the command means that the two types of remove
 * will be overloaded methods for if we are removing by name or by coordinates.
 * Many of these methods will simply call the appropriate version of the
 * SkipList method after some preparation.
 * 
 * Also note that the Database class will have a clearer role in Project2,
 * where we will have two data structures. The Database class will then
 * determine
 * which command should be directed to which data structure.
 * 
 * @author CS Staff
 * 
 * @version 2024-01-22
 */
public class Database {

    // this is the SkipList object that we are using
    // a string for the name of the rectangle and then
    // a rectangle object, these are stored in a KVPair,
    // see the KVPair class for more information
    private SkipList<String, Rectangle> list;

    // This is an Iterator object over the SkipList to loop through it from
    // outside the class.
    // You will need to define an extra Iterator for the intersections method.
    private Iterator<KVPair<String, Rectangle>> itr1;

    /**
     * The constructor for this class initializes a SkipList object with String
     * and Rectangle a its parameters.
     */
    public Database() {
        list = new SkipList<String, Rectangle>();
    }


    /**
     * Inserts the KVPair in the SkipList if the rectangle has valid coordinates
     * and dimensions, that is that the coordinates are non-negative and that
     * the rectangle object has some area (not 0, 0, 0, 0). This insert will
     * add the KVPair specified into the sorted SkipList appropriately
     * 
     * @param pair
     *            the KVPair to be inserted
     */
    public void insert(KVPair<String, Rectangle> pair) {
        // Delegates the decision mostly to our SkipList, only
        // writing the correct mesage to the console from
        // that
        if (pair.getValue().isInvalid()) {
            System.out.println("Rectangle rejected: (" + pair.getKey() + ", "
                + pair.getValue().toString() + ")");
            return;
        }

        Rectangle rect = pair.getValue();
        if (rect.getxCoordinate() < 0 || rect.getyCoordinate() < 0 || rect
            .getWidth() <= 0 || rect.getHeight() <= 0 || rect.getxCoordinate()
                + rect.getWidth() > 1024 || rect.getyCoordinate() + rect
                    .getHeight() > 1024) {
            System.out.println("Rectangle rejected: " + pair);
        }
        else {
            list.insert(pair);
            System.out.println("Rectangle inserted: " + pair);
        }
    }


    /**
     * Removes a rectangle with the name "name" if available. If not an error
     * message is printed to the console.
     * 
     * @param name
     *            the name of the rectangle to be removed
     */
    public void remove(String name) {
        if (name == null) {
            System.out.println("Rectangle not removed: " + name);
            return;
        }
        boolean found = false;
        itr1 = list.iterator();
        while (itr1.hasNext()) {
            KVPair<String, Rectangle> pair = itr1.next();
            if (pair.getKey().equals(name)) {
                list.remove(name);
                System.out.println("Rectangle removed: " + pair);
                found = true;
                break;
            }
        }
        if (!found) {
            System.out.println("Rectangle not removed: " + name);
        }
    }


    /**
     * Removes a rectangle with the specified coordinates if available. If not
     * an error message is printed to the console.
     * 
     * @param x
     *            x-coordinate of the rectangle to be removed
     * @param y
     *            x-coordinate of the rectangle to be removed
     * @param w
     *            width of the rectangle to be removed
     * @param h
     *            height of the rectangle to be removed
     */
    public void remove(int x, int y, int w, int h) {
        Rectangle toRemove = new Rectangle(x, y, w, h);
        if (toRemove.isInvalid() || x < 0 || y < 0 || x + w > 1024 || y
            + h > 1024) {
            System.out.println("Rectangle rejected: (" + x + ", " + y + ", " + w
                + ", " + h + ")");
            return;
        }
        boolean found = false;
        itr1 = list.iterator();
        while (itr1.hasNext()) {
            KVPair<String, Rectangle> pair = itr1.next();
            if (pair.getValue().equals(toRemove)) {
                list.removeByValue(pair.getValue());
                System.out.println("Rectangle removed: " + pair);
                found = true;
                break;
            }
        }
        if (!found) {
            System.out.println("Rectangle not found: (" + x + ", " + y + ", "
                + w + ", " + h + ")");
        }
    }


    /**
     * Displays all the rectangles inside the specified region. The rectangle
     * must have some area inside the area that is created by the region,
     * meaning, Rectangles that only touch a side or corner of the region
     * specified will not be said to be in the region.
     * 
     * @param x
     *            x-Coordinate of the region
     * @param y
     *            y-Coordinate of the region
     * @param w
     *            width of the region
     * @param h
     *            height of the region
     */
    public void regionsearch(int x, int y, int w, int h) {
        Rectangle searchArea = new Rectangle(x, y, w, h);
        if (searchArea.isInvalid()) {
            System.out.println("Rectangle rejected: (" + x + ", " + y + ", " + w
                + ", " + h + ")");
            return;
        }
        System.out.println("Rectangles intersecting region (" + x + ", " + y
            + ", " + w + ", " + h + "):");
        boolean found = false;
        itr1 = list.iterator();
        while (itr1.hasNext()) {
            KVPair<String, Rectangle> pair = itr1.next();
            if (pair.getValue().intersect(searchArea)) {
                System.out.println(pair);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No rectangles found intersecting the region");
        }
    }


    /**
     * Prints out all the rectangles that intersect each other. Note that
     * it is better not to implement an intersections method in the SkipList
     * class
     * as the SkipList needs to be agnostic about the fact that it is storing
     * Rectangles.
     */
    public void intersections() {
        System.out.println("Intersection pairs:");
        @SuppressWarnings("unused")
        boolean found = false;

        Iterator<KVPair<String, Rectangle>> firstIterator = list.iterator();
        while (firstIterator.hasNext()) {
            KVPair<String, Rectangle> firstPair = firstIterator.next();
            Iterator<KVPair<String, Rectangle>> secondIterator = list
                .iterator();

            // Advance the second iterator to the next element after the
            // firstPair to avoid self-comparison
            while (secondIterator.hasNext() && !secondIterator.next().equals(
                firstPair)) {
                // Intentionally empty to sync secondIterator with firstPair's
                // position
            }

            // Now, compare firstPair with all following pairs
            while (secondIterator.hasNext()) {
                KVPair<String, Rectangle> secondPair = secondIterator.next();
                if (firstPair.getValue().intersect(secondPair.getValue())) {
                    System.out.println("(" + firstPair + " | " + secondPair
                        + ")");
                    System.out.println("(" + firstPair + " | " + secondPair
                        + ")");
                    found = true;
                }
            }
        }
    }


    /**
     * Prints out all the rectangles with the specified name in the SkipList.
     * This method will delegate the searching to the SkipList class completely.
     * 
     * @param name
     *            name of the Rectangle to be searched for
     */
    public void search(String name) {
        boolean found = false;
        itr1 = list.iterator();
        while (itr1.hasNext()) {
            KVPair<String, Rectangle> pair = itr1.next();
            if (pair.getKey().equals(name)) {
                if (!found) {
                    System.out.println("Rectangles found:");
                    found = true;
                }
                System.out.println(pair); // Print each found rectangle
            }
        }
        if (!found) {
            System.out.println("Rectangle not found: (" + name + ")");
        }
    }


    /**
     * Prints out a dump of the SkipList which includes information about the
     * size of the SkipList and shows all of the contents of the SkipList. This
     * will all be delegated to the SkipList.
     */
    public void dump() {
        list.dump();
    }

}
