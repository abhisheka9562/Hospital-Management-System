package Hospital_Management_System;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class Doctor {
    private Connection connection;


    public Doctor(Connection connection) {
        this.connection = connection;

    }



    public void viewDoctors() {
        String query = "select * from doctors";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("Doctors: ");
            System.out.println("+------------+--------------------+-----------------+");
            System.out.println("| Doctor_id | Name                |Specialization   |");
            System.out.println("+------------+--------------------+-----------------+");
            while (resultSet.next()) {
                int Doctor_id = resultSet.getInt("Doctor_id");
                String Name = resultSet.getString("Name");
                String Specialization = resultSet.getString("Specialization");
                System.out.printf("|%-12d|%-20s|%-17s|\n", Doctor_id, Name,Specialization);
                System.out.println("+------------+--------------------+-----------------+");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean getDoctorById(int id) {
        String query = "SELECT * from doctors WHERE Doctor_id= ?";
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




