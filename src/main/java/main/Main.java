package main;

import avl.AVLTree;
import bst.BSTree;

import java.util.ArrayList;
import java.util.List;

public class Main {
    static void main() {
//        BSTree<Integer> bst = new BSTree<>();
//        BSTree<Integer> bst_empty = new BSTree<>();
//
//        int[] values = {6, 2, 11, 3, 9, 30, 2, 13, 18};
//        for (int v : values) bst.insert(v);
//
//        System.out.println("\nInsert:");
//        bst.printTree();
//
//        List<Integer> is = bst.intervalSearch(3, 12);
//        System.out.print("Interval search from 3 to 12: ");
//        for (Integer i : is) {
//            System.out.print(i + " ");
//        }
//
//        System.out.println("\nSearch 9: " + bst.search(9));
//        System.out.println("Search (not in the tree) 1: " + bst.search(1));
//        System.out.println("Search 10 in empty tree: " + bst_empty.search(10));
//
//        bst.delete(3);
//        System.out.println("\nDelete leaf 3:");
//        bst.printTree();
//
//        bst.delete(13);
//        System.out.println("\nDelete (one son) 13:");
//        bst.printTree();
//
//        bst.delete(11);
//        System.out.println("\nDelete (two sons) 11:");
//        bst.printTree();
//
//        bst.delete(6);
//        System.out.println("\nDelete root 6:");
//        bst.printTree();
//
//        bst.delete(1);
//        System.out.println("\nDelete (not in the tree) 1:");
//        bst.printTree();
//
//        bst_empty.delete(9);
//        System.out.println("\nDelete (from empty tree) 9:");
//        bst_empty.printTree();
//
        AVLTree<Integer> avl = new AVLTree<>();
        AVLTree<Integer> avl_empty = new AVLTree<>();

        int[] values = {6, 2, 11, 3, 9, 30, 2, 13, 18};
        for (int v : values) avl.insert(v);

        System.out.println("\nInsert:");
        avl.printTree();

        List<Integer> is = avl.intervalSearch(3, 12);
        System.out.print("Interval search from 3 to 12: ");
        for (Integer i : is) {
            System.out.print(i + " ");
        }

        System.out.println("\nSearch 9: " + avl.search(9));
        System.out.println("Search (not in the tree) 1: " + avl.search(1));
        System.out.println("Search 10 in empty tree: " + avl_empty.search(10));

//        avl.delete(3);
//        System.out.println("\nDelete leaf 3:");
//        avl.printTree();
//
//        avl.delete(2);
//        System.out.println("\nDelete (one son) 2:");
//        avl.printTree();
//
//        avl.delete(18);
//        System.out.println("\nDelete (two sons) 18:");
//        avl.printTree();
//
//        avl.delete(6);
//        System.out.println("\nDelete root 6:");
//        avl.printTree();
//
//        avl.delete(1);
//        System.out.println("\nDelete (not in the tree) 1:");
//        avl.printTree();
//
//        avl_empty.delete(9);
//        System.out.println("\nDelete (from empty tree) 9:");
//        avl_empty.printTree();
    }
}
