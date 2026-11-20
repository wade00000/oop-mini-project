package com.namada.sufeeds;

import javax.swing.*;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class RegisterFrame extends JFrame implements ActionListener{
    //Declare components
    JLabel lblName, lblid;
    JTextField txtName, txtid;
    JButton btnRegister;

    public RegisterFrame(){
        super ("Welcome");
        setLayout(new FlowLayout());

        // Initialise child component
        lblName = new JLabel("Name:");
        txtName = new JTextField(18);
        lblid = new JLabel("ID no:");
        txtid = new JTextField(10);
        btnRegister = new JButton("Register:");

        // Link Button to Handler
        btnRegister.addActionListener(this);

        // Add components to com.namada.sufeeds.RegisterFrame
        add(lblName);
        add(txtName);
        add(lblid);
        add(txtid);
        add(btnRegister);


    }

    public static void main(String[] args) {
        // Create object to visualize GUI
        RegisterFrame rFrame = new RegisterFrame();
        rFrame.setSize(200, 300);
        rFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        rFrame.setVisible(true);
    }
    @Override
    public void actionPerformed(ActionEvent e){
        // This the event handler
        /*if user clicks on Button register - print his name*/
        if (e.getSource()== btnRegister);
        String msg = "Hello "+txtName.getText();
        JOptionPane.showMessageDialog(null,msg);
        System.out.println(msg);
    }

}