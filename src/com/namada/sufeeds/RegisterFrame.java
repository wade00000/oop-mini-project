// Wade Antoine Namada BBIT-B 226828 22-11-2026
package com.namada.sufeeds;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.SQLException;

public class RegisterFrame extends BaseFrame implements ActionListener{
    //Declare components
    JLabel lblName, lblAdm,lblPassword;
    JTextField txtName, txtAdm;
    JPasswordField txtPassword;
    JButton btnRegister;

    public RegisterFrame(){
        super ("Register");
        contentPanel.setLayout(new GridLayout(3, 2, 10, 10));


        // Initialise child component
        lblName = new JLabel("Name:");
        txtName = new JTextField(18);
        lblAdm = new JLabel("Adm no.:");
        txtAdm = new JTextField(10);
        lblPassword = new JLabel("Password:");
        txtPassword = new JPasswordField(16);
        btnRegister = new JButton("Register");

        // Link Button to Handler
        btnRegister.addActionListener(this);

        // Add components to com.namada.sufeeds.RegisterFrame
        contentPanel.add(lblName);
        contentPanel.add(txtName);
        contentPanel.add(lblAdm);
        contentPanel.add(txtAdm);
        contentPanel.add(lblPassword);
        contentPanel.add(txtPassword);
        contentPanel.add(btnRegister);


    }

    @Override
    public void actionPerformed(ActionEvent e){
        if (e.getSource() == btnRegister){


            String admission = txtAdm.getText().trim();
            String name = txtName.getText().trim();
            String password = new String(txtPassword.getPassword());

            if(admission.isEmpty() || name.isEmpty() || password.isEmpty()){
                JOptionPane.showMessageDialog(this,"Please Fill In The Blanks","Empty Fields",JOptionPane.WARNING_MESSAGE);

            }

            Student s = new Student(admission,name,password);
            try {
                StudentDAO.insert(s);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }


            this.dispose();

        }
    }

}