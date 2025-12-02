package app;

import view.LoginView;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Chạy giao diện an toàn trong luồng sự kiện Swing
        SwingUtilities.invokeLater(() -> {
            new LoginView().setVisible(true);
        });
    }
}