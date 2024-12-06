import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class UserOnlineManagement extends JPanel {
    private JTable reportTable;
    private DefaultTableModel tableModel;
    private JTextField usernameFilterField;
    private JTextField activityCountField;
    private JComboBox<String> activityFilterComboBox;
    private JComboBox<String> timeFilterComboBox;
    private JComboBox<String> sortComboBox;
    private JButton applyFilterButton;
    private JButton backButton;
    private JButton showChartButton;
    private JFrame parentFrame;

    public UserOnlineManagement(JFrame parentFrame) {
        this.parentFrame = parentFrame; // Lưu tham chiếu đến JFrame cha
        initComponents();
    }
    
    private void initComponents() {
        // Main window setup
        setLayout(new BorderLayout());

        // Header panel for the title
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(Color.LIGHT_GRAY);
        headerPanel.setPreferredSize(new Dimension(800, 40));
        headerPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JLabel titleLabel = new JLabel("User Online Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        headerPanel.add(titleLabel);

        // Filter and Sort panel
        JPanel filterSortPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        // Sorting Options
        sortComboBox = new JComboBox<>(new String[]{"Date Creation", "Username"});
        filterSortPanel.add(new JLabel("Sort by:"));
        filterSortPanel.add(sortComboBox);

        // Filter by Date Range
        timeFilterComboBox = new JComboBox<>(new String[]{"All", "Today", "Last 7 Days", "Last 30 Days"});
        filterSortPanel.add(new JLabel("Date range:"));
        filterSortPanel.add(timeFilterComboBox);

        // Add filter for Activity Count
        activityFilterComboBox = new JComboBox<>(new String[]{"Equal to", "Greater than", "Less than"});
        activityCountField = new JTextField(3);
        filterSortPanel.add(new JLabel("Activity Count:"));
        filterSortPanel.add(activityFilterComboBox);
        filterSortPanel.add(activityCountField);

        // Filter by Username
        usernameFilterField = new JTextField(7);
        filterSortPanel.add(new JLabel("Username:"));
        filterSortPanel.add(usernameFilterField);

        // Apply Filter Button
        applyFilterButton = new JButton("Apply");
        filterSortPanel.add(applyFilterButton);

        // Combine header and filter panels into a north container
        JPanel northPanel = new JPanel();
        northPanel.setLayout(new BorderLayout());
        northPanel.add(headerPanel, BorderLayout.NORTH);
        northPanel.add(filterSortPanel, BorderLayout.CENTER);

        add(northPanel, BorderLayout.NORTH);

        // Table for displaying user information
        tableModel = new DefaultTableModel(new Object[]{"User ID", "Username", "Open app", "Chat with amount of people", "Chat with amount of group", "Date creation"}, 0);
        reportTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(reportTable);
        add(scrollPane, BorderLayout.CENTER);

        // Show Chart Button
        showChartButton = new JButton("Show Registration Chart");
        showChartButton.addActionListener(e -> {
            AmountUserOnlineChart amountUserOnlineChart = new AmountUserOnlineChart();
            amountUserOnlineChart.getBackButton().addActionListener(event -> switchPanel(this));
            switchPanel(amountUserOnlineChart);
        }); // Open chart view on button click
        
        // Back button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backButton = new JButton("BACK");

        buttonPanel.add(backButton);
        buttonPanel.add(showChartButton);

        add(buttonPanel, BorderLayout.SOUTH);

        sortComboBox.addActionListener(e -> applyFilters());
        timeFilterComboBox.addActionListener(e -> applyFilters());
        activityFilterComboBox.addActionListener(e -> applyFilters());
        activityCountField.addActionListener(e -> applyFilters());
        usernameFilterField.addActionListener(e -> applyFilters());

        loadDataFromDatabase();

    }

    private void loadDataFromDatabase() {
        // Base query
        String query = """
            SELECT 
                u.UserID,
                u.Username,
                COUNT(DISTINCT ua.ActivityID) AS SessionsCount,
                COUNT(DISTINCT m.ReceiverID) AS UniqueUsersMessaged,
                COUNT(DISTINCT gm.GroupID) AS UniqueGroupsMessaged,
                u.CreatedAt
            FROM 
                users u
            LEFT JOIN user_activities ua 
                ON u.UserID = ua.UserID
            LEFT JOIN messages m 
                ON u.UserID = m.SenderID
            LEFT JOIN group_messages gm 
                ON u.UserID = gm.SenderID
            GROUP BY 
                u.UserID, u.Username
            ORDER BY 
                u.UserID;
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
                int sessionsCount = resultSet.getInt("SessionsCount");
                int uniqueUsersMessaged = resultSet.getInt("UniqueUsersMessaged");
                int uniqueGroupsMessaged = resultSet.getInt("UniqueGroupsMessaged");
                java.util.Date createdAt = resultSet.getDate("CreatedAt");

                tableModel.addRow(new Object[]{userID, username, sessionsCount, uniqueUsersMessaged, uniqueGroupsMessaged, createdAt});
            }
    
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading data from database: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadDataFromDatabase(String sortBy, String dateRange, String comparison, String compareTo, String username) {
        // Base query
        StringBuilder query = new StringBuilder("""
            SELECT 
                u.UserID, 
                u.UserName, 
                COUNT(DISTINCT DATE(ua.LoginTime)) AS SessionsCount, 
                COUNT(DISTINCT m.ReceiverID) AS UniqueUsersMessaged,
                COUNT(DISTINCT gm.GroupID) AS UniqueGroupsMessaged,
                DATE(u.CreatedAt) AS CreatedAt
            FROM 
                Users u
            LEFT JOIN 
                user_activities ua ON u.UserID = ua.UserID
            LEFT JOIN 
                Messages m ON u.UserID = m.SenderID
            LEFT JOIN 
                group_messages gm ON u.UserID = gm.SenderID
            WHERE 1=1
        """);
    
        // Dynamically adding WHERE clauses based on parameters
        if (username != null && !username.trim().isEmpty()) {
            query.append(" AND u.UserName LIKE ?");
        }
    
        // Filter by date range
        switch (dateRange) {
            case "Today":
                query.append(" AND ua.LoginTime >= CURDATE()");
                break;
            case "Last 7 Days":
                query.append(" AND ua.LoginTime >= DATE_SUB(CURDATE(), INTERVAL 7 DAY)");
                break;
            case "Last 30 Days":
                query.append(" AND ua.LoginTime >= DATE_SUB(CURDATE(), INTERVAL 30 DAY)");
                break;
            case "All":
            default:
                break;
        }
    
        // Add GROUP BY clause
        query.append(" GROUP BY u.UserID, u.UserName");
    
        // Add HAVING clause for session count comparison
        if (comparison != null && !comparison.trim().isEmpty() && compareTo != null && !compareTo.trim().isEmpty()) {
            query.append(" HAVING SessionsCount ");
            switch (comparison) {
                case "Equal to":
                    query.append("= ?");
                    break;
                case "Greater than":
                    query.append("> ?");
                    break;
                case "Less than":
                    query.append("< ?");
                    break;
                default:
                    throw new IllegalArgumentException("Invalid comparison operator: " + comparison);
            }
        }
    
        // Add ORDER BY clause
        if (sortBy != null && !sortBy.trim().isEmpty()) {
            switch (sortBy) {
                case "Date Creation":
                    query.append(" ORDER BY CreatedAt DESC");
                    break;
                case "Username":
                    query.append(" ORDER BY u.UserName ASC");
                    break;
                default:
                    throw new IllegalArgumentException("Invalid sortBy value: " + sortBy);
            }
        }
    
        // Execute query
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query.toString())) {
    
            int paramIndex = 1;
    
            // Set username parameter if provided
            if (username != null && !username.trim().isEmpty()) {
                preparedStatement.setString(paramIndex++, "%" + username.trim() + "%");
            }
    
            // Set session count comparison parameter
            if (comparison != null && !comparison.trim().isEmpty() && compareTo != null && !compareTo.trim().isEmpty()) {
                preparedStatement.setInt(paramIndex++, Integer.parseInt(compareTo.trim()));
            }
    
            ResultSet resultSet = preparedStatement.executeQuery();
    
            // Clear the table before adding new data
            tableModel.setRowCount(0);
    
            // Populate table with data from ResultSet
            while (resultSet.next()) {
                int userID = resultSet.getInt("UserID");
                String userName = resultSet.getString("UserName");
                int sessionsCount = resultSet.getInt("SessionsCount");
                int uniqueUsersMessaged = resultSet.getInt("UniqueUsersMessaged");
                int uniqueGroupsMessaged = resultSet.getInt("UniqueGroupsMessaged");
                java.util.Date createdAt = resultSet.getDate("CreatedAt");
    
                tableModel.addRow(new Object[]{userID, userName, sessionsCount, uniqueUsersMessaged, uniqueGroupsMessaged, createdAt});
            }
    
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading data from database: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void applyFilters() {
        String username = usernameFilterField.getText();
        String sortBy = (String) sortComboBox.getSelectedItem();
        String dateRange = (String) timeFilterComboBox.getSelectedItem();
        String comparison = (String) activityFilterComboBox.getSelectedItem();
        String compareTo = activityCountField.getText();
    
        // Validate activity count input
        if (compareTo != null && !compareTo.trim().isEmpty() && !isValidNonNegativeInteger(compareTo)) {
            JOptionPane.showMessageDialog(this, "Please enter a valid non-negative integer for activity count.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
            return;
        }
    
        // Load data from database with filters
        loadDataFromDatabase(sortBy, dateRange, comparison, compareTo, username);
    }
    
    private boolean isValidNonNegativeInteger(String input) {
        if (input != null && !input.isEmpty()) {
            try {
                int value = Integer.parseInt(input);
                
                // Check if the number is non-negative
                if (value < 0) {
                    JOptionPane.showMessageDialog(this, "Activity count must be a non-negative integer.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                    return false; // Return false if input is invalid
                }
            } catch (NumberFormatException e) {
                // Handle the case when the input is not a valid integer
                JOptionPane.showMessageDialog(this, "Please enter a valid non-negative integer for activity count.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                return false; // Return false if input is invalid
            }
        }
        return true; // Return true if input is valid
    }

    //Switch panel
    private void switchPanel(JPanel newPanel) {
        parentFrame.getContentPane().removeAll();
        parentFrame.add(newPanel);
        parentFrame.revalidate();
        parentFrame.repaint();
    }
    
    public JButton getBackButton() {
        return backButton;
    }
}
