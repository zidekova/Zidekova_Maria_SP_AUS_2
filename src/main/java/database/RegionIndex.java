package database;

import avl.AVLTree;
import data.PCRTest;

public class RegionIndex implements Comparable<RegionIndex> {
    int regionId;
    AVLTree<PCRTest> tests;
    AVLTree<PCRTest> positiveTests;

    public RegionIndex(int regionId) {
        this.regionId = regionId;
        this.tests = new AVLTree<>();
        this.positiveTests = new AVLTree<>();
    }

    @Override
    public int compareTo(RegionIndex other) {
        return Integer.compare(this.regionId, other.regionId);
    }
}
