// Wade Antoine Namada BBIT-B 226828 22-11-2026
package com.namada.sufeeds;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    public static void main(String[] args) {
        String sql = "SELECT id, name FROM tbl_students";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                System.out.println(id + ": " + name);
            }

        } catch (SQLException e) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}

/*
 *

 * // Old statement/query from a different project:
 * // statement = conn.createStatement();
 * // result = statement.executeQuery("SELECT * FROM tblBooks");
 *
 * // Insert experiment:
 * // String sql = "INSERT INTO tbl_students(id,admission_no,name,password_hash,created_at) VALUES (?,?,?,?,?)";
 * // PreparedStatement pstmt = conn.prepareStatement(sql);
 * // pstmt.setInt(1, 1);
 * // pstmt.setString(2, "226828");
 * // pstmt.setString(3, "Wade Namada");
 * // pstmt.setString(4, "wadenamada");
 * // pstmt.setObject(5, LocalDateTime.now());
 * // pstmt.executeUpdate();
 *
 * // Old read loop (was reading from a ResultSet that was never assigned):
 * // while (result.next()) {
 * //     String author = (String) result.getObject(2);
 * //     String title = (String) result.getObject(3);
 * //     System.out.println(author + ": " + title);
 * // }
 */