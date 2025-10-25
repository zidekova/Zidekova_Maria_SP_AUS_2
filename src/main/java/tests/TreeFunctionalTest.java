package tests;

import avl.AVLTree;
import bst.BSTree;

import java.util.*;

public class TreeFunctionalTest {
    static void main(String[] args) {
        BSTree<Integer> bst = new BSTree<>();
        AVLTree<Integer> avl = new AVLTree<>();

        System.out.println("=== FUNCTIONAL TESTS OF BSTREE AND AVLTREE ===");

        System.out.println("\n--- BSTREE ---");
        testTree(bst);
        System.out.println();
        System.out.println("\n--- AVLTREE ---");
        testTree(avl);
    }

    private static void testTree(BSTree<Integer> myTree) {
        Random random = new Random();
        TreeSet<Integer> verifTree  = new TreeSet<>();

        int operations = 100000;

        for (int i = 0; i < operations; i++) {
            // 0 = insert, 1 = search, 2 = delete
            int op = random.nextInt(3);
            int value = random.nextInt(operations);

            switch (op) {
                case 0 -> { // insert
                    myTree.insert(value);
                    verifTree.add(value);
                }

                case 1 -> { // search
                    boolean inMyTree = (myTree.search(value) != null);
                    boolean inVerifTree = verifTree.contains(value);

                    if (inMyTree != inVerifTree) {
                        System.err.println("Search error for value " + value);
                        return;
                    }
                }

                case 2 -> { // delete
                    myTree.delete(value);
                    verifTree.remove(value);
                }
            }

            int sizeTree = myTree.size();
            int sizeVerifTree = verifTree.size();

            if (sizeTree != sizeVerifTree) {
                System.err.println("Size mismatch:\nTested tree size = " + sizeTree + "\nVerification tree size = " + sizeVerifTree);
                return;
            }

        }

        System.out.println("This tree is implemented correctly!");
    }
}
