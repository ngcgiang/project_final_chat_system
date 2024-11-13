package user;

import java.awt.*;
import javax.swing.*;

public class Management {
    private JPanel panel;

    public Management() {
        panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Khoảng cách giữa các button
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;

        // Thanh header với BorderLayout
        JPanel header = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnLogOut = Utilities.createButton("Log out");
        header.add(btnLogOut); // Đặt btnLogOut ở góc phải của header

        // Đặt header vào GridBagLayout
        gbc.gridx = 0; // Cột đầu tiên
        gbc.gridy = 0; // Hàng đầu tiên
        gbc.gridwidth = 2;
        panel.add(header, gbc);

        int width = 400, height = 75;
        JButton btnUpdateAccountInfo = Utilities.createButton("Update Account Info", width, height);
        JButton btnResetPassword = Utilities.createButton("Reset Password", width, height);
        JButton btnUpdatePassword = Utilities.createButton("Update Password", width, height);
        JButton btnFriendList = Utilities.createButton("Friend List", width, height);
        JButton btnMessage = Utilities.createButton("Message", width, height);
        JButton btnFindUser = Utilities.createButton("Find user", width, height);

        // Các button tiếp theo
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(btnMessage, gbc);

        gbc.gridy = 2;
        panel.add(btnFindUser, gbc);

        gbc.gridy = 3;
        panel.add(btnFriendList, gbc);

        gbc.gridy = 4;
        panel.add(btnResetPassword, gbc);

        gbc.gridy = 5;
        panel.add(btnUpdatePassword, gbc);

        gbc.gridy = 6;
        panel.add(btnUpdateAccountInfo, gbc);

        // Thiết lập hành động khi nhấn nút "Update Account Info"
        btnUpdateAccountInfo.addActionListener(e -> {
            // Tìm JFrame chứa "Management" và yêu cầu chuyển đổi sang giao diện "Update
            // Account Info"
            Container topLevel = panel.getTopLevelAncestor();
            if (topLevel instanceof UserDashboard) {
                ((UserDashboard) topLevel).switchPanel("Update Account Info");
            }
        });

        // Thiết lập hành động khi nhấn nút "Reset Password"
        btnResetPassword.addActionListener(e -> {
            // Tìm JFrame chứa "Management" và yêu cầu chuyển đổi sang giao diện "Reset
            // Password"
            Container topLevel = panel.getTopLevelAncestor();
            if (topLevel instanceof UserDashboard) {
                ((UserDashboard) topLevel).switchPanel("Reset Password");
            }
        });

        // Thiết lập hành động khi nhấn nút "Update Password"
        btnUpdatePassword.addActionListener(e -> {
            // Tìm JFrame chứa "Management" và yêu cầu chuyển đổi sang giao diện "Update
            // Password"
            Container topLevel = panel.getTopLevelAncestor();
            if (topLevel instanceof UserDashboard) {
                ((UserDashboard) topLevel).switchPanel("Update Password");
            }
        });

        // Thiết lập hành động khi nhấn nút "Friend List"
        btnFriendList.addActionListener(e -> {
            // Tìm JFrame chứa "Management" và yêu cầu chuyển đổi sang giao diện "Friend
            // List"
            Container topLevel = panel.getTopLevelAncestor();
            if (topLevel instanceof UserDashboard) {
                ((UserDashboard) topLevel).switchPanel("Friend List");
            }
        });

        // Thiết lập hành động khi nhấn nút "Message"
        btnMessage.addActionListener(e -> {
            // Tìm JFrame chứa "Management" và yêu cầu chuyển đổi sang giao diện "Message"
            Container topLevel = panel.getTopLevelAncestor();
            if (topLevel instanceof UserDashboard) {
                ((UserDashboard) topLevel).switchPanel("Message");
            }
        });

        // Thiết lập hành động khi nhấn nút "Find User"
        btnFindUser.addActionListener(e -> {
            // Tìm JFrame chứa "Management" và yêu cầu chuyển đổi sang giao diện "Find User"
            Container topLevel = panel.getTopLevelAncestor();
            if (topLevel instanceof UserDashboard) {
                ((UserDashboard) topLevel).switchPanel("Find User");
            }
        });

        // Thiết lập hành động khi nhấn nút "Log out"
        btnLogOut.addActionListener(e -> {
            // Tìm JFrame chứa "Management" và yêu cầu chuyển đổi sang giao diện "Login"
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
