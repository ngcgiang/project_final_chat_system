package view.admin;

import components.admin.user_activity.*;

import java.awt.*;
import javax.swing.*;
import java.util.List;
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
    private UserActivityBUS userActivityBUS;

    public UserOnlineManagement(JFrame parentFrame) {
        userActivityBUS = new UserActivityBUS();
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

        applyFilterButton.addActionListener(e -> applyFilters());

        String usernameDef = null;
        String sortByDef = "Date Creation";
        String dateRangeDef = "All";
        String comparisonDef = "Equal to";
        String compareToDef = null;
        loadDataFromDatabase(sortByDef, dateRangeDef, comparisonDef, compareToDef, usernameDef);

    }

    private void loadDataFromDatabase(String sortBy, String dateRange, String comparison, String compareTo, String username) {
        List<UserActivityDTO> activities = userActivityBUS.getUserActivities(sortBy, dateRange, comparison, compareTo, username);
        tableModel.setRowCount(0);
        for (UserActivityDTO activity : activities) {
            tableModel.addRow(new Object[]{
                activity.getUserID(),
                activity.getUserName(),
                activity.getSessionsCount(),
                activity.getUniqueUsersMessaged(),
                activity.getUniqueGroupsMessaged(),
                activity.getCreatedAt()
            });
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