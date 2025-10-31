package data;

import avl.AVLTree;

public class DistrictById implements Comparable<DistrictById> {
    private int districtId;
    private AVLTree<PCRTestByDate> testsByDate;
    private AVLTree<PCRTestByValue> positiveTestsByValue;

    public DistrictById(int districtId) {
        this.districtId = districtId;
        this.testsByDate = new AVLTree<>();
        this.positiveTestsByValue = new AVLTree<>();
    }

    public int getDistrictId() {
        return districtId;
    }

    public AVLTree<PCRTestByDate> getTestsByDate() {
        return testsByDate;
    }

    public AVLTree<PCRTestByValue> getPositiveTestsByValue() {
        return positiveTestsByValue;
    }

    @Override
    public int compareTo(DistrictById other) {
        return Integer.compare(this.districtId, other.districtId);
    }

    @Override
    public String toString() {
        return String.valueOf(districtId);
    }

    public static DistrictById fromString(String csvLine) {
        try {
            int districtId = Integer.parseInt(csvLine.trim());
            return new DistrictById(districtId);
        } catch (Exception e) {
            System.err.println("Error with parsing DistrictById: " + csvLine);
        }
        return null;
    }
}