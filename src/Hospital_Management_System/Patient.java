package Hospital_Management_System;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Patient {
    private Connection connection;
    private Scanner scanner;

    public Patient(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    public void addPatient() {
        System.out.print("Enter Patient name: ");
        String name = scanner.next();
        System.out.print("Enter age of Patient: ");
        int age = 0;
        boolean validInput = false;

        while (!validInput) {
            try {
                age = scanner.nextInt();
                validInput = true;  // Exit loop if input is valid
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid integer for age.");
                scanner.next();  // Clear the invalid input
            }
        }
        System.out.print("Enter patient gender: ");
        String gender = scanner.next();
        try {
            String query = "insert into Patients(Name,Age,Gender) values(?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, age);
            preparedStatement.setString(3, gender);
            int affectRows = preparedStatement.executeUpdate();
            if (affectRows > 0) {
                System.out.println("Patient added successfully");
            } else {
                System.out.println("Failed to add Patient");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void viewPatients() {
        String query = "select * from patients";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("Patients: ");
            System.out.println("+------------+--------------------+-----+-------+");
            System.out.println("| Patient_id | Name               |Age  |Gender |");
            System.out.println("+------------+--------------------+-----+-------+");
            while (resultSet.next()) {
                int Patient_id = resultSet.getInt("Patient_id");
                String Name = resultSet.getString("Name");
                int Age = resultSet.getInt("Age");
                String Gender = resultSet.getString("Gender");
                System.out.printf("|%-12d|%-20s|%-5d|%-7s|\n", Patient_id, Name, Age, Gender);
                System.out.println("+------------+--------------------+-----+-------+");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean getPatientById(int id) {
        String query = "SELECT * from patients WHERE Patient_id= ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;

    }
}


