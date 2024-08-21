package Hospital_Management_System;

import java.sql.*;
import java.util.Scanner;

public class HospitalManagementSystem {
    private static Scanner scanner = new Scanner(System.in);
    private static final String url = "jdbc:mysql://localhost:3306/hospital?user=root";
    private static final String username = "root";
    private static final String password = "password";


    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            Patient patient=new Patient(connection,scanner);
            Doctor doctor=new Doctor(connection);
            while(true)
            {
                System.out.println("Hospital Management System");
                System.out.println("1. Add Patient");
                System.out.println("2. View Patients");
                System.out.println("3. View Doctors");
                System.out.println("4. Book Appoinment");
                System.out.println("5. Exit");
                System.out.println("Enter your choice: ");
                int choice=scanner.nextInt();

                switch(choice)
                {
                    case 1: patient.addPatient();
                            break;
                    case 2: patient.viewPatients();
                            break;
                    case 3: doctor.viewDoctors();
                             break;
                    case 4:  bookAppointment(patient,doctor,connection,scanner);
                             break;
                    case 5: return;
                    default:System.out.println("Enter valid choice");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void bookAppointment(Patient patient,Doctor doctor,Connection connection,Scanner scanner)
    {
        System.out.print("Enter patient id: ");
        int pat_id=scanner.nextInt();
        System.out.print("Enter doctor id: ");
        int doc_id=scanner.nextInt();
        System.out.print("Enter appointment date (YYYY-MM-DD)1");
        String app_Date=scanner.next();
        String app_query="Insert into appointments(Patient_id,Doctor_id,Appointment_date) values(?,?,?);";
        if(patient.getPatientById(pat_id) && doctor.getDoctorById(doc_id))
        {
            if(checkDocAvail(doc_id,app_Date,connection))
            {

                       try{
                           PreparedStatement preparedStatement=connection.prepareStatement(app_query);
                           preparedStatement.setInt(1,pat_id);
                           preparedStatement.setInt(2,doc_id);
                           preparedStatement.setString(3,app_Date);
                           int rowsAffected=preparedStatement.executeUpdate();
                           if(rowsAffected>0)
                           {
                               System.out.println("Appointment Booked!!");
                           }
                           else
                           {
                               System.out.println("Failed to book Appointment");
                           }
                       }catch(SQLException e)
                       {
                           e.printStackTrace();
                       }
            }
            else
            {
                System.out.println("Doctor not available on this date");
            }
        }
        else
        {
            System.out.println("Either doctor or patient doesn't exist");
        }
    }
    public static boolean checkDocAvail(int doc_id,String app_date,Connection connection)
    {
       String query="SELECT COUNT(*) FROM appointments WHERE Doctor_id=? AND Appointment_date=?";
       try
       {
          PreparedStatement preparedStatement= connection.prepareStatement(query);
          preparedStatement.setInt(1,doc_id);
          preparedStatement.setString(2,app_date);
           ResultSet resultSet= preparedStatement.executeQuery();
           if(resultSet.next())
           {
               int count=resultSet.getInt(1);
               if(count==0)
               {
                   return true;
               }
               else
               {
                   return false;
               }
           }
       }catch(SQLException e){
           e.printStackTrace();
       }
       return false;
    }
}

