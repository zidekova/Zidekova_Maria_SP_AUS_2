package database;

import avl.AVLTree;
import data.PCRTest;
import data.Person;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static database.DataGenerator.generatePCRTest;
import static database.DataGenerator.generatePerson;

public class Database {
    private AVLTree<Person> personsByPatientId;
    private AVLTree<PCRTest> testsByTestId;
    private AVLTree<PCRTest> testsByDate;
    private AVLTree<PCRTest> positiveTestsByDate;
    private AVLTree<DistrictIndex> testsByDistrict;
    private AVLTree<RegionIndex> testsByRegion;

    public Database() {
        personsByPatientId = new AVLTree<>();
        testsByTestId = new AVLTree<>();
        testsByDate = new AVLTree<>();
        positiveTestsByDate = new AVLTree<>();
        testsByDistrict = new AVLTree<>();
        testsByRegion = new AVLTree<>();
    }

    public void fillDatabase(Database db, int numPersons, int numTests, int maxDistrict, int maxRegion, int maxWorkplace, LocalDateTime startDate, LocalDateTime endDate) {
        DataGenerator.fillDatabase(db, numPersons, numTests, maxDistrict, maxRegion, maxWorkplace, startDate, endDate);
    }

    private DistrictIndex getOrCreateDistrictIndex(int districtId) {
        DistrictIndex searchIndex = new DistrictIndex(districtId);
        DistrictIndex existing = testsByDistrict.search(searchIndex);
        if (existing == null) {
            existing = searchIndex;
            testsByDistrict.insert(existing);
        }
        return existing;
    }

    private RegionIndex getOrCreateRegionIndex(int regionId) {
        RegionIndex searchIndex = new RegionIndex(regionId);
        RegionIndex existing = testsByRegion.search(searchIndex);
        if (existing == null) {
            existing = searchIndex;
            testsByRegion.insert(existing);
        }
        return existing;
    }

    // 1 - insert the PCR test result into the system
    public boolean insertPCRTest(PCRTest test) {
        Person personSearch = new Person(null, null, null, test.getPatientId());
        Person person = personsByPatientId.search(personSearch);
        if (person == null) return false;

        if (testsByTestId.insert(test) == null) return false;
        testsByDate.insert(test);

        if (test.isPositive()) {
            positiveTestsByDate.insert(test);
        }

        DistrictIndex districtIndex = getOrCreateDistrictIndex(test.getDistrictId());
        districtIndex.tests.insert(test);
        if (test.isPositive()) {
            districtIndex.positiveTests.insert(test);
        }

        RegionIndex regionIndex = getOrCreateRegionIndex(test.getRegionId());
        regionIndex.tests.insert(test);
        if (test.isPositive()) {
            regionIndex.positiveTests.insert(test);
        }

        person.getTests().insert(test);

        return true;
    }

    // 2 - searching for the test result for the patient and displaying all data
    public PCRTest searchTestResultByTestId(String patientId, int testId) {
        PCRTest searchedPCR = new PCRTest(null, patientId, testId, 0, 0, 0, false, 0.0, null);
        return testsByTestId.search(searchedPCR);
    }

    // 3 - listing of all PCR tests performed for the given patient arranged by date and time
    public List<PCRTest> getAllTestsForPatient(String patientId) {
        List<PCRTest> result = new ArrayList<>();
        Person searchPerson = new Person(null, null, null, patientId);
        Person person = personsByPatientId.search(searchPerson);

        if (person != null) {
            for (PCRTest test : person.getTests().inorder()) {
                result.add(test);
            }
        }
        return result;
    }

    // 4 - listing of all positive tests performed for the specified time period for the specified district
    public List<PCRTest> getPositiveTestsForDistrict(int districtId, LocalDateTime from, LocalDateTime to) {
        List<PCRTest> result = new ArrayList<>();
        DistrictIndex searchIndex = new DistrictIndex(districtId);
        DistrictIndex districtIndex = testsByDistrict.search(searchIndex);

        if (districtIndex != null) {
            for (PCRTest test : districtIndex.positiveTests.inorder()) {
                if (!test.getDatetime().isBefore(from) && !test.getDatetime().isAfter(to)) {
                    result.add(test);
                }
            }
        }
        return result;
    }

