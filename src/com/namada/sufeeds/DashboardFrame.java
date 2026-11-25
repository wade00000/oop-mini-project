// Wade Antoine Namada BBIT-B 226828 22-11-2026
package com.namada.sufeeds;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DashboardFrame extends BaseFrame implements ActionListener {

    private JLabel lblWelcome;
    private JButton btnMyClasses, btnLogout;

    // Always create this screen through login, AFTER Session.setCurrentStudent(...)
    public DashboardFrame() {
        super("SU Feeds - Dashboard");

        Student current = Session.getCurrentStudent();

        // Safety guard: if nobody is logged in, go back to login instead of crashing
        if (current == null) {
            new LoginFrame().setVisible(true);
            dispose();
            return;
        }

        // One column of rows: label on top, then the buttons
        contentPanel.setLayout(new GridLayout(0, 1, 10, 10));

        lblWelcome = new JLabel("Welcome, " + current.getName(), SwingConstants.CENTER);
        btnMyClasses = new JButton("My Classes");
        btnLogout = new JButton("Logout");

        contentPanel.add(lblWelcome);
        contentPanel.add(btnMyClasses);
        contentPanel.add(btnLogout);

        btnMyClasses.addActionListener(this);
        btnLogout.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnMyClasses) {
            // Topics and feedback are reached through a class, so My Classes is the way in
            new MyClassesFrame().setVisible(true);
            dispose();
        } else if (e.getSource() == btnLogout) {
            Session.clear();   // clear BEFORE opening login, so nobody inherits this identity
            new LoginFrame().setVisible(true);
            dispose();
        }
    }
}