// --- Person.java (potrebné upraviť aby testy boli AVL<TreeByDate>) ---
package data;

import avl.AVLTree;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Person implements Comparable<Person> {
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String patientId;
    private AVLTree<PCRTestByDate> testsByDate;

    public Person(String firstName, String lastName, LocalDate dateOfBirth, String patientId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.patientId = patientId;
        this.testsByDate = new AVLTree<>();
    }

    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public String getPatientId() { return patientId; }
    public AVLTree<PCRTestByDate> getTestsByDate() { return testsByDate; }

    @Override
    public int compareTo(Person other) {
        return this.patientId.compareTo(other.patientId);
    }

    @Override
    public String toString() {
        return String.join(",",
                firstName != null ? firstName.replace(",", "\\,") : "",
                lastName != null ? lastName.replace(",", "\\,") : "",
                dateOfBirth != null ? dateOfBirth.toString() : "",
                patientId != null ? patientId.replace(",", "\\,") : ""
        );
    }

    public static Person fromString(String csvLine) {
        try {
            String[] parts = parseCSVLine(csvLine);
            if (parts.length >= 4) {
                String firstName = parts[0].isEmpty() ? null : parts[0].replace("\\,", ",");
                String lastName = parts[1].isEmpty() ? null : parts[1].replace("\\,", ",");
                java.time.LocalDate dob = parts[2].isEmpty() ? null : java.time.LocalDate.parse(parts[2]);
                String patientId = parts[3].isEmpty() ? null : parts[3].replace("\\,", ",");
                return new Person(firstName, lastName, dob, patientId);
            }
        } catch (Exception e) {
            System.err.println("Error with parsing Person: " + csvLine);
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