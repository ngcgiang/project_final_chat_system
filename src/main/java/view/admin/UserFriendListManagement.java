package view.admin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Window;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import components.admin.user_friends.UserFriendBUS;
import components.admin.user_friends.UserFriendDTO;

public class UserFriendListManagement extends JPanel {
    private JTable friendListTable;
    private DefaultTableModel tableModel;
    private JTextField nameFilterField;
    private JTextField amountOfFriendsFilterField;
    private JComboBox<String> amountFilterComboBox;
    private JComboBox<String> sortComboBox;
    private JButton applyFilterButton;
    private JButton userFriendListButton;
    private JButton backButton;
    private UserFriendBUS userFriendBUS;

    public UserFriendListManagement() {

        userFriendBUS = new UserFriendBUS();
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
        sortComboBox = new JComboBox<>(new String[]{"UserID", "FullName"});
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
        nameFilterField = new JTextField(10);
        filterSortPanel.add(new JLabel("Filter by Full-name:"));
        filterSortPanel.add(nameFilterField);

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
        tableModel = new DefaultTableModel(new Object[]{"User ID", "Full-name", "Amount of friends"}, 0);
        friendListTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(friendListTable);
        add(scrollPane, BorderLayout.CENTER);

        // Back button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backButton = new JButton("BACK");
        userFriendListButton = new JButton("User Friend List");

        buttonPanel.add(backButton);
        buttonPanel.add(userFriendListButton);

        add(buttonPanel, BorderLayout.SOUTH);

        sortComboBox.addActionListener(e -> applyFilters());
        amountFilterComboBox.addActionListener(e -> applyFilters());
        amountOfFriendsFilterField.addActionListener(e -> applyFilters());
        nameFilterField.addActionListener(e -> applyFilters());
        applyFilterButton.addActionListener(e -> applyFilters());

        // Add ActionListener for userFriendListButton
        userFriendListButton.addActionListener(e->{
            int selectedRow = friendListTable.getSelectedRow();

            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a user to view their friends list.", "No User Selected", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int userId = (int) friendListTable.getValueAt(selectedRow, 0);

            JFrame userFriendListFrame = new JFrame("Friend List Of UserID: " + userId);
            UserFriendListManagement userFriendListManagement = new UserFriendListManagement(userId);
            
            userFriendListFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            userFriendListFrame.setSize(800, 600);
            userFriendListFrame.add(userFriendListManagement);
            userFriendListFrame.setLocationRelativeTo(null);
            
            userFriendListFrame.setVisible(true);
    
        });
        // Load data from the database
        loadDataFromDatabase();

    }

    private void loadDataFromDatabase() {
        try {
            // Fetch data through the business logic layer
            List<UserFriendDTO> userList = userFriendBUS.getAllUserFriends();
            
            // Check if the list is not null and has elements
            if (userList == null || userList.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No user data available.", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
    
            // Clear existing table data
            tableModel.setRowCount(0);
    
            // Populate table with data from the userList
            for (UserFriendDTO user : userList) {
                tableModel.addRow(new Object[]{
                    user.getUserId(),
                    user.getFullName(),
                    user.getFriendCount()
                });
            }
        } catch (SQLException ex) {
            // Log error details
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading data: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    

    public UserFriendListManagement(int UserID) {

        userFriendBUS = new UserFriendBUS();
        // Main window setup
        setLayout(new BorderLayout());

        // Header panel for the title
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(Color.LIGHT_GRAY);
        headerPanel.setPreferredSize(new Dimension(800, 40));
        headerPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JLabel titleLabel = new JLabel("User Friends List Of UserID: " + UserID);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        headerPanel.add(titleLabel);

        add(headerPanel, BorderLayout.NORTH);

        // Table for displaying user friend list
        tableModel = new DefaultTableModel(new Object[]{"User ID", "Full-name", "Date of Creation"}, 0);
        friendListTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(friendListTable);
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
        try {
            // Fetch data through the business logic layer
            List<UserFriendDTO> userList = userFriendBUS.getUserFriendsByUserId(userId);
            
            // Check if the list is not null and has elements
            if (userList == null || userList.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No user data available.", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
    
            // Clear existing table data
            tableModel.setRowCount(0);
    
            // Populate table with data from the userList
            for (UserFriendDTO user : userList) {
                tableModel.addRow(new Object[]{
                    user.getUserId(),
                    user.getFullName(),
                    user.getDateOfCreation()
                });
            }
        } catch (SQLException ex) {
            // Log error details
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading data: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void loadDataFromDatabase(String sortBy, String comparison, String compareTo, String fullName) {
        try {
            // Fetch data through the business logic layer
            List<UserFriendDTO> userList = userFriendBUS.getFilteredUserFriends(sortBy, comparison, compareTo, fullName);
            
            // Check if the list is not null and has elements
            if (userList == null || userList.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No user data available.", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
    
            // Clear existing table data
            tableModel.setRowCount(0);
    
           // Populate table with data from the userList
           for (UserFriendDTO user : userList) {
            tableModel.addRow(new Object[]{
                user.getUserId(),
                user.getFullName(),
                user.getFriendCount()
            });
        }
        } catch (SQLException ex) {
            // Log error details
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading data: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void applyFilters() {
        String sortBy = sortComboBox.getSelectedItem().toString();
        String comparison = amountFilterComboBox.getSelectedItem().toString();
        String compareTo = amountOfFriendsFilterField.getText();
        String username = nameFilterField.getText();
        if (!isValidNonNegativeInteger(compareTo)) {
            return;
        }
        loadDataFromDatabase(sortBy, comparison, compareTo, username);
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

    public JButton getBackButton() {
        return backButton;
    }
}
