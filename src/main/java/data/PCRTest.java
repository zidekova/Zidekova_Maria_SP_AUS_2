package data;

import java.time.LocalDateTime;

public class PCRTest implements Comparable<PCRTest> {
    private LocalDateTime datetime;
    private String patientId;
    private int testId;
    private int workplaceId;
    private int districtId;
    private int regionId;
    private boolean positive;
    private double value;
    private String note;

    public PCRTest(LocalDateTime datetime, String patientId, int testId, int workplaceId, int districtId, int regionId, boolean positive, double value, String note) {
        this.datetime = datetime;
        this.patientId = patientId;
        this.testId = testId;
        this.workplaceId = workplaceId;
        this.districtId = districtId;
        this.regionId = regionId;
        this.positive = positive;
        this.value = value;
        this.note = note;
    }

    public LocalDateTime getDatetime() { return datetime; }
    public String getPatientId() { return patientId; }
    public int getTestId() { return testId; }
    public int getWorkplaceId() { return workplaceId; }
    public int getDistrictId() { return districtId; }
    public int getRegionId() { return regionId; }
    public boolean isPositive() { return positive; }
    public double getValue() { return value; }
    public String getNote() { return note; }

    @Override
    public int compareTo(PCRTest other) {
        return Integer.compare(this.testId, other.testId);
    }

    public boolean isPersonSick(PCRTest test, LocalDateTime date, int daysSick) {
        if (!test.isPositive()) return false;
        LocalDateTime start = test.getDatetime();
        LocalDateTime end = start.plusDays(daysSick);
        return !date.isBefore(start) && !date.isAfter(end);
    }
}
