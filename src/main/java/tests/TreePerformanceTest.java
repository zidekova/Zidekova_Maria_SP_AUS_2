package tests;

import avl.AVLTree;
import bst.BSTree;

import java.util.*;

public class TreePerformanceTest {
    private static final int INSERT_COUNT = 10_000_000;
    private static final int DELETE_COUNT = 2_000_000;
    private static final int SEARCH_COUNT = 5_000_000;
    private static final int INTERVAL_SEARCH_COUNT = 1_000_000;
    private static final int MIN_MAX_COUNT = 2_000_000;

    private Random random = new Random(42);

    static void main(String[] args) {
        TreePerformanceTest test = new TreePerformanceTest();
        test.runAllTests();
        //test.smallTest();
    }

    public void runAllTests() {
        System.out.println("=== PERFORMANCE TESTS OF BSTREE AND AVLTREE ===\n");

        // generate data
        System.out.println("GENERATING DATA...");
        List<Integer> insertData = generateRandomData(INSERT_COUNT);
        List<Integer> deleteData = generateRandomData(DELETE_COUNT);
        List<Integer> searchData = generateRandomData(SEARCH_COUNT);

        // BST
        System.out.println("\n--- BSTREE ---");
        BSTree<Integer> bstTree = new BSTree<>();
        testBST(bstTree, insertData, deleteData, searchData);

        // AVL
        System.out.println("\n--- AVLTREE ---");
        AVLTree<Integer> avlTree = new AVLTree<>();
        testAVL(avlTree, insertData, deleteData, searchData);
    }

    private void testBST(BSTree<Integer> tree, List<Integer> insertData, List<Integer> deleteData, List<Integer> searchData) {
        // test insert
        long insertTime = testInsert(tree, insertData);
        System.out.printf("Inserting %,d elements: %,d ms%n", INSERT_COUNT, insertTime);

        // test search
        long searchTime = testSearch(tree, searchData);
        System.out.printf("Random search %,d elemets: %,d ms%n", SEARCH_COUNT, searchTime);

        // test min
        long minTime = testMin(tree);
        System.out.printf("Search min %,d times: %,d ms%n", MIN_MAX_COUNT, minTime);

        // test max
        long maxTime = testMax(tree);
        System.out.printf("Search max %,d times: %,d ms%n", MIN_MAX_COUNT, maxTime);

        // test delete
        long deleteTime = testDelete(tree, deleteData);
        System.out.printf("Deleting %,d elements: %,d ms%n", DELETE_COUNT, deleteTime);
    }

    private void testAVL(AVLTree<Integer> tree, List<Integer> insertData, List<Integer> deleteData, List<Integer> searchData) {
        // test insert
        long insertTime = testInsert(tree, insertData);
        System.out.printf("Inserting %,d elements: %,d ms%n", INSERT_COUNT, insertTime);

        // test search
        long searchTime = testSearch(tree, searchData);
        System.out.printf("Random search %,d elements: %,d ms%n", SEARCH_COUNT, searchTime);

        // test min
        long minTime = testMin(tree);
        System.out.printf("Search min %,d times: %,d ms%n", MIN_MAX_COUNT, minTime);

        // test max
        long maxTime = testMax(tree);
        System.out.printf("Search max %,d times: %,d ms%n", MIN_MAX_COUNT, maxTime);

        // test delete
        long deleteTime = testDelete(tree, deleteData);
        System.out.printf("Deleting %,d elements: %,d ms%n", DELETE_COUNT, deleteTime);

    }

    private long testInsert(BSTree<Integer> tree, List<Integer> data) {
        long startTime = System.currentTimeMillis();

        for (Integer value : data) {
            tree.insert(value);
        }

        long endTime = System.currentTimeMillis();
        return endTime - startTime;
    }

    private long testSearch(BSTree<Integer> tree, List<Integer> data) {
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < SEARCH_COUNT; i++) {
            tree.search(data.get(i));
        }

        long endTime = System.currentTimeMillis();
        return endTime - startTime;
    }

    private long testMin(BSTree<Integer> tree) {
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < MIN_MAX_COUNT; i++) {
            tree.findMin();
        }

        long endTime = System.currentTimeMillis();
        return endTime - startTime;
    }

    private long testMax(BSTree<Integer> tree) {
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < MIN_MAX_COUNT; i++) {
            tree.findMax();
        }

        long endTime = System.currentTimeMillis();
        return endTime - startTime;
    }

    private long testDelete(BSTree<Integer> tree, List<Integer> data) {
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < DELETE_COUNT; i++) {
            tree.delete(data.get(i));
        }

        long endTime = System.currentTimeMillis();
        return endTime - startTime;
    }

    private List<Integer> generateRandomData(int count) {
        List<Integer> data = new ArrayList<>(count);
        Set<Integer> used = new HashSet<>();

        for (int i = 0; i < count; i++) {
            int value;
            do {
                value = random.nextInt(Integer.MAX_VALUE);
            } while (!used.add(value));

            data.add(value);
        }

        return data;
    }

    public void smallTest() {
        System.out.println("=== SMALL TEST ===");

        AVLTree<Integer> avl = new AVLTree<>();
        BSTree<Integer> bst = new BSTree<>();

        List<Integer> sortedData =  new ArrayList<>();

        for (int i = 0; i < 100000; i++) {
            sortedData.add(i);
        }

        System.out.println("Inserting data in AVL...");
        long start = System.currentTimeMillis();
        for (Integer num : sortedData) {
            avl.insert(num);
        }
        long avlTimeInsert = System.currentTimeMillis() - start;

        System.out.println("Inserting data in BST...");
        start = System.currentTimeMillis();
        for (Integer num : sortedData) {
            bst.insert(num);
        }
        long bstTimeInsert = System.currentTimeMillis() - start;

        System.out.println("Searching in AVL...");
        start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            avl.search(5296);
        }
        long avlTimeSearch = System.currentTimeMillis() - start;

        System.out.println("Searching in BST...");
        start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            bst.search(5296);
        }
        long bstTimeSearch = System.currentTimeMillis() - start;

        System.out.println("Deleting in AVL...");
        start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            avl.delete(5296);
        }
        long avlTimeDelete = System.currentTimeMillis() - start;

        System.out.println("Deleting in BST...");
        start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            bst.delete(5296);
        }
        long bstTimeDelete = System.currentTimeMillis() - start;

        System.out.printf("AVL insert time: %d ms%n", avlTimeInsert);
        System.out.printf("BST insert time: %d ms%n", bstTimeInsert);
        System.out.printf("AVL search time: %d ms%n", avlTimeSearch);
        System.out.printf("BST search time: %d ms%n", bstTimeSearch);
        System.out.printf("AVL delete time: %d ms%n", avlTimeDelete);
        System.out.printf("BST delete time: %d ms%n", bstTimeDelete);
    }

}