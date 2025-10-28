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

        int operations = 1_000_000;

        for (int i = 0; i < operations; i++) {
            // 0 = insert, 1 = search, 2 = delete, 3 = interval search, 4 = min/max
            int op = random.nextInt(5);
            int value = random.nextInt(operations);

            switch (op) {
                case 0 -> { // insert
                    myTree.insert(value);
                    verifTree.add(value);
                }

                case 1 -> { // search
                    if (verifTree.isEmpty()) break;

                    int randomExisting = getRandomElement(verifTree, random);

                    boolean inMyTree = (myTree.search(randomExisting) != null);
                    boolean inVerifTree = verifTree.contains(randomExisting);

                    if (inMyTree != inVerifTree) {
                        System.err.println("Search error for value " + randomExisting);
                        return;
                    }
                }

                case 2 -> { // delete
                    if (verifTree.isEmpty()) break;

                    int randomExisting = getRandomElement(verifTree, random);
                    myTree.delete(randomExisting);
                    verifTree.remove(randomExisting);
                }

                case 3 -> { // interval search
                    if (verifTree.size() < 2) break;

                    int from = random.nextInt(operations);
                    int to = random.nextInt(operations);

                    if (from > to) {
                        int tmp = from; from = to; to = tmp;
                    }

                    List<Integer> myResult = myTree.intervalSearch(from, to);
                    List<Integer> refResult = new ArrayList<>(verifTree.subSet(from, true, to, true));

                    if (!Objects.equals(myResult, refResult)) {
                        System.err.println("Interval search error for [" + from + ", " + to + "]");
                        System.err.println("Expected: " + refResult);
                        System.err.println("Got: " + myResult);
                        return;
                    }
                }

                case 4 -> { // findMin and findMax
                    if (verifTree.isEmpty()) break;

                    Integer expectedMin = verifTree.first();
                    Integer expectedMax = verifTree.last();
                    Integer myMin = myTree.findMin();
                    Integer myMax = myTree.findMax();

                    if (!Objects.equals(expectedMin, myMin)) {
                        System.err.println("Find minimum error: expected " + expectedMin + " got " + myMin);
                        return;
                    }
                    if (!Objects.equals(expectedMax, myMax)) {
                        System.err.println("Find maximum error: expected " + expectedMax + " got " + myMax);
                        return;
                    }
                }
            }

            int sizeTree = myTree.size();
            int sizeVerifTree = verifTree.size();

            if (sizeTree != sizeVerifTree) {
                System.err.println("Size error:\nTested tree size = " + sizeTree + "\nVerification tree size = " + sizeVerifTree);
                return;
            }

        }

        if (!myTree.verifyTree()) {
            System.err.println("This tree is not implemented correctly!");
            return;
        }

        System.out.println("This tree is implemented correctly!");
    }

    private static int getRandomElement(TreeSet<Integer> set, Random random) {
        int index = random.nextInt(set.size());
        Iterator<Integer> it = set.iterator();
        for (int i = 0; i < index; i++) it.next();
        return it.next();
    }



}
