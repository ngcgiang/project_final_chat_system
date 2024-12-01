import java.awt.*;
import java.sql.*;
import java.util.Date;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class UserManagement extends JPanel {
    private JTable userTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JComboBox<String> filterComboBox;
    private JButton searchButton;
    private JButton backButton;

    public UserManagement() {
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
            loadUserData(searchValue, filterColumn);
        });

        // Add ActionListener for the update button
        updateButton.addActionListener(e -> {
            int selectedRow = userTable.getSelectedRow();
        
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a user to update.", "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }
        
            // Lấy dữ liệu hiện tại từ bảng
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
                try {
                    // Lấy dữ liệu mới từ hộp thoại
                    fullName = dialog.getFullName();
                    address = dialog.getAddress();
                    Date newDob = dialog.getDateOfBirth();
                    gender = dialog.getGender();
                    email = dialog.getEmail();
                    status = dialog.getStatus();
        
                    // Cập nhật thông tin trong cơ sở dữ liệu
                    updateUser(userId, fullName, address, newDob, gender, email, status);
        
                    // Reload dữ liệu sau khi cập nhật
                    loadUserData(null, null);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Invalid input: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
        
            // Thực hiện câu lệnh DELETE
            String query = "DELETE FROM Users WHERE UserID = ?";
            try (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {
        
                statement.setInt(1, userId);
                int rowsDeleted = statement.executeUpdate();
        
                if (rowsDeleted > 0) {
                    JOptionPane.showMessageDialog(this, "User removed successfully.");
                    // Cập nhật lại bảng
                    loadUserData(null, null);
                } else {
                    JOptionPane.showMessageDialog(this, "No user found with the selected ID.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error removing user: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Add ActionListener for lockButton
        lockButton.addActionListener(e -> {
            int selectedRow = userTable.getSelectedRow();
        
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a user to lock/unlock.", "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }
        
            // Lấy UserID từ bảng
            int userId = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
            String currentAccess = null;
        
            // Truy vấn trạng thái Access từ cơ sở dữ liệu
            String selectQuery = "SELECT Access FROM Users WHERE UserID = ?";
            try (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement selectStatement = connection.prepareStatement(selectQuery)) {
        
                selectStatement.setInt(1, userId);
                try (ResultSet resultSet = selectStatement.executeQuery()) {
                    if (resultSet.next()) {
                        currentAccess = resultSet.getString("Access");
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error fetching user access: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        
            // Kiểm tra trạng thái Access hiện tại
            if (currentAccess == null) {
                JOptionPane.showMessageDialog(this, "User not found in database.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        
            String newAccess = currentAccess.equals("yes") ? "no" : "yes";
        
            // Xác nhận trước khi thay đổi
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to " + (newAccess.equals("no") ? "lock" : "unlock") + " this user?",
                    "Confirm Lock/Unlock",
                    JOptionPane.YES_NO_OPTION);
        
            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }
        
            // Cập nhật trạng thái Access trong cơ sở dữ liệu
            String updateQuery = "UPDATE Users SET Access = ? WHERE UserID = ?";
            try (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
        
                updateStatement.setString(1, newAccess);
                updateStatement.setInt(2, userId);
        
                int rowsUpdated = updateStatement.executeUpdate();
        
                if (rowsUpdated > 0) {
                    JOptionPane.showMessageDialog(this, "User " + (newAccess.equals("no") ? "locked" : "unlocked") + " successfully.");
                    // Làm mới bảng
                    loadUserData(null, null);
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update user access.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error updating user access: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
                    JOptionPane.PLAIN_MESSAGE);
        
            if (newPassword == null || newPassword.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Password update cancelled or empty.", "No Input", JOptionPane.WARNING_MESSAGE);
                return;
            }
        
            // Cập nhật mật khẩu trong cơ sở dữ liệu
            String updateQuery = "UPDATE Users SET Password = ? WHERE UserID = ?";
            try (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
        
                updateStatement.setString(1, newPassword);
                updateStatement.setInt(2, userId);
        
                int rowsUpdated = updateStatement.executeUpdate();
        
                if (rowsUpdated > 0) {
                    JOptionPane.showMessageDialog(this, "Password updated successfully.");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update password. User might not exist.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error updating password: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
            LoginManagement loginManagement = new LoginManagement(userId); // Truyền UserID vào constructor

            loginHistoryFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            loginHistoryFrame.setSize(800, 600);
            loginHistoryFrame.add(loginManagement);
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
        loadUserData(null, null);
    }

    public void updateUser(int userId, String fullName, String address, Date dob, String gender, String email, String status) {
        String query = "UPDATE Users SET FullName = ?, Address = ?, DateOfBirth = ?, Gender = ?, Email = ?, Status = ? WHERE UserID = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
    
            statement.setString(1, fullName);
            statement.setString(2, address);
            statement.setDate(3, new java.sql.Date(dob.getTime()));
            statement.setString(4, gender);
            statement.setString(5, email);
            statement.setString(6, status);
            statement.setInt(7, userId);
    
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("User information updated successfully.");
            } else {
                System.out.println("User not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error updating user: " + e.getMessage());
        }
    }

    public JButton getBackButton() {
        return backButton;
    }

    public void loadUserData(String searchValue, String filterColumn) {
        String query = "SELECT * FROM Users";

        // If search criteria are provided, add WHERE clause
        if (searchValue != null && !searchValue.isEmpty() && filterColumn != null) {
            query += " WHERE " + filterColumn + " LIKE ?";
        }

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            // Set the search value if provided
            if (searchValue != null && !searchValue.isEmpty() && filterColumn != null) {
                statement.setString(1, "%" + searchValue + "%");
            }

            ResultSet resultSet = statement.executeQuery();

            // Clear existing table data
            tableModel.setRowCount(0);

            // Populate table with data from ResultSet
            while (resultSet.next()) {
                tableModel.addRow(new Object[]{
                    resultSet.getString("UserID"),
                    resultSet.getString("Username"),
                    resultSet.getString("FullName"),
                    resultSet.getString("Address"),
                    resultSet.getDate("DateOfBirth"),
                    resultSet.getString("Gender"),
                    resultSet.getString("Email"),
                    resultSet.getString("Status")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage());
        }
    }
}
