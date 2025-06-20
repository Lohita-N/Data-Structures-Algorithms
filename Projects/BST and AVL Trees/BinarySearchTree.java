import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Scanner;

public class BinarySearchTree <T extends Comparable<? super T>> implements Iterable<T> {

    // Node structure for the BST
    public static class Entry<T> {
        T element;
        Entry<T> left, right;

        public Entry(T x, Entry<T> left, Entry<T> right) {
            this.element = x;
            this.left = left;
            this.right = right;
        }
    }

    public Entry<T> root;// Root of the BST
    public int size;// Number of element in the BST

    // Constructor - an empty BST
    public BinarySearchTree() {
        root = null;
        size = 0;
    }

    // Checks if the tree contains a given element
    public boolean contains(T x) {
        Entry<T> current = root;
        while (current != null) {
            int com = x.compareTo(current.element);
            if (com == 0) {
                return true;// Element found
            } else if (com < 0) {
                current = current.left;// Search the left subtree
            } else {
                current = current.right;// Search the right subtree
            }
        }
        return false;// Element not found
    }

    // Adds an element to the BST - return true if added and false if is already present
    public boolean add(T x) {
        if (root == null) {
            root = new Entry<>(x, null, null);
            size++;
            return true;// First element added
        }
        Entry<T> current = root;
        while (true) {
            int com = x.compareTo(current.element);
            if (com == 0) {
                current.element = x;// Updating the value
                return false;
            } else if (com < 0) {
                if (current.left == null) {
                    current.left = new Entry<>(x, null, null);
                    size++;
                    return true;
                }
                current = current.left;
            } else {
                if (current.right == null) {
                    current.right = new Entry<>(x, null, null);
                    size++;
                    return true;
                }
                current = current.right;
            }
        }
    }

    // Removes an element from the BST - return the removed element is founded otherwise it is null
    public T remove(T x) {
        Entry<T> current = root, parent = null;
        while (current != null) {
            int com = x.compareTo(current.element);
            if (com == 0) {
                break;// Element found
            }
            parent = current;
            if (com < 0) {
                current = current.left;
            } else {
                current = current.right;
            }
        }
        if (current == null) return null;// Element not found
        T removedElement = current.element;

        // Case 1 & 2: Node has at most one child
        if (current.left == null || current.right == null) {
            Entry<T> child = (current.left != null) ? current.left : current.right;
            if (current == root) {
                root = child;
            } else if (parent.left == current) {
                parent.left = child;
            } else {
                parent.right = child;
            }
        } else {

            // Case 3: Node that has two children
            Entry<T> successor = current.right, successorParent = current;
            while (successor.left != null) {
                successorParent = successor;
                successor = successor.left;
            }
            current.element = successor.element;
            if (successorParent.left == successor) {
                successorParent.left = successor.right;
            } else {
                successorParent.right = successor.right;
            }
        }
        size--;
        return removedElement;
    }

    // Iterator method
    public Iterator<T> iterator() {
        return null;
    }

    public static void main(String[] args) throws FileNotFoundException {
        BinarySearchTree<Long> bst = new BinarySearchTree<>();
        Scanner sc;
        if (args.length > 0) {
            File file = new File(args[0]);// Read from the file if provided
            sc = new Scanner(file);
        } else {
            sc = new Scanner(System.in);// Read from standard input
        }
        String operation = "";
        long operand = 0;
        int modValue = 999983;
        long result = 0;

        // Initialize the timer
        lxn240002.Timer timer = new lxn240002.Timer();

        while (!((operation = sc.next()).equals("End"))) {
            switch (operation) {
                case "Add": {
                    operand = sc.nextInt();
                    if (bst.add(operand)) {
                        result = (result + 1) % modValue;
                    }
                    break;
                }
                case "Remove": {
                    operand = sc.nextInt();
                    if (bst.remove(operand) != null) {
                        result = (result + 1) % modValue;
                    }
                    break;
                }
                case "Contains": {
                    operand = sc.nextInt();
                    if (bst.contains(operand)) {
                        result = (result + 1) % modValue;
                    }
                    break;
                }
            }
        }

        // End the timer after all operations
        timer.end();

        // Output the results
        System.out.println(result);
        System.out.println(timer);// Print execution time and memory usage
    }

    // Prints the tree elements in the in-oder traversal
    public void printTree() {
        System.out.print("[" + size + "]");
        printTree(root);
        System.out.println();
    }

    // Inorder traversal of tree
    void printTree(Entry<T> node) {
        if (node != null) {
            printTree(node.left);
            System.out.print(" " + node.element);
            printTree(node.right);
        }
    }

    // Prints a visual representation of the tree
    public void printTreeViz() {
        lxn240002.BTreePrinter.printNode(root);
    }
}
