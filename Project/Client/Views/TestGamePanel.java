import javax.swing.*;
import Project.Client.Views.GamePanel;

public class TestGamePanel {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Game Panel Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new GamePanel());
        frame.setSize(500, 400);
        frame.setVisible(true);
    }
}
