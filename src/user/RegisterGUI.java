package user;

import java.awt.*;
import javax.swing.*;

public class RegisterGUI {
    private JPanel panel;
    private JTextField txtUsername;
    private JPasswordField txtPassword, txtConfirmPassword;

    public RegisterGUI() {
        panel = new JPanel();
        panel.setLayout(new GridBagLayout()); // Sử dụng GridBagLayout cho bố trí linh hoạt
        panel.setBackground(Color.LIGHT_GRAY);

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
        panel.add(createLabel("Username:"), gbc);

        gbc.gridx = 1; // Cột 1
        panel.add(txtUsername, gbc);

        gbc.gridx = 0; // Cột 0
        gbc.gridy = 1; // Dòng 1
        panel.add(createLabel("Password:"), gbc);

        gbc.gridx = 1; // Cột 1
        panel.add(txtPassword, gbc);

        gbc.gridx = 0; // Cột 0
        gbc.gridy = 2; // Dòng 2
        panel.add(createLabel("Confirm Password:"), gbc);

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
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setHorizontalAlignment(SwingConstants.LEFT);
        return label;
    }

    public JPanel getPanel() {
        return panel;
    }
}
