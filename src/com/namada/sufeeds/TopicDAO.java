// Wade Antoine Namada BBIT-B 226828 22-11-2026
package com.namada.sufeeds;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TopicDAO {

    // Saves a NEW topic. Afterwards the object knows its id.
    public static void insert(Topic t) throws SQLException {
        String sql = "INSERT INTO tbl_topics (class_id, student_id, title) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, t.getClassId());
            ps.setInt(2, t.getStudentId());
            ps.setString(3, t.getTitle());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    t.setId(keys.getInt(1));
                }
            }
        }
    }

    // All topics in one class, newest first. Empty list if there are none.
    public static List<Topic> findByClass(int classId) throws SQLException {
        String sql = "SELECT id, class_id, student_id, title, created_at "
                + "FROM tbl_topics WHERE class_id = ? ORDER BY created_at DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, classId);

            try (ResultSet rs = ps.executeQuery()) {
                List<Topic> topics = new ArrayList<>();
                while (rs.next()) {
                    topics.add(new Topic(rs.getInt("id"),
                            rs.getInt("class_id"),
                            rs.getInt("student_id"),
                            rs.getString("title"),
                            rs.getTimestamp("created_at").toLocalDateTime()));
                }
                return topics;
            }
        }
    }

    // Changes the title of a topic, but only if studentId is its owner.
    // Returns true if a row was changed, false if the topic doesn't exist
    // or belongs to someone else.
    public static boolean update(Topic t, int studentId) throws SQLException {
        String sql = "UPDATE tbl_topics SET title = ? WHERE id = ? AND student_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, t.getTitle());
            ps.setInt(2, t.getId());
            ps.setInt(3, studentId);

            return ps.executeUpdate() > 0;
        }
    }

    // Deletes a topic, but only if studentId is its owner.
    // Returns true if a row was deleted, false otherwise.
    public static boolean delete(int topicId, int studentId) throws SQLException {
        String sql = "DELETE FROM tbl_topics WHERE id = ? AND student_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, topicId);
            ps.setInt(2, studentId);

            return ps.executeUpdate() > 0;
        }
    }

}
