package view.user;

import components.shared.utils.*;
import components.user.UserBUS;
import java.awt.*;
import javax.swing.*;

public class UpdatePassword {
    private JPanel panel;

    public UpdatePassword() {
        panel = new JPanel();
        panel.setLayout(new GridBagLayout()); // Sử dụng GridBagLayout cho bố trí linh hoạt

        // Tạo trường nhập liệu
        JPasswordField txtOldPassword = new JPasswordField(20);
        JPasswordField txtPassword = new JPasswordField(20);
        JPasswordField txtConfirmPassword = new JPasswordField(20);

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
            char[] oldPasswordChars = txtOldPassword.getPassword();
            String oldPassword = new String(oldPasswordChars).trim();

            char[] passwordChars = txtPassword.getPassword();
            String newPassword = new String(passwordChars).trim();

            char[] confirmPasswordChars = txtConfirmPassword.getPassword();
            String confirmNewPassword = new String(confirmPasswordChars).trim();

            UserBUS userBUS = new UserBUS();
            String username = CurrentUser.getInstance().getUsername();
            Response result = userBUS.updatePassword(username, oldPassword, newPassword, confirmNewPassword);
            if (result.isSuccess()) {
                JOptionPane.showMessageDialog(panel, result.getMessage());

                Container topLevel = panel.getTopLevelAncestor();
                if (topLevel instanceof UserDashboard) {
                    ((UserDashboard) topLevel).switchPanel("Management");
                }
            } else {
                JOptionPane.showMessageDialog(panel, result.getMessage());
            }
        });
    }

    public JPanel getPanel() {
        return panel;
    }
}