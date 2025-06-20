import java.util.Comparator;

public class AVLTree<T extends Comparable<? super T>> extends BinarySearchTree<T> {

    //Extending BinarySearchTree.Entry to store height for AVL balancing
    static class Entry<T> extends BinarySearchTree.Entry<T> {
        int height;// Height of the code

        Entry(T x, Entry<T> left, Entry<T> right) {
            super(x, left, right);
            height = 0;  // Default height for newly created node
        }
    }
    // Constructor - calls the BST constructor
    AVLTree() {
        super();
    }

    // Overriding add method to include AVL tree balancing logic
    @Override
    public boolean add(T x) {
        root = addRecursively((Entry<T>) root, x);
        root = balance((Entry<T>) root);  // Balance the tree after insertion
        size++;// Increase tree size
        return true;
    }

    // Recursively insert node and balance the tree
    private Entry<T> addRecursively(Entry<T> node, T x) {
        if (node == null) {
            size++;
            return new Entry<>(x, null, null);// Create a new node if empty
        }

        int cmp = x.compareTo(node.element);
        if (cmp < 0) {
            node.left = addRecursively((Entry<T>) node.left, x);// Insert in left subtree
        } else if (cmp > 0) {
            node.right = addRecursively((Entry<T>) node.right, x);// Insert in right subtree
        } else {
            node.element = x;  //Replace existing value
        }

        node = balance(node);  //Ensure balance after insertion
        return node;
    }

    //Balance the tree to maintain AVL properties
    private Entry<T> balance(Entry<T> node) {
        updateHeight(node);//Update height after modifications
        int balanceFactor = balanceFactor(node);

        // Left heavy
        if (balanceFactor > 1) {
            if (balanceFactor((Entry<T>) node.left) < 0) {
                node.left = rotateLeft((Entry<T>) node.left);  // Left-Right case
            }
            node = rotateRight(node);  // Left-Left case
        }
        // Right heavy
        else if (balanceFactor < -1) {
            if (balanceFactor((Entry<T>) node.right) > 0) {
                node.right = rotateRight((Entry<T>) node.right);  // Right-Left case
            }
            node = rotateLeft(node);  // Right-Right case
        }

        return node;
    }

    //Left rotations for right heavy trees
    private Entry<T> rotateLeft(Entry<T> node) {
        Entry<T> newRoot = (Entry<T>) node.right;
        node.right = newRoot.left;
        newRoot.left = node;
        updateHeight(node);// Update height after rotation
        updateHeight(newRoot);
        return newRoot;
    }

    //Right rotations for left heavy trees
    private Entry<T> rotateRight(Entry<T> node) {
        Entry<T> newRoot = (Entry<T>) node.left;
        node.left = newRoot.right;
        newRoot.right = node;
        updateHeight(node);// Update heights after rotation
        updateHeight(newRoot);
        return newRoot;
    }

    // Update the height of the node based on its children
    private void updateHeight(Entry<T> node) {
        node.height = Math.max(height((Entry<T>) node.left), height((Entry<T>) node.right)) + 1;
    }

    // Return the height of a node
    private int height(Entry<T> node) {
        return node == null ? -1 : node.height;
    }

    // Compute the balance factor of a node (left height - right height)
    private int balanceFactor(Entry<T> node) {
        return height((Entry<T>) node.left) - height((Entry<T>) node.right);
    }

    // Check if the AVL tree maintains its balance factors
    public boolean verify() {
        return verifyRecursively((Entry<T>) root);
    }

    // Recursively verifies AVL property is all nodes
    private boolean verifyRecursively(Entry<T> node) {
        if (node == null) {
            return true;
        }

        // Check balance factor of current node
        int balanceFactor = balanceFactor(node);
        if (balanceFactor < -1 || balanceFactor > 1) {
            return false;  // Tree is unbalanced
        }

        // Recursively check left and right subtrees
        return verifyRecursively((Entry<T>) node.left) && verifyRecursively((Entry<T>) node.right);
    }
}
