package view;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import view.admin.LoginFrame;
import view.user.UserDashboard;

public class App extends JFrame {
    public App() {
        // Cài đặt giao diện cơ bản
        setTitle("Login - User");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Tạo Panel chính
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // Thêm tiêu đề
        JLabel label = new JLabel("Choose login type:", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        mainPanel.add(label, BorderLayout.NORTH);

        // Thêm nút
        JPanel buttonPanel = new JPanel();
        JButton userButton = new JButton("Login as User");
        JButton adminButton = new JButton("Login as Admin");

        buttonPanel.add(userButton);
        buttonPanel.add(adminButton);

        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        // Thêm logic click button
        adminButton.addActionListener(e -> {
            dispose();
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });

        userButton.addActionListener(e -> {
            dispose();
            UserDashboard userDashboard = new UserDashboard();
            userDashboard.setVisible(true);
        });

        add(mainPanel);
        setVisible(true);
    }

    public static void main(String[] args) {
        new App();
    }
}
