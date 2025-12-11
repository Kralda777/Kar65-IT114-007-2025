// TestConnectionPanel.java
// Simple harness to test a connection-style panel for Milestone 3
package Project.Client.Views;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import java.awt.GridLayout;

public class TestConnectionPanel {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Create main window
            JFrame frame = new JFrame("Test Connection Panel - kar65");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // Create a panel that looks like a connection panel
            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(4, 2, 5, 5));

            // Username row
            panel.add(new JLabel("Username:"));
            panel.add(new JTextField(15));

            // Host row
            panel.add(new JLabel("Host:"));
            panel.add(new JTextField(15));

            // Port row
            panel.add(new JLabel("Port:"));
            panel.add(new JTextField(6));

            // Connect button
            panel.add(new JLabel("")); // spacer
            panel.add(new JButton("Connect"));

            frame.getContentPane().add(panel);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}

