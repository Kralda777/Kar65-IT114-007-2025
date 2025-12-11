package Project.Client.Views;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {

    public GamePanel() {
        setLayout(new BorderLayout());

        // User list
        DefaultListModel<String> model = new DefaultListModel<>();
        model.addElement("KarenR - 0 pts");
        model.addElement("Player2 - 1 pt");
        model.addElement("Player3 - eliminated");

        JList<String> userList = new JList<>(model);
        add(new JScrollPane(userList), BorderLayout.WEST);

        // RPS Buttons
        JPanel buttons = new JPanel();
        buttons.add(new JButton("Rock"));
        buttons.add(new JButton("Paper"));
        buttons.add(new JButton("Scissors"));
        add(buttons, BorderLayout.SOUTH);

        // Log area
        JTextArea log = new JTextArea("Battle Log:\nRound starts in 5...");
        add(new JScrollPane(log), BorderLayout.CENTER);
    }
}
