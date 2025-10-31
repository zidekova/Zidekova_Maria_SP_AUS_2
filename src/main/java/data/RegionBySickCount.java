package data;

public class RegionBySickCount implements Comparable<RegionBySickCount> {
    private int regionId;
    private int sickCount;

    public RegionBySickCount(int regionId, int sickCount) {
        this.regionId = regionId;
        this.sickCount = sickCount;
    }

    public int getRegionId() { return regionId; }
    public int getSickCount() { return sickCount; }

    @Override
    public int compareTo(RegionBySickCount other) {
        int countCompare = Integer.compare(other.sickCount, this.sickCount);
        if (countCompare != 0) return countCompare;
        return Integer.compare(this.regionId, other.regionId);
    }
}
