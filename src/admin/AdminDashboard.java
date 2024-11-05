package admin;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;

public class AdminDashboard extends JFrame {
    public AdminDashboard() {
        // Cài đặt cửa sổ chính
        setTitle("Admin Dashboard - Chat App");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.LIGHT_GRAY);
        headerPanel.setPreferredSize(new Dimension(800, 80));

        // Logo web và ngày tháng bên trái header
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setBackground(Color.LIGHT_GRAY);

        // Logo web
        ImageIcon webLogoIcon = new ImageIcon("D:\\studies\\3rdyear\\java\\app_final\\img\\webLogo.jpg");
        Image scaledWebLogo = webLogoIcon.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
        JLabel webLogoLabel = new JLabel(new ImageIcon(scaledWebLogo)); // Tạo JLabel từ ImageIcon đã chỉnh kích thước
        leftPanel.add(webLogoLabel);

        // Ngày tháng bên cạnh logo web
        JLabel dateLabel = new JLabel(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        leftPanel.add(dateLabel);

        headerPanel.add(leftPanel, BorderLayout.WEST);

        // Phần thông tin admin bên phải header
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setBackground(Color.LIGHT_GRAY);

        // Tên admin bên trái logo admin
        JLabel adminNameLabel = new JLabel("ngcjang");
        adminNameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        rightPanel.add(adminNameLabel);

        // Nút logo admin
        ImageIcon adminLogoIcon = new ImageIcon("D:\\studies\\3rdyear\\java\\app_final\\img\\profile-user.png");
        Image scaledAdminLogo = adminLogoIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        JButton adminButton = new JButton(new ImageIcon(scaledAdminLogo));
        adminButton.setFocusPainted(false);
        adminButton.setContentAreaFilled(false);
        adminButton.setBorderPainted(false);

        // Thêm sự kiện cho nút logo admin
        adminButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Mở cài đặt"));
        rightPanel.add(adminButton);

        headerPanel.add(rightPanel, BorderLayout.EAST);

        // Thêm header vào cửa sổ chính
        add(headerPanel, BorderLayout.NORTH);

        // Hiển thị cửa sổ
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminDashboard());
    }
}
