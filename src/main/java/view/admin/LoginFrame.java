package view.admin;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import components.admin.AdminUserBUS;
import components.shared.utils.Response;

public class LoginFrame extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;

    public LoginFrame() {
        // Cài đặt khung chính
        setTitle("Admin Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Tạo giao diện chính
        initUI();

        setVisible(true);
    }

    private void initUI() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblUsername = new JLabel("Username:");
        JLabel lblPassword = new JLabel("Password:");

        txtUsername = new JTextField(15);
        txtPassword = new JPasswordField(15);

        btnLogin = new JButton("Login");

        // Bố trí giao diện
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(lblUsername, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        mainPanel.add(txtUsername, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(lblPassword, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        mainPanel.add(txtPassword, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        mainPanel.add(btnLogin, gbc);

        // Thêm Logic xử lý khi click Login
        btnLogin.addActionListener(e -> handleLogin());

        add(mainPanel, BorderLayout.CENTER);
    }

    private void handleLogin() {
        String username = txtUsername.getText();
        String password = new String(txtPassword.getPassword());

        AdminUserBUS adminUserBUS = new AdminUserBUS();
        Response response = adminUserBUS.login(username, password);

        if (response.isSuccess()) {
            JOptionPane.showMessageDialog(null, "Login Successful! Redirecting to Admin Dashboard...");
            SwingUtilities.invokeLater(() -> {
                new AdminDashboard();
                dispose();
            });
        } else {
            JOptionPane.showMessageDialog(null, "Login Failed: " + response.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame());
    }
}
