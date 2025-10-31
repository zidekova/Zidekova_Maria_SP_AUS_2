package database;

import avl.AVLTree;
import data.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private AVLTree<Person> personsByPatientId;
    private AVLTree<PCRTest> testsByTestId;

    private AVLTree<PCRTestByDate> testsByDate;
    private AVLTree<PCRTestByDate> positiveTestsByDate;

    private AVLTree<DistrictById> testsByDistrict;
    private AVLTree<RegionById> testsByRegion;
    private AVLTree<WorkplaceById> testsByWorkplace;

    public Database() {
        personsByPatientId = new AVLTree<>();
        testsByTestId = new AVLTree<>();
        testsByDate = new AVLTree<>();
        positiveTestsByDate = new AVLTree<>();
        testsByDistrict = new AVLTree<>();
        testsByRegion = new AVLTree<>();
        testsByWorkplace = new AVLTree<>();
    }

    public void fillDatabase(Database db, int numPersons, int numTests, int maxDistrict, int maxRegion, int maxWorkplace, LocalDateTime startDate, LocalDateTime endDate) {
        DataGenerator.fillDatabase(db, numPersons, numTests, maxDistrict, maxRegion, maxWorkplace, startDate, endDate);
    }

    private DistrictById getOrCreateDistrictIndex(int districtId) {
        DistrictById searchIndex = new DistrictById(districtId);
        DistrictById existing = testsByDistrict.search(searchIndex);
        if (existing == null) {
            existing = searchIndex;
            testsByDistrict.insert(existing);
        }
        return existing;
    }

    private RegionById getOrCreateRegionIndex(int regionId) {
        RegionById searchIndex = new RegionById(regionId);
        RegionById existing = testsByRegion.search(searchIndex);
        if (existing == null) {
            existing = searchIndex;
            testsByRegion.insert(existing);
        }
        return existing;
    }

    private WorkplaceById getOrCreateWorkplaceIndex(int workplaceId) {
        WorkplaceById searchIndex = new WorkplaceById(workplaceId);
        WorkplaceById existing = testsByWorkplace.search(searchIndex);
        if (existing == null) {
            existing = searchIndex;
            testsByWorkplace.insert(existing);
        }
        return existing;
    }

    // 1 - insert the PCR test result into the system
    public boolean insertPCRTest(PCRTest test) {
        Person personSearch = new Person(null, null, null, test.getPatientId());
        Person person = personsByPatientId.search(personSearch);
        if (person == null) return false;

        if (testsByTestId.insert(test) == null) return false;

        testsByDate.insert(new PCRTestByDate(test));

        if (test.isPositive()) {
            positiveTestsByDate.insert(new PCRTestByDate(test));
        }

        DistrictById districtById = getOrCreateDistrictIndex(test.getDistrictId());
        districtById.getTestsByDate().insert(new PCRTestByDate(test));
        if (test.isPositive()) {
            districtById.getPositiveTestsByValue().insert(new PCRTestByValue(test));
        }

        RegionById regionById = getOrCreateRegionIndex(test.getRegionId());
        regionById.getTestsByDate().insert(new PCRTestByDate(test));

        WorkplaceById workplaceById = getOrCreateWorkplaceIndex(test.getWorkplaceId());
        workplaceById.getTestsByDate().insert(new PCRTestByDate(test));

        person.getTestsByDate().insert(new PCRTestByDate(test));

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
            for (PCRTestByDate tbd : person.getTestsByDate().inorder()) {
                result.add(tbd.getTest());
            }
        }
        return result;
    }

    // 4 - listing of all positive tests performed for the specified time period for the specified district
    public List<PCRTest> getPositiveTestsForDistrict(int districtId, LocalDateTime from, LocalDateTime to) {
        List<PCRTest> result = new ArrayList<>();
        DistrictById searchIndex = new DistrictById(districtId);
        DistrictById districtById = testsByDistrict.search(searchIndex);

        if (districtById != null) {
            PCRTest fromTest = new PCRTest(from, "", 0, 0, 0, 0, false, 0.0, "");
            PCRTest toTest = new PCRTest(to, "", 0, 0, 0, 0, false, 0.0, "");
            List<PCRTestByDate> found = districtById.getTestsByDate().intervalSearch(new PCRTestByDate(fromTest), new PCRTestByDate(toTest));

            for (PCRTestByDate tbd : found) {
                PCRTest test = tbd.getTest();
                if (test.isPositive()) {
                    result.add(test);
                }
            }
        }
        return result;
    }

    // 5 - listing of all tests performed for the specified time period for the specified district
    public List<PCRTest> getAllTestsForDistrict(int districtId, LocalDateTime from, LocalDateTime to) {
        List<PCRTest> result = new ArrayList<>();
        DistrictById searchIndex = new DistrictById(districtId);
        DistrictById districtById = testsByDistrict.search(searchIndex);

        if (districtById != null) {
            PCRTest fromTest = new PCRTest(from, "", 0, 0, 0, 0, false, 0.0, "");
            PCRTest toTest = new PCRTest(to, "", 0, 0, 0, 0, false, 0.0, "");
            List<PCRTestByDate> found = districtById.getTestsByDate().intervalSearch(new PCRTestByDate(fromTest), new PCRTestByDate(toTest));

            for (PCRTestByDate tbd : found) {
                PCRTest test = tbd.getTest();
                result.add(test);
            }
        }
        return result;
    }

    // 6 - listing of all positive tests performed for the specified time period for the specified region
    public List<PCRTest> getPositiveTestsForRegion(int regionId, LocalDateTime from, LocalDateTime to) {
        List<PCRTest> result = new ArrayList<>();
        RegionById searchIndex = new RegionById(regionId);
        RegionById regionById = testsByRegion.search(searchIndex);

        if (regionById != null) {
            PCRTest fromTest = new PCRTest(from, "", 0, 0, 0, 0, false, 0.0, "");
            PCRTest toTest = new PCRTest(to, "", 0, 0, 0, 0, false, 0.0, "");
            List<PCRTestByDate> found = regionById.getTestsByDate().intervalSearch(new PCRTestByDate(fromTest), new PCRTestByDate(toTest));

            for (PCRTestByDate tbd : found) {
                PCRTest test = tbd.getTest();
                if (test.isPositive()) {
                    result.add(test);
                }
            }
        }
        return result;
    }

    // 7 - listing of all tests performed for the specified time period for the specified region
    public List<PCRTest> getAllTestsForRegion(int regionId, LocalDateTime from, LocalDateTime to) {
        List<PCRTest> result = new ArrayList<>();
        RegionById searchIndex = new RegionById(regionId);
        RegionById regionById = testsByRegion.search(searchIndex);

        if (regionById != null) {
            PCRTest fromTest = new PCRTest(from, "", 0, 0, 0, 0, false, 0.0, "");
            PCRTest toTest = new PCRTest(to, "", 0, 0, 0, 0, false, 0.0, "");
            List<PCRTestByDate> found = regionById.getTestsByDate().intervalSearch(new PCRTestByDate(fromTest), new PCRTestByDate(toTest));

            for (PCRTestByDate tbd : found) {
                PCRTest test = tbd.getTest();
                result.add(test);
            }
        }
        return result;
    }

    // 8 - listing of all positive tests performed for the specified time period
    public List<PCRTest> getAllPositiveTests(LocalDateTime from, LocalDateTime to) {
        List<PCRTest> result = new ArrayList<>();
        PCRTest fromTest = new PCRTest(from, "", 0, 0, 0, 0, false, 0.0, "");
        PCRTest toTest = new PCRTest(to, "", 0, 0, 0, 0, false, 0.0, "");
        List<PCRTestByDate> found = positiveTestsByDate.intervalSearch(new PCRTestByDate(fromTest), new PCRTestByDate(toTest));

        for (PCRTestByDate tbd : found) {
            PCRTest test = tbd.getTest();
            result.add(test);
        }

        return result;
    }

    // 9 - listing of all tests performed for the specified time period
    public List<PCRTest> getAllTests(LocalDateTime from, LocalDateTime to) {
        List<PCRTest> result = new ArrayList<>();
        PCRTest fromTest = new PCRTest(from, "", 0, 0, 0, 0, false, 0.0, "");
        PCRTest toTest = new PCRTest(to, "", 0, 0, 0, 0, false, 0.0, "");
        List<PCRTestByDate> found = testsByDate.intervalSearch(new PCRTestByDate(fromTest), new PCRTestByDate(toTest));

        for (PCRTestByDate tbd : found) {
            PCRTest test = tbd.getTest();
            result.add(test);
        }

        return result;
    }

    // 10 - list of sick people in the district on the specified date
    public List<Person> getSickPeopleInDistrict(int districtId, LocalDateTime date, int daysSick) {
        List<Person> result = new ArrayList<>();

        DistrictById searchIndex = new DistrictById(districtId);
        DistrictById districtById = testsByDistrict.search(searchIndex);

        if (districtById == null) {
            return result;
        }

        LocalDateTime from = date.minusDays(daysSick);

        PCRTest fromTest = new PCRTest(from, "", 0, 0, 0, 0, false, 0.0, "");
        PCRTest toTest = new PCRTest(date, "", 0, 0, 0, 0, false, 0.0, "");

        List<PCRTestByDate> found = districtById.getTestsByDate().intervalSearch(new PCRTestByDate(fromTest), new PCRTestByDate(toTest));

        for (PCRTestByDate tbd : found) {
            PCRTest test = tbd.getTest();

            if (test.isPositive() && test.isPersonSick(test, date, daysSick)) {
                Person p = personsByPatientId.search(new Person(null, null, null, test.getPatientId()));

                if (p != null && !result.contains(p)) {
                    result.add(p);
                }
            }
        }

        return result;
    }

    // 11 - list of sick people in the district on the specified date sorted by value
    public List<Person> getSickPeopleInDistrictByValue(int districtId, LocalDateTime date, int daysSick) {
        List<Person> result = new ArrayList<>();

        DistrictById searchIndex = new DistrictById(districtId);
        DistrictById districtById = testsByDistrict.search(searchIndex);

        if (districtById == null) {
            return result;
        }

        List<PCRTestByValue> allPositiveTests = districtById.getPositiveTestsByValue().inorder();

        for (PCRTestByValue tbd : allPositiveTests) {
            PCRTest test = tbd.getTest();

            if (test.isPersonSick(test, date, daysSick)) {
                Person p = personsByPatientId.search(new Person(null, null, null, test.getPatientId()));

                if (p != null && !result.contains(p)) {
                    result.add(p);
                }
            }
        }

        return result;
    }


    // 12 - list of sick people in the region on the specified date
    public List<Person> getSickPeopleInRegion(int regionId, LocalDateTime date, int daysSick) {
        List<Person> result = new ArrayList<>();

        RegionById searchIndex = new RegionById(regionId);
        RegionById regionById = testsByRegion.search(searchIndex);

        if (regionById == null) {
            return result;
        }

        LocalDateTime from = date.minusDays(daysSick);

        PCRTest fromTest = new PCRTest(from, "", 0, 0, 0, 0, false, 0.0, "");
        PCRTest toTest = new PCRTest(date, "", 0, 0, 0, 0, false, 0.0, "");
        List<PCRTestByDate> found = regionById.getTestsByDate().intervalSearch(new PCRTestByDate(fromTest), new PCRTestByDate(toTest));

        for (PCRTestByDate tbd : found) {
            PCRTest test = tbd.getTest();

            if (test.isPositive() && test.isPersonSick(test, date, daysSick)) {
                Person p = personsByPatientId.search(new Person(null, null, null, test.getPatientId()));

                if (p != null && !result.contains(p)) {
                    result.add(p);
                }
            }
        }

        return result;
    }

    // 13 - list of sick people on the specified date
    public List<Person> getSickPeople(LocalDateTime date, int daysSick) {
        List<Person> result = new ArrayList<>();

        LocalDateTime from = date.minusDays(daysSick);

        PCRTest fromTest = new PCRTest(from, "", 0, 0, 0, 0, false, 0.0, "");
        PCRTest toTest = new PCRTest(date, "", 0, 0, 0, 0, false, 0.0, "");
        List<PCRTestByDate> found = positiveTestsByDate.intervalSearch(new PCRTestByDate(fromTest), new PCRTestByDate(toTest));

        for (PCRTestByDate tbd : found) {
            PCRTest test = tbd.getTest();

            if (test.isPersonSick(test, date, daysSick)) {
                Person p = personsByPatientId.search(new Person(null, null, null, test.getPatientId()));

                if (p != null && !result.contains(p)) {
                    result.add(p);
                }
            }
        }

        return result;
    }

    // 14 - list of one sick person on the specified date from each district with the highest test value
    public List<Person> getMaxValueSickPerDistrict(LocalDateTime date, int daysSick) {
        List<Person> result = new ArrayList<>();

        for (DistrictById district : testsByDistrict.inorder()) {
            if (district.getPositiveTestsByValue() == null || district.getPositiveTestsByValue().size() == 0) continue;

            PCRTestByValue maxValue = district.getPositiveTestsByValue().findMax();
            if (maxValue == null) continue;

            PCRTest maxTest = maxValue.getTest();

            if (maxTest.isPositive() && maxTest.isPersonSick(maxTest, date, daysSick)) {
                Person person = personsByPatientId.search(new Person(null, null, null, maxTest.getPatientId()));
                if (person != null && !result.contains(person)) {
                    result.add(person);
                }
            }
        }

        return result;
    }

    // 15 - list of districts sorted by the number of sick people on the specified date
    public List<DistrictBySickCount> getDistrictsBySickCount(LocalDateTime date, int daysSick) {
        AVLTree<DistrictBySickCount> sortedDistricts = new AVLTree<>();

        for (DistrictById district : testsByDistrict.inorder()) {
            int sickCount = getSickPeopleInDistrict(district.getDistrictId(), date, daysSick).size();
            if (sickCount > 0) {
                DistrictBySickCount districtCount = new DistrictBySickCount(district.getDistrictId(), sickCount);
                sortedDistricts.insert(districtCount);
            }
        }

        return sortedDistricts.inorder();
    }

    // 16 - list of regions sorted by the number of sick people on the specified date
    public List<RegionBySickCount> getRegionsBySickCount(LocalDateTime date, int daysSick) {
        AVLTree<RegionBySickCount> sortedRegions = new AVLTree<>();

        for (RegionById region : testsByRegion.inorder()) {
            int sickCount = getSickPeopleInRegion(region.getRegionId(), date, daysSick).size();
            if (sickCount > 0) {
                RegionBySickCount regionCount = new RegionBySickCount(region.getRegionId(), sickCount);
                sortedRegions.insert(regionCount);
            }
        }

        return sortedRegions.inorder();
    }

    // 17 - list of all tests performed for a specified time period at a given workplace
    public List<PCRTest> getAllTestsForWorkplace(int workplaceId, LocalDateTime from, LocalDateTime to) {
        List<PCRTest> result = new ArrayList<>();
        WorkplaceById searchIndex = new WorkplaceById(workplaceId);
        WorkplaceById workplaceById = testsByWorkplace.search(searchIndex);

        if (workplaceById != null) {
            PCRTest fromTest = new PCRTest(from, "", 0, 0, 0, 0, false, 0.0, "");
            PCRTest toTest = new PCRTest(to, "", 0, 0, 0, 0, false, 0.0, "");
            List<PCRTestByDate> found = workplaceById.getTestsByDate().intervalSearch(new PCRTestByDate(fromTest), new PCRTestByDate(toTest));

            for (PCRTestByDate tbd : found) {
                PCRTest test = tbd.getTest();
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

        testsByTestId.delete(test);
        testsByDate.delete(new PCRTestByDate(test));
        if (test.isPositive()) {
            positiveTestsByDate.delete(new PCRTestByDate(test));
        }

        DistrictById districtById = getOrCreateDistrictIndex(test.getDistrictId());
        districtById.getTestsByDate().delete(new PCRTestByDate(test));
        if (test.isPositive()) {
            districtById.getPositiveTestsByValue().delete(new PCRTestByValue(test));
        }

        RegionById regionById = getOrCreateRegionIndex(test.getRegionId());
        regionById.getTestsByDate().delete(new PCRTestByDate(test));

        WorkplaceById workplaceById = getOrCreateWorkplaceIndex(test.getWorkplaceId());
        workplaceById.getTestsByDate().delete(new PCRTestByDate(test));

        Person personSearch = new Person(null, null, null, test.getPatientId());
        Person person = personsByPatientId.search(personSearch);
        if (person != null) {
            person.getTestsByDate().delete(new PCRTestByDate(test));
        }

        return true;
    }

    // 21 - deleting a person from the system including their PCR test results
    public boolean deletePerson(String patientId) {
        Person searchPerson = new Person(null, null, null, patientId);
        Person person = personsByPatientId.search(searchPerson);
        if (person == null) return false;

        List<PCRTestByDate> testsToDelete = new ArrayList<>();
        testsToDelete.addAll(person.getTestsByDate().inorder());

        for (PCRTestByDate tbd : testsToDelete) {
            deletePCRTest(tbd.getTest().getTestId());
        }

        personsByPatientId.delete(person);
        return true;
    }

    // for displaying all data for person
    public Person searchPersonByPatientId(String patientId) {
        Person searchPerson = new Person(null, null, null, patientId);
        return personsByPatientId.search(searchPerson);
    }

    public void exportToCSV(String basePath) {
        try {
            Files.createDirectories(Paths.get(basePath));

            personsByPatientId.saveToCSV(basePath + "/persons.csv", true);
            testsByTestId.saveToCSV(basePath + "/tests.csv", true);
            testsByDistrict.saveToCSV(basePath + "/districts.csv", true);
            testsByRegion.saveToCSV(basePath + "/regions.csv", true);
            testsByWorkplace.saveToCSV(basePath + "/workplaces.csv", true);
            testsByDate.saveToCSV(basePath + "/tests_by_date.csv", true);
            positiveTestsByDate.saveToCSV(basePath + "/positive_tests_by_date.csv", true);

            exportDistrictTrees(basePath);
            exportRegionTrees(basePath);
            exportWorkplaceTrees(basePath);
            exportPersonTrees(basePath);

            System.out.println("Data exported successfully to: " + basePath);

        } catch (IOException e) {
            System.err.println("Error with export: " + e.getMessage());
        }
    }

    public void importFromCSV(String basePath) {
        try {
            this.clearDatabase();

            personsByPatientId.loadFromCSV(basePath + "/persons.csv", Person::fromString);
            testsByTestId.loadFromCSV(basePath + "/tests.csv", PCRTest::fromString);
            testsByDistrict.loadFromCSV(basePath + "/districts.csv", DistrictById::fromString);
            testsByRegion.loadFromCSV(basePath + "/regions.csv", RegionById::fromString);
            testsByWorkplace.loadFromCSV(basePath + "/workplaces.csv", WorkplaceById::fromString);
            testsByDate.loadFromCSV(basePath + "/tests_by_date.csv", PCRTestByDate::fromString);
            positiveTestsByDate.loadFromCSV(basePath + "/positive_tests_by_date.csv", PCRTestByDate::fromString);

            importDistrictTrees(basePath);
            importRegionTrees(basePath);
            importWorkplaceTrees(basePath);
            importPersonTrees(basePath);

            System.out.println("Data loaded successfully from: " + basePath);

        } catch (Exception e) {
            System.err.println("Error with import: " + e.getMessage());
        }
    }

    private void exportDistrictTrees(String basePath) throws IOException {
        String districtTestsByDatePath = basePath + "/district_tests_by_date";
        String districtPositiveTestsByValuePath = basePath + "/district_positive_tests_by_value";
        Files.createDirectories(Paths.get(districtTestsByDatePath));
        Files.createDirectories(Paths.get(districtPositiveTestsByValuePath));

        for (DistrictById district : testsByDistrict.inorder()) {
            String filenameBase = String.valueOf(district.getDistrictId());
            district.getTestsByDate().saveToCSV(districtTestsByDatePath + "/" + filenameBase + ".csv", true);
            district.getPositiveTestsByValue().saveToCSV(districtPositiveTestsByValuePath + "/" + filenameBase + ".csv", true);
        }
    }

    private void exportRegionTrees(String basePath) throws IOException {
        String regionTestsByDatePath = basePath + "/region_tests_by_date";
        Files.createDirectories(Paths.get(regionTestsByDatePath));

        for (RegionById region : testsByRegion.inorder()) {
            String filenameBase = String.valueOf(region.getRegionId());
            region.getTestsByDate().saveToCSV(regionTestsByDatePath + "/" + filenameBase + ".csv", true);
        }
    }

    private void exportWorkplaceTrees(String basePath) throws IOException {
        String workplaceTestsByDatePath = basePath + "/workplace_tests_by_date";
        Files.createDirectories(Paths.get(workplaceTestsByDatePath));

        for (WorkplaceById workplace : testsByWorkplace.inorder()) {
            String filenameBase = String.valueOf(workplace.getWorkplaceId());
            workplace.getTestsByDate().saveToCSV(workplaceTestsByDatePath + "/" + filenameBase + ".csv", true);
        }
    }

    private void exportPersonTrees(String basePath) throws IOException {
        String personTestsByDatePath = basePath + "/person_tests_by_date";
        Files.createDirectories(Paths.get(personTestsByDatePath));

        for (Person person : personsByPatientId.inorder()) {
            person.getTestsByDate().saveToCSV(personTestsByDatePath + "/" + person.getPatientId() + ".csv", true);
        }
    }

    private void importDistrictTrees(String basePath) {
        String districtTestsByDatePath = basePath + "/district_tests_by_date";
        String districtPositiveTestsByValuePath = basePath + "/district_positive_tests_by_value";

        for (DistrictById district : testsByDistrict.inorder()) {
            String filenameBase = String.valueOf(district.getDistrictId());
            district.getTestsByDate().loadFromCSV(districtTestsByDatePath + "/" + filenameBase + ".csv", PCRTestByDate::fromString);
            district.getPositiveTestsByValue().loadFromCSV(districtPositiveTestsByValuePath + "/" + filenameBase + ".csv", PCRTestByValue::fromString);
        }
    }

    private void importRegionTrees(String basePath) {
        String regionTestsByDatePath = basePath + "/region_tests_by_date";

        for (RegionById region : testsByRegion.inorder()) {
            String filenameBase = String.valueOf(region.getRegionId());
            region.getTestsByDate().loadFromCSV(regionTestsByDatePath + "/" + filenameBase + ".csv", PCRTestByDate::fromString);
        }
    }

    private void importWorkplaceTrees(String basePath) {
        String workplaceTestsByDatePath = basePath + "/workplace_tests_by_date";

        for (WorkplaceById workplace : testsByWorkplace.inorder()) {
            String filenameBase = String.valueOf(workplace.getWorkplaceId());
            workplace.getTestsByDate().loadFromCSV(workplaceTestsByDatePath + "/" + filenameBase + ".csv", PCRTestByDate::fromString);
        }
    }

    private void importPersonTrees(String basePath) {
        String personTestsByDatePath = basePath + "/person_tests_by_date";

        for (Person person : personsByPatientId.inorder()) {
            person.getTestsByDate().loadFromCSV(personTestsByDatePath + "/" + person.getPatientId() + ".csv", PCRTestByDate::fromString);
        }
    }

    private void clearDatabase() {
        personsByPatientId = new AVLTree<>();
        testsByTestId = new AVLTree<>();
        testsByDate = new AVLTree<>();
        positiveTestsByDate = new AVLTree<>();
        testsByDistrict = new AVLTree<>();
        testsByRegion = new AVLTree<>();
        testsByWorkplace = new AVLTree<>();
    }
}