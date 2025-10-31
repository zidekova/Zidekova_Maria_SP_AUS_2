package data;

import avl.AVLTree;

public class RegionById implements Comparable<RegionById> {
    private int regionId;
    private AVLTree<PCRTestByDate> testsByDate;

    public RegionById(int regionId) {
        this.regionId = regionId;
        this.testsByDate = new AVLTree<>();
    }

    public int getRegionId() {
        return regionId;
    }

    public AVLTree<PCRTestByDate> getTestsByDate() {
        return testsByDate;
    }

    @Override
    public int compareTo(RegionById other) {
        return Integer.compare(this.regionId, other.regionId);
    }

    @Override
    public String toString() { return String.valueOf(regionId); }

    public static RegionById fromString(String csvLine) {
        try {
            int regionId = Integer.parseInt(csvLine.trim());
            return new RegionById(regionId);
        } catch (Exception e) {
            System.err.println("Error with parsing RegionById: " + csvLine);
        }
        return null;
    }
}
