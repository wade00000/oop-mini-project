// Wade Antoine Namada BBIT-B 226828 22-11-2026
package com.namada.sufeeds;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FeedbackDAO {

    // Saves NEW feedback. Afterwards the object knows its id.
    // NOTE: uses getTopicId(), so rename getClassId() in Feedback first.
    public static void insert(Feedback f) throws SQLException {
        String sql = "INSERT INTO tbl_feedback (topic_id, student_id, content) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, f.getTopicId());
            ps.setInt(2, f.getStudentId());
            ps.setString(3, f.getContent());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    f.setId(keys.getInt(1));
                }
            }
        }
    }

    // All feedback on one topic, newest first. Empty list if there is none.
    public static List<Feedback> findByTopic(int topicId) throws SQLException {
        String sql = "SELECT id, topic_id, student_id, content, created_at "
                + "FROM tbl_feedback WHERE topic_id = ? ORDER BY created_at DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, topicId);

            try (ResultSet rs = ps.executeQuery()) {
                List<Feedback> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(new Feedback(rs.getInt("id"),
                            rs.getInt("topic_id"),
                            rs.getInt("student_id"),
                            rs.getString("content"),
                            rs.getTimestamp("created_at").toLocalDateTime()));
                }
                return list;
            }
        }
    }

    // Changes the content of one feedback entry, but only if studentId is its author.
    // Returns true if a row was changed, false if the feedback doesn't exist
    // or belongs to someone else.
    public static boolean update(int feedbackId, String newContent, int studentId) throws SQLException {
        String sql = "UPDATE tbl_feedback SET content = ? WHERE id = ? AND student_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, newContent);
            ps.setInt(2, feedbackId);
            ps.setInt(3, studentId);

            return ps.executeUpdate() > 0;
        }
    }

    // Deletes one feedback entry, but only if studentId is its author.
    // Returns true if a row was deleted, false otherwise.
    public static boolean delete(int feedbackId, int studentId) throws SQLException {
        String sql = "DELETE FROM tbl_feedback WHERE id = ? AND student_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, feedbackId);
            ps.setInt(2, studentId);

            return ps.executeUpdate() > 0;
        }
    }
}
