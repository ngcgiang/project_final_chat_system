import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;

public class AdminDashboard extends JFrame {
    public AdminDashboard() {
        // Setting main windows
        setTitle("Admin Dashboard - Chat App");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.LIGHT_GRAY);
        headerPanel.setPreferredSize(new Dimension(800, 80));

        // Logo web
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setBackground(Color.LIGHT_GRAY);

        // Logo web
        ImageIcon webLogoIcon = new ImageIcon("..\\img\\webLogo.jpg");
        Image scaledWebLogo = webLogoIcon.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
        JLabel webLogoLabel = new JLabel(new ImageIcon(scaledWebLogo)); // Tạo JLabel từ ImageIcon đã chỉnh kích thước
        leftPanel.add(webLogoLabel);

        // Date
        JLabel dateLabel = new JLabel(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        leftPanel.add(dateLabel);

        headerPanel.add(leftPanel, BorderLayout.WEST);

        // Admin information
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setBackground(Color.LIGHT_GRAY);

        // Admin name
        JLabel adminNameLabel = new JLabel("ngcjang");
        adminNameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        rightPanel.add(adminNameLabel);

        // Setting logo admin also button
        ImageIcon adminLogoIcon = new ImageIcon(".\\img\\profile-user.png");
        Image scaledAdminLogo = adminLogoIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        JButton adminButton = new JButton(new ImageIcon(scaledAdminLogo));
        adminButton.setFocusPainted(false);
        adminButton.setContentAreaFilled(false);
        adminButton.setBorderPainted(false);

        // Add event handle for admin button
        adminButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Mở cài đặt"));
        rightPanel.add(adminButton);

        headerPanel.add(rightPanel, BorderLayout.EAST);

        // Container chính cho các nút quản lý
        JPanel mainContainer = new JPanel();
        mainContainer.setLayout(new GridLayout(4, 2, 10, 10));
        mainContainer.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Các nút chức năng
        JButton userManagementButton = new JButton("User Management");
        JButton loginManagementButton = new JButton("Login Management");
        JButton groupChatManagementButton = new JButton("Group Chat Management");
        JButton reportManagementButton = new JButton("Report Management");
        JButton newUserManagementButton = new JButton("New-User Management");
        JButton userOnlineManagementButton = new JButton("User Online Management");

        // Thêm các nút vào container
        mainContainer.add(userManagementButton);
        mainContainer.add(loginManagementButton);
        mainContainer.add(groupChatManagementButton);
        mainContainer.add(reportManagementButton);
        mainContainer.add(newUserManagementButton);
        mainContainer.add(userOnlineManagementButton);

        // Add header to main window
        add(headerPanel, BorderLayout.NORTH);
        // Add container to main window
        add(mainContainer, BorderLayout.CENTER);
        // Hiển thị cửa sổ
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminDashboard());
    }
}
