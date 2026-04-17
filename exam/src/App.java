import javax.swing.*;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            } catch (Exception ignored) {
            }
            JFrame frame = new JFrame("Exam");
            MainWindow form = new MainWindow();
            frame.setContentPane(form.getMainPanel());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(820, 520);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}