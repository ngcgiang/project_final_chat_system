import components.admin.*;
import java.awt.*;
import java.util.Date;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class UserManagement extends JPanel {
    private AdminUserBUS adminUserBUS;
    private JTable userTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JComboBox<String> filterComboBox;
    private JButton searchButton;
    private JButton backButton;

    public UserManagement() {

        adminUserBUS = new AdminUserBUS();

        // Container
        setLayout(new BorderLayout());

        // Header panel for the title
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(Color.LIGHT_GRAY);
        headerPanel.setPreferredSize(new Dimension(800, 40));
        headerPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JLabel titleLabel = new JLabel("User Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        headerPanel.add(titleLabel);

        // Main Table Panel for User Information
        String[] columnNames = {"User ID", "Username", "Full-name", "Address", "DOB", "Gender", "Email", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0);
        userTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(userTable);

        // Filter Panel for Search and Filter Options
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchField = new JTextField(15);
        searchButton = new JButton("Search");
        filterPanel.add(new JLabel("Search:"));
        filterPanel.add(searchField);
        filterPanel.add(searchButton);

        filterComboBox = new JComboBox<>(new String[]{"Username", "Full-name", "Status"});
        filterPanel.add(new JLabel("Filter:"));
        filterPanel.add(filterComboBox);

        // North Panel to combine header and filter panels
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.add(headerPanel, BorderLayout.NORTH);
        northPanel.add(filterPanel, BorderLayout.CENTER);

        // Button Panel for User Actions
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backButton = new JButton("Back");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Remove");
        JButton lockButton = new JButton("Lock/Unlock");
        JButton resetPasswordButton = new JButton("Update password");
        JButton viewLoginHistoryButton = new JButton("Login history");
        JButton viewFriendsButton = new JButton("Friends list");

        buttonPanel.add(backButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(lockButton);
        buttonPanel.add(resetPasswordButton);
        buttonPanel.add(viewLoginHistoryButton);
        buttonPanel.add(viewFriendsButton);

        // Add components to main layout
        add(northPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Add ActionListener for the search button
        searchButton.addActionListener(e -> {
            String searchValue = searchField.getText(); // Get search value from text field
            String filterColumn;

            // Determine the column to filter based on ComboBox selection
            switch (filterComboBox.getSelectedItem().toString()) {
                case "Username":
                    filterColumn = "Username";
                    break;
                case "Full-name":
                    filterColumn = "FullName";
                    break;
                case "Status":
                    filterColumn = "Status";
                    break;
                default:
                    JOptionPane.showMessageDialog(this, "Invalid filter option.");
                    return;
            }

            // Reload data with search and filter criteria
            reloadUserData(searchValue, filterColumn);
        });

       // Thêm ActionListener cho nút Update
        updateButton.addActionListener(e -> {
            int selectedRow = userTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a user to update.", "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            int userId = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
            String fullName = tableModel.getValueAt(selectedRow, 2).toString();
            String address = tableModel.getValueAt(selectedRow, 3).toString();
            String dob = tableModel.getValueAt(selectedRow, 4).toString();
            String gender = tableModel.getValueAt(selectedRow, 5).toString();
            String email = tableModel.getValueAt(selectedRow, 6).toString();
            String status = tableModel.getValueAt(selectedRow, 7).toString();

            // Hiển thị hộp thoại cập nhật thông tin
            UpdateUserDialog dialog = new UpdateUserDialog((Frame) SwingUtilities.getWindowAncestor(this), fullName, address, dob, gender, email, status);
                dialog.setVisible(true);

                if (dialog.isSaved()) {
                    // Lấy dữ liệu mới từ hộp thoại
                    fullName = dialog.getFullName();
                    address = dialog.getAddress();
                    Date newDob = null;
                    try {
                        newDob = dialog.getDateOfBirth();
                    } catch (Exception e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                    gender = dialog.getGender();
                    email = dialog.getEmail();
                    status = dialog.getStatus();

                    AdminUserDTO user = new AdminUserDTO();
                    user.setUserId(userId);
                    user.setFullName(fullName);
                    user.setAddress(address);
                    user.setDateOfBirth(newDob);
                    user.setGender(gender);
                    user.setEmail(email);
                    user.setStatus(status);

                    if (adminUserBUS.updateUser(user)) {
                        reloadUserData(null, null);
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to update user.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
        
        // Add ActionListener for the remove button
        deleteButton.addActionListener(e -> {
            int selectedRow = userTable.getSelectedRow();
        
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a user to remove.", "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }
        
            // Lấy UserID từ hàng được chọn
            int userId = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
        
            // Xác nhận xoá
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to remove this user?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION);
        
            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }
        
            if (adminUserBUS.deleteUser(userId)) {
                reloadUserData(null, null);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update user.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        });

        // Add ActionListener for lockButton
        lockButton.addActionListener(e -> {
            int selectedRow = userTable.getSelectedRow();

            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(
                    this,
                    "Please select a user to lock/unlock.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            // get userid from table
            int userId = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());

            // get currently access
            String currentAccess = adminUserBUS.getUserAccess(userId);

            if (currentAccess == null) {
                JOptionPane.showMessageDialog(
                    this,
                    "User not found in database.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            String newAccess = currentAccess.equals("yes") ? "no" : "yes";

            // Confirm before changes
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to " + (newAccess.equals("no") ? "lock" : "unlock") + " this user?",
                "Confirm Lock/Unlock",
                JOptionPane.YES_NO_OPTION
            );

            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }

            boolean success = adminUserBUS.lockOrUnlockUser(userId);

            if (success) {
                JOptionPane.showMessageDialog(
                    this,
                    "User " + (newAccess.equals("no") ? "locked" : "unlocked") + " successfully."
                );

                // Làm mới dữ liệu trong bảng (giả sử `reloadUserData()` là phương thức cập nhật lại bảng)
                reloadUserData(null,null);
            } else {
                JOptionPane.showMessageDialog(
                    this,
                    "Failed to update user access.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        });

        // Add ActionListener for Update password Button
        resetPasswordButton.addActionListener(e -> {
            int selectedRow = userTable.getSelectedRow();
        
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a user to update password.", "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }
        
            // Lấy UserID từ bảng
            int userId = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
        
            // Hiển thị hộp thoại để nhập mật khẩu mới
            String newPassword = JOptionPane.showInputDialog(
                this,
                "Enter new password for UserID: " + userId,
                "Update Password",
                JOptionPane.PLAIN_MESSAGE
            );

            if (newPassword == null || newPassword.trim().isEmpty()) {
                JOptionPane.showMessageDialog(
                    this,
                    "Password update cancelled or empty.",
                    "No Input",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            // Gọi BUS để cập nhật mật khẩu
            boolean isUpdated = adminUserBUS.updatePassword(userId, newPassword);

            if (isUpdated) {
                JOptionPane.showMessageDialog(
                    this,
                    "Password updated successfully."
                );
            } else {
                JOptionPane.showMessageDialog(
                    this,
                    "Failed to update password. User might not exist.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        });

        // Add ActionListener for Login history button
        viewLoginHistoryButton.addActionListener(e -> {
            int selectedRow = userTable.getSelectedRow();

            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a user to view login history.", "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Lấy UserID của người dùng đã chọn
            int userId = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
            String username = tableModel.getValueAt(selectedRow, 1).toString();

            // Mở giao diện LoginManagement với UserID đã chọn
            JFrame loginHistoryFrame = new JFrame("Login History - " + username);
            LoginManagement loginManagement = new LoginManagement(userId); // Truyền userId vào constructor

            loginHistoryFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            loginHistoryFrame.setSize(800, 600);
            loginHistoryFrame.add(loginManagement); // Thêm giao diện LoginManagement vào JFrame
            loginHistoryFrame.setLocationRelativeTo(null);

            loginHistoryFrame.setVisible(true);
        });


        //add actionListener viewFriendsButton
        viewFriendsButton.addActionListener(e -> {
            int selectedRow = userTable.getSelectedRow();
            
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a user to view user friends list.", "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int userID = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
            String username = tableModel.getValueAt(selectedRow, 1).toString();

            JFrame friendListFrame = new JFrame("User friend list of " + username);
            UserFriendListManagement userFriendListManagement = new UserFriendListManagement(userID);
            
            friendListFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            friendListFrame.setSize(800, 600);
            friendListFrame.add(userFriendListManagement);
            friendListFrame.setLocationRelativeTo(null);
            
            friendListFrame.setVisible(true);
        });

        // Load initial data
        reloadUserData(null, null);
    }

    public JButton getBackButton() {
        return backButton;
    }

    public void reloadUserData(String searchValue, String filterColumn) {
        List<AdminUserDTO> userList = adminUserBUS.getUsers(searchValue, filterColumn);
        
        // Clear existing table data
        tableModel.setRowCount(0);

        // Populate table with data from the userList
        for (AdminUserDTO user : userList) {
            tableModel.addRow(new Object[]{
                user.getUserId(),
                user.getUsername(),
                user.getFullName(),
                user.getAddress(),
                user.getDateOfBirth(),
                user.getGender(),
                user.getEmail(),
                user.getStatus()
            });
        }
    }
}
