// Wade Antoine Namada BBIT-B 226828 22-11-2026
package com.namada.sufeeds;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClassDAO {


    public static void insert(ClassEntry c) throws SQLException {
        String sql = "INSERT INTO tbl_classes (code, title) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, c.getCode());
            ps.setString(2, c.getTitle());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    c.setId(keys.getInt(1));
                }
            }
        }
    }

    // Returns the matching class, or null if there isn't one.
    public static ClassEntry findByCode(String code) throws SQLException {
        String sql = "SELECT id, code, title FROM tbl_classes WHERE code = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, code);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new ClassEntry(rs.getInt("id"),
                            rs.getString("code"),
                            rs.getString("title"));
                }
                return null;
            }
        }
    }

    public static List<ClassEntry> findAll() throws SQLException{
        String sql = "SELECT id,code,title FROM tbl_classes ORDER BY code";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

                List<ClassEntry> classes = new ArrayList<> ();
                while (rs.next()) {
                    classes.add(new ClassEntry(rs.getInt("id"),
                            rs.getString("code"),
                            rs.getString("title")));
                }
                return classes;

        }

    }
}