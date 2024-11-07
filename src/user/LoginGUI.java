package user;

import java.awt.*;
import javax.swing.*;

class LoginGUI {
    private JPanel panel;
    private JTextField txtUsername;
    private JPasswordField txtPassword;

    public LoginGUI() {
        panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(Color.LIGHT_GRAY);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Tạo các trường nhập liệu
        txtUsername = new JTextField(15); // Giới hạn chiều dài
        txtPassword = new JPasswordField(15);

        // Thay đổi kích thước font chữ
        Font font = new Font("Arial", Font.PLAIN, 16);
        txtUsername.setFont(font);
        txtPassword.setFont(font);

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

        // Nút đăng nhập
        JButton btnLogin = new JButton("Login");
        btnLogin.setFont(font);
        btnLogin.setBackground(Color.BLUE);
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setBorderPainted(false);
        btnLogin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        gbc.gridx = 1; // Cột 1
        gbc.gridy = 2; // Dòng 2
        panel.add(btnLogin, gbc);
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
