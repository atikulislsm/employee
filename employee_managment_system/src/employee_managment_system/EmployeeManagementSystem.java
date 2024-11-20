/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package employee_managment_system;

/**
 *
 * @author HP
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class EmployeeManagementSystem extends JFrame {
    private JTextField idField, nameField, positionField, salaryField;
    private JButton addButton, viewButton, updateButton, deleteButton;
    private JTextArea outputArea;

    // Database connection details
    private final String DB_URL = "jdbc:mysql://localhost:3306/EmployeeDB";
    private final String USER = "root"; // Update your MySQL username
    private final String PASSWORD = "12345"; // Update your MySQL password

    public EmployeeManagementSystem() {
        // Frame configuration
        setTitle("Employee Management System");
        setSize(600, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Main panel with vertical layout
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Input fields
        mainPanel.add(createLabeledField("ID (For Update/Delete):", idField = new JTextField(20)));
        mainPanel.add(createLabeledField("Name:", nameField = new JTextField(20)));
        mainPanel.add(createLabeledField("Position:", positionField = new JTextField(20)));
        mainPanel.add(createLabeledField("Salary:", salaryField = new JTextField(20)));
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 2, 10, 10)); 
        addButton = new JButton("Add Employee");
        viewButton = new JButton("View Employees");
        updateButton = new JButton("Update Employee");
        deleteButton = new JButton("Delete Employee");
        buttonPanel.add(addButton);
        buttonPanel.add(viewButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 10))); 
        mainPanel.add(buttonPanel);

        JLabel outputLabel = new JLabel("Output:");
        outputArea = new JTextArea(8, 40);
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);

        mainPanel.add(outputLabel);
        mainPanel.add(scrollPane);

        add(mainPanel, BorderLayout.CENTER);

        // Button actions
        addButton.addActionListener(e -> addEmployee());
        viewButton.addActionListener(e -> viewEmployees());
        updateButton.addActionListener(e -> updateEmployee());
        deleteButton.addActionListener(e -> deleteEmployee());
    }
    private JPanel createLabeledField(String label, JTextField textField) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(5, 5));
        JLabel jLabel = new JLabel(label);
        panel.add(jLabel, BorderLayout.NORTH);
        panel.add(textField, BorderLayout.CENTER);
        return panel;
    }

    // Method to add an employee
    private void addEmployee() {
        String name = nameField.getText();
        String position = positionField.getText();
        String salary = salaryField.getText();

        String query = "INSERT INTO Employees (name, position, salary) VALUES (?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, position);
            preparedStatement.setDouble(3, Double.parseDouble(salary));

            int rows = preparedStatement.executeUpdate();
            outputArea.setText(rows + " employee(s) added.");
        } catch (Exception ex) {
            outputArea.setText("Error: " + ex.getMessage());
        }
    }

    // Method to view employees
    private void viewEmployees() {
        String query = "SELECT * FROM Employees";
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            StringBuilder result = new StringBuilder();
            result.append("ID\tName\tPosition\tSalary\n");
            result.append("---------------------------------\n");

            while (resultSet.next()) {
                result.append(resultSet.getInt("id")).append("\t");
                result.append(resultSet.getString("name")).append("\t");
                result.append(resultSet.getString("position")).append("\t");
                result.append(resultSet.getDouble("salary")).append("\n");
            }
            outputArea.setText(result.toString());
        } catch (Exception ex) {
            outputArea.setText("Error: " + ex.getMessage());
        }
    }
    private void updateEmployee() {
        String id = idField.getText();
        String name = nameField.getText();
        String position = positionField.getText();
        String salary = salaryField.getText();

        String query = "UPDATE Employees SET name = ?, position = ?, salary = ? WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, position);
            preparedStatement.setDouble(3, Double.parseDouble(salary));
            preparedStatement.setInt(4, Integer.parseInt(id));

            int rows = preparedStatement.executeUpdate();
            outputArea.setText(rows + " employee(s) updated.");
        } catch (Exception ex) {
            outputArea.setText("Error: " + ex.getMessage());
        }
    }
    private void deleteEmployee() {
        String id = idField.getText();

        String query = "DELETE FROM Employees WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, Integer.parseInt(id));

            int rows = preparedStatement.executeUpdate();
            outputArea.setText(rows + " employee(s) deleted.");
        } catch (Exception ex) {
            outputArea.setText("Error: " + ex.getMessage());
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            EmployeeManagementSystem app = new EmployeeManagementSystem();
            app.setVisible(true);
        });
    }
    
}
