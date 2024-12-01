package view.user;

import java.awt.*;
import javax.swing.*;

public class UpdateAccountInfo {
    private JPanel panel;

    public UpdateAccountInfo() {
        panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Thêm các label và text field cho các thông tin cập nhật
        JLabel lblName = Utilities.createLabel("Full Name", "bold", 16);
        JTextField txtName = new JTextField(20);

        JLabel lblEmail = Utilities.createLabel("Email", "bold", 16);
        JTextField txtEmail = new JTextField(20);

        JLabel lblPhone = Utilities.createLabel("Phone", "bold", 16);
        JTextField txtPhone = new JTextField(20);

        JLabel lblAge = Utilities.createLabel("Age", "bold", 16);
        JTextField txtAge = new JTextField(20);

        JLabel lblGender = Utilities.createLabel("Gender", "bold", 16);
        JComboBox<String> cbGender = new JComboBox<>(new String[] { "Male", "Female", "Other" });

        // Sắp xếp các component
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(lblName, gbc);

        gbc.gridx = 1;
        panel.add(txtName, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(lblEmail, gbc);

        gbc.gridx = 1;
        panel.add(txtEmail, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(lblPhone, gbc);

        gbc.gridx = 1;
        panel.add(txtPhone, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(lblAge, gbc);

        gbc.gridx = 1;
        panel.add(txtAge, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(lblGender, gbc);

        gbc.gridx = 1;
        panel.add(cbGender, gbc);

        // Nút "Save" để lưu thông tin
        JButton btnSave = Utilities.createButton("Save");

        gbc.gridx = 1;
        gbc.gridy = 5;
        panel.add(btnSave, gbc);

        // Thiết lập hành động khi nhấn nút "Save"
        btnSave.addActionListener(e -> {
            // Tìm JFrame chứa "Update Account Info" và yêu cầu chuyển đổi sang giao diện
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
