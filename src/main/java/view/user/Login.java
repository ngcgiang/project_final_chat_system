package view.user;

import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import components.shared.utils.Response;
import components.shared.utils.Utilities;
import components.user.UserBUS;

public class Login extends JFrame {
    private JPanel panel;
    private JTextField txtUsername;
    private JPasswordField txtPassword;

    public Login() {
        panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Tạo các trường nhập liệu
        txtUsername = new JTextField(20);
        txtPassword = new JPasswordField(20);

        // Thay đổi kích thước font chữ
        Font font = new Font("Arial", Font.PLAIN, 16);
        txtUsername.setFont(font);
        txtPassword.setFont(font);

        // Sử dụng GridBagConstraints để bố trí linh hoạt
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(Utilities.createLabel("Username:", "bold", 16), gbc);

        gbc.gridx = 1;
        panel.add(txtUsername, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(Utilities.createLabel("Password:", "bold", 16), gbc);

        gbc.gridx = 1;
        panel.add(txtPassword, gbc);

        // Nút đăng nhập
        JButton btnLogin = new JButton("Login");
        btnLogin.setFont(font);
        btnLogin.setBackground(Color.BLUE);
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setBorderPainted(true);
        btnLogin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(btnLogin, gbc);

        // Thêm nút "Register"
        JButton btnRegister = new JButton("Register");
        btnRegister.setFont(font);
        btnRegister.setBackground(Color.GRAY);
        btnRegister.setForeground(Color.WHITE);
        btnRegister.setFocusPainted(false);
        btnRegister.setBorderPainted(true);
        btnRegister.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        gbc.gridy = 4;
        panel.add(btnRegister, gbc);

        // Thêm logic cho nút Login
        btnLogin.addActionListener(e -> {
            String username = txtUsername.getText();
            String password = new String(txtPassword.getPassword());

            UserBUS userBUS = new UserBUS();
            Response result = userBUS.login(username, password);

            if (!result.isSuccess()) {
                JOptionPane.showMessageDialog(null, result.getMessage());
                return;
            }

            JOptionPane.showMessageDialog(null, result.getMessage());

            Container topLevel = panel.getTopLevelAncestor();
            if (topLevel instanceof UserDashboard) {
                ((UserDashboard) topLevel).switchPanel("Management");
            }
        });

        // Thêm logic cho nút Register
        btnRegister.addActionListener(e -> {
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
