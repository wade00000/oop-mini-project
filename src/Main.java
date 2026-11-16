// Main.java
package com.database;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Main {

    static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DATABASE_URL = "jdbc:mysql://localhost/dbbooks";
    static final String USER = "root";
    static final String PASSWORD = "mysql";

    public static void main(String[] args){
        Connection conn = null;
        Statement statement = null;
        ResultSet result = null;
        try{
            // Load the driver class
            Class.forName(DRIVER);

            // Establish connection
            conn = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);

            // Create statement for quering the database
            statement = conn.createStatement();
            result = statement.executeQuery("SELECT * FROM tblbooks");

            while(result.next()){
                    String author = (String) result.getObject(3);
                    String title = (String) result.getObject(4);
                    System.out.println(author + ": " + title);
            }
        }catch (ClassNotFoundException e){
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, e);
        }catch (SQLException e){
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}