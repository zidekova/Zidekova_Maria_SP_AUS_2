package data;

public class PCRTestByDate implements Comparable<PCRTestByDate> {
    private PCRTest test;
    public PCRTestByDate(PCRTest t) { this.test = t; }

    public PCRTest getTest() {
        return test;
    }

    @Override
    public int compareTo(PCRTestByDate other) {
        if (this.test == null && other.test == null) return 0;
        if (this.test == null) return -1;
        if (other.test == null) return 1;
        return this.test.compareByDate(other.test);
    }

    @Override
    public String toString() { return test.toString(); }

    public static PCRTestByDate fromString(String line) {
        PCRTest t = PCRTest.fromString(line);
        return (t == null) ? null : new PCRTestByDate(t);
    }
}
