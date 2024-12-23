package view.admin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class AdminDashboard extends JFrame {
    private JPanel headerPanel;
    private JPanel logoutJPanel;

    public AdminDashboard() {
        // Setting main windows
        setTitle("Admin Dashboard - Chat App");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header
        headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.LIGHT_GRAY);
        headerPanel.setPreferredSize(new Dimension(800, 30));

        // Logo web
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setBackground(Color.LIGHT_GRAY);

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

        headerPanel.add(rightPanel, BorderLayout.EAST);

        // Container chính cho các nút quản lý
        JPanel mainContainer = new JPanel();
        mainContainer.setLayout(new GridLayout(3, 2, 10, 10));
        mainContainer.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Các nút chức năng
        JButton userManagementButton = new JButton("User Management");
        JButton loginManagementButton = new JButton("Login Management");
        JButton groupChatManagementButton = new JButton("Group Chat Management");
        JButton reportManagementButton = new JButton("Report Management");
        JButton newUserManagementButton = new JButton("New-User Management");
        JButton userFriendListButton = new JButton("User Friend List Management");
        JButton userOnlineManagementButton = new JButton("User Online Management");

        // Thêm các nút vào container
        mainContainer.add(userManagementButton);
        mainContainer.add(loginManagementButton);
        mainContainer.add(groupChatManagementButton);
        mainContainer.add(reportManagementButton);
        mainContainer.add(newUserManagementButton);
        mainContainer.add(userFriendListButton);
        mainContainer.add(userOnlineManagementButton);

        // Add the logout button to the bottom-right corner of the main container
        logoutJPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton logoutButton = new JButton("Logout");

        logoutJPanel.add(logoutButton);

        add(logoutJPanel, BorderLayout.SOUTH);

        // handle event click button
        // to user management
        userManagementButton.addActionListener(e -> {
            UserManagement userManagementPanel = new UserManagement();
            userManagementPanel.reloadUserData(null, null, "UserID");
            userManagementPanel.getBackButton().addActionListener(event -> switchPanel(mainContainer));
            switchPanel(userManagementPanel);
        });
        // to login management
        loginManagementButton.addActionListener(e -> {
            LoginManagement loginManagementPanel = new LoginManagement();
            loginManagementPanel.getBackButton().addActionListener(event -> switchPanel(mainContainer));
            switchPanel(loginManagementPanel);
        });
        // to group chat management
        groupChatManagementButton.addActionListener(e -> {
            GroupChatManagement groupChatManagementPanel = new GroupChatManagement(this);
            groupChatManagementPanel.getBackButton().addActionListener(event -> switchPanel(mainContainer));
            switchPanel(groupChatManagementPanel);
        });
        // to report management
        reportManagementButton.addActionListener(e -> {
            SpamReportManagement spamReportManagementPanel = new SpamReportManagement();
            spamReportManagementPanel.getBackButton().addActionListener(event -> switchPanel(mainContainer));
            switchPanel(spamReportManagementPanel);
        });
        // to new user management
        newUserManagementButton.addActionListener(e -> {
            NewUserManagement newUserManagementPanel = new NewUserManagement(this);
            newUserManagementPanel.getBackButton().addActionListener(event -> switchPanel(mainContainer));
            switchPanel(newUserManagementPanel);
        });
        userFriendListButton.addActionListener(e -> {
            UserFriendListManagement userFriendListManagementPanel = new UserFriendListManagement();
            userFriendListManagementPanel.getBackButton().addActionListener(event -> switchPanel(mainContainer));
            switchPanel(userFriendListManagementPanel);
        });
        userOnlineManagementButton.addActionListener(e -> {
            UserOnlineManagement userOnlineManagementPanel = new UserOnlineManagement(this);
            userOnlineManagementPanel.getBackButton().addActionListener(event -> switchPanel(mainContainer));
            switchPanel(userOnlineManagementPanel);
        });
        // Handle logout button click
        logoutButton.addActionListener(e -> {
            // Logic to handle logout, e.g., redirect to login screen
            JFrame topLevelFrame = (JFrame) SwingUtilities.getWindowAncestor(mainContainer);
            topLevelFrame.dispose(); // Close the current window
            // Open the login screen (assuming you have a Login class)
            new LoginFrame().setVisible(true);
        });

        // Add header to main window
        add(headerPanel, BorderLayout.NORTH);
        // Add container to main window
        add(mainContainer, BorderLayout.CENTER);
        // Hiển thị cửa sổ
        setVisible(true);
    }

    // Switch panel
    private void switchPanel(JPanel newPanel) {
        getContentPane().removeAll();
        add(headerPanel, BorderLayout.NORTH); // Giữ header
        add(newPanel, BorderLayout.CENTER);
        if (newPanel instanceof JPanel && newPanel.getLayout() instanceof GridLayout) {
            add(logoutJPanel, BorderLayout.SOUTH);
        }
        revalidate();
        repaint();
    }

    // public static void main(String[] args) {
    // SwingUtilities.invokeLater(() -> new AdminDashboard());
    // }

}