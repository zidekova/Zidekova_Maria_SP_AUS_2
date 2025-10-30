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

    private final Random random = new Random();

    static void main(String[] args) {
        TreePerformanceTest test = new TreePerformanceTest();
        //test.runAllTests();
        test.runSortedDataTest();
    }

    public void runAllTests() {
        System.out.println(".....PERFORMANCE TESTS OF BSTREE, AVLTREE AND TREESET.....\n");

        // generate data
        System.out.println("GENERATING DATA...");
        List<Integer> insertData = generateRandomData();
        List<Integer> deleteData = insertData.subList(0, DELETE_COUNT);
        List<Integer> searchData = insertData.subList(0, SEARCH_COUNT);

        // BST
        System.out.println("\n... BSTREE ...");
        BSTree<Integer> bstTree = new BSTree<>();
        testTree(bstTree, insertData, deleteData, searchData);

        // AVL
        System.out.println("\n... AVLTREE ...");
        AVLTree<Integer> avlTree = new AVLTree<>();
        testTree(avlTree, insertData, deleteData, searchData);

        // TREESET
        System.out.println("\n... TREESET ...");
        TreeSet<Integer> treeSet = new TreeSet<>();
        testTreeSet(treeSet, insertData, deleteData, searchData);
    }

    public void runSortedDataTest() {
        System.out.println(".....PERFORMANCE TEST WITH SORTED DATA.....\n");

        // generate sorted data
        System.out.println("GENERATING SORTED DATA...");
        List<Integer> insertData = generateSortedData();
        List<Integer> deleteData = insertData.subList(0, DELETE_COUNT);
        List<Integer> searchData = insertData.subList(0, SEARCH_COUNT);

        // BST
        System.out.println("\n... BSTREE ...");
        BSTree<Integer> bstTree = new BSTree<>();
        testTree(bstTree, insertData, deleteData, searchData);

        // AVL
        System.out.println("\n... AVLTREE ...");
        AVLTree<Integer> avlTree = new AVLTree<>();
        testTree(avlTree, insertData, deleteData, searchData);

        // TREESET
        System.out.println("\n... TREESET ...");
        TreeSet<Integer> treeSet = new TreeSet<>();
        testTreeSet(treeSet, insertData, deleteData, searchData);
    }


    private void testTree(BSTree<Integer> tree, List<Integer> insertData, List<Integer> deleteData, List<Integer> searchData) {
        // test insert
        long insertTime = testInsert(tree, insertData);
        System.out.printf("Inserting %,d elements: %,d ms%n", INSERT_COUNT, insertTime);

        // test search
        long searchTime = testSearch(tree, searchData);
        System.out.printf("Random search %,d elemets: %,d ms%n", SEARCH_COUNT, searchTime);

        // test interval search
        long intervalTime = testIntervalSearch(tree, insertData);
        System.out.printf("Interval search %,d times: %,d ms%n", INTERVAL_SEARCH_COUNT, intervalTime);

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

    private long testIntervalSearch(BSTree<Integer> tree, List<Integer> data) {
        List<Integer> sortedData = new ArrayList<>(data);
        Collections.sort(sortedData);

        long startTime = System.currentTimeMillis();
        int n = sortedData.size();
        for (int i = 0; i < INTERVAL_SEARCH_COUNT; i++) {
            int fromIndex = random.nextInt(n - 1000);
            int toIndex = fromIndex + 500;
            int from  = sortedData.get(fromIndex);
            int to = sortedData.get(toIndex);

            tree.intervalSearch(from, to);
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

    private void testTreeSet(TreeSet<Integer> tree, List<Integer> insertData, List<Integer> deleteData, List<Integer> searchData) {
        // test insert
        long start = System.currentTimeMillis();
        tree.addAll(insertData);
        long insertTime = System.currentTimeMillis() - start;
        System.out.printf("Inserting %,d elements: %,d ms%n", INSERT_COUNT, insertTime);

        // test search
        start = System.currentTimeMillis();
        for (int i = 0; i < SEARCH_COUNT; i++) {
            tree.contains(searchData.get(i));
        }
        long searchTime = System.currentTimeMillis() - start;
        System.out.printf("Random search %,d elements: %,d ms%n", SEARCH_COUNT, searchTime);

        // test interval search
        List<Integer> sortedData = new ArrayList<>(insertData);
        Collections.sort(sortedData);

        start = System.currentTimeMillis();
        int n = sortedData.size();
        for (int i = 0; i < INTERVAL_SEARCH_COUNT; i++) {
            int fromIndex = random.nextInt(n - 1000);
            int toIndex = fromIndex + 500;
            int from  = sortedData.get(fromIndex);
            int to = sortedData.get(toIndex);

            SortedSet<Integer> set = tree.subSet(from, true, to, true);
            ArrayList<Integer> list = new ArrayList<>(set);
        }
        long intervalTime = System.currentTimeMillis() - start;
        System.out.printf("Interval search %,d times: %,d ms%n", INTERVAL_SEARCH_COUNT, intervalTime);

        // test min
        start = System.currentTimeMillis();
        for (int i = 0; i < MIN_MAX_COUNT; i++) {
            tree.first();
        }
        long minTime = System.currentTimeMillis() - start;
        System.out.printf("Search min %,d times: %,d ms%n", MIN_MAX_COUNT, minTime);

        // test max
        start = System.currentTimeMillis();
        for (int i = 0; i < MIN_MAX_COUNT; i++) {
            tree.last();
        }
        long maxTime = System.currentTimeMillis() - start;
        System.out.printf("Search max %,d times: %,d ms%n", MIN_MAX_COUNT, maxTime);

        // test delete
        start = System.currentTimeMillis();
        for (Integer value : deleteData) {
            tree.remove(value);
        }
        long deleteTime = System.currentTimeMillis() - start;
        System.out.printf("Deleting %,d elements: %,d ms%n", DELETE_COUNT, deleteTime);
    }


    private List<Integer> generateRandomData() {
        List<Integer> data = new ArrayList<>(INSERT_COUNT);
        Set<Integer> used = new HashSet<>();

        for (int i = 0; i < INSERT_COUNT; i++) {
            int value;
            do {
                value = random.nextInt(Integer.MAX_VALUE);
            } while (!used.add(value));

            data.add(value);
        }

        return data;
    }

    private List<Integer> generateSortedData() {
        List<Integer> data = new ArrayList<>(INSERT_COUNT);

        for (int i = 0; i < INSERT_COUNT; i++) {
            data.add(i);
        }

        return data;
    }
}