    // 5 - listing of all tests performed for the specified time period for the specified district
    public List<PCRTest> getAllTestsForDistrict(int districtId, LocalDateTime from, LocalDateTime to) {
        List<PCRTest> result = new ArrayList<>();
        DistrictIndex searchIndex = new DistrictIndex(districtId);
        DistrictIndex districtIndex = testsByDistrict.search(searchIndex);

        if (districtIndex != null) {
            for (PCRTest test : districtIndex.tests.inorder()) {
                if (!test.getDatetime().isBefore(from) && !test.getDatetime().isAfter(to)) {
                    result.add(test);
                }
            }
        }
        return result;
    }

    // 6 - listing of all positive tests performed for the specified time period for the specified region
    public List<PCRTest> getPositiveTestsForRegion(int regionId, LocalDateTime from, LocalDateTime to) {
        List<PCRTest> result = new ArrayList<>();
        RegionIndex searchIndex = new RegionIndex(regionId);
        RegionIndex regionIndex = testsByRegion.search(searchIndex);

        if (regionIndex != null) {
            for (PCRTest test : regionIndex.positiveTests.inorder()) {
                if (!test.getDatetime().isBefore(from) && !test.getDatetime().isAfter(to)) {
                    result.add(test);
                }
            }
        }
        return result;
    }

    // 7 - listing of all tests performed for the specified time period for the specified region
    public List<PCRTest> getAllTestsForRegion(int regionId, LocalDateTime from, LocalDateTime to) {
        List<PCRTest> result = new ArrayList<>();
        RegionIndex searchIndex = new RegionIndex(regionId);
        RegionIndex regionIndex = testsByRegion.search(searchIndex);

        if (regionIndex != null) {
            for (PCRTest test : regionIndex.tests.inorder()) {
                if (!test.getDatetime().isBefore(from) && !test.getDatetime().isAfter(to)) {
                    result.add(test);
                }
            }
        }
        return result;
    }

    // 8 - listing of all positive tests performed for the specified time period
    public List<PCRTest> getAllPositiveTests(LocalDateTime from, LocalDateTime to) {
        List<PCRTest> result = new ArrayList<>();

        for (PCRTest test : positiveTestsByDate.inorder()) {
            if (!test.getDatetime().isBefore(from) && !test.getDatetime().isAfter(to)) {
                result.add(test);
            }
        }

        return result;
    }

    // 9 - listing of all tests performed for the specified time period
    public List<PCRTest> getAllTests(LocalDateTime from, LocalDateTime to) {
        List<PCRTest> result = new ArrayList<>();

        for (PCRTest test : testsByDate.inorder()) {
            if (!test.getDatetime().isBefore(from) && !test.getDatetime().isAfter(to)) {
                result.add(test);
            }
        }

        return result;
    }

    // 10 - list of sick people in the district on the specified date
    public List<Person> getSickPeopleInDistrict(int districtId, LocalDateTime date, int daysSick) {
        List<Person> result = new ArrayList<>();

        for (Person person : personsByPatientId.inorder()) {
            boolean isSick = false;

            for (PCRTest test : person.getTests().inorder()) {
                if (test.isPositive() && test.getDistrictId() == districtId && test.isPersonSick(test, date, daysSick)) {
                    isSick = true;
                    break;
                }
            }

            if (isSick) {
                result.add(person);
            }
        }

        return result;
    }

    // 11 - list of sick people in the district on the specified date sorted by value
    public List<Person> getSickPeopleInDistrictByValue(int districtId, LocalDateTime date, int daysSick) {
        List<Person> sickPeople = new ArrayList<>();

        for (Person person : personsByPatientId.inorder()) {
            for (PCRTest test : person.getTests().inorder()) {
                if (test.isPositive() && test.getDistrictId() == districtId && test.isPersonSick(test, date, daysSick)) {
                    sickPeople.add(person);
                    break;
                }
            }
        }

        sickPeople.sort((a, b) -> {
            double maxValueA = getMaxTestValueForPerson(a, districtId, date, daysSick);
            double maxValueB = getMaxTestValueForPerson(b, districtId, date, daysSick);
            return Double.compare(maxValueB, maxValueA);
        });

        return sickPeople;
    }

