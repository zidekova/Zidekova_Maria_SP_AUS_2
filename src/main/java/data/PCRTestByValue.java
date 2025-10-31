package data;

public class PCRTestByValue implements Comparable<PCRTestByValue> {
    private PCRTest test;
    public PCRTestByValue(PCRTest t) { this.test = t; }

    public PCRTest getTest() {
        return test;
    }

    @Override
    public int compareTo(PCRTestByValue other) {
        if (this.test == null && other.test == null) return 0;
        if (this.test == null) return -1;
        if (other.test == null) return 1;
        return this.test.compareByValue(other.test);
    }

    @Override
    public String toString() { return test.toString(); }

    public static PCRTestByValue fromString(String line) {
        PCRTest t = PCRTest.fromString(line);
        return (t == null) ? null : new PCRTestByValue(t);
    }
}
