// Wade Antoine Namada BBIT-B 226828 22-11-2026
package com.namada.sufeeds;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EnrollmentDAO {

    // Links a student to a class. The pair (student_id, class_id) is the primary key,
    // so enrolling the same student twice throws an SQLException.
    public static void enroll(int studentId, int classId) throws SQLException {
        String sql = "INSERT INTO tbl_student_classes (student_id, class_id) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            ps.setInt(2, classId);
            ps.executeUpdate();
        }
    }

    // Removes a student from a class. Returns true if a row was deleted,
    // false if the student wasn't enrolled in that class.
    public static boolean drop(int studentId, int classId) throws SQLException {
        String sql = "DELETE FROM tbl_student_classes WHERE student_id = ? AND class_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            ps.setInt(2, classId);

            return ps.executeUpdate() > 0;
        }
    }

    // True if the student is already enrolled in the class.
    public static boolean isEnrolled(int studentId, int classId) throws SQLException {
        String sql = "SELECT 1 FROM tbl_student_classes WHERE student_id = ? AND class_id = ? LIMIT 1";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            ps.setInt(2, classId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    // All classes one student is enrolled in. Empty list if there are none.
    public static List<ClassEntry> findClassesByStudent(int studentId) throws SQLException {
        String sql = "SELECT c.id, c.code, c.title "
                + "FROM tbl_classes c "
                + "JOIN tbl_student_classes sc ON c.id = sc.class_id "
                + "WHERE sc.student_id = ? "
                + "ORDER BY c.title";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, studentId);

            try (ResultSet rs = ps.executeQuery()) {
                List<ClassEntry> classes = new ArrayList<>();
                while (rs.next()) {
                    classes.add(new ClassEntry(rs.getInt("id"),
                            rs.getString("code"),
                            rs.getString("title"))
                    );
                }
                return classes;
            }
        }
    }
}