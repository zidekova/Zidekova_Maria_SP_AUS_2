package data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    public int compareByDate(PCRTest other) {
        int dateCompare = this.datetime.compareTo(other.datetime);
        return (dateCompare != 0) ? dateCompare :
                Integer.compare(this.testId, other.testId);
    }

    public int compareByValue(PCRTest other) {
        int valueCompare = Double.compare(other.value, this.value);
        if (valueCompare != 0) return valueCompare;

        int dateCompare = other.datetime.compareTo(this.datetime);
        return (dateCompare != 0) ? dateCompare :
                Integer.compare(this.testId, other.testId);
    }

    public boolean isPersonSick(PCRTest test, LocalDateTime date, int daysSick) {
        if (!test.isPositive()) return false;
        LocalDateTime start = test.getDatetime();
        LocalDateTime end = start.plusDays(daysSick);
        return !date.isBefore(start) && !date.isAfter(end);
    }

    @Override
    public String toString() {
        return String.join(",",
                datetime != null ? datetime.toString() : "",
                patientId != null ? patientId.replace(",", "\\,") : "",
                String.valueOf(testId),
                String.valueOf(workplaceId),
                String.valueOf(districtId),
                String.valueOf(regionId),
                String.valueOf(positive),
                String.valueOf(value),
                note != null ? note.replace(",", "\\,") : ""
        );
    }

    public static PCRTest fromString(String csvLine) {
        try {
            String[] parts = parseCSVLine(csvLine);
            if (parts.length >= 9) {
                LocalDateTime dt = parts[0].isEmpty() ? null : LocalDateTime.parse(parts[0]);
                String patientId = parts[1].isEmpty() ? null : parts[1].replace("\\,", ",");
                int testId = Integer.parseInt(parts[2]);
                int workplaceId = Integer.parseInt(parts[3]);
                int districtId = Integer.parseInt(parts[4]);
                int regionId = Integer.parseInt(parts[5]);
                boolean positive = Boolean.parseBoolean(parts[6]);
                double value = Double.parseDouble(parts[7]);
                String note = parts[8].isEmpty() ? null : parts[8].replace("\\,", ",");

                return new PCRTest(dt, patientId, testId, workplaceId, districtId, regionId, positive, value, note);
            }
        } catch (Exception e) {
            System.err.println("Error with parsing PCRTest: " + csvLine);
        }
        return null;
    }

    private static String[] parseCSVLine(String line) {
        List<String> result = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder field = new StringBuilder();

        for (char c : line.toCharArray()) {
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                result.add(field.toString());
                field.setLength(0);
            } else {
                field.append(c);
            }
        }
        result.add(field.toString());
        return result.toArray(new String[0]);
    }
}
