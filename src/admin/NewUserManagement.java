import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class NewUserManagement extends JPanel {
    private JTable userTable;
    private DefaultTableModel tableModel;
    private JTextField nameFilterField;
    private JComboBox<String> dateRangeComboBox;
    private JComboBox<String> sortComboBox;
    private JButton applyFilterButton;
    private JButton showChartButton;
    private JButton backButton;
    private JFrame parentFrame;

    public NewUserManagement(JFrame parentFrame) {
        this.parentFrame = parentFrame; // Lưu tham chiếu đến JFrame cha
        initComponents();
    }
    
    private void initComponents() {
        // Main window setup
        setLayout(new BorderLayout());

        // Main header and filter/sort panel to hold all north components
        JPanel northPanel = new JPanel();
        northPanel.setLayout(new BorderLayout());

        // Header panel for the title
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(Color.LIGHT_GRAY);
        JLabel titleLabel = new JLabel("New User Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        headerPanel.add(titleLabel);

        // Filter and Sort panel
        JPanel filterSortPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        // Sorting Options
        sortComboBox = new JComboBox<>(new String[]{"Registration time", "Username"});
        filterSortPanel.add(new JLabel("Sort by:"));
        filterSortPanel.add(sortComboBox);

        // Filter by Time
        dateRangeComboBox = new JComboBox<>(new String[]{"All", "Today", "Last 7 Days", "Last 30 Days"});
        filterSortPanel.add(new JLabel("Date range:"));
        filterSortPanel.add(dateRangeComboBox);

        // Filter by Username
        nameFilterField = new JTextField(15);
        filterSortPanel.add(new JLabel("Filter by Username:"));
        filterSortPanel.add(nameFilterField);

        // Apply Filter Button
        applyFilterButton = new JButton("Apply Filter");
        filterSortPanel.add(applyFilterButton);

        // Combine header and filter panels into a north container
        northPanel = new JPanel();
        northPanel.setLayout(new BorderLayout());
        northPanel.add(headerPanel, BorderLayout.NORTH);
        northPanel.add(filterSortPanel, BorderLayout.CENTER);

        add(northPanel, BorderLayout.NORTH);

        // Table for displaying new user registrations
        tableModel = new DefaultTableModel(new Object[]{"User ID", "Username", "Registration Time"}, 0);
        userTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(userTable);
        add(scrollPane, BorderLayout.CENTER);
        
        // Show Chart Button
        showChartButton = new JButton("Show Registration Chart");
        showChartButton.addActionListener(e -> {
            NewRegistrationChartView newRegistrationChartViewPanel = new NewRegistrationChartView();
            newRegistrationChartViewPanel.getBackButton().addActionListener(event -> switchPanel(this));
            switchPanel(newRegistrationChartViewPanel);
        }); // Open chart view on button click


        // Back button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backButton = new JButton("BACK");

        buttonPanel.add(backButton);
        buttonPanel.add(showChartButton);

        add(buttonPanel, BorderLayout.SOUTH);

        //Add actionListener sort
        sortComboBox.addActionListener(e ->{
            String username = nameFilterField.getText();
            String sortBy = sortComboBox.getSelectedItem().toString();
            String time = dateRangeComboBox.getSelectedItem().toString();

            loadDataFromDatabase(sortBy, time, username);
        });

        //Add actionListener apply time
        dateRangeComboBox.addActionListener(e -> {
            String username = nameFilterField.getText();
            String sortBy = sortComboBox.getSelectedItem().toString();
            String time = dateRangeComboBox.getSelectedItem().toString();

            loadDataFromDatabase(sortBy, time, username);
        });

        //Add actionListener search filter
        applyFilterButton.addActionListener(e -> {
            String username = nameFilterField.getText();
            String sortBy = sortComboBox.getSelectedItem().toString();
            String time = dateRangeComboBox.getSelectedItem().toString();

            loadDataFromDatabase(sortBy, time, username);
        });

        loadDataFromDatabase();
    }

    private void loadDataFromDatabase(String sortBy, String time, String username) {
        // Base query
        String query = """
                SELECT u.UserID, u.Username, u.CreatedAt
                FROM Users u
                WHERE 1=1
            """;
    
        // Append WHERE clause for username search
        if (username != null && !username.isEmpty()) {
            query += " AND u.Username LIKE ?";
        }
    
        // Append WHERE clause for time filter
        switch (time) {
            case "Today":
                query += " AND u.CreatedAt >= CURDATE()";
                break;
            case "Last 7 Days":
                query += " AND u.CreatedAt >= DATE_SUB(CURDATE(), INTERVAL 7 DAY)";
                break;
            case "Last 30 Days":
                query += " AND u.CreatedAt >= DATE_SUB(CURDATE(), INTERVAL 30 DAY)";
                break;
            case "All":
            default:
                // No additional condition for "All"
                break;
        }
    
        // Append ORDER BY clause for sorting
        if (sortBy != null && !sortBy.isEmpty()) {
            switch (sortBy) {
                case "Registration time":
                    query += " ORDER BY u.CreatedAt DESC";
                    break;
                case "Username":
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
                Timestamp createdAt = resultSet.getTimestamp("CreatedAt");

                tableModel.addRow(new Object[]{userID, userName, createdAt});
            }
    
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading data from database: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadDataFromDatabase() {
        // Base query
        String query = """
                SELECT u.UserID, u.Username, u.CreatedAt
                FROM Users u
                ORDER BY u.CreatedAt DESC;
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
                Timestamp createdAt = resultSet.getTimestamp("CreatedAt");

                tableModel.addRow(new Object[]{userID, username, createdAt});
            }
    
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading data from database: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
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
