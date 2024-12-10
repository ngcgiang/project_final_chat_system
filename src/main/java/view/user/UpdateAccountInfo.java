package view.user;

import components.shared.utils.*;
import components.user.*;
import java.awt.*;
import java.util.*;
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

        JLabel lblAddress = Utilities.createLabel("Address", "bold", 16);
        JTextArea txtAddress = new JTextArea();
        txtAddress.setLineWrap(true); // Tự động ngắt dòng khi quá dài
        txtAddress.setWrapStyleWord(true); // Ngắt dòng tại từ, không phải giữa các ký tự
        txtAddress.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        // JLabel
        JLabel lblDob = Utilities.createLabel("Day Of Birth", "bold", 16);

        // JComboBox cho ngày
        JComboBox<Integer> dayComboBox = new JComboBox<>();
        for (int i = 1; i <= 31; i++) {
            dayComboBox.addItem(i);
        }

        // JComboBox cho tháng
        JComboBox<String> monthComboBox = new JComboBox<>(new String[] {
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        });

        // JComboBox cho năm
        JComboBox<Integer> yearComboBox = new JComboBox<>();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = currentYear - 50; i <= currentYear; i++) {
            yearComboBox.addItem(i);
        }

        JPanel dobPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        dobPanel.add(dayComboBox);
        dobPanel.add(monthComboBox);
        dobPanel.add(yearComboBox);

        JLabel lblEmail = Utilities.createLabel("Email", "bold", 16);
        JTextField txtEmail = new JTextField(20);

        JLabel lblPhone = Utilities.createLabel("Phone", "bold", 16);
        JTextField txtPhone = new JTextField(20);

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
        panel.add(lblAddress, gbc);

        gbc.gridx = 1;
        panel.add(txtAddress, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(lblDob, gbc);

        gbc.gridx = 1;
        panel.add(dobPanel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(lblEmail, gbc);

        gbc.gridx = 1;
        panel.add(txtEmail, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(lblPhone, gbc);

        gbc.gridx = 1;
        panel.add(txtPhone, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(lblGender, gbc);

        gbc.gridx = 1;
        panel.add(cbGender, gbc);

        // Nút "Save" để lưu thông tin
        JButton btnSave = Utilities.createButton("Save");

        gbc.gridx = 1;
        gbc.gridy = 6;
        panel.add(btnSave, gbc);

        // Lấy username của người dùng hiện tại
        String username = CurrentUser.getInstance().getUsername(); // Replace with the actual username

        // Gọi UserBus để thực hiện hiển thị và cập nhật
        UserBUS userBUS = new UserBUS();

        // Lấy thông tin
        UserDTO userDTO = userBUS.getAccountInfo(username);

        // Hiển thị thông tin vào các trường
        if (userDTO != null) {
            // Cập nhật tên
            txtName.setText(userDTO.getFullName());

            // Cập nhật địa chỉ
            txtAddress.setText(userDTO.getAddress());

            // Cập nhật email
            txtEmail.setText(userDTO.getEmail());

            // Cập nhật phone
            txtPhone.setText(userDTO.getPhone());

            // Cập nhật giới tính
            cbGender.setSelectedItem(userDTO.getGender());

            // Cập nhật ngày sinh
            java.util.Date dob = userDTO.getDob();
            if (dob != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(dob);

                // Lấy ngày, tháng, năm từ Date
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH); // Tháng bắt đầu từ 0, cần cộng 1
                int year = calendar.get(Calendar.YEAR);

                // Chọn ngày, tháng, năm trong JComboBox
                dayComboBox.setSelectedItem(day);
                monthComboBox.setSelectedIndex(month); // Lưu ý tháng trong ComboBox bắt đầu từ 0
                yearComboBox.setSelectedItem(year);
            }
        }

        btnSave.addActionListener(e -> {
            // Lấy dữ liệu từ giao diện
            String fullName = txtName.getText().trim();
            String address = txtAddress.getText().trim();
            java.util.Date dob = new GregorianCalendar(
                    (int) yearComboBox.getSelectedItem(),
                    monthComboBox.getSelectedIndex(),
                    (int) dayComboBox.getSelectedItem()).getTime();
            String email = txtEmail.getText().trim();
            String phone = txtPhone.getText().trim();
            String gender = (String) cbGender.getSelectedItem();

            // Thực hiện cập nhật thông tin
            Response result = userBUS.updateAccountInfo(username, fullName, address, dob, email, phone, gender);

            if (result.isSuccess()) {
                JOptionPane.showMessageDialog(panel, result.getMessage());
                // Chuyển giao diện về "Management"
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
