package Project.Client.Views;

import javax.swing.*;
import java.awt.*;

public class UserDetailsPanel extends JPanel {

    public UserDetailsPanel() {
        setLayout(new GridLayout(3, 2, 5, 5));

        add(new JLabel("Username:"));
        add(new JTextField("KarenR"));

        add(new JLabel("Client ID:"));
        add(new JLabel("12345"));

        add(new JLabel("Status:"));
        add(new JLabel("Connected"));
    }
}

