package gui;

import data.DistrictBySickCount;
import data.RegionBySickCount;
import database.Database;
import data.PCRTest;
import data.Person;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

import static javax.swing.JOptionPane.showMessageDialog;

public class DatabaseGUI extends JFrame {

    private final Database db;
    private final JTextArea output;

    public DatabaseGUI(Database db) {
        super("PCR Test Database");
        this.db = db;
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        output = new JTextArea();
        output.setEditable(false);
        output.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        JScrollPane sp = new JScrollPane(output);
        sp.setBorder(BorderFactory.createTitledBorder("Výstup"));

        JPanel buttonPanel = createButtonPanel();

        mainPanel.add(sp, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createButtonPanel() {
        JPanel mainButtonPanel = new JPanel();
        mainButtonPanel.setLayout(new BoxLayout(mainButtonPanel, BoxLayout.Y_AXIS));

        // basic operations 
        JPanel basicPanel = createVerticalButtonPanel("Základné operácie", new String[][] {
                {"1) Vložiť test", "actionInsertTest"},
                {"2) Nájsť test pacienta", "actionFindTestForPatient"},
                {"3) Testy pacienta", "actionAllTestsForPatient"},
                {"18) Hľadať test podľa ID", "actionSearchById"}
        });

        // district operations
        JPanel districtPanel = createVerticalButtonPanel("Okresy", new String[][] {
                {"4) Pozitívne testy", "actionPositiveDistrict"},
                {"5) Všetky testy", "actionAllDistrict"},
                {"10) Chorí (k dátumu)", "actionSickDistrict"},
                {"11) Chorí podľa hodnoty", "actionSickDistrictByValue"}
        });

        // region operations
        JPanel regionPanel = createVerticalButtonPanel("Kraje", new String[][] {
                {"6) Pozitívne testy", "actionPositiveRegion"},
                {"7) Všetky testy", "actionAllRegion"},
                {"12) Chorí v kraji", "actionSickRegion"}
        });

        // global operations
        JPanel globalPanel = createVerticalButtonPanel("Globálne", new String[][] {
                {"8) Všetky pozitívne", "actionListAllPositiveTests"},
                {"9) Všetky testy", "actionListAllTests"},
                {"13) Chorí globálne", "actionSickGlobal"}
        });

        // statistics
        JPanel statsPanel = createVerticalButtonPanel("Štatistiky", new String[][] {
                {"14) Max hodnoty okresov", "actionMaxPerDistrict"},
                {"15) Okresy podľa chorých", "actionDistrictsByCount"},
                {"16) Kraje podľa chorých", "actionRegionsByCount"}
        });

        // management
        JPanel managementPanel = createVerticalButtonPanel("Manažment", new String[][] {
                {"17) Testy podľa pracoviska", "actionWorkplaceInterval"},
                {"19) Vložiť osobu", "actionInsertPersonDetail"},
                {"20) Vymazať test", "actionDeleteTest"},
                {"21) Vymazať osobu", "actionDeletePerson"},
                {"Generovať", "actionGenerateAsync"},
                {"Export CSV", "actionExportCSV"},
                {"Import CSV", "actionImportCSV"}
        });

        JPanel horizontalPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        horizontalPanel.add(basicPanel);
        horizontalPanel.add(districtPanel);
        horizontalPanel.add(regionPanel);
        horizontalPanel.add(globalPanel);
        horizontalPanel.add(statsPanel);
        horizontalPanel.add(managementPanel);

        mainButtonPanel.add(horizontalPanel);
        return mainButtonPanel;
    }

    private JPanel createVerticalButtonPanel(String title, String[][] buttonData) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder(title));

        for (String[] buttonInfo : buttonData) {
            String text = buttonInfo[0];
            String action = buttonInfo[1];

            JButton button = new JButton(text);
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            button.setMaximumSize(new Dimension(180, 30));
            button.setPreferredSize(new Dimension(180, 30));

            switch (action) {
                case "actionInsertTest": button.addActionListener(this::actionInsertTest); break;
                case "actionFindTestForPatient": button.addActionListener(this::actionFindTestForPatient); break;
                case "actionAllTestsForPatient": button.addActionListener(this::actionAllTestsForPatient); break;
                case "actionSearchById": button.addActionListener(this::actionSearchById); break;
                case "actionPositiveDistrict": button.addActionListener(this::actionPositiveDistrict); break;
                case "actionAllDistrict": button.addActionListener(this::actionAllDistrict); break;
                case "actionSickDistrict": button.addActionListener(this::actionSickDistrict); break;
                case "actionSickDistrictByValue": button.addActionListener(this::actionSickDistrictByValue); break;
                case "actionPositiveRegion": button.addActionListener(this::actionPositiveRegion); break;
                case "actionAllRegion": button.addActionListener(this::actionAllRegion); break;
                case "actionSickRegion": button.addActionListener(this::actionSickRegion); break;
                case "actionListAllPositiveTests": button.addActionListener(this::actionListAllPositiveTests); break;
                case "actionListAllTests": button.addActionListener(this::actionListAllTests); break;
                case "actionSickGlobal": button.addActionListener(this::actionSickGlobal); break;
                case "actionMaxPerDistrict": button.addActionListener(this::actionMaxPerDistrict); break;
                case "actionDistrictsByCount": button.addActionListener(this::actionDistrictsByCount); break;
                case "actionRegionsByCount": button.addActionListener(this::actionRegionsByCount); break;
                case "actionWorkplaceInterval": button.addActionListener(this::actionWorkplaceInterval); break;
                case "actionInsertPersonDetail": button.addActionListener(this::actionInsertPersonDetail); break;
                case "actionDeleteTest": button.addActionListener(this::actionDeleteTest); break;
                case "actionDeletePerson": button.addActionListener(this::actionDeletePerson); break;
                case "actionGenerateAsync": button.addActionListener(this::actionGenerateAsync); break;
                case "actionExportCSV": button.addActionListener(this::actionExportCSV); break;
                case "actionImportCSV": button.addActionListener(this::actionImportCSV); break;
            }

            panel.add(button);
            panel.add(Box.createRigidArea(new Dimension(0, 5)));
        }

        return panel;
    }

