import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class SpamReportManagement extends JPanel {
    private JTable reportTable;
    private DefaultTableModel tableModel;
    private JTextField usernameFilterField;
    private JComboBox<String> timeFilterComboBox;
    private JComboBox<String> sortComboBox;
    private JButton applyFilterButton;
    private JButton lockAccountButton;
    private JButton backButton;

    public SpamReportManagement() {
        // Main window setup
        setLayout(new BorderLayout());

        // Header panel for the title
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(Color.LIGHT_GRAY);
        JLabel titleLabel = new JLabel("Spam Report Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        headerPanel.add(titleLabel);

        // Filter and Sort panel
        JPanel filterSortPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        // Sorting Options
        sortComboBox = new JComboBox<>(new String[]{"Sort by Time", "Sort by Username"});
        filterSortPanel.add(new JLabel("Sort by:"));
        filterSortPanel.add(sortComboBox);

        // Filter by Time
        timeFilterComboBox = new JComboBox<>(new String[]{"All", "Today", "Last 7 Days", "Last 30 Days"});
        filterSortPanel.add(new JLabel("Filter by Time:"));
        filterSortPanel.add(timeFilterComboBox);

        // Filter by Username
        usernameFilterField = new JTextField(15);
        filterSortPanel.add(new JLabel("Filter by Username:"));
        filterSortPanel.add(usernameFilterField);

        // Apply Filter Button
        applyFilterButton = new JButton("Apply Filter");
        filterSortPanel.add(applyFilterButton);

        // Combine header and filter panels into a north container
        JPanel northPanel = new JPanel();
        northPanel.setLayout(new BorderLayout());
        northPanel.add(headerPanel, BorderLayout.NORTH);
        northPanel.add(filterSortPanel, BorderLayout.CENTER);

        add(northPanel, BorderLayout.NORTH);

        // Table for displaying spam reports
        tableModel = new DefaultTableModel(new Object[]{"User ID", "Username", "Reported by", "Report Time", "Reason", "Access"}, 0);
        reportTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(reportTable);
        add(scrollPane, BorderLayout.CENTER);

        // Button panel for locking accounts
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backButton = new JButton("BACK");
        lockAccountButton = new JButton("Lock/Unlock Account");
        buttonPanel.add(backButton);
        buttonPanel.add(lockAccountButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // Add ActionListener for lockButton
        lockAccountButton.addActionListener(e -> {
            String username = usernameFilterField.getText();
            String sortBy = sortComboBox.getSelectedItem().toString();
            String time = timeFilterComboBox.getSelectedItem().toString();
            int selectedRow = reportTable.getSelectedRow();
        
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
                    loadDataFromDatabase(sortBy, time, username);
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update user access.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error updating user access: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        //Add actionListener apply filter
        applyFilterButton.addActionListener(e->{

            String username = usernameFilterField.getText();
            String sortBy = sortComboBox.getSelectedItem().toString();
            String time = timeFilterComboBox.getSelectedItem().toString();

            loadDataFromDatabase(sortBy, time, username);
        });

        //Add actionListener sort by
        sortComboBox.addActionListener(e -> {
            String username = usernameFilterField.getText();
            String sortBy = sortComboBox.getSelectedItem().toString();
            String time = timeFilterComboBox.getSelectedItem().toString();

            loadDataFromDatabase(sortBy, time, username);
        });

        //Add actionListener apply time
        timeFilterComboBox.addActionListener(e ->{
            String username = usernameFilterField.getText();
            String sortBy = sortComboBox.getSelectedItem().toString();
            String time = timeFilterComboBox.getSelectedItem().toString();

            loadDataFromDatabase(sortBy, time, username);
        });

        loadDataFromDatabase();

    }

    private void loadDataFromDatabase(String sortBy, String time, String username) {
        // Base query
        String query = """
                SELECT u.UserID, u.Username, rp.UserID AS ReporterID, sp.ReportTime, sp.Reason, u.Access
                FROM spam_reports sp
                INNER JOIN Users u ON u.UserID = sp.UserID
                INNER JOIN Users rp ON rp.UserID = sp.ReportedBy
                WHERE 1=1
            """;
    
        // Append WHERE clause for username search
        if (username != null && !username.isEmpty()) {
            query += " AND u.Username LIKE ?";
        }
    
        // Append WHERE clause for time filter
        switch (time) {
            case "Today":
                query += " AND sp.ReportTime >= CURDATE()";
                break;
            case "Last 7 Days":
                query += " AND sp.ReportTime >= DATE_SUB(CURDATE(), INTERVAL 7 DAY)";
                break;
            case "Last 30 Days":
                query += " AND sp.ReportTime >= DATE_SUB(CURDATE(), INTERVAL 30 DAY)";
                break;
            case "All":
            default:
                // No additional condition for "All"
                break;
        }
    
        // Append ORDER BY clause for sorting
        if (sortBy != null && !sortBy.isEmpty()) {
            switch (sortBy) {
                case "Sort by Time":
                    query += " ORDER BY sp.ReportTime DESC";
                    break;
                case "Sort by Username":
                    query += " ORDER BY u.Username";
                    break;
                default:
                    throw new IllegalArgumentException("Invalid sortBy value: " + sortBy);
            }
        }
    
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
    
            // Set parameters for username search
            int paramIndex = 1;
            if (username != null && !username.isEmpty()) {
                preparedStatement.setString(paramIndex++, "%" + username + "%");
            }
    
            // Execute query and fetch results
            ResultSet resultSet = preparedStatement.executeQuery();
    
            // Clear the table before adding new data
            tableModel.setRowCount(0);
    
            // Populate table with data from ResultSet
            while (resultSet.next()) {
                int userID = resultSet.getInt("UserID");
                String userName = resultSet.getString("Username");
                int reportedBy = resultSet.getInt("ReporterID");
                Timestamp reportTime = resultSet.getTimestamp("ReportTime");
                String reason = resultSet.getString("Reason");
                String status = resultSet.getString("Access");
    
                tableModel.addRow(new Object[]{userID, userName, reportedBy, reportTime, reason, status});
            }
    
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading data from database: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void loadDataFromDatabase() {
        // Base query
        String query = """
                SELECT u.UserID, u.Username, rp.UserID AS ReporterID, sp.ReportTime, sp.Reason, u.Access
                FROM spam_reports sp
                INNER JOIN Users u ON u.UserID = sp.UserID
                INNER JOIN Users rp ON rp.UserID = sp.ReportedBy
                ORDER BY sp.ReportTime DESC;
            """;
    
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Execute query and fetch results
            ResultSet resultSet = preparedStatement.executeQuery();
    
            // Clear the table before adding new data
            tableModel.setRowCount(0);
    
            // Populate table with data from ResultSet
            while (resultSet.next()) {
                int userID = resultSet.getInt("UserID");
                String username = resultSet.getString("Username");
                int reportedBy = resultSet.getInt("ReporterID");
                Timestamp reportTime = resultSet.getTimestamp("ReportTime");
                String reason = resultSet.getString("Reason");
                String status = resultSet.getString("Access");

                tableModel.addRow(new Object[]{userID, username, reportedBy, reportTime, reason, status});
            }
    
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading data from database: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public JButton getBackButton() {
        return backButton;
    }
}
