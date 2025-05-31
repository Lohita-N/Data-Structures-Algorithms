package lxn240002;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BTreePrinter {

    public static <T> void printNode(lxn240002.BinarySearchTree.Entry<T> root) {
        int maxLevel = maxLevel(root);// Compute the depth of the tree
        printNodeInternal(Collections.singletonList(root), 1, maxLevel);
    }

    private static <T> void printNodeInternal(List<lxn240002.BinarySearchTree.Entry<T>> nodes, int level, int maxLevel) {
        if (nodes.isEmpty() || isAllElementsNull(nodes))
            return;

        int floor = maxLevel - level;
        int edgeLines = (int) Math.pow(2, Math.max(floor - 1, 0));
        int firstSpaces = (int) Math.pow(2, floor) - 1;
        int betweenSpaces = (int) Math.pow(2, (floor + 1)) - 1;

        printWhitespaces(firstSpaces);

        List<lxn240002.BinarySearchTree.Entry<T>> newNodes = new ArrayList<>();
        for (lxn240002.BinarySearchTree.Entry<T> node : nodes) {
            if (node != null) {
                System.out.print(node.element);
                newNodes.add(node.left);
                newNodes.add(node.right);
            } else {
                newNodes.add(null);
                newNodes.add(null);
                System.out.print(" ");
            }

            printWhitespaces(betweenSpaces);
        }
        System.out.println();
        // Print branch connection
        for (int i = 1; i <= edgeLines; i++) {
            for (lxn240002.BinarySearchTree.Entry<T> node : nodes) {
                printWhitespaces(firstSpaces - i);
                if (node == null) {
                    printWhitespaces(edgeLines * 2 + i + 1);
                    continue;
                }
                // Print left branch
                if (node.left != null)
                    System.out.print("/");
                else
                    printWhitespaces(1);

                printWhitespaces(i * 2 - 1);
                // Print right branch
                if (node.right != null)
                    System.out.print("\\");
                else
                    printWhitespaces(1);

                printWhitespaces(edgeLines * 2 - i);
            }
            System.out.println();
        }
        // Recursively print next level
        printNodeInternal(newNodes, level + 1, maxLevel);
    }
    // Prints a given # of whitespace
    private static void printWhitespaces(int count) {
        for (int i = 0; i < count; i++)
            System.out.print(" ");
    }
    // Computes the max depth of the tree
    private static <T> int maxLevel(lxn240002.BinarySearchTree.Entry<T> node) {
        if (node == null)
            return 0;
        return Math.max(maxLevel(node.left), maxLevel(node.right)) + 1;
    }
    // Checks if all elements in a list are null
    private static <T> boolean isAllElementsNull(List<T> list) {
        for (Object obj : list) {
            if (obj != null)
                return false;
        }
        return true;
    }
}
