package view.user;

import components.shared.utils.ResponseDTO;
import components.user.*;
import java.awt.*;
import javax.swing.*;

class Login extends JFrame {
    private JPanel panel;
    private JTextField txtUsername;
    private JPasswordField txtPassword;

    public Login() {
        panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Tạo các trường nhập liệu
        txtUsername = new JTextField(20); // Giới hạn chiều dài
        txtPassword = new JPasswordField(20);

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
        panel.add(Utilities.createLabel("Username:", "bold", 16), gbc);

        gbc.gridx = 1; // Cột 1
        panel.add(txtUsername, gbc);

        gbc.gridx = 0; // Cột 0
        gbc.gridy = 1; // Dòng 1
        panel.add(Utilities.createLabel("Password:", "bold", 16), gbc);

        gbc.gridx = 1; // Cột 1
        panel.add(txtPassword, gbc);

        // Nút đăng nhập
        JButton btnLogin = new JButton("Login");
        btnLogin.setFont(font);
        btnLogin.setBackground(Color.BLUE);
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setBorderPainted(true);
        btnLogin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        gbc.gridx = 1; // Cột 1
        gbc.gridy = 2; // Dòng 2
        panel.add(btnLogin, gbc);

        // Thêm nút "Register" để chuyển qua trang đăng ký
        JButton btnRegister = new JButton("Register");
        btnRegister.setFont(font);
        btnRegister.setBackground(Color.GRAY);
        btnRegister.setForeground(Color.WHITE);
        btnRegister.setFocusPainted(false);
        btnRegister.setBorderPainted(true);
        btnRegister.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        gbc.gridy = 4; // Dòng 3
        panel.add(btnRegister, gbc);

        // Thiết lập hành động khi nhấn nút "Login"
        btnLogin.addActionListener(e -> {
            String username = txtUsername.getText();
            String password = new String(txtPassword.getPassword());

            // Xác thực đăng nhập
            UserBUS userBUS = new UserBUS();
            ResponseDTO result = userBUS.login(username, password);

            if (!result.isSuccess()) {
                JOptionPane.showMessageDialog(null, result.getMessage());
                return;
            }

            JOptionPane.showMessageDialog(null, result.getMessage());
            // Chuyển đổi sang giao diện "Management"
            Container topLevel = panel.getTopLevelAncestor();
            if (topLevel instanceof UserDashboard) {
                ((UserDashboard) topLevel).switchPanel("Management");
            }
        });

        // Thiết lập hành động khi nhấn nút "Register"
        btnRegister.addActionListener(e -> {
            // Tìm JFrame chứa "Login" và yêu cầu chuyển đổi sang giao diện "Register"
            Container topLevel = panel.getTopLevelAncestor();
            if (topLevel instanceof UserDashboard) {
                ((UserDashboard) topLevel).switchPanel("Register");
            }
        });
    }

    public JPanel getPanel() {
        return panel;
    }
}
