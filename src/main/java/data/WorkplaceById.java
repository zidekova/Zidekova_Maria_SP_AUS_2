package data;

import avl.AVLTree;

public class WorkplaceById implements Comparable<WorkplaceById> {
    private int workplaceId;
    private AVLTree<PCRTestByDate> testsByDate;


    public WorkplaceById(int workplaceId) {
        this.workplaceId = workplaceId;
        this.testsByDate = new AVLTree<>();
    }

    public int getWorkplaceId() {
        return workplaceId;
    }

    public AVLTree<PCRTestByDate> getTestsByDate() {
        return testsByDate;
    }

    @Override
    public int compareTo(WorkplaceById other) {
        return Integer.compare(this.workplaceId, other.workplaceId);
    }

    @Override
    public String toString() {
        return String.valueOf(workplaceId);
    }

    public static WorkplaceById fromString(String csvLine) {
        try {
            int workplaceId = Integer.parseInt(csvLine.trim());
            return new WorkplaceById(workplaceId);
        } catch (Exception e) {
            System.err.println("Error with parsing WorkplaceById: " + csvLine);
        }
        return null;
    }
}