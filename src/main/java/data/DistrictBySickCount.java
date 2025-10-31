package data;

public class DistrictBySickCount implements Comparable<DistrictBySickCount> {
    private int districtId;
    private int sickCount;

    public DistrictBySickCount(int districtId, int sickCount) {
        this.districtId = districtId;
        this.sickCount = sickCount;
    }

    public int getDistrictId() { return districtId; }
    public int getSickCount() { return sickCount; }

    @Override
    public int compareTo(DistrictBySickCount other) {
        int countCompare = Integer.compare(other.sickCount, this.sickCount);
        if (countCompare != 0) return countCompare;
        return Integer.compare(this.districtId, other.districtId);
    }
}
