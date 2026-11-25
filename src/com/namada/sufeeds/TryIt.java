package com.namada.sufeeds;

import javax.swing.*;

public class TryIt {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginFrame f = new LoginFrame();
            f.setVisible(true);
        });
    }
}
