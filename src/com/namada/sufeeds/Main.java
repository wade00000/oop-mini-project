// Wade Antoine Namada BBIT-B 226828 22-11-2026
package com.namada.sufeeds;
import javax.swing.*;
import java.sql.SQLException;


public class Main {

    public static void main(String[] args) throws SQLException {
        SwingUtilities.invokeLater(() -> {
            LoginFrame f = new LoginFrame();
            f.setVisible(true);
        });
    }
}