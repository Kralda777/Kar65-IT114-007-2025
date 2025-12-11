package Project.Client.Views;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class UserListPanel extends JPanel {

    // Simple data holder for one player row
    public static class UserStatus {
        public final String name;
        public final long id;
        public final int points;
        public final boolean pendingToPick;
        public final boolean eliminated;

        public UserStatus(String name, long id, int points,
                          boolean pendingToPick, boolean eliminated) {
            this.name = name;
            this.id = id;
            this.points = points;
            this.pendingToPick = pendingToPick;
            this.eliminated = eliminated;
        }
    }

    private final JPanel rowsPanel = new JPanel();

    public UserListPanel() {
        setLayout(new BorderLayout());

        // Header row
        JPanel header = new JPanel(new GridLayout(1, 4));
        header.add(new JLabel("Username:"));
        header.add(new JLabel("Client ID:"));
        header.add(new JLabel("Points:"));
        header.add(new JLabel("Status:"));
        add(header, BorderLayout.NORTH);

        rowsPanel.setLayout(new GridLayout(0, 4, 4, 4));
        add(rowsPanel, BorderLayout.CENTER);
    }

    // Called whenever we get updated user data
    //KarenRalda //Dec102025 //Kar65
    public void setUsers(List<UserStatus> users) {
        // Copy and sort: highest points first, then name
        List<UserStatus> sorted = new ArrayList<>(users);
        sorted.sort(
                Comparator.comparingInt((UserStatus u) -> -u.points)
                          .thenComparing(u -> u.name)
        );

        rowsPanel.removeAll();

        for (UserStatus u : sorted) {
            rowsPanel.add(new JLabel(u.name));
            rowsPanel.add(new JLabel(String.valueOf(u.id)));
            rowsPanel.add(new JLabel(String.valueOf(u.points)));

            String statusText;
            if (u.eliminated) {
                statusText = "Eliminated ✖";
            } else if (u.pendingToPick) {
                statusText = "Pending to pick…";
            } else {
                statusText = "Picked ✔";
            }
            rowsPanel.add(new JLabel(statusText));
        }

        revalidate();
        repaint();
    }
}

