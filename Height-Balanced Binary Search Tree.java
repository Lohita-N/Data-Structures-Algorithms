class BinarySearchTree {
    class Tree {
        int element;// The value stored in this node
        Tree left, right; // Pointers
        // Constructor to create a new tree
        Tree(int x, Tree l, Tree r) {
            element = x;
            left = l;
            right = r;
        }
    }
    // Instance Variable
    Tree root; int size; // Root of binary search tree
    // Number of elements in the BST
// Creates an empty BST
    BinarySearchTree() { // constructor
        root = null;
        size = 0;
    }
    // Creates a BST with a given root node and size
    BinarySearchTree(Tree t, int s) { // constructor
        root = t;
        size = s;
    }
    // Function to build a height-balanced BST from a sorted array
    BinarySearchTree arrayToBST(int[] arr) {
        if (arr == null || array.length == 0)
            return new BinarySearchTree();
// Convert the sorted array into a balanced BST
        Tree root = arrayToTree(arr, 0, arr.length - 1);
// Return a new BST with the root and the size of the array
        return new BinarySearchTree(root, arr.length);
    }
    // Recursive helper function to construct a height-balanced BST
    Tree arrayToTree(int[] arr, int l, int r) {
        if (l > r) // Base case: if the start index is greater than the end
            index, return null
        return null;
// Find the middle element
        int mid = l + (r - l) / 2;
// Recursively construct the left and right subtrees
        Tree leftSubtree = arrayToTree(arr, l, mid - 1);
        Tree rightSubtree = arrayToTree(arr, mid + 1, r);
// Create a new Tree node with the middle element as the root
        return new Tree(arr[mid], leftSubtree, rightSubtree);
    }
}
