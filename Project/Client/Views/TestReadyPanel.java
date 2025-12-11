package Project.Client.Views;

import javax.swing.*;
import java.awt.*;

public class TestReadyPanel {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Test Ready Panel - kar65");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 250);

            JPanel panel = new JPanel(new GridLayout(5, 2, 5, 5));

            // Column headers
            panel.add(new JLabel("Player"));
            panel.add(new JLabel("Ready?"));

            // Player 1 - ready
            panel.add(new JLabel("Player 1"));
            panel.add(new JLabel("Ready ✓"));

            // Player 2 - not ready
            panel.add(new JLabel("Player 2"));
            panel.add(new JLabel("Not Ready"));

            // Player 3 - ready
            panel.add(new JLabel("Player 3"));
            panel.add(new JLabel("Ready ✓"));

            // Ready button row (spans 2 columns using FlowLayout)
            JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.CENTER));
            JButton readyButton = new JButton("Mark Ready");
            buttonRow.add(readyButton);

            // Empty cell for alignment + button row
            panel.add(new JLabel(""));
            panel.add(buttonRow);

            frame.add(panel);
            frame.setVisible(true);
        });
    }
}
