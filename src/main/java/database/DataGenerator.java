package database;

import data.Person;
import data.PCRTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Random;

public class DataGenerator {
    private static final String[] firstNames = {"Anna","Peter","Maria","Jozef","Eva","Michal","Katarina"};
    private static final String[] lastNames = {"Novak","Horak","Kral","Bielik","Farkaš","Kovac","Urban"};
    private static Random rand = new Random();

    public static Person generatePerson(int id) {
        String firstName = firstNames[rand.nextInt(firstNames.length)];
        String lastName = lastNames[rand.nextInt(lastNames.length)];
        LocalDate birthDate = LocalDate.of(1950 + rand.nextInt(70), 1 + rand.nextInt(12), 1 + rand.nextInt(28));
        String patientId = String.format("%06d", id);
        return new Person(firstName, lastName, birthDate, patientId);
    }

    public static PCRTest generatePCRTest(int testId, String patientId, int maxDistrict, int maxRegion, int maxWorkplace, LocalDateTime startDate, LocalDateTime endDate) {
        long startEpoch = startDate.toEpochSecond(java.time.ZoneOffset.UTC);
        long endEpoch = endDate.toEpochSecond(java.time.ZoneOffset.UTC);
        long randomEpoch = startEpoch + (long)(rand.nextDouble() * (endEpoch - startEpoch));
        LocalDateTime datetime = LocalDateTime.ofEpochSecond(randomEpoch, 0, java.time.ZoneOffset.UTC);

        int workplaceId = 1 + rand.nextInt(maxWorkplace);
        int districtId = 1 + rand.nextInt(maxDistrict);
        int regionId = 1 + rand.nextInt(maxRegion);
        boolean positive = rand.nextBoolean();
        double value = positive ? 20 + rand.nextDouble() * 80 : rand.nextDouble() * 20;
        String note = String.format("Generated test %d", testId);

        return new PCRTest(datetime, patientId, testId, workplaceId, districtId, regionId, positive, value, note);
    }

    public static void fillDatabase(Database db, int numPersons, int numTests, int maxDistrict, int maxRegion, int maxWorkplace, LocalDateTime startDate, LocalDateTime endDate) {
        int testIdCounter = 1;

        for (int i = 1; i <= numPersons; i++) {
            Person p = generatePerson(i);
            db.insertPerson(p);
        }

        for (int i = 0; i < numTests; i++) {
            String randomPatientId = String.format("%06d", 1 + rand.nextInt(numPersons));
            PCRTest t = generatePCRTest(testIdCounter++, randomPatientId, maxDistrict, maxRegion, maxWorkplace, startDate, endDate);
            db.insertPCRTest(t);
        }
    }
}