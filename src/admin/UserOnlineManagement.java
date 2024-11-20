import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class UserOnlineManagement extends JPanel {
    private JTable reportTable;
    private DefaultTableModel tableModel;
    private JTextField usernameFilterField;
    private JTextField activityCountField;
    private JComboBox<String> activityFilterComboBox;
    private JComboBox<String> amountFilterComboBox;
    private JComboBox<String> sortComboBox;
    private JButton applyFilterButton;
    private JButton backButton;

    public UserOnlineManagement() {
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
        amountFilterComboBox = new JComboBox<>(new String[]{"All", "Today", "Last 7 Days", "Last 30 Days"});
        filterSortPanel.add(new JLabel("Date range:"));
        filterSortPanel.add(amountFilterComboBox);

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

        // Adding sample data to the table (optional)
        tableModel.addRow(new Object[]{"US01", "user123", "12", "8", "4", "01/01/2024"});
        tableModel.addRow(new Object[]{"US02", "user456", "23", "16", "2", "04/01/2024"});

        // Back button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backButton = new JButton("BACK");

        buttonPanel.add(backButton);

        add(buttonPanel, BorderLayout.SOUTH);

    }

    public JButton getBackButton() {
        return backButton;
    }
}
