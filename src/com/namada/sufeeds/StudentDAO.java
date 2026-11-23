// Wade Antoine Namada BBIT-B 226828 22-11-2026
package com.namada.sufeeds;

import java.sql.*;

public class StudentDAO {

    // Saves a NEW student. The hash was already created inside the Student constructor.
    public static void insert(Student s) throws SQLException {
        String sql = "INSERT INTO tbl_students (admission_no, name, password_hash) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, s.getAdmissionNumber());
            ps.setString(2, s.getName());
            ps.setString(3, s.getHashedPassword());   // the hash, never the typed password
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    s.setId(keys.getInt(1));
                }
            }
        }
    }

    // Returns the matching student (including the stored hash), or null if none exists.
    public static Student findByAdmissionNumber(String admissionNumber) throws SQLException {
        String sql = "SELECT id, admission_no, name, created_at, password_hash "
                + "FROM tbl_students WHERE admission_no = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, admissionNumber);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Five-argument constructor: the hash comes FROM the database,
                    // so it must not be hashed again.
                    return new Student(rs.getInt("id"),
                            rs.getString("admission_no"),
                            rs.getString("name"),
                            rs.getString("password_hash"),
                            rs.getTimestamp("created_at").toLocalDateTime());

                }
                return null;
            }
        }
    }

    public static boolean existsbyAdmissionNo(String admissionNumber) throws SQLException{
        String sql = "SELECT 1 from tbl_students WHERE admission_no = ? LIMIT  1";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);)   {

            ps.setString(1,admissionNumber);
            try(ResultSet rs = ps.executeQuery()){
                return rs.next();
            }
        }
    }

    public static boolean update(Student s,String name) throws SQLException{
        String sql = "UPDATE tbl_students SET name = ? WHERE admission_no = ? ";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);){

            ps.setString(1,name);
            ps.setString(2,s.getAdmissionNumber());

            Integer rowsAffected = ps.executeUpdate();
            System.out.println(rowsAffected + " row(s) changed");

            return rowsAffected > 0;

        }
    }

    public static boolean delete(Integer id) throws SQLException{
        String sql = "DELETE FROM tbl_students WHERE id = ? ";

        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);){

            ps.setInt(1,id);

            System.out.println(ps.executeUpdate() + " row(s) deleted");

            return ps.executeUpdate() > 0;

        }
    }




    // Also: PasswordHasher.verifyPassword(typed, storedHash) using BCrypt.checkpw
}