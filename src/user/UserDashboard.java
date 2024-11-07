package user;

import java.awt.*;
import javax.swing.*;

public class UserDashboard extends JFrame {
    private CardLayout cardLayout; // Khai báo CardLayout để quản lý các trang
    private JPanel mainPanel; // Panel chính chứa các trang

    public UserDashboard() {
        // Đặt tiêu đề cho cửa sổ
        setTitle("User Dashboard - Chat App");

        // Đặt kích thước cho cửa sổ
        setSize(800, 600);

        // Đặt hành động khi đóng cửa sổ là thoát chương trình
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Đặt vị trí cửa sổ ở giữa màn hình
        setLocationRelativeTo(null);

        // Khởi tạo CardLayout và JPanel chính
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Tạo giao diện trang chính
        JPanel homePanel = createHomePanel();

        // Tạo các giao diện LoginGUI và RegisterGUI
        LoginGUI loginGUI = new LoginGUI();
        RegisterGUI registerGUI = new RegisterGUI();

        // Thêm các trang vào CardLayout
        mainPanel.add(homePanel, "Home"); // Trang chính
        mainPanel.add(loginGUI.getPanel(), "Login"); // Trang đăng nhập
        mainPanel.add(registerGUI.getPanel(), "Register"); // Trang đăng ký

        // Thêm mainPanel vào JFrame
        add(mainPanel);

        // Hiển thị cửa sổ JFrame
        setVisible(true);
    }

    // Tạo giao diện trang chính với các nút "Register" và "Login"
    private JPanel createHomePanel() {
        JPanel panel = new JPanel(new GridBagLayout());

        // Tạo nút "Register"
        JButton btnRegister = new JButton("Register");
        btnRegister.setFocusPainted(false);
        btnRegister.setBorderPainted(true);
        btnRegister.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnRegister.setPreferredSize(new Dimension(400, 100));

        // Tạo nút "Login"
        JButton btnLogin = new JButton("Login");
        btnLogin.setFocusPainted(false);
        btnLogin.setBorderPainted(true);
        btnLogin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnLogin.setPreferredSize(new Dimension(400, 100));

        // Thiết lập GridBagConstraints để bố trí các thành phần
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;

        // Thêm nút "Register" vào panel tại vị trí dòng 0, cột 0
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(btnRegister, gbc);

        // Thêm nút "Login" vào panel tại vị trí dòng 1, cột 0
        gbc.gridy = 1;
        panel.add(btnLogin, gbc);

        // Thiết lập hành động cho nút "Register" để chuyển sang giao diện đăng ký
        btnRegister.addActionListener(e -> cardLayout.show(mainPanel, "Register"));

        // Thiết lập hành động cho nút "Login" để chuyển sang giao diện đăng nhập
        btnLogin.addActionListener(e -> cardLayout.show(mainPanel, "Login"));

        return panel;
    }

    // Phương thức main để chạy chương trình
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UserDashboard()); // Khởi tạo cửa sổ UserDashboard
    }
}
