package view.user;

import components.shared.utils.*;
import components.user.UserBUS;
import java.awt.*;
import javax.swing.*;

public class ResetPassword {
    private JPanel panel;

    public ResetPassword() {
        panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Tạo các thành phần hiển thị
        JLabel lblMessage = Utilities.createLabel("Your New Account Password", "bold", 16);

        // Tạo mật khẩu ngẫu nhiên
        String newPassword = GeneratePassword.generateRandomPassword();

        // Hiển thị mật khẩu ngẫu nhiên
        JTextField txtNewPassword = new JTextField(newPassword);
        txtNewPassword.setFont(new Font("Arial", Font.PLAIN, 14));
        txtNewPassword.setEditable(false); // Không cho phép chỉnh sửa
        txtNewPassword.setHorizontalAlignment(JTextField.CENTER); // Căn giữa văn bản

        // Thêm các thành phần vào panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(lblMessage, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(txtNewPassword, gbc);

        // Nút "Back" để quay lại giao diện "Management"
        JButton btnBack = Utilities.createButton("Back");

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panel.add(btnBack, gbc);

        // Cập nhật mật khẩu mới trong database
        String username = CurrentUser.getInstance().getUsername();
        UserBUS userBUS = new UserBUS();
        userBUS.resetPassword(username, newPassword);

        // Thiết lập hành động khi nhấn nút "Back"
        btnBack.addActionListener(e -> {
            // Tìm JFrame chứa "ResetPassword" và yêu cầu chuyển đổi sang giao diện
            // "Management"
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
