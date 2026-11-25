// Wade Antoine Namada BBIT-B 226828 22-11-2026
package com.namada.sufeeds;

import javax.swing.*;
import java.awt.*;

public class BaseFrame extends JFrame {

    protected JPanel contentPanel = new JPanel();   // the middle area for subclasses

    public BaseFrame(String title) {
        super(title);
        setLayout(new BorderLayout());
        JLabel footer = new JLabel("© 2026 Wade Antoine Namada", SwingConstants.CENTER);
        footer.setFont(new Font("SansSerif", Font.PLAIN, 10));

        add(contentPanel, BorderLayout.CENTER);
        add(footer,BorderLayout.SOUTH);


        setSize(450, 350);
        setLocationRelativeTo(null);       // centers the window on screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}

