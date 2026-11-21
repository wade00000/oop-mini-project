// Main.java
package com.namada.sufeeds ;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Connection;




public class Main {

     static final String DRIVER = "com.mysql.cj.jdbc.Driver";
     static final String DATABASE_URL = "jdbc:mysql://localhost/db_wade_namada_226828";
     static final String USER = "root";
     static final String PASSWORD = "mysql";


     public static void main(String[] args){

     Statement statement = null;
     ResultSet result = null;

     try{
         // Load the driver class
         ClassEntry.forName(DRIVER);
          Connection conn = DBConnection.getConnection();

         // Establish connection


         // Create statement for quering the database
//          statement = conn.createStatement();
//          result = statement.executeQuery("SELECT * FROM tblBooks");
//         String sql = "INSERT INTO tbl_students(id,admission_no,name,password_hash,created_at) VALUES (?,?,?,?,?)";

         String sql = "SELECT id,name FROM tbl_students";
         try (Statement stmt = conn.createStatement();
              ResultSet rs = stmt.executeQuery(sql)) {
             while (rs.next()) {
                 int id = rs.getInt("id");
                 String name = rs.getString("name");
                 System.out.println(id + ": " + name);
             }
         }

         //PreparedStatement pstmt = conn.prepareStatement(sql);

        // Bind values to placeholders (1-based index)
//         pstmt.setInt(1, 1);
//         pstmt.setString(2, "226828");
//         pstmt.setString(3, "Wade Namada");
//         pstmt.setString(4, "wadenamada");
//         pstmt.setObject(5, LocalDateTime.now());
//
//         pstmt.executeUpdate();

//
         while(result.next()){
          String author = (String) result.getObject(2);
          String title = (String) result.getObject(3);
          System.out.println(author + ": " +title);
          }

     }catch (ClassNotFoundException e){
          Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null,e);
     }catch (SQLException e){
          Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null,e);
     }
 }
}