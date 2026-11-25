import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame f = new JFrame("Pixel Art Editor");
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.add(new JLabel("It works!", SwingConstants.CENTER));
            f.setSize(400, 300);
            f.setLocationRelativeTo(null);
            f.setVisible(true);
        });
    }
}