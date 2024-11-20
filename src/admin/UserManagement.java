import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

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

        // Set FlowLayout to left-align the title
        headerPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JLabel titleLabel = new JLabel("User Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        headerPanel.add(titleLabel);

        // Main Table Panel for User Information
        String[] columnNames = {"User ID", "Username", "Full-name", "Address", "DOB", "Gender", "Email", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0);
        userTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(userTable);
        add(scrollPane, BorderLayout.CENTER);

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

        JButton applyFilterButton = new JButton("Apply Filter");
        filterPanel.add(applyFilterButton);
        
        JPanel northPanel = new JPanel();
        northPanel.setLayout(new BorderLayout());
        northPanel.add(headerPanel, BorderLayout.NORTH);
        northPanel.add(filterPanel, BorderLayout.CENTER);

        add(northPanel, BorderLayout.NORTH);  // Add northPanel to the main frame

        // Button Panel for User Actions
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backButton = new JButton("Back");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Remove");
        JButton lockButton = new JButton("Lock/Unlock");
        JButton resetPasswordButton = new JButton("Update password");
        JButton viewLoginHistoryButton = new JButton("Login history");
        JButton viewFriendsButton = new JButton("Friends list");

        // Adding sample data to the table
        tableModel.addRow(new Object[]{"US01", "user123", "Ngoc Giang", "123 Minh Phung", "01/01/2004", "Male", "user123@example.com", "Active"});
        tableModel.addRow(new Object[]{"US02", "user456", "Huy Tan", "456 Nguyen Trai", "02/02/2004", "Male", "user456@example.com", "Inactive"});
        
        buttonPanel.add(backButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(lockButton);
        buttonPanel.add(resetPasswordButton);
        buttonPanel.add(viewLoginHistoryButton);
        buttonPanel.add(viewFriendsButton);

        add(buttonPanel, BorderLayout.SOUTH);

    }
    public JButton getBackButton() {
        return backButton;
    }
}