    private void showError(String msg) {
        showMessageDialog(this, msg, "Chyba", JOptionPane.ERROR_MESSAGE);
    }

    private String showInputDialog(String message, String title) {
        return JOptionPane.showInputDialog(this, message, title, JOptionPane.QUESTION_MESSAGE);
    }

    private int showIntInputDialog(String message, String title) {
        String input = showInputDialog(message, title);
        if (input == null) return Integer.MIN_VALUE;
        try {
            return Integer.parseInt(input.trim());
        } catch (NumberFormatException ex) {
            showError("Neplatné číslo: " + input);
            return Integer.MIN_VALUE;
        }
    }

    private LocalDateTime[] showIntervalInputDialog() {
        String from = showInputDialog("Začiatok obdobia (YYYY-MM-DD):", "Dátum od");
        if (from == null) return null;

        String to = showInputDialog("Koniec obdobia (YYYY-MM-DD):", "Dátum do");
        if (to == null) return null;

        try {
            LocalDate f = LocalDate.parse(from.trim());
            LocalDate t = LocalDate.parse(to.trim());
            return new LocalDateTime[]{f.atStartOfDay(), t.atTime(23,59,59)};
        } catch (DateTimeParseException ex) {
            showError("Neplatný formát dátumu. Použi YYYY-MM-DD.");
            return null;
        }
    }

    private LocalDateTime showDateTimeInputDialog() {
        String dateStr = showInputDialog("Zadajte dátum (YYYY-MM-DD):", "Dátum");
        if (dateStr == null || dateStr.trim().isEmpty()) {
            showError("Zadajte dátum");
            return null;
        }

        try {
            LocalDate date = LocalDate.parse(dateStr.trim());
            return date.atStartOfDay();
        } catch (Exception e) {
            showError("Neplatný formát dátumu. Použite formát YYYY-MM-DD.");
            return null;
        }
    }

