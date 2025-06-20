import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

//Driver program for AVL tree implementation.
public class AVLTreeDriver {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner sc;
        // Check if input files is provided via command - line argument
        if (args.length > 0) {
            File file = new File(args[0]);
            sc = new Scanner(file);// Read input from file
        } else {
            sc = new Scanner(System.in);// Read input from standard input
        }
        String operation = "";
        long operand = 0;
        int modValue = 999983;
        long result = 0;
        boolean VERIFY = true;

        // Create an instance of AVLTree to store long values
        lxn240002.AVLTree<Long> avlTree = new lxn240002.AVLTree<>();
        // Initialize the timer
        lxn240002.Timer timer = new lxn240002.Timer();

        while (!((operation = sc.next()).equals("End"))) {
            switch (operation) {
                case "Add": {
                    operand = sc.nextLong();
                    if(avlTree.add(operand)) { // Insert the value into the AVL tree
                        result = (result + 1) % modValue; // Update result with modulo operation
                        // Verify AVL tree balance if the flag is enabled
                        if(VERIFY && !avlTree.verify())
                            System.out.println("Invalid AVL tree ");

                    }
                    break;
                }
                case "Remove": {
                    operand = sc.nextLong();
                    if (avlTree.remove(operand) != null) { // Removes the value if it exists

                        // Verify AVL tree balance after removal
                        result = (result + 1) % modValue;
                        if(VERIFY && !avlTree.verify())
                            System.out.println("Invalid AVL tree ");
                    }
                    break;
                }
                case "Contains":{
                    operand = sc.nextLong();
                    if (avlTree.contains(operand)) { // Check if the value exists
                        result = (result + 1) % modValue; // Update result if found
                    }
                    break;
                }
            }
        }

        // End Timer
        timer.end();

        System.out.println(result); // Print the final result
        System.out.println("Is valid AVL tree? " +  avlTree.verify()); // Validate and display the AVL
        System.out.println(timer); // Display elapsed time
    }
}
