package user;

import java.awt.*;
import javax.swing.*;

public class Register {
    private JPanel panel;
    private JTextField txtUsername;
    private JPasswordField txtPassword, txtConfirmPassword;

    public Register() {
        panel = new JPanel();
        panel.setLayout(new GridBagLayout()); // Sử dụng GridBagLayout cho bố trí linh hoạt

        // Tạo các trường nhập liệu
        txtUsername = new JTextField(20); // Giới hạn chiều dài
        txtPassword = new JPasswordField(20);
        txtConfirmPassword = new JPasswordField(20);

        // Thay đổi kích thước font chữ
        Font font = new Font("Arial", Font.PLAIN, 16);
        txtUsername.setFont(font);
        txtPassword.setFont(font);
        txtConfirmPassword.setFont(font);

        // Sử dụng GridBagConstraints để bố trí linh hoạt
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Khoảng cách giữa các thành phần
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; // Cột 0
        gbc.gridy = 0; // Dòng 0
        panel.add(Utilities.createLabel("Username:", "bold", 16), gbc);

        gbc.gridx = 1; // Cột 1
        panel.add(txtUsername, gbc);

        gbc.gridx = 0; // Cột 0
        gbc.gridy = 1; // Dòng 1
        panel.add(Utilities.createLabel("Password:", "bold", 16), gbc);

        gbc.gridx = 1; // Cột 1
        panel.add(txtPassword, gbc);

        gbc.gridx = 0; // Cột 0
        gbc.gridy = 2; // Dòng 2
        panel.add(Utilities.createLabel("Confirm Password:", "bold", 16), gbc);

        gbc.gridx = 1; // Cột 1
        panel.add(txtConfirmPassword, gbc);

        // Nút đăng ký
        JButton btnRegister = new JButton("Register");
        btnRegister.setFont(font);
        btnRegister.setBackground(Color.BLUE);
        btnRegister.setForeground(Color.WHITE);
        btnRegister.setFocusPainted(false);
        btnRegister.setBorderPainted(true);
        btnRegister.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        gbc.gridx = 1; // Cột 1
        gbc.gridy = 3; // Dòng 3
        panel.add(btnRegister, gbc);

        // Thêm nút "Login" để chuyển qua trang đăng nhập
        JButton btnLogin = new JButton("Login");
        btnLogin.setFont(font);
        btnLogin.setBackground(Color.GRAY);
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setBorderPainted(true);
        btnLogin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        gbc.gridy = 4; // Dòng 3
        panel.add(btnLogin, gbc);

        // Thiết lập hành động khi nhấn nút "Login"
        btnLogin.addActionListener(e -> {
            // Tìm JFrame chứa RegisterGUI và yêu cầu chuyển đổi sang giao diện "Login"
            Container topLevel = panel.getTopLevelAncestor();
            if (topLevel instanceof UserDashboard) {
                ((UserDashboard) topLevel).switchPanel("Login");
            }
        });
    }

    public JPanel getPanel() {
        return panel;
    }
}
