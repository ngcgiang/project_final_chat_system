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

        // Tạo các button và thiết lập kiểu dáng giống UserDashboard
        int width = 400, height = 75;
        JButton btnUpdateAccountInfo = Utilities.createButton("Update Account Info", width, height);
        JButton btnResetPassword = Utilities.createButton("Reset Password", width, height);
        JButton btnUpdatePassword = Utilities.createButton("Update Password", width, height);
        JButton btnFriendList = Utilities.createButton("Friend List", width, height);
        JButton btnReportSpam = Utilities.createButton("Report Spam", width, height);
        JButton btnChat = Utilities.createButton("Chat", width, height);

        // Thêm các button vào panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(btnUpdateAccountInfo, gbc);

        gbc.gridy = 1;
        panel.add(btnResetPassword, gbc);

        gbc.gridy = 2;
        panel.add(btnUpdatePassword, gbc);

        gbc.gridy = 3;
        panel.add(btnFriendList, gbc);

        gbc.gridy = 4;
        panel.add(btnReportSpam, gbc);

        gbc.gridy = 5;
        panel.add(btnChat, gbc);

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
    }

    public JPanel getPanel() {
        return panel;
    }
}
