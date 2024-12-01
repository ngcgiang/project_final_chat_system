package view.user;

import java.awt.*;
import javax.swing.*;

public class UpdatePassword {
    private JPanel panel;
    private JPasswordField txtOldPassword, txtPassword, txtConfirmPassword;

    public UpdatePassword() {
        panel = new JPanel();
        panel.setLayout(new GridBagLayout()); // Sử dụng GridBagLayout cho bố trí linh hoạt

        // Tạo trường nhập liệu
        txtOldPassword = new JPasswordField(20);
        txtPassword = new JPasswordField(20);
        txtConfirmPassword = new JPasswordField(20);

        // Sử dụng GridBagConstraints để bố trí linh hoạt
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Khoảng cách giữa các thành phần
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; // Cột 0
        gbc.gridy = 0; // Dòng 0
        panel.add(Utilities.createLabel("Old Password", "bold", 16), gbc);

        gbc.gridx = 1; // Cột 1
        panel.add(txtOldPassword, gbc);

        gbc.gridx = 0; // Cột 0
        gbc.gridy = 1; // Dòng 1
        panel.add(Utilities.createLabel("New Password", "bold", 16), gbc);

        gbc.gridx = 1; // Cột 1
        panel.add(txtPassword, gbc);

        gbc.gridx = 0; // Cột 0
        gbc.gridy = 2; // Dòng 2
        panel.add(Utilities.createLabel("Confirm New Password", "bold", 16), gbc);

        gbc.gridx = 1; // Cột 1
        panel.add(txtConfirmPassword, gbc);

        // Nút Update
        JButton btnUpdate = Utilities.createButton("Update");

        gbc.gridx = 1; // Cột 1
        gbc.gridy = 3; // Dòng 3
        panel.add(btnUpdate, gbc);

        // Thiết lập hành động khi nhấn nút "Update"
        btnUpdate.addActionListener(e -> {
            // Tìm JFrame chứa giao diện "Update Password" và yêu cầu chuyển đổi sang giao
            // diện "Management"
            Container topLevel = panel.getTopLevelAncestor();
            if (topLevel instanceof UserDashboard) {
                ((UserDashboard) topLevel).switchPanel("Management");
            }
        });
    }

    public JPanel getPanel() {
        return panel;
    }
}