import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class UserFriendListManagement extends JPanel {
    private JTable reportTable;
    private DefaultTableModel tableModel;
    private JTextField usernameFilterField;
    private JTextField amountOfFriendsFilterField;
    private JComboBox<String> amountFilterComboBox;
    private JComboBox<String> sortComboBox;
    private JButton applyFilterButton;
    private JButton backButton;

    public UserFriendListManagement() {
        // Main window setup
        setLayout(new BorderLayout());

        // Header panel for the title
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(Color.LIGHT_GRAY);
        headerPanel.setPreferredSize(new Dimension(800, 40));

        // Set FlowLayout to left-align the title
        headerPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JLabel titleLabel = new JLabel("User Friends List Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        headerPanel.add(titleLabel);

        // Filter and Sort panel
        JPanel filterSortPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        // Sorting Options
        sortComboBox = new JComboBox<>(new String[]{"Username"});
        filterSortPanel.add(new JLabel("Sort by:"));
        filterSortPanel.add(sortComboBox);

        // Filter by amount of friends
        amountFilterComboBox = new JComboBox<>(new String[]{"Greater than", "Equal to", "Less than"});
        filterSortPanel.add(new JLabel("Amount of friends: "));
        filterSortPanel.add(amountFilterComboBox);
        
        // Box input for amount of friends
        amountOfFriendsFilterField = new JTextField(4);
        filterSortPanel.add(amountOfFriendsFilterField);

        // Filter by Username
        usernameFilterField = new JTextField(10);
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

        // Table for displaying user friend list
        tableModel = new DefaultTableModel(new Object[]{"User ID", "Username", "Amount of friends"}, 0);
        reportTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(reportTable);
        add(scrollPane, BorderLayout.CENTER);

        // Back button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backButton = new JButton("BACK");

        buttonPanel.add(backButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // Load data from the database
        loadDataFromDatabase();

    }

    private void loadDataFromDatabase() {
        // Database query to fetch data
        String query = """
                SELECT u.UserID, u.Username, COUNT(f.FriendUserID) AS FriendCount
                FROM Friends f
                INNER JOIN Users u ON f.UserID = u.UserID
                GROUP BY u.UserID, u.Username
                ORDER BY u.UserID
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            // Clear the table before adding new data
            tableModel.setRowCount(0);

            // Populate table with data from ResultSet
            while (resultSet.next()) {
                int userId = resultSet.getInt("UserID");
                String username = resultSet.getString("Username");
                int friendCount = resultSet.getInt("FriendCount");

                tableModel.addRow(new Object[]{userId, username, friendCount});
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading data from database: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public UserFriendListManagement(int UserID) {
        // Main window setup
        setLayout(new BorderLayout());

        // Header panel for the title
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(Color.LIGHT_GRAY);
        headerPanel.setPreferredSize(new Dimension(800, 40));
        headerPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JLabel titleLabel = new JLabel("User Friends List Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        headerPanel.add(titleLabel);

        add(headerPanel, BorderLayout.NORTH);

        // Table for displaying user friend list
        tableModel = new DefaultTableModel(new Object[]{"User ID", "Username", "Date of Creation"}, 0);
        reportTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(reportTable);
        add(scrollPane, BorderLayout.CENTER);

        // Back button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backButton = new JButton("BACK");
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Add ActionListener for Back button
        backButton.addActionListener(e -> {
            Window parentWindow = SwingUtilities.getWindowAncestor(this);
            if (parentWindow instanceof JFrame) {
                ((JFrame) parentWindow).dispose(); // Close the JFrame
            }
        });

        // Load data from the database
        loadDataFromDatabase(UserID);
    }

    private void loadDataFromDatabase(int userId) {
        // Query to fetch friends for a specific user
        String query = """
                SELECT u.UserID, u.Username, MIN(f.CreatedAt) AS DateOfCreation
                FROM Friends f
                INNER JOIN Users u ON f.FriendUserID = u.UserID
                WHERE f.UserID = ?
                GROUP BY u.UserID, u.Username
                ORDER BY u.UserID
                """;
    
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
    
            // Set the userId parameter
            statement.setInt(1, userId);
    
            try (ResultSet resultSet = statement.executeQuery()) {
                // Clear the table before adding new data
                tableModel.setRowCount(0);
    
                // Populate table with data from ResultSet
                while (resultSet.next()) {
                    int friendUserId = resultSet.getInt("UserID");
                    String friendUsername = resultSet.getString("Username");
                    String dateOfCreation = resultSet.getString("DateOfCreation");
    
                    tableModel.addRow(new Object[]{friendUserId, friendUsername, dateOfCreation});
                }
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
