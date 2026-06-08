package attendancesystem;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class CreateTables {

    public static void main(String[] args) {

        try {
            Connection con = DBConnection.getConnection();

            // Users Table SQL
            String users = "CREATE TABLE IF NOT EXISTS users ("
                    + "id VARCHAR(20) PRIMARY KEY,"
                    + "name VARCHAR(100),"
                    + "username VARCHAR(50) UNIQUE,"
                    + "password VARCHAR(50),"
                    + "role VARCHAR(20))";

            PreparedStatement pst1 = con.prepareStatement(users);
            pst1.executeUpdate();

            // Attendance Table SQL
            String attendance = "CREATE TABLE IF NOT EXISTS attendance ("
                    + "username VARCHAR(50),"
                    + "date VARCHAR(30),"
                    + "status VARCHAR(20))";

            PreparedStatement pst2 = con.prepareStatement(attendance);
            pst2.executeUpdate();

            System.out.println("Tables Created Successfully");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}