import java.io.File;
import java.util.List;
import java.util.LinkedList;
import java.util.Scanner;

public class P3Driver {
    public static void main(String[] args) throws Exception {
        Scanner in;

        // Check if input is from file or console
        if (args.length > 0 && !args[0].equals("-")) {
            File file = new File(args[0]);
            in = new Scanner(file);
        } else {
            in = new Scanner(System.in);
        }

        // Check if VERBOSE mode is on
        boolean VERBOSE = false;
        if (args.length > 1) {
            VERBOSE = Boolean.parseBoolean(args[1]);
        }

        // Variables for input and output
        String operation;
        int lineno = 0;

        MDS mds = new MDS();         // Create MDS object
        Timer timer = new Timer();   // Start timer
        int id, result, total = 0, price;
        List<Integer> name = new LinkedList<>();


        // Read and process each operation
        whileloop:
        while (in.hasNext()) {
            lineno++;
            result = 0;
            operation = in.next();

            // Skip comment lines
            if (operation.charAt(0) == '#') {
                in.nextLine();
                continue;
            }

            // Handle each operation
            switch (operation) {
                case "End":
                    break whileloop; // Stop processing

                case "Insert":
                    id = in.nextInt();
                    price = in.nextInt();
                    name.clear();
                    // Read list until 0
                    while (true) {
                        int val = in.nextInt();
                        if (val == 0) break;
                        name.add(val);
                    }
                    result = mds.insert(id, price, name);
                    break;

                case "Find":
                    id = in.nextInt();
                    result = mds.find(id);
                    break;

                case "Delete":
                    id = in.nextInt();
                    result = mds.delete(id);
                    break;

                case "FindMinPrice":
                    result = mds.findMinPrice(in.nextInt());
                    break;

                case "FindMaxPrice":
                    result = mds.findMaxPrice(in.nextInt());
                    break;

                case "FindPriceRange":
                    result = mds.findPriceRange(in.nextInt(), in.nextInt(), in.nextInt());
                    break;

                case "RemoveNames":
                    id = in.nextInt();
                    name.clear();
                    // Read list until 0
                    while (true) {
                        int val = in.nextInt();
                        if (val == 0) break;
                        name.add(val);
                    }
                    result = mds.removeNames(id, name);
                    break;

                default:
                    // Handle unknown command
                    System.out.println("Unknown operation: " + operation);
                    break;
            }

            // Keep track of total result
            total += result;

            // Print details if VERBOSE mode is on
            if (VERBOSE) {
                System.out.println(lineno + "\t" + operation + "\t" + result + "\t" + total);
            }
        }

        // Print final result and timer
        System.out.println(total);
        System.out.println(timer.end());
    }

    // Timer class to measure time and memory
    public static class Timer {
        long startTime, endTime, elapsedTime, memAvailable, memUsed;

        public Timer() {
            startTime = System.currentTimeMillis();
        }

        public void start() {
            startTime = System.currentTimeMillis();
        }

        public Timer end() {
            endTime = System.currentTimeMillis();
            elapsedTime = endTime - startTime;
            memAvailable = Runtime.getRuntime().totalMemory();
            memUsed = memAvailable - Runtime.getRuntime().freeMemory();
            return this;
        }

        public String toString() {
            return "Time: " + elapsedTime + " msec.\n" +
                    "Memory: " + (memUsed / 1048576) + " MB / " +
                    (memAvailable / 1048576) + " MB.";
        }
    }
}
