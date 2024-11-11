import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class NewUserManagement extends JFrame {
    private JTable userTable;
    private DefaultTableModel tableModel;
    private JTextField nameFilterField;
    private JComboBox<String> dateRangeComboBox;
    private JComboBox<String> sortComboBox;
    private JButton applyFilterButton;
    private JButton showChartButton;

    public NewUserManagement() {
        // Main window setup
        setTitle("New User Management - Chat App");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
        
        // Date Range Selection
        dateRangeComboBox = new JComboBox<>(new String[]{"All", "Today", "Last 7 Days", "Last 30 Days"});
        filterSortPanel.add(new JLabel("Select Date Range:"));
        filterSortPanel.add(dateRangeComboBox);

        // Sorting Options
        sortComboBox = new JComboBox<>(new String[]{"Sort by Name", "Sort by Registration Time"});
        filterSortPanel.add(new JLabel("Sort by:"));
        filterSortPanel.add(sortComboBox);

        // Filter by Username
        nameFilterField = new JTextField(15);
        filterSortPanel.add(new JLabel("Filter by Name:"));
        filterSortPanel.add(nameFilterField);

        // Apply Filter Button
        applyFilterButton = new JButton("Apply Filter");
        filterSortPanel.add(applyFilterButton);

        // Show Chart Button
        showChartButton = new JButton("Show Registration Chart");
        showChartButton.addActionListener(e -> openChartView()); // Open chart view on button click
        add(showChartButton, BorderLayout.SOUTH);

        // Add header and filter/sort panel to the north panel
        northPanel.add(headerPanel, BorderLayout.NORTH);
        northPanel.add(filterSortPanel, BorderLayout.SOUTH);

        // Add the combined northPanel to the frame's BorderLayout.NORTH
        add(northPanel, BorderLayout.NORTH);

        // Table for displaying new user registrations
        tableModel = new DefaultTableModel(new Object[]{"User ID", "Username", "Registration Time"}, 0);
        userTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(userTable);
        add(scrollPane, BorderLayout.CENTER);

        // Adding sample data to the table (optional)
        tableModel.addRow(new Object[]{"US01", "user123", "2024-11-08 10:30"});
        tableModel.addRow(new Object[]{"US02", "user456", "2024-11-07 14:15"});

        setVisible(true);
    }

    // Method to open the chart view
    private void openChartView() {
        new NewRegistrationChartView();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new NewUserManagement());
    }
}
