// Wade Antoine Namada BBIT-B 226828 22-11-2026
package com.namada.sufeeds;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

// Small helpers so every screen shows pop-ups the same way.
public class Dialogs {

    private Dialogs() {
    }

    public static void warn(Component parent, String message, String title) {
        JOptionPane.showMessageDialog(parent, message, title, JOptionPane.WARNING_MESSAGE);
    }

    public static void info(Component parent, String message, String title) {
        JOptionPane.showMessageDialog(parent, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    // Yes/No question. Returns true only if the user clicked Yes.
    public static boolean confirm(Component parent, String message, String title) {
        int choice = JOptionPane.showConfirmDialog(parent, message, title,
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        return choice == JOptionPane.YES_OPTION;
    }

    // Technical cause goes to the console for you; the user gets a calm message.
    public static void dbError(Component parent, SQLException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(parent, "A database error occurred. Is MySQL running?",
                "Database Error", JOptionPane.ERROR_MESSAGE);
    }

    // One-line text prompt. Returns the trimmed text, or null if the user cancelled.
    public static String promptText(Component parent, String label, String title, String initial) {
        JTextField field = new JTextField(initial, 28);
        JPanel panel = new JPanel(new BorderLayout(0, 6));
        panel.add(new JLabel(label), BorderLayout.NORTH);
        panel.add(field, BorderLayout.CENTER);

        int choice = JOptionPane.showConfirmDialog(parent, panel, title,
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        return choice == JOptionPane.OK_OPTION ? field.getText().trim() : null;
    }

    // Multi-line text prompt (for longer feedback). Returns trimmed text, or null if cancelled.
    public static String promptParagraph(Component parent, String title, String initial) {
        JTextArea area = new JTextArea(initial, 8, 36);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);

        int choice = JOptionPane.showConfirmDialog(parent, new JScrollPane(area), title,
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        return choice == JOptionPane.OK_OPTION ? area.getText().trim() : null;
    }
}