    private double getMaxTestValueForPerson(Person person, int districtId, LocalDateTime date, int daysSick) {
        double maxValue = -1;
        for (PCRTest test : person.getTests().inorder()) {
            if (test.isPositive() && test.getDistrictId() == districtId && test.isPersonSick(test, date, daysSick)) {
                if (test.getValue() > maxValue) {
                    maxValue = test.getValue();
                }
            }
        }
        return maxValue;
    }

    // 12 - list of sick people in the region on the specified date
    public List<Person> getSickPeopleInRegion(int regionId, LocalDateTime date, int daysSick) {
        List<Person> result = new ArrayList<>();

        for (Person person : personsByPatientId.inorder()) {
            boolean isSick = false;

            for (PCRTest test : person.getTests().inorder()) {
                if (test.isPositive() && test.getRegionId() == regionId && test.isPersonSick(test, date, daysSick)) {
                    isSick = true;
                    break;
                }
            }

            if (isSick) {
                result.add(person);
            }
        }

        return result;
    }

    // 13 - list of sick people on the specified date
    public List<Person> getSickPeople(LocalDateTime date, int daysSick) {
        List<Person> result = new ArrayList<>();

        for (Person person : personsByPatientId.inorder()) {
            boolean isSick = false;

            for (PCRTest test : person.getTests().inorder()) {
                if (test.isPositive() && test.isPersonSick(test, date, daysSick)) {
                    isSick = true;
                    break;
                }
            }

            if (isSick) {
                result.add(person);
            }
        }

        return result;
    }

    // 14 - list of one sick person on the specified date from each district with the highest test value
    public List<Person> getMaxValueSickPerDistrict(LocalDateTime date, int daysSick) {
        List<Person> result = new ArrayList<>();

        for (DistrictIndex district : testsByDistrict.inorder()) {
            Person maxPerson = null;
            double maxValue = -1;

            for (Person person : personsByPatientId.inorder()) {
                double personMaxValue = -1;

                for (PCRTest test : person.getTests().inorder()) {
                    if (test.isPositive() && test.getDistrictId() == district.districtId &&
                            test.isPersonSick(test, date, daysSick)) {
                        if (test.getValue() > personMaxValue) {
                            personMaxValue = test.getValue();
                        }
                    }
                }

                if (personMaxValue > maxValue) {
                    maxValue = personMaxValue;
                    maxPerson = person;
                }
            }

            if (maxPerson != null) {
                result.add(maxPerson);
            }
        }

        return result;
    }

    // 15 - list of districts sorted by the number of sick people on the specified date
    public List<Integer> getDistrictsBySickCount(LocalDateTime date, int daysSick) {
        List<DistrictSickCount> districtCounts = new ArrayList<>();

        for (DistrictIndex district : testsByDistrict.inorder()) {
            int sickCount = 0;

            for (Person person : personsByPatientId.inorder()) {
                boolean isPersonSickInDistrict = false;

                for (PCRTest test : person.getTests().inorder()) {
                    if (test.isPositive() && test.getDistrictId() == district.districtId &&
                            test.isPersonSick(test, date, daysSick)) {
                        isPersonSickInDistrict = true;
                        break;
                    }
                }

                if (isPersonSickInDistrict) {
                    sickCount++;
                }
            }

            if (sickCount > 0) {
                districtCounts.add(new DistrictSickCount(district.districtId, sickCount));
            }
        }

        districtCounts.sort((d1, d2) -> Integer.compare(d2.count, d1.count));

        List<Integer> result = new ArrayList<>();
        for (DistrictSickCount dsc : districtCounts) {
            result.add(dsc.districtId);
        }

        return result;
    }

    private static class DistrictSickCount {
        int districtId;
        int count;

        DistrictSickCount(int districtId, int count) {
            this.districtId = districtId;
            this.count = count;
        }
    }

    private static class RegionSickCount {
        int regionId;
        int count;

