package data;

import avl.AVLTree;

import java.time.LocalDate;

public class Person implements Comparable<Person> {
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String patientId;
    private AVLTree<PCRTest> tests;

    public Person(String firstName, String lastName, LocalDate dateOfBirth, String patientId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.patientId = patientId;
        this.tests = new AVLTree<>();
    }

    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public String getPatientId() { return patientId; }
    public AVLTree<PCRTest> getTests() {
        return tests;
    }

    @Override
    public int compareTo(Person other) {
        return this.patientId.compareTo(other.patientId);
    }
}
