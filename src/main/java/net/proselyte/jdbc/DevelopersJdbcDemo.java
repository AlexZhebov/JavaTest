package net.proselyte.jdbc;

import java.sql.*;

public class DevelopersJdbcDemo {


    /**
     * JDBC Driver and database url
     */
    //static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DATABASE_URL = "jdbc:mysql://localhost:3306/test1";

    /**
     * User and Password
     */
    static final String USER = "root";
    static final String PASSWORD = "12345678";

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Connection connection = null;
        Statement statement = null;
        System.out.println("Registering JDBC driver...");
        //showDB.resultHTML = " gfg";
        Class.forName("com.mysql.jdbc.Driver");

        System.out.println("Creating database connection...");
        connection = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);

        System.out.println("Executing statement...");
        statement = connection.createStatement();

        String sql;
        sql = "SELECT * FROM persons";

        ResultSet resultSet = statement.executeQuery(sql);

        System.out.println("Retrieving data from database...");
        System.out.println("\nDevelopers:");
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String firstname = resultSet.getString("firstname");
            String lastname = resultSet.getString("lastname");
            String city = resultSet.getString("city");
            String dataR = resultSet.getString("dataR");

            System.out.println("\n================\n");
            System.out.println("id: " + id);
            System.out.println("FirstName: " + firstname);
            System.out.println("LastName: " + lastname);
            System.out.println("City: " + city);
            System.out.println("DataR: " + dataR);
        }

        System.out.println("Closing connection and releasing resources...");
        resultSet.close();
        statement.close();
        connection.close();
    }
}