        RegionSickCount(int regionId, int count) {
            this.regionId = regionId;
            this.count = count;
        }
    }

    // 16 - list of regions sorted by the number of sick people on the specified date
    public List<Integer> getRegionsBySickCount(LocalDateTime date, int daysSick) {
        List<RegionSickCount> regionCounts = new ArrayList<>();

        for (RegionIndex region : testsByRegion.inorder()) {
            int sickCount = 0;

            for (Person person : personsByPatientId.inorder()) {
                boolean isPersonSickInRegion = false;

                for (PCRTest test : person.getTests().inorder()) {
                    if (test.isPositive() && test.getRegionId() == region.regionId &&
                            test.isPersonSick(test, date, daysSick)) {
                        isPersonSickInRegion = true;
                        break;
                    }
                }

                if (isPersonSickInRegion) {
                    sickCount++;
                }
            }

            if (sickCount > 0) {
                regionCounts.add(new RegionSickCount(region.regionId, sickCount));
            }
        }

        regionCounts.sort((r1, r2) -> Integer.compare(r2.count, r1.count));

        List<Integer> result = new ArrayList<>();
        for (RegionSickCount rsc : regionCounts) {
            result.add(rsc.regionId);
        }

        return result;
    }

    // 17 - list of all tests performed for a specified time period at a given workplace
    public List<PCRTest> getAllTestsForWorkplace(int workplaceId, LocalDateTime from, LocalDateTime to) {
        List<PCRTest> result = new ArrayList<>();

        for (PCRTest test : testsByDate.inorder()) {
            if (test.getWorkplaceId() == workplaceId &&
                    !test.getDatetime().isBefore(from) &&
                    !test.getDatetime().isAfter(to)) {
                result.add(test);
            }
        }

        return result;
    }

    // 18 - search for a PCR test by its code
    public PCRTest searchTestByTestId(int testId) {
        return testsByTestId.search(new PCRTest(null, null, testId, 0, 0, 0, false, 0.0, ""));
    }

    // 19 - insert a person into the system
    public boolean insertPerson(Person person) {
        return personsByPatientId.insert(person) != null;
    }

    // 20 - permanent and irreversible deletion of a PCR test result
    public boolean deletePCRTest(int testId) {
        PCRTest searchTest = new PCRTest(null, null, testId, 0, 0, 0, false, 0.0, "");
        PCRTest test = testsByTestId.search(searchTest);

        if (test == null) return false;

        // Odstránenie z hlavných stromov
        testsByTestId.delete(test);
        testsByDate.delete(test);
        if (test.isPositive()) {
            positiveTestsByDate.delete(test);
        }

        // Odstránenie z district indexu
        DistrictIndex districtIndex = getOrCreateDistrictIndex(test.getDistrictId());
        districtIndex.tests.delete(test);
        if (test.isPositive()) {
            districtIndex.positiveTests.delete(test);
        }

        // Odstránenie z region indexu
        RegionIndex regionIndex = getOrCreateRegionIndex(test.getRegionId());
        regionIndex.tests.delete(test);
        if (test.isPositive()) {
            regionIndex.positiveTests.delete(test);
        }

        // Odstránenie z osoby (person)
        Person personSearch = new Person(null, null, null, test.getPatientId());
        Person person = personsByPatientId.search(personSearch);
        if (person != null) {
            person.getTests().delete(test);
        }

        return true;
    }

    // 21 - deleting a person from the system including their PCR test results.
    public boolean deletePerson(String patientId) {
        Person searchPerson = new Person(null, null, null, patientId);
        Person person = personsByPatientId.search(searchPerson);
        if (person == null) return false;

        // Odstránenie všetkých testov osoby
        // Vytvoríme kópiu zoznamu testov, pretože nemôžeme mazať počas iterácie
        List<PCRTest> testsToDelete = new ArrayList<>();
        testsToDelete.addAll(person.getTests().inorder());

        // Odstránenie každého testu
        for (PCRTest test : testsToDelete) {
            deletePCRTest(test.getTestId());
        }

        // Odstránenie osoby
        personsByPatientId.delete(person);
        return true;
    }
}