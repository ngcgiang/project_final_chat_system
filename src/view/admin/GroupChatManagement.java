import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class GroupChatManagement extends JPanel {
    private JTable groupTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JComboBox<String> filterComboBox;
    private JButton searchButton;
    private JButton backButton;
    private JFrame parentFrame;

    public GroupChatManagement(JFrame parentFrame) {
        this.parentFrame = parentFrame; // Lưu tham chiếu đến JFrame cha
        initComponents();
    }
    
    private void initComponents() {
        // Setting main window properties
        setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.LIGHT_GRAY);
        headerPanel.setPreferredSize(new Dimension(800, 70)); // Adjusted to fit filter panel

        // Left Panel for Header
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setBackground(Color.LIGHT_GRAY);
        JLabel chatLabel = new JLabel("Group chat management");
        chatLabel.setFont(new Font("Arial", Font.BOLD, 16));
        leftPanel.add(chatLabel);
        headerPanel.add(leftPanel, BorderLayout.WEST);

        // Filter Panel for Search and Filter Options
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchField = new JTextField(15);
        searchButton = new JButton("Search");
        filterPanel.add(new JLabel("Search:"));
        filterPanel.add(searchField);
        filterPanel.add(searchButton);

        filterComboBox = new JComboBox<>(new String[]{"Group ID", "Group name", "Creation time"});
        filterPanel.add(new JLabel("Filter:"));
        filterPanel.add(filterComboBox);

        JButton applyButton = new JButton("Apply");
        filterPanel.add(applyButton);
        
        // Add filter panel within the header panel
        headerPanel.add(filterPanel, BorderLayout.SOUTH);
        add(headerPanel, BorderLayout.NORTH);

        // Table for Group Chat Information
        tableModel = new DefaultTableModel(new Object[]{"Group ID", "Name group", "Creation time", "Amount of members"}, 0);
        groupTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(groupTable);
        add(scrollPane, BorderLayout.CENTER);

        // Button Panel for User Actions
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        // Back button
        backButton = new JButton("BACK");
        JButton viewMember = new JButton("View member list");

        JButton viewAdmin = new JButton("View admin list");

        buttonPanel.add(backButton);
        buttonPanel.add(viewMember);
        buttonPanel.add(viewAdmin);

        add(buttonPanel, BorderLayout.SOUTH);

        // Add ActionListener for the search button
        searchButton.addActionListener(e -> handleButtonClick());

        // Add ActionListener for Order button
        applyButton.addActionListener(e -> handleButtonClick());

        // Add ActionListener for viewMember button
        viewMember.addActionListener(e -> {
            int selectedRow = groupTable.getSelectedRow();
        
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a group.", "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int groupID = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
            String groupName = tableModel.getValueAt(selectedRow, 1).toString();
        
            MemberListManagement memberListManagementPanel  = new MemberListManagement(groupID, groupName);
            memberListManagementPanel.getBackButton().addActionListener(event -> switchPanel(this));
            switchPanel(memberListManagementPanel);
        }); 
        
        // Open admin list view 
        viewAdmin.addActionListener(e -> {

            int selectedRow = groupTable.getSelectedRow();
        
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a group.", "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }
      
            int groupID = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
            String groupName = tableModel.getValueAt(selectedRow, 1).toString();

            AdminListManagement adminListManagementPanel = new AdminListManagement(groupID, groupName);
            adminListManagementPanel.getBackButton().addActionListener(event -> switchPanel(this));
            switchPanel(adminListManagementPanel);
        }); // Open admin list view 

        loadDataFromDatabase();

    }

    // Hàm chung để xử lý hành động search và sort
    private void handleButtonClick() {
        String searchValue = searchField.getText(); // Get search value from text field
        String orderBy;

        // Determine the column to filter based on ComboBox selection
        switch (filterComboBox.getSelectedItem().toString()) {
            case "Group ID":
                orderBy = "GroupID";
                break;
            case "Group name":
                orderBy = "GroupName";
                break;
            case "Creation time":
                orderBy = "CreatedAt";
                break;
            default:
                JOptionPane.showMessageDialog(this, "Invalid filter option.");
                return;
        }

        // Reload data with search and filter criteria
        loadDataFromDatabase(searchValue, orderBy);
    }

    private void loadDataFromDatabase(String searchValue, String orderBy) {
        // Base query
        String query = """
            SELECT g.GroupID, g.GroupName, g.CreatedAt, COUNT(gm.UserID) AS AmountOfMember
            FROM GroupInfo g
            INNER JOIN GroupMembers gm ON gm.GroupID = g.GroupID
            """;
    
        // Append WHERE clause if searchValue is provided
        if (searchValue != null && !searchValue.isEmpty()) {
            query += " WHERE g.GroupName LIKE ?";
        }
    
        // Add GROUP BY clause
        query += " GROUP BY g.GroupID, g.GroupName, g.CreatedAt";
    
        // Append ORDER BY clause if orderBy is provided
        if (orderBy != null && !orderBy.isEmpty()) {
            // Map filter options to database column names
            switch (orderBy) {
                case "GroupID":
                case "GroupName":
                case "CreatedAt":
                    query += " ORDER BY " + orderBy;
                    break;
                default:
                    throw new IllegalArgumentException("Invalid orderBy value: " + orderBy);
            }
        }
    
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
    
            // Set the search value if provided
            if (searchValue != null && !searchValue.isEmpty()) {
                preparedStatement.setString(1, "%" + searchValue + "%");
            }
    
            // Execute query and fetch results
            ResultSet resultSet = preparedStatement.executeQuery();
    
            // Clear the table before adding new data
            tableModel.setRowCount(0);
    
            // Populate table with data from ResultSet
            while (resultSet.next()) {
                int groupID = resultSet.getInt("GroupID");
                String groupName = resultSet.getString("GroupName");
                String dateOfCreation = resultSet.getString("CreatedAt");
                int amountOfMember = resultSet.getInt("AmountOfMember");
    
                tableModel.addRow(new Object[]{groupID, groupName, dateOfCreation, amountOfMember});
            }
    
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading data from database: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadDataFromDatabase() {
        // Base query
        String query = """
            SELECT g.GroupID, g.GroupName, g.CreatedAt, COUNT(gm.UserID) AS AmountOfMember
            FROM GroupInfo g
            INNER JOIN GroupMembers gm ON gm.GroupID = g.GroupID
            Group by g.GroupID, g.GroupName, g.CreatedAt
            Order by g.GroupID
            """;
    
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Execute query and fetch results
            ResultSet resultSet = preparedStatement.executeQuery();
    
            // Clear the table before adding new data
            tableModel.setRowCount(0);
    
            // Populate table with data from ResultSet
            while (resultSet.next()) {
                int groupID = resultSet.getInt("GroupID");
                String groupName = resultSet.getString("GroupName");
                String dateOfCreation = resultSet.getString("CreatedAt");
                int amountOfMember = resultSet.getInt("AmountOfMember");
    
                tableModel.addRow(new Object[]{groupID, groupName, dateOfCreation, amountOfMember});
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
