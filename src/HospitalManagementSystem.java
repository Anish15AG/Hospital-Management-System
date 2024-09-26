package HospitalManagementSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Vector;

public class HospitalManagementSystem {
    private static final String url = "jdbc:mysql://localhost:3306/hospital";
    private static final String username = "root";
    private static final String password = "12345678"; // update with your password

    private Connection connection;
    private JFrame frame;

    public HospitalManagementSystem() {
        try {
            connection = DriverManager.getConnection(url, username, password);
            createAndShowGUI();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createAndShowGUI() {
        frame = new JFrame("Hospital Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        // Create Tabs
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Manage Patients", createPatientPanel());
        tabbedPane.addTab("Manage Doctors", createDoctorPanel());
        frame.add(tabbedPane, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    private JPanel createPatientPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Form to add patients
        JPanel form = new JPanel(new GridLayout(4, 2));
        JTextField nameField = new JTextField();
        JTextField ageField = new JTextField();
        JTextField genderField = new JTextField();

        form.add(new JLabel("Patient Name:"));
        form.add(nameField);
        form.add(new JLabel("Patient Age:"));
        form.add(ageField);
        form.add(new JLabel("Patient Gender:"));
        form.add(genderField);

        JButton addButton = new JButton("Add Patient");
        addButton.addActionListener(e -> addPatient(nameField, ageField, genderField));
        form.add(addButton);

        panel.add(form, BorderLayout.NORTH);

        // Table to view patients
        JTable patientTable = new JTable();
        refreshPatientTable(patientTable);
        panel.add(new JScrollPane(patientTable), BorderLayout.CENTER);

        return panel;
    }

    private void addPatient(JTextField nameField, JTextField ageField, JTextField genderField) {
        String name = nameField.getText();
        int age = Integer.parseInt(ageField.getText());
        String gender = genderField.getText();

        try {
            String query = "INSERT INTO patients(name, age, gender) VALUES(?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, age);
            preparedStatement.setString(3, gender);
            preparedStatement.executeUpdate();
            JOptionPane.showMessageDialog(frame, "Patient Added Successfully!");
            nameField.setText("");
            ageField.setText("");
            genderField.setText("");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error adding patient.");
        }
    }

    private void refreshPatientTable(JTable table) {
        try {
            String query = "SELECT * FROM patients";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            // Convert ResultSet to TableModel
            Vector<Vector<Object>> data = new Vector<>();
            Vector<String> columnNames = new Vector<>();
            columnNames.add("ID");
            columnNames.add("Name");
            columnNames.add("Age");
            columnNames.add("Gender");

            while (resultSet.next()) {
                Vector<Object> row = new Vector<>();
                row.add(resultSet.getInt("id"));
                row.add(resultSet.getString("name"));
                row.add(resultSet.getInt("age"));
                row.add(resultSet.getString("gender"));
                data.add(row);
            }

            table.setModel(new javax.swing.table.DefaultTableModel(data, columnNames));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private JPanel createDoctorPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Form to add doctors
        JPanel form = new JPanel(new GridLayout(3, 2));
        JTextField nameField = new JTextField();
        JTextField specializationField = new JTextField();

        form.add(new JLabel("Doctor Name:"));
        form.add(nameField);
        form.add(new JLabel("Specialization:"));
        form.add(specializationField);

        JButton addButton = new JButton("Add Doctor");
        addButton.addActionListener(e -> addDoctor(nameField, specializationField));
        form.add(addButton);

        panel.add(form, BorderLayout.NORTH);

        // Table to view doctors
        JTable doctorTable = new JTable();
        refreshDoctorTable(doctorTable);
        panel.add(new JScrollPane(doctorTable), BorderLayout.CENTER);

        return panel;
    }

    private void addDoctor(JTextField nameField, JTextField specializationField) {
        String name = nameField.getText();
        String specialization = specializationField.getText();

        try {
            String query = "INSERT INTO doctors(name, specialization) VALUES(?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, specialization);
            preparedStatement.executeUpdate();
            JOptionPane.showMessageDialog(frame, "Doctor Added Successfully!");
            nameField.setText("");
            specializationField.setText("");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error adding doctor.");
        }
    }

    private void refreshDoctorTable(JTable table) {
        try {
            String query = "SELECT * FROM doctors";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            Vector<Vector<Object>> data = new Vector<>();
            Vector<String> columnNames = new Vector<>();
            columnNames.add("ID");
            columnNames.add("Name");
            columnNames.add("Specialization");

            while (resultSet.next()) {
                Vector<Object> row = new Vector<>();
                row.add(resultSet.getInt("id"));
                row.add(resultSet.getString("name"));
                row.add(resultSet.getString("specialization"));
                data.add(row);
            }

            table.setModel(new javax.swing.table.DefaultTableModel(data, columnNames));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(HospitalManagementSystem::new);
    }
}
