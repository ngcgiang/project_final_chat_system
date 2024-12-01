import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class LoginManagement extends JPanel {
    private JTable historyLoginTable;
    private DefaultTableModel tableModel;
    private JButton backButton;

    public LoginManagement() {
        // Container
        setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.LIGHT_GRAY);
        headerPanel.setPreferredSize(new Dimension(800, 40));

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setBackground(Color.LIGHT_GRAY);

        // Label
        JLabel loginHisLabel = new JLabel("Login history management");
        loginHisLabel.setFont(new Font("Arial", Font.BOLD, 16));
        leftPanel.add(loginHisLabel);
        headerPanel.add(leftPanel, BorderLayout.WEST);

        add(headerPanel, BorderLayout.NORTH); // Add header to frame

        // Table for Login History
        tableModel = new DefaultTableModel(new Object[]{"Login Time", "User ID", "User-name", "Full-name"}, 0);
        historyLoginTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(historyLoginTable);
        add(scrollPane, BorderLayout.CENTER);

        // Back button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backButton = new JButton("BACK");
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Load data from database
        loadLoginHistoryData();

        setVisible(true);
    }

    /**
     * Loads login history data from the database and populates the table.
     */
    private void loadLoginHistoryData() {
        String query = """
                SELECT ua.LoginTime, u.UserID, u.Username, u.FullName
                FROM UserActivities ua
                JOIN Users u ON ua.UserID = u.UserID
                WHERE ua.ActivityType = 'Login'
                ORDER BY ua.LoginTime DESC
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            // Clear existing data in the table
            tableModel.setRowCount(0);

            // Add rows to the table from the result set
            while (resultSet.next()) {
                String loginTime = resultSet.getString("LoginTime");
                String userId = resultSet.getString("UserID");
                String username = resultSet.getString("Username");
                String fullName = resultSet.getString("FullName");

                tableModel.addRow(new Object[]{loginTime, userId, username, fullName});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading login history: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public LoginManagement(int userId) {
        // Container
        setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.LIGHT_GRAY);
        headerPanel.setPreferredSize(new Dimension(800, 40));

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setBackground(Color.LIGHT_GRAY);

        // Label
        JLabel loginHisLabel = new JLabel("Login history management");
        loginHisLabel.setFont(new Font("Arial", Font.BOLD, 16));
        leftPanel.add(loginHisLabel);
        headerPanel.add(leftPanel, BorderLayout.WEST);

        add(headerPanel, BorderLayout.NORTH); // Add header to frame

        // Table for Login History
        tableModel = new DefaultTableModel(new Object[]{"Login Time", "Logout Time", "Activity Type"}, 0);
        historyLoginTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(historyLoginTable);
        add(scrollPane, BorderLayout.CENTER);

        // Back button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backButton = new JButton("BACK");
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Load data from database
        loadLoginHistoryData(userId);

        // Add ActionListener for Back button
        backButton.addActionListener(e -> {
            Window parentWindow = SwingUtilities.getWindowAncestor(this);
            if (parentWindow instanceof JFrame) {
                ((JFrame) parentWindow).dispose(); // Close the JFrame
            }
        });

        setVisible(true);
    }

    /**
     * Loads login history data for a specific user from the database and populates the table.
     *
     * @param userId The ID of the user to filter login history.
     */
    private void loadLoginHistoryData(int userId) {
        String query = """
                SELECT LoginTime, LogoutTime, ActivityType
                FROM UserActivities
                WHERE UserID = ?
                ORDER BY LoginTime DESC
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                // Clear existing data in the table
                tableModel.setRowCount(0);

                // Add rows to the table from the result set
                while (resultSet.next()) {
                    String loginTime = resultSet.getString("LoginTime");
                    String logoutTime = resultSet.getString("LogoutTime");
                    String activityType = resultSet.getString("ActivityType");

                    tableModel.addRow(new Object[]{loginTime, logoutTime, activityType});
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading login history: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public JButton getBackButton() {
        return backButton;
    }
}
