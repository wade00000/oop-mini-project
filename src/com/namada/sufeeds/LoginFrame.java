// Wade Antoine Namada BBIT-B 226828 22-11-2026
package com.namada.sufeeds;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class LoginFrame extends BaseFrame implements ActionListener {

    private JLabel lblAdmission, lblPassword;
    private JTextField txtAdmission;
    private JPasswordField txtPassword;
    private JButton btnLogin, btnRegister;

    public LoginFrame() {
        super("SU Feeds - Login");

        contentPanel.setLayout(new GridLayout(3, 2, 10, 10));

        lblAdmission = new JLabel("Adm No.:");
        txtAdmission = new JTextField(18);
        lblPassword = new JLabel("Password:");
        txtPassword = new JPasswordField(16);
        btnLogin = new JButton("Login");
        btnRegister = new JButton("Register");

        contentPanel.add(lblAdmission);
        contentPanel.add(txtAdmission);
        contentPanel.add(lblPassword);
        contentPanel.add(txtPassword);
        contentPanel.add(btnLogin);
        contentPanel.add(btnRegister);

        btnLogin.addActionListener(this);
        btnRegister.addActionListener(this);

        // Pressing Enter anywhere in this window now clicks Login
        getRootPane().setDefaultButton(btnLogin);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnLogin) {
            login();
        } else if (e.getSource() == btnRegister) {
            RegisterFrame frame = new RegisterFrame();
            frame.setVisible(true);
            dispose();
        }
    }

    private void login() {
        // A new attempt must never leave a previous student logged in
        Session.clear();

        String admission = txtAdmission.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (admission.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in both fields.",
                    "Empty Fields", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Student student = StudentDAO.findByAdmissionNumber(admission);

            if (student == null || !PasswordHasher.verifyPassword(password, student.getHashedPassword())) {
                JOptionPane.showMessageDialog(this, "Invalid admission number or password.",
                        "Login Failed", JOptionPane.WARNING_MESSAGE);
                txtPassword.setText("");
                return;
            }

            // Session must be set BEFORE the dashboard is created: its constructor reads it
            Session.setCurrentStudent(student);
            DashboardFrame dashboard = new DashboardFrame();
            dashboard.setVisible(true);
            this.dispose();

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Can't reach the database. Is MySQL running?",
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}