    // 1) insert PCR test
    private void actionInsertTest(ActionEvent ev) {
        String pid = showInputDialog("ID pacienta:", "ID pacienta");
        if (pid == null || pid.trim().isEmpty()) {
            showError("Zadaj ID pacienta");
            return;
        }

        int testId = showIntInputDialog("ID testu:", "ID testu");
        if (testId == Integer.MIN_VALUE) return;

        int workplace = showIntInputDialog("ID pracoviska:", "ID pracoviska");
        if (workplace == Integer.MIN_VALUE) workplace = 1;

        int district = showIntInputDialog("ID okresu:", "ID okresu");
        if (district == Integer.MIN_VALUE) district = 1;

        int region = showIntInputDialog("ID kraja:", "ID kraja");
        if (region == Integer.MIN_VALUE) region = 1;

        int opt = JOptionPane.showConfirmDialog(
                this,
                "Je test pozitívny?",
                "Výsledok testu",
                JOptionPane.YES_NO_CANCEL_OPTION
        );
        if (opt == JOptionPane.CANCEL_OPTION || opt == JOptionPane.CLOSED_OPTION) {
            appendAndScroll("Vloženie testu bolo zrušené.");
            return;
        }

        boolean positive = (opt == JOptionPane.YES_OPTION);

        String sval = showInputDialog("Hodnota testu:", "Hodnota");
        if (sval == null) {
            appendAndScroll("Vloženie testu bolo zrušené.");
            return;
        }

        double value;
        try {
            value = Double.parseDouble(sval);
        } catch (NumberFormatException ex) {
            showError("Neplatná hodnota testu.");
            return;
        }

        String note = showInputDialog("Poznámka:", "Poznámka");
        if (note == null) {
            appendAndScroll("Vloženie testu bolo zrušené.");
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        PCRTest t = new PCRTest(now, pid.trim(), testId, workplace, district, region, positive, value, note);
        boolean ok = db.insertPCRTest(t);
        appendAndScroll(ok ? "Test vložený: " + testId : "Osoba neexistuje alebo test s týmto ID už existuje: " + testId);
    }

    // 2) search for the test result of a patient
    private void actionFindTestForPatient(ActionEvent ev) {
        String pid = showInputDialog("ID pacienta:", "ID pacienta");
        if (pid == null || pid.trim().isEmpty()) {
            showError("Zadaj ID pacienta");
            return;
        }

        int testId = showIntInputDialog("ID testu:", "ID testu");
        if (testId == Integer.MIN_VALUE) return;

        Person person = db.searchPersonByPatientId(pid.trim());

        PCRTest t = db.searchTestResultByTestId(pid.trim(), testId);

        if (t == null) {
            appendAndScroll("Test nenájdený pre pacienta: " + pid + ", ID testu = " + testId);
        } else {
            displayTests(List.of(t), "Test " + testId + ": " + formatPersonLine(person));
        }
    }

    // 3) list of all test for a patient sorted by a date
    private void actionAllTestsForPatient(ActionEvent ev) {
        String pid = showInputDialog("ID pacienta:", "ID pacienta");
        if (pid == null || pid.trim().isEmpty()) {
            showError("Zadaj ID pacienta");
            return;
        }

        Person person = db.searchPersonByPatientId(pid.trim());

        List<PCRTest> tests = db.getAllTestsForPatient(pid.trim());

        displayTests(tests, "Všetky testy pacienta: " + formatPersonLine(person));
    }

    // 17) tests for workplace in interval
    private void actionWorkplaceInterval(ActionEvent ev) {
        LocalDateTime[] interval = showIntervalInputDialog();
        if (interval == null) return;

        int workplace = showIntInputDialog("ID pracoviska:", "ID pracoviska");
        if (workplace == Integer.MIN_VALUE) return;

        List<PCRTest> res = db.getAllTestsForWorkplace(workplace, interval[0], interval[1]);
        displayTests(res, "Pracovisko " + workplace);
    }

    // 18) search by test id
    private void actionSearchById(ActionEvent ev) {
        int testId = showIntInputDialog("ID testu:", "ID testu");
        if (testId == Integer.MIN_VALUE) return;

        PCRTest t = db.searchTestByTestId(testId);
        output.setText("");
        if (t == null) appendAndScroll("Test nenájdený: " + testId);
        else displayTests(List.of(t), "Test nájdený");
    }

    // 9) all tests 
    private void actionListAllTests(ActionEvent ev) {
        LocalDateTime[] interval = showIntervalInputDialog();
        if (interval == null) return;

        List<PCRTest> res = db.getAllTests(interval[0], interval[1]);
        displayTests(res, "Všetky testy");
    }

    // 8) all positive
    private void actionListAllPositiveTests(ActionEvent ev) {
        LocalDateTime[] interval = showIntervalInputDialog();
        if (interval == null) return;

        List<PCRTest> res = db.getAllPositiveTests(interval[0], interval[1]);
        displayTests(res, "Všetky pozitívne testy");
    }

    // 4) positive tests by district interval
    private void actionPositiveDistrict(ActionEvent ev) {
        LocalDateTime[] interval = showIntervalInputDialog();
        if (interval == null) return;

        int district = showIntInputDialog("ID okresu:", "ID okresu");
        if (district == Integer.MIN_VALUE) return;

        List<PCRTest> res = db.getPositiveTestsForDistrict(district, interval[0], interval[1]);
        displayTests(res, "Pozitívne testy v okrese " + district);
    }

    // 5) all tests by district interval
    private void actionAllDistrict(ActionEvent ev) {
        LocalDateTime[] interval = showIntervalInputDialog();
        if (interval == null) return;

        int district = showIntInputDialog("ID okresu:", "ID okresu");
        if (district == Integer.MIN_VALUE) return;

        List<PCRTest> res = db.getAllTestsForDistrict(district, interval[0], interval[1]);
        displayTests(res, "Všetky testy v okrese " + district);
    }

    // 6) positive tests by region interval
    private void actionPositiveRegion(ActionEvent ev) {
        LocalDateTime[] interval = showIntervalInputDialog();
        if (interval == null) return;

        int region = showIntInputDialog("ID kraja:", "ID kraja");
        if (region == Integer.MIN_VALUE) return;

        List<PCRTest> res = db.getPositiveTestsForRegion(region, interval[0], interval[1]);
        displayTests(res, "Pozitívne testy v kraji " + region);
    }

    // 7) all tests by region interval
    private void actionAllRegion(ActionEvent ev) {
        LocalDateTime[] interval = showIntervalInputDialog();
        if (interval == null) return;

        int region = showIntInputDialog("ID kraja:", "ID kraja");
        if (region == Integer.MIN_VALUE) return;

        List<PCRTest> res = db.getAllTestsForRegion(region, interval[0], interval[1]);
        displayTests(res, "Všetky testy v kraji " + region);
    }

    // 10) sick people in district
    private void actionSickDistrict(ActionEvent ev) {
        int district = showIntInputDialog("ID okresu:", "ID okresu");
        if (district == Integer.MIN_VALUE) return;

        LocalDateTime date = showDateTimeInputDialog();
        if (date == null) return;

        int days = showIntInputDialog("Počet dní:", "Dni");
        if (days == Integer.MIN_VALUE) return;

        List<Person> res = db.getSickPeopleInDistrict(district, date, days);
        displayPeople(res, "Chorí ľudia v okrese " + district + " k dátumu " + date.toLocalDate() + " (posledných " + days + " dní)");
    }

    // 11) sick people in district by value
    private void actionSickDistrictByValue(ActionEvent ev) {
        int district = showIntInputDialog("ID okresu:", "ID okresu");
        if (district == Integer.MIN_VALUE) return;

        LocalDateTime date = showDateTimeInputDialog();
        if (date == null) return;

        int days = showIntInputDialog("Počet dní:", "Dni");
        if (days == Integer.MIN_VALUE) return;

        List<Person> res = db.getSickPeopleInDistrictByValue(district, date, days);
        displayPeople(res, String.format("Chorí ľudia v okrese %d usporiadaní podľa hodnoty testu k dátumu %s", district, date.toLocalDate()));
    }

    // 12) sick people in region
    private void actionSickRegion(ActionEvent ev) {
        int region = showIntInputDialog("ID kraja:", "ID kraja");
        if (region == Integer.MIN_VALUE) return;

        LocalDateTime date = showDateTimeInputDialog();
        if (date == null) return;

        int days = showIntInputDialog("Počet dní:", "Dni");
        if (days == Integer.MIN_VALUE) return;

        List<Person> res = db.getSickPeopleInRegion(region, date, days);
        displayPeople(res, "Chorí ľudia v kraji " + region + " k dátumu " + date.toLocalDate());
    }

    // 13) sick people global
    private void actionSickGlobal(ActionEvent ev) {
        LocalDateTime date = showDateTimeInputDialog();
        if (date == null) return;

        int days = showIntInputDialog("Počet dní:", "Dni");
        if (days == Integer.MIN_VALUE) return;

        List<Person> res = db.getSickPeople(date, days);
        displayPeople(res, "Chorí ľudia (globálne) k dátumu " + date.toLocalDate());
    }

    // 14) max value per district
    private void actionMaxPerDistrict(ActionEvent ev) {
        LocalDateTime date = showDateTimeInputDialog();
        if (date == null) return;

        int days = showIntInputDialog("Počet dní:", "Dni");
        if (days == Integer.MIN_VALUE) return;

        List<Person> res = db.getMaxValueSickPerDistrict(date, days);
        displayPeople(res, "Osoby s najvyššou hodnotou testu v každom okrese k dátumu " + date.toLocalDate());
    }

    // 15) districts by sick count
    private void actionDistrictsByCount(ActionEvent ev) {
        LocalDateTime date = showDateTimeInputDialog();
        if (date == null) return;

        int days = showIntInputDialog("Počet dní:", "Dni");
        if (days == Integer.MIN_VALUE) return;

        List<DistrictBySickCount> res = db.getDistrictsBySickCount(date, days);
        output.setText("");
        appendAndScroll("Okresy usporiadané podľa počtu chorých (zostupne) k dátumu " + date.toLocalDate() + ":");
        for (DistrictBySickCount d : res) appendAndScroll(String.format("Okres %d - %d chorých", d.getDistrictId(), d.getSickCount()));
    }

    // 16) regions by sick count
    private void actionRegionsByCount(ActionEvent ev) {
        LocalDateTime date = showDateTimeInputDialog();
        if (date == null) return;

        int days = showIntInputDialog("Počet dní:", "Dni");
        if (days == Integer.MIN_VALUE) return;

        List<RegionBySickCount> res = db.getRegionsBySickCount(date, days);
        output.setText("");
        appendAndScroll("Kraje usporiadané podľa počtu chorých (zostupne) k dátumu " + date.toLocalDate() + ":");
        for (RegionBySickCount r : res) appendAndScroll(String.format("Kraj %d - %d chorých", r.getRegionId(), r.getSickCount()));
    }

    // 19) insert person detail (dialog)
    private void actionInsertPersonDetail(ActionEvent ev) {
        JTextField first = new JTextField();
        JTextField last = new JTextField();
        JTextField dob = new JTextField();
        JTextField pid = new JTextField();
        Object[] fields = {
                "Krstné mesto:", first,
                "Priezvisko:", last,
                "Dátum narodenia (YYYY-MM-DD):", dob,
                "ID pacienta:", pid
        };
        int res = JOptionPane.showConfirmDialog(this, fields, "Vložiť osobu", JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.OK_OPTION) {
            try {
                LocalDate d = LocalDate.parse(dob.getText().trim());
                Person p = new Person(first.getText().trim(), last.getText().trim(), d, pid.getText().trim());
                boolean ok = db.insertPerson(p);
                appendAndScroll(ok ? "Osoba vložená: " + pid.getText().trim() : "Osoba už existuje: " + pid.getText().trim());
            } catch (Exception ex) {
                showError("Chybné údaje.");
            }
        }
    }

    // 20) delete test by id
    private void actionDeleteTest(ActionEvent ev) {
        int tid = showIntInputDialog("ID testu:", "Vymazať test");
        if (tid == Integer.MIN_VALUE) return;

        boolean ok = db.deletePCRTest(tid);
        appendAndScroll(ok ? "Test vymazaný: " + tid : "Test neexistuje: " + tid);
    }

    // 21) delete person and all tests
    private void actionDeletePerson(ActionEvent ev) {
        String pid = showInputDialog("ID pacienta:", "Vymazať osobu");
        if (pid == null || pid.trim().isEmpty()) {
            showError("Zadaj ID pacienta");
            return;
        }

        boolean ok = db.deletePerson(pid.trim());
        appendAndScroll(ok ? "Osoba a jej testy vymazané: " + pid : "Osoba neexistuje: " + pid);
    }

    // generator
    private void actionGenerateAsync(ActionEvent ev) {
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));

