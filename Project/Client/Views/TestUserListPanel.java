package Project.Client.Views;

import javax.swing.*;
import java.util.Arrays;

public class TestUserListPanel {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Test User List Panel - kar65");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            UserListPanel panel = new UserListPanel();

            // Fake data for 4 players
            panel.setUsers(Arrays.asList(
                    new UserListPanel.UserStatus("Player1",   1, 7, false, false), // picked
                    new UserListPanel.UserStatus("Player2",  2, 3, true,  false), // pending
                    new UserListPanel.UserStatus("Player3", 3, 7, false, true),  // eliminated
                    new UserListPanel.UserStatus("Player4",  4, 1, false, false)  // picked
            ));

            frame.setContentPane(panel);
            frame.setSize(550, 300);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}

