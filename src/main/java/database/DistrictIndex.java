package database;

import avl.AVLTree;
import data.PCRTest;

public class DistrictIndex implements Comparable<DistrictIndex> {
    int districtId;
    AVLTree<PCRTest> tests;
    AVLTree<PCRTest> positiveTests;

    public DistrictIndex(int districtId) {
        this.districtId = districtId;
        this.tests = new AVLTree<>();
        this.positiveTests = new AVLTree<>();
    }

    @Override
    public int compareTo(DistrictIndex other) {
        return Integer.compare(this.districtId, other.districtId);
    }
}