        JTextField personsField = new JTextField("100");
        JTextField testsField = new JTextField("500");
        JTextField districtsField = new JTextField("10");
        JTextField regionsField = new JTextField("5");
        JTextField workplacesField = new JTextField("20");
        JTextField startDateField = new JTextField("2024-01-01");
        JTextField endDateField = new JTextField("2025-12-31");

        panel.add(new JLabel("Počet osôb:"));
        panel.add(personsField);
        panel.add(new JLabel("Počet testov:"));
        panel.add(testsField);
        panel.add(new JLabel("Počet okresov:"));
        panel.add(districtsField);
        panel.add(new JLabel("Počet krajov:"));
        panel.add(regionsField);
        panel.add(new JLabel("Počet pracovísk:"));
        panel.add(workplacesField);
        panel.add(new JLabel("Začiatok (yyyy-MM-dd):"));
        panel.add(startDateField);
        panel.add(new JLabel("Koniec (yyyy-MM-dd):"));
        panel.add(endDateField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Generovať dáta",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        try {
            int persons = Integer.parseInt(personsField.getText().trim());
            int numTests = Integer.parseInt(testsField.getText().trim());
            int maxDistrict = Integer.parseInt(districtsField.getText().trim());
            int maxRegion = Integer.parseInt(regionsField.getText().trim());
            int maxWorkplace = Integer.parseInt(workplacesField.getText().trim());
            LocalDateTime startDate = LocalDate.parse(startDateField.getText().trim()).atStartOfDay();
            LocalDateTime endDate = LocalDate.parse(endDateField.getText().trim()).atStartOfDay();

            if (startDate.isAfter(endDate)) {
                showMessageDialog(this,
                        "Začiatok musí byť pred koncom!",
                        "Chyba",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (persons <= 0 || numTests <= 0 || maxDistrict <= 0 || maxRegion <= 0 || maxWorkplace <= 0) {
                showMessageDialog(this,
                        "Všetky číselné hodnoty musia byť väčšie ako 0!",
                        "Chyba",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            output.setText("");
            appendAndScroll("Spúšťam generovanie " + persons + " osôb, " + numTests + " testov...");
            appendAndScroll("Okresy: " + maxDistrict + ", Kraje: " + maxRegion + ", Pracoviská: " + maxWorkplace);
            appendAndScroll("Obdobie: " + startDate.toLocalDate() + " - " + endDate.toLocalDate());

            SwingWorker<Void, String> worker = new SwingWorker<>() {
                @Override
                protected Void doInBackground() {
                    db.fillDatabase(db, persons, numTests, maxDistrict, maxRegion, maxWorkplace, startDate, endDate);
                    return null;
                }

                @Override
                protected void done() {
                    appendAndScroll("Generovanie dokončené.");
                }
            };
            worker.execute();

        } catch (NumberFormatException e) {
            showMessageDialog(this,
                    "Neplatný formát čísla! Zadajte platné celé číslo.",
                    "Chyba",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            showMessageDialog(this,
                    "Neplatný formát dátumu! Použite formát yyyy-MM-dd.",
                    "Chyba",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actionExportCSV(ActionEvent ev) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setDialogTitle("Vyberte priečinok pre export");

        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedDir = fileChooser.getSelectedFile();
            db.exportToCSV(selectedDir.getAbsolutePath());
            appendAndScroll("Export dokončený do: " + selectedDir.getAbsolutePath());
        }
    }

    private void actionImportCSV(ActionEvent ev) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setDialogTitle("Vyberte priečinok s CSV súbormi");

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedDir = fileChooser.getSelectedFile();
            db.importFromCSV(selectedDir.getAbsolutePath());
            appendAndScroll("Import dokončený z: " + selectedDir.getAbsolutePath());
        }
    }

    private void displayPeople(List<Person> list, String heading) {
        output.setText("");
        appendAndScroll("*** " + heading + " ***");
        for (Person person : list) {
            appendAndScroll(formatPersonLine(person));
        }
        appendAndScroll("POČET: " + list.size());
    }

    private String formatPersonLine(Person person) {
        return String.format("Osoba: %s %s (ID: %s, Dátum narodenia: %s, Počet testov: %d)",
                person.getFirstName(),
                person.getLastName(),
                person.getPatientId(),
                person.getDateOfBirth(),
                person.getTestsByDate().inorder().size());
    }

    private void displayTests(List<PCRTest> list, String heading) {
        output.setText("");
        appendAndScroll("*** " + heading + " ***");
        for (PCRTest t : list) {
            appendAndScroll(formatTestLine(t));
        }
        appendAndScroll("POČET: " + list.size());
    }

    private String formatTestLine(PCRTest t) {
        return String.format("ID testu = %d | ID pacienta = %s | Dátum a čas vykonania testu = %s | Pozitívny? = %b | Hodnota = %.2f | Pracovisko = %d | Okres = %d | Kraj = %d | Poznámka = %s",
                t.getTestId(), t.getPatientId(), t.getDatetime(), t.isPositive(), t.getValue(),
                t.getWorkplaceId(), t.getDistrictId(), t.getRegionId(), t.getNote());
    }

    private void appendAndScroll(String s) {
        output.append(s + "\n");
        output.setCaretPosition(output.getDocument().getLength());
    }

    static void main(String[] args) {
        // create DB and GUI
        Database db = new Database();
        SwingUtilities.invokeLater(() -> {
            DatabaseGUI gui = new DatabaseGUI(db);
            gui.setVisible(true);
        });
    }
}